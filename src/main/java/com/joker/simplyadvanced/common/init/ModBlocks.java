package com.joker.simplyadvanced.common.init;

import com.joker.simplyadvanced.common.blocks.BlockAlloyFurnace;
import com.joker.simplyadvanced.common.blocks.BlockCopperOre;
import com.joker.simplyadvanced.common.blocks.BlockGenerator;
import com.joker.simplyadvanced.common.blocks.BlockKiln;
import com.joker.simplyadvanced.common.tiles.TileEntityAlloyFurnace;
import com.joker.simplyadvanced.common.tiles.TileEntityGenerator;
import com.joker.simplyadvanced.common.tiles.TileEntityKiln;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
    public static Block ALLOYSMELTER;
    public static Block COPPER_ORE;
    public static Block KILN;
    public static Block GENERATOR;

    public static void init() {
        ALLOYSMELTER = new BlockAlloyFurnace();
        COPPER_ORE = new BlockCopperOre();
        KILN = new BlockKiln();
        GENERATOR = new BlockGenerator();
    }

    public static void register() {
        registerBlock(ALLOYSMELTER);
        registerBlock(COPPER_ORE);
        registerBlock(KILN);
        registerBlock(GENERATOR);
        GameRegistry.registerTileEntity(TileEntityAlloyFurnace.class, "alloy_furnace");
        GameRegistry.registerTileEntity(TileEntityGenerator.class, "generator");
        GameRegistry.registerTileEntity(TileEntityKiln.class, "kiln");
    }


    private static void registerBlock(net.minecraft.block.Block block) {
        ForgeRegistries.BLOCKS.register(block);
        ItemBlock item = new ItemBlock(block);
        item.setRegistryName(block.getRegistryName());
        ForgeRegistries.ITEMS.register(item);
    }

    public static void registerRenders() {
        registerRender(ALLOYSMELTER);
        registerRender(COPPER_ORE);
        registerRender(KILN);
        registerRender(GENERATOR);
    }

    public static void registerRender(net.minecraft.block.Block block) {
        Item item = Item.getItemFromBlock(block);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }
}
