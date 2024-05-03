package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class PacketCrafterOnOffButton {

    private BlockPos pos;

    public PacketCrafterOnOffButton(BlockPos pos) {
        this.pos = pos;
    }

    public PacketCrafterOnOffButton(FriendlyByteBuf buf) {
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

                    if (level.getBlockState(pos).getValue(CrafterBlock.POWERED)) {
                        level.setBlockAndUpdate(pos, ModBlocks.CRAFTER.get().defaultBlockState().setValue(CrafterBlock.POWERED, false).setValue(CrafterBlock.FACING, level.getBlockState(pos).getValue(CrafterBlock.FACING)));
                    } else
                        level.setBlockAndUpdate(pos, ModBlocks.CRAFTER.get().defaultBlockState().setValue(CrafterBlock.POWERED, true).setValue(CrafterBlock.FACING, level.getBlockState(pos).getValue(CrafterBlock.FACING)));


                }
            }
        });
        return true;
    }
}