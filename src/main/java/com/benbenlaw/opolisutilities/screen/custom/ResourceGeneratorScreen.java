package com.benbenlaw.opolisutilities.screen.custom;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.block.custom.ResourceGeneratorBlock;
import com.benbenlaw.opolisutilities.block.entity.custom.ResourceGeneratorBlockEntity;
import com.benbenlaw.opolisutilities.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.benbenlaw.opolisutilities.block.entity.custom.ResourceGeneratorBlockEntity.INPUT_SLOT;
import static com.benbenlaw.opolisutilities.block.entity.custom.ResourceGeneratorBlockEntity.UPGRADE_SLOT;

public class ResourceGeneratorScreen extends AbstractContainerScreen<ResourceGeneratorMenu> {

    Level level;
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "textures/gui/resource_generator_gui.png");

    public ResourceGeneratorScreen(ResourceGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
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
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);

        renderOutputSlotTooltip(guiGraphics, mouseX, mouseY, x, y);
        renderInWorldBlocksAsItems(guiGraphics, mouseX, mouseY, x, y);
        renderTickRate(guiGraphics, mouseX, mouseY, x, y);

    }


    private void renderInWorldBlocksAsItems (GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {

        Block speedBlock = level.getBlockState(this.menu.blockPos.above(2)).getBlock();
        Block genBlock = level.getBlockState(this.menu.blockPos.above(1)).getBlock();

        if (!menu.getSlot(INPUT_SLOT).getItem().isEmpty() && genBlock != Blocks.AIR) {
            guiGraphics.renderFakeItem(genBlock.asItem().getDefaultInstance(), x + 80, y + 16);
            if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 80, 16, 16, 16)) {
                if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
                    guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.block_in_world"), mouseX, mouseY);
                }
            }
        }

        if (genBlock == Blocks.AIR && !menu.getSlot(INPUT_SLOT).getItem().isEmpty()) {
            if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 80, 16, 16, 16)) {
                guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.resource"), mouseX, mouseY);
            }
        }

        if (!menu.getSlot(UPGRADE_SLOT).getItem().isEmpty() && speedBlock != Blocks.AIR) {
            guiGraphics.renderFakeItem(speedBlock.asItem().getDefaultInstance(), x + 116, y + 16);
            if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 116, 16, 16, 16)) {
                if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
                    guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.block_in_world"), mouseX, mouseY);

                }
            }
        }

        if (speedBlock == Blocks.AIR && !menu.getSlot(UPGRADE_SLOT).getItem().isEmpty()) {
            if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 116, 16, 16, 16)) {
                guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.speed_upgrade"), mouseX, mouseY);
            }
        }
    }

    private void renderOutputSlotTooltip (GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 80, 64, 16, 16)) {
            if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
                guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.output"), mouseX, mouseY);
            }
        }
    }

    @Nullable
    private void renderTickRate(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 116, 16, 16, 16)) {
            guiGraphics.drawString(this.font, this.menu.blockEntity.maxProgress + " ticks", this.leftPos + 120,
                    this.topPos + 68, 0x3F3F3F, false);
        }
    }
}
