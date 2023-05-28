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
import net.minecraft.core.Registry;
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


public class ResourceGenerator2BlockEntity extends BlockEntity {
    // Add a counter variable
    private int counter = 0;
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
        entity.counter++;
        int tickRate = 220;

        //Check For Speed Block and apply correct tickrate

        if (!level.getBlockState(blockPos.above(2)).is(Blocks.AIR)) {

            for (RG2SpeedBlocksRecipe match : level.getRecipeManager().getRecipesFor(RG2SpeedBlocksRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                String blockName = match.getBlock();
                Block speedBlock = Registry.BLOCK.get(new ResourceLocation(blockName));
                TagKey<Block> speedBlockTag = BlockTags.create(new ResourceLocation(blockName));

                if (level.getBlockState(blockPos.above(2)).getBlockHolder().containsTag(speedBlockTag) ||
                        level.getBlockState(blockPos.above(2)).is(speedBlock)) {
                    tickRate = match.getTickRate();
                }
            }
        }

        //Check valid structure

        if (entity.counter % 5 == 0) {

            for (RG2BlocksRecipe genBlocks : level.getRecipeManager().getRecipesFor(RG2BlocksRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                String genBlock = genBlocks.getBlock();
                Block genBlockBlock = Registry.BLOCK.get(new ResourceLocation(genBlock));
                TagKey<Block> genBlockTag = BlockTags.create(new ResourceLocation(genBlock));


                if (level.getBlockState(blockPos.above(1)).getBlockHolder().containsTag(genBlockTag) && !(genBlockTag == ModTags.Blocks.EMPTY)) {
                    isValidStructure = level.getBlockState(blockPos.above(1)).getBlockHolder().containsTag(genBlockTag);
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(ResourceGenerator2Block.LIT, true));
                    break;
                }

                if (level.getBlockState(blockPos.above(1)).is(genBlockBlock)) {
                    isValidStructure = level.getBlockState(blockPos.above(1)).is(genBlockBlock) && !(genBlockBlock == Blocks.AIR);
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(ResourceGenerator2Block.LIT, true));
                    break;
                } else isValidStructure = false;
            }
        }

        //Update Blockstate

        if(level.getBlockState(blockPos).is(ModBlocks.RESOURCE_GENERATOR_2.get())) {

            if (!isValidStructure) {
                level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(ResourceGenerator2Block.LIT, false));
            }
        }

        if (entity.counter % tickRate == 0 && isValidStructure) {

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
}