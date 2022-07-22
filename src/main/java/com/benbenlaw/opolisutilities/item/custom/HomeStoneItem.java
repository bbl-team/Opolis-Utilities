package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.DataSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.dimension.DimensionDefaults;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.logging.Logger;

public class HomeStoneItem extends Item {

    public HomeStoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);
        CompoundTag nbt = itemstack.getTag();
        if (nbt == null) nbt = new CompoundTag();

        //Check player is crouching and has item in off hand

        if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD) && player.isCrouching() && hand == InteractionHand.OFF_HAND) {

            //set x,y and x inside item nbt and playsound and print message

            nbt.putFloat("x", player.getOnPos().getX());
            nbt.putFloat("y", player.getOnPos().getY());
            nbt.putFloat("z", player.getOnPos().getZ());

          //  nbt.putString("dimension", player.getLevel().dimension().toString());

            itemstack.setTag(nbt);
            player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.2f,1);
            player.sendSystemMessage(Component.literal("Location Set!"));
        }

        //Checks Location set if not nothing and send message

        if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD) && hand == InteractionHand.MAIN_HAND && !nbt.contains("x")) {
            player.playNotifySound(SoundEvents.SHIELD_BLOCK, SoundSource.AMBIENT, 0.2f,1);
            player.sendSystemMessage(Component.literal("No Location Set!"));
        }

        //If location Set tps
        else if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD) && hand == InteractionHand.MAIN_HAND){

            player.teleportTo(nbt.getFloat("x") + 0.5, nbt.getFloat("y")+ 1, nbt.getFloat("z")+ 0.5);

            player.getCooldowns().addCooldown(this,6000); // TODO make this configurable

            player.playNotifySound(SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.2f,1);
            player.sendSystemMessage(Component.literal("Going to saved location!"));
        }
        return super.use(level, player, hand);
    }


    //Tooltip

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {

        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.shift.held")
                    .withStyle(ChatFormatting.GREEN));
            }
        else {
            components.add(Component.translatable("tooltips.hover.shift").withStyle(ChatFormatting.BLUE));
        }

        if(Screen.hasAltDown()) {

            if(stack.hasTag()) {

                components.add(Component.literal(String.valueOf("X: " + stack.getTag().getFloat("x")))
                        .withStyle(ChatFormatting.GREEN));
                components.add(Component.literal(String.valueOf("Y: " + stack.getTag().getFloat("y")))
                        .withStyle(ChatFormatting.GREEN));
                components.add(Component.literal(String.valueOf("Z: " + stack.getTag().getFloat("z")))
                        .withStyle(ChatFormatting.GREEN));
            }
            if(!stack.hasTag()) {
                components.add(Component.translatable("tooltips.no_location_set")
                        .withStyle(ChatFormatting.RED));
            }
        }
        else {
            components.add(Component.translatable("tooltips.hover.alt")
                    .withStyle(ChatFormatting.BLUE));
        }

        super.appendHoverText(stack, level, components, flag);
    }
}












