package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.benbenlaw.opolisutilities.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CrookItem extends Item {
    public CrookItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, BlockState state, @NotNull BlockPos blockPos, @NotNull LivingEntity entity) {

        if (state.getBlock() instanceof LeavesBlock || state.is(BlockTags.LEAVES)) {

            for (int i = 0; i < ConfigFile.crookBoost.get(); i++) {
                List<ItemStack> blockDrops = Block.getDrops(state.getBlock().defaultBlockState(), (ServerLevel) level, blockPos,
                        level.getBlockEntity(blockPos), null, ModItems.CROOK.get().getDefaultInstance());

                for (ItemStack drop : blockDrops) {
                    spawnBlockAsEntity(level, blockPos, drop);
                }
            }
        //    if (entity instanceof Player) {
        //        entity.getItemBySlot(EquipmentSlot.MAINHAND).hurtAndBreak(1, entity,
        //                (damage) -> entity.broadcastBreakEvent(entity.getUsedItemHand()));
        //    }
        }
        return super.mineBlock(stack, level, state, blockPos, entity);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.getBlock() instanceof LeavesBlock || state.is(BlockTags.LEAVES)) {
            return 10f;
        }
        return super.getDestroySpeed(stack, state);
    }

    private static void spawnBlockAsEntity(Level level, BlockPos pos, ItemStack stack) {
        ItemEntity itemAsEntity = new ItemEntity(level,
                ((level.random.nextFloat() * 0.1) + 0.5) + pos.getX(),
                ((level.random.nextFloat() * 0.1) + 0.5) + pos.getY(),
                ((level.random.nextFloat() * 0.1) + 0.5) + pos.getZ(),
                stack
        );
        itemAsEntity.setDefaultPickUpDelay();
        itemAsEntity.setDeltaMovement((level.random.nextFloat() * 0.1 - 0.05), (level.random.nextFloat() * 0.1 - 0.03), (level.random.nextFloat() * 0.1 - 0.05));
        level.addFreshEntity(itemAsEntity);
    }







}
