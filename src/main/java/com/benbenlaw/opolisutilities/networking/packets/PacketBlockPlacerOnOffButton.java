package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.BlockPlacerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;


public record PacketBlockPlacerOnOffButton(BlockPos blockPos) implements CustomPacketPayload {

    public static final Type<PacketBlockPlacerOnOffButton> TYPE = new Type<>(new ResourceLocation(OpolisUtilities.MOD_ID, "block_placer_on_off_button"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PacketBlockPlacerOnOffButton> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull PacketBlockPlacerOnOffButton decode(RegistryFriendlyByteBuf buf) {
            BlockPos blockPos = buf.readBlockPos();
            return new PacketBlockPlacerOnOffButton(blockPos);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, PacketBlockPlacerOnOffButton packet) {
            buf.writeBlockPos(packet.blockPos);

        }
    };

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {

        Player player = context.player();
        Level level = player.level();

        if (level.getBlockState(blockPos).getBlock() instanceof BlockPlacerBlock) {

            if (level.getBlockState(blockPos).getValue(BlockPlacerBlock.POWERED)) {
                level.setBlockAndUpdate(blockPos, ModBlocks.BLOCK_PLACER.get().defaultBlockState().setValue(BlockPlacerBlock.POWERED, false)
                        .setValue(BlockPlacerBlock.FACING, level.getBlockState(blockPos).getValue(BlockPlacerBlock.FACING))
                        .setValue(BlockPlacerBlock.TIMER, level.getBlockState(blockPos).getValue(BlockPlacerBlock.TIMER)));
            } else
                level.setBlockAndUpdate(blockPos, ModBlocks.BLOCK_PLACER.get().defaultBlockState().setValue(BlockPlacerBlock.POWERED, true)
                        .setValue(BlockPlacerBlock.FACING, level.getBlockState(blockPos).getValue(BlockPlacerBlock.FACING))
                        .setValue(BlockPlacerBlock.TIMER, level.getBlockState(blockPos).getValue(BlockPlacerBlock.TIMER)));

        }
    }
}