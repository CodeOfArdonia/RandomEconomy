package com.iafenvoy.nee.registry;

import com.iafenvoy.nee.render.SystemStationBlockEntityRenderer;
import com.iafenvoy.nee.render.TradeStationBlockEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl;

@Environment(EnvType.CLIENT)
public final class NeeRenderers {
    public static void registerBlockEntityRenderers() {
        BlockEntityRendererRegistryImpl.register(NeeBlockEntities.TRADE_STATION, TradeStationBlockEntityRenderer::new);
        BlockEntityRendererRegistryImpl.register(NeeBlockEntities.SYSTEM_STATION, SystemStationBlockEntityRenderer::new);
    }
}
