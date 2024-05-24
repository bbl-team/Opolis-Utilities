package com.benbenlaw.opolisutilities.networking.packets;

public class PacketCapabilitySyncToClient {

    /*
    private final ICapabilitySync capabilitySync;
    private final ResourceLocation ID;
    private final Capability<?> capability;
    private CompoundTag data;
    private Object rawData;

    public <T> PacketCapabilitySyncToClient(ICapabilitySync capabilitySync, Capability<?> capability, ResourceLocation ID, Object data) {
        // To Client
        this.ID = ID;
        this.capability = capability;
        this.capabilitySync = capabilitySync;
        this.rawData = data;
    }

    public PacketCapabilitySyncToClient(FriendlyByteBuf buf) {
        // From Server
        /**
         * ID
         * CAP RL
         * BLOCKPOS/PLAYER

        this.capabilitySync = null;
        this.ID = buf.readResourceLocation();
        this.capability = CapabillitySyncronizer.get(buf.readResourceLocation());
        this.data = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(ID);
        buf.writeResourceLocation(CapabillitySyncronizer.get(capability));
        CompoundTag tag = new CompoundTag();
        CapabillitySyncronizer.getDataHandler(ID).ifPresent(e -> e.server().handle(tag, rawData));
        capabilitySync.toNetwork(tag);
        buf.writeNbt(tag);
    }

    @SuppressWarnings("all")
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            CapabillitySyncronizer.getDataHandler(ID).ifPresent(e -> e.client().handle(data, null));
        });
        return true;
    }

     */
}
