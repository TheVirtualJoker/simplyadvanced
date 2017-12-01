package com.joker.simplyadvanced.common.tiles;

import com.joker.simplyadvanced.common.utils.SAEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class TileEntityGenerator extends ContainerTileEntity implements ITickable, IEnergyStorage {
    private SAEnergyStorage storage;
    private boolean toggle = false;
    private final int maxStorage = 200000, defaultPerTick = 2;
    private int perTick = 0, storageTier = 1, speedTier = 1;

    public TileEntityGenerator () {
        super(2, "Generator");
        storage = new SAEnergyStorage(maxStorage, 500, 500);
    }

    @Override
    public void update() {
        if (toggle) receiveEnergy(perTick, false);
        storageTier = 1;
        speedTier = 1;
        if (!getStackInSlot(0).isEmpty()) {
            storageTier += getStackInSlot(0).getCount();
        }
        if (!getStackInSlot(1).isEmpty()) {
            speedTier += getStackInSlot(1).getCount();
        }
        perTick = (defaultPerTick*speedTier);
        this.storage.setCapacity((maxStorage*storageTier));


        toggle=!toggle;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return (getEnergyStored() != 0);
    }

    @Override
    public boolean canReceive() {
        return (getEnergyStored() != getMaxEnergyStored());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("energy", storage.getEnergyStored());
        compound.setInteger("maxEnergy", storage.getMaxEnergyStored());
        compound.setInteger("storageTier", storageTier);
        compound.setInteger("speedTier", speedTier);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        storage.setEnergy(compound.getInteger("energy"));
        storage.setCapacity(compound.getInteger("maxEnergy"));
        storageTier = compound.getInteger("storageTier");
        speedTier = compound.getInteger("speedTier");
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY)
            return (T) this;
        return super.getCapability(capability, facing);
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return getEnergyStored();
            case 1:
                return perTick;
            case 2:
                return storageTier;
            case 3:
                return speedTier;
            case 4:
                return getMaxEnergyStored();
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                storage.receiveEnergy(value, false);
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
}
