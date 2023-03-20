package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.entity.custom.ShopBlockEntity;
import com.benbenlaw.opolisutilities.recipe.ShopRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.Objects;

public class ShopScreen extends AbstractContainerScreen<ShopMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/drying_table_gui.png");

    private static final ResourceLocation BUTTON_TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/item/home_stone.png");

    public ShopScreen(ShopMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    private boolean buttonClicked = false;
    private int selectedItemIndex = 0;

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        if(menu.isCrafting()) {
            blit(pPoseStack, x + 102, y + 41, 176, 0, 8, menu.getScaledProgress());
        }

    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(pPoseStack, x + 20, y + 10, 0, 0, 20, 20);

        boolean range = mouseX >= x + 20 && mouseY >= y + 10 && mouseX < x + 40 && mouseY < y + 30;

        if (range && buttonClicked) {


            assert minecraft != null;
            Player player = minecraft.player;
            assert player != null;
            Level level = player.getLevel();
            HitResult hitResult = minecraft.hitResult;
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {


                    this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.GOLD_INGOT), x + 70, y + 60);



            }

        }

        if (range && !buttonClicked) {
            this.renderTooltip(pPoseStack, Component.literal("Not clicked"), mouseX, mouseY + 20);
        }




    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // left mouse button
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;
            boolean range = mouseX >= x + 20 && mouseY >= y + 10 && mouseX < x + 40 && mouseY < y + 30;
            if (range) {
                buttonClicked = !buttonClicked;
                return true; // return true to indicate that the event was handled
            }
        }
        return super.mouseClicked(mouseX, mouseY, button); // call the superclass method to handle other cases
    }


}