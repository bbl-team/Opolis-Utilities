package com.benbenlaw.opolisutilities.screen;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.recipe.CatalogueRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class CatalogueScreen extends  AbstractContainerScreen<CatalogueMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/shop_inventory.png");

    private static final int SCROLLER_WIDTH = 12;
    private static final int SCROLLER_HEIGHT = 15;
    private static final int RECIPES_COLUMNS = 4;
    private static final int RECIPES_ROWS = 3;
    private static final int RECIPES_IMAGE_SIZE_WIDTH = 18;
    private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
    private static final int SCROLLER_FULL_HEIGHT = 54;
    private static final int RECIPES_X = 48;
    private static final int RECIPES_Y = 23;
    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;
    private boolean displayRecipes;
    private EditBox searchBar;
    private int selectedRecipeIndex = -1;
    private List<RecipeHolder<CatalogueRecipe>> filteredRecipes;


    public CatalogueScreen(CatalogueMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        pMenu.registerUpdateListener(this::containerChanged);
        this.titleLabelY = 4;
        this.titleLabelX = 4;
        this.inventoryLabelY = 100000;
        this.inventoryLabelX = 9;
    }

    @Override
    protected void init() {
        super.init();
        this.font = this.getMinecraft().font;
        this.searchBar = new EditBox(this.font, this.width / 2 - 21, this.height / 2 - 77, 100, 14, Component.literal("")) {
            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                boolean result = super.mouseClicked(mouseX, mouseY, button);
                if (button == 1) { // Right-click
                    if (isMouseOver(mouseX, mouseY)) {
                        setValue(""); // Clear the search bar
                        return true;
                    }
                }
                this.setFocused(result);
                return result;
            }

        };

        this.searchBar.setMaxLength(50);
        this.addWidget(this.searchBar);
        filteredRecipes = menu.getRecipes();
    }

    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(guiGraphics, pMouseX, pMouseY);
        this.searchBar.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    private void updateFilteredRecipes() {
        String searchQuery = searchBar.getValue().toLowerCase();

        if (searchQuery.isEmpty()) {
            filteredRecipes = menu.getRecipes(); // Show all recipes if the search query is empty
        } else {
            filteredRecipes = menu.getRecipes().stream()
                    .filter(recipe -> {
                        assert Minecraft.getInstance().level != null;
                        return recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess())
                                .getHoverName().getString().toLowerCase().contains(searchQuery);
                    })
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.searchBar.keyPressed(keyCode, scanCode, modifiers) || this.searchBar.canConsumeInput()) {
            String searchQuery = searchBar.getValue().toLowerCase();

            if (searchQuery.isEmpty()) {
                filteredRecipes = menu.getRecipes(); // Show all recipes if the search query is empty
            } else {
                filteredRecipes = menu.getRecipes().stream()
                        .filter(recipe -> {
                            assert Minecraft.getInstance().level != null;
                            return recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess())
                                    .getHoverName().getString().toLowerCase().contains(searchQuery);
                        })
                        .collect(Collectors.toList());
            }
            startIndex = 0;
            selectedRecipeIndex = -1;

            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.onClose();
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char charTyped, int keyCode) {
        if (this.searchBar.charTyped(charTyped, keyCode)) {
            return true;
        }
        return super.charTyped(charTyped, keyCode);
    }

    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pX, int pY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(TEXTURE, i, j, 0, 0, imageWidth, imageHeight);
        int k = (int) (39.0F * this.scrollOffs);

        guiGraphics.blit(TEXTURE, i + 123, j + 26 + k, 220 + (!this.isScrollBarActive() ? 24 : scrolling ? 12 : 0), 0, 12, 15);
        int l = this.leftPos + RECIPES_X;
        int i1 = this.topPos + RECIPES_Y;
        int j1 = this.startIndex + 12;
        this.renderButtons(guiGraphics, pX, pY, l, i1, j1, false);
        this.renderRecipes(guiGraphics, l, i1, j1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (!this.menu.resultContainer.isEmpty()) {
            guiGraphics.blit(TEXTURE, i + 146, j + 46, 211, 0, 8, 11);
        }
    }

    protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int pX, int pY) {
        super.renderTooltip(guiGraphics, pX, pY);

        boolean searchBarFocused = this.searchBar.isFocused();
        boolean shouldRenderTooltips = this.displayRecipes || searchBarFocused;

        if (shouldRenderTooltips) {
            int i = this.leftPos + RECIPES_X;
            int j = this.topPos + RECIPES_Y;
            int k = this.startIndex + 12;
            List<RecipeHolder<CatalogueRecipe>> list = searchBarFocused ? this.filteredRecipes : this.menu.getRecipes();

            for (int l = this.startIndex; l < k && l < list.size(); ++l) {
                int i1 = l - this.startIndex;
                int j1 = i + i1 % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH + 2;
                int k1 = j + i1 / RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_HEIGHT + 2;
                if (pX >= j1 && pX < j1 + RECIPES_IMAGE_SIZE_WIDTH && pY >= k1 && pY < k1 + RECIPES_IMAGE_SIZE_HEIGHT) {
                    CatalogueRecipe recipe = list.get(l).value();
                    assert Minecraft.getInstance().level != null;
                    ItemStack result = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());
                    assert Minecraft.getInstance().level != null;
                    guiGraphics.renderTooltip(this.font, result, pX, pY);

                    if (!searchBarFocused) {
                        this.selectedRecipeIndex = this.menu.getRecipes().indexOf(recipe);
                    }

                    return;
                }
            }
        }

        if (CatalogueScreen.isHovering((double) pX, pY, this.leftPos + 142, this.topPos + 32, 16, 16)) {
            guiGraphics.renderTooltip(this.font, Component.translatable("Price"), pX, pY);
        }
    }

    public static boolean isHovering(double mouseX, double mouseY, double x, double y, double width, double height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private void renderButtons(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int pX, int pY, int pLastVisibleElementIndex, boolean overlay) {
        List<RecipeHolder<CatalogueRecipe>> list = this.menu.getRecipes();

        for (int i = this.startIndex; i < pLastVisibleElementIndex && i < list.size(); ++i) {
            int j = i - this.startIndex;
            int k = pX + j % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
            int l = j / RECIPES_COLUMNS;
            int i1 = pY + l * RECIPES_IMAGE_SIZE_HEIGHT + 2;
            int j1 = 0;
            int xOffset = overlay ? 22 : 0;

            if (i < filteredRecipes.size() || searchBar.getValue().isEmpty()) {
                if (i == this.menu.getSelectedRecipeIndex()) {
                    j1 += 22;
                } else if (pMouseX >= k + 2 && pMouseY >= i1 + 2 && pMouseX < k + 2 + RECIPES_IMAGE_SIZE_WIDTH && pMouseY < i1 + 2 + RECIPES_IMAGE_SIZE_HEIGHT) {
                    j1 += 44;
                }

                guiGraphics.pose();
                if (overlay) {
                    guiGraphics.pose().translate(0, 50, 100);
                }

                guiGraphics.blit(TEXTURE, k, i1 - 1, xOffset, 122 + j1 + 50, 22, 22);
                guiGraphics.pose();
            }
        }
    }

    private void renderRecipes(GuiGraphics guiGraphics, int pLeft, int pTop, int pRecipeIndexOffsetMax) {
        List<RecipeHolder<CatalogueRecipe>> list = this.menu.getRecipes();

        for (int i = this.startIndex; i < pRecipeIndexOffsetMax && i < list.size(); ++i) {
            int j = i - this.startIndex;
            int k = pLeft + j % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH + 3;
            int l = j / RECIPES_COLUMNS;
            int i1 = pTop + l * RECIPES_IMAGE_SIZE_HEIGHT + 4;

            if (i < filteredRecipes.size() || searchBar.getValue().isEmpty()) {
                CatalogueRecipe recipe;
                ItemStack result;

                if (searchBar.getValue().isEmpty()) {
                    recipe = list.get(i).value();
                } else {
                    recipe = filteredRecipes.get(i).value();
                }
                assert Minecraft.getInstance().level != null;
                result = recipe.getResultItem(Minecraft.getInstance().level.registryAccess());

                guiGraphics.renderItem(result, k, i1);
                guiGraphics.renderItemDecorations(this.font, result, k, i1);
            }
        }

        if (this.menu.getSelectedRecipeIndex() != -1 && this.menu.getRecipes().size() >= this.menu.getSelectedRecipeIndex() + 1) {
            CatalogueRecipe recipe = this.menu.getRecipes().get(this.menu.getSelectedRecipeIndex()).value();
            ItemStack stack = recipe.getIngredients().get(0).getItems()[0];
            stack.setCount(recipe.getIngredientStackCount());

            guiGraphics.renderItem(stack, this.leftPos + 142, this.topPos + 32);
            guiGraphics.renderItemDecorations(this.font, stack, this.leftPos + 142, this.topPos + 32);
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        System.out.println("Mouse clicked at: (" + pMouseX + ", " + pMouseY + ")");

        if (this.searchBar.mouseClicked(pMouseX, pMouseY, pButton)) {
            return true;
        }

        this.scrolling = false;
        if (this.displayRecipes) {
            int i = this.leftPos + RECIPES_X;
            int j = this.topPos + RECIPES_Y;
            int k = this.startIndex + 12;

            // Use filteredRecipes list when a search query is active
            List<RecipeHolder<CatalogueRecipe>> recipesToUse = this.searchBar.getValue().isEmpty() ? menu.getRecipes() : this.filteredRecipes;

            for (int l = this.startIndex; l < k && l < recipesToUse.size(); ++l) {
                int i1 = l - this.startIndex;
                double d0 = pMouseX - (double)(i + i1 % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH + 2);
                double d1 = pMouseY - (double)(j + i1 / RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_HEIGHT + 3);

                System.out.println("Checking recipe slot " + l + ": (" + d0 + ", " + d1 + ")");

                if (d0 >= 0.0D && d1 >= 0.0D && d0 < RECIPES_IMAGE_SIZE_WIDTH && d1 < RECIPES_IMAGE_SIZE_HEIGHT) {
                    assert this.minecraft != null;
                    assert this.minecraft.player != null;
                    if (this.menu.clickMenuButton(this.minecraft.player, l)) {
                        // Ensure the correct recipe is selected based on the filtered list
                        if (l >= 0 && l < recipesToUse.size()) {
                            CatalogueRecipe recipe = recipesToUse.get(l).value();
                            int originalIndex = menu.getRecipes().indexOf(recipe);

                            System.out.println("Recipe slot " + l + " clicked. Recipe index: " + originalIndex);

                            if (originalIndex >= 0) {
                                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                                assert Objects.requireNonNull(this.minecraft).gameMode != null;
                                assert this.minecraft.gameMode != null;
                                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, originalIndex);
                                this.selectedRecipeIndex = originalIndex;
                                this.searchBar.setValue("");
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }


    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int i = this.topPos + 26;
            int j = i + 54;
            this.scrollOffs = ((float) pMouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = Math.max(0, ((int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5D) * RECIPES_COLUMNS));
            return true;
        } else {
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }

    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY) {
        if (this.isScrollBarActive()) {
            int i = this.getOffscreenRows();
            float f = (float)pScrollY / (float)i;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5) * 4;
        }

        return true;
    }

    private boolean isScrollBarActive() {
        return this.displayRecipes && this.menu.getNumRecipes() > 12;
    }

    protected int getOffscreenRows() {
        return (this.menu.getNumRecipes() + RECIPES_COLUMNS - 1) / RECIPES_COLUMNS - 3;
    }

    private void containerChanged() {
        this.displayRecipes = this.menu.hasInputItem();
        if (!this.displayRecipes) {
            this.scrollOffs = 0.0F;
            this.startIndex = 0;
        }
        if (!this.isScrollBarActive()) {
            this.scrollOffs = 0;
            this.startIndex = 0;
        }

        updateFilteredRecipes();
    }
}