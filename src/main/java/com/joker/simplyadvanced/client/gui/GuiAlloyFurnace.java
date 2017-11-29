package com.joker.simplyadvanced.client.gui;

import com.joker.simplyadvanced.client.containers.ContainerAlloyFurnace;
import com.joker.simplyadvanced.common.lib.References;
import com.joker.simplyadvanced.common.tiles.TileEntityAlloyFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiAlloyFurnace extends GuiContainer {

    private static final ResourceLocation ALLOYFURNACE_TEXTURE = new ResourceLocation(References.MODID + ":textures/gui/container/alloyfurnace.png");
    private final InventoryPlayer player;
    private TileEntityAlloyFurnace tileentity;

    public GuiAlloyFurnace(InventoryPlayer player, TileEntityAlloyFurnace tileentity) {
        super(new ContainerAlloyFurnace(player, tileentity));
        this.player = player;
        this.tileentity = tileentity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.tileentity.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 4, Color.WHITE.getRGB());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(ALLOYFURNACE_TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        if (TileEntityAlloyFurnace.isBurning(this.tileentity)) {
            int k = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(this.guiLeft + 80, this.guiTop + 42 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = this.getcookProgressScaled(24);
        int r = this.getcookProgressScaled(24);
        this.drawTexturedModalRect(this.guiLeft + 43, this.guiTop + 18, 176, 14, l + 1, 16);
        this.drawTexturedModalRect(this.guiLeft + 109, this.guiTop + 18, 176, 31, 23 - r, 16);
    }

    private int getBurnLeftScaled(int pixels) {
        int i = this.tileentity.getField(1);
        if (i == 0) {
            i = 200;
        }
        return this.tileentity.getField(0) * pixels / i;
    }

    private int getcookProgressScaled(int pixels) {
        int i = this.tileentity.getField(2);
        int j = this.tileentity.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
