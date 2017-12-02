package com.joker.simplyadvanced.common.tiles;

import cofh.api.tileentity.IEnergyInfo;
import cofh.redstoneflux.api.IEnergyReceiver;
import cofh.redstoneflux.impl.EnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

/**
 * This class handles all the tasks for RF (besides the extracking of RF)
 */
public abstract class TileEntityMachine extends ContainerTileEntity implements IEnergyReceiver, IEnergyInfo {
    private EnergyStorage storage;
    private int perTick = 0;

    public TileEntityMachine(int size, int maxStackSize, String defaultName, EnergyStorage storage) {
        super(size, maxStackSize, defaultName);
        this.storage = storage;
    }

    public TileEntityMachine(int size, String defaultName, EnergyStorage storage) {
        this(size, 64, defaultName, storage);
    }

    public EnergyStorage getStorage() {
        return storage;
    }

    @Override
    public int getInfoEnergyPerTick() {
        return perTick;
    }

    @Override
    public int getInfoMaxEnergyPerTick() {
        return storage.getMaxReceive();
    }

    @Override
    public int getInfoEnergyStored() {
        return storage.getEnergyStored();
    }

    @Override
    public int receiveEnergy(EnumFacing enumFacing, int i, boolean b) {
        perTick = storage.receiveEnergy(i, false);
        markDirty();
        IBlockState iblockstate = world.getBlockState(pos);
        final int FLAGS = 3;
        world.notifyBlockUpdate(pos, iblockstate, iblockstate, FLAGS);
        return perTick;
    }

    @Override
    public int getEnergyStored(EnumFacing enumFacing) {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing enumFacing) {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing enumFacing) {
        return true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        storage.writeToNBT(compound);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        storage.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return (capability == CapabilityEnergy.ENERGY) || (super.hasCapability(capability, facing));
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing from) {
        if (capability == CapabilityEnergy.ENERGY) {
            CapabilityEnergy.ENERGY.cast(new net.minecraftforge.energy.IEnergyStorage() {
                public int receiveEnergy(int maxReceive, boolean simulate) {
                    return TileEntityMachine.this.receiveEnergy(from, maxReceive, simulate);
                }

                public int extractEnergy(int maxExtract, boolean simulate) {
                    return 0;
                }

                public int getEnergyStored() {
                    return TileEntityMachine.this.getEnergyStored(from);
                }

                public int getMaxEnergyStored() {
                    return TileEntityMachine.this.getMaxEnergyStored(from);
                }

                public boolean canExtract() {
                    return false;
                }

                public boolean canReceive() {
                    return true;
                }
            });
        }
        return super.getCapability(capability, from);
    }
}
