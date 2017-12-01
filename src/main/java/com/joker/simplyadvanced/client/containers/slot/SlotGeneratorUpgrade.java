package com.joker.simplyadvanced.client.containers.slot;

import com.joker.simplyadvanced.common.tiles.TileEntityGenerator;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotGeneratorUpgrade extends Slot {
    private Item upgrade;

    public SlotGeneratorUpgrade(TileEntityGenerator kiln, Item upgrade, int index, int xPosition, int yPosition) {
        super(kiln, index, xPosition, yPosition);
        this.upgrade = upgrade;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() == upgrade;
    }
}
