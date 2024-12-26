package com.iafenvoy.nee.item.block.entity;

import com.iafenvoy.nee.component.TradeStationComponent;
import com.iafenvoy.nee.registry.NeeBlockEntities;
import com.iafenvoy.nee.screen.handler.SystemStationCustomerScreenHandler;
import com.iafenvoy.nee.screen.handler.SystemStationOwnerScreenHandler;
import com.iafenvoy.nee.screen.inventory.ImplementedInventory;
import com.iafenvoy.nee.util.SyncBlockEntity;
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
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SystemStationBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, SyncBlockEntity {
    private static final String OWNER_KEY = "owner";
    private static final String OWNER_NAME_KEY = "owner_name";
    @Nullable
    private UUID owner;
    @Nullable
    private String ownerNameCache;
    private final DefaultedList<ItemStack> left = DefaultedList.ofSize(12, ItemStack.EMPTY);
    private final DefaultedList<ItemStack> right = DefaultedList.ofSize(12, ItemStack.EMPTY);
    private final DefaultedList<ItemStack> display = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public SystemStationBlockEntity(BlockPos pos, BlockState state) {
        super(NeeBlockEntities.SYSTEM_STATION, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.readFromNbt(nbt);
        Inventories.readNbt(nbt.getCompound("left"), this.left);
        Inventories.readNbt(nbt.getCompound("right"), this.right);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        this.writeToNbt(nbt);
        nbt.put("left", Inventories.writeNbt(new NbtCompound(), this.left));
        nbt.put("right", Inventories.writeNbt(new NbtCompound(), this.right));
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

    @Override
    public Text getDisplayName() {
        return Text.translatable("screen.not_enough_economy.system_station");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        ScreenHandlerContext ctx = ScreenHandlerContext.create(this.world, this.pos);
        if (player.hasPermissionLevel(2) && player.isSneaking() && player.isCreative())
            return new SystemStationOwnerScreenHandler(syncId,
                    playerInventory,
                    ImplementedInventory.of(this.left, this::markDirty),
                    ImplementedInventory.of(this.right, () -> {
                        this.markDirty();
                        TradeStationComponent.COMPONENT.sync(this);
                    }),
                    ImplementedInventory.of(this.display, () -> {
                        this.markDirty();
                        TradeStationComponent.COMPONENT.sync(this);
                    }),
                    ctx);
        else return new SystemStationCustomerScreenHandler(syncId, playerInventory,
                ImplementedInventory.of(this.left),
                ImplementedInventory.of(this.right),
                ctx);
    }

    public ItemStack getDisplayStack() {
        return this.right.get(0);
    }
}
