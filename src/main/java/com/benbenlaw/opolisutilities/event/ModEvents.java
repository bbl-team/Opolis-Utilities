package com.benbenlaw.opolisutilities.event;

//@Mod.EventBusSubscriber(modid = OpolisUtilities.MOD_ID)

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.block.entity.custom.EnderScramblerBlockEntity;
import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.benbenlaw.opolisutilities.item.ModDataComponents;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.item.custom.AnimalNetItem;
import com.benbenlaw.opolisutilities.networking.payload.OnOffButtonPayload;
import com.benbenlaw.opolisutilities.networking.payload.PortableGUISelectorPayload;
import com.benbenlaw.opolisutilities.screen.utils.ConfigValues;
import com.benbenlaw.opolisutilities.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = OpolisUtilities.MOD_ID)
public class ModEvents {

    public static BlockPos globalEntity;
    public static Level globalLevel;

    @SubscribeEvent
    public static void cancelEndermanTeleportation(EntityTeleportEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EnderMan) {
            BlockPos pos = entity.getOnPos();
            BlockEntity blockEntity = entity.level().getBlockEntity(pos);

            int maxRange = ConfigValues.ENDER_SCRAMBLER_MAX_RANGE; // Get the maximum range

            for (int x = -maxRange; x <= maxRange; x++) {
                for (int y = -maxRange; y <= maxRange; y++) {
                    for (int z = -maxRange; z <= maxRange; z++) {
                        BlockPos p = pos.offset(x, y, z);
                        BlockState state = entity.level().getBlockState(p);
                        if (state.is(ModBlocks.ENDER_SCRAMBLER.get())) {
                            BlockEntity enderScramblerBlockEntity = entity.level().getBlockEntity(p);

                            if (enderScramblerBlockEntity instanceof EnderScramblerBlockEntity e) {
                                int r1 = e.SCRAMBLER_RANGE;

                                for (int x1 = -r1; x1 <= r1; x1++) {
                                    for (int y1 = -r1; y1 <= r1; y1++) {
                                        for (int z1 = -r1; z1 <= r1; z1++) {
                                            BlockPos p1 = pos.offset(x1, y1, z1);
                                            BlockState state1 = entity.level().getBlockState(p1);
                                            if (state1.is(ModBlocks.ENDER_SCRAMBLER.get())) {
                                                if (state1.getValue(EnderScramblerBlock.POWERED).equals(true)) {
                                                    event.setCanceled(true);
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    @SubscribeEvent
    public static void getPlayerDeathPoint(LivingDeathEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof ServerPlayer) {
            globalEntity = entity.getOnPos();
            globalLevel = entity.level();
        }

    }



    @SubscribeEvent
    public static void addDeathStoneOnPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {

        Player player = event.getEntity();
        Level level = player.level();

        ItemStack deathStoneItem = new ItemStack(ModItems.DEATH_STONE.get());

        if (!(globalEntity == null) && !(globalLevel == null)) {

            deathStoneItem.set(ModDataComponents.INT_X.get(), globalEntity.getX());
            deathStoneItem.set(ModDataComponents.INT_Y.get(), globalEntity.getY());
            deathStoneItem.set(ModDataComponents.INT_Z.get(), globalEntity.getZ());

            ResourceLocation dim = globalLevel.dimension().location();

            deathStoneItem.set(ModDataComponents.DIMENSION.get(), dim.getNamespace() +":"+ dim.getPath());

        }

        if (level.getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).get()) {

            level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(),
                    deathStoneItem));
        }

        if (!level.getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).get()) {
            player.addItem(deathStoneItem);
        }
    }

    @SubscribeEvent
    public static void addLootBoxesToEntities(LivingDeathEvent event) {


        Vec3 entityPos = event.getEntity().position();
        Level world = event.getEntity().level();
        Entity e = event.getEntity();

        if (!(e instanceof ServerPlayer) && Math.random() > ConfigFile.basicLootBoxDropChance.get()) {
            world.addFreshEntity(new ItemEntity(world, entityPos.x(), entityPos.y(), entityPos.z(),
                    new ItemStack(ModItems.BASIC_LOOT_BOX.get())));
        }
    }

    //Add Crook to sheep temp
    @SubscribeEvent
    public static void sheepFollowCrook(EntityJoinLevelEvent event) {

        if (event.getEntity() instanceof Sheep sheep) {
            int priority = getTemptGoalPriority(sheep);
            if (priority >= 0)
                sheep.goalSelector.addGoal(priority, new TemptGoal(sheep, 1.1D, Ingredient.of(ModItems.CROOK.get()), false));
        }
    }

    public static int getTemptGoalPriority(Mob mob) {
        return mob.goalSelector.getAvailableGoals().stream()
                .filter(goal -> goal.getGoal() instanceof TemptGoal)
                .findFirst()
                .map(WrappedGoal::getPriority)
                .orElse(-1);
    }


    public static final List<String> TAGS_TO_REMOVE = List.of(
            "SleepingX", "SleepingY", "SleepingZ" // We need to remove sleeping tags because they case issues
    );

    @SubscribeEvent
    public static void onEntityRightClickEvent(PlayerInteractEvent.EntityInteract event) {

        Level level = event.getLevel();
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        Entity livingEntity = event.getTarget();

        if (!level.isClientSide()) {

            if (stack.getItem() instanceof AnimalNetItem && stack.get(ModDataComponents.ENTITY_TYPE.get()) == null) {

                ItemStack itemstack = player.getItemInHand(hand);
                DataComponentMap resultItemWithDataComponents = itemstack.getComponents();
                itemstack.applyComponents(resultItemWithDataComponents);

                boolean hostileMobs = ConfigFile.animalNetHostileMobs.get();
                boolean waterMobs = ConfigFile.animalNetWaterMobs.get();
                boolean animalMobs = ConfigFile.animalNetAnimalMobs.get();
                boolean villagerMobs = ConfigFile.animalNetVillagerMobs.get();

                boolean captureHostile = livingEntity instanceof Monster && hostileMobs;
                boolean captureWater = livingEntity instanceof WaterAnimal && waterMobs;
                boolean captureAnimal = livingEntity instanceof Animal && animalMobs;
                boolean captureVillagers = livingEntity instanceof Villager && villagerMobs;

                if (!level.isClientSide()) {
                    if (captureHostile || captureWater || captureAnimal || captureVillagers || livingEntity.isAlliedTo(player)) {

                        // Capture the mob
                        assert resultItemWithDataComponents != null;

                        final var nbt = new CompoundTag();
                        livingEntity.saveWithoutId(nbt);
                        TAGS_TO_REMOVE.forEach(nbt::remove);
                        itemstack.set(ModDataComponents.ENTITY_TYPE.get(), EntityType.getKey(livingEntity.getType()).toString());
                        itemstack.set(ModDataComponents.ENTITY_DATA.get(), nbt);
                        livingEntity.remove(Entity.RemovalReason.DISCARDED);

                        player.sendSystemMessage(Component.translatable("tooltips.animal_net.mob_caught").withStyle(ChatFormatting.GREEN));
                        level.playSound(null, livingEntity.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS);

                    } else {
                        player.sendSystemMessage(Component.translatable("tooltips.animal_net.not_compatible_mob").withStyle(ChatFormatting.RED));
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void doorBellSounds(PlayerInteractEvent.RightClickBlock event) {
        if(ConfigFile.woodenButtonsMakeDoorbellSound.get()) {
            Level level = event.getLevel();
            BlockState state = event.getLevel().getBlockState(event.getPos());
            if (!level.isClientSide()) {
                if (state.is(BlockTags.WOODEN_BUTTONS)) {
                    level.playSound(null, event.getPos(), ModSounds.DOORBELL.get(), SoundSource.BLOCKS, 0.25F, 1.0F);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onScrollWheelUse(InputEvent.MouseScrollingEvent event) {
        Player player = Minecraft.getInstance().player;
        assert player != null;
        ItemStack itemStack = player.getMainHandItem();

        double scrollDelta = event.getScrollDeltaY();

        if (player.isCrouching() && scrollDelta != 0.0 && itemStack.is(ModItems.PORTABLE_GUI)) {
            int locationValue = 0;

            if (itemStack.get(ModDataComponents.LOCATION_VALUE) != null) {
                locationValue = itemStack.get(ModDataComponents.LOCATION_VALUE);
            }

            boolean increase = scrollDelta > 0;

            if (increase) {
                locationValue++;
                if (locationValue > 5) {
                    locationValue = 1; // Wrap around to 1
                }
            } else {
                locationValue--;
                if (locationValue < 1) {
                    locationValue = 5; // Wrap around to 8
                }
            }

            PacketDistributor.sendToServer(new PortableGUISelectorPayload(player.getInventory().selected, increase, locationValue));

            event.setCanceled(true);
        }
    }


}


