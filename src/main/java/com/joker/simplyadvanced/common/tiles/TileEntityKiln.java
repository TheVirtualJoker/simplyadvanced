package com.joker.simplyadvanced.common.tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public class TileEntityKiln extends TileEntity {
    private NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
}
