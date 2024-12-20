package com.iafenvoy.nee.registry;

import com.iafenvoy.nee.NotEnoughEconomy;
import com.iafenvoy.nee.screen.gui.ChequeTableScreen;
import com.iafenvoy.nee.screen.gui.ExchangeStationScreen;
import com.iafenvoy.nee.screen.handler.ChequeTableScreenHandler;
import com.iafenvoy.nee.screen.handler.ExchangeStationScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public final class NeeScreenHandlers {
    public static final ScreenHandlerType<ExchangeStationScreenHandler> EXCHANGE_STATION = register("exchange_station", new ScreenHandlerType<>(ExchangeStationScreenHandler::new, FeatureSet.of(FeatureFlags.VANILLA)));
    public static final ScreenHandlerType<ChequeTableScreenHandler> CHEQUE_TABLE = register("cheque_table", new ScreenHandlerType<>(ChequeTableScreenHandler::new, FeatureSet.of(FeatureFlags.VANILLA)));

    public static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType<T> handler) {
        return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(NotEnoughEconomy.MOD_ID, id), handler);
    }

    public static void init() {
        HandledScreens.register(EXCHANGE_STATION, ExchangeStationScreen::new);
        HandledScreens.register(CHEQUE_TABLE, ChequeTableScreen::new);
    }
}
