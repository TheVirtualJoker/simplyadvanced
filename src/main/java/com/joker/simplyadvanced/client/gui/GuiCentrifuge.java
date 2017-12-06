package com.joker.simplyadvanced.client.gui;

import com.joker.simplyadvanced.client.containers.ContainerCentrifuge;
import com.joker.simplyadvanced.client.utils.Pair;
import com.joker.simplyadvanced.client.utils.ProgressBar;
import com.joker.simplyadvanced.client.utils.Utils;
import com.joker.simplyadvanced.common.config.Config;
import com.joker.simplyadvanced.common.lib.References;
import com.joker.simplyadvanced.common.tiles.machines.powered.TileEntityCentrifuge;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiCentrifuge extends GuiContainer {
    private static final ResourceLocation CENTRIFUGE_TEXTURE = new ResourceLocation(References.MODID + ":textures/gui/container/centrifuge.png");
    private final InventoryPlayer player;
    private TileEntityCentrifuge tileentity;
    private List<Pair<Integer, Integer>> pairs = new ArrayList<>();
    private ProgressBar energyBar, arrow1, arrow2, arrow3, arrow4;
    private int energy = 0, maxEnergy;

    public GuiCentrifuge(InventoryPlayer player, TileEntityCentrifuge tileentity) {
        super(new ContainerCentrifuge(player, tileentity));
        this.player = player;
        this.tileentity = tileentity;
        this.xSize = 175;
        this.ySize = 164;
        pairs = Arrays.asList(
                new Pair<> (68, 37),
                new Pair<> (83, 22),
                new Pair<> (98, 37),
                new Pair<> (83, 52)
        );
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(CENTRIFUGE_TEXTURE);
        fontRenderer.drawString(tileentity.hasCustomName() ? tileentity.getName() : "Centrifuge", 22, 5, Color.white.getRGB());
        energyBar.draw(mc);
        if (tileentity.isSpining()) {
            arrow1.draw(mc);
            arrow2.draw(mc);
            arrow3.draw(mc);
            arrow4.draw(mc);
        }
        energy = tileentity.getField(2);
        maxEnergy = tileentity.getField(3);

        if (Config.TASK_PERCENTAGE) {
            int percent = 0;
            if (tileentity.getField(0) != 0) percent = Utils.percent(tileentity.getField(0), tileentity.getField(1));

            for (Pair<Integer, Integer> pair : pairs) {
                if (Utils.mouseInRegion(this, pair.getL(), pair.getR(), pair.getL() + 9, pair.getR() + 9, mouseX, mouseY)) {
                    List<String> hoveringText = new ArrayList<>();
                    hoveringText.add(String.valueOf(percent) + TextFormatting.GOLD + "%");
                    drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
                }
            }
        }

        if (Utils.mouseInRegion(this, 9, 6, 18, 78, mouseX, mouseY)) {
            List<String> hoveringText = new ArrayList<>();
            if (Config.POWER_STORED) hoveringText.add(TextFormatting.GOLD+"RF: " + TextFormatting.WHITE + energy + " / " + maxEnergy);
            if (Config.POWER_PERCENTAGE) hoveringText.add("Percent Filled: " + Utils.percent(energy, maxEnergy) + TextFormatting.GOLD + "%");

            if (tileentity.getField(4) == 0) {
                if (!hoveringText.isEmpty()) hoveringText.add(" ");
                hoveringText.add(TextFormatting.RED + "Not Enough Power");
            }
            if (!hoveringText.isEmpty()) drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);

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
