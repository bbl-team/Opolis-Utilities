package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.*;
import com.benbenlaw.opolisutilities.block.entity.custom.*;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Mod.EventBusSubscriber(modid = OpolisUtilities.MOD_ID)

public class HoverInWorldEvents {

    @OnlyIn(Dist.CLIENT)
    public static void renderEnderScramblerInfo(GuiGraphics graphics, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        assert player != null;
        Level level = player.level();

        if (player.getMainHandItem().is(ModItems.OPOLIS_WRENCH.get()) || player.getOffhandItem().is(ModItems.OPOLIS_WRENCH.get())) {

            if (minecraft.screen == null) {

                HitResult hitResult = minecraft.hitResult;
                if (hitResult instanceof BlockHitResult hit) {
                    BlockPos blockPos = hit.getBlockPos();
                    BlockState blockState = minecraft.level.getBlockState(blockPos);
                    Block block = blockState.getBlock();
                    ItemStack lookingAtBlock = block.getCloneItemStack(minecraft.level, blockPos, blockState);
                    Window window = minecraft.getWindow();
                    int x = window.getGuiScaledWidth() / 2 + 3;
                    int y = window.getGuiScaledHeight() / 2 + 3;

                    if (lookingAtBlock.is(ModBlocks.ENDER_SCRAMBLER.get().asItem())) {
                        EnderScramblerBlockEntity entity = (EnderScramblerBlockEntity) level.getBlockEntity(blockPos);
                        if (entity != null) {

                            graphics.renderTooltip(minecraft.font, getEnderScramblerInformation(entity), Optional.empty(), window.getX() + 2, window.getY() + 2);
                            graphics.pose().pushPose();
                            graphics.pose().scale(0.75F, 0.75F, 1F);
                            graphics.pose().popPose();
                        }
                    }

                    if (lookingAtBlock.is(ModBlocks.BLOCK_BREAKER.get().asItem())) {
                        BlockBreakerBlockEntity entity = (BlockBreakerBlockEntity) level.getBlockEntity(blockPos);
                        if (entity != null) {
                            graphics.renderTooltip(minecraft.font, getBlockBreakerInformation(entity), Optional.empty() , window.getX() + 2 , window.getY() + 2);
                            graphics.pose().pushPose();
                            graphics.pose().scale(0.75F, 0.75F, 1F);
                            graphics.pose().popPose();
                        }
                    }

                    if (lookingAtBlock.is(ModBlocks.BLOCK_PLACER.get().asItem())) {
                        BlockPlacerBlockEntity entity = (BlockPlacerBlockEntity) level.getBlockEntity(blockPos);
                        if (entity != null) {
                            graphics.renderTooltip(minecraft.font, getBlockPlacerInformation(entity), Optional.empty() , window.getX() + 2 , window.getY() + 2);
                            graphics.pose().pushPose();
                            graphics.pose().scale(0.75F, 0.75F, 1F);
                            graphics.pose().popPose();
                        }
                    }

                    if (lookingAtBlock.is(ModBlocks.ITEM_REPAIRER.get().asItem())) {

                        ItemRepairerBlockEntity entity = (ItemRepairerBlockEntity) level.getBlockEntity(blockPos);

                        if (entity != null) {

                            graphics.renderTooltip(minecraft.font, getItemRepairerInformation(entity), Optional.empty() , window.getX() + 2 , window.getY() + 2);
                            graphics.pose().pushPose();
                            graphics.pose().scale(0.75F, 0.75F, 1F);
                            graphics.pose().popPose();

                        }
                    }

                    if (lookingAtBlock.is(ModBlocks.DRYING_TABLE.get().asItem())) {
                        DryingTableBlockEntity entity = (DryingTableBlockEntity) level.getBlockEntity(blockPos);

                        if (entity != null) {
                            graphics.renderTooltip(minecraft.font, getDryingTableInformation(entity), Optional.empty(), window.getX() + 2 , window.getY() + 2);
                            graphics.pose().pushPose();
                            graphics.pose().scale(0.75F, 0.75F, 1F);
                            graphics.pose().popPose();

                        }
                    }

                    if (lookingAtBlock.is(ModBlocks.REDSTONE_CLOCK.get().asItem())) {
                        RedstoneClockBlockEntity entity = (RedstoneClockBlockEntity) level.getBlockEntity(blockPos);

                        if (entity != null) {
                            graphics.renderTooltip(minecraft.font, getRedstoneClockInformation(entity), Optional.empty(), window.getX() + 2 , window.getY() + 2);
                            graphics.pose().pushPose();
                            graphics.pose().scale(0.75F, 0.75F, 1F);
                            graphics.pose().popPose();

                        }
                    }

                    if (lookingAtBlock.is(ModBlocks.FLUID_GENERATOR.get().asItem())) {
                        FluidGeneratorBlockEntity entity = (FluidGeneratorBlockEntity) level.getBlockEntity(blockPos);

                        if (entity != null) {
                            graphics.renderTooltip(minecraft.font, getFluidGenInformation(entity), Optional.empty(), window.getX() + 2 , window.getY() + 2);
                            graphics.pose().pushPose();
                            graphics.pose().scale(0.75F, 0.75F, 1F);
                            graphics.pose().popPose();

                        }
                    }

                    if (lookingAtBlock.is(ModBlocks.RESOURCE_GENERATOR_2.get().asItem())) {
                        ResourceGenerator2BlockEntity entity = (ResourceGenerator2BlockEntity) level.getBlockEntity(blockPos);

                        if (entity != null) {
                            graphics.renderTooltip(minecraft.font, getResourceGen2Information(entity), Optional.empty(), window.getX() + 2 , window.getY() + 2);
                            graphics.pose().pushPose();
                            graphics.pose().scale(0.75F, 0.75F, 1F);
                            graphics.pose().popPose();

                        }
                    }
                }
            }
        }
    }

