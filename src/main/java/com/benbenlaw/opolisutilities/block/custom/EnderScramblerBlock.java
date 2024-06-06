package com.benbenlaw.opolisutilities.block.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.EnderScramblerBlockEntity;
import com.benbenlaw.opolisutilities.screen.custom.EnderScramblerMenu;
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

public class EnderScramblerBlock extends BaseEntityBlock {

    public static final MapCodec<EnderScramblerBlock> CODEC = simpleCodec(EnderScramblerBlock::new);

    public EnderScramblerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    /* PROPERTIES */
    public static final int MAX_RANGE = 8;// ConfigFile.maxScramblerRange.get();
    public static final int MIN_RANGE = 1;// ConfigFile.minScramblerRange.get();

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final IntegerProperty SCRAMBLER_RANGE = IntegerProperty.create("scrambler_range", MIN_RANGE, MAX_RANGE);


    /* ROTATION */

    @Override
    public @NotNull BlockState rotate(BlockState blockState, @NotNull LevelAccessor level, @NotNull BlockPos blockPos, Rotation direction) {
        return blockState.setValue(POWERED, blockState.getValue(POWERED)).setValue(SCRAMBLER_RANGE, blockState.getValue(SCRAMBLER_RANGE));

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED, SCRAMBLER_RANGE);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(POWERED, false).setValue(SCRAMBLER_RANGE, MIN_RANGE);
    }


    /* BLOCK ENTITY */

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        blockState.getBlock();
        newBlockState.getBlock();
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult hit) {

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide()) {

            EnderScramblerBlockEntity enderScramblerBlockEntity = (EnderScramblerBlockEntity) level.getBlockEntity(blockPos);

            //MENU OPEN//

            if (enderScramblerBlockEntity instanceof EnderScramblerBlockEntity) {
                ContainerData data = enderScramblerBlockEntity.data;
                player.openMenu(new SimpleMenuProvider(
                        (windowId, playerInventory, playerEntity) -> new EnderScramblerMenu(windowId, playerInventory, blockPos, data),
                        Component.translatable("block.opolisutilities.ender_scrambler")), (buf -> buf.writeBlockPos(blockPos)));

            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }



    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new EnderScramblerBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.ENDER_SCRAMBLER_BLOCK_ENTITY.get(),
                (world, blockPos, thisBlockState, blockEntity) -> blockEntity.tick());
    }
}
