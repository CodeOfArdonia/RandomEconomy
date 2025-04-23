package com.iafenvoy.random.economy.registry;

import com.iafenvoy.random.economy.RandomEconomy;
import com.iafenvoy.random.economy.item.ChequeItem;
import com.iafenvoy.random.economy.item.CoinItem;
import com.iafenvoy.random.economy.trade.CoinExchangeHolder;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public final class NeeItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(RandomEconomy.MOD_ID, RegistryKeys.ITEM);

    public static final RegistrySupplier<CoinItem> COPPER_COIN = register("copper_coin", () -> new CoinItem(1));
    public static final RegistrySupplier<CoinItem> IRON_COIN = register("iron_coin", () -> new CoinItem(10));
    public static final RegistrySupplier<CoinItem> GOLD_COIN = register("gold_coin", () -> new CoinItem(100));
    public static final RegistrySupplier<CoinItem> DIAMOND_COIN = register("diamond_coin", () -> new CoinItem(1000));
    public static final RegistrySupplier<CoinItem> EMERALD_COIN = register("emerald_coin", () -> new CoinItem(10000));
    public static final RegistrySupplier<CoinItem> NETHERITE_COIN = register("netherite_coin", () -> new CoinItem(100000));

    public static final RegistrySupplier<ChequeItem> CHEQUE = register("cheque", ChequeItem::new);

    public static <T extends Item> RegistrySupplier<T> register(String id, Supplier<T> item) {
        RegistrySupplier<T> r = REGISTRY.register(id, item);
        NeeItemGroups.ITEMS.add(r::get);
        return r;
    }

    public static void init() {
        CoinExchangeHolder.register(COPPER_COIN.get(), new CoinExchangeHolder(Items.AIR, 0, IRON_COIN.get(), 10));
        CoinExchangeHolder.register(IRON_COIN.get(), new CoinExchangeHolder(COPPER_COIN.get(), 10, GOLD_COIN.get(), 10));
        CoinExchangeHolder.register(GOLD_COIN.get(), new CoinExchangeHolder(IRON_COIN.get(), 10, DIAMOND_COIN.get(), 10));
        CoinExchangeHolder.register(DIAMOND_COIN.get(), new CoinExchangeHolder(GOLD_COIN.get(), 10, EMERALD_COIN.get(), 10));
        CoinExchangeHolder.register(EMERALD_COIN.get(), new CoinExchangeHolder(DIAMOND_COIN.get(), 10, NETHERITE_COIN.get(), 10));
        CoinExchangeHolder.register(NETHERITE_COIN.get(), new CoinExchangeHolder(EMERALD_COIN.get(), 10, Items.AIR, 0));
    }
}
