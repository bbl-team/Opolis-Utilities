package com.benbenlaw.opolisutilities.block.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.ResourceGenerator2BlockEntity;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.benbenlaw.opolisutilities.recipe.RG2SpeedBlocksRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourceGenerator2Block extends BaseEntityBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public ResourceGenerator2Block(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.valueOf(false)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55484_) {
        p_55484_.add(LIT);
    }

    @Override
    public @NotNull InteractionResult use(BlockState pState, Level level, BlockPos blockPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        int tickRate = 220;

        //Check for Cap and apply correct tickrate
        if (!level.getBlockState(blockPos.above(2)).is(Blocks.AIR)) {

            for (RG2SpeedBlocksRecipe match : level.getRecipeManager().getRecipesFor(RG2SpeedBlocksRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                String blockName = match.getBlock();
                TagKey<Block> speedBlock = BlockTags.create(new ResourceLocation(blockName));
                Block speedBlockBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

                if (level.getBlockState(blockPos.above(2)).getBlockHolder().containsTag(speedBlock) || level.getBlockState(blockPos.above(2)).is(speedBlockBlock)) {
                    tickRate = match.getTickRate();
                }
            }
        }

        if (!level.isClientSide()) {
            if (pHand.equals(InteractionHand.MAIN_HAND)){
                if (pState.getValue(ResourceGenerator2Block.LIT)) {

                    BlockState block = level.getBlockState(blockPos.above(1));
                    Block translatedBlock = block.getBlock();
                    String translatedName = translatedBlock.getName().getString();

                    if (tickRate == 220){
                        pPlayer.sendSystemMessage(Component.literal("No speed block detected, place a valid block above the resource block").withStyle(ChatFormatting.RED));
                    }
                    pPlayer.sendSystemMessage(Component.literal("Current tick rate is " + tickRate).withStyle(ChatFormatting.GREEN));
                    pPlayer.sendSystemMessage(Component.literal("Obtaining " + translatedName).withStyle(ChatFormatting.GREEN));
                }

                if(!pState.getValue(ResourceGenerator2Block.LIT)) {
                    pPlayer.sendSystemMessage(Component.literal("Not running! Check the block above is valid!").withStyle(ChatFormatting.RED));
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(LIT, false);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ResourceGenerator2BlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.RESOURCE_GENERATOR_2_BLOCK_ENTITY.get(),
                (world, blockPos, blockState, blockEntity) -> blockEntity.tick());
    }
}