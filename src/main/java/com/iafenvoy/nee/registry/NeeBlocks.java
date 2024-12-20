package com.iafenvoy.nee.registry;

import com.iafenvoy.nee.item.block.ChequeTableBlock;
import com.iafenvoy.nee.item.block.ExchangeStationBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class NeeBlocks {
    public static final ExchangeStationBlock EXCHANGE_STATION = register("exchange_station", new ExchangeStationBlock());
    public static final ChequeTableBlock CHEQUE_TABLE = register("cheque_table", new ChequeTableBlock());

    public static <T extends Block> T register(String id, T block) {
        Registry.register(Registries.BLOCK, id, block);
        NeeItems.register(id, new BlockItem(block, new Item.Settings()));
        return block;
    }

    public static void init() {
    }
}
