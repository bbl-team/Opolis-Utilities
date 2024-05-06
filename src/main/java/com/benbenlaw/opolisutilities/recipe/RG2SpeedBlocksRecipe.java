package com.benbenlaw.opolisutilities.recipe;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RG2SpeedBlocksRecipe {} /* implements Recipe<NoInventoryRecipe> {

    private final ResourceLocation id;
    private final String block;
    private final int tickRate;

    public RG2SpeedBlocksRecipe(ResourceLocation id, String block, int tickRate) {
        this.id = id;
        this.block = block;
        this.tickRate = tickRate;
    }

    public String getBlock() {
        return block;

    }

    public int getTickRate() {
        return tickRate;
    }

    @Override
    public boolean matches(@NotNull NoInventoryRecipe inv, @NotNull Level pLevel) {
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull NoInventoryRecipe inv, RegistryAccess p_267165_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess p_267052_) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RG2SpeedBlocksRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RG2SpeedBlocksRecipe.Type.INSTANCE;
    }
    public static class Type implements RecipeType<RG2SpeedBlocksRecipe> {
        private Type() { }
        public static final RG2SpeedBlocksRecipe.Type INSTANCE = new RG2SpeedBlocksRecipe.Type();
        public static final String ID = "rg2_speed_blocks";
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    public static class Serializer implements RecipeSerializer<RG2SpeedBlocksRecipe> {
        public static final RG2SpeedBlocksRecipe.Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(OpolisUtilities.MOD_ID, "rg2_speed_blocks");

        @Override
        public RG2SpeedBlocksRecipe fromJson(ResourceLocation id, JsonObject json) {
            String block = GsonHelper.getAsString(json, "block");
            int tickRate = GsonHelper.getAsInt(json, "tickRate");


            return new RG2SpeedBlocksRecipe(id, block, tickRate);
        }

        @Override
        public RG2SpeedBlocksRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String block = buf.readUtf();
            int tickRate = buf.readInt();

            return new RG2SpeedBlocksRecipe(id, block, tickRate);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, RG2SpeedBlocksRecipe recipe) {

            buf.writeUtf(recipe.getBlock(), Short.MAX_VALUE);
            buf.writeInt(recipe.getTickRate());

        }
    }
}
*/
