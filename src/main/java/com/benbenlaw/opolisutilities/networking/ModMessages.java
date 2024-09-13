package com.benbenlaw.opolisutilities.networking;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.networking.packets.*;
import com.benbenlaw.opolisutilities.networking.payload.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public class ModMessages {

    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(OpolisUtilities.MOD_ID);

        //To Server From Client
        registrar.playToServer(OnOffButtonPayload.TYPE, OnOffButtonPayload.STREAM_CODEC, PacketOnOffButton.get()::handle);
        registrar.playToServer(IncreaseTickButtonPayload.TYPE, IncreaseTickButtonPayload.STREAM_CODEC, PacketIncreaseTickButton.get()::handle);
        registrar.playToServer(DecreaseTickButtonPayload.TYPE, DecreaseTickButtonPayload.STREAM_CODEC, PacketDecreaseTickButton.get()::handle);
        registrar.playToServer(SaveRecipePayload.TYPE, SaveRecipePayload.STREAM_CODEC, PacketSaveRecipeButton.get()::handle);
        registrar.playToServer(PortableGUISelectorPayload.TYPE, PortableGUISelectorPayload.STREAM_CODEC, PacketPortableGUISelector.get()::handle);
        registrar.playToServer(ClearTankPayload.TYPE, ClearTankPayload.STREAM_CODEC, ClearTankPacket.get()::handle);


        //To Client From Server


    }

}



