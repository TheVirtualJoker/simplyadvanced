package com.joker.simplyadvanced.client.containers;

import com.joker.simplyadvanced.client.containers.slot.SlotGeneratorUpgrade;
import com.joker.simplyadvanced.common.init.ModItems;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerGenerator extends Container {
    private final TileEntityGenerator tileentity;
    private int storageTier, speedTier, energy;

    public ContainerGenerator(InventoryPlayer player, TileEntityGenerator tileentity) {
        this.tileentity = tileentity;
        this.addSlotToContainer(new SlotGeneratorUpgrade(tileentity, ModItems.STORAGE_UPGRADE, 0, 152, 9));
        this.addSlotToContainer(new SlotGeneratorUpgrade(tileentity, ModItems.SPEED_UPGRADE, 1, 152, 32));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(player, x + y * 9 + 9, 8 + x * 18, 70 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(player, x, 8 + x * 18, 128));
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
            if (energy != this.tileentity.getField(0))
                listener.sendWindowProperty(this, 0, this.tileentity.getField(0));
            if (this.storageTier != this.tileentity.getField(2))
                listener.sendWindowProperty(this, 2, this.tileentity.getField(2));
            if (this.speedTier != this.tileentity.getField(3))
                listener.sendWindowProperty(this, 3, this.tileentity.getField(3));
        }

        this.energy = this.tileentity.getField(0);
        this.storageTier = this.tileentity.getField(2);
        this.speedTier = this.tileentity.getField(3);
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

