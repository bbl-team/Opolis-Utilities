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

    public static final RegistryObject<BlockEntityType<RedstoneClockBlockEntity>> REDSTONE_CLOCK_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("redstone_clock_block_entity", () ->
                    BlockEntityType.Builder.of(RedstoneClockBlockEntity::new,
                            ModBlocks.REDSTONE_CLOCK.get()).build(null));




    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
