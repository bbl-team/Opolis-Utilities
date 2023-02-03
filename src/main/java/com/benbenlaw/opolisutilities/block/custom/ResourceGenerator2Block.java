package com.benbenlaw.opolisutilities.block.custom;

import com.benbenlaw.opolisutilities.block.entity.ModBlockEntities;
import com.benbenlaw.opolisutilities.block.entity.custom.ResourceGenerator2BlockEntity;
import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.benbenlaw.opolisutilities.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class ResourceGenerator2Block extends BaseEntityBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public ResourceGenerator2Block(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, Boolean.valueOf(false)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55484_) {
        p_55484_.add(LIT);
    }

    /* DISABLED IN 1.16.4 MOVED TO JEI
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide) {



            pPlayer.sendSystemMessage(Component.literal("Use JEI and search $resource_generator_blocks to view what blocks can be produced!")
                    .setStyle(Style.EMPTY.withUnderlined(false).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "$resource_generator_blocks"))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to copy the tag!"))))
                    .withStyle(ChatFormatting.WHITE));

            pPlayer.sendSystemMessage(Component.literal("Place a compatible block on top and a inventory such as a chest or hopper below."));

            pPlayer.sendSystemMessage(Component.literal("This can be sped up by placing a additional block from the $resource_generator_speed_blocks tag above the block you are generating")
                    .setStyle(Style.EMPTY.withUnderlined(false).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "$resource_generator_speed_blocks"))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to copy the tag!"))))
                    .withStyle(ChatFormatting.WHITE));

        }



        return InteractionResult.SUCCESS;
    }

     */

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(LIT, false);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ResourceGenerator2BlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.RESOURCE_GENERATOR_2_BLOCK_ENTITY.get(),
                ResourceGenerator2BlockEntity::tick);
    }
}
