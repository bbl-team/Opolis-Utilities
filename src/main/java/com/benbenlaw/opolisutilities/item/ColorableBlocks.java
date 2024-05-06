package com.benbenlaw.opolisutilities.item;


import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.function.BiFunction;
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

    private final String id;
    ColorableBlocks(String id) {
        this.id = id;
    }

    public record Instance<T, X>(DeferredRegister<T> ADR, DeferredRegister<X> BDR) {

        /*
        public <I extends T, L extends X> EnumMap<ColorableBlocks, DualRegistryObject<I, L>> register(Function<String, String> idMaker, Function<ColorableBlocks, Supplier<I>> supplierFunctionA, BiFunction<ColorableBlocks, RegistryObject<I>, Supplier<L>> supplierFunctionB) {
            EnumMap<ColorableBlocks, DualRegistryObject<I, L>> MAP = new EnumMap<>(ColorableBlocks.class);
            for (ColorableBlocks color : ColorableBlocks.values()) {
                RegistryH<I> A = ADR.register(idMaker.apply(color.id), supplierFunctionA.apply(color));
                RegistryObject<L> B = BDR.register(idMaker.apply(color.id), supplierFunctionB.apply(color, A));
                MAP.put(color, new DualRegistryObject<>(A, B));
            }
            return MAP;
        }
    }

    public record DualRegistryObject<T, X>(RegistryObject<T> A, RegistryObject<X> B) implements Supplier<RegistryObject<T>> {
        @Override
        public RegistryObject<T> get() {
            return A();
        }
        
         */
    }
}
