package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;


@Mod.EventBusSubscriber(modid = OpolisUtilities.MOD_ID)

public class ModEvents {

    public static Vec3 globalEntity;
    public static Level globalLevel;
/*
    @SubscribeEvent
    public static void cancelEndermanTeleportation(EntityTeleportEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EnderMan) {
            BlockPos pos = entity.getOnPos();

            int r = entity.level().getBlockState(pos).getValue(EnderScramblerBlock.SCRAMBLER_RANGE);

            for (int x = -r; x <= r; x++) {
                for (int y = -r; y <= r; y++) {
                    for (int z = -r; z <= r; z++) {
                        BlockPos p = pos.offset(x, y, z);
                        BlockState state = entity.level().getBlockState(p);
                        if (state.is(ModBlocks.ENDER_SCRAMBLER.get())) {
                            if (state.getValue(EnderScramblerBlock.POWERED).equals(true)) {
                                event.setCanceled(true);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

 */

    public static void onRenderHUD(GuiGraphics graphics, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        ItemStack stack = player.getMainHandItem();
        if (minecraft.screen == null) {

            HitResult hitResult = minecraft.hitResult;

            if (hitResult instanceof BlockHitResult hit) {

                BlockPos blockPos = hit.getBlockPos();
                BlockState blockState = minecraft.level.getBlockState(blockPos);
                Block block = blockState.getBlock();
                ItemStack picked = block.getCloneItemStack(minecraft.level, blockPos, blockState);

                if (picked.is(ModBlocks.ENDER_SCRAMBLER.get().asItem())) {

                    Window window = minecraft.getWindow();
                    int x = window.getGuiScaledWidth() / 2 + 3;
                    int y = window.getGuiScaledHeight() / 2 + 3;

                    int currentRange = blockState.getValue(EnderScramblerBlock.SCRAMBLER_RANGE);
                    boolean isRunning = blockState.getValue(EnderScramblerBlock.POWERED);

                    graphics.pose().pushPose();
                    graphics.pose().translate(0, 0, 10);
                    graphics.pose().scale(0.5F, 0.5F, 1);
               //     graphics.renderItem(stack, (x + 8) * 2, (y + 8) * 2);
               //     graphics.renderItemDecorations(minecraft.font, stack, (x + 8) * 2, (y + 8) * 2);
                    graphics.pose().popPose();

                    graphics.drawString(minecraft.font, "Range: " + currentRange, x -18, y + 3, 0xFFFFFF, false);

                    if (isRunning) {
                        graphics.drawString(minecraft.font, "Teleporting Not Allowed!", x -18, y + 27, 0xFFFFFF, false);
                    }
                    if (!isRunning) {
                        graphics.drawString(minecraft.font, "Teleporting Allowed!", x - 18, y + 27, 0xFFFFFF, false);
                    }
                    graphics.pose().pushPose();
                    graphics.pose().scale(0.75F, 0.75F, 1F);
                    graphics.pose().popPose();


                }
            }
        }


    }

    @SubscribeEvent
    public static void getPlayerDeathPoint(LivingDeathEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof ServerPlayer) {
            globalEntity = entity.position();
            globalLevel = entity.level();
        }

    }


    @SubscribeEvent
    public static void addDeathStoneOnPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {

        Player player = event.getEntity();
        Level level = player.level();

        ItemStack deathStoneItem = new ItemStack(ModItems.DEATH_STONE.get());
        CompoundTag nbt = new CompoundTag();

        if (!(globalEntity == null) && !(globalLevel == null)) {

            nbt.putInt("Age", -32768);
            nbt.putDouble("x", globalEntity.x);
            nbt.putDouble("y", globalEntity.y);
            nbt.putDouble("z", globalEntity.z);
            ResourceLocation dim = globalLevel.dimension().location();
            nbt.putString("dimension", dim.getNamespace() + ":" + dim.getPath());
            nbt.putString("namespace", dim.getNamespace());
            nbt.putString("path", dim.getPath());

            deathStoneItem.setTag(nbt);
        }

        if (level.getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).get()) {

            level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(),
                    deathStoneItem));
        }

        if (!level.getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).get()) {
            player.addItem(deathStoneItem);
        }
    }

    @SubscribeEvent
    public static void addLootBoxesToEntities(LivingDeathEvent event) {

        Vec3 entityPos = event.getEntity().position();
        Level world = event.getEntity().level();
        Entity e = event.getEntity();

        if (!(e instanceof ServerPlayer) && Math.random() > ConfigFile.basicLootBoxDropChance.get()) {
            world.addFreshEntity(new ItemEntity(world, entityPos.x(), entityPos.y(), entityPos.z(),
                    new ItemStack(ModItems.BASIC_LOOT_BOX.get())));
        }
    }

    //Add Crook to sheep temp
    @SubscribeEvent
    public static void sheepFollowCrook(EntityJoinLevelEvent event) {

        if (event.getEntity() instanceof Sheep sheep) {
            int priority = getTemptGoalPriority(sheep);
            if (priority >= 0)
                sheep.goalSelector.addGoal(priority, new TemptGoal(sheep, 1.1D, Ingredient.of(ModItems.CROOK.get()), false));
        }
    }

    public static int getTemptGoalPriority(Mob mob) {
        return mob.goalSelector.getAvailableGoals().stream()
                .filter(goal -> goal.getGoal() instanceof TemptGoal)
                .findFirst()
                .map(WrappedGoal::getPriority)
                .orElse(-1);
    }
}


