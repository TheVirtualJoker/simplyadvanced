package com.joker.simplyadvanced.common.items;

import com.joker.simplyadvanced.common.utils.CreativeUtil;
import net.minecraft.item.Item;

public class ItemStorageUpgrade extends Item {
    public ItemStorageUpgrade() {
        setUnlocalizedName("storage_upgrade");
        setRegistryName("storage_upgrade");
        setCreativeTab(CreativeUtil.TAB);
    }
}
