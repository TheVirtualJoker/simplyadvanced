package com.joker.simplyadvanced.common.init;

import com.joker.simplyadvanced.common.blocks.*;
import com.joker.simplyadvanced.common.tiles.machines.hightemp.TileEntityAlloyFurnace;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityCentrifuge;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityCompressor;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityGenerator;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityKiln;
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
    public static Block CENTRIFUGE;
    public static Block COMPRESSOR;
    public static Block ORE_REED;

    public static void init() {
        ALLOYSMELTER = new BlockAlloyFurnace();
        COPPER_ORE = new BlockCopperOre();
        KILN = new BlockKiln();
        CENTRIFUGE = new BlockCentrifuge();
        GENERATOR = new BlockGenerator();
        COMPRESSOR = new BlockCompressor();
        ORE_REED = new BlockOreReed();
    }

    public static void register() {
        registerBlock(ALLOYSMELTER);
        registerBlock(COPPER_ORE);
        registerBlock(KILN);
        registerBlock(CENTRIFUGE);
        registerBlock(GENERATOR);
        registerBlock(COMPRESSOR);
        registerBlock(ORE_REED);
        GameRegistry.registerTileEntity(TileEntityAlloyFurnace.class, "alloy_furnace");
        GameRegistry.registerTileEntity(TileEntityGenerator.class, "generator");
        GameRegistry.registerTileEntity(TileEntityKiln.class, "kiln");
        GameRegistry.registerTileEntity(TileEntityCentrifuge.class, "centrifuge");
        GameRegistry.registerTileEntity(TileEntityCompressor.class, "compressor");
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
        registerRender(COMPRESSOR);
    }

    public static void registerRender(net.minecraft.block.Block block) {
        Item item = Item.getItemFromBlock(block);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }
}
