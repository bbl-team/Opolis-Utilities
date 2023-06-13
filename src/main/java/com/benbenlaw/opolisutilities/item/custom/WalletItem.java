package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WalletItem extends Item {

    public WalletItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, InteractionHand hand) {

        ItemStack itemstack = player.getOffhandItem();
        CompoundTag nbt = itemstack.getTag();
        if (nbt == null) nbt = new CompoundTag();

        //Check wallet in off hand and b bucks in main hand

        if (!player.isCrouching()) {


            if (!level.isClientSide() && player.getOffhandItem().is(this)) {

                if (player.getMainHandItem().is(ModItems.B_BUCKS.get())) {

                    player.getMainHandItem().shrink(1);

                    if (nbt.isEmpty()) {
                        nbt.putInt("b_bucks_amount", 0);
                    }

                    if (!nbt.isEmpty()) {
                        int totalBucks = nbt.getInt("b_bucks_amount");
                        nbt.putInt("b_bucks_amount", totalBucks + 1);
                    }

                    itemstack.setTag(nbt);
                }
            }
        }

        if (player.isCrouching()) {
            if (!level.isClientSide() && player.getOffhandItem().is(this)) {

                if (player.getMainHandItem().is(ModItems.B_BUCKS.get()) || player.getMainHandItem().isEmpty()) {

                    if (!nbt.isEmpty() && nbt.getInt("b_bucks_amount") > 0) {
                        int totalBucks = nbt.getInt("b_bucks_amount");
                        nbt.putInt("b_bucks_amount", totalBucks - 1);
                        player.addItem(new ItemStack(ModItems.B_BUCKS.get()));
                    }

                    if (nbt.getInt("b_bucks_amount") <= 0) {
                        player.sendSystemMessage(Component.translatable("tooltips.wallet.no_b_bucks").withStyle(ChatFormatting.RED));
                    }

                    itemstack.setTag(nbt);
                }
            }
        }
        return super.use(level, player, hand);
    }

    //Tooltip

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {

        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.wallet.shift.held")
                    .withStyle(ChatFormatting.GREEN));
        }
        else {
            components.add(Component.translatable("tooltips.wallet.hover.shift").withStyle(ChatFormatting.BLUE));
        }

        if(Screen.hasAltDown()) {

            if(stack.hasTag()) {

                components.add(Component.literal("This wallet contains " + stack.getTag().getInt("b_bucks_amount") + " B Bucks")
                        .withStyle(ChatFormatting.GREEN));
            }
            if(!stack.hasTag()) {
                components.add(Component.translatable("This Wallet Is Empty")
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

        /*

            //set x,y and x inside item nbt and playsound and print message

            nbt.putFloat("b_bucks_amount", player.getOnPos().getX());


          //  nbt.putString("dimension", player.getLevel().dimension().toString());

            itemstack.setTag(nbt);
            player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.2f,1);
            player.sendSystemMessage(Component.translatable("tooltip.home_stone.location_set").withStyle(ChatFormatting.GREEN));
        }

        //Checks Location set if not nothing and send message

        if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD) && hand == InteractionHand.MAIN_HAND && !nbt.contains("x")) {
            player.playNotifySound(SoundEvents.SHIELD_BLOCK, SoundSource.AMBIENT, 0.2f,1);
            player.sendSystemMessage(Component.translatable("tooltip.home_stone.no_location").withStyle(ChatFormatting.RED));
        }

        //If location Set tps
        else if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD) && hand == InteractionHand.MAIN_HAND){

            player.teleportTo(nbt.getFloat("x") + 0.5, nbt.getFloat("y")+ 1, nbt.getFloat("z")+ 0.5);

            player.getCooldowns().addCooldown(this, ConfigFile.homeStoneCooldown.get());

            player.playNotifySound(SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.2f,1);
            player.sendSystemMessage(Component.translatable("tooltip.home_stone.going_saved_location").withStyle(ChatFormatting.GREEN));

            if(ConfigFile.homeStoneTakesDamage.get().equals(true)) {

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
            components.add(Component.translatable("tooltips.home_stone.shift.held")
                    .withStyle(ChatFormatting.GREEN));
            }
        else {
            components.add(Component.translatable("tooltips.home_stone.hover.shift").withStyle(ChatFormatting.BLUE));
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

*/










