package com.iafenvoy.nee.registry;

import com.iafenvoy.nee.NotEnoughEconomy;
import com.iafenvoy.nee.item.block.entity.SystemStationBlockEntity;
import com.iafenvoy.nee.item.block.entity.TradeStationBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class NeeBlockEntities {
    public static final BlockEntityType<TradeStationBlockEntity> TRADE_STATION = register("trade_station", BlockEntityType.Builder.create(TradeStationBlockEntity::new, NeeBlocks.TRADE_STATION).build(null));
    public static final BlockEntityType<SystemStationBlockEntity> SYSTEM_STATION = register("system_station", BlockEntityType.Builder.create(SystemStationBlockEntity::new, NeeBlocks.TRADE_STATION).build(null));

    public static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(NotEnoughEconomy.MOD_ID, id), type);
    }

    public static void init() {
    }
}
