package com.iafenvoy.random.economy.fabric.client;

import com.iafenvoy.random.economy.RandomEconomyClient;
import net.fabricmc.api.ClientModInitializer;

public final class RandomEconomyFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RandomEconomyClient.process();
    }
}
