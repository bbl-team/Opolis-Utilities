package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CrafterScreen extends AbstractContainerScreen<CrafterMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/crafter_gui.png");

    private static final ResourceLocation SET_RECIPE_BUTTON =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/crafter_set_recipe_button.png");

    public CrafterScreen(CrafterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {


        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 89, y + 33, 176, 30, menu.getScaledProgress(), 16);

        }

    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderBackground(guiGraphics);
        this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, 0, 0, 19, SET_RECIPE_BUTTON, (p_289630_) -> {

            this.menu.blockEntity.updateRecipe();

            Item item = this.menu.blockEntity.getItemStackHandler().getStackInSlot(8).getItem();

            this.menu.blockEntity.getItemStackHandler().setStackInSlot(8, Items.ACACIA_LEAVES.getDefaultInstance());

            System.out.println("Button Pressed top update recipes" + item);
            p_289630_.setPosition(this.leftPos + 5, this.height / 2 - 49);
        }));

        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);


    }
}

