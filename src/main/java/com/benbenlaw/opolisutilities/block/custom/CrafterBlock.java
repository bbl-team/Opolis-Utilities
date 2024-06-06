package com.benbenlaw.opolisutilities.block.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.CrafterBlockEntity;
import com.benbenlaw.opolisutilities.screen.custom.CrafterMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrafterBlock extends BaseEntityBlock {

    public static final MapCodec<CrafterBlock> CODEC = simpleCodec(CrafterBlock::new);

    public CrafterBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public static final int MAX_TIMER = 1200; // 1 minute
    public static final int MIN_TIMER = 40; // 2 seconds
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty TIMER = IntegerProperty.create("timer", MIN_TIMER, MAX_TIMER);

    @Override
    public @NotNull BlockState rotate(BlockState blockState, Rotation direction) {
        return blockState.setValue(FACING, direction.rotate(blockState.getValue(FACING))).setValue(POWERED, false).setValue(TIMER, MIN_TIMER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, TIMER, POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(POWERED, false).setValue(TIMER, 80);
    }

    /* FACING */

    /* BLOCK ENTITY */

    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        if (blockState.getBlock() != newBlockState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof CrafterBlockEntity) {
                ((CrafterBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos blockPos,
                                                        @NotNull Player player , @NotNull BlockHitResult hit) {
        
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide()) {

            CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity) level.getBlockEntity(blockPos);

            if (crafterBlockEntity instanceof CrafterBlockEntity) {
                ContainerData data = crafterBlockEntity.data;
                player.openMenu(new SimpleMenuProvider(
                        (windowId, playerInventory, playerEntity) -> new CrafterMenu(windowId, playerInventory, blockPos, data),
                        Component.translatable("block.opolisutilities.crafter")), (buf -> buf.writeBlockPos(blockPos)));
                return InteractionResult.SUCCESS;

            }
        }
        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new CrafterBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.CRAFTER_BLOCK_ENTITY.get(),
                (world, blockPos, thisBlockState, blockEntity) -> blockEntity.tick());
    }



}
