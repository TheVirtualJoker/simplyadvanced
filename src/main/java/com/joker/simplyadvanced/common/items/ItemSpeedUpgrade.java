package com.joker.simplyadvanced.common.items;

import com.joker.simplyadvanced.common.utils.CreativeUtil;
import net.minecraft.item.Item;

public class ItemSpeedUpgrade extends Item {
    public ItemSpeedUpgrade() {
        setUnlocalizedName("speed_upgrade");
        setRegistryName("speed_upgrade");
        setCreativeTab(CreativeUtil.TAB);
    }
}
