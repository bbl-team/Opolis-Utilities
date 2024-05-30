package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.screen.BlockBreakerMenu;
import com.benbenlaw.opolisutilities.screen.CrafterMenu;
import com.benbenlaw.opolisutilities.util.DirectionUtils;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.benbenlaw.opolisutilities.block.custom.CrafterBlock.FACING;
import static com.benbenlaw.opolisutilities.block.custom.CrafterBlock.POWERED;

public class CrafterBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            if (slot <= 8)
                return 2;
            return super.getStackLimit(slot, stack);
        }
    };

    public final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;
    private int recipeChecker = 0;
        public ItemStack craftingItem = ItemStack.EMPTY;
    private NonNullList<Ingredient> craftingIngredients;

    //Item Handler Per Sides
    private final IItemHandler upItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> { // Input condition
                if (i >= 0 && i <= 8 && itemHandler.isItemValid(i, stack)) {
                    ItemStack slotStack = itemHandler.getStackInSlot(i);
                    return !slotStack.isEmpty() && ItemStack.isSameItem(slotStack, stack) && slotStack.getCount() < 2;  // Allow insertion if the item matches and count is less than 2
                }
                return false;
            },
            i -> false // No output slots
    );
    private final IItemHandler downItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> false,  // No input slots
            i -> i == 9  // Allow output from slot 9
    );

    private final IItemHandler northItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> { // Input condition
                if (i >= 0 && i <= 8 && itemHandler.isItemValid(i, stack)) {
                    ItemStack slotStack = itemHandler.getStackInSlot(i);
                    return !slotStack.isEmpty() && ItemStack.isSameItem(slotStack, stack) && slotStack.getCount() < 2;  // Allow insertion if the item matches and count is less than 2
                }
                return false;
            },
            i -> false // No output slots
    );
    private final IItemHandler southItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> { // Input condition
                if (i >= 0 && i <= 8 && itemHandler.isItemValid(i, stack)) {
                    ItemStack slotStack = itemHandler.getStackInSlot(i);
                    return !slotStack.isEmpty() && ItemStack.isSameItem(slotStack, stack) && slotStack.getCount() < 2;  // Allow insertion if the item matches and count is less than 2
                }
                return false;
            },
            i -> false // No output slots
    );
    private final IItemHandler eastItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> { // Input condition
                if (i >= 0 && i <= 8 && itemHandler.isItemValid(i, stack)) {
                    ItemStack slotStack = itemHandler.getStackInSlot(i);
                    return !slotStack.isEmpty() && ItemStack.isSameItem(slotStack, stack) && slotStack.getCount() < 2;  // Allow insertion if the item matches and count is less than 2
                }
                return false;
            },
            i -> false // No output slots
    );
    private final IItemHandler westItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> { // Input condition
                if (i >= 0 && i <= 8 && itemHandler.isItemValid(i, stack)) {
                    ItemStack slotStack = itemHandler.getStackInSlot(i);
                    return !slotStack.isEmpty() && ItemStack.isSameItem(slotStack, stack) && slotStack.getCount() < 2;  // Allow insertion if the item matches and count is less than 2
                }
                return false;
            },
            i -> false // No output slots
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
        compoundTag.putInt("crafter.progress", progress);
        compoundTag.putInt("crafter.maxProgress", maxProgress);
        compoundTag.putInt("crafter.recipeChecker", recipeChecker);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        this.itemHandler.deserializeNBT(provider, compoundTag.getCompound("inventory"));
        progress = compoundTag.getInt("crafter.progress");
        maxProgress = compoundTag.getInt("crafter.maxProgress");
        recipeChecker = compoundTag.getInt("crafter.recipeChecker");
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


        recipeChecker++;

        Level pLevel = this.level;
        BlockPos pPos  = this.worldPosition;
        assert pLevel != null;
        BlockState pState = pLevel.getBlockState(pPos);
        CrafterBlockEntity pBlockEntity = this;

        if (recipeChecker >= 20) {
            recipeChecker = 0;
            updateRecipe();
        }
    //    maxProgress = this.getBlockState().getValue(CrafterBlock.TIMER);

        assert level != null;
        if (level.isClientSide()) return;

        if (pState.getValue(POWERED)) {
            if (!pBlockEntity.craftingItem.isEmpty()) {
                if (pBlockEntity.canCraft()) {
                    if (pBlockEntity.hasMaterial()) {
                        pBlockEntity.progress++;
                        if (pBlockEntity.progress >= this.maxProgress) {
                            resetProgress();
                            pBlockEntity.craft();
                            setChanged(pLevel, pPos, pState);
                        }
                    }
                }
            }
        } else {
            resetProgress();
            setChanged(pLevel, pPos, pState);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public boolean canCraft() {
        ItemStack stack = itemHandler.getStackInSlot(9);
        int count = stack.getCount();
        boolean same = stack.getItem() == craftingItem.getItem();
        boolean fits = count + craftingItem.getCount() <= craftingItem.getMaxStackSize();
        return stack.isEmpty() || (same && fits);
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


        assert level != null;
        Optional<RecipeHolder<CraftingRecipe>> recipe = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, container, level);
        if (recipe.isPresent()) {
            CraftingRecipe r = recipe.get().value();
            craftingItem = r.getResultItem(RegistryAccess.EMPTY).copy();
            craftingIngredients = r.getIngredients();
        } else {
            craftingItem = ItemStack.EMPTY.copy();
        }
    }

    public void craft() {
        extractIngredients();
        itemHandler.insertItem(9, craftingItem.copy(), false);
    }

    public void extractIngredients() {
        HashMap<Item, Integer> itemCount = countIngredients(craftingIngredients);

        // Iterate over each item in the recipe
        for (Map.Entry<Item, Integer> entry : itemCount.entrySet()) {
            Item item = entry.getKey();
            int needs = entry.getValue();

            // Iterate over the slots containing the item
            for (int i = 0; i < 9 && needs > 0; i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() == item) {
                    // Extract one item from this slot
                    ItemStack extractedStack = itemHandler.extractItem(i, 1, false);
                    needs--;

                    // Handle remaining item after extraction
                    if (!extractedStack.isEmpty()) {
                        // If there's a remaining item after crafting, attempt to insert it back into the item handler
                        if (!extractedStack.getCraftingRemainingItem().isEmpty()) {
                            if (!tryInserting(extractedStack.getCraftingRemainingItem())) {
                                // If insertion fails, drop the remaining item into the world
                                Containers.dropItemStack(level, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), extractedStack.getCraftingRemainingItem());
                            }
                        }
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
            for (ItemStack stack : ingredient.getItems()) {
                map.put(stack.getItem(), map.getOrDefault(stack.getItem(), 0) + stack.getCount());
            }
        }
        return map;
    }

    private boolean hasIngredientsForRecipe() {
        for (int i = 0; i < 9; i++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(i);
            if (!stackInSlot.isEmpty() && stackInSlot.getCount() < 2) {
                return false; // If any slot has an item with less than 2 count, return false immediately
            }
        }
        return true;
    }





}