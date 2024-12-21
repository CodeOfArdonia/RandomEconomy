package com.iafenvoy.nee.item.block.entity;

import com.iafenvoy.nee.registry.NeeBlockEntities;
import com.iafenvoy.nee.screen.handler.TradeStationOwnerScreenHandler;
import com.iafenvoy.nee.screen.inventory.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

public class TradeStationBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    private static final String OWNER_KEY = "owner";
    @Nullable
    private UUID owner;
    private final DefaultedList<ItemStack> coins = DefaultedList.ofSize(27, ItemStack.EMPTY), goods = DefaultedList.ofSize(12, ItemStack.EMPTY), forSell = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public TradeStationBlockEntity(BlockPos pos, BlockState state) {
        super(NeeBlockEntities.TRADE_STATION, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains(OWNER_KEY, NbtElement.INT_ARRAY_TYPE)) this.setOwner(nbt.getUuid(OWNER_KEY));
        Inventories.readNbt(nbt.getCompound("coins"), this.coins);
        Inventories.readNbt(nbt.getCompound("goods"), this.goods);
        Inventories.readNbt(nbt.getCompound("forSell"), this.forSell);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.getOwner() != null) nbt.putUuid(OWNER_KEY, this.getOwner());
        nbt.put("coins", Inventories.writeNbt(new NbtCompound(), this.coins));
        nbt.put("goods", Inventories.writeNbt(new NbtCompound(), this.goods));
        nbt.put("forSell", Inventories.writeNbt(new NbtCompound(), this.forSell));
    }

    public @Nullable UUID getOwner() {
        return this.owner;
    }

    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.not_enough_economy.trade_station");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TradeStationOwnerScreenHandler(syncId, playerInventory, ImplementedInventory.of(this.coins), ImplementedInventory.of(this.goods), new ScreenHandlerContext() {
            @Override
            public <T> Optional<T> get(BiFunction<World, BlockPos, T> getter) {
                return Optional.of(getter.apply(TradeStationBlockEntity.this.world, TradeStationBlockEntity.this.pos));
            }
        });
    }
}
