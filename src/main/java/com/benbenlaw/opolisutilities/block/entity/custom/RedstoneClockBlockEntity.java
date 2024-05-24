package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.RedstoneClockBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RedstoneClockBlockEntity extends BlockEntity {
    private int maxProgress = 20;
    private int progress = 0;
    private int pulseDuration = 0;

    public RedstoneClockBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.REDSTONE_CLOCK_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public void tick() {
            // Increment the counter

        assert level != null;
        if (!level.isClientSide()) {

            RedstoneClockBlockEntity entity = this;
            entity.progress++;
            int maxProgress = this.getBlockState().getValue(RedstoneClockBlock.CLOCK_TIMER);

            if (pulseDuration > 0) {
                pulseDuration--;
                if (pulseDuration == 0) {
                    level.setBlock(this.worldPosition, this.getBlockState().setValue(RedstoneClockBlock.POWERED, false), 3);
                }
            } else if (progress >= maxProgress && this.getBlockState().is(ModBlocks.REDSTONE_CLOCK.get())) {
                if (progress == maxProgress) {
                    level.setBlock(this.worldPosition, this.getBlockState().setValue(RedstoneClockBlock.CLOCK_TIMER, maxProgress)
                            .setValue(RedstoneClockBlock.POWERED, true), 3);
                }
                progress = -20;
                pulseDuration = 20;
            } else if (progress == (maxProgress / 2) && this.getBlockState().is(ModBlocks.REDSTONE_CLOCK.get())) {
                level.setBlock(this.worldPosition, this.getBlockState().setValue(RedstoneClockBlock.CLOCK_TIMER, maxProgress)
                        .setValue(RedstoneClockBlock.POWERED, false), 3);
            }
        }
    }


    /*
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.putInt("redstone_clock.progress", progress);
        tag.putInt("redstone_clock.maxProgress", maxProgress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        progress = nbt.getInt("redstone_clock.progress");
        maxProgress = nbt.getInt("redstone_clock.maxProgress");
    }

     */
}