package com.benbenlaw.opolisutilities.block;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(OpolisUtilities.MOD_ID);


    public static final DeferredBlock<Block> FLOATING_BLOCK = registerBlock(
    );

    public static final DeferredBlock<Block> DRYING_TABLE = registerBlock(
    );

    public static final DeferredBlock<Block> CRAFTER = registerBlock(
    );

    public static final DeferredBlock<Block> BLOCK_PLACER = registerBlock(
    );

    public static final DeferredBlock<Block> BLOCK_BREAKER = registerBlock(
    );

    public static final DeferredBlock<Block> ITEM_REPAIRER = registerBlock(
    );

    public static final DeferredBlock<Block> RESOURCE_GENERATOR = registerBlock(
    );


    public static final DeferredBlock<Block> RESOURCE_GENERATOR_2 = registerBlock(
    );


    public static final DeferredBlock<Block> FLUID_GENERATOR = registerBlock(
    );

    public static final DeferredBlock<Block> CATALOGUE = registerBlock(
    );

    public static final DeferredBlock<Block> REDSTONE_CLOCK = registerBlock(
    );

    /*

    public static final DeferredBlock<Block> ENDER_ORE = registerBlock(() -> new EnderOreBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(3.0f,3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)),
            "ender_ore");

    public static final DeferredBlock<Block> DEEPSLATE_ENDER_ORE = registerBlock(() -> new EnderOreBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(4.5f,3.0f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.DEEPSLATE)
            .lightLevel(litBlockEmission(9)),
            UniformInt.of(2, 4)),
            "deepslate_ender_ore");

     */

    public static final DeferredBlock<Block> ENDER_SCRAMBLER = registerBlock(
    );

    public static final DeferredBlock<Block> FAN = registerBlock(
    );




    public static DeferredBlock<Block> registerBlock(
            String name, Supplier<Block> block) {
        DeferredBlock<Block> blockReg = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(blockReg.get(), new Item.Properties()));
        return blockReg;
    }



}