package com.joker.simplyadvanced.client.containers;

import com.joker.simplyadvanced.common.tiles.TileEntityAlloyFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerAlloyFurnace extends Container {

    private final TileEntityAlloyFurnace tileentity;

    private int cookTime;
    private int totalCookTime;
    private int burnTime;
    private int currentBurnTime;

    public ContainerAlloyFurnace(InventoryPlayer player, TileEntityAlloyFurnace tileentity) {
        this.tileentity = tileentity;
        this.addSlotToContainer(new Slot(tileentity, 0, 20, 18));
        this.addSlotToContainer(new Slot(tileentity, 1, 140, 18));
        this.addSlotToContainer(new SlotFurnaceFuel(tileentity, 2, 80, 59));
        this.addSlotToContainer(new SlotFurnaceOutput(player.player, tileentity, 3, 80, 19));

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

        for (int i = 0; i < this.listeners.size(); i++) {
            IContainerListener listener = this.listeners.get(i);

            if (this.cookTime != this.tileentity.getField(2))
                listener.sendWindowProperty(this, 2, this.tileentity.getField(2));
            if (this.burnTime != this.tileentity.getField(0))
                listener.sendWindowProperty(this, 0, this.tileentity.getField(0));
            if (this.currentBurnTime != this.tileentity.getField(1))
                listener.sendWindowProperty(this, 1, this.tileentity.getField(1));
            if (this.totalCookTime != this.tileentity.getField(3))
                listener.sendWindowProperty(this, 3, this.tileentity.getField(3));
        }

        this.cookTime = this.tileentity.getField(2);
        this.burnTime = this.tileentity.getField(0);
        this.currentBurnTime = this.tileentity.getField(1);
        this.totalCookTime = this.tileentity.getField(3);
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
                slot.onTake(playerIn, stack1);
            }
        }
        return stack;
    }
}

