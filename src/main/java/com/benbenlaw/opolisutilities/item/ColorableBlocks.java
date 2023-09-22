package com.benbenlaw.opolisutilities.item;

import com.benbenlaw.opolisutilities.registry.EnumBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.benbenlaw.opolisutilities.block.ModBlocks.BLOCKS;

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

    public static EnumMap<ColorableBlocks, EnumMap<EnumBlockItem, RegistryObject<?>>> register(String id, Instance instance, Function<ColorableBlocks, Supplier<Block>> block, Function<ColorableBlocks, Supplier<Item>> item) {
        EnumMap<ColorableBlocks, EnumMap<EnumBlockItem, RegistryObject<?>>> MAP = new EnumMap<>(ColorableBlocks.class);
        for (ColorableBlocks color : ColorableBlocks.values()) {
            var REG_MAP = MAP.computeIfAbsent(color, (c) -> new EnumMap<>(EnumBlockItem.class));
            RegistryObject<Block> blockRO = instance.BLOCKS.register("%s_%s".formatted(color.id, id), block.apply(color));
            REG_MAP.put(EnumBlockItem.BLOCK, blockRO);
            REG_MAP.put(EnumBlockItem.ITEM, instance.ITEMS.register("%s_%s".formatted(color.id, id), item.apply(color)));
        }
        return MAP;
    }
}
