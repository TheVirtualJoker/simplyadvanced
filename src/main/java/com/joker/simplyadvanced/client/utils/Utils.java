package com.joker.simplyadvanced.client.utils;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class Utils {
    private static final Random random = new Random();

    public static float randomFloat(float start, float end) {
        return start + random.nextFloat() * (end - start);
    }

    public static int percent(double min, double max) {
        double percent = ((min / max)) * 100;
        return (int) Math.round(Math.abs(percent));
    }

    public static boolean mouseInRegion(GuiContainer container, int upperX, int upperY, int lowerX, int lowerY, int mouseX, int mouseY) {
        int actualMouseX = mouseX - ((container.width - container.getXSize()) / 2);
        int actualMouseY = mouseY - ((container.height - container.getYSize()) / 2);
        return ((actualMouseX >= upperX && actualMouseX <= lowerX) && (actualMouseY >= upperY && actualMouseY <= lowerY));
    }

    /**
     * I put this method here just in case we needed it later on for different class files.
     */
    public static ItemStack modifySize (ItemStack input, int count) {
        ItemStack copy = input.copy();
        copy.setCount(count);
        return copy;
    }
}
