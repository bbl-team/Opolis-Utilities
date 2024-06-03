package com.benbenlaw.opolisutilities.screen;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.custom.BlockBreakerBlock;
import com.benbenlaw.opolisutilities.block.custom.ItemRepairerBlock;
import com.benbenlaw.opolisutilities.block.entity.custom.ItemRepairerBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.ResourceGeneratorBlockEntity;
import com.benbenlaw.opolisutilities.networking.payload.OnOffButtonPayload;
import com.benbenlaw.opolisutilities.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.benbenlaw.opolisutilities.block.entity.custom.ItemRepairerBlockEntity.UPGRADE_SLOT;
import static com.benbenlaw.opolisutilities.block.entity.custom.ResourceGeneratorBlockEntity.INPUT_SLOT;

public class ItemRepairerScreen extends AbstractContainerScreen<ItemRepairerMenu>{

    Level level;
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(OpolisUtilities.MOD_ID, "textures/gui/item_repairer_gui.png");

    public ItemRepairerScreen(ItemRepairerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.level = pMenu.level;
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
            guiGraphics.blit(TEXTURE, x + 84, y + 35, 176, 0, 8, menu.getScaledProgress());
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);

        if (this.menu.blockEntity != null) {

            if (!this.menu.blockEntity.getBlockState().getValue(ItemRepairerBlock.POWERED)) {
                this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, ModButtons.OFF_BUTTONS, (pressed) ->
                        PacketDistributor.sendToServer(new OnOffButtonPayload(this.menu.blockEntity.getBlockPos()))));
            } else {
                this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, ModButtons.ON_BUTTONS, (pressed) ->
                        PacketDistributor.sendToServer(new OnOffButtonPayload(this.menu.blockEntity.getBlockPos()))));
            }
        }

        renderInWorldBlocksAsItems(guiGraphics, mouseX, mouseY, x, y);
    }

    private void renderInWorldBlocksAsItems (GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        Block speedBlock = level.getBlockState(this.menu.blockPos.above(1)).getBlock();
        if (menu.getSlot(ItemRepairerBlockEntity.UPGRADE_SLOT).getItem().isEmpty() && speedBlock != Blocks.AIR) {
            if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 116, 16, 16, 16)) {
                if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
                    guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.block_in_world"), mouseX, mouseY);
                }
            }
        }
    }
}
