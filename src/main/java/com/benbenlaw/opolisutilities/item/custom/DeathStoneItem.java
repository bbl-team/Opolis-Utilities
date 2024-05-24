package com.benbenlaw.opolisutilities.item.custom;

import net.minecraft.world.item.Item;

public class DeathStoneItem extends Item {

    public DeathStoneItem(Properties properties) {
        super(properties);
    }


    /*
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);
        CompoundTag nbt = itemstack.getTag();
        if (nbt == null) nbt = new CompoundTag();

        //Checks Location set if not nothing and send message

        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND && !nbt.contains("x")) {
            player.playNotifySound(SoundEvents.SHIELD_BLOCK, SoundSource.AMBIENT, 0.2f,1);
            player.sendSystemMessage(Component.translatable("tooltips.death_stone.how").withStyle(ChatFormatting.RED));
        }

        //If location set then teleport
        else if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND){

            ResourceKey<Level> dimension = ResourceKey.create(ResourceKey.createRegistryKey(
                    new ResourceLocation("minecraft", "dimension")),
                    new ResourceLocation(nbt.getString("namespace"), nbt.getString("path")));

            MinecraftServer minecraftserver = player.getServer();
            assert minecraftserver != null;
            ServerLevel destinationWorld = minecraftserver.getLevel(dimension);
            assert destinationWorld != null;
            player.changeDimension(destinationWorld, new ModTeleport(destinationWorld));

            //TP COORDINATES

            player.teleportTo(nbt.getFloat("x") + 0.5, nbt.getFloat("y") + 1, nbt.getFloat("z") + 0.5);
            player.getItemInHand(hand).shrink(1);
            player.playNotifySound(SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.2f, 1);
            player.sendSystemMessage(Component.translatable("tooltips.death_stone.going_saved_location").withStyle(ChatFormatting.GREEN));

        }
        return super.use(level, player, hand);
    }


    //Tooltip

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {

        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.death_stone.shift.held")
                    .withStyle(ChatFormatting.GREEN));
            }
        else {
            components.add(Component.translatable("tooltips.home_stone.hover.shift").withStyle(ChatFormatting.BLUE));
        }

        if(Screen.hasAltDown()) {

            if(stack.hasTag()) {

                components.add(Component.literal("X: " + stack.getTag().getFloat("x"))
                        .withStyle(ChatFormatting.GREEN));
                components.add(Component.literal("Y: " + stack.getTag().getFloat("y"))
                        .withStyle(ChatFormatting.GREEN));
                components.add(Component.literal("Z: " + stack.getTag().getFloat("z"))
                        .withStyle(ChatFormatting.GREEN));
                components.add(Component.literal("Dimension: " + stack.getTag().getString("dimension"))
                        .withStyle(ChatFormatting.GREEN));

            }
            if(!stack.hasTag()) {
                components.add(Component.translatable("tooltips.home_stone.no_location_set")
                        .withStyle(ChatFormatting.RED));
            }
        }
        else {
            components.add(Component.translatable("tooltips.home_stone.hover.alt")
                    .withStyle(ChatFormatting.BLUE));
        }

        super.appendHoverText(stack, level, components, flag);
    }

     */

}












