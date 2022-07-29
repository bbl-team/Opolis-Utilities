package com.benbenlaw.opolisutilities.block.custom;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MinerBlock extends Block {
    public MinerBlock(Properties properties) {
        super(properties);
    }

    public static final VoxelShape SHAPE = Block.box(0,0,0,16,16,16);

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }



}
