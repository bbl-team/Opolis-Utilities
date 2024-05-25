package com.benbenlaw.opolisutilities.block.entity;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.entity.custom.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, OpolisUtilities.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrafterBlockEntity>> CRAFTER_BLOCK_ENTITY =
            register("crafter_block_entity", () ->
                    BlockEntityType.Builder.of(CrafterBlockEntity::new, ModBlocks.CRAFTER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockPlacerBlockEntity>> BLOCK_PLACER_BLOCK_ENTITY =
            register("block_placer_block_entity", () ->
                    BlockEntityType.Builder.of(BlockPlacerBlockEntity::new, ModBlocks.BLOCK_PLACER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockBreakerBlockEntity>> BLOCK_BREAKER_BLOCK_ENTITY =
            register("block_breaker_block_entity", () ->
                    BlockEntityType.Builder.of(BlockBreakerBlockEntity::new, ModBlocks.BLOCK_BREAKER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DryingTableBlockEntity>> DRYING_TABLE_BLOCK_ENTITY =
            register("drying_table_block_entity", () ->
                    BlockEntityType.Builder.of(DryingTableBlockEntity::new, ModBlocks.DRYING_TABLE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ItemRepairerBlockEntity>> ITEM_REPAIRER_BLOCK_ENTITY =
            register("item_repairer_block_entity", () ->
                    BlockEntityType.Builder.of(ItemRepairerBlockEntity::new, ModBlocks.ITEM_REPAIRER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ResourceGeneratorBlockEntity>> RESOURCE_GENERATOR_BLOCK_ENTITY =
            register("resource_generator_block_entity", () ->
                    BlockEntityType.Builder.of(ResourceGeneratorBlockEntity::new, ModBlocks.RESOURCE_GENERATOR.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FluidGeneratorBlockEntity>> FLUID_GENERATOR_BLOCK_ENTITY =
            register("fluid_generator_block_entity", () ->
                    BlockEntityType.Builder.of(FluidGeneratorBlockEntity::new, ModBlocks.FLUID_GENERATOR.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RedstoneClockBlockEntity>> REDSTONE_CLOCK_BLOCK_ENTITY =
            register("redstone_clock_block_entity", () ->
                    BlockEntityType.Builder.of(RedstoneClockBlockEntity::new, ModBlocks.REDSTONE_CLOCK.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FanBlockEntity>> FAN_BLOCK_ENTITY =
            register("fan_block_entity", () ->
                    BlockEntityType.Builder.of(FanBlockEntity::new, ModBlocks.FAN.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnderScramblerBlockEntity>> ENDER_SCRAMBLER_BLOCK_ENTITY =
            register("ender_scrambler_block_entity", () ->
                    BlockEntityType.Builder.of(EnderScramblerBlockEntity::new, ModBlocks.ENDER_SCRAMBLER.get()));


    //Capability Registration (Item Handler)

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.BLOCK_BREAKER_BLOCK_ENTITY.get(), BlockBreakerBlockEntity::getItemHandlerCapability);

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.BLOCK_PLACER_BLOCK_ENTITY.get(), BlockPlacerBlockEntity::getItemHandlerCapability);
        
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.RESOURCE_GENERATOR_BLOCK_ENTITY.get(), ResourceGeneratorBlockEntity::getItemHandlerCapability);

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.DRYING_TABLE_BLOCK_ENTITY.get(), DryingTableBlockEntity::getItemHandlerCapability);

    }



    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(@Nonnull String name, @Nonnull Supplier<BlockEntityType.Builder<T>> initializer) {
        return BLOCK_ENTITIES.register(name, () -> initializer.get().build(null));
    }
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
