package com.benbenlaw.opolisutilities.block.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.DryingTableBlockEntity;
import com.benbenlaw.opolisutilities.screen.custom.DryingTableMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DryingTableBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final MapCodec<DryingTableBlock> CODEC = simpleCodec(DryingTableBlock::new);

    public DryingTableBlock(Properties properties) {
        super(properties);
    }

    /* PROPERTIES */
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final VoxelShape SHAPE = Block.box(0,0,0,16,16,16);

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {

        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(blockPos);
        Direction direction = context.getHorizontalDirection().getOpposite();

        if (blockState.is(Blocks.WATER)) {
            return this.defaultBlockState().setValue(WATERLOGGED, true).setValue(FACING, direction.getOpposite());
        } else {
            return this.defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, direction.getOpposite());
        }
    }

    /* ROTATION */
    @Override
    public @NotNull BlockState rotate(BlockState blockState, @NotNull LevelAccessor level, @NotNull BlockPos blockPos, Rotation direction) {
        return blockState.setValue(WATERLOGGED, blockState.getValue(WATERLOGGED)).setValue(FACING, direction.rotate(blockState.getValue(FACING)));

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(WATERLOGGED, FACING);
    }

    public @NotNull FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    /* BLOCK ENTITY */

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState newBlockState, boolean isMoving) {
        if (blockState.getBlock() != blockState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof DryingTableBlockEntity) {
                ((DryingTableBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult hit) {

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide()) {

            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof DryingTableBlockEntity dryingTableBlockEntity) {
                ContainerData data = dryingTableBlockEntity.data;

                if (blockState.getValue(WATERLOGGED)) {
                    player.openMenu(new SimpleMenuProvider(
                            (windowId, playerInventory, playerEntity) -> new DryingTableMenu(windowId, playerInventory, blockPos, data),
                            Component.translatable("block.opolisutilities.soaking_table")), (buf -> buf.writeBlockPos(blockPos)));
                }
                if (!blockState.getValue(WATERLOGGED)) {
                    player.openMenu(new SimpleMenuProvider(
                            (windowId, playerInventory, playerEntity) -> new DryingTableMenu(windowId, playerInventory, blockPos, data),
                            Component.translatable("block.opolisutilities.drying_table")), (buf -> buf.writeBlockPos(blockPos)));
                }
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new DryingTableBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.DRYING_TABLE_BLOCK_ENTITY.get(),
                (world, blockPos, thisBlockState, blockEntity) -> blockEntity.tick());
    }

}
