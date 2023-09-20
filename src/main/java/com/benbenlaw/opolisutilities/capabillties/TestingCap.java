package com.benbenlaw.opolisutilities.capabillties;

import com.benbenlaw.opolisutilities.networking.ModMessages;
import com.benbenlaw.opolisutilities.networking.packets.PacketCapabilitySyncToClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

public class TestingCap implements ITesting, ICapabilitySync<Capabilities.Data> {
    private int value;
    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int test) {
        this.value = test;
        if (!FMLEnvironment.dist.isClient()) return;
        ModMessages.sendToClients(new PacketCapabilitySyncToClient(this, getCap()));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeInt(value);
    }

    @Override
    public LazyOptional<Capabilities.Data> fromNetwork(FriendlyByteBuf buf) {
        return LazyOptional.of(() -> new Capabilities.Data(buf.readInt()));
    }

    @Override
    public void handle(NetworkEvent.Context context, LazyOptional<Capabilities.Data> t) {
        t.resolve().ifPresent(data -> {
            var la = this.value;
            this.value = data.getValue();
        });
    }

    @Override
    public Capability<?> getCap() {
        return Capabilities.TESTING;
    }
}
