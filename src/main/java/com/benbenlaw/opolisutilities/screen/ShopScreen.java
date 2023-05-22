package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.recipe.ShopRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class ShopScreen extends AbstractContainerScreen<ShopMenu> {

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

    public ShopScreen(ShopMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        pMenu.registerUpdateListener(this::containerChanged);
        this.titleLabelY = 4;
        this.titleLabelX = 4;
        this.inventoryLabelY = 100000;
        this.inventoryLabelX = 9;
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pX, int pY) {
        this.renderBackground(pPoseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(pPoseStack, i, j, 0, 0, imageWidth, imageHeight);
        int k = (int) (39.0F * this.scrollOffs);

        this.blit(pPoseStack, i + 123, j + 26 + k, 220 + (!this.isScrollBarActive() ? 24 : scrolling ? 12 : 0), 0, 12, 15);
        int l = this.leftPos + RECIPES_X;
        int i1 = this.topPos + RECIPES_Y;
        int j1 = this.startIndex + 12;
        this.renderButtons(pPoseStack, pX, pY, l, i1, j1, false);
        this.renderRecipes(l, i1, j1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (!this.menu.resultContainer.isEmpty()) {
            this.blit(pPoseStack, i + 146, j + 46, 211, 0, 8, 11);
        }
    }

    protected void renderTooltip(PoseStack pPoseStack, int pX, int pY) {
        super.renderTooltip(pPoseStack, pX, pY);
        if (this.displayRecipes) {
            int i = this.leftPos + RECIPES_X;
            int j = this.topPos + RECIPES_Y;
            int k = this.startIndex + 12;
            List<ShopRecipe> list = this.menu.getRecipes();

            for (int l = this.startIndex; l < k && l < this.menu.getNumRecipes(); ++l) {
                int i1 = l - this.startIndex;
                int j1 = i + i1 % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH + 2;
                int k1 = j + i1 / RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_HEIGHT + 2;
                if (pX >= j1 && pX < j1 + RECIPES_IMAGE_SIZE_WIDTH && pY >= k1 && pY < k1 + RECIPES_IMAGE_SIZE_HEIGHT) {
                    this.renderTooltip(pPoseStack, list.get(l).getResultItem(), pX, pY);
                }
            }
        }

        if (ShopScreen.isHovering((double) pX, pY, this.leftPos + 142, this.topPos + 32, 16, 16)) {
            this.renderTooltip(pPoseStack, Component.translatable("Price"), pX, pY);
        }


    }

    public static boolean isHovering(double mouseX, double mouseY, double x, double y, double width, double height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private void renderButtons(PoseStack pPoseStack, int pMouseX, int pMouseY, int pX, int pY, int pLastVisibleElementIndex, boolean overlay) {
        for (int i = this.startIndex; i < pLastVisibleElementIndex && i < this.menu.getNumRecipes(); ++i) {
            int j = i - this.startIndex;

            int k = pX + j % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
            int l = j / RECIPES_COLUMNS;
            int i1 = pY + l * RECIPES_IMAGE_SIZE_HEIGHT + 2;
            int j1 = 0;
            int xOffset = overlay ? 22 : 0;
            if (i == this.menu.getSelectedRecipeIndex()) {
                j1 += 22;
            } else if (pMouseX >= k + 2 && pMouseY >= i1 + 2 && pMouseX < k + 2 + RECIPES_IMAGE_SIZE_WIDTH && pMouseY < i1 + 2 + RECIPES_IMAGE_SIZE_HEIGHT) {
                j1 += 44;
            }
            pPoseStack.pushPose();
            if (overlay)
                pPoseStack.translate(0, 50, 100);

            this.blit(pPoseStack, k, i1 - 1, xOffset, 122 + j1 + 50, 22, 22);

            pPoseStack.popPose();

        }

    }

    private void renderRecipes(int pLeft, int pTop, int pRecipeIndexOffsetMax) {
        List<ShopRecipe> list = this.menu.getRecipes();

        for (int i = this.startIndex; i < pRecipeIndexOffsetMax && i < this.menu.getNumRecipes(); ++i) {
            int j = i - this.startIndex;
            int k = pLeft + j % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH + 3;
            int l = j / RECIPES_COLUMNS;
            int i1 = pTop + l * RECIPES_IMAGE_SIZE_HEIGHT + 4;

            ItemStack result = list.get(i).getResultItem();
            this.minecraft.getItemRenderer().renderAndDecorateItem(result, k, i1);
            this.minecraft.getItemRenderer().renderGuiItemDecorations(this.font, result, k, i1);
        }

        if (this.menu.getSelectedRecipeIndex() != -1 && this.menu.getRecipes().size() >= this.menu.getSelectedRecipeIndex() + 1) {
            ShopRecipe selectedRecipe = this.menu.getRecipes().get(this.menu.getSelectedRecipeIndex());
            ItemStack stack = selectedRecipe.getIngredients().get(0).getItems()[0];
            stack.setCount(selectedRecipe.ingredientCount);

            this.minecraft.getItemRenderer().renderAndDecorateItem(stack, this.leftPos + 142, this.topPos + 32);
            this.minecraft.getItemRenderer().renderGuiItemDecorations(this.font, stack, this.leftPos + 142, this.topPos + 32);
        }


    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        this.scrolling = false;
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        this.scrolling = false;
        if (this.displayRecipes) {
            int i = this.leftPos + RECIPES_X;
            int j = this.topPos + RECIPES_Y;
            int k = this.startIndex + 12;

            for (int l = this.startIndex; l < k; ++l) {
                int i1 = l - this.startIndex;
                double d0 = pMouseX - (double) (i + i1 % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH + 2);
                double d1 = pMouseY - (double) (j + i1 / RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_HEIGHT + 3);
                if (d0 >= 0.0D && d1 >= 0.0D && d0 < RECIPES_IMAGE_SIZE_WIDTH && d1 < RECIPES_IMAGE_SIZE_HEIGHT && this.menu.clickMenuButton(this.minecraft.player, l)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
                    return true;
                }
            }

            i = this.leftPos + 123;
            j = this.topPos + 26;
            if (pMouseX >= (double) i && pMouseX < (double) (i + 12) && pMouseY >= (double) j && pMouseY < (double) (j + 54)) {
                if (this.isScrollBarActive()) {
                    this.scrolling = true;
                    i = this.topPos + 26;
                    j = i + 54;
                    this.scrollOffs = ((float) pMouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
                    this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
                    this.startIndex = Math.max(0, ((int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5D) * RECIPES_COLUMNS));
                } else {
                    this.scrollOffs = 0;
                    this.startIndex = 0;
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

    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (this.isScrollBarActive()) {
            int i = this.getOffscreenRows();
            float f = (float) pDelta / (float) i;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            this.startIndex = Math.max(0, ((int) ((double) (this.scrollOffs * (float) i) + 0.5D) * RECIPES_COLUMNS));
        }

        return true;
    }

    private boolean isScrollBarActive() {
        return this.displayRecipes && this.menu.getNumRecipes() > 12;
    }

    protected int getOffscreenRows() {
        return (this.menu.getNumRecipes() + RECIPES_COLUMNS - 1) / RECIPES_COLUMNS - 3;
    }

    /**
     * Called every time this screen's container is changed (is marked as dirty).
     */
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

    }
}