package com.joker.simplyadvanced.common.utils;

import com.joker.simplyadvanced.common.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class SATab {
    public static CreativeTabs TAB = new CreativeTabs("simplyadvanced") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.COPPER_INGOT);
        }
    };
}
