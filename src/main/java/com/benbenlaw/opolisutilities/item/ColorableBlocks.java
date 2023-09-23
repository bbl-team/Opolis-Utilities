package com.benbenlaw.opolisutilities.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.EnumMap;
import java.util.function.Function;
import java.util.function.Supplier;

public enum ColorableBlocks {
    WHITE("white"),
    GRAY("gray"),
    LIGHT_GRAY("light_gray"),
    PINK("pink"),
    LIME("lime"),
    YELLOW("yellow"),
    LIGHT_BLUE("light_blue"),
    MAGENTA("magenta"),
    ORANGE("orange"),
    CYAN("cyan"),
    PURPLE("purple"),
    BLUE("blue"),
    BROWN("brown"),
    GREEN("green"),
    RED("red"),
    BLACK("black");

    private String id;
    ColorableBlocks(String id) {
        this.id = id;
    }

    public record Instance(DeferredRegister<Block> BLOCKS, DeferredRegister<Item> ITEMS) {}
    public static class DualInstance<T, X> {
        private final RegistryObject<T> A;
        private final RegistryObject<X> B;
        public DualInstance(RegistryObject<T> A, RegistryObject<X> B) {
            this.A = A;
            this.B = B;
        }

        public RegistryObject<T> getA() {
            return A;
        }

        public RegistryObject<X> getB() {
            return B;
        }
    }

    public static EnumMap<ColorableBlocks, DualInstance<Block, Item>> register(String id, Instance instance, Function<ColorableBlocks, Supplier<Block>> block, Function<ColorableBlocks, Supplier<Item>> item) {
        EnumMap<ColorableBlocks, DualInstance<Block, Item>> MAP = new EnumMap<>(ColorableBlocks.class);
        for (ColorableBlocks color : ColorableBlocks.values()) {
            RegistryObject<Block> blockRO = instance.BLOCKS.register("%s_%s".formatted(color.id, id), block.apply(color));
            RegistryObject<Item> itemRO = instance.ITEMS.register("%s_%s".formatted(color.id, id), item.apply(color));
            MAP.computeIfAbsent(color, (c) -> new DualInstance<>(blockRO, itemRO));
        }
        return MAP;
    }
}
