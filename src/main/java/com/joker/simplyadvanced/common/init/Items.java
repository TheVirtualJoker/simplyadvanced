package com.joker.simplyadvanced.common.init;

import com.joker.simplyadvanced.common.items.CopperIngot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Items {
    public static Item copperingot;

    public static void init() {
        copperingot = new CopperIngot();
    }

    public static void reg() {
        regItem(copperingot);
    }

    private static void regItem(Item item) {
        ForgeRegistries.ITEMS.register(item);
    }
}
