package com.iafenvoy.nee.screen.handler;

import com.iafenvoy.nee.registry.NeeBlocks;
import com.iafenvoy.nee.registry.NeeScreenHandlers;
import com.iafenvoy.nee.screen.inventory.InventoryWithCallback;
import com.iafenvoy.nee.screen.slot.MoneyOnlySlot;
import com.iafenvoy.nee.screen.slot.TakeOnlySlot;
import com.iafenvoy.nee.trade.CoinExchangeHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class ExchangeStationScreenHandler extends ScreenHandler {
    private final Inventory inputs, outputs;
    private final ScreenHandlerContext context;
    @Nullable
    private CoinExchangeHolder currentHolder = null;

    public ExchangeStationScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public ExchangeStationScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(NeeScreenHandlers.EXCHANGE_STATION.get(), syncId);
        this.inputs = new InventoryWithCallback(1, this);
        this.outputs = new SimpleInventory(2);
        this.context = context;
        this.addSlot(new MoneyOnlySlot(this.inputs, 0, 80, 20));
        this.addSlot(new TakeOnlySlot(this, this.outputs, 0, 22, 20, amount -> {
            if (this.currentHolder == null) return;
            int ratio = this.currentHolder.leftRatio();
            this.inputs.getStack(0).decrement(amount / ratio);
            ScreenHandlerUtils.playCoinsSound(this.context);
        }));
        this.addSlot(new TakeOnlySlot(this, this.outputs, 1, 138, 20, amount -> {
            if (this.currentHolder == null) return;
            int ratio = this.currentHolder.rightRatio();
            this.inputs.getStack(0).decrement(amount * ratio);
            ScreenHandlerUtils.playCoinsSound(this.context);
        }));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, i * 18 + 51));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        ItemStack in = this.inputs.getStack(0);
        this.currentHolder = CoinExchangeHolder.get(in.getItem());
        this.outputs.setStack(0, this.currentHolder == null ? ItemStack.EMPTY : this.currentHolder.parseLeft(in));
        this.outputs.setStack(1, this.currentHolder == null ? ItemStack.EMPTY : this.currentHolder.parseRight(in));
        super.onContentChanged(inventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot < 3) {
                if (!this.insertItem(itemStack2, 3, this.slots.size(), true))
                    return ItemStack.EMPTY;
                slot2.onTakeItem(player, itemStack);
            } else if (!this.insertItem(itemStack2, 0, 1, false))
                return ItemStack.EMPTY;
            if (itemStack2.isEmpty()) slot2.setStack(ItemStack.EMPTY);
            else slot2.markDirty();
            this.onContentChanged(this.inputs);
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, NeeBlocks.EXCHANGE_STATION.get());
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.dropInventory(player, this.inputs);
    }
}
