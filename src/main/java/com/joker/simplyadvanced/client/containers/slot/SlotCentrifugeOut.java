package com.joker.simplyadvanced.client.containers.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCentrifugeOut extends Slot {
    private final EntityPlayer player;
    private int removeCount;

    public SlotCentrifugeOut(EntityPlayer player, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack())
            this.removeCount += Math.min(amount, this.getStack().getCount());
        return super.decrStackSize(amount);
    }

    @Override
    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
        this.onCrafting(stack);
        super.onTake(thePlayer, stack);
        return stack;
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.onCrafting(stack, amount);
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        super.onCrafting(stack);
        this.removeCount = 0;
    }
}
