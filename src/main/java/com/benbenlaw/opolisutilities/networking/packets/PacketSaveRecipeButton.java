package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.BlockBreakerBlock;
import com.benbenlaw.opolisutilities.block.custom.BlockPlacerBlock;
import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.block.entity.custom.CrafterBlockEntity;
import com.benbenlaw.opolisutilities.networking.payload.OnOffButtonPayload;
import com.benbenlaw.opolisutilities.networking.payload.SaveRecipePayload;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public record PacketSaveRecipeButton() {

    public static final PacketSaveRecipeButton INSTANCE = new PacketSaveRecipeButton();

    public static PacketSaveRecipeButton get() {
        return INSTANCE;
    }

    public void handle(final SaveRecipePayload payload, IPayloadContext context) {

        Player player = context.player();
        Level level = player.level();
        BlockPos blockPos = payload.blockPos();
        BlockState blockState = level.getBlockState(blockPos);

        //Crafter Save Recipe Button
        if (blockState.getBlock() instanceof CrafterBlock) {
            CrafterBlockEntity entity = (CrafterBlockEntity) level.getBlockEntity(blockPos);
            assert entity != null;
            entity.updateRecipeButton();
        }
    }
}