package com.benbenlaw.opolisutilities.block.entity.custom;

import com.benbenlaw.opolisutilities.block.custom.FluidGeneratorBlock;
import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.recipe.FluidGeneratorRecipe;
import com.benbenlaw.opolisutilities.recipe.NoInventoryRecipe;
import com.benbenlaw.opolisutilities.recipe.RG2SpeedBlocksRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class FluidGeneratorBlockEntity extends BlockEntity {
    private int progress = 0;
    private int maxProgress = 220;
    private int fluidAmount;
    public String resource;
    private boolean isValidStructure = false;

    public FluidGeneratorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.FLUID_GENERATOR_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public void tick() {

        /*

        Level pLevel = this.level;
        BlockPos blockPos = this.worldPosition;
        assert pLevel != null;
        FluidGeneratorBlockEntity entity = this;
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

        if (level.getBlockState(blockPos.above(2)).is(Blocks.AIR)) {
            maxProgress = 220;
        }

        if (entity.progress % 5 == 0) {

            for (FluidGeneratorRecipe fluidBlocks : level.getRecipeManager().getRecipesFor(FluidGeneratorRecipe.Type.INSTANCE, NoInventoryRecipe.INSTANCE, level)) {

                Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidBlocks.getFluid()));

                if (level.getFluidState(blockPos.above(1)).is(fluid)) {
                    isValidStructure = level.getFluidState(blockPos.above(1)).is(fluid);// && !(fluid == Blocks.AIR);
                    level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(FluidGeneratorBlock.LIT, true));
                    fluidAmount = fluidBlocks.getFluidAmount();
                    assert fluid != null;
                    resource = fluid.getFluidType().toString();
                    setChanged(level, blockPos, this.getBlockState());
                    break;
                } else {
                    isValidStructure = false;
                    resource = "";
                    setChanged(level, blockPos, this.getBlockState());

                }

            }
        }

        //Update Blockstate

        if (level.getBlockState(blockPos).is(ModBlocks.FLUID_GENERATOR.get())) {
            if (!isValidStructure) {
                level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(FluidGeneratorBlock.LIT, false));
                setChanged(level, blockPos, this.getBlockState());

            }
        }

        if (progress >= maxProgress && isValidStructure) {
            progress = 0;
            setChanged(level, blockPos, this.getBlockState());

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

         */
    }

    public String getResource() {
        return resource;
    }

    public Integer getTickrate() {
        return maxProgress;
    }

    public Integer getFluidAmount() {
        return fluidAmount;
    }

    /*

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.putInt("fluid_generator.progress", progress);
        tag.putInt("fluid_generator.maxProgress", maxProgress);
        tag.putString("resource", resource);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        progress = nbt.getInt("fluid_generator.progress");
        maxProgress = nbt.getInt("fluid_generator.maxProgress");
        resource = nbt.getString("resource");
    }

     */


}

