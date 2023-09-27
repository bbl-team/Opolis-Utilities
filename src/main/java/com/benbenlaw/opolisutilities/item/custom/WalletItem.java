package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.capabillties.ICapabilitySync;
import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.networking.ModMessages;
import com.benbenlaw.opolisutilities.networking.packets.PacketCapabilitySyncToClient;
import com.mojang.datafixers.types.templates.CompoundList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static com.benbenlaw.opolisutilities.OpolisUtilities.MOD_ID;

public class WalletItem extends Item {
    public static final ResourceLocation WALLET_CAP = new ResourceLocation(MOD_ID, "wallet");
    public static class WalletSlot {
        private final ResourceLocation RL;
        private final AtomicInteger integer;
        public WalletSlot(ResourceLocation RL) {
            this.RL = RL;
            this.integer = new AtomicInteger();
        }

        public WalletSlot(ItemStack stack) {
            this(ForgeRegistries.ITEMS.getKey(stack.getItem()));
        }

        public int getAmount() {
            return integer.get();
        }

        public void setAmount(int amount) {
            integer.set(amount);
        }

        public boolean isSame(ItemStack stack) {
            return getItem() != null && stack.is(getItem());
        }


        // Returns null if item doesn't exist
        public Item getItem() {
            return ForgeRegistries.ITEMS.getValue(RL);
        }
    }

    public static class CapabilityProvider implements ICapabilitySerializable<CompoundTag> {
        public static class WalletItemHandler implements IItemHandler, INBTSerializable<CompoundTag>, ICapabilitySync<WalletItemHandler.Data> {
            @Override
            public void toNetwork(FriendlyByteBuf buf) {
                buf.writeNbt(serializeNBT());
            }

            @Override
            public LazyOptional<Data> fromNetwork(FriendlyByteBuf buf) {
                return LazyOptional.of(() -> new Data(buf.readNbt()));
            }

            @Override
            public void handle(NetworkEvent.Context context, LazyOptional<Data> data) {
                data.ifPresent(e -> {
                    deserializeNBT(e.getTag());
                });
            }

            @Override
            public Capability<?> getCap() {
                return ForgeCapabilities.ITEM_HANDLER;
            }

            public static class Data {
                final CompoundTag tag;

                public Data(CompoundTag tag) {
                    this.tag = tag;
                }

                public CompoundTag getTag() {
                    return tag;
                }
            }

            final ArrayList<WalletSlot> ITEMS = new ArrayList<>();


            public void setChanged() {
                ModMessages.sendToClients(new PacketCapabilitySyncToClient(this, getCap()));
            }
            @Override
            public int getSlots() {
                return ITEMS.size();
            }

            // Only Insert/Extract
            // Use getItemInSlot() instead.
            @Override
            public @NotNull ItemStack getStackInSlot(int slot) {
                return ItemStack.EMPTY;
            }

            // Returns null if item doesn't exist!
            public WalletSlot getWalletSlot(int slot) {
                return ITEMS.get(slot);
            }

            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                Optional<WalletSlot> SLOT = ITEMS.stream().filter(e -> e.isSame(stack)).findAny();
                WalletSlot walletSlot;
                if (SLOT.isPresent()) {
                    walletSlot = SLOT.get();
                } else {
                    walletSlot = new WalletSlot(stack);
                    ITEMS.add(walletSlot);
                }

                if (simulate) {
                    return ItemStack.EMPTY;
                }

                walletSlot.setAmount(walletSlot.getAmount() + stack.getCount());
                stack.shrink(stack.getCount());
                setChanged();
                return stack;
            }

            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (slot <= ITEMS.size()) {
                    WalletSlot walletSlot = ITEMS.get(slot);
                    if (walletSlot.getAmount() >= amount && walletSlot.getItem() != null) {
                        if (simulate)
                            return new ItemStack(walletSlot.getItem(), amount);
                        ItemStack stack = new ItemStack(walletSlot.getItem(), amount);
                        walletSlot.setAmount(walletSlot.getAmount() - amount);
                        if (walletSlot.getAmount() <= 0)
                            ITEMS.remove(walletSlot);
                        setChanged();
                        return stack;
                    }
                }
                return ItemStack.EMPTY;
            }

            @Override
            public int getSlotLimit(int slot) {
                return ITEMS.size() + 1;
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return true;
            }

