package com.benbenlaw.opolisutilities.block.entity;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.entity.custom.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OpolisUtilities.MOD_ID);

    public static final RegistryObject<BlockEntityType<CrafterBlockEntity>> CRAFTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("crafter_block_entity", () ->
                    BlockEntityType.Builder.of(CrafterBlockEntity::new,
                            ModBlocks.CRAFTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockPlacerBlockEntity>> BLOCK_PLACER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("block_placer_block_entity", () ->
                    BlockEntityType.Builder.of(BlockPlacerBlockEntity::new,
                            ModBlocks.BLOCK_PLACER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockBreakerBlockEntity>> BLOCK_BREAKER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("block_breaker_block_entity", () ->
                    BlockEntityType.Builder.of(BlockBreakerBlockEntity::new,
                            ModBlocks.BLOCK_BREAKER.get()).build(null));

    public static final RegistryObject<BlockEntityType<DryingTableBlockEntity>> DRYING_TABLE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("drying_table_block_entity", () ->
                    BlockEntityType.Builder.of(DryingTableBlockEntity::new,
                            ModBlocks.DRYING_TABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<ItemRepairerBlockEntity>> ITEM_REPAIRER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("item_repairer_block_entity", () ->
                    BlockEntityType.Builder.of(ItemRepairerBlockEntity::new,
                            ModBlocks.ITEM_REPAIRER.get()).build(null));

    public static final RegistryObject<BlockEntityType<ResourceGeneratorBlockEntity>> RESOURCE_GENERATOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("resource_generator_block_entity", () ->
                    BlockEntityType.Builder.of(ResourceGeneratorBlockEntity::new,
                            ModBlocks.RESOURCE_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<ResourceGenerator2BlockEntity>> RESOURCE_GENERATOR_2_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("resource_generator_2_block_entity", () ->
                    BlockEntityType.Builder.of(ResourceGenerator2BlockEntity::new,
                            ModBlocks.RESOURCE_GENERATOR_2.get()).build(null));

    public static final RegistryObject<BlockEntityType<FluidGeneratorBlockEntity>> FLUID_GENERATOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("fluid_generator_block_entity", () ->
                    BlockEntityType.Builder.of(FluidGeneratorBlockEntity::new,
                            ModBlocks.FLUID_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<RedstoneClockBlockEntity>> REDSTONE_CLOCK_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("redstone_clock_block_entity", () ->
                    BlockEntityType.Builder.of(RedstoneClockBlockEntity::new,
                            ModBlocks.REDSTONE_CLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<FanBlockEntity>> FAN_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("fan_block_entity", () ->
                    BlockEntityType.Builder.of(FanBlockEntity::new,
                            ModBlocks.FAN.get()).build(null));



    public static final RegistryObject<BlockEntityType<EnderScramblerBlockEntity>> ENDER_SCRAMBLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ender_scrambler_block_entity", () ->
                    BlockEntityType.Builder.of(EnderScramblerBlockEntity::new,
                            ModBlocks.ENDER_SCRAMBLER.get()).build(null));




    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
