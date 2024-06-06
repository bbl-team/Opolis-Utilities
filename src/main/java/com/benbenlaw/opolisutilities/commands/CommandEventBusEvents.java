package com.benbenlaw.opolisutilities.commands;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = OpolisUtilities.MOD_ID)
public class CommandEventBusEvents {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        DiscordCommand.register(event.getDispatcher());
        ModpackVersionCommand.register(event.getDispatcher());
    }
}
