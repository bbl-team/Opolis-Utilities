package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.commands.DiscordCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OpolisUtilities.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandEventBusEvents {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        DiscordCommand.register(event.getDispatcher());
    }


}
