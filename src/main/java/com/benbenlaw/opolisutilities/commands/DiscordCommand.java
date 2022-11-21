package com.benbenlaw.opolisutilities.commands;

import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.internal.TextComponentMessageFormatHandler;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.awt.*;

public class DiscordCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("discord").executes((command) -> {
            return execute(command);
        }));
    }

    private static int execute(CommandContext<CommandSourceStack> command){
        if(command.getSource().getEntity() instanceof Player){
            Player player = (Player) command.getSource().getEntity();

            player.sendSystemMessage(Component.literal(ConfigFile.discordURL.get())
                    .setStyle(Style.EMPTY.withUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, ConfigFile.discordURL.get()))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Join The Discord Here!"))))
                    .withStyle(ChatFormatting.BLUE));

        }
        return Command.SINGLE_SUCCESS;
    }
}
