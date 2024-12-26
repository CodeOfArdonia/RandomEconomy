package com.iafenvoy.nee.registry;

import com.iafenvoy.nee.NotEnoughEconomy;
import com.iafenvoy.nee.item.ChequeItem;
import com.iafenvoy.nee.item.CoinItem;
import com.iafenvoy.nee.trade.CoinExchangeHolder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class NeeItems {
    public static final CoinItem COPPER_COIN = register("copper_coin", new CoinItem(1));
    public static final CoinItem IRON_COIN = register("iron_coin", new CoinItem(10));
    public static final CoinItem GOLD_COIN = register("gold_coin", new CoinItem(100));
    public static final CoinItem DIAMOND_COIN = register("diamond_coin", new CoinItem(1000));
    public static final CoinItem EMERALD_COIN = register("emerald_coin", new CoinItem(10000));
    public static final CoinItem NETHERITE_COIN = register("netherite_coin", new CoinItem(100000));

    public static final ChequeItem CHEQUE = register("cheque", new ChequeItem());

    public static <T extends Item> T register(String id, T item) {
        NeeItemGroups.ITEMS.add(item);
        return Registry.register(Registries.ITEM, Identifier.of(NotEnoughEconomy.MOD_ID, id), item);
    }

    public static void init() {
        CoinExchangeHolder.register(COPPER_COIN, new CoinExchangeHolder(Items.AIR, 0, IRON_COIN, 10));
        CoinExchangeHolder.register(IRON_COIN, new CoinExchangeHolder(COPPER_COIN, 10, GOLD_COIN, 10));
        CoinExchangeHolder.register(GOLD_COIN, new CoinExchangeHolder(IRON_COIN, 10, DIAMOND_COIN, 10));
        CoinExchangeHolder.register(DIAMOND_COIN, new CoinExchangeHolder(GOLD_COIN, 10, EMERALD_COIN, 10));
        CoinExchangeHolder.register(EMERALD_COIN, new CoinExchangeHolder(DIAMOND_COIN, 10, NETHERITE_COIN, 10));
        CoinExchangeHolder.register(NETHERITE_COIN, new CoinExchangeHolder(EMERALD_COIN, 10, Items.AIR, 0));
    }
}
