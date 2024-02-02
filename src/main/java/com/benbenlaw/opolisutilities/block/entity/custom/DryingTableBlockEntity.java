package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.networking.ModMessages;
import com.benbenlaw.opolisutilities.networking.packets.PacketSyncItemStackToClient;
import com.benbenlaw.opolisutilities.recipe.DryingTableRecipe;
import com.benbenlaw.opolisutilities.recipe.SoakingTableRecipe;
import com.benbenlaw.opolisutilities.screen.DryingTableMenu;
import com.benbenlaw.opolisutilities.util.inventory.IInventoryHandlingBlockEntity;
import com.benbenlaw.opolisutilities.util.inventory.WrappedHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.benbenlaw.opolisutilities.block.custom.DryingTableBlock.WATERLOGGED;

public class DryingTableBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            assert level != null;
            if(!level.isClientSide()) {
                ModMessages.sendToClients(new PacketSyncItemStackToClient(this, worldPosition));
            }
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1, (i, s) -> false)),

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

    public ItemStack getRenderStack() {
        ItemStack stack;

        if(!itemHandler.getStackInSlot(0).isEmpty()) {
            stack = itemHandler.getStackInSlot(0);
        } else {
            stack = itemHandler.getStackInSlot(1);
        }

        return stack;
    }

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
    public Component getDisplayName() {
        if (this.getBlockState().getValue(WATERLOGGED)) {
            return Component.literal("Soaking Table");
        }
        else return Component.literal("Drying Table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player) {
        return new DryingTableMenu(containerID, inventory, this, this.data);
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
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
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
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        for (Direction dir : Direction.values()) {
            if(directionWrappedHandlerMap.containsKey(dir)) {
                directionWrappedHandlerMap.get(dir).invalidate();
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("drying_table.progress", progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("drying_table.progress");
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

        Level pLevel = this.level;
        BlockPos pPos  = this.worldPosition;
        assert pLevel != null;
        BlockState pState = pLevel.getBlockState(pPos);
        DryingTableBlockEntity pBlockEntity = this;

        if(hasRecipe(pBlockEntity)) {
            pBlockEntity.progress++;
            setChanged(pLevel, pPos, pState);
            if(pBlockEntity.progress > pBlockEntity.maxProgress) {
                craftItem(pBlockEntity);
            }
        } else{
            pBlockEntity.resetProgress();
            setChanged(pLevel, pPos, pState);
        }
    }

    private boolean hasRecipe(DryingTableBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        assert level != null;
        Optional<DryingTableRecipe> match = level.getRecipeManager()
                .getRecipeFor(DryingTableRecipe.Type.INSTANCE, inventory, level);

        Optional<SoakingTableRecipe> matchSoaking = level.getRecipeManager()
                .getRecipeFor(SoakingTableRecipe.Type.INSTANCE, inventory, level);


        if (!entity.getBlockState().getValue(WATERLOGGED) && match.isPresent()) {

            maxProgress = match.get().getDuration();
            return canInsertItemIntoOutputSlot(inventory, match.get().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()))
                    && hasOutputSpaceMaking(entity, match.get())
                    && hasCorrectCountInInputSlot(entity, match.get())
                    && hasDuration(match.get());
        }


        else if (entity.getBlockState().getValue(WATERLOGGED) && matchSoaking.isPresent()) {

            maxProgress = matchSoaking.get().getDuration();

            return canInsertItemIntoOutputSlot(inventory, matchSoaking.get().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()))
                    && hasOutputSpaceMakingSoaking(entity, matchSoaking.get())
                    && hasCorrectCountInInputSlotSoaking(entity, matchSoaking.get())
                    && hasDurationSoaking(matchSoaking.get());
        }

        return false;

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

            Optional<DryingTableRecipe> match = level.getRecipeManager()
                    .getRecipeFor(DryingTableRecipe.Type.INSTANCE, inventory, level);

            if (match.isPresent() && !entity.getBlockState().getValue(WATERLOGGED)) {

                entity.itemHandler.extractItem(0, match.get().getCount(), false);

                ItemStack resultItem = new ItemStack(match.get().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getItem(),
                        entity.itemHandler.getStackInSlot(1).getCount() + 1);
                CompoundTag resultItemNBT = match.get().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getTag();

                if (resultItemNBT != null) {
                    resultItem.setTag(resultItemNBT);
                }

                entity.itemHandler.setStackInSlot(1, resultItem);

            }

            //SOAKING

            Optional<SoakingTableRecipe> matchSoaking = level.getRecipeManager()
                    .getRecipeFor(SoakingTableRecipe.Type.INSTANCE, inventory, level);

            if (matchSoaking.isPresent() && entity.getBlockState().getValue(WATERLOGGED)) {

                entity.itemHandler.extractItem(0, matchSoaking.get().getCount(), false);

                ItemStack resultItem = new ItemStack(matchSoaking.get().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getItem(),
                        entity.itemHandler.getStackInSlot(1).getCount() + 1);
                CompoundTag resultItemNBT = matchSoaking.get().getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getTag();

                if (resultItemNBT != null) {
                    resultItem.setTag(resultItemNBT);
                }

                entity.itemHandler.setStackInSlot(1, resultItem);

            }

            resetProgress();
        }
    }
    private void resetProgress() {
        this.progress = 0;
    }

    private boolean hasDuration(DryingTableRecipe recipe) {
        return 0 <= recipe.getDuration();
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
        return entity.itemHandler.getStackInSlot(0).getCount() >= recipe.getCount();
    }

    private boolean hasCorrectCountInInputSlotSoaking(DryingTableBlockEntity entity, SoakingTableRecipe recipe) {
        return entity.itemHandler.getStackInSlot(0).getCount() >= recipe.getCount();
    }

    private boolean hasOutputSpaceMaking(DryingTableBlockEntity entity, DryingTableRecipe recipe) {
        assert Minecraft.getInstance().level != null;
        return entity.itemHandler.getStackInSlot(1).getCount() + recipe.getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getCount() - 1 <
                entity.itemHandler.getStackInSlot(1).getMaxStackSize();
    }

    private boolean hasOutputSpaceMakingSoaking(DryingTableBlockEntity entity, SoakingTableRecipe recipe) {
        assert Minecraft.getInstance().level != null;
        return entity.itemHandler.getStackInSlot(1).getCount() + recipe.getResultItem(Objects.requireNonNull(getLevel()).registryAccess()).getCount() - 1 <
                entity.itemHandler.getStackInSlot(1).getMaxStackSize();
    }




}