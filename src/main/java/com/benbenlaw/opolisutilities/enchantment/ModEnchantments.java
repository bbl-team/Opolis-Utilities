package com.benbenlaw.opolisutilities.enchantment;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(BuiltInRegistries.ENCHANTMENT, OpolisUtilities.MOD_ID);

    public static final DeferredHolder<Enchantment, Enchantment> WARDEN_SMITE =
            ENCHANTMENTS.register("warden_smite", () -> new WardenSmiteEnchantment(
                    Enchantment.definition(
                            ItemTags.SWORD_ENCHANTABLE,
                            3,
                            5,
                            Enchantment.dynamicCost(1, 10),
                            Enchantment.dynamicCost(20, 10),
                            5,
                            EquipmentSlot.MAINHAND
                    )

            ));

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}