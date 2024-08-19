package com.benbenlaw.opolisutilities.block.entity.client;

import com.benbenlaw.opolisutilities.block.entity.custom.ClocheBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.Objects;

public class ClocheBlockEntityRenderer implements BlockEntityRenderer<ClocheBlockEntity> {
    public ClocheBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(ClocheBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        ItemStack soilStack = pBlockEntity.getSoilBlock();
        ItemStack seedStack = pBlockEntity.getSeed();

        BlockState soilAsBlock;

        if (soilStack.getItem() instanceof BlockItem blockItem) {
            soilAsBlock = blockItem.getBlock().defaultBlockState();

            pPoseStack.pushPose();  // Save the current state of the PoseStack

            pPoseStack.scale(0.9f, 0.2f, 0.9f);
            pPoseStack.translate(0.05f, 0.1f, 0.05f);

            blockRenderDispatcher.renderSingleBlock(
                    soilAsBlock, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, ModelData.EMPTY, RenderType.cutout());

            pPoseStack.popPose();  // Restore the PoseStack to the saved state
        }

        BlockState seedAsBlock;
        int progress = pBlockEntity.progress;
        int maxProgress = pBlockEntity.maxProgress;

        if (seedStack.getItem() instanceof BlockItem blockItem) {
            seedAsBlock = blockItem.getBlock().defaultBlockState();

            if (seedAsBlock.getBlock() instanceof CropBlock ageCropBlock) {
                int maxAge = ageCropBlock.getMaxAge();
                int age = Math.round((float) progress / maxProgress * maxAge);

                // Determine the correct age property
                Property<?> ageProperty = null;
                for (Property<?> property : ageCropBlock.getStateDefinition().getProperties()) {
                    if (property instanceof IntegerProperty integerProperty) {
                        if (integerProperty.getPossibleValues().contains(age)) {
                            ageProperty = integerProperty;
                            break;
                        }
                    }
                }

                if (ageProperty instanceof IntegerProperty integerProperty) {
                    BlockState seedAsBlockCrop = seedAsBlock.setValue(integerProperty, age);

                    pPoseStack.pushPose();
                    pPoseStack.scale(0.8f, 0.8f, 0.8f);
                    pPoseStack.translate(0.1f, 0.25f, 0.1f);

                    blockRenderDispatcher.renderSingleBlock(
                            seedAsBlockCrop, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, ModelData.EMPTY, RenderType.cutout());

                    pPoseStack.popPose();
                }
            }


            else if (seedAsBlock.getBlock() instanceof StemBlock) {
                int maxAge = 7;
                int age = Math.round((float) progress / maxProgress * maxAge);
                BlockState seedAsBlockCrop = seedAsBlock.setValue(BlockStateProperties.AGE_7, age);

                pPoseStack.pushPose();
                pPoseStack.scale(0.8f, 0.8f, 0.8f);
                pPoseStack.translate(0.1f, 0.25f, 0.1f);

                blockRenderDispatcher.renderSingleBlock(
                        seedAsBlockCrop, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, ModelData.EMPTY, RenderType.cutout());

                pPoseStack.popPose();

            } else {

                pPoseStack.pushPose();

                float growthProgress = progress / (float) maxProgress;
                float minScale = 0.3f;
                float maxScale = 0.6f;
                float scale = minScale + growthProgress * (maxScale - minScale);

                pPoseStack.translate(0.5f, 0.5f, 0.5f);

                float initialLowerOffset = -0.3f;
                float verticalOffset = (scale - minScale) / 2.0f;

                pPoseStack.scale(scale, scale, scale);
                pPoseStack.translate(0.0f, initialLowerOffset + verticalOffset, 0.0f);
                pPoseStack.translate(-0.5f, -0.5f, -0.5f);

                blockRenderDispatcher.renderSingleBlock(
                        seedAsBlock, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, ModelData.EMPTY, RenderType.cutout());

                pPoseStack.popPose();
            }
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}