package com.joker.simplyadvanced.common.items.fuel;

import com.joker.simplyadvanced.common.utils.SATab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHighTempFuel extends Item {
    public ItemHighTempFuel() {
        setUnlocalizedName("high_temp_fuel");
        setRegistryName("high_temp_fuel");
        setCreativeTab(SATab.TAB);
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack) {
        return 16000;
    }
}
