package com.joker.simplyadvanced.common.tiles.machines.powered;

import cofh.core.util.helpers.BlockHelper;
import cofh.redstoneflux.api.IEnergyHandler;
import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import cofh.redstoneflux.api.IEnergyStorage;
import cofh.redstoneflux.impl.EnergyStorage;
import com.joker.simplyadvanced.common.tiles.TileEntityMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityGenerator extends TileEntityMachine implements ITickable, IEnergyProvider {
    private boolean toggle = false;
    private final int maxStorage = 100000, defaultPerTick = 2;
    private int perTick = 0, storageTier = 1, speedTier = 1, updateTask = 0;
    private boolean cached = false, sendEnergy = false;
    private IEnergyHandler[] adjacentEnergyHandlers = new IEnergyHandler[6];


    public TileEntityGenerator() {
        super(2, "Generator", new EnergyStorage(200000, 0, 500));
    }

    private void updateAdjacentHandler(BlockPos pos) {
        if (world.isRemote) return;
        int side = BlockHelper.determineAdjacentSide(this, pos);

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityGenerator) return;

        if (tile instanceof IEnergyStorage) {
            adjacentEnergyHandlers[side] = (IEnergyHandler) tile;
        }else if (tile instanceof IEnergyReceiver) {
            adjacentEnergyHandlers[side] = (IEnergyHandler) tile;
        }else {
            adjacentEnergyHandlers[side] = null;
        }
        cached = true;
    }

    @Override
    public void onNeighborChange(BlockPos pos, BlockPos neighbor) {
        updateAdjacentHandler(neighbor);
    }

    @Override
    public void update() {
        if (toggle) getStorage().setEnergyStored(getStorage().getEnergyStored() + perTick);
        storageTier = 1;
        speedTier = 1;
        if (!getStackInSlot(0).isEmpty()) {
            storageTier += getStackInSlot(0).getCount();
        }
        if (!getStackInSlot(1).isEmpty()) {
            speedTier += getStackInSlot(1).getCount();
        }
        perTick = (defaultPerTick * speedTier);
        if (!sendEnergy) getStorage().setCapacity((maxStorage * storageTier));
        if (updateTask == 0) {
            updateTask = 30;
            updateAdjacentHandler(pos.up());
            updateAdjacentHandler(pos.down());
            updateAdjacentHandler(pos.north());
            updateAdjacentHandler(pos.east());
            updateAdjacentHandler(pos.south());
            updateAdjacentHandler(pos.west());
        }

        if (cached) {
            Result result = canSendEnergy();
            sendEnergy = (result != null);
            if (result == null) return;
            IEnergyHandler handler = result.getHandler();

            if (handler instanceof IEnergyReceiver) {
                int remaining = ((IEnergyReceiver)handler).receiveEnergy(result.getSide(), getStorage().getMaxExtract(), false);
                extractEnergy(result.getSide(), remaining, false);
            }
        }

        toggle = !toggle;
        updateTask--;
    }

    private Result canSendEnergy () {
        if (getStorage().getEnergyStored() < getStorage().getMaxExtract()) return null;

        if (cached) {
            for (int side = 0; side < 6 && !sendEnergy; side++) {
                EnumFacing face = EnumFacing.VALUES[side];
                IEnergyHandler handler = adjacentEnergyHandlers[side];
                if (handler == null) continue;
                return new Result () {
                    @Override public EnumFacing getSide() {
                        return face;
                    }

                    @Override public IEnergyHandler getHandler() {
                        return handler;
                    }
                };
            }
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        getStorage().setEnergyStored(compound.getInteger("energy"));
        getStorage().setCapacity(compound.getInteger("maxEnergy"));
        storageTier = compound.getInteger("storageTier");
        speedTier = compound.getInteger("speedTier");
        super.readFromNBT(compound);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("energy", getStorage().getEnergyStored());
        compound.setInteger("maxEnergy", getStorage().getMaxEnergyStored());
        compound.setInteger("storageTier", storageTier);
        compound.setInteger("speedTier", speedTier);
        super.writeToNBT(compound);
        return compound;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return getStorage().getEnergyStored();
            case 1:
                return perTick;
            case 2:
                return storageTier;
            case 3:
                return speedTier;
            case 4:
                return getStorage().getMaxEnergyStored();
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                getStorage().receiveEnergy(value, false);
                break;
            case 1:
                perTick = value;
                break;
            case 2:
                storageTier = value;
                break;
            case 3:
                speedTier = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 5;
    }

    @Override
    public void setSlot(int slot, ItemStack item) {
    }


    @Override
    public int receiveEnergy(EnumFacing enumFacing, int i, boolean b) {
        return 0;
    }

    // -------- methods from IEnergyProvider -----------
    @Override
    public int extractEnergy(EnumFacing enumFacing, int i, boolean b) {
        int out = getStorage().extractEnergy(i, false);
        markDirty();
        IBlockState iblockstate = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
        return out;
    }

    private interface Result {
        EnumFacing getSide ();

        IEnergyHandler getHandler();
    }
}
