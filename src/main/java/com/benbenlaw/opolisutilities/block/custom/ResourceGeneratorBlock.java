package com.benbenlaw.opolisutilities.block.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.DryingTableBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.ResourceGeneratorBlockEntity;
import com.benbenlaw.opolisutilities.screen.ResourceGeneratorMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourceGeneratorBlock extends BaseEntityBlock {
    public static final MapCodec<ResourceGeneratorBlock> CODEC = simpleCodec(ResourceGeneratorBlock::new);

    public ResourceGeneratorBlock(Properties properties) {
        super(properties);
    }
    /* PROPERTIES */
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
            if (blockEntity instanceof ResourceGeneratorBlockEntity) {
                ((ResourceGeneratorBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }
    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull BlockHitResult hit) {

        int tickRate;
        ResourceGeneratorBlockEntity entity = (ResourceGeneratorBlockEntity) level.getBlockEntity(blockPos);

        if (!level.isClientSide()) {

            //STAT CHECK//
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(Items.STICK)) {
                if (blockState.getValue(ResourceGeneratorBlock.POWERED)) {

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

                if (!blockState.getValue(ResourceGeneratorBlock.POWERED)) {
                    {
                        player.sendSystemMessage(Component.literal("Not running! Check the block above is valid!").withStyle(ChatFormatting.RED));
                    }
                }
                return InteractionResult.SUCCESS;
            }

            //MENU OPEN//

            if (entity instanceof ResourceGeneratorBlockEntity resourceGeneratorBlockEntity) {
                ContainerData data = resourceGeneratorBlockEntity.data;
                player.openMenu(new SimpleMenuProvider(
                        (windowId, playerInventory, playerEntity) -> new ResourceGeneratorMenu(windowId, playerInventory, blockPos, data),
                        Component.translatable("block.opolisutilities.resource_generator")), (buf -> buf.writeBlockPos(blockPos)));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ResourceGeneratorBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.RESOURCE_GENERATOR_BLOCK_ENTITY.get(),
                (world, blockPos, thisBlockState, blockEntity) -> blockEntity.tick());
    }
}