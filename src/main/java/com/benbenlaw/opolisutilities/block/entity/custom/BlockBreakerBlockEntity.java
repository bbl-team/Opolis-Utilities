package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.custom.BlockBreakerBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.networking.ModMessages;
import com.benbenlaw.opolisutilities.networking.packets.PacketSyncItemStackToClient;
import com.benbenlaw.opolisutilities.screen.BlockBreakerMenu;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import com.benbenlaw.opolisutilities.util.inventory.WrappedHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
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
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.benbenlaw.opolisutilities.block.custom.BlockBreakerBlock.FACING;
import static com.benbenlaw.opolisutilities.block.custom.BlockBreakerBlock.POWERED;

public class BlockBreakerBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            assert level != null;
            if (!level.isClientSide()) {
            //    ModMessages.sendToClients(new PacketSyncItemStackToClient(this, worldPosition));
            }
        }
    };



    private Lazy<IItemHandler> lazyItemHandler = null;

    private final Map<Direction, Lazy<WrappedHandler>> directionWrappedHandlerMap;
    private final IItemHandler itemHandlerSided;


    /*
    private final Map<Direction, Lazy<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(
                    Direction.DOWN, Lazy.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.UP, Lazy.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.NORTH, Lazy.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.SOUTH, Lazy.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.WEST, Lazy.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack))),

                    Direction.EAST, Lazy.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> index == 0 && itemHandler.isItemValid(0, stack)))
            );

     */




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
        this.directionWrappedHandlerMap = new HashMap<>();

        this.itemHandlerSided = new InputOutputItemHandler(this.itemHandler, (i, stack) -> {
            return i == 0 || i == 1;
        }, (i) -> {
            return i == 2 || i == 3 || i == 4 || i == 5 || i == 6 || i == 7 || i == 8 || i == 9;
        });

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

    public @Nullable IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        return (IItemHandler)(side == null ? this.itemHandler : this.itemHandlerSided);
    }


    @Override
    public Component getDisplayName() {
        return Component.literal("Block Breaker");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new BlockBreakerMenu(pContainerId, pPlayerInventory, this, this.data);
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
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.loadAdditional(tag, lookupProvider);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
    }



    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("inventory", this.itemHandler.serializeNBT(provider));
        tag.putInt("block_breaker.progress", progress);
        tag.putInt("block_breaker.maxProgress", maxProgress);
    }


    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        this.itemHandler.deserializeNBT(provider, tag.getCompound("inventory"));
        progress = tag.getInt("block_breaker.progress");
        maxProgress = tag.getInt("block_breaker.maxProgress");
        super.loadAdditional(tag, provider);
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


        Level level = this.level;
        BlockPos pos = this.worldPosition;
        assert level != null;
        BlockState blockState = level.getBlockState(pos);
        maxProgress = this.getBlockState().getValue(BlockBreakerBlock.TIMER);

        setChanged(level, worldPosition, blockState);

        if (!blockState.isAir() && !blockState.is(Blocks.VOID_AIR) && level instanceof ServerLevel) {

            if (blockState.hasProperty(FACING) && blockState.getValue(POWERED)) {

                Direction direction = blockState.getValue(FACING);
                BlockPos placeHere = pos.relative(direction);
                List<ItemStack> blockDrops;
                final Block block = this.level.getBlockState(placeHere).getBlock();
                ItemStack tool = itemHandler.getStackInSlot(0);
                int damageValue = this.itemHandler.getStackInSlot(0).getDamageValue();


                blockDrops = Block.getDrops(block.defaultBlockState(), (ServerLevel) this.level, placeHere, this.level.getBlockEntity(pos), null, tool);
                SoundType blockSounds = this.level.getBlockState(placeHere).getBlock().getSoundType(this.level.getBlockState(placeHere).getBlock().defaultBlockState(), this.level, pos, null);


                if (level.getBlockState(placeHere).getBlock() != Blocks.AIR && !blockState.is(Blocks.VOID_AIR) && !blockState.isAir()) {

                    this.progress++;
                    if (progress >= maxProgress) {
                        progress = 0;

                        if (tool.getItem().isCorrectToolForDrops(tool, block.defaultBlockState()) || !block.defaultBlockState().requiresCorrectToolForDrops()) {

                            if (this.itemHandler.getStackInSlot(1).is(Item.byBlock(block)) && this.itemHandler.getStackInSlot(2).isEmpty()) {

                                this.level.setBlockAndUpdate(placeHere, Blocks.AIR.defaultBlockState());


                                for (ItemStack drop : blockDrops) {
                                    spawnBlockAsEntity(this.level, placeHere, drop);
                                }
                                if (tool.isDamageableItem()) {
                                    this.itemHandler.getStackInSlot(0).hurtAndBreak(1, RandomSource.create(), null, null);
                                    level.playSound(null, pos, blockSounds.getBreakSound(), SoundSource.BLOCKS, (float) 1, 1);
                                }
                                if (damageValue + 1 == tool.getMaxDamage()) {
                                    this.itemHandler.extractItem(0, 1, false);
                                    level.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1, 1);
                                }

                            }

                            else if (!this.itemHandler.getStackInSlot(2).is(Item.byBlock(block)) && this.itemHandler.getStackInSlot(1).isEmpty()) {

                                this.level.setBlockAndUpdate(placeHere, Blocks.AIR.defaultBlockState());


                                for (ItemStack drop : blockDrops) {
                                    spawnBlockAsEntity(this.level, placeHere, drop);
                                }
                                if (tool.isDamageableItem()) {
                                    this.itemHandler.getStackInSlot(0).hurtAndBreak(1, RandomSource.create(), null, null);
                                    level.playSound(null, pos, blockSounds.getBreakSound(), SoundSource.BLOCKS, (float) 1, 1);
                                }
                                if (damageValue + 1 == tool.getMaxDamage()) {
                                    this.itemHandler.extractItem(0, 1, false);
                                    level.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1, 1);
                                }

                            }

                            else if (this.itemHandler.getStackInSlot(1).isEmpty() && this.itemHandler.getStackInSlot(2).isEmpty()) {
                                //break block with/ without the needing the tool (no filter)

                                this.level.setBlockAndUpdate(placeHere, Blocks.AIR.defaultBlockState());


                                for (ItemStack drop : blockDrops) {
                                    spawnBlockAsEntity(this.level, placeHere, drop);
                                }
                                if (tool.isDamageableItem()) {
                                    this.itemHandler.getStackInSlot(0).hurtAndBreak(1, RandomSource.create(), null, null);
                                    level.playSound(null, pos, blockSounds.getBreakSound(), SoundSource.BLOCKS, (float) 1, 1);
                                }
                                if (damageValue + 1 == tool.getMaxDamage()) {
                                    this.itemHandler.extractItem(0, 1, false);
                                    level.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1, 1);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!this.level.isClientSide()) {

            setChanged();
        //    ModMessages.sendToClients(new PacketSyncItemStackToClient(this.itemHandler, this.worldPosition));
        }


    }




}
