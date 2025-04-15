package com.iafenvoy.nee;

import com.iafenvoy.nee.command.TradeCommand;
import com.iafenvoy.nee.registry.*;

public final class NotEnoughEconomy {
    public static final String MOD_ID = "not_enough_economy";

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
