package com.joker.simplyadvanced.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Random;

public class TileEntityKiln extends TileEntity implements IInventory, ITickable {
    private NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    private String customName;
    private int burnTime;
    private int currentBurnTime;
    private int cookTime;
    private boolean alter = false;
    private final int totalCookTime = 300;

    @Override
    public int getSizeInventory() {
        return inventory.size();
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
        this.inventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
            stack.setCount(this.getInventoryStackLimit());
        if (index == 0) {
            this.cookTime = 0;
            this.markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.inventory);
        this.burnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");

        if (compound.hasKey("CustomName", 8)) this.setCustomName(compound.getString("CustomName"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", this.burnTime);
        compound.setInteger("CookTime", this.cookTime);
        ItemStackHelper.saveAllItems(compound, this.inventory);

        if (this.hasCustomName()) compound.setString("CustomName", this.customName);

        return compound;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
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
        return index == 0;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return this.burnTime;
            case 1:
                return this.currentBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.burnTime = value;
                break;
            case 1:
                this.currentBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    public boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public void update() {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning()) {
            this.burnTime--;
            if (alter) spawnParticles(EnumParticleTypes.FLAME, world, pos);
        }

        if (!this.world.isRemote) {
            if (this.isBurning() && this.canSmelt()) {
                this.cookTime++;

                if (this.cookTime == this.totalCookTime) {
                    this.cookTime = 0;
                    this.smeltItem();
                    flag1 = true;
                }
            } else this.cookTime = 0;
            if (!this.isBurning() && this.cookTime > 0)
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);

            if (flag != this.isBurning()) {
                flag1 = true;
            }
        }
        if (flag1) this.markDirty();
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "Kiln";
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

    private void spawnParticles(EnumParticleTypes particle, World worldIn, BlockPos pos) {
        Random random = worldIn.rand;

        for (int i = 0; i < 6; ++i) {
            double d1 = (double) ((float) pos.getX() - random.nextFloat());
            double d2 = (double) ((float) pos.getY() - random.nextFloat());
            double d3 = (double) ((float) pos.getZ() - random.nextFloat());

            if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube()) d2 = (double) pos.getY() + 0.0625D + 1.0D;
            if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube()) d2 = (double) pos.getY() - 0.0625D;
            if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube()) d3 = (double) pos.getZ() + 0.0625D + 1.0D;
            if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube()) d3 = (double) pos.getZ() - 0.0625D;
            if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube()) d1 = (double) pos.getX() + 0.0625D + 1.0D;
            if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube()) d1 = (double) pos.getX() - 0.0625D;

            if (d1 < (double) pos.getX()
                    || d1 > (double) (pos.getX() + 1)
                    || d2 < 0.0D || d2 > (double) (pos.getY() + 1)
                    || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1))
                worldIn.spawnParticle(particle, d1, d2, d3, 0.0D, 0.0D, 0.0D);

        }
    }

    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack input = this.inventory.get(0);
            ItemStack copy = input.copy();
            NBTTagCompound itemNBT = copy.getTagCompound();
            Item item = copy.getItem().setMaxDamage(copy.getMaxDamage() * 2);
            NBTTagCompound nbt = new ItemStack(item).getTagCompound();
            nbt.merge(itemNBT);
            ItemStack result = new ItemStack(nbt);

            ItemStack output = this.inventory.get(1);

            if (output.isEmpty()) inventory.set(1, result.copy());
            input.shrink(1);
        }
    }

    private boolean canSmelt() {
        if (this.inventory.get(0).isEmpty() || this.inventory.get(1).isEmpty())
            return false;
        else {
            ItemStack result = inventory.get(0);

            if (result.isEmpty())
                return false;
            else {
                ItemStack output = this.inventory.get(1);
                if (output.isEmpty()) return true;
                if (!output.isItemEqual(result)) return false;
                int res = output.getCount() + result.getCount();
                return res <= getInventoryStackLimit() && res <= output.getMaxStackSize();
            }
        }
    }

    public boolean canHarden(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null) {
            NBTTagCompound disp = nbt.getCompoundTag("display");
            if (disp != null) {
                NBTTagList lore = disp.getTagList("Lore", 8);
                if (lore != null) {
                    for (NBTBase aLore : lore) {
                        NBTTagString string = (NBTTagString) aLore;
                        if (string.getString().equals("Hardened")) return false;
                    }
                }
            }
        }

        Item item = stack.getItem();
        return ((item instanceof ItemTool)
                || (item instanceof ItemAxe)
                || (item instanceof ItemSpade)
                || (item instanceof ItemSword)
                || (item instanceof ItemPickaxe)
                || (item instanceof ItemHoe)
                || (item instanceof ItemBow));
    }}
