package com.benbenlaw.opolisutilities.data.loot;

import com.benbenlaw.opolisutilities.block.ModBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLootTables extends BlockLoot {
    @Override
    protected void addTables() {
        simpleDrops();
    }

    private void simpleDrops() {
        this.dropSelf(ModBlocks.DRYING_TABLE.get());
        this.dropSelf(ModBlocks.RESOURCE_GENERATOR.get());

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
