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


        public static final TagKey<Block> MINEABLE_WITH_HAMMER = tag("mineable_with_hammer");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(OpolisUtilities.MOD_ID, name));
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }

    }




    public static class Items {
   //     public static final TagKey<Item> SPACE_SUIT_HELMET = tag("space_suit_helmet");
        public static final TagKey<Item> HAMMER = tag("hammer");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(OpolisUtilities.MOD_ID, name));
        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }

    }

}
