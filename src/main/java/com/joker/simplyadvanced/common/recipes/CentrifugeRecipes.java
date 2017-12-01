package com.joker.simplyadvanced.common.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CentrifugeRecipes {
    private static final CentrifugeRecipes SPIN = new CentrifugeRecipes();
    Map<ItemStack, LinkedList<ItemStack>> map = new HashMap<>();

    public static CentrifugeRecipes instance() {
        return SPIN;
    }

    private CentrifugeRecipes() {
        this.addSpinRecipe(new ItemStack(Items.APPLE), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.EMERALD), new ItemStack(Items.DIAMOND), new ItemStack(Items.IRON_INGOT));
    }

    private void addSpinRecipe(ItemStack input, ItemStack output1, ItemStack output2, ItemStack output3, ItemStack output4) {
        LinkedList<ItemStack> list = new LinkedList<>();
        list.addFirst(output1);
        list.add(output2);
        list.add(output3);
        list.addLast(output4);

        map.put(input, list);
    }

    public LinkedList<ItemStack> getSpinResult(ItemStack input) {
        return map.getOrDefault(input, new LinkedList<>());
    }
}
