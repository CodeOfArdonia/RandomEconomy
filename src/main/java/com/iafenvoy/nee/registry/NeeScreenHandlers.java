package com.iafenvoy.nee.registry;

import com.iafenvoy.nee.NotEnoughEconomy;
import com.iafenvoy.nee.screen.gui.ChequeTableScreen;
import com.iafenvoy.nee.screen.gui.ExchangeStationScreen;
import com.iafenvoy.nee.screen.gui.TradeStationCustomerScreen;
import com.iafenvoy.nee.screen.gui.TradeStationOwnerScreen;
import com.iafenvoy.nee.screen.handler.ChequeTableScreenHandler;
import com.iafenvoy.nee.screen.handler.ExchangeStationScreenHandler;
import com.iafenvoy.nee.screen.handler.TradeStationCustomerScreenHandler;
import com.iafenvoy.nee.screen.handler.TradeStationOwnerScreenHandler;
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
    public static final ScreenHandlerType<TradeStationOwnerScreenHandler> TRADE_STATION_OWNER = register("trade_station_owner", new ScreenHandlerType<>(TradeStationOwnerScreenHandler::new, FeatureSet.of(FeatureFlags.VANILLA)));
    public static final ScreenHandlerType<TradeStationCustomerScreenHandler> TRADE_STATION_CUSTOMER = register("trade_station_customer", new ScreenHandlerType<>(TradeStationCustomerScreenHandler::new, FeatureSet.of(FeatureFlags.VANILLA)));

    public static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType<T> handler) {
        return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(NotEnoughEconomy.MOD_ID, id), handler);
    }

    public static void init() {
    }

    public static void registerScreen(){
        HandledScreens.register(EXCHANGE_STATION, ExchangeStationScreen::new);
        HandledScreens.register(CHEQUE_TABLE, ChequeTableScreen::new);
        HandledScreens.register(TRADE_STATION_OWNER, TradeStationOwnerScreen::new);
        HandledScreens.register(TRADE_STATION_CUSTOMER, TradeStationCustomerScreen::new);
    }
}
