package com.joker.simplyadvanced.common.tiles.machines.powered;

import cofh.redstoneflux.impl.EnergyStorage;
import com.joker.simplyadvanced.common.recipes.CentrifugeRecipes;
import com.joker.simplyadvanced.common.tiles.TileEntityMachine;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;

public class TileEntityCentrifuge extends TileEntityMachine implements ITickable, ISidedInventory {
    private int spinTime;
    private int totalSpinTime;

    private static final int[] SLOTS_TOP = new int[]{4};
    private static final int[] SLOTS_BOTTOM = new int[]{0, 1, 2, 3};

    public TileEntityCentrifuge() {
        super(5, "Centrifuge", new EnergyStorage(25000, 240));
    }

    @Override
    public void setSlot(int slot, ItemStack item) {
        if (slot == 4) {
            this.totalSpinTime = this.getSpinTime(item);
            this.spinTime = 0;
            this.markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.spinTime = compound.getInteger("spinTime");
        this.totalSpinTime = compound.getInteger("totalSpinTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("spinTime", (short) this.spinTime);
        compound.setInteger("totalSpinTime", (short) this.totalSpinTime);
        return compound;
    }

    public boolean isSpining() {
        return this.spinTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isSpining(IInventory inventory) {
        return inventory.getField(0) > 0;
    }

    public static int getEnergyPerTick(ItemStack itemStack) {
        if (itemStack.getItem() == Items.APPLE) {
            return 100;
        } else {
            return 0;
        }
    }

    @Override
    public void update() {
        boolean mk = false;
        this.totalSpinTime = this.getSpinTime(getStackInSlot(4));
        if (canSpin()) {
            if (getStorage().getEnergyStored() >= perTick()) {
                this.spinTime++;
                if (isSpining() && !getStackInSlot(4).isEmpty()) {
                    getStorage().extractEnergy(perTick(), false);
                }
            }

            if (this.spinTime >= this.totalSpinTime) {
                this.spinTime = 0;
                this.centrifugeItem();
                mk = true;
            }
        } else {
            this.spinTime = 0;
        }
        if (mk) markDirty();
    }

    public int getSpinTime(ItemStack input) {
        if (input.isItemEqual(new ItemStack(Items.APPLE)))
            return 1000;
        if (input.isItemEqual(new ItemStack(Items.MAGMA_CREAM)))
            return 200;
        return 0;
    }

    private int perTick () {
        return getEnergyPerTick(getStackInSlot(4));
    }

    private boolean canSpin() {
        if (getStackInSlot(4).isEmpty())
            return false;
        else {
            if (getStorage().getEnergyStored() < perTick()) return false;
            LinkedList<ItemStack> result = CentrifugeRecipes.instance().getSpinResult(getStackInSlot(4));
            if (result.isEmpty()) {
                return false;
            } else {
                ItemStack output1 = getStackInSlot(0);
                ItemStack output2 = getStackInSlot(1);
                ItemStack output3 = getStackInSlot(2);
                ItemStack output4 = getStackInSlot(3);
                ItemStack result1 = result.get(0);
                ItemStack result2 = result.get(1);
                ItemStack result3 = result.get(2);
                ItemStack result4 = result.get(3);
                if (output1.isEmpty()
                        && output2.isEmpty()
                        && output3.isEmpty()
                        && output4.isEmpty()) return true;
                if (!output1.isItemEqual(result1)
                        && !output2.isItemEqual(result2)
                        && !output3.isItemEqual(result3)
                        && !output4.isItemEqual(result4)) return false;
                int res1 = output1.getCount() + result1.getCount();
                int res2 = output2.getCount() + result2.getCount();
                int res3 = output3.getCount() + result3.getCount();
                int res4 = output4.getCount() + result4.getCount();
                return belowLimit(getInventoryStackLimit(), res1, res2, res3, res4);
            }
        }
    }

    private boolean belowLimit (int limit, int... array) {
        for (int count : array) {
            if (count >= limit) return false;
        }
        return true;
    }

    public void centrifugeItem() {
        if (this.canSpin()) {
            ItemStack input = getStackInSlot(4);
            ItemStack out1 = getStackInSlot(0);
            ItemStack out2 = getStackInSlot(1);
            ItemStack out3 = getStackInSlot(2);
            ItemStack out4 = getStackInSlot(3);
            LinkedList<ItemStack> result = CentrifugeRecipes.instance().getSpinResult(getStackInSlot(4));
            ItemStack res1 = result.get(0);
            ItemStack res2 = result.get(1);
            ItemStack res3 = result.get(2);
            ItemStack res4 = result.get(3);

            if (out1.isEmpty()){
                setInventorySlotContents(0, res1.copy());
            }else if (out1.getItem() == res1.getItem()) {
                out1.grow(res1.getCount());
            }
            if (out2.isEmpty()){
                setInventorySlotContents(1, res2.copy());
            }else if (out2.getItem() == res2.getItem()) {
                out2.grow(res2.getCount());
            }
            if (out3.isEmpty()){
                setInventorySlotContents(2, res3.copy());
            }else if (out3.getItem() == res3.getItem()) {
                out3.grow(res3.getCount());
            }
            if (out4.isEmpty()){
                setInventorySlotContents(3, res4.copy());
            }else if (out4.getItem() == res4.getItem()) {
                out4.grow(res4.getCount());
            }
            input.shrink(1);
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return (index == 4) && !CentrifugeRecipes.instance().getSpinResult(stack).isEmpty();
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return this.spinTime;
            case 1:
                return this.totalSpinTime;
            case 2:
                return getStorage().getEnergyStored();
            case 3:
                return getStorage().getMaxEnergyStored();
            case 4:
                return (getStorage().getEnergyStored() < perTick() ? 0: 1);
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.spinTime = value;
                break;
            case 1:
                this.totalSpinTime = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 5;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN) return SLOTS_BOTTOM;
        return SLOTS_TOP;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 4;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index != 4;
    }
}
