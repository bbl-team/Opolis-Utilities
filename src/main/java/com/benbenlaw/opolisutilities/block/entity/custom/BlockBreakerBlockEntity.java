package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.handler.InputOutputItemHandler;
import com.benbenlaw.opolisutilities.screen.custom.BlockBreakerMenu;
import com.benbenlaw.opolisutilities.util.DirectionUtils;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import com.mojang.authlib.GameProfile;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.benbenlaw.opolisutilities.block.custom.BlockBreakerBlock.FACING;
import static com.benbenlaw.opolisutilities.block.custom.BlockBreakerBlock.POWERED;

public class BlockBreakerBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            sync();
        }

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            if (slot == 1 || slot == 2)
                return 1;
            return super.getStackLimit(slot, stack);
        }
    };

    private FakePlayer fakePlayer;

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
    public int maxProgress = 0;
    private static final int INPUT_SLOT = 0;

    //Item Handler Per Sides
    private final IItemHandler upItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> i == INPUT_SLOT, // Allow in INPUT_SLOT
            i -> false // No output slots
    );
    private final IItemHandler downItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> i == INPUT_SLOT, // Allow in INPUT_SLOT
            i -> false // No output slots
    );
    private final IItemHandler northItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> i == INPUT_SLOT, // Allow in INPUT_SLOT
            i -> false // No output slots
    );
    private final IItemHandler southItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> i == INPUT_SLOT, // Allow in INPUT_SLOT
            i -> false // No output slots
    );
    private final IItemHandler eastItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> i == INPUT_SLOT, // Allow in INPUT_SLOT
            i -> false // No output slots
    );
    private final IItemHandler westItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> i == INPUT_SLOT, // Only allow insertion in slot 0
            i -> false // No output slots on the top
    );

    //If sides don't need to be handled use this
    private final IItemHandler noSideItemHandlerSided = new InputOutputItemHandler(itemHandler,
            (i, stack) -> false,
            i -> false
    );

    //Called in startup for sides of the block
    public @Nullable IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        side = DirectionUtils.adjustPosition(this.getBlockState().getValue(FACING), side);
        if(side == null)
            return itemHandler;

        if(side == Direction.UP)
            return upItemHandlerSide;
        if(side == Direction.DOWN)
            return downItemHandlerSide;
        if(side == Direction.NORTH)
            return northItemHandlerSide;
        if(side == Direction.SOUTH)
            return southItemHandlerSide;
        if(side == Direction.EAST)
            return eastItemHandlerSide;
        if(side == Direction.WEST)
            return westItemHandlerSide;

        return noSideItemHandlerSided;
    }

    public void setHandler(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            this.itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
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


        // Initialize fakePlayer if level is a ServerLevel
        if (level instanceof ServerLevel serverLevel) {
            this.fakePlayer = createFakePlayer(serverLevel);
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.opolisutilities.block_breaker");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new BlockBreakerMenu(container, inventory, this.getBlockPos(), data);
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
        compoundTag.putInt("block_breaker.progress", progress);
        compoundTag.putInt("block_breaker.maxProgress", maxProgress);
    }
    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        this.itemHandler.deserializeNBT(provider, compoundTag.getCompound("inventory"));
        progress = compoundTag.getInt("block_breaker.progress");
        maxProgress = compoundTag.getInt("block_breaker.maxProgress");
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

    //Spawn Block As Entity Method inspired by https://github.com/stfwi/engineers-decor/blob/1.19/src/main/java/wile/engineersdecor/blocks/EdBreaker.java
    private static void spawnBlockAsEntity(Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide && !stack.isEmpty()) {
            level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS);
        }
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

                boolean blockRequiresCorrectTool = block.defaultBlockState().requiresCorrectToolForDrops();
                boolean hasCorrectTool = tool.isCorrectToolForDrops(block.defaultBlockState());

                if (level.getBlockState(placeHere).getBlock() != Blocks.AIR && !blockState.is(Blocks.VOID_AIR) && !blockState.isAir()) {

                    if (!blockRequiresCorrectTool || hasCorrectTool) {

                        // Calculate the time it takes to break the block
                        float destroySpeed = block.defaultBlockState().getDestroySpeed(this.level, pos);
                        float explosionResistance = block.defaultBlockState().getExplosionResistance(this.level, pos,
                                new Explosion(level, null, 0, 0, 0, 0, false, Explosion.BlockInteraction.KEEP));
                        float combined = destroySpeed + explosionResistance;
                        int calculatedBreakTime = (int) (combined * 50);

                        if (calculatedBreakTime > 1000) {
                            calculatedBreakTime = 1000;
                        }

                        maxProgress = calculatedBreakTime;

                        // Play the breaking sound
                        playBreakingSound(level, placeHere);

                        // Progress the breaking
                        this.progress++;
                        if (progress >= maxProgress) {
                            progress = 0;

                            if (this.itemHandler.getStackInSlot(1).is(Item.BY_BLOCK.get(block)) && this.itemHandler.getStackInSlot(2).isEmpty()) {

                                this.level.setBlockAndUpdate(placeHere, Blocks.AIR.defaultBlockState());

                                for (ItemStack drop : blockDrops) {
                                    spawnBlockAsEntity(this.level, placeHere, drop);
                                    maxProgress = 0;
                                }
                                if (tool.isDamageableItem()) {
                                    this.itemHandler.getStackInSlot(0).hurtAndBreak(1, createFakePlayer((ServerLevel) level), fakePlayer.getEquipmentSlotForItem(tool));
                                    playBrokenSound(level, placeHere);
                                }
                                if (damageValue + 1 == tool.getMaxDamage()) {
                                    this.itemHandler.extractItem(0, 1, false);
                                    level.playSound(null, placeHere, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1, 1);
                                }

                            } else if (!this.itemHandler.getStackInSlot(2).is(Item.BY_BLOCK.get(block)) && this.itemHandler.getStackInSlot(1).isEmpty()) {

                                this.level.setBlockAndUpdate(placeHere, Blocks.AIR.defaultBlockState());

                                for (ItemStack drop : blockDrops) {
                                    spawnBlockAsEntity(this.level, placeHere, drop);
                                    maxProgress = 0;

                                }
                                if (tool.isDamageableItem()) {
                                    this.itemHandler.getStackInSlot(0).hurtAndBreak(1, createFakePlayer((ServerLevel) level), fakePlayer.getEquipmentSlotForItem(tool));
                                    playBrokenSound(level, placeHere);
                                }
                                if (damageValue + 1 == tool.getMaxDamage()) {
                                    this.itemHandler.extractItem(0, 1, false);
                                    level.playSound(null, placeHere, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1, 1);
                                }

                            } else if (this.itemHandler.getStackInSlot(1).isEmpty() && this.itemHandler.getStackInSlot(2).isEmpty()) {
                                //break block with/ without the needing the tool (no filter)

                                this.level.setBlockAndUpdate(placeHere, Blocks.AIR.defaultBlockState());

                                for (ItemStack drop : blockDrops) {
                                    spawnBlockAsEntity(this.level, placeHere, drop);
                                    maxProgress = 0;

                                }
                                if (tool.isDamageableItem()) {
                                    this.itemHandler.getStackInSlot(0).hurtAndBreak(1, createFakePlayer((ServerLevel) level), fakePlayer.getEquipmentSlotForItem(tool));
                                    playBrokenSound(level, placeHere);
                                }
                                if (damageValue + 1 == tool.getMaxDamage()) {
                                    this.itemHandler.extractItem(0, 1, false);
                                    level.playSound(null, placeHere, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1, 1);
                                }
                            }

                        }
                    } else {
                        progress = 0;
                        maxProgress = 0;
                        sync();
                    }
                }
            }
        }
        if (!this.level.isClientSide()) {

            setChanged();
            sync();
        }
    }


    // PLAY BLOCK BREAKING SOUND //
    int playingSound = 0;
    private void playBreakingSound(Level level, BlockPos blockPos) {

        playingSound++;
        if (playingSound / 10 == 1) {
            SoundType blockSound = level.getBlockState(blockPos).getSoundType(level, blockPos, null);
            level.playSound(null, blockPos, blockSound.getHitSound(),
                    SoundSource.BLOCKS, 1, 1);
            playingSound = 0;
        }
    }

    private void playBrokenSound(Level level, BlockPos blockPos) {

        SoundType blockSound = level.getBlockState(blockPos).getSoundType(level, blockPos, null);
        level.playSound(null, blockPos, blockSound.getBreakSound(),
                SoundSource.BLOCKS, 1, 1);
    }

    private FakePlayer createFakePlayer(ServerLevel level) {
        return new FakePlayer(level, new GameProfile(UUID.randomUUID(), "BlockBreaker"));
    }
}
