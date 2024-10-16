package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.config.StartupItemConfigFile;
import com.benbenlaw.opolisutilities.item.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


public class AnimalNetItem extends Item {
    public AnimalNetItem(Properties properties) {
        super(properties);
    }



    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player, @NotNull LivingEntity livingEntity, @NotNull InteractionHand hand) {
        // NULL this is in the ModEvents class because of taming / right click interacting mobs
        return InteractionResult.FAIL;
    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        assert player != null;
        ItemStack itemstack = player.getItemInHand(hand);
        BlockPos blockPos = context.getClickedPos();

        if (!level.isClientSide()) {
            if (itemstack.get(ModDataComponents.ENTITY_TYPE.get()) != null) {

                EntityType<?> type = EntityType.byString(Objects.requireNonNull(itemstack.get(ModDataComponents.ENTITY_TYPE.get())))
                        .orElse(null);

                if (type != null) {

                    // Spawn the mob
                    Entity entity = type.create(level);
                    assert entity != null;
                    entity.load(Objects.requireNonNull(itemstack.get(ModDataComponents.ENTITY_DATA)));
                    entity.absMoveTo(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, 0, 0);
                    itemstack.remove(ModDataComponents.ENTITY_TYPE);
                    itemstack.remove(ModDataComponents.ENTITY_DATA);

                    if (StartupItemConfigFile.animalNetTakesDamage.get().equals(true)) {
                        player.getItemInHand(hand).hurtAndBreak(1, player, player.getEquipmentSlotForItem(this.asItem().getDefaultInstance()));
                    }

                    level.addFreshEntity(entity);
                    level.playSound(null, blockPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS);


                } else {
                    player.sendSystemMessage(Component.translatable("tooltips.animal_net.no_mob").withStyle(ChatFormatting.RED));
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {


            if (stack.get(ModDataComponents.ENTITY_TYPE.get()) != null) {

                EntityType<?> entity = EntityType.byString(Objects.requireNonNull(stack.get(ModDataComponents.ENTITY_TYPE.get())))
                        .orElse(null);

                components.add(Component.translatable("tooltips.animal_net.shift.held").withStyle(ChatFormatting.BLUE));
                components.add(Component.translatable(entity.toString()).withStyle(ChatFormatting.GREEN));
            }
            else {

                boolean hostileMobs = StartupItemConfigFile.animalNetHostileMobs.get();
                boolean waterMobs = StartupItemConfigFile.animalNetWaterMobs.get();
                boolean animalMobs = StartupItemConfigFile.animalNetAnimalMobs.get();
                boolean villagerMobs = StartupItemConfigFile.animalNetVillagerMobs.get();

                components.add(Component.translatable("tooltips.animal_net.mob_types").withStyle(ChatFormatting.BLUE));

                if (hostileMobs) {
                    components.add(Component.translatable("tooltips.animal_net.hostile_mobs").withStyle(ChatFormatting.GREEN));
                }
                if (waterMobs) {
                    components.add(Component.translatable("tooltips.animal_net.water_mobs").withStyle(ChatFormatting.GREEN));
                }
                if (animalMobs) {
                    components.add(Component.translatable("tooltips.animal_net.animal_mobs").withStyle(ChatFormatting.GREEN));
                }
                if (villagerMobs) {
                    components.add(Component.translatable("tooltips.animal_net.villager_mobs").withStyle(ChatFormatting.GREEN));
                }

                components.add(Component.translatable("tooltips.animal_net.no_mob").withStyle(ChatFormatting.RED));
            }
        }
        else {
            components.add(Component.translatable("tooltips.animal_net.hover.shift").withStyle(ChatFormatting.BLUE));

        }
        super.appendHoverText(stack, tooltipContext, components, flag);
    }
}