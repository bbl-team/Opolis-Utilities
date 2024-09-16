package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.item.ModDataComponents;
import com.benbenlaw.opolisutilities.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PortableGUIItem extends Item {
    public PortableGUIItem(Properties properties) {
        super(properties);
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        // Check if it's a server-side operation
        if (!level.isClientSide() && itemstack.getItem() instanceof PortableGUIItem) {
            // Handle block interaction when the player is crouching
            if (blockHitResult.getType() == HitResult.Type.BLOCK && player.isCrouching()) {
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = level.getBlockState(blockPos);
                boolean hasMenuProvider = blockState.getMenuProvider(level, blockPos) != null;
                boolean hasBlockEntity = level.getBlockEntity(blockPos) != null;

                // Ensure block is not banned from portable GUI interaction
                if (!blockState.is(ModTags.Blocks.BANNED_IN_PORTABLE_GUI)) {
                    if (hasMenuProvider || hasBlockEntity) {

                        int locationValue = itemstack.get(ModDataComponents.LOCATION_VALUE) != null ?
                                itemstack.get(ModDataComponents.LOCATION_VALUE) : 1;

                        int blockX = blockPos.getX();
                        int blockY = blockPos.getY();
                        int blockZ = blockPos.getZ();
                        String blockName = blockState.getBlock().getDescriptionId();

                        // Store block coordinates and name based on location value
                        switch (locationValue) {
                            case 1:
                                itemstack.set(ModDataComponents.LOCATION_1, blockX + " " + blockY + " " + blockZ);
                                itemstack.set(ModDataComponents.BLOCK_NAME_1, blockName);
                                break;
                            case 2:
                                itemstack.set(ModDataComponents.LOCATION_2, blockX + " " + blockY + " " + blockZ);
                                itemstack.set(ModDataComponents.BLOCK_NAME_2, blockName);
                                break;
                            case 3:
                                itemstack.set(ModDataComponents.LOCATION_3, blockX + " " + blockY + " " + blockZ);
                                itemstack.set(ModDataComponents.BLOCK_NAME_3, blockName);
                                break;
                            case 4:
                                itemstack.set(ModDataComponents.LOCATION_4, blockX + " " + blockY + " " + blockZ);
                                itemstack.set(ModDataComponents.BLOCK_NAME_4, blockName);
                                break;
                            case 5:
                                itemstack.set(ModDataComponents.LOCATION_5, blockX + " " + blockY + " " + blockZ);
                                itemstack.set(ModDataComponents.BLOCK_NAME_5, blockName);
                                break;
                        }
                        return InteractionResultHolder.success(itemstack);
                    }
                } else {
                    player.sendSystemMessage(Component.translatable("tooltips.portable_gui.banned_block").withStyle(ChatFormatting.RED));
                    return InteractionResultHolder.fail(itemstack);
                }
            }

            // Handle stored location opening (regardless of whether block is clicked or air)
            int locationValue = itemstack.get(ModDataComponents.LOCATION_VALUE);
            String location = null;
            String blockName = null;

            // Get the stored block location and name based on location value
            switch (locationValue) {
                case 1:
                    location = itemstack.get(ModDataComponents.LOCATION_1);
                    blockName = itemstack.get(ModDataComponents.BLOCK_NAME_1);
                    break;
                case 2:
                    location = itemstack.get(ModDataComponents.LOCATION_2);
                    blockName = itemstack.get(ModDataComponents.BLOCK_NAME_2);
                    break;
                case 3:
                    location = itemstack.get(ModDataComponents.LOCATION_3);
                    blockName = itemstack.get(ModDataComponents.BLOCK_NAME_3);
                    break;
                case 4:
                    location = itemstack.get(ModDataComponents.LOCATION_4);
                    blockName = itemstack.get(ModDataComponents.BLOCK_NAME_4);
                    break;
                case 5:
                    location = itemstack.get(ModDataComponents.LOCATION_5);
                    blockName = itemstack.get(ModDataComponents.BLOCK_NAME_5);
                    break;
                default:
                    player.sendSystemMessage(Component.translatable("tooltips.portable_gui.invalid_location_value").withStyle(ChatFormatting.RED));
                    return InteractionResultHolder.fail(itemstack);
            }

            // Check if location data exists and attempt to open the stored block GUI
            if (location != null && !location.isEmpty()) {
                String[] coordinates = location.split(" ");
                if (coordinates.length == 3) {
                    try {
                        int x = Integer.parseInt(coordinates[0]);
                        int y = Integer.parseInt(coordinates[1]);
                        int z = Integer.parseInt(coordinates[2]);
                        BlockPos savedBlockPos = new BlockPos(x, y, z);

                        if (level.isLoaded(savedBlockPos)) {
                            BlockState savedBlockState = level.getBlockState(savedBlockPos);
                            MenuProvider menuProvider = savedBlockState.getMenuProvider(level, savedBlockPos);

                            if (menuProvider != null) {
                                if (level.isAreaLoaded(savedBlockPos, 16)) {
                                    player.openMenu(menuProvider, savedBlockPos);
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
                    } catch (NumberFormatException e) {
                        player.sendSystemMessage(Component.translatable("tooltips.portable_gui.invalid_location").withStyle(ChatFormatting.RED));
                    }
                } else {
                    player.sendSystemMessage(Component.translatable("tooltips.portable_gui.invalid_location_format").withStyle(ChatFormatting.RED));
                }
            } else {
                player.sendSystemMessage(Component.translatable("tooltips.portable_gui.no_location").withStyle(ChatFormatting.RED));
            }
        }

        return InteractionResultHolder.fail(itemstack);
    }


    /*

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

            if (!blockState.is(ModTags.Blocks.BANNED_IN_PORTABLE_GUI)) {

                if (hasMenuProvider || hasBlockEntity && itemstack.getItem() instanceof PortableGUIItem) {

                    int locationValue = 1;

                    if (itemstack.get(ModDataComponents.LOCATION_VALUE) != null) {
                        locationValue = itemstack.get(ModDataComponents.LOCATION_VALUE);
                    }

                    int blockX = blockPos.getX();
                    int blockY = blockPos.getY();
                    int blockZ = blockPos.getZ();

                    String blockName = blockState.getBlock().getDescriptionId();

                    switch (locationValue) {
                        case 1:
                            itemstack.set(ModDataComponents.LOCATION_1, blockX + " " + blockY + " " + blockZ);
                            itemstack.set(ModDataComponents.BLOCK_NAME_1, blockName);
                            break;
                        case 2:
                            itemstack.set(ModDataComponents.LOCATION_2, blockX + " " + blockY + " " + blockZ);
                            itemstack.set(ModDataComponents.BLOCK_NAME_2, blockName);
                            break;
                        case 3:
                            itemstack.set(ModDataComponents.LOCATION_3, blockX + " " + blockY + " " + blockZ);
                            itemstack.set(ModDataComponents.BLOCK_NAME_3, blockName);
                            break;
                        case 4:
                            itemstack.set(ModDataComponents.LOCATION_4, blockX + " " + blockY + " " + blockZ);
                            itemstack.set(ModDataComponents.BLOCK_NAME_4, blockName);
                            break;
                        case 5:
                            itemstack.set(ModDataComponents.LOCATION_5, blockX + " " + blockY + " " + blockZ);
                            itemstack.set(ModDataComponents.BLOCK_NAME_5, blockName);
                            break;
                    }

                    return InteractionResult.SUCCESS;
                }
            } else {
                player.sendSystemMessage(Component.translatable("tooltips.portable_gui.banned_block").withStyle(ChatFormatting.RED));
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (itemstack.getItem() instanceof PortableGUIItem) {

                int locationValue = itemstack.get(ModDataComponents.LOCATION_VALUE);

                String location = null;
                String blockName = null;

                switch (locationValue) {
                    case 1:
                        location = itemstack.get(ModDataComponents.LOCATION_1);
                        blockName = itemstack.get(ModDataComponents.BLOCK_NAME_1);
                        break;
                    case 2:
                        location = itemstack.get(ModDataComponents.LOCATION_2);
                        blockName = itemstack.get(ModDataComponents.BLOCK_NAME_2);
                        break;
                    case 3:
                        location = itemstack.get(ModDataComponents.LOCATION_3);
                        blockName = itemstack.get(ModDataComponents.BLOCK_NAME_3);
                        break;
                    case 4:
                        location = itemstack.get(ModDataComponents.LOCATION_4);
                        blockName = itemstack.get(ModDataComponents.BLOCK_NAME_4);
                        break;
                    case 5:
                        location = itemstack.get(ModDataComponents.LOCATION_5);
                        blockName = itemstack.get(ModDataComponents.BLOCK_NAME_5);
                        break;
                    default:
                        player.sendSystemMessage(Component.translatable("tooltips.portable_gui.invalid_location_value").withStyle(ChatFormatting.RED));
                        return InteractionResultHolder.fail(itemstack);
                }

                if (location != null && !location.isEmpty()) {
                    // Split the location string into x, y, z coordinates
                    String[] coordinates = location.split(" ");
                    if (coordinates.length == 3) {
                        try {
                            int x = Integer.parseInt(coordinates[0]);
                            int y = Integer.parseInt(coordinates[1]);
                            int z = Integer.parseInt(coordinates[2]);
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
                        } catch (NumberFormatException e) {
                            player.sendSystemMessage(Component.translatable("tooltips.portable_gui.invalid_location").withStyle(ChatFormatting.RED));
                        }
                    } else {
                        player.sendSystemMessage(Component.translatable("tooltips.portable_gui.invalid_location_format").withStyle(ChatFormatting.RED));
                    }
                } else {
                    player.sendSystemMessage(Component.translatable("tooltips.portable_gui.no_location").withStyle(ChatFormatting.RED));
                }
            }
        }
        return InteractionResultHolder.fail(itemstack);
    }

     */

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> components, TooltipFlag flag) {

        // Default block name
        String blockName = "Unknown Block";

        // Retrieve location value, default to -1 if null
        Integer locationValue = itemStack.get(ModDataComponents.LOCATION_VALUE);
        if (locationValue == null) {
            locationValue = -1;
        }

        // Default message for no location
        String locationMessage = "tooltips.portable_gui.no_location";
        String location;

        switch (locationValue) {
            case 1:
                location = itemStack.get(ModDataComponents.LOCATION_1);
                blockName = itemStack.get(ModDataComponents.BLOCK_NAME_1);
                break;
            case 2:
                location = itemStack.get(ModDataComponents.LOCATION_2);
                blockName = itemStack.get(ModDataComponents.BLOCK_NAME_2);
                break;
            case 3:
                location = itemStack.get(ModDataComponents.LOCATION_3);
                blockName = itemStack.get(ModDataComponents.BLOCK_NAME_3);
                break;
            case 4:
                location = itemStack.get(ModDataComponents.LOCATION_4);
                blockName = itemStack.get(ModDataComponents.BLOCK_NAME_4);
                break;
            case 5:
                location = itemStack.get(ModDataComponents.LOCATION_5);
                blockName = itemStack.get(ModDataComponents.BLOCK_NAME_5);
                break;
            default:
                // Handle unexpected locationValue values
                location = null;
                break;
        }

        if (location == null || location.trim().isEmpty()) {
            components.add(Component.translatable(locationMessage)
                    .withStyle(ChatFormatting.RED));
            super.appendHoverText(itemStack, tooltipContext, components, flag);
            return;
        }

        // If blockName is null, use the default blockName
        if (blockName == null || blockName.trim().isEmpty()) {
            blockName = "Unknown Block";
        }

        if (Screen.hasShiftDown()) {
            components.add(Component.translatable(blockName)
                    .withStyle(ChatFormatting.GREEN)
                    .append(Component.translatable("tooltips.portable_gui.shift.held"))
                    .append(Component.literal(location.replace(" ", ", "))
                            .withStyle(ChatFormatting.GREEN)));
        } else {
            components.add(Component.translatable("tooltips.portable_gui.hover.shift")
                    .withStyle(ChatFormatting.BLUE));
        }

        super.appendHoverText(itemStack, tooltipContext, components, flag);
    }

    @Override
    public @NotNull Component getName(ItemStack itemStack) {

        // Retrieve location value, default to -1 if null
        Integer locationValue = itemStack.get(ModDataComponents.LOCATION_VALUE);
        if (locationValue == null) {
            locationValue = -1;
        }

        // Default message in case of invalid location
        String locationMessage = "item.opolisutilities.portable_gui";

        String location = null;
        String blockName = null;

        switch (locationValue) {
            case 1:
                location = itemStack.get(ModDataComponents.LOCATION_1);
                blockName = itemStack.get(ModDataComponents.BLOCK_NAME_1);
                break;
            case 2:
                location = itemStack.get(ModDataComponents.LOCATION_2);
                blockName = itemStack.get(ModDataComponents.BLOCK_NAME_2);
                break;
            case 3:
                location = itemStack.get(ModDataComponents.LOCATION_3);
                blockName = itemStack.get(ModDataComponents.BLOCK_NAME_3);
                break;
            case 4:
                location = itemStack.get(ModDataComponents.LOCATION_4);
                blockName = itemStack.get(ModDataComponents.BLOCK_NAME_4);
                break;
            case 5:
                location = itemStack.get(ModDataComponents.LOCATION_5);
                blockName = itemStack.get(ModDataComponents.BLOCK_NAME_5);
                break;
            default:
                // Handle unexpected locationValue values
                break;
        }

        if (location != null && !location.trim().isEmpty()) {
            if (blockName == null || blockName.trim().isEmpty()) {
                blockName = "Unknown Block";
            }

            return Component.translatable(locationMessage)
                    .append(" ")
                    .append(Component.translatable(blockName).withStyle(ChatFormatting.GREEN))
                    .append(Component.literal(" (" + locationValue + ")").withStyle(ChatFormatting.BLUE));
        }

        return Component.translatable(locationMessage)
                .append(" ")
                .append(Component.literal(" (" + locationValue + ")").withStyle(ChatFormatting.BLUE));
    }
}
