package com.joker.simplyadvanced.client.gui;

import com.joker.simplyadvanced.client.containers.ContainerKiln;
import com.joker.simplyadvanced.client.utils.ProgressBar;
import com.joker.simplyadvanced.client.utils.Utils;
import com.joker.simplyadvanced.common.config.Config;
import com.joker.simplyadvanced.common.lib.References;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityKiln;
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
    private int energy = 0, maxEnergy;

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
        if (tileentity.isHardening()) progressBar.draw(mc);
        energyBar.draw(mc);
        fontRenderer.drawString(tileentity.hasCustomName() ? tileentity.getName() :"Kiln", 5, 5, Color.darkGray.getRGB());
        energy = tileentity.getField(2);
        maxEnergy = tileentity.getField(3);

        if (Config.TASK_PERCENTAGE && Utils.mouseInRegion(this, 82, 28, 103, 42, mouseX, mouseY)) {
            List<String> hoveringText = new ArrayList<>();
            hoveringText.add(String.valueOf(Utils.percent(tileentity.getField(0), tileentity.getField(1))) + TextFormatting.GREEN + "%");
            drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
        }

        if (Utils.mouseInRegion(this, 8, 65, 167, 71, mouseX, mouseY)) {
            List<String> hoveringText = new ArrayList<>();
            if (Config.POWER_STORED) hoveringText.add(TextFormatting.GREEN+"RF: " + TextFormatting.WHITE + energy + " / " + maxEnergy);
            if (Config.POWER_PERCENTAGE) hoveringText.add("Percent Filled: " + Utils.percent(energy, maxEnergy) + TextFormatting.GREEN + "%");

            if (tileentity.getField(4) == 0) {
                if (!hoveringText.isEmpty()) hoveringText.add(" ");
                hoveringText.add(TextFormatting.RED+"Not Enough Power");
            }

            if (!hoveringText.isEmpty()) drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
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
}
