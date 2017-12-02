package com.joker.simplyadvanced.client.render;

import com.joker.simplyadvanced.common.blocks.BlockCompressor;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityCompressor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

public class CompressorItemRender extends TileEntitySpecialRenderer<TileEntityCompressor> {

    @Override
    public void render(TileEntityCompressor compressor, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GL11.glPushMatrix();

        float xOffset = 0.0F;
        float zOffset = 0.0F;
        float angle = 0.0F;
        IBlockState state = compressor.getWorld().getBlockState(compressor.getPos());
        EnumFacing facing = state.getValue(BlockCompressor.FACING);
        switch (facing) {
            case WEST:
                angle = 270F;
                zOffset = 0.2F;
                xOffset = 0.7F;
                break;
            case EAST:
                angle = 90F;
                xOffset -= 0.7F;
                zOffset += 0.2F;
                break;
            case NORTH:
                angle = 180F;
                zOffset += 0.9F;
                break;
            case SOUTH:
                zOffset -= 0.5F;
                break;
        }

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glTranslatef((float) x + 0.5F + xOffset, (float) y + 0.005F, (float) z + 0.3F + zOffset);
        GL11.glRotatef(angle, 0, 1, 0);
        GL11.glRotatef(180, 0, 1, 1);
        GlStateManager.translate(0, 0.1, 0);
        GL11.glScalef(1.3F, 1.3F, 1.3F);

        ItemStack stack = compressor.getStackInSlot(0).copy();
        if (!compressor.getStackInSlot(1).copy().isEmpty() && stack.isEmpty())
            stack = compressor.getStackInSlot(1).copy();
        if (stack.isEmpty() && compressor.getStackInSlot(1).copy().isEmpty())
            stack = ItemStack.EMPTY;

        if (!stack.isEmpty()) {
            stack.setCount(1);
            EntityItem entityItem = new EntityItem(Minecraft.getMinecraft().world, 0D, 0D, 0D, stack);
            entityItem.hoverStart = 0.0F;
            Minecraft.getMinecraft().getRenderManager().renderEntity(entityItem, 0.0D, 0.0D, 0.1D, 0.0F, 0.0F, false);
        }

        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();
    }
}
