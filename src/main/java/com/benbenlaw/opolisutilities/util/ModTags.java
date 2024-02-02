package com.benbenlaw.opolisutilities.util;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static class Blocks {

        public static final TagKey<Block> EMPTY = tag("empty");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(OpolisUtilities.MOD_ID, name));
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }

    }


    public static class Items {

        public static final TagKey<Item> WALLET_ITEM = tag("wallet_item");
        public static final TagKey<Item> UPGRADES = tag("upgrades");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(OpolisUtilities.MOD_ID, name));
        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }

    }
    /*

    public static class Structures {

    //    public static final TagKey<Item> RESOURCE_GENERATOR_BLOCKS = tag("resource_generator_blocks");

        private static TagKey<StructureType<?>> tag(String name) {
            return TagKey.create(Registry.STRUCTURE_TYPE_REGISTRY, new ResourceLocation(OpolisUtilities.MOD_ID, name));
        }

        private static TagKey<StructureType<?>> forgeTag(String name) {
            return TagKey.create(Registry.STRUCTURE_TYPE_REGISTRY, new ResourceLocation("forge", name));
        }

    }

     */


}
