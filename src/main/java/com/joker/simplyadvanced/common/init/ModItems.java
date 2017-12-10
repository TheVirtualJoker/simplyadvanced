package com.joker.simplyadvanced.common.init;

import com.joker.simplyadvanced.common.items.fuel.ItemHighTempFuel;
import com.joker.simplyadvanced.common.items.metals.dust.ItemTinDust;
import com.joker.simplyadvanced.common.items.metals.ingot.ItemCopperIngot;
import com.joker.simplyadvanced.common.items.metals.ingot.ItemTinIngot;
import com.joker.simplyadvanced.common.items.plants.ItemReedOre;
import com.joker.simplyadvanced.common.items.tools.*;
import com.joker.simplyadvanced.common.items.upgrade.ItemSpeedUpgrade;
import com.joker.simplyadvanced.common.items.upgrade.ItemStorageUpgrade;
import com.joker.simplyadvanced.common.lib.References;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModItems {
    public static Item COPPER_INGOT,
            COPPER_SPADE,
            COPPER_PICKAXE,
            COPPER_AXE,
            COPPER_HOE,
            COPPER_SWORD,
            TIN_SPADE,
            TIN_PICKAXE,
            TIN_AXE,
            TIN_HOE,
            TIN_SWORD,
            HIGH_TEMP_FUEL,
            SPEED_UPGRADE,
            STORAGE_UPGRADE,
            TIN_DUST,
            TIN_INGOT,
            ORE_REED;

    public static void init() {
        COPPER_INGOT = new ItemCopperIngot();
        TIN_DUST = new ItemTinDust();
        TIN_INGOT = new ItemTinIngot();

        // -------- Tools ----------
        COPPER_SPADE = new CustomSpade(SAToolMaterial.COPPER);
        COPPER_PICKAXE = new CustomPickaxe(SAToolMaterial.COPPER);
        COPPER_AXE = new CustomAxe(SAToolMaterial.COPPER);
        COPPER_HOE = new CustomHoe(SAToolMaterial.COPPER);
        COPPER_SWORD = new CustomSword(SAToolMaterial.COPPER);
        TIN_SPADE = new CustomSpade(SAToolMaterial.TIN);
        TIN_PICKAXE = new CustomPickaxe(SAToolMaterial.TIN);
        TIN_AXE = new CustomAxe(SAToolMaterial.TIN);
        TIN_HOE = new CustomHoe(SAToolMaterial.TIN);
        TIN_SWORD = new CustomSword(SAToolMaterial.TIN);

        // --------- Machine Items ---------
        HIGH_TEMP_FUEL = new ItemHighTempFuel();
        SPEED_UPGRADE = new ItemSpeedUpgrade();
        STORAGE_UPGRADE = new ItemStorageUpgrade();

        //---------- Plants ---------
        ORE_REED = new ItemReedOre();
    }

    public static void reg() {
        regItem(COPPER_INGOT);
        regItem(COPPER_SPADE);
        regItem(COPPER_PICKAXE);
        regItem(COPPER_AXE);
        regItem(COPPER_HOE);
        regItem(COPPER_SWORD);
        regItem(TIN_SPADE);
        regItem(TIN_PICKAXE);
        regItem(TIN_AXE);
        regItem(TIN_HOE);
        regItem(TIN_SWORD);
        regItem(HIGH_TEMP_FUEL);
        regItem(SPEED_UPGRADE);
        regItem(STORAGE_UPGRADE);
        regItem(TIN_DUST);
        regItem(TIN_INGOT);
        regItem(ORE_REED);
    }

    private static void regItem(Item item) {
        ForgeRegistries.ITEMS.register(item);
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(
                new ResourceLocation(References.MODID, item.getUnlocalizedName().substring(5)), "inventory"));
    }
}
