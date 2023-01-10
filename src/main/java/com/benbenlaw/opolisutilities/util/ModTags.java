package com.benbenlaw.opolisutilities.util;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.integration.InformationJEI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {



    public static class Blocks {


//        public static final TagKey<Block> MINEABLE_WITH_HAMMER = tag("mineable_with_hammer");
        public static final TagKey<Block> RESOURCE_GENERATOR_BLOCKS = tag("resource_generator_blocks");
        public static final TagKey<Block> RESOURCE_GENERATOR_SPEED_BLOCKS_1 = tag("resource_generator_speed_blocks_1");
        public static final TagKey<Block> RESOURCE_GENERATOR_SPEED_BLOCKS_2 = tag("resource_generator_speed_blocks_2");
        public static final TagKey<Block> RESOURCE_GENERATOR_SPEED_BLOCKS_3 = tag("resource_generator_speed_blocks_3");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(OpolisUtilities.MOD_ID, name));
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }

    }




    public static class Items {

        public static final TagKey<Item> RESOURCE_GENERATOR_BLOCKS = tag("resource_generator_blocks");
        public static final TagKey<Item> RESOURCE_GENERATOR_SPEED_BLOCKS_1 = tag("resource_generator_speed_blocks_1");
        public static final TagKey<Item> RESOURCE_GENERATOR_SPEED_BLOCKS_2 = tag("resource_generator_speed_blocks_2");
        public static final TagKey<Item> RESOURCE_GENERATOR_SPEED_BLOCKS_3 = tag("resource_generator_speed_blocks_3");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(OpolisUtilities.MOD_ID, name));



        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }

    }

}
