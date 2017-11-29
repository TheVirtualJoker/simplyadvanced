package com.joker.simplyadvanced.common.blocks;

import com.joker.simplyadvanced.common.init.ModBlocks;
import com.joker.simplyadvanced.common.utils.CreativeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockCopperOre extends Block {
    public BlockCopperOre() {
        super(Material.ROCK);
        setUnlocalizedName("copper_ore");
        setRegistryName("copper_ore");
        setCreativeTab(CreativeUtil.TAB);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.COPPER_ORE);
    }
}
