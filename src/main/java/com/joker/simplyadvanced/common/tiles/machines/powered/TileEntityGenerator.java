package com.joker.simplyadvanced.common.tiles.machines.powered;

import cofh.core.util.helpers.EnergyHelper;
import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.impl.EnergyStorage;
import com.joker.simplyadvanced.common.tiles.TileEntityMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityGenerator extends TileEntityMachine implements ITickable, IEnergyProvider {
    private boolean toggle = false;
    private final int maxStorage = 100000, defaultPerTick = 2;
    private int perTick = 0, storageTier = 1, speedTier = 1;

    public TileEntityGenerator () {
        super(2, "Generator", new EnergyStorage(200000, 0, 500));
    }

    @Override
    public void update() {
        if (toggle) getStorage().setEnergyStored(getStorage().getEnergyStored()+perTick);
        storageTier = 1;
        speedTier = 1;
        if (!getStackInSlot(0).isEmpty()) {
            storageTier += getStackInSlot(0).getCount();
        }
        if (!getStackInSlot(1).isEmpty()) {
            speedTier += getStackInSlot(1).getCount();
        }
        perTick = (defaultPerTick*speedTier);
        getStorage().setCapacity((maxStorage*storageTier));
        toggle=!toggle;

        transferEnergy();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("maxEnergy", getStorage().getMaxEnergyStored());
        compound.setInteger("storageTier", storageTier);
        compound.setInteger("speedTier", speedTier);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        getStorage().setCapacity(compound.getInteger("maxEnergy"));
        storageTier = compound.getInteger("storageTier");
        speedTier = compound.getInteger("speedTier");
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
    public void setSlot(int slot, ItemStack item) {}


    @Override
    public int receiveEnergy(EnumFacing enumFacing, int i, boolean b) {
        return 0;
    }

    // -------- methods from IEnergyProvider -----------
    @Override
    public int extractEnergy(EnumFacing enumFacing, int i, boolean b) {
        return getStorage().extractEnergy(i, b);
    }

    protected void transferEnergy() {
        for (EnumFacing face : EnumFacing.values()) {
            if (getStorage().getEnergyStored() > 0) {
                int amount = EnergyHelper.insertEnergyIntoAdjacentEnergyReceiver(this, face, Math.min(getStorage().getMaxExtract(), getStorage().getEnergyStored()), false);
                getStorage().setEnergyStored(getStorage().getEnergyStored() - amount);
            }
        }
    }
}
