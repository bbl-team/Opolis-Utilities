package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.commands.DiscordCommand;
import com.benbenlaw.opolisutilities.commands.ModpackVersionCommand;
import com.benbenlaw.opolisutilities.item.custom.WalletItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.benbenlaw.opolisutilities.OpolisUtilities.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandEventBusEvents {
    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof WalletItem)
            event.addCapability(WalletItem.WALLET_CAP, new WalletItem.CapabilityProvider(event.getObject()));
    }


    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        DiscordCommand.register(event.getDispatcher());
        ModpackVersionCommand.register(event.getDispatcher());
    }


}
