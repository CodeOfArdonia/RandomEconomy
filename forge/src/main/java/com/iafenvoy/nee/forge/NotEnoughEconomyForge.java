package com.iafenvoy.nee.forge;

import com.iafenvoy.nee.NotEnoughEconomy;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NotEnoughEconomy.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class NotEnoughEconomyForge {
    public NotEnoughEconomyForge() {
        EventBuses.registerModEventBus(NotEnoughEconomy.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        NotEnoughEconomy.init();
    }

    @SubscribeEvent
    public static void onInit(FMLCommonSetupEvent event) {
        event.enqueueWork(NotEnoughEconomy::process);
    }
}
