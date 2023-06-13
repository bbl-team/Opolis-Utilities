package com.benbenlaw.opolisutilities.block.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderScramblerBlock extends Block {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public EnderScramblerBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, Boolean.valueOf(false)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55484_) {
        p_55484_.add(POWERED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(POWERED, false);
    }

    private final int i = 0;

    @Override
    public @NotNull InteractionResult use(BlockState pState, @NotNull Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pState.getValue(EnderScramblerBlock.POWERED).equals(true) && !pPlayer.isCrouching()) {
            pLevel.setBlockAndUpdate(pPos, this.defaultBlockState().setValue(EnderScramblerBlock.POWERED, false));
        }
        else if (pState.getValue(EnderScramblerBlock.POWERED).equals(false) && !pPlayer.isCrouching()) {
            pLevel.setBlockAndUpdate(pPos, this.defaultBlockState().setValue(EnderScramblerBlock.POWERED, true));
        }

        /* TODO: SAVE FOR ANOTHER DAY!

        if(pPlayer.isCrouching() && pHand.equals(ModItems.ENDER_PEARL_FRAGMENT.get())) {
            int range = i + 1;
        }

        if(pPlayer.isCrouching() && !pHand.equals(ModItems.ENDER_PEARL_FRAGMENT.get())) {
            int range = i - 1;
        }

         */

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> components, TooltipFlag flag) {

        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.ender_scrambler.shift.held")
                    .withStyle(ChatFormatting.GREEN));
        }
        else {
            components.add(Component.translatable("tooltips.home_stone.hover.shift").withStyle(ChatFormatting.BLUE));
        }
    }
}
