package com.benbenlaw.opolisutilities.enchantment;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class WardenSmiteEnchantment extends Enchantment {
    public WardenSmiteEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public void doPostAttack(LivingEntity pUser, Entity pTarget, int pLevel) {

        if(!pUser.level.isClientSide()) {
            ServerLevel world = ((ServerLevel) pUser.level);
            EntityType<?> entity = pTarget.getType();

            if(pLevel == 1 && entity.equals(EntityType.WARDEN)) {
                pTarget.hurt(DamageSource.MAGIC, 100);
            }

            if(pLevel == 2 && entity.equals(EntityType.WARDEN)) {
                pTarget.hurt(DamageSource.MAGIC, 200);
            }

            if(pLevel == 3 && entity.equals(EntityType.WARDEN)) {
                pTarget.hurt(DamageSource.MAGIC, 300);
            }

            if(pLevel == 4 && entity.equals(EntityType.WARDEN)) {
                pTarget.hurt(DamageSource.MAGIC, 400);
            }

            if(pLevel == 5 && entity.equals(EntityType.WARDEN)) {
                pTarget.hurt(DamageSource.MAGIC, 500);
            }

        }
        super.doPostAttack(pUser, pTarget, pLevel);
    }
}
