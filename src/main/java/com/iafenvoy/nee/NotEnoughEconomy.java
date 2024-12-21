package com.iafenvoy.nee;

import com.iafenvoy.nee.registry.*;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public final class NotEnoughEconomy implements ModInitializer {
    public static final String MOD_ID = "not_enough_economy";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        NeeBlockEntities.init();
        NeeBlocks.init();
        NeeItemGroups.init();
        NeeItems.init();
        NeeScreenHandlers.init();
        NeeSounds.init();
    }
}
