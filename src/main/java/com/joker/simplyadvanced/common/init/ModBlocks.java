package com.joker.simplyadvanced.common.init;

import com.joker.simplyadvanced.common.blocks.BlockAlloyFurnace;
import com.joker.simplyadvanced.common.tiles.TileEntityAlloyFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
    public static net.minecraft.block.Block alloySmelter;

    public static void init() {
        alloySmelter = new BlockAlloyFurnace();
    }

    public static void register() {
        registerBlock(alloySmelter);
        GameRegistry.registerTileEntity(TileEntityAlloyFurnace.class, "alloyfurnace");
    }


    private static void registerBlock(net.minecraft.block.Block block) {
        ForgeRegistries.BLOCKS.register(block);
        ItemBlock item = new ItemBlock(block);
        item.setRegistryName(block.getRegistryName());
        ForgeRegistries.ITEMS.register(item);
    }

    public static void registerRenders() {
        registerRender(alloySmelter);
    }

    public static void registerRender(net.minecraft.block.Block block) {
        Item item = Item.getItemFromBlock(block);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }
}
