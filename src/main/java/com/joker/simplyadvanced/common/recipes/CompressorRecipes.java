package com.joker.simplyadvanced.common.recipes;

import cofh.thermalfoundation.item.ItemMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CompressorRecipes {
    private static final CompressorRecipes INSTANCE = new CompressorRecipes ();
    private Map<ItemStack, Result> map = new HashMap<>();

    private CompressorRecipes () {
        register(ItemMaterial.dustTin, new Result(200, ItemMaterial.ingotTin));
        register(ItemMaterial.dustCopper, new Result(200, ItemMaterial.ingotCopper));
    }

    public static CompressorRecipes getInstance() {
        return INSTANCE;
    }

    public Result getResult (ItemStack item) {
        for (Map.Entry<ItemStack, Result> entry : map.entrySet()) {
            if (item.isItemEqual(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void register (Item input, Result result) {
        register(new ItemStack(input), result);
    }

    private void register (ItemStack input, Result result) {
        map.put(input, result);
    }

    public class Result {
        private int time = 0;
        private ItemStack output = ItemStack.EMPTY;

        Result (int time, ItemStack output) {
            this.time = time;
            this.output = output;
        }

        Result (int time, Item output) {
            this.time = time;
            this.output = new ItemStack(output);
        }

        public int getTime() {
            return time;
        }

        public ItemStack getOutput() {
            return output;
        }
    }
}
