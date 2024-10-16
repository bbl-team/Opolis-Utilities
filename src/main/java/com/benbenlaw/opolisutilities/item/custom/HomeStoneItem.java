package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.config.StartupItemConfigFile;
import com.benbenlaw.opolisutilities.item.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HomeStoneItem extends Item {

    public HomeStoneItem(Properties properties) {
        super(properties);
    }

    public ResourceKey<Level> dimension = null;


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide() && player.isCrouching() && hand == InteractionHand.OFF_HAND) {

            itemstack.set(ModDataComponents.INT_X.get(), player.getOnPos().getX());
            itemstack.set(ModDataComponents.INT_Y.get(), player.getOnPos().getY());
            itemstack.set(ModDataComponents.INT_Z.get(), player.getOnPos().getZ());

            ResourceLocation dim = level.dimension().location();

            itemstack.set(ModDataComponents.DIMENSION.get(), dim.getNamespace() +":"+ dim.getPath());

            player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.2f,1);
            player.sendSystemMessage(Component.translatable("tooltips.home_stone.location_set").withStyle(ChatFormatting.GREEN));
        }

        //Checks Location set if not nothing and send message

        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND && itemstack.get(ModDataComponents.INT_X.get()) == null) {
            player.playNotifySound(SoundEvents.SHIELD_BLOCK, SoundSource.AMBIENT, 0.2f,1);
            player.sendSystemMessage(Component.translatable("tooltips.home_stone.no_location").withStyle(ChatFormatting.RED));
        }

        //If location set then teleport
        else if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND){

            dimension = ResourceKey.create(ResourceKey.createRegistryKey(
                            ResourceLocation.fromNamespaceAndPath("minecraft", "dimension")),
                    ResourceLocation.parse(Objects.requireNonNull(itemstack.get(ModDataComponents.DIMENSION.get()))));

            MinecraftServer minecraftserver = player.getServer();
            assert minecraftserver != null;
            ServerLevel destinationWorld = minecraftserver.getLevel(dimension);
            assert destinationWorld != null;
            player.changeDimension(new DimensionTransition(destinationWorld, player, DimensionTransition.DO_NOTHING));
            //TP COORDINATES

            player.teleportTo(itemstack.get(ModDataComponents.INT_X.get()) + 0.5,
                    itemstack.get(ModDataComponents.INT_Y.get()) + 1,
                    itemstack.get(ModDataComponents.INT_Z.get()) + 0.5
            );

            player.getCooldowns().addCooldown(this, StartupItemConfigFile.homeStoneCooldown.get());
            player.playNotifySound(SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.2f, 1);
            player.sendSystemMessage(Component.translatable("tooltips.home_stone.going_saved_location").withStyle(ChatFormatting.GREEN));

            if (StartupItemConfigFile.homeStoneTakesDamage.get().equals(true)) {
                player.getItemBySlot(EquipmentSlot.MAINHAND).hurtAndBreak(1, player,
                        EquipmentSlot.MAINHAND);
            }
        }
        return super.use(level, player, hand);
    }

    //Tooltip


    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext tooltipContext, java.util.@NotNull List<Component> components, TooltipFlag flag) {
        ;

        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.super_home_stone.shift.held")
                    .withStyle(ChatFormatting.GREEN));
        }
        else {
            components.add(Component.translatable("tooltips.home_stone.hover.shift").withStyle(ChatFormatting.BLUE));
        }

        if(Screen.hasAltDown()) {

            Integer x = stack.get(ModDataComponents.INT_X.get());
            Integer y = stack.get(ModDataComponents.INT_Y.get());
            Integer z = stack.get(ModDataComponents.INT_Z.get());
            String dimension = stack.get(ModDataComponents.DIMENSION.get());

            if (x != null && y != null && z != null && dimension != null) {

                components.add(Component.literal("X: " + stack.get(ModDataComponents.INT_X))
                        .withStyle(ChatFormatting.GREEN));
                components.add(Component.literal("Y: " + stack.get(ModDataComponents.INT_Y))
                        .withStyle(ChatFormatting.GREEN));
                components.add(Component.literal("Z: " + stack.get(ModDataComponents.INT_Z))
                        .withStyle(ChatFormatting.GREEN));
                components.add(Component.literal("Dimension: " + stack.get(ModDataComponents.DIMENSION))
                        .withStyle(ChatFormatting.GREEN));

            }
            else {
                components.add(Component.translatable("tooltips.home_stone.no_location_set")
                        .withStyle(ChatFormatting.RED));
            }
        }
        else {
            components.add(Component.translatable("tooltips.home_stone.hover.alt")
                    .withStyle(ChatFormatting.BLUE));
        }

        super.appendHoverText(stack, tooltipContext, components, flag);
    }
}












