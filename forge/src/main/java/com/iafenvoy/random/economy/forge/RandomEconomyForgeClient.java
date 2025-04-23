package com.iafenvoy.random.economy.forge;

import com.iafenvoy.random.economy.RandomEconomyClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class RandomEconomyForgeClient {
    @SubscribeEvent
    public static void onInit(FMLClientSetupEvent event) {
        event.enqueueWork(RandomEconomyClient::process);
    }
}
