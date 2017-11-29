package com.joker.simplyadvanced.common.utils;

import com.joker.simplyadvanced.common.init.ModBlocks;
import com.joker.simplyadvanced.common.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeHandler {

    public static void registerCrafting () {}

    public static void registerFurnace (){
        GameRegistry.addSmelting(ModBlocks.COPPER_ORE, new ItemStack(ModItems.COPPER_INGOT), 1.0F);
    }
}
