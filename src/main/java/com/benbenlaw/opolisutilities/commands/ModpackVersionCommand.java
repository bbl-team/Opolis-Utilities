package com.benbenlaw.opolisutilities.commands;

import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;

public class ModpackVersionCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("modpack_version").executes((command) -> {
            return execute(command);
        }));
    }

    private static int execute(CommandContext<CommandSourceStack> command){
        if(command.getSource().getEntity() instanceof Player){
            Player player = (Player) command.getSource().getEntity();

            player.sendSystemMessage(Component.literal("You are playing " + ConfigFile.modpackName.get() + " version " + ConfigFile.modpackVersion.get())
                    .withStyle(ChatFormatting.BLUE));

        }
        return Command.SINGLE_SUCCESS;
    }
}
