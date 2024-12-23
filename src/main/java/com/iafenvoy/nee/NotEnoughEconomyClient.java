package com.iafenvoy.nee;

import com.iafenvoy.nee.registry.NeeRenderers;
import net.fabricmc.api.ClientModInitializer;

public final class NotEnoughEconomyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NeeRenderers.registerBlockEntityRenderers();
    }
}
