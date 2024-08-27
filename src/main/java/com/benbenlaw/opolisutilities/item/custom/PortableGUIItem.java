package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.item.ModDataComponents;
import com.benbenlaw.opolisutilities.screen.utils.PortableMenuProvider;
import com.benbenlaw.opolisutilities.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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

            if(!blockState.is(ModTags.Blocks.BANNED_IN_PORTABLE_GUI)) {

                if (hasMenuProvider || hasBlockEntity && itemstack.getItem() instanceof PortableGUIItem) {

                    int locationValue = 1;

                    if (itemstack.get(ModDataComponents.LOCATION_VALUE) != null) {
                        locationValue = itemstack.get(ModDataComponents.LOCATION_VALUE);
                    }

                    int blockX = blockPos.getX();
                    int blockY = blockPos.getY();
                    int blockZ = blockPos.getZ();

                    switch (locationValue) {
                        case 1:
                            itemstack.set(ModDataComponents.LOCATION_1, blockX + " " + blockY + " " + blockZ);
                            break;
                        case 2:
                            itemstack.set(ModDataComponents.LOCATION_2, blockX + " " + blockY + " " + blockZ);
                            break;
                        case 3:
                            itemstack.set(ModDataComponents.LOCATION_3, blockX + " " + blockY + " " + blockZ);
                            break;
                        case 4:
                            itemstack.set(ModDataComponents.LOCATION_4, blockX + " " + blockY + " " + blockZ);
                            break;
                        case 5:
                            itemstack.set(ModDataComponents.LOCATION_5, blockX + " " + blockY + " " + blockZ);
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
                switch (locationValue) {
                    case 1:
                        location = itemstack.get(ModDataComponents.LOCATION_1);
                        break;
                    case 2:
                        location = itemstack.get(ModDataComponents.LOCATION_2);
                        break;
                    case 3:
                        location = itemstack.get(ModDataComponents.LOCATION_3);
                        break;
                    case 4:
                        location = itemstack.get(ModDataComponents.LOCATION_4);
                        break;
                    case 5:
                        location = itemstack.get(ModDataComponents.LOCATION_5);
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

                                        PortableMenuProvider portableMenuProvider = new PortableMenuProvider(menuProvider, player);
                                        player.openMenu(portableMenuProvider, blockPos);

                                        /*
                                        Container originalContainer = player.openMenu(menuProvider, blockPos);
                                        player.openMenu(menuProvider, blockPos)
                                        ;
                                         */

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

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> components, TooltipFlag flag) {
        Level level = Minecraft.getInstance().level;

        assert level != null;
        String block = "No Block";

        Integer locationValue = itemStack.get(ModDataComponents.LOCATION_VALUE);

        String location = null;
        switch (locationValue) {
            case 1:
                location = itemStack.get(ModDataComponents.LOCATION_1);
                break;
            case 2:
                location = itemStack.get(ModDataComponents.LOCATION_2);
                break;
            case 3:
                location = itemStack.get(ModDataComponents.LOCATION_3);
                break;
            case 4:
                location = itemStack.get(ModDataComponents.LOCATION_4);
                break;
            case 5:
                location = itemStack.get(ModDataComponents.LOCATION_5);
                break;
            case null:
                break;
            default:
                // Handle unexpected locationValue values
                break;
        }

        if (location == null || location.trim().isEmpty()) {
            components.add(Component.translatable("tooltips.portable_gui.no_location")
                    .withStyle(ChatFormatting.RED));
            super.appendHoverText(itemStack, tooltipContext, components, flag);
            return;
        }

        String[] coordinates = location.split(" ");
        if (coordinates.length == 3) {
            try {
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                int z = Integer.parseInt(coordinates[2]);
                BlockPos blockPos = new BlockPos(x, y, z);

                block = level.getBlockState(blockPos).getBlock().getDescriptionId();

                if (Screen.hasShiftDown()) {
                    components.add(Component.translatable(block)
                            .withStyle(ChatFormatting.GREEN)
                            .append(Component.translatable("tooltips.portable_gui.shift.held"))
                            .append(Component.literal(location.replace(" ", ", "))
                            .withStyle(ChatFormatting.GREEN)));

                } else {
                    components.add(Component.translatable("tooltips.portable_gui.hover.shift")
                            .withStyle(ChatFormatting.BLUE));
                }
            } catch (NumberFormatException e) {
                components.add(Component.translatable("tooltips.portable_gui.invalid_location_format")
                        .withStyle(ChatFormatting.RED));
            }
        } else {
            // Handle the case where coordinates do not have exactly 3 parts
            components.add(Component.translatable("tooltips.portable_gui.invalid_coordinates")
                    .withStyle(ChatFormatting.RED));
        }

        super.appendHoverText(itemStack, tooltipContext, components, flag);
    }

    @Override
    public @NotNull Component getName(ItemStack itemStack) {
        Level level = Minecraft.getInstance().level;
        assert level != null;

        Integer locationValue = itemStack.get(ModDataComponents.LOCATION_VALUE);

        // Default message in case of invalid location
        String locationMessage = "item.opolisutilities.portable_gui";

        // Retrieve location based on the locationValue
        String location = null;
        switch (locationValue) {
            case 1:
                location = itemStack.get(ModDataComponents.LOCATION_1);
                break;
            case 2:
                location = itemStack.get(ModDataComponents.LOCATION_2);
                break;
            case 3:
                location = itemStack.get(ModDataComponents.LOCATION_3);
                break;
            case 4:
                location = itemStack.get(ModDataComponents.LOCATION_4);
                break;
            case 5:
                location = itemStack.get(ModDataComponents.LOCATION_5);
                break;
            case null:
                break;
            default:
                // Location value is not recognized
                break;
        }

        if (location != null && !location.trim().isEmpty()) {
            String[] coordinates = location.split(" ");
            if (coordinates.length == 3) {
                try {
                    int x = Integer.parseInt(coordinates[0]);
                    int y = Integer.parseInt(coordinates[1]);
                    int z = Integer.parseInt(coordinates[2]);
                    BlockPos blockPos = new BlockPos(x, y, z);
                    String block = level.getBlockState(blockPos).getBlock().getDescriptionId();

                    if (block.contains("block.minecraft.void_air")) {
                        block = "Not Loaded!";
                        return Component.translatable(locationMessage)
                                .append(" ")
                                .append(Component.translatable(block).withStyle(ChatFormatting.RED))
                                .append(Component.literal(" (" + locationValue + ")").withStyle(ChatFormatting.BLUE));
                    } else {

                        return Component.translatable(locationMessage)
                                .append(" ")
                                .append(Component.translatable(block).withStyle(ChatFormatting.GREEN))
                                .append(Component.literal(" (" + locationValue + ")").withStyle(ChatFormatting.BLUE));
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return Component.translatable(locationMessage)
                .append(" ")
                .append(Component.literal(" (" + locationValue + ")").withStyle(ChatFormatting.BLUE));
    }

}