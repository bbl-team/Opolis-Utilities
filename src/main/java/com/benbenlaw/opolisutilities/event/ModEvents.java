package com.benbenlaw.opolisutilities.event;

import com.benbenlaw.opolisutilities.OpolisUtilities;
import com.benbenlaw.opolisutilities.block.ModBlocks;
import com.benbenlaw.opolisutilities.block.custom.EnderScramblerBlock;
import com.benbenlaw.opolisutilities.config.ConfigFile;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.item.custom.AnimalNetItem;
import com.benbenlaw.opolisutilities.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.checkerframework.dataflow.qual.SideEffectFree;
import org.jetbrains.annotations.NotNull;


@Mod.EventBusSubscriber(modid = OpolisUtilities.MOD_ID)

public class ModEvents {

    public static Vec3 globalEntity;
    public static Level globalLevel;

    @SubscribeEvent
    public static void cancelEndermanTeleportation(EntityTeleportEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EnderMan) {
            BlockPos pos = entity.getOnPos();

            int maxRange = EnderScramblerBlock.maxRange; // Get the maximum range

            for (int x = -maxRange; x <= maxRange; x++) {
                for (int y = -maxRange; y <= maxRange; y++) {
                    for (int z = -maxRange; z <= maxRange; z++) {
                        BlockPos p = pos.offset(x, y, z);
                        BlockState state = entity.level().getBlockState(p);
                        if (state.is(ModBlocks.ENDER_SCRAMBLER.get())) {

                            int r1 = state.getValue(EnderScramblerBlock.SCRAMBLER_RANGE);

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

    @SubscribeEvent
    public static void getPlayerDeathPoint(LivingDeathEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof ServerPlayer) {
            globalEntity = entity.position();
            globalLevel = entity.level();
        }

    }


    @SubscribeEvent
    public static void addDeathStoneOnPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {

        Player player = event.getEntity();
        Level level = player.level();

        ItemStack deathStoneItem = new ItemStack(ModItems.DEATH_STONE.get());
        CompoundTag nbt = new CompoundTag();

        if (!(globalEntity == null) && !(globalLevel == null)) {

            nbt.putInt("Age", -32768);
            nbt.putDouble("x", globalEntity.x);
            nbt.putDouble("y", globalEntity.y);
            nbt.putDouble("z", globalEntity.z);
            ResourceLocation dim = globalLevel.dimension().location();
            nbt.putString("dimension", dim.getNamespace() + ":" + dim.getPath());
            nbt.putString("namespace", dim.getNamespace());
            nbt.putString("path", dim.getPath());

            deathStoneItem.setTag(nbt);
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


    @SubscribeEvent
    public static void onEntityRightClickEvent(PlayerInteractEvent.EntityInteract event) {

        Level level = event.getLevel();
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        Entity livingEntity = event.getTarget();

        if (!level.isClientSide()) {

            if (stack.getItem() instanceof AnimalNetItem && !stack.getTag().contains("entity")) {

                ItemStack itemstack = player.getItemInHand(hand);
                CompoundTag nbt = itemstack.getTag();

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
                        assert nbt != null;
                        nbt.putString("entity", EntityType.getKey(livingEntity.getType()).toString());
                        livingEntity.saveWithoutId(nbt);
                        stack.setTag(nbt);
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
                    level.playSound(null, event.getPos(), ModSounds.DOORBELL.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }
}


