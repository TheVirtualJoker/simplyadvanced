package com.joker.simplyadvanced.common.recipes;

import cofh.thermalfoundation.item.ItemMaterial;
import com.joker.simplyadvanced.client.utils.Utils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CentrifugeRecipes {
    private static final CentrifugeRecipes SPIN = new CentrifugeRecipes();
    private Map<ItemStack, LinkedList<ItemStack>> map = new HashMap<>();

    public static CentrifugeRecipes instance() {
        return SPIN;
    }

    private CentrifugeRecipes() {
        this.addSpinRecipe(new ItemStack(Items.DIAMOND), Utils.modifySize(ItemMaterial.ingotTin, 3), new ItemStack(Items.EMERALD), new ItemStack(Items.DIAMOND), new ItemStack(Items.IRON_INGOT));
        this.addSpinRecipe(new ItemStack(Items.APPLE), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.EMERALD), new ItemStack(Items.DIAMOND), new ItemStack(Items.IRON_INGOT));
        this.addSpinRecipe(new ItemStack(Items.MAGMA_CREAM), new ItemStack(Items.BLAZE_POWDER), new ItemStack(Items.SLIME_BALL), new ItemStack(ItemStack.EMPTY.getItem()), new ItemStack(ItemStack.EMPTY.getItem()));
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
        for (Map.Entry<ItemStack, LinkedList<ItemStack>> entry : map.entrySet()) {
            if (entry.getKey().isItemEqual(input)) return entry.getValue();
        }

        return new LinkedList<>();
    }
}
