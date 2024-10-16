package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.FluidGeneratorBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.handler.InputOutputItemHandler;
import com.benbenlaw.opolisutilities.recipe.FluidGeneratorRecipe;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.benbenlaw.opolisutilities.recipe.SpeedUpgradesRecipe;
import com.benbenlaw.opolisutilities.screen.custom.FluidGeneratorMenu;
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
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class FluidGeneratorBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            sync();

        }
    };

    public final FluidTank FLUID_TANK = new FluidTank(64000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            sync();
        }
    };


    public final ContainerData data;

    public int progress = 0;
    public int maxProgress = 220;
    public int fluidAmount;
    public String resource = "";
    private final IItemHandler outputItemHandler = new InputOutputItemHandler(itemHandler,
            (i, stack) -> false, // No input slots
            i -> i == 1 // Allow output from OUTPUT_SLOT
    );
    public IItemHandler getItemHandlerCapability(Direction side) {
        if (side == null)
            return itemHandler;
        return outputItemHandler;
    }

    private final IFluidHandler fluidHandler = new IFluidHandler() {
        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return FLUID_TANK.getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return FLUID_TANK.getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return FLUID_TANK.isFluidValid(stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return resource.getAmount() - FLUID_TANK.fill(resource, action);
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {

            if (resource.getFluid() == FLUID_TANK.getFluid().getFluid()) {
                return FLUID_TANK.drain(resource.getAmount(), action);
            }

            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            if (FLUID_TANK.getFluidAmount() > 0) {
                return FLUID_TANK.drain(maxDrain, action);
            }
            return FluidStack.EMPTY;
        }
    };

    public IFluidHandler getFluidHandlerCapability(Direction side) {
        return fluidHandler;
    }


    public void setHandler(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            this.itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    public ItemStackHandler getItemStackHandler() {
        return this.itemHandler;
    }

    public FluidGeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.FLUID_GENERATOR_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> FluidGeneratorBlockEntity.this.progress;
                    case 1 -> FluidGeneratorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FluidGeneratorBlockEntity.this.progress = value;
                    case 1 -> FluidGeneratorBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }


        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.opolisutilities.fluid_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new FluidGeneratorMenu(container, inventory, this.getBlockPos(), data);
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

    public boolean onPlayerUse(Player player, InteractionHand hand) {
        return FluidUtil.interactWithFluidHandler(player, hand, FLUID_TANK);
    }


    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

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


    public void setFluid(FluidStack stack) {
        this.FLUID_TANK.setFluid(stack);
    }

    public void getFluid(FluidStack stack) {
        FLUID_TANK.setFluid(stack);
    }

    public FluidStack getFluidStack() {
        return this.FLUID_TANK.getFluid();
    }


    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.put("inventory", this.itemHandler.serializeNBT(provider));
        compoundTag.putInt("progress", progress);
        compoundTag.putInt("maxProgress", maxProgress);
        compoundTag.putString("resource", resource);
        compoundTag.putInt("fluidAmount", fluidAmount);
        compoundTag.put("fluidTank", FLUID_TANK.writeToNBT(provider, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        this.itemHandler.deserializeNBT(provider, compoundTag.getCompound("inventory"));
        progress = compoundTag.getInt("progress");
        maxProgress = compoundTag.getInt("maxProgress");
        resource = compoundTag.getString("resource");
        fluidAmount = compoundTag.getInt("fluidAmount");
        FLUID_TANK.readFromNBT(provider, compoundTag.getCompound("fluidTank"));
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

        if (!level.isClientSide()) {

            sync();

            for (RecipeHolder<SpeedUpgradesRecipe> match : level.getRecipeManager().getRecipesFor(SpeedUpgradesRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {
                NonNullList<Ingredient> input = match.value().getIngredients();
                for (Ingredient ingredient : input) {
                    for (ItemStack itemStack : ingredient.getItems()) {
                        if (this.itemHandler.getStackInSlot(1).is(itemStack.getItem())) {
                            maxProgress = match.value().tickRate();
                            break;
                        }
                    }
                }
            }

            // Reset if upgrade is removed
            if (itemHandler.getStackInSlot(1).isEmpty()) {
                maxProgress = 220;
            }

            for (RecipeHolder<FluidGeneratorRecipe> genBlocks : level.getRecipeManager().getRecipesFor(FluidGeneratorRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {
                Item bucketItem = genBlocks.value().input().getFluid().getBucket();
                if (this.itemHandler.getStackInSlot(0).is(bucketItem)) {
                    resource = genBlocks.value().input().getFluid().getFluidType().toString();
                    fluidAmount = genBlocks.value().input().getAmount();
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(FluidGeneratorBlock.POWERED, true));
                    break;
                } else {
                    resource = "";
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(FluidGeneratorBlock.POWERED, false));                    }
            }

            if (this.itemHandler.getStackInSlot(0).isEmpty()) {
                progress = 0;
            } else if (FLUID_TANK.getFluidAmount() < FLUID_TANK.getCapacity()) {
                FluidStack currentFluidInTank = FLUID_TANK.getFluid();
                Fluid fluidStack = BuiltInRegistries.FLUID.get(ResourceLocation.parse(resource));

                if (currentFluidInTank.isEmpty() || currentFluidInTank.getFluid() == fluidStack) {
                    progress++;
                    if (progress >= maxProgress) {
                        progress = 0;
                        if (level.getBlockState(blockPos).is(ModBlocks.FLUID_GENERATOR.get())) {
                            this.FLUID_TANK.fill(new FluidStack(fluidStack, fluidAmount), IFluidHandler.FluidAction.EXECUTE);
                            setChanged();
                        }
                    }
                }
            }
        }
    }
}

