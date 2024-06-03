package com.benbenlaw.opolisutilities.block.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.BlockBreakerBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.RedstoneClockBlockEntity;
import com.benbenlaw.opolisutilities.screen.BlockBreakerMenu;
import com.benbenlaw.opolisutilities.screen.RedstoneClockMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RedstoneClockBlock extends BaseEntityBlock {

    public static final MapCodec<RedstoneClockBlock> CODEC = simpleCodec(RedstoneClockBlock::new);

    public RedstoneClockBlock(Properties properties) {
        super(properties);
    }
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    /* PROPERTIES */
    public static final int MAX_TIMER = 1200; // 1 min
    public static final int MIN_TIMER = 20; // 1 seconds

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty TIMER = IntegerProperty.create("timer", MIN_TIMER, MAX_TIMER);


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED, TIMER);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(POWERED, false).setValue(TIMER, 80);
    }
    @SuppressWarnings("deprecation")
    @Override
    public int getSignal(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, Direction direction) {

        if (blockState.getValue(POWERED)) {
            return 15;
        }
        return 0;
    }

    /* OPEN MENU */
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult hit) {

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        RedstoneClockBlockEntity redstoneClockBlockEntity = (RedstoneClockBlockEntity) level.getBlockEntity(blockPos);

        if (redstoneClockBlockEntity instanceof RedstoneClockBlockEntity) {
            ContainerData data = redstoneClockBlockEntity.data;
            player.openMenu(new SimpleMenuProvider(
                    (windowId, playerInventory, playerEntity) -> new RedstoneClockMenu(windowId, playerInventory, blockPos, data),
                    Component.translatable("block.opolisutilities.redstone_clock")), (buf -> buf.writeBlockPos(blockPos)));

        }
        return InteractionResult.SUCCESS;
    }

    /* BLOCK ENTITY */
    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new RedstoneClockBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.REDSTONE_CLOCK_BLOCK_ENTITY.get(),
                (world, blockPos, thisBlockState, blockEntity) -> blockEntity.tick());
    }

}
