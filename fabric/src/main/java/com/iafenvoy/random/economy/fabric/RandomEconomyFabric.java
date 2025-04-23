package com.iafenvoy.random.economy.fabric;

import com.iafenvoy.random.economy.RandomEconomy;
import net.fabricmc.api.ModInitializer;

public final class RandomEconomyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        RandomEconomy.init();
        RandomEconomy.process();
    }
}
