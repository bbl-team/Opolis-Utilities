package com.benbenlaw.opolisutilities.screen.custom;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.custom.CrafterBlock;
import com.benbenlaw.opolisutilities.block.entity.custom.BlockPlacerBlockEntity;
import com.benbenlaw.opolisutilities.block.entity.custom.CrafterBlockEntity;
import com.benbenlaw.opolisutilities.networking.payload.DecreaseTickButtonPayload;
import com.benbenlaw.opolisutilities.networking.payload.IncreaseTickButtonPayload;
import com.benbenlaw.opolisutilities.networking.payload.OnOffButtonPayload;
import com.benbenlaw.opolisutilities.networking.payload.SaveRecipePayload;
import com.benbenlaw.opolisutilities.screen.utils.ModButtons;
import com.benbenlaw.opolisutilities.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrafterScreen extends AbstractContainerScreen<CrafterMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OpolisUtilities.MOD_ID, "textures/gui/crafter_gui.png");


    public CrafterScreen(CrafterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
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
            guiGraphics.blit(TEXTURE, x + 89, y + 33, 176, 30, menu.getScaledProgress(), 16);
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

    }

    @Nullable
    private void renderTickRate(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 4, 13, 20, 18 * 3)) {
            int ticks = 0;
            if (this.menu.level.getBlockEntity(this.menu.blockPos) instanceof CrafterBlockEntity crafterBlockEntity) {
                ticks = crafterBlockEntity.maxProgress;
            }

            guiGraphics.drawString(this.font, Component.translatable("gui.opolisutilities.shift"), this.leftPos + 95,
                    this.topPos + 45, 0x3F3F3F, false);

            guiGraphics.drawString(this.font, ticks + " ticks", this.leftPos + 90,
                    this.topPos + 60, 0x3F3F3F, false);

        }

        if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, 148, 30, 20, 18)) {
            ItemStack itemStack = ItemStack.EMPTY;
            if (this.menu.level.getBlockEntity(this.menu.blockPos) instanceof CrafterBlockEntity crafterBlockEntity) {
                if (crafterBlockEntity.recipeID != null) {

                    Optional<RecipeHolder<?>> recipe = this.menu.level.getRecipeManager().byKey(ResourceLocation.parse(String.valueOf(crafterBlockEntity.recipeID)));
                    if (recipe.isPresent()) {
                        CraftingRecipe r = (CraftingRecipe) recipe.get().value();
                        itemStack = r.getResultItem(RegistryAccess.EMPTY).copy();

                    }
                }

                String additionalInfo;

                if (itemStack.getCount() == 1) {
                    additionalInfo = itemStack.getDisplayName().getString().replace("[", "").replace("]", "");
                } else {
                    additionalInfo = itemStack.getCount() + "x " + itemStack.getDisplayName().getString().replace("[", "").replace("]", "") ;
                }

                guiGraphics.drawString(this.font, "Current Recipe:", this.leftPos + 87,
                        this.topPos + 60, 0x3F3F3F, false);

                guiGraphics.drawString(this.font, additionalInfo, this.leftPos + 87,
                        this.topPos + 70, 0x3F3F3F, false);
            }
        }
    }

    private void addMenuButtons() {

        //Power Button
        if (!this.menu.blockEntity.getBlockState().getValue(CrafterBlock.POWERED)) {
            this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, ModButtons.OFF_BUTTONS, (pressed) ->
                    PacketDistributor.sendToServer(new OnOffButtonPayload(this.menu.blockEntity.getBlockPos()))));
        } else {
            this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, ModButtons.ON_BUTTONS, (pressed) ->
                    PacketDistributor.sendToServer(new OnOffButtonPayload(this.menu.blockEntity.getBlockPos()))));
        }

        boolean isShiftDown = CrafterScreen.hasShiftDown();

        //Tick Buttons
        this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 32, 20, 18, ModButtons.DECREASE_BUTTONS, (pressed) ->
                PacketDistributor.sendToServer(new DecreaseTickButtonPayload(this.menu.blockEntity.getBlockPos(), isShiftDown))));

        this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 66, 20, 18, ModButtons.INCREASE_BUTTONS, (pressed) ->
                PacketDistributor.sendToServer(new IncreaseTickButtonPayload(this.menu.blockEntity.getBlockPos(), isShiftDown))));

        //Recipe Button

        this.addRenderableWidget(new ImageButton(this.leftPos + 148, this.height / 2 - 49, 20, 18, ModButtons.SAVED_RECIPE_BUTTONS, (pressed) ->
                PacketDistributor.sendToServer(new SaveRecipePayload(this.menu.blockEntity.getBlockPos()))));

    }
}