package com.benbenlaw.opolisutilities.block.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.BlockBreakerBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.CrafterBlockEntity;
import com.benbenlaw.opolisutilities.screen.BlockBreakerMenu;
import com.benbenlaw.opolisutilities.screen.CrafterMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
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

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

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
            return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(POWERED, true);
        }
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(POWERED, false);
    }


    /* FACING */


    /* ROTATION */

    @Override
    public @NotNull BlockState rotate(BlockState blockState, @NotNull LevelAccessor level, @NotNull BlockPos blockPos, Rotation direction) {
        return blockState.setValue(FACING, direction.rotate(blockState.getValue(FACING))).setValue(POWERED, blockState.getValue(POWERED));

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, POWERED);
    }

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
    protected InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos blockPos,
                                               @NotNull Player player , @NotNull BlockHitResult hit) {

        if (!level.isClientSide()) {

            CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity) level.getBlockEntity(blockPos);


            if (crafterBlockEntity instanceof CrafterBlockEntity) {
                ContainerData data = crafterBlockEntity.data;
                player.openMenu(new SimpleMenuProvider(
                        (windowId, playerInventory, playerEntity) -> new CrafterMenu(windowId, playerInventory, blockPos, data),
                        Component.translatable("block.opolisutilities.crafter")), (buf -> buf.writeBlockPos(blockPos)));

            }
            return InteractionResult.SUCCESS;
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
