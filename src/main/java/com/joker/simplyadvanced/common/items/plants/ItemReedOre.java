package com.joker.simplyadvanced.common.items.plants;

import com.joker.simplyadvanced.common.init.ModBlocks;
import com.joker.simplyadvanced.common.utils.SATab;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRedstone;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemReedOre extends Item {
    public ItemReedOre(){
        this.setRegistryName("reed_ore");
        this.setUnlocalizedName("reed_ore");
        this.setCreativeTab(SATab.TAB);
    }


    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        itemStack.shrink(1);

        worldIn.u

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
