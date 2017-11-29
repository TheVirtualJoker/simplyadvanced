package com.joker.simplyadvanced.common.recipes;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;

import java.util.Map;
import java.util.Map.Entry;

public class AlloyFurnaceRecipes {
    private static final AlloyFurnaceRecipes SMELT = new AlloyFurnaceRecipes();
    private final Table<ItemStack, ItemStack, ItemStack> alloySmeltingList = HashBasedTable.create();
    private final Map<ItemStack, Float> experienceList = Maps.newHashMap();

    public static AlloyFurnaceRecipes instance() {
        return SMELT;
    }

    private AlloyFurnaceRecipes() {
        this.addAlloyFurnaceRecipe(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.DIAMOND, 2), 10F);
    }

    public void addAlloyFurnaceRecipe(ItemStack input1, ItemStack input2, ItemStack result, float exp) {
        if (getAlloyFurnaceResult(input1, input2) != ItemStack.EMPTY) {
            FMLLog.info("A recipe has already been registered for the 'Alloy Furnace' with the same recipe! Ignoring this so there is no conflict!!!");
            return;
        }
        this.alloySmeltingList.put(input1, input2, result);
        this.alloySmeltingList.put(input2, input1, result);
        this.experienceList.put(result, Float.valueOf(exp));
    }

    public ItemStack getAlloyFurnaceResult(ItemStack input, ItemStack input2) {
        for (Entry<ItemStack, Map<ItemStack, ItemStack>> entry : this.alloySmeltingList.columnMap().entrySet()) {
            if (this.compareItemStacks(input, entry.getKey())) {
                for (Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet()) {
                    if (this.compareItemStacks(input2, ent.getKey())) {
                        return ent.getValue();
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean compareItemStacks(ItemStack stackl, ItemStack stack2) {
        return stack2.getItem() == stackl.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stackl.getMetadata());
    }

    public Table<ItemStack, ItemStack, ItemStack> getAlloySmeltingList() {
        return this.alloySmeltingList;
    }

    public float getAlloyExperience(ItemStack stack) {
        for (Map.Entry<ItemStack, Float> entry : this.experienceList.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey())) {
                return entry.getValue().floatValue();
            }
        }
        return 0.0F;
    }
}
