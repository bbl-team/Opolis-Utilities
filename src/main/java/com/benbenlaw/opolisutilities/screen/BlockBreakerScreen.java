package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.custom.BlockBreakerBlock;
import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.networking.payload.OnOffButtonPayload;
import com.benbenlaw.opolisutilities.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class BlockBreakerScreen extends AbstractContainerScreen<BlockBreakerMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/block_breaker_gui.png");
    public BlockBreakerScreen(BlockBreakerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
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

        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 84, y + 45, 176, 0, 8, menu.getScaledProgress());
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderBackground(guiGraphics, mouseX, mouseY, delta);

        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);

        renderToolSlotTooltip(guiGraphics, mouseX, mouseY, x, y);
        renderWhitelistTooltip(guiGraphics, mouseX, mouseY, x, y);
        renderBlacklistTooltip(guiGraphics, mouseX, mouseY, x, y);

        if (this.menu.blockEntity != null) {

            if (!this.menu.blockEntity.getBlockState().getValue(BlockBreakerBlock.POWERED)) {
                this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, ModButtons.OFF_BUTTONS, (pressed) ->
                        PacketDistributor.sendToServer(new OnOffButtonPayload(this.menu.blockEntity.getBlockPos()))));
            } else {
                this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, ModButtons.ON_BUTTONS, (pressed) ->
                        PacketDistributor.sendToServer(new OnOffButtonPayload(this.menu.blockEntity.getBlockPos()))));
            }
        }
    }

    private void renderToolSlotTooltip (GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 40, 26, 16, 16)) {
            if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
                guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.tool_slot"), mouseX, mouseY);
            }
        }
    }

    private void renderWhitelistTooltip (GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 80, 26, 16, 16)) {
            if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
                guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.whitelist_slot"), mouseX, mouseY);
            }
        }
    }

    private void renderBlacklistTooltip (GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 120, 26, 16, 16)) {
            if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
                guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.blacklist_slot"), mouseX, mouseY);
            }
        }
    }

}
