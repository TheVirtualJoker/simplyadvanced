package com.joker.simplyadvanced.client.gui;

import com.joker.simplyadvanced.client.containers.ContainerCentrifuge;
import com.joker.simplyadvanced.common.lib.References;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityCentrifuge;
import com.joker.simplyadvanced.client.utils.ProgressBar;
import com.joker.simplyadvanced.client.utils.Utils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiCentrifuge extends GuiContainer {
    private static final ResourceLocation CENTRIFUGE_TEXTURE = new ResourceLocation(References.MODID + ":textures/gui/container/centrifuge.png");
    private final InventoryPlayer player;
    private TileEntityCentrifuge tileentity;
    private ProgressBar energyBar, arrow1, arrow2, arrow3, arrow4;

    public GuiCentrifuge(InventoryPlayer player, TileEntityCentrifuge tileentity) {
        super(new ContainerCentrifuge(player, tileentity));
        this.player = player;
        this.tileentity = tileentity;
        this.xSize = 175;
        this.ySize = 164;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(CENTRIFUGE_TEXTURE);
        energyBar.draw(mc);
        if (tileentity.isSpining()) {
            arrow1.draw(mc);
            arrow2.draw(mc);
            arrow3.draw(mc);
            arrow4.draw(mc);
        }


        fontRenderer.drawString(tileentity.hasCustomName() ? tileentity.getName() : "Centrifuge", 22, 5, Color.white.getRGB());

        if (Utils.mouseInRegion(this, 9, 6, 18, 78, mouseX, mouseY)) {
            List<String> hoveringText = new ArrayList<>();
            String text = TextFormatting.GOLD + "RF: " + TextFormatting.WHITE + "%c / %m";
            text = text.replace("%c", String.valueOf(tileentity.getField(2)));
            text = text.replace("%m", String.valueOf(tileentity.getField(3)));
            hoveringText.add(text);
            hoveringText.add("Percent Filled: " + Utils.percent(tileentity.getField(2), tileentity.getField(3)) + "%");

            if (tileentity.getField(4) == 0) {
                hoveringText.add(" ");
                hoveringText.add(TextFormatting.RED + "Not Enough Power");
            }
            drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);

        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (energyBar == null)
            energyBar = new ProgressBar(CENTRIFUGE_TEXTURE, ProgressBar.ProgressBarDirection.DOWN_TO_UP,
                    10, 73, 9, 6, 176, 25);
        if (arrow1 == null)
            arrow1 = new ProgressBar(CENTRIFUGE_TEXTURE, ProgressBar.ProgressBarDirection.RIGHT_TO_LEFT,
                    10, 10, 68, 37, 186, 15);
        if (arrow2 == null)
            arrow2 = new ProgressBar(CENTRIFUGE_TEXTURE, ProgressBar.ProgressBarDirection.DOWN_TO_UP,
                    10, 10, 83, 22, 201, 0);
        if (arrow3 == null)
            arrow3 = new ProgressBar(CENTRIFUGE_TEXTURE, ProgressBar.ProgressBarDirection.LEFT_TO_RIGHT,
                    10, 10, 98, 37, 216, 15);
        if (arrow4 == null)
            arrow4 = new ProgressBar(CENTRIFUGE_TEXTURE, ProgressBar.ProgressBarDirection.UP_TO_DOWN,
                    10, 10, 83, 52, 201, 30);
        energyBar.setMin(tileentity.getField(2)).setMax(tileentity.getField(3));
        arrow1.setMin(tileentity.getField(0)).setMax(tileentity.getField(1));
        arrow2.setMin(tileentity.getField(0)).setMax(tileentity.getField(1));
        arrow3.setMin(tileentity.getField(0)).setMax(tileentity.getField(1));
        arrow4.setMin(tileentity.getField(0)).setMax(tileentity.getField(1));
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(CENTRIFUGE_TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
