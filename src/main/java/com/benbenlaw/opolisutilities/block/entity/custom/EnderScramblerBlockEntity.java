package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnderScramblerBlockEntity extends BlockEntity {

    public EnderScramblerBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.ENDER_SCRAMBLER_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, EnderScramblerBlockEntity blockEntity) {
        cancelEndermanTeleportation(new EntityTeleportEvent(level.getEntity(1), 1, 1, 1));
    }

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

}
