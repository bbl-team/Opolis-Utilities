package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import com.benbenlaw.opolisutilities.recipe.SoakingTableRecipe;
import com.benbenlaw.opolisutilities.screen.DryingTableMenu;
import com.benbenlaw.opolisutilities.screen.ResourceGeneratorMenu;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
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
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.benbenlaw.opolisutilities.block.custom.DryingTableBlock.WATERLOGGED;

public class DryingTableBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };


    public ItemStack getRenderStack() {
        ItemStack stack;
        if (!itemHandler.getStackInSlot(0).isEmpty()) {
            stack = itemHandler.getStackInSlot(0);
        } else {
            stack = itemHandler.getStackInSlot(1);
        }
        return stack;
    }


    public final ContainerData data;
    public int progress = 0;
    public int maxProgress = 80;
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;


    private final IItemHandler dryingTableItemHandlerSide = new InputOutputItemHandler(itemHandler,
            (i, stack) -> i == 0, // Only allow insertion in slot 0
            i -> i == 1 // Only allow extraction from slot 1
    );

    private final IItemHandler noSideItemHandlerSided = new InputOutputItemHandler(itemHandler,
            (i, stack) -> false,
            i -> false
    );

    public IItemHandler getItemHandlerCapability(Direction side) {
        if (side == null)
            return dryingTableItemHandlerSide;

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

    public DryingTableBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.DRYING_TABLE_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> DryingTableBlockEntity.this.progress;
                    case 1 -> DryingTableBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> DryingTableBlockEntity.this.progress = value;
                    case 1 -> DryingTableBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        if (this.getBlockState().getValue(WATERLOGGED)) {
            return Component.translatable("block.opolisutilities.soaking_table");
        } else return Component.translatable("block.opolisutilities.drying_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new DryingTableMenu(container, inventory, this.getBlockPos(), data);
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
        compoundTag.putInt("resource_generator.progress", progress);
        compoundTag.putInt("resource_generator.maxProgress", maxProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        this.itemHandler.deserializeNBT(provider, compoundTag.getCompound("inventory"));
        progress = compoundTag.getInt("resource_generator.progress");
        maxProgress = compoundTag.getInt("resource_generator.maxProgress");
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
        DryingTableBlockEntity pBlockEntity = this;

        assert level != null;
        if (!level.isClientSide()) {

            SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
            for (int i = 0; i < this.itemHandler.getSlots(); i++) {
                inventory.setItem(i, this.itemHandler.getStackInSlot(i));
            }

            Optional<RecipeHolder<DryingTableRecipe>> match = level.getRecipeManager()
                    .getRecipeFor(DryingTableRecipe.Type.INSTANCE, inventory, level);

            Optional<RecipeHolder<SoakingTableRecipe>> matchSoaking = level.getRecipeManager()
                    .getRecipeFor(SoakingTableRecipe.Type.INSTANCE, inventory, level);

            if (!this.getBlockState().getValue(WATERLOGGED) && match.isPresent()) {

                maxProgress = match.get().value().getDuration();
                progress++;

                if (progress >= maxProgress) {
                    if ( canInsertItemIntoOutputSlot(inventory, match.get().value().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()))
                            && hasOutputSpaceMaking(this, match.get().value())
                            && hasCorrectCountInInputSlot(this, match.get().value())
                            && hasDuration(match.get().value())) {
                        craftItem(this);
                        setChanged();
                    }
                }
            }

            else if (this.getBlockState().getValue(WATERLOGGED) && matchSoaking.isPresent()) {

                maxProgress = matchSoaking.get().value().getDuration();
                progress++;

                if (progress >= maxProgress) {
                    if ( canInsertItemIntoOutputSlot(inventory, matchSoaking.get().value().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()))
                            && hasOutputSpaceMakingSoaking(this, matchSoaking.get().value())
                            && hasCorrectCountInInputSlotSoaking(this, matchSoaking.get().value())
                            && hasDurationSoaking(matchSoaking.get().value())) {
                        craftItem(this);
                        setChanged();
                    }
                }
            }

            else {
                resetProgress();
                setChanged();
            }

        }
    }

    private void craftItem(@NotNull DryingTableBlockEntity entity) {

        Level level = entity.level;

        assert level != null;
        if(!level.isClientSide()) {

            SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
            for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
                inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
            }

            //DRYING

            Optional<RecipeHolder<DryingTableRecipe>> match = level.getRecipeManager()
                    .getRecipeFor(DryingTableRecipe.Type.INSTANCE, inventory, level);

            if (match.isPresent() && !entity.getBlockState().getValue(WATERLOGGED)) {

                entity.itemHandler.extractItem(0, match.get().value().getIngredientStackCount(), false);

                ItemStack resultItem = new ItemStack(match.get().value().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getItem(),
                        entity.itemHandler.getStackInSlot(1).getCount() + match.get().value().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getCount());

                DataComponentMap resultItemWithDataComponents = match.get().value().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getComponents();
                resultItem.applyComponents(resultItemWithDataComponents);

                entity.itemHandler.setStackInSlot(1, resultItem);

            }

            //SOAKING

            Optional<RecipeHolder<SoakingTableRecipe>> matchSoaking = level.getRecipeManager()
                    .getRecipeFor(SoakingTableRecipe.Type.INSTANCE, inventory, level);

            if (matchSoaking.isPresent() && entity.getBlockState().getValue(WATERLOGGED)) {

                entity.itemHandler.extractItem(0, matchSoaking.get().value().getIngredientStackCount(), false);

                ItemStack resultItem = new ItemStack(matchSoaking.get().value().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getItem(),
                        entity.itemHandler.getStackInSlot(1).getCount() + matchSoaking.get().value().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getCount());

                DataComponentMap resultItemWithDataComponents = matchSoaking.get().value().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getComponents();
                resultItem.applyComponents(resultItemWithDataComponents);

                entity.itemHandler.setStackInSlot(1, resultItem);

            }

            resetProgress();
        }
    }
    private void resetProgress() {
        this.progress = 0;
    }

    private boolean hasDuration(DryingTableRecipe recipe) {
        return 0 <= recipe.duration();
    }

    private boolean hasDurationSoaking(SoakingTableRecipe recipe) {
        return 0 <= recipe.getDuration();
    }

    private boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack output) {
        return inventory.getItem(1).getItem() == output.getItem() || inventory.getItem(1).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(1).getMaxStackSize() > inventory.getItem(1).getCount();
    }

    private boolean hasCorrectCountInInputSlot(DryingTableBlockEntity entity, DryingTableRecipe recipe) {
        return entity.itemHandler.getStackInSlot(0).getCount() >= recipe.getIngredientStackCount();
    }

    private boolean hasCorrectCountInInputSlotSoaking(DryingTableBlockEntity entity, SoakingTableRecipe recipe) {
        return entity.itemHandler.getStackInSlot(0).getCount() >= recipe.getIngredientStackCount();
    }

    private boolean hasOutputSpaceMaking(DryingTableBlockEntity entity, DryingTableRecipe recipe) {
        ItemStack outputSlotStack = entity.itemHandler.getStackInSlot(OUTPUT_SLOT);
        ItemStack resultStack = recipe.getResultItem(Objects.requireNonNull(getLevel()).registryAccess());

        if (outputSlotStack.isEmpty()) {
            return resultStack.getCount() <= resultStack.getMaxStackSize();
        } else if (outputSlotStack.getItem() == resultStack.getItem()) {
            return outputSlotStack.getCount() + resultStack.getCount() <= outputSlotStack.getMaxStackSize();
        } else {
            return false;
        }
    }


    private boolean hasOutputSpaceMakingSoaking(DryingTableBlockEntity entity, SoakingTableRecipe recipe) {
        ItemStack outputSlotStack = entity.itemHandler.getStackInSlot(OUTPUT_SLOT);
        ItemStack resultStack = recipe.getResultItem(Objects.requireNonNull(getLevel()).registryAccess());

        if (outputSlotStack.isEmpty()) {
            return resultStack.getCount() <= resultStack.getMaxStackSize();
        } else if (outputSlotStack.getItem() == resultStack.getItem()) {
            return outputSlotStack.getCount() + resultStack.getCount() <= outputSlotStack.getMaxStackSize();
        } else {
            return false;
        }
    }

}