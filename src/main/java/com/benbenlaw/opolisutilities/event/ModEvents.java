package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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


@Mod.EventBusSubscriber(modid = OpolisUtilities.MOD_ID)

public class ModEvents {

    public static Vec3 globalEntity;
    public static Level globalLevel;

    @SubscribeEvent
    public static void cancelEndermanTeleportation(EntityTeleportEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EnderMan) {
            BlockPos pos = entity.getOnPos();

            int maxRange = EnderScramblerBlock.maxRange; // Get the maximum range

            for (int x = -maxRange; x <= maxRange; x++) {
                for (int y = -maxRange; y <= maxRange; y++) {
                    for (int z = -maxRange; z <= maxRange; z++) {
                        BlockPos p = pos.offset(x, y, z);
                        BlockState state = entity.level().getBlockState(p);
                        if (state.is(ModBlocks.ENDER_SCRAMBLER.get())) {

                            int r1 = state.getValue(EnderScramblerBlock.SCRAMBLER_RANGE);

                            for (int x1 = -r1; x1 <= r1; x1++) {
                                for (int y1 = -r1; y1 <= r1; y1++) {
                                    for (int z1 = -r1; z1 <= r1; z1++) {
                                        BlockPos p1 = pos.offset(x1, y1, z1);
                                        BlockState state1 = entity.level().getBlockState(p1);
                                        if (state1.is(ModBlocks.ENDER_SCRAMBLER.get())) {
                                            if (state1.getValue(EnderScramblerBlock.POWERED).equals(true)) {
                                                event.setCanceled(true);
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
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


