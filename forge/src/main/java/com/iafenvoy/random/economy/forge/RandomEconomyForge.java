package com.iafenvoy.random.economy.forge;

import com.iafenvoy.random.economy.RandomEconomy;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RandomEconomy.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("removal")
public final class RandomEconomyForge {
    public RandomEconomyForge() {
        EventBuses.registerModEventBus(RandomEconomy.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        RandomEconomy.init();
    }

    @SubscribeEvent
    public static void onInit(FMLCommonSetupEvent event) {
        event.enqueueWork(RandomEconomy::process);
    }
}
