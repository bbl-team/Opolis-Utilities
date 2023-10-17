package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.networking.ModMessages;
import com.benbenlaw.opolisutilities.networking.packets.PacketSyncItemStackToClient;
import com.benbenlaw.opolisutilities.screen.BlockBreakerMenu;
import com.benbenlaw.opolisutilities.util.inventory.WrappedHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
import java.util.List;
import java.util.Map;

import static com.benbenlaw.opolisutilities.block.custom.BlockBreakerBlock.FACING;

public class BlockBreakerBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new PacketSyncItemStackToClient(this, worldPosition));
            }
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(
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

    public BlockBreakerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.BLOCK_BREAKER_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> BlockBreakerBlockEntity.this.progress;
                    case 1 -> BlockBreakerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BlockBreakerBlockEntity.this.progress = value;
                    case 1 -> BlockBreakerBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Block Breaker");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player) {
        return new BlockBreakerMenu(containerID, inventory, this, this.data);
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
        tag.putInt("block_breaker.progress", progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("block_breaker.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    //Spawn Block As Entity Method inpsired by https://github.com/stfwi/engineers-decor/blob/1.19/src/main/java/wile/engineersdecor/blocks/EdBreaker.java

    private static void spawnBlockAsEntity(Level level, BlockPos pos, ItemStack stack) {
        if (level.isClientSide || stack.isEmpty() || (!level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) || level.restoringBlockSnapshots)
            ;
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

    public void tick() {

        Level pLevel = this.level;
        BlockPos pPos = this.worldPosition;
        assert pLevel != null;
        BlockState blockState = pLevel.getBlockState(pPos);

        if (!blockState.isAir() && !blockState.is(Blocks.VOID_AIR) && pLevel instanceof ServerLevel) {

            if (blockState.hasProperty(FACING)) {

                Direction direction = blockState.getValue(FACING);
                BlockPos placeHere = pPos.relative(direction);
                List<ItemStack> blockDrops;
                final Block block = level.getBlockState(placeHere).getBlock();
                ItemStack tool = itemHandler.getStackInSlot(0);
                int damageValue = this.itemHandler.getStackInSlot(0).getDamageValue();

                if (tool.getItem().isCorrectToolForDrops(block.defaultBlockState()) && this.itemHandler.getStackInSlot(1).isEmpty() && this.itemHandler.getStackInSlot(2).isEmpty()) {

                    blockDrops = Block.getDrops(block.defaultBlockState(), (ServerLevel) level, placeHere, this.level.getBlockEntity(pPos), null, tool);
                    SoundType blockSounds = level.getBlockState(placeHere).getBlock().getSoundType(level.getBlockState(placeHere).getBlock().defaultBlockState(), level, pPos, null);

                    level.setBlockAndUpdate(placeHere, Blocks.AIR.defaultBlockState());

                    for (ItemStack drop : blockDrops) {
                        spawnBlockAsEntity(level, placeHere, drop);
                    }

                    if (tool.isDamageableItem()) {
                        this.itemHandler.getStackInSlot(0).hurt(1, RandomSource.create(), null);
                        pLevel.playSound(null, pPos, blockSounds.getBreakSound(), SoundSource.BLOCKS, (float) 1, 1);
                    }

                    if (damageValue + 1 == tool.getMaxDamage()) {
                        this.itemHandler.extractItem(0, 1, false);
                        pLevel.playSound(null, pPos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1, 1);
                    }

                } else if (tool.getItem().isCorrectToolForDrops(block.defaultBlockState()) && this.itemHandler.getStackInSlot(1).is(Item.byBlock(block)) && this.itemHandler.getStackInSlot(2).isEmpty()) {
                    blockDrops = Block.getDrops(block.defaultBlockState(), (ServerLevel) level, placeHere, this.level.getBlockEntity(pPos), null, tool);
                    SoundType blockSounds = level.getBlockState(placeHere).getBlock().getSoundType(level.getBlockState(placeHere).getBlock().defaultBlockState(), level, pPos, null);

                    level.setBlockAndUpdate(placeHere, Blocks.AIR.defaultBlockState());

                    for (ItemStack drop : blockDrops) {
                        spawnBlockAsEntity(level, placeHere, drop);

                    }

                    if (tool.isDamageableItem()) {
                        this.itemHandler.getStackInSlot(0).hurt(1, RandomSource.create(), null);
                        pLevel.playSound(null, pPos, blockSounds.getBreakSound(), SoundSource.BLOCKS, (float) 1, 1);


                    }
                    if (damageValue + 1 == tool.getMaxDamage()) {
                        this.itemHandler.extractItem(0, 1, false);
                        pLevel.playSound(null, pPos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1, 1);
                    }
                } else if (tool.getItem().isCorrectToolForDrops(block.defaultBlockState()) && !this.itemHandler.getStackInSlot(2).is(Item.byBlock(block)) && this.itemHandler.getStackInSlot(1).isEmpty()) {
                    blockDrops = Block.getDrops(block.defaultBlockState(), (ServerLevel) level, placeHere, this.level.getBlockEntity(pPos), null, tool);
                    SoundType blockSounds = level.getBlockState(placeHere).getBlock().getSoundType(level.getBlockState(placeHere).getBlock().defaultBlockState(), level, pPos, null);

                    level.setBlockAndUpdate(placeHere, Blocks.AIR.defaultBlockState());

                    for (ItemStack drop : blockDrops) {
                        spawnBlockAsEntity(level, placeHere, drop);
                    }

                    if (tool.isDamageableItem()) {
                        this.itemHandler.getStackInSlot(0).hurt(1, RandomSource.create(), null);
                        pLevel.playSound(null, pPos, blockSounds.getBreakSound(), SoundSource.BLOCKS, (float) 1, 1);
                    }

                    if (damageValue + 1 == tool.getMaxDamage()) {
                        this.itemHandler.extractItem(0, 1, false);
                        pLevel.playSound(null, pPos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1, 1);
                    }
                }
            }
        }
    }
}
