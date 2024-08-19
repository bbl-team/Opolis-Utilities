package com.benbenlaw.opolisutilities.screen.custom;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.custom.ClocheBlock;
import com.benbenlaw.opolisutilities.block.custom.ItemRepairerBlock;
import com.benbenlaw.opolisutilities.block.entity.custom.ClocheBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.CrafterBlockEntity;
import com.benbenlaw.opolisutilities.networking.payload.OnOffButtonPayload;
import com.benbenlaw.opolisutilities.screen.utils.ModButtons;
import com.benbenlaw.opolisutilities.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClocheScreen extends AbstractContainerScreen<ClocheMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "textures/gui/cloche_gui.png");

    public ClocheScreen(ClocheMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }


    @Override
    protected void init() {
        super.init();
        addMenuButtons();
    }

    @Override
    protected void containerTick() {
        this.clearWidgets();
        addMenuButtons();
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
            guiGraphics.blit(TEXTURE, x + 67, y + 25, 176, 30, menu.getScaledProgress(), 16);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderSlotTooltips(guiGraphics, mouseX, mouseY, x, y);
        renderTickRate(guiGraphics, mouseX, mouseY, x, y);

    }

    private void renderSlotTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 35, 18, 16, 16)) {
            if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
                guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.seed_slot"), mouseX, mouseY);
            }
        }
        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 35, 36, 16, 16)) {
            if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
                guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.soil_slot"), mouseX, mouseY);
            }
        }
        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 35, 54, 16, 16)) {
            if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
                guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.catalyst_slot"), mouseX, mouseY);
            }
        }
        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 71, 54, 16, 16)) {
            if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
                guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.speed_upgrade"), mouseX, mouseY);
            }
        }
    }

    @Nullable
    private void renderTickRate(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 71, 54, 16, 16)) {
            guiGraphics.drawString(this.font, this.menu.blockEntity.maxProgress + " ticks", this.leftPos + 104,
                    this.topPos + 13, 0x3F3F3F, false);
        }
    }

    private void addMenuButtons() {
        //Power Button
        if (this.menu.blockEntity != null) {

            if (!this.menu.blockEntity.getBlockState().getValue(ClocheBlock.POWERED)) {
                this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, ModButtons.OFF_BUTTONS, (pressed) ->
                        PacketDistributor.sendToServer(new OnOffButtonPayload(this.menu.blockEntity.getBlockPos()))));
            } else {
                this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, ModButtons.ON_BUTTONS, (pressed) ->
                        PacketDistributor.sendToServer(new OnOffButtonPayload(this.menu.blockEntity.getBlockPos()))));
            }

        }
    }
}
