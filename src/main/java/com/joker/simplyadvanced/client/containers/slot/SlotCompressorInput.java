package com.joker.simplyadvanced.client.containers.slot;

import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityCompressor;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCompressorInput extends Slot {

    private final TileEntityCompressor compressor;

    public SlotCompressorInput(TileEntityCompressor compressor, int index, int xPosition, int yPosition) {
        super(compressor, index, xPosition, yPosition);
        this.compressor = compressor;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return compressor.canCompress(stack);
    }
}
