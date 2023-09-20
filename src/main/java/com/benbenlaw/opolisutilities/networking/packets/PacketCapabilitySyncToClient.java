package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.capabillties.CapabillitySyncronizer;
import com.benbenlaw.opolisutilities.capabillties.ICapabilitySync;
import com.benbenlaw.opolisutilities.capabillties.ICapabilitySyncHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketCapabilitySyncToClient {
    private final ICapabilitySync capabilitySync;
    private final ICapabilitySyncHandler handler;

    private final int id;
    private final Capability<?> capability;
    private final BlockPos pos;
    private final FriendlyByteBuf buf;
    public PacketCapabilitySyncToClient(ICapabilitySync capabilitySync, Capability<?> capability) {
        this(capabilitySync, capability, BlockPos.ZERO, 1);
    }

    public PacketCapabilitySyncToClient(ICapabilitySync capabilitySync, Capability<?> cap, BlockPos pos, int id) {
        this.capabilitySync = capabilitySync;
        this.handler = null;
        this.id = id;
        this.capability = cap;
        this.pos = pos;
        this.buf = null;
    }

    public PacketCapabilitySyncToClient(FriendlyByteBuf buf) {
        /**
         * ID
         * CAP RL
         * BLOCKPOS/PLAYER
         */
        this.capabilitySync = null;
        this.handler = null;

        this.id = buf.readInt();
        this.capability = CapabillitySyncronizer.get(buf.readResourceLocation());
        this.pos = id == 0 ? buf.readBlockPos() : BlockPos.ZERO;
        this.buf = new FriendlyByteBuf(buf.copy());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeResourceLocation(CapabillitySyncronizer.get(capability));
        if (id == 0)
            buf.writeBlockPos(pos);
        capabilitySync.toNetwork(buf);
    }

    @SuppressWarnings("all")
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (id == 0) {
                if (Minecraft.getInstance().level == null)
                    return;
                if (Minecraft.getInstance().level.getBlockEntity(pos) == null)
                    return;

                Minecraft.getInstance().level.getBlockEntity(pos).getCapability(capability).ifPresent(e -> {
                    if (e instanceof ICapabilitySync<?> sync)
                        sync.handle(context, sync.fromNetwork(buf).cast());
                });
            } else if (id == 1) {
                if (Minecraft.getInstance().player == null) return;
                Minecraft.getInstance().player.getCapability(capability).ifPresent(e -> {
                    if (e instanceof ICapabilitySync<?> sync)
                        sync.handle(context, sync.fromNetwork(buf).cast());
                });
            }
        });
        return true;
    }
}
