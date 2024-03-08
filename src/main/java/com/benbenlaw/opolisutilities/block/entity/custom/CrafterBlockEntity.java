package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.networking.ModMessages;
import com.benbenlaw.opolisutilities.networking.packets.PacketSyncItemStackToClient;
import com.benbenlaw.opolisutilities.screen.CrafterMenu;
import com.benbenlaw.opolisutilities.util.EmptyContainer;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import com.benbenlaw.opolisutilities.util.inventory.WrappedHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
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
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CrafterBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            assert level != null;
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
    public ItemStack craftingItem = ItemStack.EMPTY;
    private NonNullList<Ingredient> craftingIngredients;

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
        return Component.literal("Crafter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player) {
        return new CrafterMenu(containerID, inventory, this, this.data);
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
        tag.putInt("crafter.progress", progress);
        tag.putInt("crafter.maxProgress", maxProgress);

        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("crafter.progress");
        maxProgress = nbt.getInt("crafter.maxProgress");
    }


    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void tick() {

        this.updateRecipe();
        if (!level.isClientSide()) {

            if (!this.craftingItem.isEmpty() && this.canCraft() && this.hasMaterial()){
                this.progress++;
                if (this.progress >= this.maxProgress) {
                    this.progress = 0;
                    this.craft();
                }
            } else this.progress = 0;
            this.setChanged();

        }

    }

    public void updateRecipe() {
        CraftingContainer container = new CraftingContainer() {
            @Override
            public int getWidth() {return 3;}
            @Override
            public int getHeight() {return 3;}
            @Override
            public List<ItemStack> getItems() {
                List<ItemStack> list = new ArrayList<>();
                for (int i = 0; i < 9; i++) list.add(Optional.of(itemHandler.getStackInSlot(i)).orElse(ItemStack.EMPTY));
                return list;
            }
            @Override
            public int getContainerSize() {return getWidth() * getHeight();}
            @Override
            public boolean isEmpty() {return false;}
            @Override
            public ItemStack getItem(int pSlot) {
                return getItems().get(pSlot);
            }
            @Override
            public ItemStack removeItem(int pSlot, int pAmount) {return ItemStack.EMPTY;}
            @Override
            public ItemStack removeItemNoUpdate(int pSlot) {return ItemStack.EMPTY;}
            @Override
            public void setItem(int pSlot, ItemStack pStack) {}
            @Override
            public void setChanged() {}
            @Override
            public boolean stillValid(Player pPlayer) {return true;}
            @Override
            public void clearContent() {}
            @Override
            public void fillStackedContents(StackedContents pHelper) {}
        };
        Optional<CraftingRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, container, level);
        if (recipe.isPresent()) {
            CraftingRecipe r = recipe.get();
            craftingItem = r.getResultItem(RegistryAccess.EMPTY).copy();
            craftingIngredients = r.getIngredients();
        } else {
            craftingItem = ItemStack.EMPTY.copy();
        }
    }

    public boolean canCraft() {
        ItemStack stack = itemHandler.getStackInSlot(9);
        int count = stack.getCount();
        boolean same = stack.getItem() == craftingItem.getItem();
        boolean fits = count + craftingItem.getCount() <= craftingItem.getMaxStackSize();
        return stack.isEmpty() || (same && fits);
    }

    public void craft() {
        extractIngredients();
        itemHandler.insertItem(9, craftingItem.copy(), false);
    }

    public void extractIngredients() {
        HashMap<Item, Integer> itemCount = countIngredients(craftingIngredients);
        for (int i = 0; i < 8 && itemCount.size() > 0; i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!itemCount.containsKey(stack.getItem())) continue;
            int needs = itemCount.get(stack.getItem());
            int amount = Math.min(stack.getCount(), needs);
            itemHandler.extractItem(i, amount, false);
            if (!stack.getCraftingRemainingItem().isEmpty()) {
                if (!tryInserting(stack.getCraftingRemainingItem())) {
                    Containers.dropItemStack(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), stack.getCraftingRemainingItem());
                }
            }
            int remaining = needs - amount;
            if (remaining > 0) {
                itemCount.put(stack.getItem(), remaining);
            } else itemCount.remove(stack.getItem());
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
        HashMap<Item, Integer> itemCount = countIngredients(craftingIngredients);
        return checkContents(itemCount);
    }

    private boolean checkContents(HashMap<Item, Integer> itemCount) {
        for (int i = 0; i < 8 && itemCount.size() > 0; i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!itemCount.containsKey(stack.getItem())) continue;
            int remaining = itemCount.get(stack.getItem()) - stack.getCount();
            if (remaining > 0) {
                itemCount.put(stack.getItem(), remaining);
            } else itemCount.remove(stack.getItem());
        }
        return itemCount.size() == 0;
    }

    private HashMap<Item, Integer> countIngredients(NonNullList<Ingredient> ingredients) {
        HashMap<Item, Integer> map = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getItems().length == 0) continue;
            ItemStack stack = ingredient.getItems()[0];
            map.put(stack.getItem(), map.getOrDefault(stack.getItem(), 0) + stack.getCount());
        }
        return map;
    }

    private boolean isItemCompatible(ItemStack stack) {
        if (craftingIngredients == null) return false;
        for (Ingredient ingredient : craftingIngredients) {
            if (ingredient.test(stack)) return true;
        }
        return false;
    }


}
