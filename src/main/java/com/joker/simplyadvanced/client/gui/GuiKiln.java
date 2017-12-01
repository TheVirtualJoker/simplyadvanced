package com.joker.simplyadvanced.client.gui;

import com.joker.simplyadvanced.client.containers.ContainerKiln;
import com.joker.simplyadvanced.common.lib.References;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityKiln;
import com.joker.simplyadvanced.common.utils.ProgressBar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiKiln extends GuiContainer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(References.MODID + ":textures/gui/container/kiln.png");
    private TileEntityKiln tileentity;
    private ProgressBar progressBar;

    public GuiKiln(InventoryPlayer player, TileEntityKiln tileentity) {
        super(new ContainerKiln(player, tileentity));
        this.tileentity = tileentity;
        this.xSize = 175;
        this.ySize = 165;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        if (tileentity.isBurning()) progressBar.draw(mc);
        String s = this.tileentity.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 4, Color.WHITE.getRGB());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (progressBar == null) {
            progressBar = new ProgressBar(TEXTURE, ProgressBar.ProgressBarDirection.LEFT_TO_RIGHT,
                    24, 17, 81, 28, 176, 0);
        }
        progressBar.setMax(tileentity.getField(1)).setMin(tileentity.getField(0));
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
