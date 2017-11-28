package com.joker.simplyadvanced.common.init;

import com.joker.simplyadvanced.common.items.ItemCopperIngot;
import net.minecraft.item.Item;
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
    }
}
