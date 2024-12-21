package com.iafenvoy.nee.screen.handler;

import com.iafenvoy.nee.registry.NeeBlocks;
import com.iafenvoy.nee.registry.NeeItems;
import com.iafenvoy.nee.registry.NeeScreenHandlers;
import com.iafenvoy.nee.screen.slot.ButtonLikeSlot;
import com.iafenvoy.nee.screen.slot.MoneyOnlySlot;
import com.iafenvoy.nee.screen.slot.SingleItemSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class TradeStationOwnerScreenHandler extends ScreenHandler {
    private final Inventory coins, goods, forSell;
    private final ScreenHandlerContext context;

    public TradeStationOwnerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(27), new SimpleInventory(1), ScreenHandlerContext.EMPTY);
    }

    public TradeStationOwnerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory coins, Inventory goods, ScreenHandlerContext context) {
        super(NeeScreenHandlers.TRADE_STATION_OWNER, syncId);
        checkSize(coins, 27);
        checkSize(goods, 1);
        this.coins = coins;
        this.goods = goods;
        this.forSell = new SimpleInventory(1);
        this.context = context;
        for (int i = 0; i < 2; ++i)
            for (int j = 0; j < 6; ++j)
                this.addSlot(new Slot(this.goods, j + i * 9, 8 + i * 18, 27 + i * 18));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new MoneyOnlySlot(this.coins, j + i * 9, 8 + j * 18, 72 + i * 18));
        this.addSlot(new SingleItemSlot(this.forSell, 0, 152, 45));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 198));
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        if (this.coins.size() <= slot && slot < this.coins.size() + 8) return itemStack;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot < this.coins.size()) {
                if (!this.insertItem(itemStack2, this.coins.size() + 8, this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!this.insertItem(itemStack2, 0, this.coins.size(), false))
                return ItemStack.EMPTY;
            if (itemStack2.isEmpty()) slot2.setStack(ItemStack.EMPTY);
            else slot2.markDirty();
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, NeeBlocks.TRADE_STATION);
    }
}
