package com.joker.simplyadvanced.common.items.tools;

import com.joker.simplyadvanced.common.init.ModItems;
import net.minecraft.item.ItemStack;

public enum SAToolMaterial {
    COPPER(1, 300, 6.5F, 2.0F, 14);

    /**
     * The level of material this tool can harvest (3 = DIAMOND, 2 = IRON, 1 = STONE, 0 = COPPER/GOLD)
     */
    private final int harvestLevel;
    /**
     * The number of uses this material allows. (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32)
     */
    private final int maxUses;
    /**
     * The strength of this tool material against blocks which it is effective against.
     */
    private final float efficiency;
    /**
     * Damage versus entities.
     */
    private final float attackDamage;
    /**
     * Defines the natural enchantability factor of the material.
     */
    private final int enchantability;
    //Added by forge for custom Tool materials.
    private ItemStack repairMaterial = ItemStack.EMPTY;

    SAToolMaterial(int harvestLevel,
                   int maxUses,
                   float efficiency,
                   float damageVsEntity,
                   int enchantability) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = damageVsEntity;
        this.enchantability = enchantability;
    }

    /**
     * The number of uses this material allows. (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32)
     */
    public int getMaxUses() {
        return this.maxUses;
    }

    /**
     * The strength of this tool material against blocks which it is effective against.
     */
    public float getEfficiency() {
        return this.efficiency;
    }

    /**
     * Returns the damage against a given entity.
     */
    public float getAttackDamage() {
        return this.attackDamage;
    }

    /**
     * The level of material this tool can harvest (3 = DIAMOND, 2 = IRON, 1 = STONE, 0 = IRON/GOLD)
     */
    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    /**
     * Return the natural enchantability factor of the material.
     */
    public int getEnchantability() {
        return this.enchantability;
    }

    public net.minecraft.item.Item getRepairItem() {
        if (this == COPPER) {
            return ModItems.COPPER_INGOT;
        }
        return null;
    }

    public SAToolMaterial setRepairItem(ItemStack stack) {
        this.repairMaterial = stack;
        return this;
    }

    public ItemStack getRepairItemStack() {
        if (!repairMaterial.isEmpty()) return repairMaterial;
        net.minecraft.item.Item ret = this.getRepairItem();
        if (ret != null)
            repairMaterial = new ItemStack(ret, 1, net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE);
        return repairMaterial;
    }
}