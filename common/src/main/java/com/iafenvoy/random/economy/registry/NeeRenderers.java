package com.iafenvoy.random.economy.registry;

import com.iafenvoy.random.economy.render.SystemStationBlockEntityRenderer;
import com.iafenvoy.random.economy.render.TradeStationBlockEntityRenderer;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class NeeRenderers {
    public static void registerBlockEntityRenderers() {
        BlockEntityRendererRegistry.register(NeeBlockEntities.TRADE_STATION.get(), TradeStationBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NeeBlockEntities.SYSTEM_STATION.get(), SystemStationBlockEntityRenderer::new);
    }
}
