package com.joker.simplyadvanced.common.recipes;

import com.joker.simplyadvanced.common.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CompressorRecipes {
    private static final CompressorRecipes INSTANCE = new CompressorRecipes ();
    private Map<ItemStack, ItemStack> map = new HashMap<>();

    private CompressorRecipes () {
        register(ModItems.TIN_DUST, ModItems.TIN_INGOT);
    }

    public static CompressorRecipes getInstance() {
        return INSTANCE;
    }

    public ItemStack getResult (ItemStack item) {
        for (Map.Entry<ItemStack, ItemStack> entry : map.entrySet()) {
            if (item.isItemEqual(entry.getKey())) {
                return entry.getValue();
            }
        }
        return ItemStack.EMPTY;
    }

    private void register (Item input, Item outut) {
        register(new ItemStack(input), new ItemStack(outut));
    }

    private void register (ItemStack input, ItemStack outut) {
        map.put(input, outut);
    }
}
