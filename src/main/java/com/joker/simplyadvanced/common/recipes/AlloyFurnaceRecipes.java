package com.joker.simplyadvanced.common.recipes;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class AlloyFurnaceRecipes {
    private static final AlloyFurnaceRecipes SMELT = new AlloyFurnaceRecipes();
    private final Table<ItemStack, ItemStack, ItemStack> alloySmeltingList = HashBasedTable.<ItemStack, ItemStack, ItemStack>create();
    private final Map<ItemStack, Float> experienceList = Maps.<ItemStack, Float>newHashMap();

    public static AlloyFurnaceRecipes instance() {
        return SMELT;
    }


}
