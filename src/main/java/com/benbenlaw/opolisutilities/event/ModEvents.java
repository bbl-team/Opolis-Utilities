package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = OpolisUtilities.MOD_ID)

public class ModEvents {

    @SubscribeEvent
    public static void hammerOnAsteroidBlock(BlockEvent.BreakEvent event) {
        BlockPos blockPos = event.getPos();
        BlockState blockState = event.getLevel().getBlockState(blockPos);
        Player player = event.getPlayer();
        Level world = player.getLevel();

        if (player.getMainHandItem().is(ModTags.Items.HAMMER)) {
            if (!ModLoader.get().equals("cosmopolis")) {
                if (blockState.is(Blocks.COBBLESTONE)) {

                    world.addFreshEntity(new ItemEntity(world, blockPos.get(Direction.Axis.X), blockPos.get(Direction.Axis.Y), blockPos.get(Direction.Axis.Z),
                            new ItemStack(ModItems.HOME_STONE.get())));

                }
            }
        }

    }


    /*

    @SubscribeEvent
    public static void toColorStone(PlayerInteractEvent.RightClickBlock event) {
        BlockPos blockPos = event.getPos();
        BlockState blockState = event.getWorld().getBlockState(blockPos);
        Level world = event.getWorld();
        if (event.getPlayer().getMainHandItem().is(ModItems.WHITE_PAINTER.get())) {
            if (blockState.is(ModTags.Blocks.COLORED_STONE)) {

                world.setBlock(blockPos, ModBlocks.WHITE_COLORED_STONE.get().defaultBlockState(), 1);

                event.getPlayer().getItemBySlot(EquipmentSlot.MAINHAND).hurtAndBreak(1, event.getEntityLiving(),
                        (player) -> player.broadcastBreakEvent(player.getUsedItemHand()));

                world.addParticle(ParticleTypes.DRIPPING_WATER, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 1.1D, (double) blockPos.getZ() + 0.5D, 0.0D, 0.05D, 0.0D);
            }
        }
    }

    @SubscribeEvent
    public static void toColorStoneBricks(PlayerInteractEvent.RightClickBlock event) {
        BlockPos blockPos = event.getPos();
        BlockState blockState = event.getWorld().getBlockState(blockPos);
        Level world = event.getWorld();
        if (event.getPlayer().getMainHandItem().is(ModItems.WHITE_PAINTER.get())) {
            if (blockState.is(ModTags.Blocks.COLORED_STONE_BRICKS)) {

                world.setBlock(blockPos, ModBlocks.WHITE_COLORED_STONE_BRICKS.get().defaultBlockState(), 1);

                event.getPlayer().getItemBySlot(EquipmentSlot.MAINHAND).hurtAndBreak(1, event.getEntityLiving(),
                        (player) -> player.broadcastBreakEvent(player.getUsedItemHand()));

                world.addParticle(ParticleTypes.DRIPPING_WATER, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 1.1D, (double) blockPos.getZ() + 0.5D, 0.0D, 0.05D, 0.0D);
            }
        }
    }

    public static void toColorStoneStairs(PlayerInteractEvent.RightClickBlock event) {
        BlockPos blockPos = event.getPos();
        BlockState blockState = event.getWorld().getBlockState(blockPos);
        Level world = event.getWorld();
        if (event.getPlayer().getMainHandItem().is(ModItems.WHITE_PAINTER.get())) {
            if (blockState.is(ModTags.Blocks.COLORED_STONE_STAIRS)) {

                world.setBlock(blockPos, ModBlocks.WHITE_COLORED_STONE_STAIRS.get().defaultBlockState(), 1);

                event.getPlayer().getItemBySlot(EquipmentSlot.MAINHAND).hurtAndBreak(1, event.getEntityLiving(),
                        (player) -> player.broadcastBreakEvent(player.getUsedItemHand()));

                world.addParticle(ParticleTypes.DRIPPING_WATER, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 1.1D, (double) blockPos.getZ() + 0.5D, 0.0D, 0.05D, 0.0D);
            }
        }
    }

    @SubscribeEvent
    public static void toColorStoneBrickStairs(PlayerInteractEvent.RightClickBlock event) {
        BlockPos blockPos = event.getPos();
        BlockState blockState = event.getWorld().getBlockState(blockPos);
        Level world = event.getWorld();
        if (event.getPlayer().getMainHandItem().is(ModItems.WHITE_PAINTER.get())) {
            if (blockState.is(ModTags.Blocks.COLORED_STONE_BRICK_STAIRS)) {

                world.setBlock(blockPos, ModBlocks.WHITE_COLORED_STONE_BRICK_STAIRS.get().defaultBlockState(), 1);

                event.getPlayer().getItemBySlot(EquipmentSlot.MAINHAND).hurtAndBreak(1, event.getEntityLiving(),
                        (player) -> player.broadcastBreakEvent(player.getUsedItemHand()));

                world.addParticle(ParticleTypes.DRIPPING_WATER, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 1.1D, (double) blockPos.getZ() + 0.5D, 0.0D, 0.05D, 0.0D);
            }
        }
    }

    public static void toColorStoneSlab(PlayerInteractEvent.RightClickBlock event) {
        BlockPos blockPos = event.getPos();
        BlockState blockState = event.getWorld().getBlockState(blockPos);
        Level world = event.getWorld();
        if (event.getPlayer().getMainHandItem().is(ModItems.WHITE_PAINTER.get())) {
            if (blockState.is(ModTags.Blocks.COLORED_STONE_SLAB)) {

                world.setBlock(blockPos, ModBlocks.WHITE_COLORED_STONE_SLAB.get().defaultBlockState(), 1);

                event.getPlayer().getItemBySlot(EquipmentSlot.MAINHAND).hurtAndBreak(1, event.getEntityLiving(),
                        (player) -> player.broadcastBreakEvent(player.getUsedItemHand()));

                world.addParticle(ParticleTypes.DRIPPING_WATER, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 1.1D, (double) blockPos.getZ() + 0.5D, 0.0D, 0.05D, 0.0D);
            }
        }
    }

    @SubscribeEvent
    public static void toColorStoneBrickSlab(PlayerInteractEvent.RightClickBlock event) {
        BlockPos blockPos = event.getPos();
        BlockState blockState = event.getWorld().getBlockState(blockPos);
        Level world = event.getWorld();
        if (event.getPlayer().getMainHandItem().is(ModItems.WHITE_PAINTER.get())) {
            if (blockState.is(ModTags.Blocks.COLORED_STONE_BRICK_SLAB)) {

                world.setBlock(blockPos, ModBlocks.WHITE_COLORED_STONE_BRICK_SLAB.get().defaultBlockState(), 1);

                event.getPlayer().getItemBySlot(EquipmentSlot.MAINHAND).hurtAndBreak(1, event.getEntityLiving(),
                        (player) -> player.broadcastBreakEvent(player.getUsedItemHand()));

                world.addParticle(ParticleTypes.DRIPPING_WATER, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 1.1D, (double) blockPos.getZ() + 0.5D, 0.0D, 0.05D, 0.0D);
            }
        }
    }

    public static void toColorStonewall(PlayerInteractEvent.RightClickBlock event) {
        BlockPos blockPos = event.getPos();
        BlockState blockState = event.getWorld().getBlockState(blockPos);
        Level world = event.getWorld();
        if (event.getPlayer().getMainHandItem().is(ModItems.WHITE_PAINTER.get())) {
            if (blockState.is(ModTags.Blocks.COLORED_STONE_WALL)) {

                world.setBlock(blockPos, ModBlocks.WHITE_COLORED_STONE_WALL.get().defaultBlockState(), 1);

                event.getPlayer().getItemBySlot(EquipmentSlot.MAINHAND).hurtAndBreak(1, event.getEntityLiving(),
                        (player) -> player.broadcastBreakEvent(player.getUsedItemHand()));

                world.addParticle(ParticleTypes.DRIPPING_WATER, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 1.1D, (double) blockPos.getZ() + 0.5D, 0.0D, 0.05D, 0.0D);
            }
        }
    }


     */
/*

    @SubscribeEvent
    public static void toColorStoneBrickWall(PlayerInteractEvent.RightClickBlock event) {
        BlockPos blockPos = event.getPos();
        Level world = event.getWorld();
        BlockState blockState = event.getWorld().getBlockState(blockPos);


        if (event.getPlayer().getMainHandItem().is(ModItems.WHITE_PAINTER.get())) {
            if (blockState.is(ModTags.Blocks.COLORED_STONE_BRICK_WALL)) {

                world.setBlock(blockPos, ModBlocks.WHITE_COLORED_STONE_BRICK_WALL.get().withPropertiesOf(blockState), 1);

                event.getPlayer().getItemBySlot(EquipmentSlot.MAINHAND).hurtAndBreak(1, event.getEntityLiving(),
                        (player) -> player.broadcastBreakEvent(player.getUsedItemHand()));

                world.addParticle(ParticleTypes.DRIPPING_WATER, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 1.1D, (double) blockPos.getZ() + 0.5D, 0.0D, 0.05D, 0.0D);
            }
        }
    }

    */






}



