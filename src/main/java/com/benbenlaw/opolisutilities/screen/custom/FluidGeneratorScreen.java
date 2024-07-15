package com.benbenlaw.opolisutilities.screen.custom;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.screen.utils.FluidStackWidget;
import com.benbenlaw.opolisutilities.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FluidGeneratorScreen extends AbstractContainerScreen<FluidGeneratorMenu> {

    Level level;

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "textures/gui/fluid_generator_gui.png");

    public FluidGeneratorScreen(FluidGeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.level = pMenu.level;
    }

    @Override
    protected void init() {
        super.init();
        addFluidWidgets();
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

        renderTickRate(guiGraphics, mouseX, mouseY, x, y);
        renderInWorldBlocks(guiGraphics, mouseX, mouseY, x, y);

    }

    private void renderTickRate(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 116, 16, 16, 16)) {
            guiGraphics.drawString(this.font, this.menu.blockEntity.maxProgress + " ticks", this.leftPos + 120,
                    this.topPos + 68, 0x3F3F3F, false);
        }
    }

    private void renderInWorldBlocks(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {

        //Render Speed Upgrade Block In GUI
        if (!this.menu.blockEntity.useInventorySpeedBlocks) {
            Item inWorldBlockAsItem = level.getBlockState(this.menu.blockPos.above(2)).getBlock().asItem();
            guiGraphics.renderFakeItem(new ItemStack(inWorldBlockAsItem), x + 116, y + 16);

            if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 116, 16, 16, 16)) {
                guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.speed_upgrade_in_world"), mouseX, mouseY);
            }
        }

        //Render Resource Block In GUI
        if (this.menu.blockEntity.hasInputInWorld) {
            Item inWorldFluidAsBucket = level.getFluidState(this.menu.blockPos.above(1)).getType().getBucket();
            guiGraphics.renderFakeItem(new ItemStack(inWorldFluidAsBucket), x + 80, y + 16);


            if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 80, 16, 16, 16)) {
                guiGraphics.renderTooltip(this.font, Component.translatable("block.gui.block_in_world"), mouseX, mouseY);
            }
        }
    }


    private void addFluidWidgets() {
        addRenderableOnly(new FluidStackWidget(this, getMenu().blockEntity.FLUID_TANK, leftPos + 14, topPos + 15, 14, 56));
    }



}