            @Override
            public CompoundTag serializeNBT() {
                CompoundTag tag = new CompoundTag();
                CompoundTag list = new CompoundTag();
                AtomicInteger count = new AtomicInteger(0);
                ITEMS.forEach(slot -> {
                    CompoundTag slotTag = new CompoundTag();
                    slotTag.putString("RL", slot.RL.toString());
                    slotTag.putInt("amount", slot.getAmount());
                    list.put("Item%s".formatted(count.getAndAdd(1)), slotTag);
                });
                tag.put("list", list);
                return tag;
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                ITEMS.clear();
                CompoundTag list = nbt.getCompound("list");
                list.getAllKeys().forEach(e -> {
                    CompoundTag tag = list.getCompound(e);
                    WalletSlot slot = new WalletSlot(new ResourceLocation(tag.getString("RL")));
                    slot.setAmount(tag.getInt("amount"));
                    ITEMS.add(slot);
                });
            }
        }

        private final WalletItemHandler handler = new WalletItemHandler();
        private final LazyOptional<IItemHandler> ITEMLO = LazyOptional.of(() -> handler);

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap == ForgeCapabilities.ITEM_HANDLER)
                return ITEMLO.cast();
            return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.put("handler", handler.serializeNBT());
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            if (nbt.contains("handler"))
                handler.deserializeNBT(nbt.getCompound("handler"));
        }
    }


    private final Supplier<Item> currencyItem;

    public WalletItem(Properties properties) {
        this(properties, ModItems.B_BUCKS);
    }

    public WalletItem(Properties properties, Supplier<Item> currencyItem) {
        super(properties);
        this.currencyItem = currencyItem;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, InteractionHand hand) {
        if (level.isClientSide)
            return InteractionResultHolder.pass(player.getItemInHand(hand));

        LazyOptional<IItemHandler> handlerLO = player.getOffhandItem().getCapability(ForgeCapabilities.ITEM_HANDLER);
        handlerLO.ifPresent(e -> {
            if (player.isCrouching()) {
                ItemStack oppositeHand = player.getMainHandItem();
                if (!oppositeHand.isEmpty())
                    e.insertItem(0, oppositeHand, false);
            } else if (player.getInventory().getFreeSlot() != -1) {
                // Do logic
                player.getInventory().add(e.extractItem(0, 64, false));
            }
        });


        return super.use(level, player, hand);
    }

    public void insertCurrency(ItemStack wallet, int amount) {
    }


    public void extractCurrency(ItemStack wallet, int amount) {
    }

    public int getCurrencyStored(ItemStack wallet) {
        return 0;
    }

    public String getCurrencyName() {
        return getCurrencyItem().getDefaultInstance().getDisplayName().getString();
    }

    public Item getCurrencyItem() {
        return currencyItem.get();
    }

    //Tooltip

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {

        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.wallet.shift.held", getCurrencyName(), getCurrencyName())
                    .withStyle(ChatFormatting.GREEN));
        }
        else {
            components.add(Component.translatable("tooltips.wallet.hover.shift").withStyle(ChatFormatting.BLUE));
        }

        if(Screen.hasShiftDown()) {

            LazyOptional<IItemHandler> handlerLazyOptional = stack.getCapability(ForgeCapabilities.ITEM_HANDLER);

            if(handlerLazyOptional.isPresent()) {
                handlerLazyOptional.ifPresent(e -> {
                    if (e.getSlots() > 0) {
                        components.add(Component.literal("This wallet contains:").withStyle(ChatFormatting.GREEN));

                        if (e instanceof CapabilityProvider.WalletItemHandler walletItemHandler)
                            for (int i = 0; i < walletItemHandler.getSlots(); i++) {
                                components.add(Component.literal("%s %s".formatted(walletItemHandler.getWalletSlot(i).getAmount(), walletItemHandler.getWalletSlot(i).getItem().getDefaultInstance().getDisplayName().getString())).withStyle(ChatFormatting.GREEN));
                            }
                    } else {
                        components.add(Component.literal("This Wallet Is Empty")
                                .withStyle(ChatFormatting.RED));
                    }
                });
            } else {
                components.add(Component.literal("This Wallet Is Empty")
                        .withStyle(ChatFormatting.RED));
            }
        }
        else {
            components.add(Component.translatable("tooltips.home_stone.hover.alt")
                    .withStyle(ChatFormatting.BLUE));
        }

        super.appendHoverText(stack, level, components, flag);
    }




}

        /*

            //set x,y and x inside item nbt and playsound and print message

            nbt.putFloat("b_bucks_amount", player.getOnPos().getX());


          //  nbt.putString("dimension", player.getLevel().dimension().toString());

            itemstack.setTag(nbt);
            player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.2f,1);
            player.sendSystemMessage(Component.translatable("tooltip.home_stone.location_set").withStyle(ChatFormatting.GREEN));
        }

        //Checks Location set if not nothing and send message

        if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD) && hand == InteractionHand.MAIN_HAND && !nbt.contains("x")) {
            player.playNotifySound(SoundEvents.SHIELD_BLOCK, SoundSource.AMBIENT, 0.2f,1);
            player.sendSystemMessage(Component.translatable("tooltip.home_stone.no_location").withStyle(ChatFormatting.RED));
        }

        //If location Set tps
        else if (!level.isClientSide() && level.dimension().equals(Level.OVERWORLD) && hand == InteractionHand.MAIN_HAND){

            player.teleportTo(nbt.getFloat("x") + 0.5, nbt.getFloat("y")+ 1, nbt.getFloat("z")+ 0.5);

            player.getCooldowns().addCooldown(this, ConfigFile.homeStoneCooldown.get());

            player.playNotifySound(SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.2f,1);
            player.sendSystemMessage(Component.translatable("tooltip.home_stone.going_saved_location").withStyle(ChatFormatting.GREEN));

            if(ConfigFile.homeStoneTakesDamage.get().equals(true)) {

                player.getItemBySlot(EquipmentSlot.MAINHAND).hurtAndBreak(1, player,
                        (damage) -> player.broadcastBreakEvent(player.getUsedItemHand()));
            }

        }
        return super.use(level, player, hand);
    }


    //Tooltip

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {

        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.home_stone.shift.held")
                    .withStyle(ChatFormatting.GREEN));
            }
        else {
            components.add(Component.translatable("tooltips.home_stone.hover.shift").withStyle(ChatFormatting.BLUE));
        }

        if(Screen.hasAltDown()) {

            if(stack.hasTag()) {

                components.add(Component.literal(String.valueOf("X: " + stack.getTag().getFloat("x")))
                        .withStyle(ChatFormatting.GREEN));
                components.add(Component.literal(String.valueOf("Y: " + stack.getTag().getFloat("y")))
                        .withStyle(ChatFormatting.GREEN));
                components.add(Component.literal(String.valueOf("Z: " + stack.getTag().getFloat("z")))
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
}

*/







