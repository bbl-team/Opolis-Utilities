package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.ResourceGeneratorBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.recipe.*;
import com.benbenlaw.opolisutilities.screen.CatalogueMenu;
import com.benbenlaw.opolisutilities.screen.ResourceGeneratorMenu;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
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
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class CatalogueBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public final ContainerData data;

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    private final IItemHandler outputItemHandler = new InputOutputItemHandler(itemHandler,
            (i, stack) -> false, // No input slots
            i -> i == OUTPUT_SLOT // Allow output from OUTPUT_SLOT
    );

    public IItemHandler getItemHandlerCapability(Direction side) {
        if (side == null)
            return itemHandler;

        return outputItemHandler;
    }

    public void setHandler(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            this.itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    public ItemStackHandler getItemStackHandler() {
        return this.itemHandler;
    }

    public CatalogueBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CATALOGUE_BLOCK_ENTITY.get(), blockPos, blockState);

        this.data = new ContainerData() {

            @Override
            public int get(int pIndex) {
                return itemHandler.getSlots();
            }

            @Override
            public void set(int pIndex, int pValue) {

            }

            public int getCount() {
                return 0;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.opolisutilities.catalogue");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new CatalogueMenu(container, inventory, this.getBlockPos(), data);
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

    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        this.itemHandler.deserializeNBT(provider, compoundTag.getCompound("inventory"));
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
    }
}
        /*



        assert level != null;
        if (!level.isClientSide()) {

            SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
            for (int i = 0; i < this.itemHandler.getSlots(); i++) {
                inventory.setItem(i, this.itemHandler.getStackInSlot(i));
            }

            Optional<RecipeHolder<CatalogueRecipe>> match = level.getRecipeManager()
                    .getRecipeFor(CatalogueRecipe.Type.INSTANCE, inventory, level);

            if (canInsertItemIntoOutputSlot(inventory, match.get().value().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()))
                    && hasCorrectItem(this, match.get().value())
                    && hasOutputSpaceMaking(this, match.get().value())
                    && hasCorrectCountInInputSlot(this, match.get().value())) {

                craftItem(this);
                setChanged();
            }
            else {
                setChanged();
            }

        }
    }


    private void craftItem(@NotNull CatalogueBlockEntity entity) {

        Level level = entity.level;

        assert level != null;
        if(!level.isClientSide()) {

            SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
            for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
                inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
            }

            Optional<RecipeHolder<CatalogueRecipe>> match = level.getRecipeManager()
                    .getRecipeFor(CatalogueRecipe.Type.INSTANCE, inventory, level);

            entity.itemHandler.extractItem(0, match.get().value().getIngredientStackCount(), false);
            ItemStack resultItem = new ItemStack(match.get().value().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getItem(),
                    entity.itemHandler.getStackInSlot(1).getCount() + match.get().value().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getCount());

            entity.itemHandler.setStackInSlot(1, resultItem);
        }
    }



    private boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack output) {
        return inventory.getItem(OUTPUT_SLOT).getItem() == output.getItem() || inventory.getItem(1).isEmpty();
    }

    private boolean hasCorrectCountInInputSlot(CatalogueBlockEntity entity, CatalogueRecipe recipe) {
        return entity.itemHandler.getStackInSlot(INPUT_SLOT).getCount() >= recipe.getIngredientStackCount();
    }

    private boolean hasCorrectItem(CatalogueBlockEntity entity, CatalogueRecipe recipe) {
        return entity.itemHandler.getStackInSlot(INPUT_SLOT).getItem() == recipe.getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getItem();
    }


    private boolean hasOutputSpaceMaking(CatalogueBlockEntity entity, CatalogueRecipe recipe) {
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
*/