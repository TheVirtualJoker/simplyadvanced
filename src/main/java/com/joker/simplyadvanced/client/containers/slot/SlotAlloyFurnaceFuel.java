package com.joker.simplyadvanced.client.containers.slot;

import com.joker.simplyadvanced.common.tiles.machines.hightemp.TileEntityAlloyFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotAlloyFurnaceFuel extends Slot {

    public SlotAlloyFurnaceFuel(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return TileEntityAlloyFurnace.isItemFuel(stack);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return super.getItemStackLimit(stack);
    }
}
