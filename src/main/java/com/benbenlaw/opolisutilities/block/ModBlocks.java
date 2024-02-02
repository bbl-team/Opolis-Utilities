package com.benbenlaw.opolisutilities.block;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.custom.*;
import com.benbenlaw.opolisutilities.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, OpolisUtilities.MOD_ID);

    public static final RegistryObject<Block> FLOATING_BLOCK = registerBlockWithoutBlockItem("floating_block_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .instabreak()));

    public static final RegistryObject<Block> DRYING_TABLE = registerBlock("drying_table",
            () -> new DryingTableBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final RegistryObject<Block> BLOCK_PLACER = registerBlock("block_placer",
            () -> new BlockPlacerBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final RegistryObject<Block> BLOCK_BREAKER = registerBlock("block_breaker",
            () -> new BlockBreakerBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final RegistryObject<Block> ITEM_REPAIRER = registerBlock("item_repairer",
            () -> new ItemRepairerBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));
    
    public static final RegistryObject<Block> RESOURCE_GENERATOR = registerBlock("resource_generator",
            () -> new ResourceGeneratorBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final RegistryObject<Block> RESOURCE_GENERATOR_2 = registerBlock("resource_generator_2",
            () -> new ResourceGenerator2Block(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final RegistryObject<Block> FLUID_GENERATOR = registerBlock("fluid_generator",
            () -> new FluidGeneratorBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final RegistryObject<Block> CATALOGUE = registerBlock("catalogue",
            () -> new CatalogueBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));

    public static final RegistryObject<Block> REDSTONE_CLOCK = registerBlock("redstone_clock",
            () -> new RedstoneClockBlock(BlockBehaviour.Properties.of()
                    .strength(2.0f,2.0f)
                    .noOcclusion()));


    public static final RegistryObject<Block> ENDER_ORE = registerBlock("ender_ore",
            () -> new EnderOreBlock(BlockBehaviour.Properties.of()
                    .strength(3.0f,3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
                    .lightLevel(litBlockEmission(9)),
                    UniformInt.of(2, 4)));

    public static final RegistryObject<Block> DEEPSLATE_ENDER_ORE = registerBlock("deepslate_ender_ore",
            () -> new EnderOreBlock(BlockBehaviour.Properties.of()
                    .strength(4.5f,3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE)
                    .lightLevel(litBlockEmission(9)),
                    UniformInt.of(2, 4)));

    public static final RegistryObject<Block> ENDER_SCRAMBLER = registerBlock("ender_scrambler",
            () -> new EnderScramblerBlock(BlockBehaviour.Properties.of()
                    .strength(4.5f,3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> FAN = registerBlock("fan",
            () -> new FanBlock(BlockBehaviour.Properties.of()
                    .strength(4.5f,3.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));














    //Light Level When Interacted With

    private static ToIntFunction<BlockState> litBlockEmission(int p_50760_) {
        return (p_50763_) -> p_50763_.getValue(BlockStateProperties.LIT) ? p_50760_ : 0;
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}