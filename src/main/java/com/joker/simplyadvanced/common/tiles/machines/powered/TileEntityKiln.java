package com.joker.simplyadvanced.common.tiles.machines.powered;

import cofh.redstoneflux.impl.EnergyStorage;
import com.joker.simplyadvanced.client.utils.ParticleDisplay;
import com.joker.simplyadvanced.client.utils.Utils;
import com.joker.simplyadvanced.common.blocks.BlockKiln;
import com.joker.simplyadvanced.common.tiles.TileEntityMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class TileEntityKiln extends TileEntityMachine implements ITickable {
    private int hardenTime, per3 = 0;
    private final int totalHardenTime = 500, perUse = 200;

    public TileEntityKiln() {
        super(2, 1, "Kiln", new EnergyStorage(25000));
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

    @Override
    public void setSlot(int slot, ItemStack item) {
        if (slot == 0) {
            this.hardenTime = 0;
            this.markDirty();
        }
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return this.hardenTime;
            case 1:
                return this.totalHardenTime;
            case 2:
                return getStorage().getEnergyStored();
            case 3:
                return getStorage().getMaxEnergyStored();
            case 4:
                return (getStorage().getEnergyStored() >= (perUse - 5)) ? 1 : 0;
            case 5:
                return Utils.percent(hardenTime, totalHardenTime);
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.hardenTime = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 5;
    }

    public boolean isHardening() {
        return this.hardenTime > 0;
    }

    @Override
    public void update() {
        boolean mk = false;
        IBlockState state = getWorld().getBlockState(getPos());
        if (isHardening() && !state.getValue(BlockKiln.BURNING))
            world.setBlockState(pos, state.withProperty(BlockKiln.BURNING, true));
        if (!isHardening() && state.getValue(BlockKiln.BURNING))
            world.setBlockState(pos, state.withProperty(BlockKiln.BURNING, false));
        if (canHarden() && (!getStackInSlot(0).isEmpty()) && getStackInSlot(1).isEmpty()) {
            if (per3 >= 3) {
                per3 = 0;
                if (state.getValue(BlockKiln.BURNING)) {
                    if ((getStorage().getEnergyStored() >= (perUse - 5))) {
                        spawnParticles(EnumParticleTypes.FLAME,
                                Utils.randomFloat(-0.3F, 0.3F),
                                Utils.randomFloat(0.2F, 0.4F),
                                Utils.randomFloat(-0.3F, 0.3F));
                        spawnParticles(EnumParticleTypes.SMOKE_NORMAL,
                                Utils.randomFloat(-0.2F, 0.2F),
                                Utils.randomFloat(0.3F, 0.4F),
                                Utils.randomFloat(-0.2F, 0.2F));
                    } else {
                        spawnParticles(EnumParticleTypes.SNOW_SHOVEL,
                                Utils.randomFloat(-0.3F, 0.3F),
                                Utils.randomFloat(0.2F, 0.4F),
                                Utils.randomFloat(-0.3F, 0.3F));
                    }
                }
            }

            per3++;
            if (getStorage().getEnergyStored() >= perUse) {
                this.hardenTime++;
                getStorage().extractEnergy(perUse, false);
            }
            if (this.hardenTime == this.totalHardenTime) {
                this.hardenTime = 0;
                this.hardenItem();
                mk = true;
            }
        } else {
            per3 = 0;
            this.hardenTime = 0;
        }

        if (mk) markDirty();
    }

    private void spawnParticles(EnumParticleTypes particle,
                                float spreadX, float spreadY, float spreadZ) {
        IBlockState state = getWorld().getBlockState(getPos());
        if (!state.getValue(BlockKiln.OPENED)) return;
        ParticleDisplay display = new ParticleDisplay(world, pos);
        display.setCount(3).setParticleType(particle).setSpread(spreadX, spreadY, spreadZ);
        display.spawnParticles(true);
    }

    public void hardenItem() {
        if (this.canHarden()) {
            ItemStack input = this.getStackInSlot(0).copy();
            Item item = input.getItem().setMaxDamage(input.getMaxDamage() * 2);
            ItemStack result = item.getDefaultInstance();
            result.setTagInfo("ench", input.getEnchantmentTagList());
            result.setTagInfo("hardened", new NBTTagString("This item has been Hardened via the Kiln"));
            result = Utils.addLoreLine(result, TextFormatting.GRAY + "Hardened");
            ItemStack output = getStackInSlot(1);

            if (output.isEmpty()) {
                setInventorySlotContents(0, ItemStack.EMPTY);
                setInventorySlotContents(1, result.copy());
            }
        }
    }

    private boolean canHarden() {

        if (getStackInSlot(0).isEmpty())
            return false;
        else {
            ItemStack result = getStackInSlot(0);

            if (result.isEmpty()) {
                return false;
            } else {
                if (!canHarden(result)) return false;

                ItemStack output = getStackInSlot(1);
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

    @Override
    public boolean canConnectEnergy(EnumFacing enumFacing) {
        IBlockState state = getWorld().getBlockState(getPos());
        return enumFacing != state.getValue(BlockKiln.FACING).getOpposite();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.hardenTime = compound.getInteger("hardenTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("hardenTime", this.hardenTime);
        return compound;
    }
}
