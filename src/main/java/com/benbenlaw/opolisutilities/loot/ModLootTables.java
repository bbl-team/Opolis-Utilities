package com.benbenlaw.opolisutilities.loot;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collections;
import java.util.Set;

public class ModLootTables {

    private static ResourceKey<LootTable> register(String string) {
        return register(ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, string));
    }

    private static final Set<ResourceKey<LootTable>> LOCATIONS = Sets.newHashSet();

    //LOOT TABLES//
    public static final ResourceKey<LootTable> BASIC_LOOT_BOX = register("loot_boxes/basic_loot_box");



    private static final Set<ResourceKey<LootTable>> IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);

    private static ResourceKey<LootTable> register(ResourceLocation id) {
        return register(ResourceKey.create(Registries.LOOT_TABLE, id));
    }
    private static ResourceKey<LootTable> register(ResourceKey<LootTable> id) {
        if (LOCATIONS.add(id)) {
            return id;
        } else {
            throw new IllegalArgumentException(id + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceKey<LootTable>> allBuiltin() {
        return IMMUTABLE_LOCATIONS;
    }

}
