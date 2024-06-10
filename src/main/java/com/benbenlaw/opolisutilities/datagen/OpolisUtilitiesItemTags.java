package com.benbenlaw.opolisutilities.datagen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class OpolisUtilitiesItemTags extends ItemTagsProvider {

    OpolisUtilitiesItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags.contentsGetter(), OpolisUtilities.MOD_ID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        //Ores
        tag(ModTags.Items.ENDER_ORE).add(ModBlocks.ENDER_ORE.get().asItem(), ModBlocks.DEEPSLATE_ENDER_ORE.get().asItem());
        tag(Tags.Items.ORES).add(ModBlocks.ENDER_ORE.get().asItem(), ModBlocks.DEEPSLATE_ENDER_ORE.get().asItem());

        //Banned In Block Placer
        tag(ModTags.Items.BANNED_IN_BLOCK_PLACER)
                .add(Blocks.REDSTONE_TORCH.asItem())
                .add(Blocks.TORCH.asItem())
                .add(Blocks.REDSTONE_WIRE.asItem())
                .add(Blocks.SOUL_TORCH.asItem())
                .addTag(ItemTags.BUTTONS)
                .addTag(ItemTags.DOORS)
                .addTag(ItemTags.TRAPDOORS)
                .addTag(ItemTags.WOODEN_PRESSURE_PLATES)
                .add(Items.POLISHED_BLACKSTONE_PRESSURE_PLATE)
                .add(Items.STONE_PRESSURE_PLATE)
                .add(Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .add(Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
       ;

        //Banned In Item Repairer (Just to crete the tag)
        tag(ModTags.Items.BANNED_IN_ITEM_REPAIRER)
                .add(Items.WOODEN_AXE)
        ;

        //Nugget
        tag(Tags.Items.NUGGETS).add(ModItems.COPPER_NUGGET.get());
        tag(ModTags.Items.NUGGETS_COPPER).add(ModItems.COPPER_NUGGET.get());

        //Shears
        tag(Tags.Items.TOOLS_SHEARS).add(ModItems.WOODEN_SHEARS.get());


    }

    @Override
    public @NotNull String getName() {
        return OpolisUtilities.MOD_ID + " Item Tags";
    }
}
