package com.joker.simplyadvanced.client.containers;

import com.joker.simplyadvanced.common.tiles.TileEntityAlloyFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerAlloyFurnace extends Container {

    private final TileEntityAlloyFurnace tileentity;

    private int cookTime;
    private int totalCookTime;
    private int burnTime;
    private int currentBurnTime;

    ContainerAlloyFurnace(InventoryPlayer player, TileEntityAlloyFurnace tileentity) {
        this.tileentity = tileentity;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}
