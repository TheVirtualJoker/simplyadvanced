package com.joker.simplyadvanced.common.utils;

import com.joker.simplyadvanced.common.init.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeUtil {
    public static CreativeTabs TAB = new CreativeTabs("simplyadvanced") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.COPPER_INGOT);
        }
    };
}
