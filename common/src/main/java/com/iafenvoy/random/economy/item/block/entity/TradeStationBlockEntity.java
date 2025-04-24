package com.iafenvoy.random.economy.item.block.entity;

import com.iafenvoy.random.economy.registry.NeeBlockEntities;
import com.iafenvoy.random.economy.registry.NeeBlocks;
import com.iafenvoy.random.economy.screen.handler.TradeStationCustomerScreenHandler;
import com.iafenvoy.random.economy.screen.handler.TradeStationOwnerScreenHandler;
import com.iafenvoy.random.library.inventory.ImplementedInventory;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
        super(NeeBlockEntities.TRADE_STATION.get(), pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains(OWNER_KEY, NbtElement.INT_ARRAY_TYPE)) this.owner = nbt.getUuid(OWNER_KEY);
        if (nbt.contains(OWNER_NAME_KEY, NbtElement.STRING_TYPE)) this.ownerNameCache = nbt.getString(OWNER_NAME_KEY);
        this.display.clear();
        Inventories.readNbt(nbt.getCompound("display"), this.display);
        Inventories.readNbt(nbt.getCompound("left"), this.left);
        Inventories.readNbt(nbt.getCompound("right"), this.right);
        Inventories.readNbt(nbt.getCompound("inventory"), this.inventory);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.owner != null) nbt.putUuid(OWNER_KEY, this.owner);
        if (this.ownerNameCache != null) nbt.putString(OWNER_NAME_KEY, this.ownerNameCache);
        nbt.put("display", Inventories.writeNbt(new NbtCompound(), this.display));
        nbt.put("left", Inventories.writeNbt(new NbtCompound(), this.left));
        nbt.put("right", Inventories.writeNbt(new NbtCompound(), this.right));
        nbt.put("inventory", Inventories.writeNbt(new NbtCompound(), this.inventory));
    }

    public @Nullable UUID getOwner() {
        return this.owner;
    }

    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
        this.ownerNameCache = Optional.ofNullable(this.getWorld()).map(x -> x.getPlayerByUuid(owner)).map(PlayerEntity::getGameProfile).map(GameProfile::getName).orElse(null);
        this.markDirty();
        if (this.world != null)
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 0);
    }

    public Text getFloatingName() {
        String ownerName = this.getOwnerName();
        if (ownerName != null)
            return Text.translatable("screen.random_economy.trade_station_float", ownerName);
        return Text.translatable("screen.random_economy.trade_station_empty");
    }

    @Override
    public Text getDisplayName() {
        String ownerName = this.getOwnerName();
        if (ownerName != null)
            return Text.translatable("screen.random_economy.trade_station", ownerName);
        return Text.translatable("screen.random_economy.trade_station_empty");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        ScreenHandlerContext ctx = ScreenHandlerContext.create(this.world, this.pos);
        if (Objects.equals(this.getOwner(), player.getUuid())) return new TradeStationOwnerScreenHandler(syncId,
                playerInventory,
                ImplementedInventory.of(this.left, this::markDirty),
                ImplementedInventory.of(this.right, this::markDirty),
                ImplementedInventory.of(this.inventory, this::markDirty),
                ImplementedInventory.of(this.display, () -> {
                    this.markDirty();
                    if (this.world != null)
                        this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), 0);
                }),
                ctx);
        else return new TradeStationCustomerScreenHandler(syncId, playerInventory,
                ImplementedInventory.of(this.left),
                ImplementedInventory.of(this.right),
                ImplementedInventory.of(this.inventory, this::markDirty),
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

    public void dropInventory() {
        assert this.world != null;
        for (ItemStack stack : this.inventory) {
            ItemEntity itemEntity = new ItemEntity(this.world, (double) this.pos.getX() + 0.5, (double) this.pos.getY() + 0.5, (double) this.pos.getZ() + 0.5, stack);
            itemEntity.setToDefaultPickupDelay();
            this.world.spawnEntity(itemEntity);
        }
    }

    public void dropFullBlock() {
        assert this.world != null;
        ItemStack stack = new ItemStack(NeeBlocks.TRADE_STATION.get());
        NbtCompound compound = new NbtCompound();
        this.writeNbt(compound);
        BlockItem.setBlockEntityNbt(stack, NeeBlockEntities.TRADE_STATION.get(), compound);
        ItemEntity itemEntity = new ItemEntity(this.world, (double) this.pos.getX() + 0.5, (double) this.pos.getY() + 0.5, (double) this.pos.getZ() + 0.5, stack);
        itemEntity.setToDefaultPickupDelay();
        this.world.spawnEntity(itemEntity);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }
}
