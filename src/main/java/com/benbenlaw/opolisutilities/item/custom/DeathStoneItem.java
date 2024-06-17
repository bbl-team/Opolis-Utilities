package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.item.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;

import java.util.Objects;

public class DeathStoneItem extends Item {

    public DeathStoneItem(Properties properties) {
        super(properties);
    }

    public ResourceKey<Level> dimension = null;


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);
        DataComponentMap dataComponentMap = itemstack.getComponents();

        //Checks Location set if not nothing and send message

        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND && Objects.requireNonNull(itemstack.get(ModDataComponents.DIMENSION)).isEmpty()) {
            player.playNotifySound(SoundEvents.SHIELD_BLOCK, SoundSource.AMBIENT, 0.2f, 1);
            player.sendSystemMessage(Component.translatable("tooltips.death_stone.how").withStyle(ChatFormatting.RED));
        }

        //If location set then teleport
        else if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {

            dimension = ResourceKey.create(ResourceKey.createRegistryKey(
                            ResourceLocation.fromNamespaceAndPath("minecraft", "dimension")),
                    ResourceLocation.parse(Objects.requireNonNull(dataComponentMap.get(ModDataComponents.DIMENSION.get()))));

            MinecraftServer minecraftserver = player.getServer();
            assert minecraftserver != null;
            ServerLevel destinationWorld = minecraftserver.getLevel(dimension);
            assert destinationWorld != null;
            player.changeDimension(new DimensionTransition(destinationWorld, player, DimensionTransition.DO_NOTHING));

            //TP COORDINATES

            player.teleportTo(dataComponentMap.get(ModDataComponents.INT_X.get()) + 0.5, dataComponentMap.get(ModDataComponents.INT_Y.get()) + 1, dataComponentMap.get(ModDataComponents.INT_Z.get()) + 0.5);
            player.getItemInHand(hand).shrink(1);
            player.playNotifySound(SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.2f, 1);
            player.sendSystemMessage(Component.translatable("tooltips.home_stone.going_saved_location").withStyle(ChatFormatting.GREEN));

        }
        return super.use(level, player, hand);
    }


    //Tooltip

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, java.util.List<Component> components, TooltipFlag flag) {

        if (Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.death_stone.shift.held")
                    .withStyle(ChatFormatting.GREEN));
        } else {
            components.add(Component.translatable("tooltips.home_stone.hover.shift").withStyle(ChatFormatting.BLUE));
        }

        if (Screen.hasAltDown()) {


            components.add(Component.literal("X: " + stack.get(ModDataComponents.INT_X))
                    .withStyle(ChatFormatting.GREEN));
            components.add(Component.literal("Y: " + stack.get(ModDataComponents.INT_Y))
                    .withStyle(ChatFormatting.GREEN));
            components.add(Component.literal("Z: " + stack.get(ModDataComponents.INT_Z))
                    .withStyle(ChatFormatting.GREEN));
            components.add(Component.literal("Dimension: " + stack.get(ModDataComponents.DIMENSION))
                    .withStyle(ChatFormatting.GREEN));


        } else {
            components.add(Component.translatable("tooltips.home_stone.hover.alt")
                    .withStyle(ChatFormatting.BLUE));
        }

        super.appendHoverText(stack, tooltipContext, components, flag);
    }
}