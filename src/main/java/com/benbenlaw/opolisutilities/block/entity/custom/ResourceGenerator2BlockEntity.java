package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.ResourceGenerator2Block;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.benbenlaw.opolisutilities.recipe.RG2BlocksRecipe;
import com.benbenlaw.opolisutilities.recipe.RG2SpeedBlocksRecipe;
import com.benbenlaw.opolisutilities.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class ResourceGenerator2BlockEntity extends BlockEntity {
    private int progress = 0;
    private int maxProgress = 220;
    public String resource;
    private boolean isValidStructure = false;


    public ResourceGenerator2BlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.RESOURCE_GENERATOR_2_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public void tick() {

        // Increment the counter
        Level pLevel = this.level;
        BlockPos blockPos = this.worldPosition;
        assert pLevel != null;
        ResourceGenerator2BlockEntity entity = this;
        entity.progress++;

        if (!level.getBlockState(blockPos.above(2)).is(Blocks.AIR)) {

            for (RG2SpeedBlocksRecipe match : level.getRecipeManager().getRecipesFor(RG2SpeedBlocksRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                String blockName = match.getBlock();
                Block speedBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

                TagKey<Block> speedBlockTag = BlockTags.create(new ResourceLocation(blockName));

                if (level.getBlockState(blockPos.above(2)).getBlockHolder().containsTag(speedBlockTag) ||
                        level.getBlockState(blockPos.above(2)).is(Objects.requireNonNull(speedBlock))) {
                    maxProgress = match.getTickRate();

                }
            }
        }

        //Check valid structure

        if (entity.progress % 5 == 0) {

            for (RG2BlocksRecipe genBlocks : level.getRecipeManager().getRecipesFor(RG2BlocksRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                String genBlock = genBlocks.getBlock();
                Block genBlockBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(genBlock));

                //1.19.2
                //Block genBlockBlock = Registry.BLOCK.get(new ResourceLocation(genBlock));
                TagKey<Block> genBlockTag = BlockTags.create(new ResourceLocation(genBlock));


                if (level.getBlockState(blockPos.above(1)).getBlockHolder().containsTag(genBlockTag) && !(genBlockTag == ModTags.Blocks.EMPTY)) {
                    isValidStructure = level.getBlockState(blockPos.above(1)).getBlockHolder().containsTag(genBlockTag);
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(ResourceGenerator2Block.LIT, true));
                    resource = level.getBlockState(blockPos.above(1)).getBlock().toString();
                    break;
                }

                if (level.getBlockState(blockPos.above(1)).is(genBlockBlock)) {
                    isValidStructure = level.getBlockState(blockPos.above(1)).is(genBlockBlock) && !(genBlockBlock == Blocks.AIR);
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(ResourceGenerator2Block.LIT, true));
                    resource = level.getBlockState(blockPos.above(1)).getBlock().toString();

                    break;
                } else {
                    isValidStructure = false;
                    resource = "";
                }
            }
        }

        //Update Blockstate

        if (level.getBlockState(blockPos).is(ModBlocks.RESOURCE_GENERATOR_2.get())) {

            if (!isValidStructure) {
                level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(ResourceGenerator2Block.LIT, false));
            }
        }

        if (progress >= maxProgress && isValidStructure) {
            progress = 0;

            if (level.getBlockState(blockPos).is(ModBlocks.RESOURCE_GENERATOR_2.get())) {

                if (level.getBlockEntity(blockPos.below()) != null) {

                    BlockEntity ent = level.getBlockEntity(blockPos.below());
                    Block blockAbove = level.getBlockState(blockPos.above()).getBlock();
                    assert ent != null;
                    ent.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN).ifPresent(itemHandler -> {

                                ItemStack stack = new ItemStack(blockAbove.asItem());

                                for (int i = 0; i < itemHandler.getSlots(); i++) {
                                    if (itemHandler.isItemValid(i, stack) && itemHandler.insertItem(i, stack, true).isEmpty()) {
                                        itemHandler.insertItem(i, stack, false);
                                        break;
                                    }
                                }
                            }
                    );
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


    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.putInt("resource_generator_2.progress", progress);
        tag.putInt("resource_generator_2.maxProgress", maxProgress);
        tag.putString("resource", resource);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        progress = nbt.getInt("resource_generator_2.progress");
        maxProgress = nbt.getInt("resource_generator_2.maxProgress");
        resource = nbt.getString("resource");
    }
}
