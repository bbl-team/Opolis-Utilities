package com.benbenlaw.opolisutilities.item;

import com.benbenlaw.opolisutilities.registry.EnumBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
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

    @SuppressWarnings("unchecked")
    public static EnumMap<ColorableBlocks, EnumMap<EnumBlockItem, RegistryObject<?>>> register(String id, Instance instance, Supplier<Block> blockSupplier, Item.Properties itemProperties) {
        EnumMap<ColorableBlocks, EnumMap<EnumBlockItem, RegistryObject<?>>> MAP = new EnumMap<>(ColorableBlocks.class);
        for (ColorableBlocks color : ColorableBlocks.values()) {
            var REG_MAP = MAP.computeIfAbsent(color, (c) -> new EnumMap<>(EnumBlockItem.class));
            RegistryObject<Block> blockRO = instance.BLOCKS.register("%s_%s".formatted(color.id, id), blockSupplier);
            REG_MAP.put(EnumBlockItem.BLOCK, blockRO);
            REG_MAP.put(EnumBlockItem.ITEM, instance.ITEMS.register("%s_%s".formatted(color.id, id), () -> new BlockItem(blockRO.get(), itemProperties)));
        }
        return MAP;
    }
}
