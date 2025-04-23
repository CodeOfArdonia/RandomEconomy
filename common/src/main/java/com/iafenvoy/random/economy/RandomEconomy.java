package com.iafenvoy.random.economy;

import com.iafenvoy.random.economy.command.TradeCommand;
import com.iafenvoy.random.economy.registry.*;

public final class RandomEconomy {
    public static final String MOD_ID = "random_economy";

    public static void init() {
        NeeBlocks.REGISTRY.register();
        NeeBlockEntities.REGISTRY.register();
        NeeItems.REGISTRY.register();
        NeeItemGroups.REGISTRY.register();
        NeeScreenHandlers.REGISTRY.register();
        NeeSounds.REGISTRY.register();
        TradeCommand.register();
    }

    public static void process() {
        NeeItems.init();
    }
}
