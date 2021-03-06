package com.joker.simplyadvanced.common.tiles.machines.powered;

import cofh.redstoneflux.impl.EnergyStorage;
import com.joker.simplyadvanced.client.utils.ParticleDisplay;
import com.joker.simplyadvanced.client.utils.Utils;
import com.joker.simplyadvanced.common.blocks.BlockCompressor;
import com.joker.simplyadvanced.common.recipes.CompressorRecipes;
import com.joker.simplyadvanced.common.tiles.TileEntityMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCompressor extends TileEntityMachine implements ITickable, ISidedInventory {
    private int compressTime = 0, totalCompressTime = 300, perUse = 2;

    public TileEntityCompressor() {
        super(2, "Compressor", new EnergyStorage(25000, 500));
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

    @Override
    public void update() {
        boolean mk = false;
        IBlockState state = getWorld().getBlockState(getPos());
        if (isCompressing() && (getField(5) >= 60)) {
            world.setBlockState(pos, state.withProperty(BlockCompressor.COMPRESSING, true));
            ParticleDisplay display = new ParticleDisplay(world, pos).setCount(4)
                    .setParticleType(EnumParticleTypes.SMOKE_NORMAL)
                    .setSpread(
                            Utils.randomFloat(-0.3F, 0.3F),
                            0.0F,
                            Utils.randomFloat(-0.3F, 0.3F)
                    ).add(0, -0.14, 0);
            display.spawnParticles(true);
        }else{
            world.setBlockState(pos, state.withProperty(BlockCompressor.COMPRESSING, false));
        }

        if (canCompress()) {
            totalCompressTime = CompressorRecipes.getInstance().getResult(getStackInSlot(0)).getTime();
            if (getStorage().getEnergyStored() >= perUse) {
                this.compressTime++;
                if (isCompressing() && !getStackInSlot(0).isEmpty()) {
                    getStorage().extractEnergy(perUse, false);
                }
            }
            if (this.compressTime == this.totalCompressTime) {
                this.compressTime = 0;
                this.compressItem();
                mk = true;
            }
        } else {
            this.compressTime = 0;
        }

        if (mk) markDirty();
    }

    public boolean isCompressing() {
        return (compressTime > 0);
    }

    public boolean canCompress(ItemStack stack) {
        return CompressorRecipes.getInstance().getResult(stack) != null;
    }

    public boolean canCompress() {
        ItemStack input = getStackInSlot(0);
        if (input.isEmpty() && isCompressing()) {
            compressTime = 0;
            return false;
        }
        CompressorRecipes.Result result = CompressorRecipes.getInstance().getResult(input);
        if (result == null) return false;
        ItemStack output = getStackInSlot(1);
        if (output.isEmpty()) return true;
        if (!output.isItemEqual(result.getOutput())) return false;
        int res = output.getCount() + result.getOutput().getCount();
        return res <= getInventoryStackLimit() && res <= output.getMaxStackSize();
    }

    public void compressItem() {
        if (canCompress()) {
            ItemStack input = this.getStackInSlot(0).copy();
            CompressorRecipes.Result result = CompressorRecipes.getInstance().getResult(input);
            ItemStack output = getStackInSlot(1);
            if (output.isEmpty())
                setInventorySlotContents(1, result.getOutput().copy());
            else if (output.getItem() == result.getOutput().getItem())
                output.grow(result.getOutput().getCount());
            getStackInSlot(0).shrink(1);
            this.compressTime = 0;
        }
    }

    @Override
    public void setSlot(int slot, ItemStack item) {
        if (slot == 0) {
            compressTime = 0;
            markDirty();
        }
    }

    @Override
    public void setField(int id, int value) {
        if (id == 0) compressTime = value;
    }

    @Override
    public int getField(int id) {
        if (id == 0) return compressTime;
        if (id == 1) return totalCompressTime;
        if (id == 2) return (getStorage().getEnergyStored() >= (perUse - 5)) ? 1 : 0;
        if (id == 3) return getStorage().getEnergyStored();
        if (id == 4) return getStorage().getMaxEnergyStored();
        if (id == 5) return Utils.percent(compressTime, totalCompressTime);
        return 0;
    }

    @Override
    public int getFieldCount() {
        return 6;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN) return new int[]{1};
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (index == 0) {
            ItemStack inputSlot = getStackInSlot(0);
            if (inputSlot.isEmpty()) return canCompress(itemStackIn);
            return true;
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return ((index == 1) && !stack.isEmpty());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("compressTime", compressTime);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        compressTime = compound.getInteger("compressTime");
    }
}
