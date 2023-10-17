package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.block.custom.RedstoneClockBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock.SCRAMBLER_RANGE;

public class EnderScramblerBlockEntity extends BlockEntity {
    // Add a counter variable
  //  private int counter = 0;

    public EnderScramblerBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.REDSTONE_CLOCK_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, EnderScramblerBlockEntity entity) {

        /*
        EntityTeleportEvent event = null;

        assert false;
        Entity enderman = event.getEntity();
        if (enderman instanceof EnderMan) {
            BlockPos pos = enderman.blockPosition(); // Use blockPosition() instead of getOnPos()

            int r = scramblerRange(level, blockPos, blockState);

            if (r != 0) {
                for (int x = -r; x <= r; x++) {
                    for (int y = -r; y <= r; y++) {
                        for (int z = -r; z <= r; z++) {
                            BlockPos p = pos.offset(x, y, z);
                            BlockState state = level.getBlockState(p); // Use level.getBlockState() instead of enderman.level().getBlockState()
                            if (state.is(ModBlocks.ENDER_SCRAMBLER.get())) {
                                if (state.getValue(EnderScramblerBlock.POWERED)) { // Use state.getValue(EnderScramblerBlock.POWERED) directly
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
    }

    public static Integer scramblerRange(Level level, BlockPos blockPos, BlockState blockState) {

        if (blockState.getBlock() == ModBlocks.ENDER_SCRAMBLER.get()) {
            return level.getBlockState(blockPos).getValue(SCRAMBLER_RANGE);
        }
        return 0;
    }

}