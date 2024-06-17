package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.custom.SummoningBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.handler.InputOutputItemHandler;
import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.benbenlaw.opolisutilities.recipe.*;
import com.benbenlaw.opolisutilities.screen.custom.SummoningBlockMenu;
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
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SummoningBlockEntity extends BlockEntity implements MenuProvider, IInventoryHandlingBlockEntity {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
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

    public final ContainerData data;
    public int validCheck = 0;
    public int progress = 0;
    public int maxProgress = 220;
    public String mob = "";
    public static final int INPUT_SLOT = 0;
    public static final int CATALYST = 1;
    public static final int UPGRADE_SLOT = 2;

    private final IItemHandler summoningBlockItemHandler = new InputOutputItemHandler(itemHandler,
            (i, stack) -> i == INPUT_SLOT,
            i -> false
    );

    public IItemHandler getItemHandlerCapability(Direction side) {
        return summoningBlockItemHandler;
    }

    public void setHandler(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            this.itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    public ItemStackHandler getItemStackHandler() {
        return this.itemHandler;
    }

    public SummoningBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SUMMONING_BLOCK_ENTITY.get(), blockPos, blockState);

        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> SummoningBlockEntity.this.progress;
                    case 1 -> SummoningBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> SummoningBlockEntity.this.progress = value;
                    case 1 -> SummoningBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.opolisutilities.summoning_block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory, @NotNull Player player) {
        return new SummoningBlockMenu(container, inventory, this.getBlockPos(), data);
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
        compoundTag.putInt("summoning_block.progress", progress);
        compoundTag.putInt("summoning_block.maxProgress", maxProgress);
        compoundTag.putInt("summoning_block.validCheck", validCheck);
        compoundTag.putString("summoning_block.mob", mob);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        this.itemHandler.deserializeNBT(provider, compoundTag.getCompound("inventory"));
        progress = compoundTag.getInt("summoning_block.progress");
        maxProgress = compoundTag.getInt("summoning_block.maxProgress");
        validCheck = compoundTag.getInt("summoning_block.validCheck");
        mob = compoundTag.getString("summoning_block.mob");
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
        assert level != null;
        if (!level.isClientSide()) {
            sync();

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
            if (itemHandler.getStackInSlot(2).isEmpty()) {
                maxProgress = 220;
            }

            if (this.getBlockState().getValue(SummoningBlock.POWERED)) {

                if (ConfigFile.summoningBlockCheckForSameEntityBeforeSpawningNewEntity.get()) {
                    int range = ConfigFile.summoningBlockRangeCheck.get();
                    AABB boundingBox = new AABB(getBlockPos().offset(-range, -range, -range).getCenter(), getBlockPos().offset(2, 2, 2).getCenter());
                    List<Entity> nearbyEntities = level.getEntitiesOfClass(Entity.class, boundingBox, entity -> entity.getType().equals(Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(mob)))));
                    if (!nearbyEntities.isEmpty()) {
                        return; // PAUSE if entity of same type is nearby and in the range and config true
                    }
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

                sync();

                Optional<RecipeHolder<SummoningBlockRecipe>> selectedRecipe = Optional.empty();

                for (RecipeHolder<SummoningBlockRecipe> recipeHolder : level.getRecipeManager().getRecipesFor(SummoningBlockRecipe.Type.INSTANCE, inventory, level)) {
                    SummoningBlockRecipe recipe = recipeHolder.value();
                    if (hasInput(this, recipe) && hasCatalyst(this, recipe)) {
                        selectedRecipe = Optional.of(recipeHolder);
                        break;
                    }
                }

                if (selectedRecipe.isPresent()) {
                    RecipeHolder<SummoningBlockRecipe> match = selectedRecipe.get();
                    progress++;
                    mob = match.value().mob();
                    if (progress >= maxProgress) {
                        progress = 0;
                        EntityType<?> entity = Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(mob)));
                        Entity mobAsEntity = entity.create(level);
                        assert mobAsEntity != null;
                        mobAsEntity.setPos(getBlockPos().getX() + 0.5, getBlockPos().getY() + 1, getBlockPos().getZ() + 0.5);
                        level.addFreshEntity(mobAsEntity);
                        itemHandler.getStackInSlot(INPUT_SLOT).shrink(match.value().input().count());
                        // Clear mob state after summoning
                        mob = "";
                    }
                } else {
                    resetProgress();
                }
            }
        }
    }

    public Entity getEntity() {
        if (mob.isEmpty()) {
            return null;
        } else {
            assert level != null;
            return Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(mob))).create(level);
        }
    }

    public float getScaledProgress() {
        float entitySize = 1f;
        return maxProgress != 0 && progress != 0 ? progress * entitySize / maxProgress : 0;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean hasInput(SummoningBlockEntity entity, SummoningBlockRecipe recipe) {
        if (recipe.input().test(entity.itemHandler.getStackInSlot(INPUT_SLOT)) && recipe.catalyst().test(entity.itemHandler.getStackInSlot(CATALYST))) {
            return recipe.input().count() <= entity.itemHandler.getStackInSlot(INPUT_SLOT).getCount();
        }
        return false;
    }

    private boolean hasCatalyst(SummoningBlockEntity entity, SummoningBlockRecipe recipe) {
        return recipe.catalyst().test(entity.itemHandler.getStackInSlot(CATALYST));
    }
}
