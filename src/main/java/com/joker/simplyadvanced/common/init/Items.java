package com.joker.simplyadvanced.common.init;

import com.joker.simplyadvanced.common.items.ItemCopperIngot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Items {
    public static Item copperIngot;

    public static void init() {
        copperIngot = new ItemCopperIngot();
    }

    public static void reg() {
        regItem(copperIngot);
    }

    private static void regItem(Item item) {
        ForgeRegistries.ITEMS.register(item);
    }
}
