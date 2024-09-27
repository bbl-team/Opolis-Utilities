package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.handler.InputOutputItemHandler;
import com.benbenlaw.opolisutilities.recipe.ClocheRecipe;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.benbenlaw.opolisutilities.recipe.SpeedUpgradesRecipe;
import com.benbenlaw.opolisutilities.screen.custom.ClocheMenu;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
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
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

import static com.benbenlaw.opolisutilities.block.custom.ClocheBlock.POWERED;

public class ClocheBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(8) {

        @Override
        protected int getStackLimit(int slot, ItemStack stack) {

            if(slot == UPGRADE_SLOT || slot == CATALYST_SLOT || slot == SOIL_SLOT || slot == SEED_SLOT) {
                return 1;
            }

            return slot;
        }

        @Override
        protected void onContentsChanged(int slot) {
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


    public ItemStack getSoilBlock() {
        return itemHandler.getStackInSlot(SOIL_SLOT);
    }

    public ItemStack getSeed() {
        return itemHandler.getStackInSlot(SEED_SLOT);
    }


    public final ContainerData data;
    public int progress = 0;
    public int maxProgress = 80;
    public static final int SEED_SLOT = 0;
    public static final int SOIL_SLOT = 1;
    public static final int CATALYST_SLOT = 2;
    public static final int UPGRADE_SLOT = 3;
    public static final int[] OUTPUT_SLOTS = {4, 5, 6, 7};


    private final IItemHandler clocheItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> false,
            this::isOutputSlot
    );

    private boolean isOutputSlot(int slot) {
        for (int outputSlot : OUTPUT_SLOTS) {
            if (slot == outputSlot) {
                return true;
            }
        }
        return false;
    }

    public IItemHandler getItemHandlerCapability(Direction side) {
        return clocheItemHandlerSide;
    }

