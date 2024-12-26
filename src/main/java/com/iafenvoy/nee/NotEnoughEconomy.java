package com.iafenvoy.nee;

import com.iafenvoy.nee.command.TradeCommand;
import com.iafenvoy.nee.registry.*;
import net.fabricmc.api.ModInitializer;

public final class NotEnoughEconomy implements ModInitializer {
    public static final String MOD_ID = "not_enough_economy";

    @Override
    public void onInitialize() {
        NeeBlockEntities.init();
        NeeBlocks.init();
        NeeItemGroups.init();
        NeeItems.init();
        NeeScreenHandlers.init();
        NeeSounds.init();

        TradeCommand.register();
    }
}
