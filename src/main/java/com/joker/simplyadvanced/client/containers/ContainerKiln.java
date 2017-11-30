package com.joker.simplyadvanced.client.containers;

import com.joker.simplyadvanced.client.containers.slot.SlotKilnOutput;
import com.joker.simplyadvanced.common.tiles.TileEntityKiln;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerKiln extends Container {
    private final TileEntityKiln tileentity;
    private int cookTime;
    private int burnTime;
    private int currentBurnTime;

    public ContainerKiln(InventoryPlayer player, TileEntityKiln tileentity) {
        this.tileentity = tileentity;
        this.addSlotToContainer(new Slot(tileentity, 0, 52, 28));
        this.addSlotToContainer(new SlotKilnOutput(player.player, tileentity, 1, 118, 28));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 142));
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileentity);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : this.listeners) {
            if (this.burnTime != this.tileentity.getField(0))
                listener.sendWindowProperty(this, 0, this.tileentity.getField(0));
            if (this.currentBurnTime != this.tileentity.getField(1))
                listener.sendWindowProperty(this, 1, this.tileentity.getField(1));
            if (this.cookTime != this.tileentity.getField(2))
                listener.sendWindowProperty(this, 2, this.tileentity.getField(2));
        }

        this.burnTime = this.tileentity.getField(0);
        this.currentBurnTime = this.tileentity.getField(1);
        this.cookTime = this.tileentity.getField(2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.tileentity.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileentity.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if (index == 1) {
                if (!this.mergeItemStack(stack1, 4, playerIn.inventory.getSizeInventory(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack1, stack);
            } else if (index != 0) {
                if (tileentity.canHarden(slot.getStack())) {
                    if (!this.mergeItemStack(stack1, 0, 1, false))
                        return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack1, 4, playerIn.inventory.getSizeInventory(), false))
                return ItemStack.EMPTY;

            if (stack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }else {
                slot.onSlotChanged();
            }
            if (stack1.getCount() == stack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, stack1);
        }
        return stack;
    }
}

