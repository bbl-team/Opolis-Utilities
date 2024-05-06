package com.benbenlaw.opolisutilities.item.custom;

import com.benbenlaw.opolisutilities.item.ModItems;
import com.benbenlaw.opolisutilities.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static com.benbenlaw.opolisutilities.OpolisUtilities.MOD_ID;

public class WalletItem extends Item {
    public WalletItem(Properties pProperties) {
        super(pProperties);
    }

    /*
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
        public static class WalletItemHandler implements IItemHandler {
            final ItemStack stack;
            final ArrayList<WalletSlot> ITEMS = new ArrayList<>();

            private WalletItemHandler(ItemStack stack) {
                this.stack = stack;
            }

            public void setChanged() {
                CompoundTag tag = stack.getOrCreateTag();
                tag.putBoolean("dirty", true);
                tag.put("data", serializeNBT());
            }

            public void checkChanged() {
                CompoundTag tag = stack.getOrCreateTag();
                if (tag.contains("dirty") && tag.getBoolean("dirty")) {
                    if (tag.contains("data")) {
                        deserializeNBT(tag.getCompound("data"));
                        tag.putBoolean("dirty", false);
                    }
                }
            }

            @Override
            public int getSlots() {
                checkChanged();
                return ITEMS.size();
            }

            @Override
            public @NotNull ItemStack getStackInSlot(int slot) {
                return ItemStack.EMPTY;
            }

            // Returns null if item doesn't exist!
            public WalletSlot getWalletSlot(int slot) {
                return ITEMS.get(slot);
            }

            public int getSlotByItem(Item item) {
                return ITEMS.indexOf(getWalletSlotByItem(item));
            }

            public WalletSlot getWalletSlotByItem(Item item) {
                checkChanged();
                return ITEMS.stream().filter(e -> e.getItem() == item).findAny().orElse(null);
            }

            @Override
            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if (stack.is(ModTags.Items.WALLET_ITEM)) {
                    checkChanged();
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
                return stack;
            }

            @Override
            public @NotNull ItemStack extractItem(int slot, int maxamount, boolean simulate) {
                checkChanged();
                int amount = maxamount;
                if (ITEMS.size() >= slot && !ITEMS.isEmpty()) {
                    WalletSlot walletSlot = ITEMS.get(slot);
                    if (walletSlot.getAmount() < amount)
                        amount = walletSlot.getAmount();

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
                checkChanged();
                return ITEMS.size() + 1;
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return true;
            }

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

        private final WalletItemHandler handler;
        private final LazyOptional<IItemHandler> ITEMLO;

        public CapabilityProvider(ItemStack stack) {
            this.handler = new WalletItemHandler(stack);
            this.ITEMLO = LazyOptional.of(() -> handler);
        }

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

    private CapabilityProvider.WalletItemHandler getHandler(ItemStack wallet) {

        var LO = wallet.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
        if (LO.isPresent()) {
            var handler = LO.get();
            if (handler instanceof CapabilityProvider.WalletItemHandler walletItemHandler)
                return walletItemHandler;
        }

        return null;
    }

    public void extractCurrency(ItemStack wallet, Item item, int amount) {
        var handler = getHandler(wallet);
        if (handler == null) return;
        var slot = handler.getSlotByItem(item);
        if (slot == -1) return;
        handler.extractItem(slot, amount, false);
    }

    public int getCurrencyStored(ItemStack wallet, Item item) {
        var handler = getHandler(wallet);
        if (handler != null) {
            var slot = handler.getWalletSlotByItem(item);
            if (slot == null) return 0;
            return slot.getAmount();
        }
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

        if (Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltips.wallet.shift.held", getCurrencyName(), getCurrencyName())
                    .withStyle(ChatFormatting.GREEN));
        } else {
            components.add(Component.translatable("tooltips.wallet.hover.shift").withStyle(ChatFormatting.BLUE));
        }

        if (Screen.hasShiftDown()) {

            LazyOptional<IItemHandler> handlerLazyOptional = stack.getCapability(ForgeCapabilities.ITEM_HANDLER);

            if (handlerLazyOptional.isPresent()) {
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
        } else {
            components.add(Component.translatable("tooltips.home_stone.hover.alt")
                    .withStyle(ChatFormatting.BLUE));
        }

        super.appendHoverText(stack, level, components, flag);
    }

     */
}








