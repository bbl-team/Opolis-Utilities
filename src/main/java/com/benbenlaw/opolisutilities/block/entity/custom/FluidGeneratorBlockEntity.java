package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.FluidGeneratorBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.recipe.FluidGeneratorRecipe;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.benbenlaw.opolisutilities.recipe.RG2SpeedBlocksRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;


public class FluidGeneratorBlockEntity extends BlockEntity {
    // Add a counter variable
    private int counter = 0;
    private boolean isValidStructure = false;


    public FluidGeneratorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.FLUID_GENERATOR_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public void tick() {

        // Increment the counter
        Level pLevel = this.level;
        BlockPos blockPos = this.worldPosition;
        assert pLevel != null;
        FluidGeneratorBlockEntity entity = this;
        entity.counter++;
        int tickRate = 220;
        int fluidAmount = 0;

        if (!level.getBlockState(blockPos.above(2)).is(Blocks.AIR)) {

            for (RG2SpeedBlocksRecipe match : level.getRecipeManager().getRecipesFor(RG2SpeedBlocksRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                String blockName = match.getBlock();
                Block speedBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

                TagKey<Block> speedBlockTag = BlockTags.create(new ResourceLocation(blockName));

                if (level.getBlockState(blockPos.above(2)).getBlockHolder().containsTag(speedBlockTag) ||
                        level.getBlockState(blockPos.above(2)).is(Objects.requireNonNull(speedBlock))) {
                    tickRate = match.getTickRate();
                }
            }
        }

        //Check valid structure

        if (entity.counter % 5 == 0) {

            for (FluidGeneratorRecipe fluidBlocks : level.getRecipeManager().getRecipesFor(FluidGeneratorRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidBlocks.getFluid()));

                if (level.getFluidState(blockPos.above(1)).is(fluid)) {
                    isValidStructure = level.getFluidState(blockPos.above(1)).is(fluid);// && !(fluid == Blocks.AIR);
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(FluidGeneratorBlock.LIT, true));
                    fluidAmount = fluidBlocks.getFluidAmount();

                    break;
                } else isValidStructure = false;
            }
        }

        //Update Blockstate

        if (level.getBlockState(blockPos).is(ModBlocks.FLUID_GENERATOR.get())) {
            if (!isValidStructure) {
                level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(FluidGeneratorBlock.LIT, false));
            }
        }

        if (entity.counter % tickRate == 0 && isValidStructure) {

            if (level.getBlockState(blockPos).is(ModBlocks.FLUID_GENERATOR.get())) {

                if (level.getBlockEntity(blockPos.below()) != null) {

                    BlockEntity ent = level.getBlockEntity(blockPos.below());
                    FluidState fluidState = level.getFluidState(blockPos.above());
                    assert ent != null;
                    int finalFluidAmount = fluidAmount;

                        ent.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.DOWN).ifPresent(iFluidHandler -> {
                            FluidStack fluid = new FluidStack(fluidState.getType(), finalFluidAmount);
                            iFluidHandler.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
                        });

                }
            }
        }
    }
}

