package com.joker.simplyadvanced.client.gui;

import com.joker.simplyadvanced.client.containers.ContainerCompressor;
import com.joker.simplyadvanced.common.lib.References;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityCompressor;
import com.joker.simplyadvanced.client.utils.ProgressBar;
import com.joker.simplyadvanced.client.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiCompressor extends GuiContainer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(References.MODID + ":textures/gui/container/compressor.png");
    private TileEntityCompressor tileentity;
    private ProgressBar progressBar;
    private ProgressBar energyBar;

    public GuiCompressor(InventoryPlayer player, TileEntityCompressor tileentity) {
        super(new ContainerCompressor(player, tileentity));
        this.tileentity = tileentity;
        this.xSize = 175;
        this.ySize = 165;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        if (tileentity.isCompressing()) progressBar.draw(mc);
        energyBar.draw(mc);
        fontRenderer.drawString(tileentity.hasCustomName() ? tileentity.getName() : "Compressor", 5, 5, Color.darkGray.getRGB());

        if (Utils.mouseInRegion(this, 79, 20, 96, 44, mouseX, mouseY)) {
            List<String> hoveringText = new ArrayList<>();
            hoveringText.add(String.valueOf(Utils.percent(tileentity.getField(0), tileentity.getField(1))) + TextFormatting.GREEN + "%");
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
        }

        if (Utils.mouseInRegion(this, 8, 65, 167, 71, mouseX, mouseY)) {
            List<String> hoveringText = new ArrayList<>();
            String text = TextFormatting.WHITE + "%c* / %m*";
            text = text.replace("%c", String.valueOf(tileentity.getField(3)));
            text = text.replace("%m", String.valueOf(tileentity.getField(4)));
            text = text.replace("*", TextFormatting.GREEN + "RF" + TextFormatting.WHITE);
            hoveringText.add(TextFormatting.WHITE + "RF Stored: ");
            hoveringText.add(text);
            hoveringText.add("Percent Filled: " + Utils.percent(tileentity.getField(3), tileentity.getField(4)) + TextFormatting.GREEN + "%");

            if (tileentity.getField(2) == 0) {
                hoveringText.add(" ");
                hoveringText.add(TextFormatting.RED + "Not Enough Power");
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (progressBar == null)
            progressBar = new ProgressBar(TEXTURE, ProgressBar.ProgressBarDirection.UP_TO_DOWN,
                    18, 25, 79, 20, 177, 1);
        if (energyBar == null)
            energyBar = new ProgressBar(TEXTURE, ProgressBar.ProgressBarDirection.LEFT_TO_RIGHT,
                    160, 7, 8, 65, 8, 169);

        progressBar.setMax(tileentity.getField(1)).setMin(tileentity.getField(0));
        energyBar.setMax(tileentity.getField(4)).setMin(tileentity.getField(3));
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
