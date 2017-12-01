package com.joker.simplyadvanced.client.gui;

import com.joker.simplyadvanced.client.containers.ContainerCentrifuge;
import com.joker.simplyadvanced.common.lib.References;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityCentrifuge;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiCentrifuge extends GuiContainer {
    private static final ResourceLocation CENTRIFUGE_TEXTURE = new ResourceLocation(References.MODID + ":textures/gui/container/centrifuge.png");
    private final InventoryPlayer player;
    private TileEntityCentrifuge tileentity;

    public GuiCentrifuge(InventoryPlayer player, TileEntityCentrifuge tileentity) {
        super(new ContainerCentrifuge(player, tileentity));
        this.player = player;
        this.tileentity = tileentity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.tileentity.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, (this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2) + 48, 4, Color.WHITE.getRGB());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(CENTRIFUGE_TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        int l = this.getPowerLeftScaled(73);
        this.drawTexturedModalRect(this.guiLeft + 9, this.guiTop + 6, 176, 25, 10, l);
    }

    private int getPowerLeftScaled(int pixels) {
        int i = this.tileentity.getField(2);
        if (i == 0)
            i = 1;
        return 25000 * pixels / i;
    }

    private int getSpinProgressScaled(int pixels) {
        int i = this.tileentity.getField(0); // spin time
        int j = this.tileentity.getField(1); // total spin time
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
