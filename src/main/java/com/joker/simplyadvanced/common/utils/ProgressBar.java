package com.joker.simplyadvanced.common.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ProgressBar extends Gui {
    private ResourceLocation texture;
    private ProgressBarDirection direction;
    private int positionX, positionY;
    private int width, height;
    private int textureX, textureY;
    private float min = 0, max = 0;

    /**
     *
     * @param texture Location where the GUI texture is
     * @param direction How does the progress bar move
     * @param width How wide is the progress bar
     * @param height How high is the progress bar
     * @param positonX Upper-X coordinate of where the progress bar will be located
     * @param positionY Upper-Y coordinate of where the progress bar will be located
     * @param textureX Upper-X coordinate of where the texture is located
     * @param textureY Upper-Y coordinate of where the texture is located
     */
    public ProgressBar(ResourceLocation texture,
                       ProgressBarDirection direction,
                       int width, int height,
                       int positonX, int positionY,
                       int textureX, int textureY) {
        this.texture = texture;
        this.direction = direction;
        this.width = width;
        this.height = height;
        this.positionX = positonX;
        this.positionY = positionY;
        this.textureX = textureX;
        this.textureY = textureY;
    }
 
    public ProgressBar setMin(int min) {
        this.min = min;
        return this;
    }
 
    public ProgressBar setMax(int max) {
        this.max = max;
        return this;
    }

    @Override
    public String toString() {
        return "ProgressBar[min=" + min + ",max=" + max + "]";
    }

    private int getAdjustedWidth() {
        return (int) (min != 0 && max != 0 ? min / max * width : 0);
    }
 
    private int getAdjustedHeight() {
        return (int) (min != 0 && max != 0 ? min / max * height : 0);
    }
 
    public void draw(Minecraft mc) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        switch (direction) {
        case DIAGONAL_UP_LEFT:
            this.drawTexturedModalRect(positionX, positionY, textureX, textureY, width, height);
            this.drawTexturedModalRect(positionX, positionY, positionX, positionY, width - getAdjustedWidth(),
                    height - getAdjustedHeight());
            break;
        case DIAGONAL_UP_RIGHT:
            this.drawTexturedModalRect(positionX, positionY, textureX, textureY, width, height);
            this.drawTexturedModalRect(positionX + getAdjustedWidth(), positionY, positionX + getAdjustedWidth(),
                    positionY, width - getAdjustedWidth(), height - getAdjustedHeight());
            break;
        case DIAGONAL_DOWN_LEFT:
            this.drawTexturedModalRect(positionX, positionY, textureX, textureY, width, height);
            this.drawTexturedModalRect(positionX, positionY + getAdjustedHeight(), positionX,
                    positionY + getAdjustedHeight(), width - getAdjustedWidth(), height - getAdjustedHeight());
            break;
        case DIAGONAL_DOWN_RIGHT:
            this.drawTexturedModalRect(positionX, positionY, textureX, textureY, getAdjustedWidth(),
                    getAdjustedHeight());
            break;
        case DOWN_TO_UP:
            this.drawTexturedModalRect(positionX, positionY, textureX, textureY, width, height);
            this.drawTexturedModalRect(positionX, positionY, positionX, positionY, width, height - getAdjustedHeight());
            break;
        case LEFT_TO_RIGHT:
            this.drawTexturedModalRect(positionX, positionY, textureX, textureY, getAdjustedWidth(), height);
            break;
        case RIGHT_TO_LEFT:
            this.drawTexturedModalRect(positionX, positionY, textureX, textureY, width, height);
            this.drawTexturedModalRect(positionX, positionY, positionX, positionY, width - getAdjustedWidth(), height);
            break;
        case UP_TO_DOWN:
            this.drawTexturedModalRect(positionX, positionY, textureX, textureY, width, getAdjustedHeight());
            break;
        default:
            this.drawTexturedModalRect(positionX, positionY, textureX, textureY, width, height);
            break;
        }
    }
 
    public enum ProgressBarDirection {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT,
        UP_TO_DOWN,
        DOWN_TO_UP,
        DIAGONAL_UP_RIGHT,
        DIAGONAL_UP_LEFT,
        DIAGONAL_DOWN_RIGHT,
        DIAGONAL_DOWN_LEFT
    }
}