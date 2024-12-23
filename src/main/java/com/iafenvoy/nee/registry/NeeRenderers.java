package com.iafenvoy.nee.registry;

import com.iafenvoy.nee.render.TradeStationBlockEntityRenderer;
import net.fabricmc.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl;

public final class NeeRenderers {
    public static void registerBlockEntityRenderers(){
        BlockEntityRendererRegistryImpl.register(NeeBlockEntities.TRADE_STATION, TradeStationBlockEntityRenderer::new);
    }
}
