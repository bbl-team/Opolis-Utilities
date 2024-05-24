package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.ResourceGeneratorBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.benbenlaw.opolisutilities.recipe.ResourceGeneratorRecipe;
import com.benbenlaw.opolisutilities.recipe.SpeedUpgradesRecipe;
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


public class ResourceGeneratorBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public final ContainerData data;
    public int progress = 0;
    public int maxProgress = 220;
    public String resource = "";
    public boolean isValidStructure = false;

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int UPGRADE_SLOT = 2;

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

    public ResourceGeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.RESOURCE_GENERATOR_BLOCK_ENTITY.get(), blockPos, blockState);

        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> ResourceGeneratorBlockEntity.this.progress;
                    case 1 -> ResourceGeneratorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ResourceGeneratorBlockEntity.this.progress = value;
                    case 1 -> ResourceGeneratorBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.opolisutilities.resource_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new ResourceGeneratorMenu(container, inventory, this.getBlockPos());
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
        compoundTag.putString("resource_generator.resource", resource);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        this.itemHandler.deserializeNBT(provider, compoundTag.getCompound("inventory"));
        progress = compoundTag.getInt("resource_generator.progress");
        maxProgress = compoundTag.getInt("resource_generator.maxProgress");
        resource = compoundTag.getString("resource_generator.resource");
        super.loadAdditional(compoundTag, provider);
    }

    public void drops() {
        if (level != null) {
            SimpleContainer container = new SimpleContainer(1);
            container.setItem(0, new ItemStack(Objects.requireNonNull(level.getBlockState(worldPosition).getBlock()).asItem()));
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), container.getItem(0));
        }
    }

    public void tick() {

        Block genBlockBlock = null;

        // Increment the counter
        Level pLevel = this.level;
        BlockPos blockPos = this.worldPosition;
        assert pLevel != null;
        ResourceGeneratorBlockEntity entity = this;
        entity.progress++;

        if (!level.isClientSide()) {

            //Set Tickrate

            boolean useInventoySpeedBlocks = true;

            for (RecipeHolder<SpeedUpgradesRecipe> match : level.getRecipeManager().getRecipesFor(SpeedUpgradesRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                NonNullList<Ingredient> input = match.value().getIngredients();
                for (Ingredient ingredient : input) {
                    for (ItemStack itemStack : ingredient.getItems()) {
                        Block speedBlock = Block.byItem(itemStack.getItem());
                        if (level.getBlockState(blockPos.above(2)).is(speedBlock)) {
                            maxProgress = match.value().tickRate();
                            useInventoySpeedBlocks = false;
                            break;
                        }
                        if (this.itemHandler.getStackInSlot(UPGRADE_SLOT).is(itemStack.getItem())) {
                            maxProgress = match.value().tickRate();
                            break;
                        }
                    }
                }
            }

            //Reset if upgrade is removed
            if (itemHandler.getStackInSlot(UPGRADE_SLOT).isEmpty() && useInventoySpeedBlocks) {
                maxProgress = 220;
            }

            //Check valid block

            if (entity.progress % 5 == 0) {

                for (RecipeHolder<ResourceGeneratorRecipe> genBlocks : level.getRecipeManager().getRecipesFor(ResourceGeneratorRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                    NonNullList<Ingredient> input = genBlocks.value().getIngredients();
                    for (Ingredient ingredient : input) {
                        for (ItemStack itemStack : ingredient.getItems()) {
                            genBlockBlock = Block.byItem(itemStack.getItem());
                            if (level.getBlockState(blockPos.above(1)).is(genBlockBlock)) {
                                resource = itemStack.getItem().toString();
                                isValidStructure = true;
                                level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(ResourceGeneratorBlock.POWERED, true));
                                break;
                            }
                            if (this.itemHandler.getStackInSlot(INPUT_SLOT).is(itemStack.getItem())) {
                                resource = this.itemHandler.getStackInSlot(INPUT_SLOT).getItem().toString();
                                isValidStructure = true;
                                level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(ResourceGeneratorBlock.POWERED, true));
                                break;
                            }
                            else {

                                isValidStructure = false;
                                resource = "";
                            }
                        }
                    }
                }
            }

            //Update Blockstate

            if (level.getBlockState(blockPos).is(ModBlocks.RESOURCE_GENERATOR.get())) {
                if (!isValidStructure) {
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(ResourceGeneratorBlock.POWERED, false));
                }
            }

            if (progress >= maxProgress && isValidStructure) {
                progress = 0;

                if (level.getBlockState(blockPos).is(ModBlocks.RESOURCE_GENERATOR.get())) {
                    ItemStack itemStack = BuiltInRegistries.ITEM.get(new ResourceLocation(resource)).getDefaultInstance();
                    this.itemHandler.insertItem(OUTPUT_SLOT, new ItemStack(itemStack.getItem()), false);
                    setChanged();
                }
            }
        }
    }
}


                /*

                if (!validBlock) {
                    for (RecipeHolder<RG2BlocksRecipe> genBlocks : level.getRecipeManager().getRecipesFor(RG2BlocksRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                        NonNullList<Ingredient> input = genBlocks.value().getIngredients();
                        for (Ingredient ingredient : input) {
                            for (ItemStack itemStack : ingredient.getItems()) {
                                if (this.itemHandler.getStackInSlot(INPUT_SLOT).is(itemStack.getItem())) {
                                    resource = itemStack.toString();
                                    isValidStructure = true;
                                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(ResourceGeneratorBlock.POWERED, true));

                                    break;
                                }
                            }
                        }

                    }
                }
                 */

                /*



                    if (level.getBlockState(blockPos.above(1)).is(genBlockBlock)) {
                        isValidStructure = level.getBlockState(blockPos.above(1)).is(genBlockBlock) && !(genBlockBlock == Blocks.AIR);
                        level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(ResourceGeneratorBlock.POWERED, true));
                        resource = level.getBlockState(blockPos.above(1)).getBlock().toString();

                        break;
                    } else {
                        isValidStructure = false;
                        resource = "";
                    }



            }

            }
        }
    }


    public int getTickrate() {
        return maxProgress;
    }
    public int getProgress() {
        return progress;
    }

    public String getResource() {
        return resource;
    }
}

                 */
