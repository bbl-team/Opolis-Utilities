package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

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