    public static List<Component> getRedstoneClockInformation(RedstoneClockBlockEntity entity) {

        Component informationTitle = Component.literal("Redstone Clock Information").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.BOLD);
        Component empty = Component.literal("");
        Component currentTick = Component.literal("Current Tick: " + entity.getBlockState().getValue(RedstoneClockBlock.CLOCK_TIMER));
        Component increaseBy10 = Component.literal("Right Click: Increase by 10");
        Component increaseBy50 = Component.literal("Ctrl Right Click: Increase by 50");
        Component decreaseBy10 = Component.literal("Shift Right Click: Decrease by 10");
        Component decreaseBy50 = Component.literal("Shift Ctrl Right Click: Decrease by 50");

        return List.of(informationTitle, empty, currentTick, increaseBy10, increaseBy50, decreaseBy10, decreaseBy50);
    }

    public static List<Component> getBlockBreakerInformation(BlockBreakerBlockEntity entity) {

        boolean isWhiteList = !entity.getItemStackHandler().getStackInSlot(1).isEmpty();
        Item whiteListItem = entity.getItemStackHandler().getStackInSlot(1).getItem();
        String translatedWhiteListItem = whiteListItem.getName(whiteListItem.getDefaultInstance()).getString();

        boolean isBlackList = !entity.getItemStackHandler().getStackInSlot(2).isEmpty();
        Item blackListItem = entity.getItemStackHandler().getStackInSlot(2).getItem();
        String translatedBlackListItem = blackListItem.getName(blackListItem.getDefaultInstance()).getString();

        Item currentTool = entity.getItemStackHandler().getStackInSlot(0).getItem();
        String translatedTool = currentTool.getName(currentTool.getDefaultInstance()).getString();

        int toolDurabilityTaken = entity.getItemStackHandler().getStackInSlot(0).getDamageValue();
        int toolDurabilityRemaining = entity.getItemStackHandler().getStackInSlot(0).getMaxDamage() - toolDurabilityTaken;

        Component currentToolTooltip = Component.literal("NULL").withStyle(ChatFormatting.RED);
        Component currentItemTooltip = Component.literal("No Filter!").withStyle(ChatFormatting.RED);

        if(!translatedTool.equals("Air")) {
            currentToolTooltip = Component.literal("Current Tool: " + translatedTool + " with " + toolDurabilityRemaining + " durability");
        }

        if(translatedTool.equals("Air")) {
            currentToolTooltip = Component.literal("Current Tool: No Tool");
        }

        if (isWhiteList) {
            currentItemTooltip = Component.literal("Will only break " + translatedWhiteListItem);
        }
        if (isBlackList) {
            currentItemTooltip = Component.literal("Will break any block except " + translatedBlackListItem);
        }

        Component informationTitle = Component.literal("Block Breaker Information").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.BOLD);
        Component empty = Component.literal("");
        Component currentTickrate = Component.literal("Ticks Per Block Break: " + entity.getBlockState().getValue(BlockBreakerBlock.TIMER));
     //   Component currentProgress = Component.literal("Progress: " + entity.getPersistentData().getInt("block_breaker.progress"));
        Component increaseBy10 = Component.literal("Shift Right Click: Increase by 10").withStyle(ChatFormatting.GREEN);
        Component decreaseBy10 = Component.literal("Shift Ctrl Right Click: Decrease by 10").withStyle(ChatFormatting.RED);

        return List.of(informationTitle, empty, currentTickrate, increaseBy10, decreaseBy10, currentToolTooltip, currentItemTooltip);

    }
    public static List<Component> getBlockPlacerInformation(BlockPlacerBlockEntity entity) {

        Item itemPlaced = entity.getItemStackHandler().getStackInSlot(0).getItem();
        String translatedPlaced  = itemPlaced.getName(itemPlaced.getDefaultInstance()).getString();

        Component placing = Component.literal("Placing Nothing!").withStyle(ChatFormatting.RED);

        if (!translatedPlaced.equals("Air")) {
            placing = Component.literal("Placing: " + translatedPlaced);
        }

        Component informationTitle = Component.literal("Block Placer Information").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.BOLD);
        Component empty = Component.literal("");
        Component currentTickrate = Component.literal("Ticks Per Block Place: " + entity.getBlockState().getValue(BlockPlacerBlock.TIMER));
        Component increaseBy10 = Component.literal("Shift Right Click: Increase by 10").withStyle(ChatFormatting.GREEN);
        Component decreaseBy10 = Component.literal("Shift Ctrl Right Click: Decrease by 10").withStyle(ChatFormatting.RED);

        return List.of(informationTitle, empty, currentTickrate, increaseBy10, decreaseBy10, placing);

    }


    public static List<Component> getFluidGenInformation(FluidGeneratorBlockEntity entity) {

        Block fluid  = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(entity.getResource()));
        assert fluid != null;
        String name = fluid.getName().getString();
        Component collecting = Component.literal("Generating Nothing!").withStyle(ChatFormatting.RED);

        if (!name.equals("Air")) {
            collecting = Component.literal("Generating: " + name + " at " + entity.getFluidAmount() + "mb per " + entity.getTickrate() + " ticks");
        }

        Component informationTitle = Component.literal("Fluid Generator Information").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.BOLD);
        Component empty = Component.literal("");

        return List.of(informationTitle, empty, collecting);

    }

    public static List<Component> getResourceGen2Information(ResourceGenerator2BlockEntity entity) {

        Block block = Objects.requireNonNull(entity.getLevel()).getBlockState(entity.getBlockPos().above()).getBlock();
        String name = block.getName().getString();
        Component collecting = Component.literal("Generating Nothing!").withStyle(ChatFormatting.RED);

        if (!name.equals("Air") && entity.getBlockState().getValue(ResourceGenerator2Block.LIT)) {
            collecting = Component.literal("Generating: " + name + " every " + entity.getTickrate() + " ticks");
        }


        System.out.println("ResourceGen2Information: " + block);
        Component informationTitle = Component.literal("Resource Generator 2 Information").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.BOLD);
        Component empty = Component.literal("");

        return List.of(informationTitle, empty, collecting);

    }

    public static List<Component> getDryingTableInformation(DryingTableBlockEntity entity) {

        Component informationTitle = Component.literal("Drying Table Information").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.BOLD);

        if (entity.getBlockState().getValue(DryingTableBlock.WATERLOGGED)) {
            informationTitle = Component.literal("Soaking Table Information").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.BOLD);
        }

        Component waterlogged = Component.literal("").withStyle(ChatFormatting.RED);

        if (!entity.getBlockState().getValue(DryingTableBlock.WATERLOGGED)) {
            waterlogged = Component.literal("Waterlog to create a Drying Table");
        }

        Component empty = Component.literal("");

        if (!entity.getBlockState().getValue(DryingTableBlock.WATERLOGGED)) {
            return List.of(informationTitle, empty, waterlogged);
        } else {

            return List.of(informationTitle, empty);
        }
    }

    public static List<Component> getItemRepairerInformation(ItemRepairerBlockEntity entity) {

        Item itemBeingRepaired = entity.getItemStackHandler().getStackInSlot(0).getItem();
        String translatedWhiteListItem = itemBeingRepaired.getName(itemBeingRepaired.getDefaultInstance()).getString();

        Item itemRepaired = entity.getItemStackHandler().getStackInSlot(1).getItem();
        String translatedRepaired  = itemRepaired.getName(itemRepaired.getDefaultInstance()).getString();

        int toolDurabilityTaken = entity.getItemStackHandler().getStackInSlot(0).getDamageValue();

        Component repairer = Component.literal("Repairing Nothing!").withStyle(ChatFormatting.RED);

        if (!translatedWhiteListItem.equals("Air")) {
            repairer = Component.literal("Repairing: " + translatedWhiteListItem + " " + toolDurabilityTaken + " to go ");
        }

        Component stats = Component.literal("No Items repaired");

        if (!translatedRepaired.equals("Air")) {
            stats = Component.literal("Repaired: " + translatedRepaired);
        }

        Component informationTitle = Component.literal("Drying Table Information").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.BOLD);
        Component empty = Component.literal("");

        return List.of(informationTitle, empty, repairer, stats);
    }

    public static List<Component> getEnderScramblerInformation(EnderScramblerBlockEntity entity) {

        int currentRange = entity.getBlockState().getValue(EnderScramblerBlock.SCRAMBLER_RANGE);
        boolean isRunning = entity.getBlockState().getValue(EnderScramblerBlock.POWERED);

        Component running = Component.literal("Range: " + currentRange);
        Component allowed = Component.literal("Teleporting Allowed!");

        if (isRunning) {
            allowed = Component.literal("Teleporting Not Allowed!").withStyle(ChatFormatting.RED);
        }

        Component informationTitle = Component.literal("Ender Scrambler Information").withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.BOLD);
        Component empty = Component.literal("");
        Component instructions1 = Component.literal("Right Click: Increase Range").withStyle(ChatFormatting.GREEN);
        Component instructions2 = Component.literal("Shift Right Click: Decrease Range").withStyle(ChatFormatting.RED);
        Component instructions3 = Component.literal("Offhand Right Click to Enable/Disable");

        return List.of(informationTitle, empty, running, instructions1, instructions2,instructions3, allowed);
    }

}