    public void setHandler(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            this.itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    public ItemStackHandler getItemStackHandler() {
        return this.itemHandler;
    }

    public ClocheBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CLOCHE_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> ClocheBlockEntity.this.progress;
                    case 1 -> ClocheBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ClocheBlockEntity.this.progress = value;
                    case 1 -> ClocheBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.opolisutilities.cloche");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new ClocheMenu(container, inventory, this.getBlockPos(), data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.setChanged();
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


    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
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
        BlockPos blockPos = this.worldPosition;
        BlockState blockState = level.getBlockState(blockPos);

        assert level != null;
        if (!level.isClientSide()) {
            sync();

            maxProgress = 220;

            for (RecipeHolder<SpeedUpgradesRecipe> match : level.getRecipeManager().getRecipesFor(SpeedUpgradesRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {
                NonNullList<Ingredient> input = match.value().getIngredients();
                for (Ingredient ingredient : input) {
                    for (ItemStack itemStack : ingredient.getItems()) {
                        if (this.itemHandler.getStackInSlot(UPGRADE_SLOT).is(itemStack.getItem())) {
                            maxProgress = match.value().tickRate();
                            break;
                        }
                    }
                }
            }

            // Reset if upgrade is removed
            if (itemHandler.getStackInSlot(UPGRADE_SLOT).isEmpty()) {
                maxProgress = 220;
            }

            if (itemHandler.getStackInSlot(SEED_SLOT).isEmpty() || itemHandler.getStackInSlot(SOIL_SLOT).isEmpty()) {
                progress = 0;
            }

            RecipeInput inventory = new RecipeInput() {
                @Override
                public @NotNull ItemStack getItem(int index) {
                    return itemHandler.getStackInSlot(index);
                }

                @Override
                public int size() {
                    return itemHandler.getSlots();
                }
            };

            Optional<RecipeHolder<ClocheRecipe>> match = level.getRecipeManager()
                    .getRecipeFor(ClocheRecipe.Type.INSTANCE, inventory, level);

            match.ifPresent(clocheRecipeRecipeHolder -> maxProgress = (int) (maxProgress * clocheRecipeRecipeHolder.value().durationModifier()));

            if (match.isPresent() && blockState.getValue(POWERED) &&
                    ( match.get().value().catalyst().test(inventory.getItem(CATALYST_SLOT)) || match.get().value().catalyst().isEmpty()) ) {

                boolean allSlotsFull = true;

                Item mainOutput = match.get().value().mainOutput().getItem().asItem();
                Item chanceOutput1 = match.get().value().chanceOutput1().getItem().asItem();
                Item chanceOutput2 = match.get().value().chanceOutput2().getItem().asItem();
                Item chanceOutput3 = match.get().value().chanceOutput3().getItem().asItem();

                for (int slot = 4; slot <= 7; slot++) {
                    ItemStack currentStack = this.itemHandler.getStackInSlot(slot);

                    if (currentStack.isEmpty() ||
                            (currentStack.getItem() == mainOutput && currentStack.getCount() < currentStack.getMaxStackSize()) ||
                            (currentStack.getItem() == chanceOutput1 && currentStack.getCount() < currentStack.getMaxStackSize()) ||
                            (currentStack.getItem() == chanceOutput2 && currentStack.getCount() < currentStack.getMaxStackSize()) ||
                            (currentStack.getItem() == chanceOutput3 && currentStack.getCount() < currentStack.getMaxStackSize())) {
                        allSlotsFull = false;
                        break;
                    }
                }

                // If all slots are full, return early without increasing progress
                if (allSlotsFull) {
                    return;
                }

                // Increase progress after confirming that slots are not full
                progress++;

                if (progress >= maxProgress) {
                    boolean mainOutputPlaced = false;
                    boolean chanceOutput1Placed = false;
                    boolean chanceOutput2Placed = false;
                    boolean chanceOutput3Placed = false;

                    // Place Main Output
                    for (int slot = 4; slot <= 7; slot++) {
                        ItemStack currentStack = this.itemHandler.getStackInSlot(slot);

                        if (currentStack.isEmpty()) {
                            this.itemHandler.setStackInSlot(slot, new ItemStack(mainOutput));
                            mainOutputPlaced = true;
                            setChanged();
                        } else if (currentStack.getItem() == mainOutput && currentStack.getCount() < currentStack.getMaxStackSize()) {
                            int amountToAdd = Math.min(currentStack.getMaxStackSize() - currentStack.getCount(), 1);
                            currentStack.grow(amountToAdd);
                            mainOutputPlaced = true;
                            setChanged();
                        }

                        if (mainOutputPlaced) break;
                    }

                    // Place ChanceOutput 1
                    if (Math.random() < match.get().value().chanceOutputChance1()) {
                        for (int slot = 4; slot <= 7; slot++) {
                            ItemStack currentStack = this.itemHandler.getStackInSlot(slot);

                            if (currentStack.isEmpty()) {
                                this.itemHandler.setStackInSlot(slot, new ItemStack(chanceOutput1));
                                chanceOutput1Placed = true;
                                setChanged();
                            } else if (currentStack.getItem() == chanceOutput1 && currentStack.getCount() < currentStack.getMaxStackSize()) {
                                int amountToAdd = Math.min(currentStack.getMaxStackSize() - currentStack.getCount(), 1);
                                currentStack.grow(amountToAdd);
                                chanceOutput1Placed = true;
                                setChanged();
                            }

                            if (chanceOutput1Placed) break;
                        }
                    }

                    // Place ChanceOutput 2
                    if (Math.random() < match.get().value().chanceOutputChance2()) {
                        for (int slot = 4; slot <= 7; slot++) {
                            ItemStack currentStack = this.itemHandler.getStackInSlot(slot);

                            if (currentStack.isEmpty()) {
                                this.itemHandler.setStackInSlot(slot, new ItemStack(chanceOutput2));
                                chanceOutput2Placed = true;
                                setChanged();
                            } else if (currentStack.getItem() == chanceOutput2 && currentStack.getCount() < currentStack.getMaxStackSize()) {
                                int amountToAdd = Math.min(currentStack.getMaxStackSize() - currentStack.getCount(), 1);
                                currentStack.grow(amountToAdd);
                                chanceOutput2Placed = true;
                                setChanged();
                            }

                            if (chanceOutput2Placed) break;
                        }
                    }

                    // Place ChanceOutput 3
                    if (Math.random() < match.get().value().chanceOutputChance3()) {
                        for (int slot = 4; slot <= 7; slot++) {
                            ItemStack currentStack = this.itemHandler.getStackInSlot(slot);

                            if (currentStack.isEmpty()) {
                                this.itemHandler.setStackInSlot(slot, new ItemStack(chanceOutput3));
                                chanceOutput3Placed = true;
                                setChanged();
                            } else if (currentStack.getItem() == chanceOutput3 && currentStack.getCount() < currentStack.getMaxStackSize()) {
                                int amountToAdd = Math.min(currentStack.getMaxStackSize() - currentStack.getCount(), 1);
                                currentStack.grow(amountToAdd);
                                chanceOutput3Placed = true;
                                setChanged();
                            }

                            if (chanceOutput3Placed) break;
                        }
                    }

                    if (mainOutputPlaced || chanceOutput1Placed || chanceOutput2Placed || chanceOutput3Placed) {
                        progress = 0;
                    }
                }
            }
        }
    }
}