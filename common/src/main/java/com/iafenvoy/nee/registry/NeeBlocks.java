package com.iafenvoy.nee.registry;

import com.iafenvoy.nee.NotEnoughEconomy;
import com.iafenvoy.nee.item.block.ChequeTableBlock;
import com.iafenvoy.nee.item.block.ExchangeStationBlock;
import com.iafenvoy.nee.item.block.SystemStationBlock;
import com.iafenvoy.nee.item.block.TradeStationBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public final class NeeBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(NotEnoughEconomy.MOD_ID, RegistryKeys.BLOCK);

    public static final RegistrySupplier<ExchangeStationBlock> EXCHANGE_STATION = register("exchange_station", ExchangeStationBlock::new);
    public static final RegistrySupplier<ChequeTableBlock> CHEQUE_TABLE = register("cheque_table", ChequeTableBlock::new);
    public static final RegistrySupplier<TradeStationBlock> TRADE_STATION = register("trade_station", TradeStationBlock::new);
    public static final RegistrySupplier<SystemStationBlock> SYSTEM_STATION = register("system_station", SystemStationBlock::new);

    public static <T extends Block> RegistrySupplier<T> register(String id, Supplier<T> block) {
        RegistrySupplier<T> r = REGISTRY.register(id, block);
        NeeItems.register(id, () -> new BlockItem(r.get(), new Item.Settings()));
        return r;
    }
}
