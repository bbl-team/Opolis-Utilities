package com.benbenlaw.opolisutilities.datagen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class OpolisUtilitiesBlockTags extends BlockTagsProvider {

    OpolisUtilitiesBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, OpolisUtilities.MOD_ID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        //Ores
        tag(ModTags.Blocks.ENDER_ORE).add(ModBlocks.ENDER_ORE.get(), ModBlocks.DEEPSLATE_ENDER_ORE.get());
        tag(Tags.Blocks.ORES).add(ModBlocks.ENDER_ORE.get(), ModBlocks.DEEPSLATE_ENDER_ORE.get());

        //Axe
        tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.DRYING_TABLE.get());

        //Pickaxe
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.ENDER_ORE.get())
                .add(ModBlocks.DEEPSLATE_ENDER_ORE.get())
                .add(ModBlocks.SUMMONING_BLOCK.get())
                .add(ModBlocks.RESOURCE_GENERATOR.get())
                .add(ModBlocks.REDSTONE_CLOCK.get())
                .add(ModBlocks.FLUID_GENERATOR.get())
                .add(ModBlocks.ENDER_SCRAMBLER.get())
                .add(ModBlocks.BLOCK_PLACER.get())
                .add(ModBlocks.BLOCK_BREAKER.get())
                .add(ModBlocks.ITEM_REPAIRER.get())
                .add(ModBlocks.CRAFTER.get())
                .add(ModBlocks.CATALOGUE.get())
                .add(ModBlocks.CLOCHE.get())
        ;

        //Need Iron Tool
        tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.ENDER_ORE.get(), ModBlocks.DEEPSLATE_ENDER_ORE.get());

        //Banned In Block Placer
        tag(ModTags.Blocks.BANNED_IN_BLOCK_PLACER)
                .add(Blocks.REDSTONE_TORCH)
                .add(Blocks.TORCH)
                .add(Blocks.REDSTONE_WIRE)
                .add(Blocks.SOUL_TORCH)
                .addTag(BlockTags.BUTTONS)
                .addTag(BlockTags.DOORS)
                .addTag(BlockTags.TRAPDOORS)
                .addTag(BlockTags.PRESSURE_PLATES)
                .addTag(BlockTags.PRESSURE_PLATES)
                .add(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .add(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)
       ;

    }

    @Override
    public @NotNull String getName() {
        return OpolisUtilities.MOD_ID + " Block Tags";
    }
}
