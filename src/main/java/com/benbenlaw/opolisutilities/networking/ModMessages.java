package com.benbenlaw.opolisutilities.networking;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.networking.packets.PacketBlockPlacerOnOffButton;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public class ModMessages {

    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(OpolisUtilities.MOD_ID);

        //To Server From Client

        registrar.playToServer(PacketBlockPlacerOnOffButton.TYPE, PacketBlockPlacerOnOffButton.STREAM_CODEC, PacketBlockPlacerOnOffButton::handle);
            // Handle packet
        };

    }


/*
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(OpolisUtilities.MOD_ID);

        //Client To Server

        //Server To Client
    }

    /*

    private static SimpleChanner INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }



    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(OpolisUtilities.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(PacketSyncItemStackToClient.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncItemStackToClient::new)
                .encoder(PacketSyncItemStackToClient::toBytes)
                .consumerMainThread(PacketSyncItemStackToClient::handle)
                .add();

        net.messageBuilder(PacketCapabilitySyncToClient.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketCapabilitySyncToClient::new)
                .encoder(PacketCapabilitySyncToClient::toBytes)
                .consumerMainThread(PacketCapabilitySyncToClient::handle)
                .add();

        //On Off Buttons
        net.messageBuilder(PacketCrafterOnOffButton.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketCrafterOnOffButton::new)
                .encoder(PacketCrafterOnOffButton::toBytes)
                .consumerMainThread(PacketCrafterOnOffButton::handle)
                .add();

        net.messageBuilder(PacketItemRepairerOnOffButton.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketItemRepairerOnOffButton::new)
                .encoder(PacketItemRepairerOnOffButton::toBytes)
                .consumerMainThread(PacketItemRepairerOnOffButton::handle)
                .add();

        net.messageBuilder(PacketBlockPlacerOnOffButton.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketBlockPlacerOnOffButton::new)
                .encoder(PacketBlockPlacerOnOffButton::toBytes)
                .consumerMainThread(PacketBlockPlacerOnOffButton::handle)
                .add();

        net.messageBuilder(PacketBlockBreakerOnOffButton.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketBlockBreakerOnOffButton::new)
                .encoder(PacketBlockBreakerOnOffButton::toBytes)
                .consumerMainThread(PacketBlockBreakerOnOffButton::handle)
                .add();

    }



    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }


    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

 */



