package com.iafenvoy.nee.fabric;

import com.iafenvoy.nee.NotEnoughEconomy;
import net.fabricmc.api.ModInitializer;

public final class NotEnoughEconomyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        NotEnoughEconomy.init();
        NotEnoughEconomy.process();
    }
}
