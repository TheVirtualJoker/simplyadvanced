package com.joker.simplyadvanced.client.utils;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

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

    /**
     * Adds a line to the input items Lore (mostly used for the Kiln so far)
     *
     * @param input | ItemStack that you want to add the line to
     * @param line | Text that will be added to the Items Lore
     * @return | input with the updated Lore
     */
    public static ItemStack addLoreLine (ItemStack input, String line) {
        NBTTagCompound nbt = input.getTagCompound();
        if (nbt != null) {
            NBTTagCompound disp = nbt.getCompoundTag("display");
            if (disp != null) {
                NBTTagList lore = disp.getTagList("Lore", 9);
                if (lore == null) lore = new NBTTagList();
                lore.appendTag(new NBTTagString(line));

                disp.setTag("Lore", lore);
            }
            input.setTagInfo("display", disp);
        }

        return input;
    }
}
