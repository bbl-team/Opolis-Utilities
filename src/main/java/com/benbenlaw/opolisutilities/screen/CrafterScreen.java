package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.block.entity.custom.CrafterBlockEntity;
import com.benbenlaw.opolisutilities.networking.payload.OnOffButtonPayload;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class CrafterScreen extends AbstractContainerScreen<CrafterMenu> {
    protected CrafterBlockEntity blockEntity;

    public static final WidgetSprites RECIPE_BUTTON_SPRITES = new WidgetSprites(
            new ResourceLocation(OpolisUtilities.MOD_ID, "on"), new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/crafter_off_button"));
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/crafter_gui.png");

    private static final ResourceLocation ON_BUTTON =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/crafter_on_button.png");

    private static final ResourceLocation OFF_BUTTON =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/crafter_off_button.png");

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

        renderBackground(guiGraphics, mouseX, mouseY, delta);
        renderLabels(guiGraphics, mouseX, mouseY);

        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);

        if (this.menu.blockEntity != null) {

            if (!this.menu.blockEntity.getBlockState().getValue(CrafterBlock.POWERED)) {
                this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, ModButtons.OFF_BUTTONS, (pressed) ->
                        PacketDistributor.sendToServer(new OnOffButtonPayload(this.menu.blockEntity.getBlockPos()))));
            } else {
                this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, ModButtons.ON_BUTTONS, (pressed) ->
                        PacketDistributor.sendToServer(new OnOffButtonPayload(this.menu.blockEntity.getBlockPos()))));
            }
        }
    }
}