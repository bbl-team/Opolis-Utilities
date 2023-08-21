package com.benbenlaw.opolisutilities.enchantment;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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

        if(!pUser.level().isClientSide()) {
            ServerLevel world = ((ServerLevel) pUser.level());
            EntityType<?> entity = pTarget.getType();

            if(pLevel == 1 && entity.equals(EntityType.WARDEN)) {
                pTarget.hurt(world.getLevel().damageSources().magic(), 100f );
            }

            if(pLevel == 2 && entity.equals(EntityType.WARDEN)) {
                pTarget.hurt(world.getLevel().damageSources().magic(), 200f );
            }

            if(pLevel == 3 && entity.equals(EntityType.WARDEN)) {
                pTarget.hurt(world.getLevel().damageSources().magic(), 300f );
            }

            if(pLevel == 4 && entity.equals(EntityType.WARDEN)) {
                pTarget.hurt(world.getLevel().damageSources().magic(), 400f );
            }

            if(pLevel == 5 && entity.equals(EntityType.WARDEN)) {
                pTarget.hurt(world.getLevel().damageSources().magic(), 500f );
            }

        }
        super.doPostAttack(pUser, pTarget, pLevel);


    }
}
