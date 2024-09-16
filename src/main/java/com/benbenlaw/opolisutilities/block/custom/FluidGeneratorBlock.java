package com.benbenlaw.opolisutilities.block.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.FluidGeneratorBlockEntity;
import com.benbenlaw.opolisutilities.item.ModDataComponents;
import com.benbenlaw.opolisutilities.screen.custom.FluidGeneratorMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

        FluidGeneratorBlockEntity entity = (FluidGeneratorBlockEntity) level.getBlockEntity(blockPos);

        if (!level.isClientSide()) {

            //FILL BUCKET//

            assert entity != null;
            if (entity.onPlayerUse(player, InteractionHand.MAIN_HAND)) {
                return InteractionResult.SUCCESS;
            }


            else {
                ContainerData data = entity.data;
                player.openMenu(new SimpleMenuProvider(
                        (windowId, playerInventory, playerEntity) -> new FluidGeneratorMenu(windowId, playerInventory, blockPos, data),
                        Component.translatable("block.opolisutilities.fluid_generator")), (buf -> buf.writeBlockPos(blockPos)));
            }

        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity entity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, entity, itemStack);

        if (itemStack.has(ModDataComponents.FLUID_TYPE) && itemStack.has(ModDataComponents.FLUID_AMOUNT)) {
            String fluidAsString = itemStack.get(ModDataComponents.FLUID_TYPE);
            assert fluidAsString != null;
            Fluid fluid = BuiltInRegistries.FLUID.get(ResourceLocation.tryParse(fluidAsString));
            int fluidAmount = itemStack.get(ModDataComponents.FLUID_AMOUNT);
            FluidGeneratorBlockEntity fluidGeneratorBlockEntity = (FluidGeneratorBlockEntity) level.getBlockEntity(blockPos);
            fluidGeneratorBlockEntity.setFluid(new FluidStack(fluid, fluidAmount));
        }
    }


    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
        if (entity instanceof FluidGeneratorBlockEntity fluidGeneratorBlockEntity) {

            if (fluidGeneratorBlockEntity.getFluidStack().getFluid() != Fluids.EMPTY && fluidGeneratorBlockEntity.getFluidStack().getAmount() > 0 ){
                ItemStack itemStackWithFluid = new ItemStack(this);
                itemStackWithFluid.set(ModDataComponents.FLUID_TYPE, fluidGeneratorBlockEntity.getFluidStack().getFluid().getFluidType().toString());
                itemStackWithFluid.set(ModDataComponents.FLUID_AMOUNT, fluidGeneratorBlockEntity.getFluidStack().getAmount());
                popResource(level, pos, itemStackWithFluid);
            } else {
                popResource(level, pos, this.asItem().getDefaultInstance());
            }
        }
        super.playerDestroy(level, player, pos, state, entity, stack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.@NotNull TooltipContext context, @NotNull List<Component> components, @NotNull TooltipFlag flag) {

        if (Screen.hasShiftDown()) {

            if (itemStack.has(ModDataComponents.FLUID_TYPE)) {
                String fluidAsString = itemStack.get(ModDataComponents.FLUID_TYPE);
                int fluidAmount = itemStack.get(ModDataComponents.FLUID_AMOUNT);
                assert fluidAsString != null;
                FluidType fluid = BuiltInRegistries.FLUID.get(ResourceLocation.tryParse(fluidAsString)).getFluidType();
                components.add(Component.literal("Contains: ").append(fluidAmount + "mb ").append(Component.translatable(fluid.getDescriptionId())).withStyle(ChatFormatting.GREEN));
            }
        } else {
            components.add(Component.translatable("tooltips.blocks.with_fluid.shift").withStyle(ChatFormatting.BLUE));
        }

        super.appendHoverText(itemStack, context, components, flag);

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

