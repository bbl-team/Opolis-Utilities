package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.handler.InputOutputItemHandler;
import com.benbenlaw.opolisutilities.screen.custom.CrafterMenu;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.benbenlaw.opolisutilities.block.custom.CrafterBlock.POWERED;

public class CrafterBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            sync();
        }

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            if (slot <= 8)
                return 2;
            return super.getStackLimit(slot, stack);
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
    private int recipeChecker = 0;
    public ItemStack craftingItem = ItemStack.EMPTY;

    public ResourceLocation recipeID = ResourceLocation.parse("minecraft:air");
    private NonNullList<Ingredient> craftingIngredients;

    // Item Handler Per Sides
    private final IItemHandler crafterItemHandler = new InputOutputItemHandler(itemHandler,
            (i, stack) -> {
                if (i >= 0 && i <= 8 && itemHandler.isItemValid(i, stack)) {
                    return isIngredientValidForSlot(stack, i);
                }
                return false;
            },
            i -> i == 9
    );

    private boolean isIngredientValidForSlot(ItemStack insertedItem, int slotIndex) {
        if (craftingIngredients != null && slotIndex < craftingIngredients.size()) {
            Ingredient ingredient = craftingIngredients.get(slotIndex);
            return ingredient.test(insertedItem);
        }
        return false;
    }

    // Called in startup for sides of the block
    public @Nullable IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        return crafterItemHandler;
    }

    public void setHandler(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            this.itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    public ItemStackHandler getItemStackHandler() {
        return this.itemHandler;
    }

    public CrafterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CRAFTER_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> CrafterBlockEntity.this.progress;
                    case 1 -> CrafterBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CrafterBlockEntity.this.progress = value;
                    case 1 -> CrafterBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.opolisutilities.crafter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new CrafterMenu(container, inventory, this.getBlockPos(), data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        getCraftingIngredients();
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
        compoundTag.putInt("recipeChecker", recipeChecker);
        compoundTag.putString("recipeID", recipeID.toString());
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        this.itemHandler.deserializeNBT(provider, compoundTag.getCompound("inventory"));
        progress = compoundTag.getInt("progress");
        maxProgress = compoundTag.getInt("maxProgress");
        recipeChecker = compoundTag.getInt("recipeChecker");
        recipeID = ResourceLocation.parse(compoundTag.getString("recipeID"));

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

    private void resetProgress() {
        this.progress = 0;
        setChanged();
    }

    public void tick() {
        recipeChecker++;
        Level level = this.level;
        assert level != null;
        BlockPos blockPos = this.worldPosition;
        BlockState blockState = level.getBlockState(blockPos);
        sync();

        if (!level.isClientSide()) {
            if (blockState.getValue(POWERED)) {
                if (!craftingItem.isEmpty() && canCraft() && hasMaterial()) {
                    progress++;
                    if (progress >= maxProgress) {
                        craft();
                        resetProgress();
                        setChanged();
                    }
                } else {
                    resetProgress();
                    setChanged();
                }
            } else {
                resetProgress();
                setChanged();
            }
        }
    }

    public boolean canCraft() {
        ItemStack stack = itemHandler.getStackInSlot(9);
        int count = stack.getCount();
        boolean same = ItemStack.isSameItem(stack, craftingItem) && ItemStack.isSameItemSameComponents(stack, craftingItem);
        boolean fits = count + craftingItem.getCount() <= craftingItem.getMaxStackSize();
        return stack.isEmpty() || (same && fits);
    }

    public void updateRecipe() {
        CraftingContainer container = new CraftingContainer() {
            @Override
            public int getWidth() {
                return 3;
            }

            @Override
            public int getHeight() {
                return 3;
            }

            @Override
            public @NotNull List<ItemStack> getItems() {
                List<ItemStack> list = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    list.add(itemHandler.getStackInSlot(i));
                }
                return list;
            }

            @Override
            public int getContainerSize() {
                return getWidth() * getHeight();
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public @NotNull ItemStack getItem(int slot) {
                return getItems().get(slot);
            }

            @Override
            public @NotNull ItemStack removeItem(int slot, int amount) {
                return ItemStack.EMPTY;
            }

            @Override
            public @NotNull ItemStack removeItemNoUpdate(int slot) {
                return ItemStack.EMPTY;
            }

            @Override
            public void setItem(int slot, @NotNull ItemStack stack) {
            }

            @Override
            public void setChanged() {
            }

            @Override
            public boolean stillValid(@NotNull Player player) {
                return true;
            }

            @Override
            public void clearContent() {
            }

            @Override
            public void fillStackedContents(@NotNull StackedContents helper) {
            }
        };

        for (int i = 0; i < 9; i++) {
            container.setItem(i, itemHandler.getStackInSlot(i));
        }

        assert level != null;
        Optional<RecipeHolder<CraftingRecipe>> recipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, container.asCraftInput(), level);
        if (recipe.isPresent()) {
            CraftingRecipe r = recipe.get().value();
            craftingItem = r.getResultItem(RegistryAccess.EMPTY).copy();
            craftingIngredients = r.getIngredients();
            recipeID = recipe.get().id();
            System.out.println("Recipe found: " + recipeID);
        } else {
            craftingItem = ItemStack.EMPTY.copy();
            System.out.println("No recipe found.");
        }
    }

    public void getCraftingIngredients() {
        assert level != null;
        Optional<RecipeHolder<?>> recipe = level.getRecipeManager().byKey(recipeID);
        if (recipe.isPresent()) {
            CraftingRecipe r = (CraftingRecipe) recipe.get().value();
            craftingItem = r.getResultItem(RegistryAccess.EMPTY).copy();
            craftingIngredients = r.getIngredients();
            recipeID = recipe.get().id();
        } else {
            craftingItem = ItemStack.EMPTY.copy();
        }
    }

    public void craft() {
        extractIngredients();
        itemHandler.insertItem(9, craftingItem.copy(), false);
    }

    public void extractIngredients() {
        if (craftingIngredients != null) {
            for (int i = 0; i < 9; i++) {
                if (i >= craftingIngredients.size()) break;
                ItemStack stackInSlot = itemHandler.getStackInSlot(i);
                if (!stackInSlot.isEmpty()) {
                    ItemStack extractedStack = itemHandler.extractItem(i, 1, false);
                    if (!extractedStack.isEmpty() && !extractedStack.getCraftingRemainingItem().isEmpty()) {
                        Containers.dropItemStack(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), extractedStack.getCraftingRemainingItem());
                    }
                }
            }
        }
    }

    private boolean tryInserting(ItemStack stack) {
        for (int i = 0; i < 8; i++) {
            if (itemHandler.insertItem(i, stack, true).isEmpty()) {
                itemHandler.insertItem(i, stack, false);
                return true;
            }
        }
        return false;
    }

    public boolean hasMaterial() {
        return hasIngredientsForRecipe();
    }

    private boolean hasIngredientsForRecipe() {
        if (craftingIngredients == null || craftingIngredients.isEmpty()) {
            return false;
        }

        for (int i = 0; i < 9; i++) {
            if (i < craftingIngredients.size()) {
                ItemStack slotStack = itemHandler.getStackInSlot(i);
                Ingredient ingredient = craftingIngredients.get(i);
                if (!ingredient.test(slotStack)) {
                    return false;
                }
            } else {
                if (!itemHandler.getStackInSlot(i).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private HashMap<Item, Integer> countIngredients(NonNullList<Ingredient> ingredients) {
        HashMap<Item, Integer> map = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            for (ItemStack stack : ingredient.getItems()) {
                map.put(stack.getItem(), map.getOrDefault(stack.getItem(), 0) + stack.getCount());
            }
        }
        return map;
    }
}
