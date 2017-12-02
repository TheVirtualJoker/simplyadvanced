package com.joker.simplyadvanced.client.gui;

import com.joker.simplyadvanced.client.containers.ContainerKiln;
import com.joker.simplyadvanced.common.lib.References;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityKiln;
import com.joker.simplyadvanced.client.utils.ProgressBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiKiln extends GuiContainer {

    private static final ResourceLocation TEXTURE = new ResourceLocation(References.MODID + ":textures/gui/container/kiln.png");
    private TileEntityKiln tileentity;
    private ProgressBar progressBar;
    private ProgressBar energyBar;

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
        energyBar.draw(mc);
        fontRenderer.drawString(tileentity.hasCustomName() ? tileentity.getName() :"Kiln", 5, 5, Color.darkGray.getRGB());
        List<String> hoveringText = new ArrayList<>();
        if (isInRect(8, 65, 167, 71, mouseX, mouseY)) {
            String text = TextFormatting.WHITE+"%c* / %m*";
            text = text.replace("%c", String.valueOf(tileentity.getField(2)));
            text = text.replace("%m", String.valueOf(tileentity.getField(3)));
            text = text.replace("*", TextFormatting.GREEN + "RF" + TextFormatting.WHITE);
            hoveringText.add(TextFormatting.WHITE+"RF Stored: ");
            hoveringText.add(text);

            if (tileentity.getField(4) == 0) {
                hoveringText.add(" ");
                hoveringText.add(TextFormatting.RED+"Not Enough Power");
            }
        }
        if (!hoveringText.isEmpty()) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (progressBar == null)
            progressBar = new ProgressBar(TEXTURE, ProgressBar.ProgressBarDirection.LEFT_TO_RIGHT,
                    24, 17, 81, 28, 176, 0);
        if (energyBar == null)
            energyBar = new ProgressBar(TEXTURE, ProgressBar.ProgressBarDirection.LEFT_TO_RIGHT,
                    160, 7, 8, 65, 8, 169);

        progressBar.setMax(tileentity.getField(1)).setMin(tileentity.getField(0));
        energyBar.setMax(tileentity.getField(3)).setMin(tileentity.getField(2));
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
