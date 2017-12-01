package com.joker.simplyadvanced.client.gui;

import com.joker.simplyadvanced.client.containers.ContainerGenerator;
import com.joker.simplyadvanced.common.lib.References;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityGenerator;
import com.joker.simplyadvanced.common.utils.ProgressBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiGenerator extends GuiContainer {

    private final String rfMsg = TextFormatting.WHITE+"%c* / %m*";
    private static final ResourceLocation TEXTURE = new ResourceLocation(References.MODID + ":textures/gui/container/generator.png");
    private TileEntityGenerator tileentity;
    private ProgressBar progressBar;

    public GuiGenerator(InventoryPlayer player, TileEntityGenerator tileentity) {
        super(new ContainerGenerator(player, tileentity));
        this.tileentity = tileentity;
        this.xSize = 175;
        this.ySize = 151;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        progressBar.draw(mc);
        fontRenderer.drawString(tileentity.hasCustomName() ? tileentity.getName() :"Generator", 5, 5, Color.darkGray.getRGB());
        fontRenderer.drawString("Generated RF/t: " + tileentity.getField(1), 5, 16, Color.white.getRGB());
        fontRenderer.drawString("Storage Tier: " + tileentity.getField(2), 5, 25, Color.white.getRGB());
        fontRenderer.drawString("Speed Tier: " + tileentity.getField(3), 5, 34, Color.white.getRGB());
        List<String> hoveringText = new ArrayList<>();

        if (isInRect(8, 56, 167, 62, mouseX, mouseY)) {
            String text = rfMsg;
            text = text.replace("%c", String.valueOf(tileentity.getField(0)));
            text = text.replace("%m", String.valueOf(tileentity.getField(4)));
            text = text.replace("*", TextFormatting.GREEN + "RF" + TextFormatting.WHITE);
            hoveringText.add(TextFormatting.WHITE+"RF Stored: ");
            hoveringText.add(text);
        }
        if (!hoveringText.isEmpty()) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (progressBar == null) {
            progressBar = new ProgressBar(TEXTURE, ProgressBar.ProgressBarDirection.LEFT_TO_RIGHT,
                    160, 7, 8, 56, 2, 154);
        }
        progressBar.setMax(tileentity.getField(4)).setMin(tileentity.getField(0));
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

    public boolean isInRect(int upperX, int upperY, int lowerX, int lowerY, int mouseX, int mouseY) {
        int actualMouseX = mouseX - ((this.width - this.xSize) / 2);
        int actualMouseY = mouseY - ((this.height - this.ySize) / 2);
        return ((actualMouseX >= upperX && actualMouseX <= lowerX) && (actualMouseY >= upperY && actualMouseY <= lowerY));
    }
}
