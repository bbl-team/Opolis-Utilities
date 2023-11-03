package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.screen.CatalogueMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock.*;

public class WrenchItem extends Item {
    public WrenchItem(Properties p_41383_) {
        super(p_41383_);
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {

        if (!level.isClientSide()) {

            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.screen == null) {
                HitResult hitResult = minecraft.hitResult;
                if (hitResult instanceof BlockHitResult hit) {
                    BlockPos blockPos = hit.getBlockPos();
                    BlockState blockState = minecraft.level.getBlockState(blockPos);

                    if (blockState.is(ModBlocks.ENDER_SCRAMBLER.get())) {

                        int currentRange = blockState.getValue(SCRAMBLER_RANGE);

                        if (hand.equals(InteractionHand.MAIN_HAND) && player.getItemInHand(hand).is(ModItems.OPOLIS_WRENCH.get())) {

                            if (!player.isCrouching() && player.getItemInHand(hand).is(ModItems.OPOLIS_WRENCH.get()) && currentRange < maxRange) {
                                level.setBlockAndUpdate(blockPos, blockState.setValue(EnderScramblerBlock.SCRAMBLER_RANGE, currentRange + 1));
                                return InteractionResultHolder.success(this.getDefaultInstance());
                            } else if (player.isCrouching() && player.getItemInHand(hand).is(ModItems.OPOLIS_WRENCH.get()) && currentRange > minRange) {
                                level.setBlockAndUpdate(blockPos, blockState.setValue(EnderScramblerBlock.SCRAMBLER_RANGE, currentRange - 1));
                                return InteractionResultHolder.success(this.getDefaultInstance());
                            }
                        }
                        else if (hand.equals(InteractionHand.OFF_HAND) && player.getItemInHand(hand).is(ModItems.OPOLIS_WRENCH.get())) {
                            if (blockState.getValue(POWERED).equals(true)) {
                                level.setBlockAndUpdate(blockPos, blockState.setValue(POWERED, false));
                                return InteractionResultHolder.success(this.getDefaultInstance());
                            } else if (blockState.getValue(POWERED).equals(false)) {
                                level.setBlockAndUpdate(blockPos, blockState.setValue(POWERED, true));
                                return InteractionResultHolder.success(this.getDefaultInstance());
                            }
                        }
                    }
                }
            }
        }

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {

        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.scrambler_item.shift.held")
                    .withStyle(ChatFormatting.GREEN));
        }
        else {
            components.add(Component.translatable("tooltips.scrambler_item.hover.shift").withStyle(ChatFormatting.BLUE));
        }
    }

}

