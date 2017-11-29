package com.joker.simplyadvanced.common.init;

import com.joker.simplyadvanced.common.items.ItemCopperIngot;
import com.joker.simplyadvanced.common.lib.References;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Items {
    public static Item COPPER_INGOT;

    public static void init() {
        COPPER_INGOT = new ItemCopperIngot();
    }

    public static void reg() {
        regItem(COPPER_INGOT);
    }

    private static void regItem(Item item) {
        ForgeRegistries.ITEMS.register(item);
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(
                new ResourceLocation(References.MODID, item.getUnlocalizedName().substring(5)), "inventory"));
    }
}
