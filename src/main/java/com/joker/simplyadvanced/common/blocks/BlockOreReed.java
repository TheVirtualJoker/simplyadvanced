package com.joker.simplyadvanced.common.blocks;

import com.joker.simplyadvanced.common.init.ModBlocks;
import com.joker.simplyadvanced.common.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class BlockOreReed extends Block implements IPlantable{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    protected static final AxisAlignedBB OREED_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875, 1.0D, 0.875D);

    public BlockOreReed(){
        super(Material.PLANTS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
        this.setTickRandomly(true);
        this.setUnlocalizedName("ore_reed");
        this.setRegistryName("ore_reed");
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return OREED_AABB;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.ORE_REED || this.checkForDrop(worldIn, pos, state))
        {
            if (worldIn.isAirBlock(pos.up()))
            {
                int i;

                for (i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; ++i)
                {
                    ;
                }

                if (i < 3)
                {
                    int j = ((Integer)state.getValue(AGE)).intValue();

                    if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, true))
                    {
                        if (j == 15)
                        {
                            worldIn.setBlockState(pos.up(), this.getDefaultState());
                            worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(0)), 4);
                        }
                        else
                        {
                            worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(j + 1)), 4);
                        }
                        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
                    }
                }
            }
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos.down());
        Block block = state.getBlock();
        if (block.canSustainPlant(state, worldIn, pos.down(), EnumFacing.UP, this)) return true;

        if (block == this)
        {
            return true;
        }
        else if (block != Blocks.GRASS && block != Blocks.DIRT && block != Blocks.SAND)
        {
            return false;
        }
        else
        {
            BlockPos blockpos = pos.down();

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                IBlockState iblockstate = worldIn.getBlockState(blockpos.offset(enumfacing));

                if (iblockstate.getMaterial() == Material.WATER || iblockstate.getBlock() == Blocks.FROSTED_ICE)
                {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    protected final boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.canBlockStay(worldIn, pos))
        {
            return true;
        }
        else
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        this.checkForDrop(worldIn, pos, state);
    }

    public boolean canBlockStay(World worldIn, BlockPos pos)
    {
        return this.canPlaceBlockAt(worldIn, pos);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ModItems.ORE_REED);
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(AGE)).intValue();
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Beach;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return this.getDefaultState();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {AGE});
    }
}
