package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.RedstoneClockBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.screen.custom.RedstoneClockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RedstoneClockBlockEntity extends BlockEntity implements MenuProvider {
    public final ContainerData data;
    public int progress = 0;
    public int maxProgress = 220;
    private int pulseDuration = 0;
    int maxTickChecker;
    public RedstoneClockBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.REDSTONE_CLOCK_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> RedstoneClockBlockEntity.this.progress;
                    case 1 -> RedstoneClockBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> RedstoneClockBlockEntity.this.progress = value;
                    case 1 -> RedstoneClockBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    public void sync() {
        if (level instanceof ServerLevel serverLevel) {
            LevelChunk chunk = serverLevel.getChunkAt(getBlockPos());
            if (Objects.requireNonNull(chunk.getLevel()).getChunkSource() instanceof ServerChunkCache chunkCache) {
                chunkCache.chunkMap.getPlayers(chunk.getPos(), false).forEach(this::syncContents);
            }
        }
    }
    public void syncContents(ServerPlayer player) {
        player.connection.send(Objects.requireNonNull(getUpdatePacket()));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.opolisutilities.redstone_clock");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new RedstoneClockMenu(container, inventory, this.getBlockPos(), data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.setChanged();
    }

    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(compoundTag, provider);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        saveAdditional(compoundTag, provider);
        return compoundTag;
    }

    @Override
    public void onDataPacket(@NotNull Connection connection, @NotNull ClientboundBlockEntityDataPacket clientboundBlockEntityDataPacket,
                             HolderLookup.@NotNull Provider provider) {
        super.onDataPacket(connection, clientboundBlockEntityDataPacket, provider);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putInt("progress", progress);
        compoundTag.putInt("maxProgress", maxProgress);
        compoundTag.putInt("pulseDuration", pulseDuration);
    }
    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        progress = compoundTag.getInt("progress");
        maxProgress = compoundTag.getInt("maxProgress");
        pulseDuration = compoundTag.getInt("pulseDuration");
        super.loadAdditional(compoundTag, provider);
    }

    public void tick() {
        sync();

        maxTickChecker++;

        Level pLevel = this.level;
        BlockPos pPos = this.worldPosition;
        assert pLevel != null;

        if (!level.isClientSide()) {

            progress++;

            if (pulseDuration > 0) {
                pulseDuration--;
                if (pulseDuration == 0) {
                    level.setBlock(this.worldPosition, this.getBlockState().setValue(RedstoneClockBlock.POWERED, false), 3);
                }
            } else if (progress >= maxProgress && this.getBlockState().is(ModBlocks.REDSTONE_CLOCK.get())) {
                if (progress == maxProgress) {
                    level.setBlock(this.worldPosition, this.getBlockState()
                            .setValue(RedstoneClockBlock.POWERED, true), 3);
                }
                progress = -20;
                pulseDuration = 20;
            } else if (progress == (maxProgress / 2) && this.getBlockState().is(ModBlocks.REDSTONE_CLOCK.get())) {
                level.setBlock(this.worldPosition, this.getBlockState()
                        .setValue(RedstoneClockBlock.POWERED, false), 3);
            }
        }
    }
}