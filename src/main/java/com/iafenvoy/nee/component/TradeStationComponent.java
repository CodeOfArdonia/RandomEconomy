package com.iafenvoy.nee.component;

import com.iafenvoy.nee.NotEnoughEconomy;
import com.iafenvoy.nee.item.block.entity.TradeStationBlockEntity;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class TradeStationComponent implements ComponentV3, AutoSyncedComponent {
    public static final ComponentKey<TradeStationComponent> COMPONENT = ComponentRegistry.getOrCreate(Identifier.of(NotEnoughEconomy.MOD_ID, "trade_station"), TradeStationComponent.class);
    private final TradeStationBlockEntity blockEntity;

    public TradeStationComponent(TradeStationBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.blockEntity.readFromNbt(tag);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        this.blockEntity.writeToNbt(tag);
    }
}
