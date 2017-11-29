package com.joker.simplyadvanced.client.containers.slot;

import com.joker.simplyadvanced.common.tiles.TileEntityKiln;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotKilnInput extends Slot {
    private TileEntityKiln kiln;

    public SlotKilnInput(TileEntityKiln kiln, int index, int xPosition, int yPosition) {
        super(kiln, index, xPosition, yPosition);
        this.kiln = kiln;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return kiln.canHarden(stack);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return super.getItemStackLimit(stack);
    }
}
