package com.benbenlaw.opolisutilities.block;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.custom.*;
import com.benbenlaw.opolisutilities.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.awt.*;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(OpolisUtilities.MOD_ID);

    public static final DeferredBlock<Block> FLOATING_BLOCK = registerBlockWithoutBlockItem("floating_block_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .instabreak()));

    public static final DeferredBlock<Block> DRYING_TABLE = registerBlock("drying_table",
            () -> new DryingTableBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final DeferredBlock<Block> CRAFTER = registerBlock("crafter",
            () -> new CrafterBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final DeferredBlock<Block> BLOCK_PLACER = registerBlock("block_placer",
            () -> new BlockPlacerBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final DeferredBlock<Block> BLOCK_BREAKER = registerBlock("block_breaker",
            () -> new BlockBreakerBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final DeferredBlock<Block> ITEM_REPAIRER = registerBlock("item_repairer",
            () -> new ItemRepairerBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final DeferredBlock<Block> RESOURCE_GENERATOR = registerBlock("resource_generator",
            () -> new ResourceGeneratorBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final DeferredBlock<Block> FLUID_GENERATOR = registerBlock("fluid_generator",
            () -> new FluidGeneratorBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)

                    .noOcclusion()));

    public static final DeferredBlock<Block> CATALOGUE = registerBlock("catalogue",
            () -> new CatalogueBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));
    public static final DeferredBlock<Block> SUMMONING_BLOCK = registerBlock("summoning_block",
            () -> new SummoningBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));
    public static final DeferredBlock<Block> CLOCHE = registerBlock("cloche",
            () -> new ClocheBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final DeferredBlock<Block> REDSTONE_CLOCK = registerBlock("redstone_clock",
            () -> new RedstoneClockBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final DeferredBlock<Block> ENDER_ORE = registerBlock("ender_ore",
            () -> new EnderOreBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f,3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
                    .lightLevel(litBlockEmission())));

    public static final DeferredBlock<Block> DEEPSLATE_ENDER_ORE = registerBlock("deepslate_ender_ore",
            () -> new EnderOreBlock(BlockBehaviour.Properties.of()
                    .strength(4.5f,3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE)
                    .lightLevel(litBlockEmission())));

    public static final DeferredBlock<Block> ENDER_SCRAMBLER = registerBlock("ender_scrambler",
            () -> new EnderScramblerBlock(BlockBehaviour.Properties.of()
                    .strength(4.5f,3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));


    //Light Level When Interacted With

    private static ToIntFunction<BlockState> litBlockEmission() {
        return (lightLevel) -> lightLevel.getValue(BlockStateProperties.LIT) ? 9 : 0;
    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}