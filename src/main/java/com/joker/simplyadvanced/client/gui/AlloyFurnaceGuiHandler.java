package com.joker.simplyadvanced.client.gui;

import com.joker.simplyadvanced.client.containers.ContainerAlloyFurnace;
import com.joker.simplyadvanced.client.containers.ContainerKiln;
import com.joker.simplyadvanced.common.tiles.TileEntityAlloyFurnace;
import com.joker.simplyadvanced.common.tiles.TileEntityKiln;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class AlloyFurnaceGuiHandler implements IGuiHandler {
    public static final int ALLOYFURNACE = 0;
    public static final int KILN = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ALLOYFURNACE)
            return new ContainerAlloyFurnace(player.inventory, ((TileEntityAlloyFurnace) world.getTileEntity(new BlockPos(x, y, z))));
        if (ID == KILN)
            return new ContainerKiln(player.inventory, ((TileEntityKiln) world.getTileEntity(new BlockPos(x, y, z))));
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == ALLOYFURNACE)
            return new GuiAlloyFurnace(player.inventory, ((TileEntityAlloyFurnace) world.getTileEntity(new BlockPos(x, y, z))));
        if (ID == KILN)
            return new GuiKiln(player.inventory, ((TileEntityKiln) world.getTileEntity(new BlockPos(x, y, z))));
        return null;
    }
}
