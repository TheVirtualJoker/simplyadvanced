package com.joker.simplyadvanced.client.containers;

import com.joker.simplyadvanced.client.containers.slot.SlotCompressorInput;
import com.joker.simplyadvanced.client.containers.slot.SlotOutput;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityCompressor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCompressor extends Container {
    private final TileEntityCompressor tileentity;
    private int compressTime;

    public ContainerCompressor(InventoryPlayer player, TileEntityCompressor tileentity) {
        this.tileentity = tileentity;
        this.addSlotToContainer(new SlotCompressorInput(tileentity, 0, 53, 27));
        this.addSlotToContainer(new SlotOutput(tileentity, 1, 106, 27));

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
            if (this.compressTime != this.tileentity.getField(0))
                listener.sendWindowProperty(this, 0, this.tileentity.getField(0));
        }
        this.compressTime = this.tileentity.getField(0);
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
                slot.onSlotChanged();
            }
        }
        return stack;
    }
}

