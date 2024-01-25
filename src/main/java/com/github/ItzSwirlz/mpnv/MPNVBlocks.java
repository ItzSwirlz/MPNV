package com.github.ItzSwirlz.mpnv;

import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MPNVBlocks {
	public static final Block PRIME_BLOCK = new Block(QuiltBlockSettings.create());

    public static void registerBlocks() {
        Registry.register(Registries.BLOCK, new Identifier("mpnv", "prime_block"), PRIME_BLOCK);
        Registry.register(Registries.ITEM, new Identifier("mpnv", "prime_block"), new BlockItem(PRIME_BLOCK, new QuiltItemSettings()));

        // TODO: make an item group
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.addItem(PRIME_BLOCK.asItem());
        });
    }
}
