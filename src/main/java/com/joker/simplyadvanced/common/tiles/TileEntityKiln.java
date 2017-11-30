package com.joker.simplyadvanced.common.tiles;

import com.joker.simplyadvanced.common.blocks.BlockKiln;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Random;

public class TileEntityKiln extends TileEntity implements IInventory, ITickable {
    private NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    private String customName;
    private int cookTime, per3 = 0;
    private final int totalCookTime = 500;

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

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
        this.cookTime = compound.getInteger("CookTime");

        if (compound.hasKey("CustomName", 8)) this.setCustomName(compound.getString("CustomName"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
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
                return this.cookTime;
            case 1:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
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
        return this.cookTime > 0;
    }

    @Override
    public void update() {
        boolean mk = false;
        IBlockState state = getWorld().getBlockState(getPos());
        if (isBurning() && !state.getValue(BlockKiln.BURNING))
            world.setBlockState(pos, state.withProperty(BlockKiln.BURNING, true));
        if (!isBurning() && state.getValue(BlockKiln.BURNING))
            world.setBlockState(pos, state.withProperty(BlockKiln.BURNING, false));
        if (canHarden() && (!inventory.get(0).isEmpty()) && inventory.get(1).isEmpty()) {
            if (per3 >= 3) {
                per3 = 0;
                if (state.getValue(BlockKiln.OPENED) && state.getValue(BlockKiln.BURNING)) {
                    spawnParticles(EnumParticleTypes.FLAME, world, pos);
                }
            }
            per3++;
            this.cookTime++;
            if (this.cookTime == this.totalCookTime) {
                this.cookTime = 0;
                this.hardenItem();
                mk = true;
            }
        } else {
            per3 = 0;
            this.cookTime = 0;
        }

        if (mk) markDirty();
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

        for (int i = 0; i < 3; ++i) {
            double d1 = (double) ((float) (pos.getX() + 0.5) + randomFloat(random, -0.3F, 0.3F));
            double d2 = (double) ((float) (pos.getY()+0.5) + randomFloat(random, 0.2F, 0.4F));
            double d3 = (double) ((float) (pos.getZ() + 0.5) + randomFloat(random, -0.3F, 0.3F));

            worldIn.spawnParticle(particle, d1, d2, d3, 0.0D, 0.0D, 0.0D);
        }
    }

    public float randomFloat(Random random, float start, float end) {
        return start + random.nextFloat() * (end - start);
    }

    public void hardenItem() {
        if (this.canHarden()) {
            ItemStack input = this.inventory.get(0).copy();
            Item item = input.getItem().setMaxDamage(input.getMaxDamage() * 2);
            ItemStack result = item.getDefaultInstance();
            result.setTagInfo("ench", input.getEnchantmentTagList());
            result.setTagInfo("hardened", new NBTTagString("This item has been Hardened via the Kiln"));
            ItemStack output = this.inventory.get(1);

            if (output.isEmpty()) {
                inventory.set(0, ItemStack.EMPTY);
                inventory.set(1, result.copy());
            }
        }
    }

    private boolean canHarden() {
        if (this.inventory.get(0).isEmpty())
            return false;
        else {
            ItemStack result = inventory.get(0);

            if (result.isEmpty()){
                return false;
            } else {
                if (!canHarden(result)) return false;

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
        if ((nbt != null) && nbt.hasKey("hardened")) {
            return false;
        }

        Item item = stack.getItem();
        return (item.getClass().getSimpleName().contains("Tool")
                || item.getClass().getSimpleName().contains("Axe")
                || item.getClass().getSimpleName().contains("Spade")
                || item.getClass().getSimpleName().contains("Sword")
                || item.getClass().getSimpleName().contains("Pickaxe")
                || item.getClass().getSimpleName().contains("Hoe")
                || item.getClass().getSimpleName().contains("Shovel")
                || item.getClass().getSimpleName().contains("Bow"));
    }
}
