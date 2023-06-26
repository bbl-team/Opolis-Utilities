package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.custom.BlockBreakerBlock;
import com.benbenlaw.opolisutilities.block.entity.IInventoryHandlingBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.networking.ModMessages;
import com.benbenlaw.opolisutilities.networking.packets.PacketSyncItemStackToClient;
import com.benbenlaw.opolisutilities.screen.BlockPlacerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.benbenlaw.opolisutilities.block.custom.BlockPlacerBlock.FACING;

public class BlockPlacerBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            assert level != null;
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new PacketSyncItemStackToClient(this, worldPosition));
            }
        }
    };

    private int counter = 0;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(

            //        Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1, (i, s) -> false)),

                    Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack)))
            );


    public final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;

    public void setHandler(ItemStackHandler handler) {
        copyHandlerContents(handler);
    }

    private void copyHandlerContents(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
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
                return 1;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Block Placer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, @NotNull Inventory inventory, @NotNull Player player) {
        return new BlockPlacerMenu(containerID, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return directionWrappedHandlerMap.get(side).cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        for (Direction dir : Direction.values()) {
            if (directionWrappedHandlerMap.containsKey(dir)) {
                directionWrappedHandlerMap.get(dir).invalidate();
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("block_placer.progress", progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("block_placer.progress");
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

        Level pLevel = this.level;
        BlockPos pPos = this.worldPosition;
        assert pLevel != null;
        BlockPlacerBlockEntity pBlockEntity = this;
        BlockState blockState = pLevel.getBlockState(pPos);
        this.counter++;

        if (!blockState.isAir() && !blockState.is(Blocks.VOID_AIR) && pLevel instanceof ServerLevel) {

            ItemStack itemStackInSlot = pBlockEntity.getItemStackHandler().getStackInSlot(0);
            Direction direction = pLevel.getBlockState(pPos).getValue(FACING);
            BlockPos placeHere = pPos.relative(direction);

            if (blockState.hasProperty(BlockBreakerBlock.FACING)) {

                if (!itemStackInSlot.isEmpty() && this.counter % 40 == 0) {

                    Item item = itemStackInSlot.getItem();

                    if (item instanceof BlockItem && level.getBlockState(placeHere).isAir()) {
                        BlockState itemToBlockState = ((BlockItem) itemStackInSlot.getItem().asItem()).getBlock().defaultBlockState();
                        SoundType blockSounds = itemToBlockState.getBlock().getSoundType(itemToBlockState.getBlock().defaultBlockState(), level, pPos, null);

                        if (!itemToBlockState.isAir()) {
                            this.itemHandler.getStackInSlot(0).shrink(1);
                            pLevel.playSound(null, pPos, blockSounds.getPlaceSound(), SoundSource.BLOCKS, 1, 1);
                            level.setBlockAndUpdate(placeHere, itemToBlockState);
                        }
                    }
                }
            }
        }
    }
}