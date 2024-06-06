package com.benbenlaw.opolisutilities.block.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.FluidGeneratorBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.ResourceGeneratorBlockEntity;
import com.benbenlaw.opolisutilities.screen.FluidGeneratorMenu;
import com.benbenlaw.opolisutilities.screen.ResourceGeneratorMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidGeneratorBlock extends BaseEntityBlock {

    public static final MapCodec<FluidGeneratorBlock> CODEC = simpleCodec(FluidGeneratorBlock::new);

    public FluidGeneratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    /* ROTATION */
    @Override
    public @NotNull BlockState rotate(BlockState blockState, @NotNull LevelAccessor level, @NotNull BlockPos blockPos, @NotNull Rotation direction) {
        return blockState.setValue(POWERED, blockState.getValue(POWERED));

    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(POWERED, false);
    }

    /* BLOCK ENTITY */

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        if (blockState.getBlock() != newBlockState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof FluidGeneratorBlockEntity) {
                ((FluidGeneratorBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult hit) {

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        int tickRate;
        FluidGeneratorBlockEntity entity = (FluidGeneratorBlockEntity) level.getBlockEntity(blockPos);

        if (!level.isClientSide()) {

            //STAT CHECK//
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.STICK)) {
                if (blockState.getValue(FluidGeneratorBlock.POWERED)) {

                    assert entity != null;
                    tickRate = entity.maxProgress;
                    ItemStack itemStack = BuiltInRegistries.ITEM.get(new ResourceLocation(entity.resource)).getDefaultInstance();
                    String itemName = itemStack.getDisplayName().getString();

                    if (tickRate == 220) {
                        player.sendSystemMessage(Component.translatable("block.in_world.no_speed_upgrade").withStyle(ChatFormatting.RED));
                    }

                    player.sendSystemMessage(Component.translatable("block.in_world.tickrate", tickRate).withStyle(ChatFormatting.GREEN));
                    player.sendSystemMessage(Component.translatable("block.in_world.obtaining", itemName).withStyle(ChatFormatting.GREEN));
                }

                if (!blockState.getValue(FluidGeneratorBlock.POWERED)) {
                    {
                        player.sendSystemMessage(Component.literal("Not running! Check the block above is valid!").withStyle(ChatFormatting.RED));
                    }
                }
                return InteractionResult.SUCCESS;
            }

            //FILL BUCKET//

            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.BUCKET)) {
                if (blockState.getValue(FluidGeneratorBlock.POWERED)) {
                    assert entity != null;
                    FluidGeneratorBlockEntity fluidGeneratorBlockEntity = (FluidGeneratorBlockEntity) level.getBlockEntity(blockPos);
                    if (fluidGeneratorBlockEntity != null && fluidGeneratorBlockEntity.onPlayerUse(player, InteractionHand.MAIN_HAND)) {
                        return InteractionResult.SUCCESS;
                    }
                }
                return InteractionResult.FAIL;
            }

            //MENU OPEN//

            if (entity instanceof FluidGeneratorBlockEntity fluidGeneratorBlockEntity) {
                ContainerData data = fluidGeneratorBlockEntity.data;
                player.openMenu(new SimpleMenuProvider(
                        (windowId, playerInventory, playerEntity) -> new FluidGeneratorMenu(windowId, playerInventory, blockPos, data),
                        Component.translatable("block.opolisutilities.fluid_generator")), (buf -> buf.writeBlockPos(blockPos)));
            }

        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new FluidGeneratorBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.FLUID_GENERATOR_BLOCK_ENTITY.get(),
                (world, blockPos, thisBlockState, blockEntity) -> blockEntity.tick());
    }

}

