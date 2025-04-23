package com.iafenvoy.random.economy.registry;

import com.iafenvoy.random.economy.RandomEconomy;
import com.iafenvoy.random.economy.screen.gui.*;
import com.iafenvoy.random.economy.screen.handler.*;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

import java.util.function.Supplier;

public final class NeeScreenHandlers {
    public static final DeferredRegister<ScreenHandlerType<?>> REGISTRY = DeferredRegister.create(RandomEconomy.MOD_ID, RegistryKeys.SCREEN_HANDLER);

    public static final RegistrySupplier<ScreenHandlerType<ExchangeStationScreenHandler>> EXCHANGE_STATION = register("exchange_station", () -> new ScreenHandlerType<>(ExchangeStationScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final RegistrySupplier<ScreenHandlerType<ChequeTableScreenHandler>> CHEQUE_TABLE = register("cheque_table", () -> new ScreenHandlerType<>(ChequeTableScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final RegistrySupplier<ScreenHandlerType<TradeStationOwnerScreenHandler>> TRADE_STATION_OWNER = register("trade_station_owner", () -> new ScreenHandlerType<>(TradeStationOwnerScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final RegistrySupplier<ScreenHandlerType<TradeStationCustomerScreenHandler>> TRADE_STATION_CUSTOMER = register("trade_station_customer", () -> new ScreenHandlerType<>(TradeStationCustomerScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final RegistrySupplier<ScreenHandlerType<SystemStationOwnerScreenHandler>> SYSTEM_STATION_OWNER = register("system_station_owner", () -> new ScreenHandlerType<>(SystemStationOwnerScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final RegistrySupplier<ScreenHandlerType<SystemStationCustomerScreenHandler>> SYSTEM_STATION_CUSTOMER = register("system_station_customer", () -> new ScreenHandlerType<>(SystemStationCustomerScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    public static final RegistrySupplier<ScreenHandlerType<TradeCommandScreenHandler>> TRADE_COMMAND = register("trade_command", () -> MenuRegistry.ofExtended(TradeCommandScreenHandler::new));

    public static <T extends ScreenHandler> RegistrySupplier<ScreenHandlerType<T>> register(String id, Supplier<ScreenHandlerType<T>> handler) {
        return REGISTRY.register(id, handler);
    }

    public static void registerScreen() {
        MenuRegistry.registerScreenFactory(EXCHANGE_STATION.get(), ExchangeStationScreen::new);
        MenuRegistry.registerScreenFactory(CHEQUE_TABLE.get(), ChequeTableScreen::new);
        MenuRegistry.registerScreenFactory(TRADE_STATION_OWNER.get(), TradeStationOwnerScreen::new);
        MenuRegistry.registerScreenFactory(TRADE_STATION_CUSTOMER.get(), TradeStationCustomerScreen::new);
        MenuRegistry.registerScreenFactory(SYSTEM_STATION_OWNER.get(), SystemStationOwnerScreen::new);
        MenuRegistry.registerScreenFactory(SYSTEM_STATION_CUSTOMER.get(), SystemStationCustomerScreen::new);
        MenuRegistry.registerScreenFactory(TRADE_COMMAND.get(), TradeCommandScreen::new);
    }
}
