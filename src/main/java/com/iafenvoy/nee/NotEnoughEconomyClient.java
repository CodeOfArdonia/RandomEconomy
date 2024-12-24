package com.iafenvoy.nee;

import com.iafenvoy.nee.registry.NeeRenderers;
import com.iafenvoy.nee.registry.NeeScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class NotEnoughEconomyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NeeRenderers.registerBlockEntityRenderers();
        NeeScreenHandlers.registerScreen();
    }
}
