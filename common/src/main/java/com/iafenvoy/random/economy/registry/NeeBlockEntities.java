package com.iafenvoy.random.economy.registry;

import com.iafenvoy.random.economy.RandomEconomy;
import com.iafenvoy.random.economy.item.block.entity.SystemStationBlockEntity;
import com.iafenvoy.random.economy.item.block.entity.TradeStationBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public final class NeeBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(RandomEconomy.MOD_ID, RegistryKeys.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<TradeStationBlockEntity>> TRADE_STATION = register("trade_station", () -> BlockEntityType.Builder.create(TradeStationBlockEntity::new, NeeBlocks.TRADE_STATION.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<SystemStationBlockEntity>> SYSTEM_STATION = register("system_station", () -> BlockEntityType.Builder.create(SystemStationBlockEntity::new, NeeBlocks.TRADE_STATION.get()).build(null));

    public static <T extends BlockEntity> RegistrySupplier<BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> type) {
        return REGISTRY.register(id, type);
    }
}
