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

        //OpolisUtilities Tags
        public static final TagKey<Block> BANNED_IN_BLOCK_PLACER = tag("banned_in_block_placer");


        //Common Tags
        public static final TagKey<Block> ENDER_ORE = commonTags("ores/ender_ore");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, name));
        }

        private static TagKey<Block> commonTags(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }

    }


    public static class Items {

        //OpolisUtilities Item Tags
        public static final TagKey<Item> BANNED_IN_BLOCK_PLACER = tag("banned_in_block_placer");
        public static final TagKey<Item> BANNED_IN_ITEM_REPAIRER = tag("banned_in_item_repairer");


        //Common Tags
        public static final TagKey<Item> ENDER_ORE = commonTags("ores/ender_ore");
        public static final TagKey<Item> NUGGETS_COPPER = commonTags("nuggets/copper");


        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, name));
        }

        private static TagKey<Item> commonTags(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }

    }


}
