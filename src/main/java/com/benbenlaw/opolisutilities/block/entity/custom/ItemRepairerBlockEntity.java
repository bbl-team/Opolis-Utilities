package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.handler.InputOutputItemHandler;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.benbenlaw.opolisutilities.recipe.SpeedUpgradesRecipe;
import com.benbenlaw.opolisutilities.screen.custom.ItemRepairerMenu;
import com.benbenlaw.opolisutilities.util.ModTags;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

import static com.benbenlaw.opolisutilities.block.custom.ItemRepairerBlock.POWERED;

public class ItemRepairerBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            sync();
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
    public int maxProgress = 220;
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    public static final int UPGRADE_SLOT = 2;
    private final IItemHandler itemRepairerHandler = new InputOutputItemHandler(itemHandler,
            (i, stack ) -> i == INPUT_SLOT && !stack.is(ModTags.Items.BANNED_IN_ITEM_REPAIRER),
            i -> i == OUTPUT_SLOT
    );

    public @Nullable IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        return itemRepairerHandler;
    }

    public void setHandler(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            this.itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    public ItemStackHandler getItemStackHandler() {
        return this.itemHandler;
    }

    public ItemRepairerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.ITEM_REPAIRER_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> ItemRepairerBlockEntity.this.progress;
                    case 1 -> ItemRepairerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ItemRepairerBlockEntity.this.progress = value;
                    case 1 -> ItemRepairerBlockEntity.this.maxProgress = value;
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
    public Component getDisplayName() {
        return Component.translatable("block.opolisutilities.item_repairer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new ItemRepairerMenu(container, inventory, this.getBlockPos(), data);
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

        Level level = this.level;
        BlockPos blockPos = this.worldPosition;
        assert level != null;
        BlockState blockState = level.getBlockState(blockPos);
        ItemRepairerBlockEntity blockEntity = this;
        ItemStack inputAsStack = new ItemStack(blockEntity.itemHandler.getStackInSlot(0).getItem());
        boolean isDamaged = inputAsStack.isDamaged();
        int damageValue = blockEntity.itemHandler.getStackInSlot(0).getDamageValue();
        ItemStack stackInSlot0 = blockEntity.itemHandler.getStackInSlot(0);
        ItemStack copiedStack = stackInSlot0.copy();

        if (!level.isClientSide()) {
            // Ensure fakePlayer is initialized
            if (this.fakePlayer == null && level instanceof ServerLevel serverLevel) {
                this.fakePlayer = createFakePlayer(serverLevel);
            }

            // Set Tickrate
            for (RecipeHolder<SpeedUpgradesRecipe> match : level.getRecipeManager().getRecipesFor(SpeedUpgradesRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {
                NonNullList<Ingredient> input = match.value().getIngredients();
                for (Ingredient ingredient : input) {
                    for (ItemStack itemStack : ingredient.getItems()) {

                        if (this.itemHandler.getStackInSlot(2).is(itemStack.getItem())) {
                            maxProgress = match.value().tickRate();
                            break;
                        }
                    }
                }
            }

            // Reset if upgrade is removed
            if (itemHandler.getStackInSlot(2).isEmpty()) {
                maxProgress = 220;
            }

            if (inputAsStack.isDamageableItem() && !(damageValue == 0) && blockState.getValue(POWERED)) {
                blockEntity.progress++;
                playBreakingSound(level, blockPos);
                if (blockEntity.progress > blockEntity.maxProgress) {

                    if (!isDamaged) {
                        blockEntity.itemHandler.getStackInSlot(0).hurtAndBreak(-1, fakePlayer, fakePlayer.getEquipmentSlotForItem(ItemStack.EMPTY));
                        level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS, (float) 0.5, 3, false);
                    }
                    blockEntity.resetProgress();
                    setChanged();
                    sync();
                }
            }

            if (inputAsStack.isDamageableItem() && damageValue == 0 && blockEntity.itemHandler.getStackInSlot(1).isEmpty()) {

                blockEntity.itemHandler.setStackInSlot(1, copiedStack);
                blockEntity.itemHandler.extractItem(0, 1, false);
                blockEntity.resetProgress();
                playCompletedSound(level, blockPos);
                setChanged();
                sync();
            }

            if (!inputAsStack.isDamageableItem() && blockEntity.itemHandler.getStackInSlot(1).isEmpty()) {
                blockEntity.itemHandler.setStackInSlot(1, copiedStack);
                blockEntity.itemHandler.extractItem(0, copiedStack.getCount(), false);
                blockEntity.resetProgress();
                setChanged();
                sync();
            }
        }
    }

    private FakePlayer createFakePlayer(ServerLevel level) {
        return new FakePlayer(level, new GameProfile(UUID.randomUUID(), "ItemRepairer"));
    }

    private void resetProgress() {
        this.progress = 0;
    }

    // PLAY REPAIRER SOUND //
    int playingSound = 0;
    private void playBreakingSound(Level level, BlockPos blockPos) {
        playingSound++;
        if (playingSound / 30 == 1) {
            level.playSound(null, blockPos, SoundEvents.ANVIL_USE,
                    SoundSource.BLOCKS, 0.3f, 1);
            playingSound = 0;
        }
    }

    // PLAY COMPLETED SOUND //
    private void playCompletedSound(Level level, BlockPos blockPos) {
        level.playSound(null, blockPos, SoundEvents.PLAYER_LEVELUP,
                SoundSource.BLOCKS, 0.3f, 1);
    }
}
