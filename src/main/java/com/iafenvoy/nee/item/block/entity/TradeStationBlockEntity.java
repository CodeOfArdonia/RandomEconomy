package com.iafenvoy.nee.item.block.entity;

import com.iafenvoy.nee.component.TradeStationComponent;
import com.iafenvoy.nee.registry.NeeBlockEntities;
import com.iafenvoy.nee.screen.handler.TradeStationCustomerScreenHandler;
import com.iafenvoy.nee.screen.handler.TradeStationOwnerScreenHandler;
import com.iafenvoy.nee.screen.inventory.ImplementedInventory;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

public class TradeStationBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    private static final String OWNER_KEY = "owner";
    private static final String OWNER_NAME_KEY = "owner_name";
    @Nullable
    private UUID owner;
    @Nullable
    private String ownerNameCache;
    private final DefaultedList<ItemStack> left = DefaultedList.ofSize(12, ItemStack.EMPTY);
    private final DefaultedList<ItemStack> right = DefaultedList.ofSize(12, ItemStack.EMPTY);
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(21, ItemStack.EMPTY);
    private final DefaultedList<ItemStack> display = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public TradeStationBlockEntity(BlockPos pos, BlockState state) {
        super(NeeBlockEntities.TRADE_STATION, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.readFromNbt(nbt);
        Inventories.readNbt(nbt.getCompound("left"), this.left);
        Inventories.readNbt(nbt.getCompound("right"), this.right);
        Inventories.readNbt(nbt.getCompound("inventory"), this.inventory);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        this.writeToNbt(nbt);
        nbt.put("left", Inventories.writeNbt(new NbtCompound(), this.left));
        nbt.put("right", Inventories.writeNbt(new NbtCompound(), this.right));
        nbt.put("inventory", Inventories.writeNbt(new NbtCompound(), this.inventory));
    }

    public void writeToNbt(NbtCompound nbt) {
        if (this.owner != null) nbt.putUuid(OWNER_KEY, this.owner);
        if (this.ownerNameCache != null) nbt.putString(OWNER_NAME_KEY, this.ownerNameCache);
        nbt.put("display", Inventories.writeNbt(new NbtCompound(), this.display));
    }

    public void readFromNbt(NbtCompound nbt) {
        if (nbt.contains(OWNER_KEY, NbtElement.INT_ARRAY_TYPE)) this.owner = nbt.getUuid(OWNER_KEY);
        if (nbt.contains(OWNER_NAME_KEY, NbtElement.STRING_TYPE)) this.ownerNameCache = nbt.getString(OWNER_NAME_KEY);
        this.display.clear();
        Inventories.readNbt(nbt.getCompound("display"), this.display);
    }

    public @Nullable UUID getOwner() {
        return this.owner;
    }

    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
        this.ownerNameCache = Optional.ofNullable(this.getWorld()).map(x -> x.getPlayerByUuid(owner)).map(PlayerEntity::getGameProfile).map(GameProfile::getName).orElse(null);
        this.markDirty();
        TradeStationComponent.COMPONENT.sync(this);
    }

    public Text getFloatingName() {
        String ownerName = this.getOwnerName();
        if (ownerName != null)
            return Text.translatable("screen.not_enough_economy.trade_station_float", ownerName);
        return Text.translatable("screen.not_enough_economy.trade_station_empty");
    }

    @Override
    public Text getDisplayName() {
        String ownerName = this.getOwnerName();
        if (ownerName != null)
            return Text.translatable("screen.not_enough_economy.trade_station", ownerName);
        return Text.translatable("screen.not_enough_economy.trade_station_empty");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        ScreenHandlerContext ctx = new ScreenHandlerContext() {
            @Override
            public <T> Optional<T> get(BiFunction<World, BlockPos, T> getter) {
                return Optional.of(getter.apply(TradeStationBlockEntity.this.world, TradeStationBlockEntity.this.pos));
            }
        };
        if (Objects.equals(this.getOwner(), player.getUuid())) return new TradeStationOwnerScreenHandler(syncId,
                playerInventory,
                ImplementedInventory.of(this.left, this::markDirty),
                ImplementedInventory.of(this.right, this::markDirty),
                ImplementedInventory.of(this.inventory, this::markDirty),
                ImplementedInventory.of(this.display, () -> {
                    this.markDirty();
                    TradeStationComponent.COMPONENT.sync(this);
                }),
                ctx);
        else return new TradeStationCustomerScreenHandler(syncId, playerInventory,
                ImplementedInventory.of(this.left),
                ImplementedInventory.of(this.right),
                ctx);
    }

    public ItemStack getDisplayStack() {
        return this.display.get(0);
    }

    @Nullable
    public String getOwnerName() {
        if (this.getOwner() == null) return this.ownerNameCache;
        return Optional.ofNullable(this.getWorld()).map(x -> x.getPlayerByUuid(this.getOwner())).map(PlayerEntity::getGameProfile).map(GameProfile::getName).orElse(this.ownerNameCache);
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
