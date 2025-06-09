package com.benbenlaw.opolisutilities.commands;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
@Deprecated(forRemoval = true, since = "4.11.10")
@EventBusSubscriber(modid = OpolisUtilities.MOD_ID)
public class CommandEventBusEvents {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        DiscordCommand.register(event.getDispatcher());
        ModpackVersionCommand.register(event.getDispatcher());
    }
}
