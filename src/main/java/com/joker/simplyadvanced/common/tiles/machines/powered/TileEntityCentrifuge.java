package com.joker.simplyadvanced.common.tiles.machines.powered;

import cofh.redstoneflux.api.IEnergyHandler;
import cofh.redstoneflux.api.IEnergyReceiver;
import cofh.redstoneflux.api.IEnergyStorage;
import com.joker.simplyadvanced.common.recipes.CentrifugeRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.LinkedList;

public class TileEntityCentrifuge extends TileEntity implements IInventory, ITickable, ISidedInventory, IEnergyReceiver {

    private NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);
    private String customName;

    private int maxEnergyStored = 25000;
    private int energyStored;
    private int spinTime;
    private int totalSpinTime;

    private static final int[] SLOTS_TOP = new int[]{4};
    private static final int[] SLOTS_BOTTOM = new int[]{0, 1, 2, 3};


    //-----------------------naming-------------------------
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "Alloy Furnace";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    //--------------------------Inventory------------------------------

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory)
            if (!stack.isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.inventory.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.inventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
            stack.setCount(this.getInventoryStackLimit());
        if (index == 0 && index + 1 == 1 && !flag) {
            ItemStack stack1 = this.inventory.get(index + 1);
            this.totalSpinTime = this.getSpinTime(stack);
            this.spinTime = 0;
            this.markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.inventory);
        this.energyStored = compound.getInteger("energyStored");
        this.spinTime = compound.getInteger("spinTime");
        this.totalSpinTime = compound.getInteger("totalSpinTime");

        if (compound.hasKey("CustomName", 8))
            this.setCustomName(compound.getString("CustomName"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("energyStored", (short) this.energyStored);
        compound.setInteger("spinTime", (short) this.spinTime);
        compound.setInteger("totalSpinTime", (short) this.totalSpinTime);
        ItemStackHelper.saveAllItems(compound, this.inventory);
        if (this.hasCustomName())
            compound.setString("customName", this.customName);

        return compound;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
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
        boolean flag = this.isSpining();
        boolean flag1 = false;

        if (this.isSpining())
            this.energyStored -= this.getEnergyPerTick(this.inventory.get(4));

        if (!this.world.isRemote) {
            if (!this.inventory.get(4).isEmpty() && this.energyStored > 0) {
                if(!this.isSpining() && this.canSpin()){
                    //Note Fix Class
                }
                if (this.isSpining() && this.canSpin()) {
                    this.spinTime++;

                    if (this.spinTime == this.totalSpinTime) {
                        this.spinTime = 0;
                        this.totalSpinTime = this.getSpinTime(this.inventory.get(4));
                        this.centrifugeItem();
                        flag1 = true;
                    }
                } else
                    this.spinTime = 0;
            } else if (!this.isSpining() && this.spinTime > 0)
                this.spinTime = MathHelper.clamp(this.spinTime - 2, 0, this.totalSpinTime);
        }
        if (flag1)
            this.markDirty();
    }

    public int getSpinTime(ItemStack input) {
        if (input == new ItemStack(Items.APPLE))
            return 200;
        return 0;
    }

    private boolean canSpin() {
        if (this.inventory.get(4).isEmpty())
            return false;
        else {
            LinkedList<ItemStack> result = CentrifugeRecipes.instance().getSpinResult(this.inventory.get(4));
            if (result.isEmpty()) {
                return false;
            } else {
                ItemStack output1 = this.inventory.get(0);
                ItemStack output2 = this.inventory.get(1);
                ItemStack output3 = this.inventory.get(2);
                ItemStack output4 = this.inventory.get(3);
                ItemStack result1 = result.get(0);
                ItemStack result2 = result.get(1);
                ItemStack result3 = result.get(2);
                ItemStack result4 = result.get(3);
                if (output1.isEmpty() && output2.isEmpty() && output3.isEmpty() && output4.isEmpty()) return true;
                if (!output1.isItemEqual(result1) || !output2.isItemEqual(result2) || !output3.isItemEqual(result3) || !output4.isItemEqual(result4))
                    return false;
                int res1 = output1.getCount() + result1.getCount();
                int res2 = output2.getCount() + result2.getCount();
                int res3 = output3.getCount() + result3.getCount();
                int res4 = output4.getCount() + result4.getCount();
                return res1 <= getInventoryStackLimit() && res2 <= getInventoryStackLimit() && res3 <= getInventoryStackLimit() && res4 <= getInventoryStackLimit() && res1 <= output1.getMaxStackSize() && res2 <= output2.getMaxStackSize() && res3 <= output3.getMaxStackSize() && res4 <= output4.getMaxStackSize();
            }
        }
    }

    public void centrifugeItem() {
        if (this.canSpin()) {
            ItemStack input = this.inventory.get(4);
            ItemStack out1 = this.inventory.get(0);
            ItemStack out2 = this.inventory.get(1);
            ItemStack out3 = this.inventory.get(2);
            ItemStack out4 = this.inventory.get(3);
            LinkedList<ItemStack> result = CentrifugeRecipes.instance().getSpinResult(this.inventory.get(4));
            ItemStack res1 = result.get(0);
            ItemStack res2 = result.get(1);
            ItemStack res3 = result.get(2);
            ItemStack res4 = result.get(3);

            if (out1.isEmpty())
                this.inventory.set(0, res1.copy());
            else if (out1.getItem() == res1.getItem())
                out1.grow(res1.getCount());
            if (out2.isEmpty())
                this.inventory.set(1, res2.copy());
            else if (out2.getItem() == res2.getItem())
                out2.grow(res2.getCount());
            if (out3.isEmpty())
                this.inventory.set(2, res3.copy());
            else if (out3.getItem() == res3.getItem())
                out3.grow(res3.getCount());
            if (out4.isEmpty())
                this.inventory.set(3, res4.copy());
            else if (out4.getItem() == res4.getItem())
                out4.grow(res4.getCount());
            input.shrink(1);
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0 || index == 1 || index == 2 || index == 3)
            return false;
        else if (index == 4)
            return true;
        else
            return false;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return this.spinTime;
            case 1:
                return this.totalSpinTime;
            case 2:
                return this.energyStored;
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
            case 2:
                this.energyStored = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 3;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }


    //----------------------------power---------------------------


    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public int receiveEnergy(EnumFacing enumFacing, int i, boolean b) {
        return 100;
    }

    @Override
    public int getEnergyStored(EnumFacing enumFacing) {
        return this.energyStored;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing enumFacing) {
        return 25000;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing enumFacing) {
        return true;
    }
}
