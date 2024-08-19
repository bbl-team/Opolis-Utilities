package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.block.entity.custom.CatalogueBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.OpolisBlockEntity;
import com.benbenlaw.opolisutilities.item.ModDataComponents;
import com.benbenlaw.opolisutilities.screen.custom.CatalogueMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class PortableGUIItem extends Item {
    public PortableGUIItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        assert player != null;
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide() && player.isCrouching()) {

            BlockState blockState = level.getBlockState(blockPos);
            boolean hasMenuProvider = blockState.getMenuProvider(level, blockPos) != null;
            boolean hasBlockEntity = level.getBlockEntity(blockPos) != null;

            if (hasMenuProvider || hasBlockEntity && itemstack.getItem() instanceof PortableGUIItem) {
                itemstack.set(ModDataComponents.INT_X, blockPos.getX());
                itemstack.set(ModDataComponents.INT_Y, blockPos.getY());
                itemstack.set(ModDataComponents.INT_Z, blockPos.getZ());
                return InteractionResult.SUCCESS;
            }

        }
        return InteractionResult.FAIL;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (itemstack.getItem() instanceof PortableGUIItem) {

                Integer x = itemstack.get(ModDataComponents.INT_X);
                Integer y = itemstack.get(ModDataComponents.INT_Y);
                Integer z = itemstack.get(ModDataComponents.INT_Z);

                if (x != null && y != null && z != null) {
                    BlockPos blockPos = new BlockPos(x, y, z);

                    if (level.isLoaded(blockPos)) {
                        BlockState blockState = level.getBlockState(blockPos);

                        // Check if the BlockState has a MenuProvider
                        MenuProvider menuProvider = blockState.getMenuProvider(level, blockPos);

                        if (menuProvider != null) {
                            if (level.isAreaLoaded(blockPos, 16)) {
                                player.openMenu(menuProvider, blockPos);
                                return InteractionResultHolder.success(itemstack);
                            } else {
                                player.sendSystemMessage(Component.translatable("tooltips.portable_gui.in_spawn_chunk").withStyle(ChatFormatting.RED));
                            }
                        } else {
                            player.sendSystemMessage(Component.translatable("tooltips.portable_gui.no_gui").withStyle(ChatFormatting.RED));
                        }
                    } else {
                        player.sendSystemMessage(Component.translatable("tooltips.portable_gui.not_loaded").withStyle(ChatFormatting.RED));
                    }
                }
            }
        }
        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, java.util.List<Component> components, TooltipFlag flag) {

        Level level = Minecraft.getInstance().level;

        assert level != null;
        String block = "No Block";

        if (stack.get(ModDataComponents.INT_X) != null && stack.get(ModDataComponents.INT_Y) != null && stack.get(ModDataComponents.INT_Z) != null) {
            block = level.getBlockState(new BlockPos(stack.get(ModDataComponents.INT_X), stack.get(ModDataComponents.INT_Y), stack.get(ModDataComponents.INT_Z))).getBlock().getDescriptionId();

        }

        if (Screen.hasShiftDown()) {
            if (block != null) {
                components.add(Component.translatable(block)
                        .withStyle(ChatFormatting.GREEN).append(Component.translatable("tooltips.portable_gui.shift.held")));
            } else {
                components.add(Component.translatable("tooltips.portable_gui.shift.held")
                        .withStyle(ChatFormatting.RED));
            }

            components.add(Component.literal("X: " + stack.get(ModDataComponents.INT_X))
                    .withStyle(ChatFormatting.GREEN));
            components.add(Component.literal("Y: " + stack.get(ModDataComponents.INT_Y))
                    .withStyle(ChatFormatting.GREEN));
            components.add(Component.literal("Z: " + stack.get(ModDataComponents.INT_Z))
                    .withStyle(ChatFormatting.GREEN));
        } else {
            components.add(Component.translatable("tooltips.portable_gui.hover.shift").withStyle(ChatFormatting.BLUE));
        }

        super.appendHoverText(stack, tooltipContext, components, flag);
    }
}