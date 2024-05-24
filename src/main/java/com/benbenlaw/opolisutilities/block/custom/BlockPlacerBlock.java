package com.benbenlaw.opolisutilities.block.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.BlockPlacerBlockEntity;
import com.benbenlaw.opolisutilities.screen.BlockPlacerMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
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

public class BlockPlacerBlock extends BaseEntityBlock {

    public static final MapCodec<BlockPlacerBlock> CODEC = simpleCodec(BlockPlacerBlock::new);

    public BlockPlacerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    public static final int MAX_TIMER = 1200; // 1 minute
    public static final int MIN_TIMER = 10; // 0.5 seconds

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty TIMER = IntegerProperty.create("timer", MIN_TIMER, MAX_TIMER);

    /* REDSTONE SIGNAL */
    @Override
    protected void neighborChanged(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos,
                                   @NotNull Block neighborBlock, @NotNull BlockPos neighborBlockPos, boolean movedByPiston) {

        if (!level.isClientSide()) {
            boolean powered = level.hasNeighborSignal(blockPos);
            if (powered != blockState.getValue(POWERED)) {
                level.setBlock(blockPos, blockState.setValue(POWERED, powered), 3);
            }
        }
    }

    /* FACING WITH REDSTONE NEIGHBOUR CHECK */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        boolean powered = level.hasNeighborSignal(blockPos);
        if (powered) {
            return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(POWERED, true).setValue(TIMER, MIN_TIMER);
        }
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(POWERED, false).setValue(TIMER, MIN_TIMER);
    }

    /* ROTATION */
    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(BlockState blockState, Rotation direction) {
        return blockState.setValue(FACING, direction.rotate(blockState.getValue(FACING))).setValue(POWERED, false).setValue(TIMER, MIN_TIMER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, TIMER, POWERED);
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
            if (blockEntity instanceof BlockPlacerBlockEntity) {
                ((BlockPlacerBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult hit) {

        if (!level.isClientSide()) {

            BlockPlacerBlockEntity blockPlacerBlockEntity = (BlockPlacerBlockEntity) level.getBlockEntity(blockPos);

            //STAT CHECK//
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.STICK)) {
                assert blockPlacerBlockEntity != null;
                int maxProgress = blockPlacerBlockEntity.maxProgress;
                int currentProgress = blockPlacerBlockEntity.progress;
                player.sendSystemMessage(Component.literal("Max Progress: " + maxProgress));
                player.sendSystemMessage(Component.literal("Current Progress: " + currentProgress));
            }

            //MENU OPEN//
            else {
                player.openMenu(new SimpleMenuProvider(
                        (windowId, playerInventory, playerEntity) -> new BlockPlacerMenu(windowId, playerInventory, blockPos),
                        Component.translatable("block.opolisutilities.block_placer")), (buf -> buf.writeBlockPos(blockPos)));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new BlockPlacerBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.BLOCK_PLACER_BLOCK_ENTITY.get(),
                (world, blockPos, thisBlockState, blockEntity) -> blockEntity.tick());
    }

}
