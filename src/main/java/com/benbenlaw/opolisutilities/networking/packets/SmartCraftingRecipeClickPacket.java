package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.networking.payload.SmartCraftingRecipeClickPayload;
import com.benbenlaw.opolisutilities.networking.payload.SmartCraftingRecipePayload;
import com.benbenlaw.opolisutilities.screen.custom.SmartCraftingMenu;
import com.benbenlaw.opolisutilities.screen.custom.SmartCraftingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Optional;

public record SmartCraftingRecipeClickPacket() {

    public static final SmartCraftingRecipeClickPacket INSTANCE = new SmartCraftingRecipeClickPacket();

    public static SmartCraftingRecipeClickPacket get() {
        return INSTANCE;
    }

    public void handle(final SmartCraftingRecipeClickPayload payload, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();

        System.out.println(payload.isShifting());

        if (player.containerMenu instanceof SmartCraftingMenu menu) {
            menu.craftRecipeById(payload.recipeID(), payload.isShifting());
        }
    }
}

