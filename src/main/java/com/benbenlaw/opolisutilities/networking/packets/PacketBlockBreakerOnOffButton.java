package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.custom.BlockBreakerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;


import java.util.function.Supplier;


public class PacketBlockBreakerOnOffButton {

    /*

    private BlockPos pos;

    public PacketBlockBreakerOnOffButton(BlockPos pos) {
        this.pos = pos;
    }

    public PacketBlockBreakerOnOffButton(FriendlyByteBuf buf) {
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

                    if (level.getBlockState(pos).getValue(BlockBreakerBlock.POWERED)) {
                        level.setBlockAndUpdate(pos, ModBlocks.BLOCK_BREAKER.get().defaultBlockState().setValue(BlockBreakerBlock.POWERED, false)
                                .setValue(BlockBreakerBlock.FACING, level.getBlockState(pos).getValue(BlockBreakerBlock.FACING))
                                .setValue(BlockBreakerBlock.TIMER, level.getBlockState(pos).getValue(BlockBreakerBlock.TIMER)));
                    } else
                        level.setBlockAndUpdate(pos, ModBlocks.BLOCK_BREAKER.get().defaultBlockState().setValue(BlockBreakerBlock.POWERED, true)
                                .setValue(BlockBreakerBlock.FACING, level.getBlockState(pos).getValue(BlockBreakerBlock.FACING))
                                .setValue(BlockBreakerBlock.TIMER, level.getBlockState(pos).getValue(BlockBreakerBlock.TIMER)));


                }
            }
        });
        return true;

    }

     */
}