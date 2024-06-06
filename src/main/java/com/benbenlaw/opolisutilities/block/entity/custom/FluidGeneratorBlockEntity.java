package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.FluidGeneratorBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.recipe.FluidGeneratorRecipe;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.benbenlaw.opolisutilities.recipe.SpeedUpgradesRecipe;
import com.benbenlaw.opolisutilities.screen.FluidGeneratorMenu;
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
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class FluidGeneratorBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    FluidGeneratorBlockEntity entity = this;
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
    public int validCheck = 0;

    public int progress = 0;
    public int maxProgress = 220;
    public int fluidAmount;
    public String resource = "";
    public boolean isValidStructure = false;

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
            return null;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return null;
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

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.put("inventory", this.itemHandler.serializeNBT(provider));
        compoundTag.putInt("fluid_generator.progress", progress);
        compoundTag.putInt("fluid_generator.maxProgress", maxProgress);
        compoundTag.putInt("fluid_generator.validCheck", validCheck);
        compoundTag.putString("fluid_generator.resource", resource);
        compoundTag.putInt("fluid_generator.fluidAmount", fluidAmount);
        compoundTag.put("fluid_generator.fluidTank", FLUID_TANK.writeToNBT(provider, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        this.itemHandler.deserializeNBT(provider, compoundTag.getCompound("inventory"));
        progress = compoundTag.getInt("fluid_generator.progress");
        maxProgress = compoundTag.getInt("fluid_generator.maxProgress");
        validCheck = compoundTag.getInt("fluid_generator.validCheck");
        resource = compoundTag.getString("fluid_generator.resource");
        fluidAmount = compoundTag.getInt("fluid_generator.fluidAmount");
        FLUID_TANK.readFromNBT(provider, compoundTag.getCompound("fluid_generator.fluidTank"));
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

        Block genBlockBlock;
        Level level = this.level;
        BlockPos blockPos = this.worldPosition;
        assert level != null;
        FluidGeneratorBlockEntity entity = this;
        entity.validCheck++;

        if (!level.isClientSide()) {


            boolean useInventoySpeedBlocks = true;

            for (RecipeHolder<SpeedUpgradesRecipe> match : level.getRecipeManager().getRecipesFor(SpeedUpgradesRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {
                NonNullList<Ingredient> input = match.value().getIngredients();
                for (Ingredient ingredient : input) {
                    for (ItemStack itemStack : ingredient.getItems()) {
                        if (itemStack.getItem() instanceof BlockItem) {
                            Block speedBlock = Block.byItem(itemStack.getItem());
                            if (level.getBlockState(blockPos.above(2)).is(speedBlock)) {
                                maxProgress = match.value().tickRate();
                                useInventoySpeedBlocks = false;
                                break;
                            }
                        }
                        if (this.itemHandler.getStackInSlot(1).is(itemStack.getItem())) {
                            maxProgress = match.value().tickRate();
                            break;
                        }
                    }
                }
            }

            // Reset if upgrade is removed
            if (itemHandler.getStackInSlot(1).isEmpty() && useInventoySpeedBlocks) {
                maxProgress = 220;
            }

            // Check valid block
            if (entity.validCheck % 10 == 0) {
                validCheck = 0;
                isValidStructure = false;
                sync();
                boolean foundBlock = false;

                // Check for valid fluid block above the generator
                for (RecipeHolder<FluidGeneratorRecipe> genBlocks : level.getRecipeManager().getRecipesFor(FluidGeneratorRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {
                    FluidStack inputFluid = genBlocks.value().input();

                    if (level.getFluidState(blockPos.above(1)).is(inputFluid.getFluid())) {
                        resource = inputFluid.getFluidType().toString(); // Correctly assign resource as fluid's registry name
                        fluidAmount = genBlocks.value().input().getAmount();
                        isValidStructure = true;
                        foundBlock = true;
                        level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(FluidGeneratorBlock.POWERED, true));
                        break;
                    }
                }

                // No Block check item slots
                if (!foundBlock) {
                    for (RecipeHolder<FluidGeneratorRecipe> genBlocks : level.getRecipeManager().getRecipesFor(FluidGeneratorRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {
                        Item bucketItem = genBlocks.value().input().getFluid().getBucket();
                        if (this.itemHandler.getStackInSlot(0).is(bucketItem)) {
                            resource = genBlocks.value().input().getFluid().getFluidType().toString();
                            fluidAmount = genBlocks.value().input().getAmount();
                            isValidStructure = true;
                            level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(FluidGeneratorBlock.POWERED, true));
                            break;

                        }
                    }
                }

                // Update Blockstate if no valid structure was found
                if (!isValidStructure) {
                    resource = "";
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(FluidGeneratorBlock.POWERED, false));
                }
            }

            // Check for input and reset progress if no input is found
            boolean hasInputInWorld = false;

            // Check if there is a valid block above the resource generator
            for (RecipeHolder<FluidGeneratorRecipe> genBlocks : level.getRecipeManager().getRecipesFor(FluidGeneratorRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {
                FluidStack inputFluid = genBlocks.value().input();
                Fluid fluid = inputFluid.getFluid();

                if (level.getFluidState(blockPos.above(1)).is(fluid.defaultFluidState().getType())) {
                    hasInputInWorld = true;
                    break;
                }
            }

            // Check if input slot is empty or there is no valid block above
            if (this.itemHandler.getStackInSlot(0).isEmpty() && !hasInputInWorld) {
                progress = 0;
            } else if (isValidStructure) {
                progress++;

                if (progress >= maxProgress) {
                    progress = 0;
                    if (level.getBlockState(blockPos).is(ModBlocks.FLUID_GENERATOR.get())) {
                        Fluid fluidStack = BuiltInRegistries.FLUID.get(new ResourceLocation(resource));
                        this.FLUID_TANK.fill(new FluidStack(fluidStack, fluidAmount), IFluidHandler.FluidAction.EXECUTE);
                        setChanged();
                        sync();
                    }
                }
            }
        }
    }
}

