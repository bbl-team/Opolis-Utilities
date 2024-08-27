package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.custom.BlockPlacerBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.handler.InputOutputItemHandler;
import com.benbenlaw.opolisutilities.screen.custom.BlockPlacerMenu;
import com.benbenlaw.opolisutilities.util.DirectionUtils;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.benbenlaw.opolisutilities.block.custom.BlockPlacerBlock.FACING;
import static com.benbenlaw.opolisutilities.block.custom.BlockPlacerBlock.POWERED;

public class BlockPlacerBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        public void onContentsChanged(int slot) {
            setChanged();
            sync();
        }
    };

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

    public final ContainerData data;
    public int progress = 0;
    public int maxProgress = 220;
    private int maxTickChecker = 0;
    private static final int INPUT_SLOT = 0;

    private final IItemHandler blockPlacerItemHandler = new InputOutputItemHandler(itemHandler,
            (i, stack) -> i == INPUT_SLOT, // Allow in INPUT_SLOT
            i -> false // No output slots
    );


    //Called in startup for sides of the block
    public @Nullable IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        return blockPlacerItemHandler;
    }

    public void setHandler(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            this.itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }


    public ItemStackHandler getItemStackHandler() {
        return this.itemHandler;
    }


    public BlockPlacerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.BLOCK_PLACER_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> BlockPlacerBlockEntity.this.progress;
                    case 1 -> BlockPlacerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BlockPlacerBlockEntity.this.progress = value;
                    case 1 -> BlockPlacerBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.opolisutilities.block_placer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new BlockPlacerMenu(container, inventory, this.getBlockPos(), data);
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
        compoundTag.put("inventory", this.itemHandler.serializeNBT(provider));
        compoundTag.putInt("progress", progress);
        compoundTag.putInt("maxProgress", maxProgress);
    }
    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        this.itemHandler.deserializeNBT(provider, compoundTag.getCompound("inventory"));
        progress = compoundTag.getInt("progress");
        maxProgress = compoundTag.getInt("maxProgress");
        super.loadAdditional(compoundTag, provider);
    }
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    public void tick() {

        maxTickChecker++;

        Level pLevel = this.level;
        BlockPos pPos = this.worldPosition;
        assert pLevel != null;
        BlockPlacerBlockEntity pBlockEntity = this;
        BlockState blockState = pLevel.getBlockState(pPos);

        if (maxTickChecker >= 20) {
            maxTickChecker = 0;
        }

        if (!blockState.isAir() && !blockState.is(Blocks.VOID_AIR) && pLevel instanceof ServerLevel && blockState.getValue(POWERED)) {

            ItemStack itemStackInSlot = pBlockEntity.getItemStackHandler().getStackInSlot(0);
            Direction direction = pLevel.getBlockState(pPos).getValue(FACING);
            BlockPos placeHere = pPos.relative(direction);

            if (!itemStackInSlot.isEmpty() && blockState.hasProperty(BlockPlacerBlock.FACING) && level.getBlockState(placeHere).isAir()) {

                this.progress++;
                if (progress >= maxProgress) {
                    progress = 0;

                    Item item = itemStackInSlot.getItem();

                    if (item instanceof BlockItem) {
                        BlockState itemToBlockState = ((BlockItem) itemStackInSlot.getItem().asItem()).getBlock().defaultBlockState();
                        SoundType blockSounds = itemToBlockState.getBlock().getSoundType(itemToBlockState.getBlock().defaultBlockState(), level, pPos, null);

                        if (!itemToBlockState.isAir()) {

                            if (itemToBlockState.getBlock() instanceof BushBlock ||
                                    itemToBlockState.getBlock() instanceof SaplingBlock ||
                                    itemToBlockState.getBlock() instanceof RedStoneWireBlock) // ||
                                  //  itemToBlockState.getBlock().defaultBlockState().is(ModTags.Blocks.BANNED_IN_BLOCK_PLACER))
                                    {

                                if (level.getBlockState(placeHere.below()).is(BlockTags.DIRT)) {
                                    this.itemHandler.getStackInSlot(0).shrink(1);
                                    level.setBlockAndUpdate(placeHere, itemToBlockState);
                                    sync();
                                }
                            }

                            else {
                                this.itemHandler.getStackInSlot(0).shrink(1);
                                pLevel.playSound(null, pPos, blockSounds.getPlaceSound(), SoundSource.BLOCKS, 1, 1);
                                level.setBlockAndUpdate(placeHere, itemToBlockState);
                                sync();
                            }
                        }
                    }
                }
            }
        }
        if (!level.isClientSide) {
            setChanged();
            sync();
        }
    }
}