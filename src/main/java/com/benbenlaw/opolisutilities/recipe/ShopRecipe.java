package com.benbenlaw.opolisutilities.recipe;

public class ShopRecipe{} /* extends SingleItemRecipe {

    public ShopRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult, int itemInCount) {
        super(Type.INSTANCE, ModRecipes.SHOP_SERIALIZER.get(), pId, pGroup, pIngredient, pResult);
        this.itemInCount = itemInCount;
    }

    public int itemInCount;


    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return this.ingredient.test(pContainer.getItem(0));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ShopRecipe> {
        private Type() { }
        public static final Type INSTANCE = new ShopRecipe.Type();
        public static final String ID = "shop";
    }

    public static class Serializer implements RecipeSerializer<ShopRecipe> {
        public static final ShopRecipe.Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(OpolisUtilities.MOD_ID,"shop");

        @Override
        public @NotNull ShopRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            String group = GsonHelper.getAsString(pJson, "group", "");

            Ingredient ingredient;
            int itemInCount = GsonHelper.getAsInt(pJson, "itemInCount", 1);
            if (GsonHelper.isArrayNode(pJson, "ingredient")) {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(pJson, "ingredient"));
            } else {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(pJson, "ingredient"));
            }

            String result = GsonHelper.getAsString(pJson, "result");
            int count = GsonHelper.getAsInt(pJson, "itemOutCount", 1);
            ItemStack itemstack = new ItemStack(Items.registerItem(new ResourceLocation(result), null));

            return new ShopRecipe(pRecipeId, group, ingredient, itemstack, itemInCount);
        }

        @Override
        public ShopRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {

            String group = pBuffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack result = pBuffer.readItem();
            int itemInCount = pBuffer.readInt();

            return new ShopRecipe(pRecipeId, group, ingredient, result, itemInCount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ShopRecipe pRecipe) {

            pBuffer.writeUtf(pRecipe.group);
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeInt(pRecipe.itemInCount);
        }
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    @SuppressWarnings("unchecked") // Need this wrapper, because generics
    private static <G> Class<G> castClass(Class<?> cls) {
        return (Class<G>) cls;
    }
}
*/

