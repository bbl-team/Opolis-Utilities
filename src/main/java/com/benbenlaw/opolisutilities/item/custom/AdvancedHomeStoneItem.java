package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.benbenlaw.opolisutilities.util.ModTeleport;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AdvancedHomeStoneItem extends SimpleFoiledItem {

    public AdvancedHomeStoneItem(Properties properties) {
        super(properties);
    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);
        CompoundTag nbt = itemstack.getTag();
        if (nbt == null) nbt = new CompoundTag();

        //Check player is crouching and has item in off hand

        if (!level.isClientSide() && ( level.dimension().equals(Level.OVERWORLD) || level.dimension().equals(Level.NETHER) || level.dimension().equals(Level.END) )
                && player.isCrouching() && hand == InteractionHand.OFF_HAND) {

            //set x,y and x inside item nbt and play sound and print message

            nbt.putFloat("x", player.getOnPos().getX());
            nbt.putFloat("y", player.getOnPos().getY());
            nbt.putFloat("z", player.getOnPos().getZ());

            ResourceLocation dim = level.dimension().location();

            nbt.putString("dimension", dim.getNamespace() +":"+ dim.getPath());

            itemstack.setTag(nbt);

            player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.2f,1);
            player.sendSystemMessage(Component.translatable("tooltips.home_stone.location_set").withStyle(ChatFormatting.GREEN));
        }

        //Checks Location set if not nothing and send message

        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND && !nbt.contains("x")) {
            player.playNotifySound(SoundEvents.SHIELD_BLOCK, SoundSource.AMBIENT, 0.2f,1);
            player.sendSystemMessage(Component.translatable("tooltips.home_stone.no_location").withStyle(ChatFormatting.RED));
        }

        //If location set then teleport
        else if (!level.isClientSide() && ( level.dimension().equals(Level.OVERWORLD)
                || level.dimension().equals(Level.NETHER) || level.dimension().equals(Level.END) )
                && hand == InteractionHand.MAIN_HAND){

            //IF A DIMENSION CHANGE IS REQUIRED
            if(nbt.getString("dimension").equals("minecraft:overworld")){
                MinecraftServer minecraftserver = player.getServer();
                ResourceKey<Level> destination = Level.OVERWORLD;
                ServerLevel destinationWorld = minecraftserver.getLevel(destination);
                player.changeDimension(destinationWorld, new ModTeleport(destinationWorld));
            }

            else if(nbt.getString("dimension").equals("minecraft:the_nether")){
                MinecraftServer minecraftserver = player.getServer();
                ResourceKey<Level> destination = Level.NETHER;
                ServerLevel destinationWorld = minecraftserver.getLevel(destination);
                player.changeDimension(destinationWorld, new ModTeleport(destinationWorld));
            }

            else if(nbt.getString("dimension").equals("minecraft:the_end")){
                MinecraftServer minecraftserver = player.getServer();
                ResourceKey<Level> destination = Level.END;
                ServerLevel destinationWorld = minecraftserver.getLevel(destination);
                player.changeDimension(destinationWorld, new ModTeleport(destinationWorld));
            }

            //TP COORDINATES

            player.teleportTo(nbt.getFloat("x") + 0.5, nbt.getFloat("y") + 1, nbt.getFloat("z") + 0.5);
            player.getCooldowns().addCooldown(this, ConfigFile.homeStoneCooldown.get());
            player.playNotifySound(SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.2f, 1);
            player.sendSystemMessage(Component.translatable("tooltips.home_stone.going_saved_location").withStyle(ChatFormatting.GREEN));

            if (ConfigFile.homeStoneTakesDamage.get().equals(true)) {

                player.getItemBySlot(EquipmentSlot.MAINHAND).hurtAndBreak(1, player,
                        (damage) -> player.broadcastBreakEvent(player.getUsedItemHand()));
            }
        }
        return super.use(level, player, hand);
    }


    //Tooltip

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {

        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.advanced_home_stone.shift.held")
                    .withStyle(ChatFormatting.GREEN));
            }
        else {
            components.add(Component.translatable("tooltips.home_stone.hover.shift").withStyle(ChatFormatting.BLUE));
        }

        if(Screen.hasAltDown()) {

            if(stack.hasTag()) {

                components.add(Component.literal("X: " + stack.getTag().getFloat("x"))
                        .withStyle(ChatFormatting.GREEN));
                components.add(Component.literal("Y: " + stack.getTag().getFloat("y"))
                        .withStyle(ChatFormatting.GREEN));
                components.add(Component.literal("Z: " + stack.getTag().getFloat("z"))
                        .withStyle(ChatFormatting.GREEN));
                components.add(Component.literal("Dimension: " + stack.getTag().getString("dimension"))
                        .withStyle(ChatFormatting.GREEN));

            }
            if(!stack.hasTag()) {
                components.add(Component.translatable("tooltips.home_stone.no_location_set")
                        .withStyle(ChatFormatting.RED));
            }
        }
        else {
            components.add(Component.translatable("tooltips.home_stone.hover.alt")
                    .withStyle(ChatFormatting.BLUE));
        }

        super.appendHoverText(stack, level, components, flag);
    }
}












