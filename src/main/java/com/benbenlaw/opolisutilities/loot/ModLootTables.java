package com.benbenlaw.opolisutilities.loot;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.Set;

//Thanks foliet

public class ModLootTables {

    private static ResourceLocation register(String string) {
        return register(new ResourceLocation(OpolisUtilities.MOD_ID, string));
    }

    private static final Set<ResourceLocation> LOCATIONS = Sets.newHashSet();
  //  public static final ResourceLocation FLOWER_LOOT_BOX = register("loot_boxes/flower_loot_box");
 //   public static final ResourceLocation SAPLING_LOOT_BOX = register("loot_boxes/sapling_loot_box");
    public static final ResourceLocation BASIC_LOOT_BOX = register("loot_boxes/basic_loot_box");
    public static final ResourceLocation ADVANCED_LOOT_BOX = register("loot_boxes/advanced_loot_box");
    public static final ResourceLocation ELITE_LOOT_BOX = register("loot_boxes/elite_loot_box");
    private static final Set<ResourceLocation> IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);


    private static ResourceLocation register(ResourceLocation location) {
        if (LOCATIONS.add(location)) {
            return location;
        } else {
            throw new IllegalArgumentException(location + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceLocation> all() {
        return IMMUTABLE_LOCATIONS;
    }
}
