package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.benbenlaw.opolisutilities.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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
            for (int x = -7; x <= 7; x++) {
                for (int y = -7; y <= 7; y++) {
                    for (int z = -7; z <= 7; z++) {
                        BlockPos p = pos.offset(x, y, z);
                        BlockState state = entity.getLevel().getBlockState(p);
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



    @SubscribeEvent
    public static void getPlayerDeathPoint(LivingDeathEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof ServerPlayer) {
            globalEntity = entity.position();
            globalLevel = entity.getLevel();
        }

    }


    @SubscribeEvent
    public static void addDeathStoneOnPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {

        Player player = event.getEntity();
        Level level = player.getLevel();

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
        Level world = event.getEntity().getLevel();
        Entity e = event.getEntity();

        if (!(e instanceof ServerPlayer) && Math.random() > ConfigFile.basicLootBoxDropChance.get()) {
            world.addFreshEntity(new ItemEntity(world, entityPos.x(), entityPos.y(), entityPos.z(),
                    new ItemStack(ModItems.BASIC_LOOT_BOX.get())));
        }
    }
}



