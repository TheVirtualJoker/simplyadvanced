package com.joker.simplyadvanced.client.containers;

import com.joker.simplyadvanced.client.containers.slot.SlotCentrifugeOut;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityCentrifuge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCentrifuge extends Container {
    private final TileEntityCentrifuge tileentity;

    private int spinTime;
    private int totalSpinTime;
    private int energyStored;

    public ContainerCentrifuge(InventoryPlayer player, TileEntityCentrifuge tileentity) {
        this.tileentity = tileentity;
        this.addSlotToContainer(new Slot(tileentity, 4, 80, 34));
        this.addSlotToContainer(new SlotCentrifugeOut(player.player, tileentity, 0, 80, 4));
        this.addSlotToContainer(new SlotCentrifugeOut(player.player, tileentity, 1, 110, 34));
        this.addSlotToContainer(new SlotCentrifugeOut(player.player, tileentity, 2, 80, 64));
        this.addSlotToContainer(new SlotCentrifugeOut(player.player, tileentity, 3, 50, 34));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 83 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 141));
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

        for (int i = 0; i < this.listeners.size(); i++) {
            IContainerListener listener = this.listeners.get(i);
            if (this.spinTime != this.tileentity.getField(0))
                listener.sendWindowProperty(this, 0, this.tileentity.getField(0));
            if (this.totalSpinTime != this.tileentity.getField(1))
                listener.sendWindowProperty(this, 1, this.tileentity.getField(1));
            if (this.energyStored != this.tileentity.getField(2))
                listener.sendWindowProperty(this, 2, this.tileentity.getField(2));
        }
        this.spinTime = this.tileentity.getField(0);
        this.totalSpinTime = this.tileentity.getField(1);
        this.energyStored = this.tileentity.getField(2);
    }

    @Override
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

            if (index < tileentity.getSizeInventory()) {
                if (!this.mergeItemStack(stack1, tileentity.getSizeInventory(), inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack1, 0, tileentity.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (stack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onTake(playerIn, stack1);
            }
        }
        return stack;
    }
}
