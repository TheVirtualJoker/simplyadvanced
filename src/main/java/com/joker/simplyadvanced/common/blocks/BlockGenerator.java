package com.joker.simplyadvanced.common.blocks;

import com.joker.simplyadvanced.client.gui.SAGuiHandler;
import com.joker.simplyadvanced.common.lib.References;
import com.joker.simplyadvanced.common.tiles.TileEntityMachine;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityGenerator;
import com.joker.simplyadvanced.common.utils.SATab;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockGenerator extends Block implements ITileEntityProvider{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockGenerator() {
        super(Material.ROCK);
        setUnlocalizedName("generator");
        setRegistryName("generator");
        setCreativeTab(SATab.TAB);
        setResistance(3F);
        setHarvestLevel("pickaxe", 2);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing facing = placer.getHorizontalFacing();
        IBlockState newState = getBlockState().getBaseState().withProperty(FACING, facing);
        worldIn.setBlockState(pos, newState);
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);


        TileEntity entity = worldIn.getTileEntity(pos);
        if (entity == null) return;
        if (entity instanceof TileEntityMachine) {
            TileEntityMachine machine = (TileEntityMachine)entity;
            machine.onNeighborChange(pos, pos.up());
            machine.onNeighborChange(pos, pos.down());
            machine.onNeighborChange(pos, pos.north());
            machine.onNeighborChange(pos, pos.east());
            machine.onNeighborChange(pos, pos.south());
            machine.onNeighborChange(pos, pos.west());
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(References.MODID, SAGuiHandler.GENERATOR, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity entity = worldIn.getTileEntity(pos);
        if (entity instanceof TileEntityGenerator) {
            TileEntityGenerator generator = (TileEntityGenerator)entity;
            InventoryHelper.dropInventoryItems(worldIn, pos, generator);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);

        TileEntity entity = world.getTileEntity(pos);
        if (entity == null) return;
        if (entity instanceof TileEntityMachine) {
            TileEntityMachine machine = (TileEntityMachine)entity;
            machine.onNeighborChange(pos, neighbor);
        }
    }

    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
            enumfacing = EnumFacing.NORTH;

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityGenerator();
    }
}
