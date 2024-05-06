package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.custom.BlockPlacerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Supplier;


public class PacketBlockPlacerOnOffButton {

    /*

    private BlockPos pos;

    public PacketBlockPlacerOnOffButton(BlockPos pos) {
        this.pos = pos;
    }

    public PacketBlockPlacerOnOffButton(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = player.serverLevel();
                if (pos != null) {

                    if (level.getBlockState(pos).getValue(BlockPlacerBlock.POWERED)) {
                        level.setBlockAndUpdate(pos, ModBlocks.BLOCK_PLACER.get().defaultBlockState().setValue(BlockPlacerBlock.POWERED, false)
                                .setValue(BlockPlacerBlock.FACING, level.getBlockState(pos).getValue(BlockPlacerBlock.FACING))
                                .setValue(BlockPlacerBlock.TIMER, level.getBlockState(pos).getValue(BlockPlacerBlock.TIMER)));
                    } else
                        level.setBlockAndUpdate(pos, ModBlocks.BLOCK_PLACER.get().defaultBlockState().setValue(BlockPlacerBlock.POWERED, true)
                                .setValue(BlockPlacerBlock.FACING, level.getBlockState(pos).getValue(BlockPlacerBlock.FACING))
                                .setValue(BlockPlacerBlock.TIMER, level.getBlockState(pos).getValue(BlockPlacerBlock.TIMER)));


                }
            }
        });
        return true;
    }

     */
}