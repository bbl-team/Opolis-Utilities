package com.benbenlaw.opolisutilities.networking.packets;

import com.benbenlaw.opolisutilities.networking.payload.RequestChunkLoadPayload;
import com.benbenlaw.opolisutilities.networking.payload.SmartCraftingRecipePayload;
import com.benbenlaw.opolisutilities.screen.custom.SmartCraftingMenu;
import com.benbenlaw.opolisutilities.screen.custom.SmartCraftingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Optional;

public record SmartCraftingRecipePacket() {

    public static final SmartCraftingRecipePacket INSTANCE = new SmartCraftingRecipePacket();

    public static SmartCraftingRecipePacket get() {
        return INSTANCE;
    }

    public void handle(final SmartCraftingRecipePayload payload, IPayloadContext context) {
        Minecraft.getInstance().execute(() -> {
            assert Minecraft.getInstance().player != null;
            if (Minecraft.getInstance().player.containerMenu instanceof SmartCraftingMenu menu &&
                    Minecraft.getInstance().screen instanceof SmartCraftingScreen screen) {

                List<RecipeHolder<CraftingRecipe>> resolvedRecipes = payload.recipeIds().stream()
                        .map(id -> {
                            assert Minecraft.getInstance().level != null;
                            return Minecraft.getInstance().level.getRecipeManager().byKey(id);
                        })
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .filter(holder -> holder.value() instanceof CraftingRecipe)
                        .map(holder -> (RecipeHolder<CraftingRecipe>) holder)
                        .toList();

                screen.setClientRecipes(resolvedRecipes);
            }
        });
    }
}
