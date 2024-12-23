package com.iafenvoy.nee.component;

import com.iafenvoy.nee.item.block.entity.TradeStationBlockEntity;
import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;

public class ModComponentEntry implements BlockComponentInitializer {
    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(TradeStationBlockEntity.class, TradeStationComponent.COMPONENT, TradeStationComponent::new);
    }
}
