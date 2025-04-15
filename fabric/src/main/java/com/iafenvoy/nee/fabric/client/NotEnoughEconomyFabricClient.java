package com.iafenvoy.nee.fabric.client;

import com.iafenvoy.nee.NotEnoughEconomyClient;
import net.fabricmc.api.ClientModInitializer;

public final class NotEnoughEconomyFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NotEnoughEconomyClient.process();
    }
}
