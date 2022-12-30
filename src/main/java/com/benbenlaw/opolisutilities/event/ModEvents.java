package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = OpolisUtilities.MOD_ID)

public class ModEvents {

    /*

    public static Vec3 globalEntity;

    @SubscribeEvent
    public static void getPlayerDeathPoint (LivingDeathEvent event) {

        Entity entity = event.getEntity();

        if (entity instanceof ServerPlayer) {
            globalEntity = entity.position();
        }

    }

    @SubscribeEvent
    public static void addDeathStoneOnPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {

        Player player = event.getEntity();
        Level level = player.getLevel();

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.DEATH_STONE.get()));
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
        CompoundTag nbt = itemstack.getTag();
        if (nbt == null) nbt = new CompoundTag();

        nbt.putFloat("x", (float) globalEntity.z);
        nbt.putFloat("y", (float) globalEntity.y);
        nbt.putFloat("z", (float) globalEntity.z);

        ResourceLocation dim = level.dimension().location();
        nbt.putString("dimension", dim.getNamespace() +":"+ dim.getPath());
        nbt.putString("namespace", dim.getNamespace());
        nbt.putString("path", dim.getPath());


    }
     */

    public static Vec3 globalEntity;
    public static Level globalLevel;

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

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.DEATH_STONE.get()));
        ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
        CompoundTag nbt = itemstack.getTag();
        if (nbt == null) nbt = new CompoundTag();

        nbt.putDouble("x", globalEntity.x);
        nbt.putDouble("y", globalEntity.y);
        nbt.putDouble("z", globalEntity.z);

        ResourceLocation dim = globalLevel.dimension().location();
        nbt.putString("dimension", dim.getNamespace() + ":" + dim.getPath());
        nbt.putString("namespace", dim.getNamespace());
        nbt.putString("path", dim.getPath());

        itemstack.setTag(nbt);
    }




    @SubscribeEvent
    public static void addLootBoxesToEntities(LivingDeathEvent event) {

        Vec3 entityPos = event.getEntity().position();
        Level world = event.getEntity().getLevel();
        Entity e = event.getEntity();

        if (e instanceof ServerPlayer) {}

        else if (Math.random() > ConfigFile.basicLootBoxDropChance.get()) {

            world.addFreshEntity(new ItemEntity(world, entityPos.x(), entityPos.y(), entityPos.z(),
                    new ItemStack(ModItems.BASIC_LOOT_BOX.get())));
        }
    }
}



