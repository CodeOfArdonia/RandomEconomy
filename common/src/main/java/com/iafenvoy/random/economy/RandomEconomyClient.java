package com.iafenvoy.random.economy;

import com.iafenvoy.random.economy.registry.NeeRenderers;
import com.iafenvoy.random.economy.registry.NeeScreenHandlers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class RandomEconomyClient {
    public static void process() {
        NeeScreenHandlers.registerScreen();
        NeeRenderers.registerBlockEntityRenderers();
    }
}
