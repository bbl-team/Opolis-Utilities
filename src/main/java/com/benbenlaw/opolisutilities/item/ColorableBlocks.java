package com.benbenlaw.opolisutilities.item;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import java.util.EnumMap;
import java.util.function.Function;
import java.util.function.Predicate;
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

    public record Instance<T, X>(DeferredRegister<T> ADR, DeferredRegister<X> BDR) {
        public <I extends T, L extends X> EnumMap<ColorableBlocks, DualRegistryObject<I, L>> register(String id, Function<ColorableBlocks, Supplier<I>> supplierFunctionA, Function<ColorableBlocks, Supplier<L>> supplierFunctionB) {
            EnumMap<ColorableBlocks, DualRegistryObject<I, L>> MAP = new EnumMap<>(ColorableBlocks.class);
            for (ColorableBlocks color : ColorableBlocks.values()) {
                RegistryObject<I> A = ADR.register("%s_%s".formatted(color.id, id), supplierFunctionA.apply(color));
                RegistryObject<L> B = BDR.register("%s_%s".formatted(color.id, id), supplierFunctionB.apply(color));
                MAP.put(color, new DualRegistryObject<>(A, B));
            }
            return MAP;
        }
    }
    public record DualRegistryObject<T, X>(RegistryObject<T> A, RegistryObject<X> B) {}
}
