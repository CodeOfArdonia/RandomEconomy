package com.iafenvoy.nee.screen.handler;

import com.iafenvoy.nee.registry.NeeBlocks;
import com.iafenvoy.nee.registry.NeeScreenHandlers;
import com.iafenvoy.nee.screen.slot.FakeItemSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class TradeStationOwnerScreenHandler extends ScreenHandler {
    private final Inventory left, right, inventory;
    private final ScreenHandlerContext context;

    public TradeStationOwnerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(12), new SimpleInventory(12), new SimpleInventory(21), new SimpleInventory(1), ScreenHandlerContext.EMPTY);
    }

    public TradeStationOwnerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory left, Inventory right, Inventory inventory, Inventory display, ScreenHandlerContext context) {
        super(NeeScreenHandlers.TRADE_STATION_OWNER.get(), syncId);
        checkSize(left, 12);
        checkSize(right, 12);
        checkSize(inventory, 21);
        checkSize(display, 1);
        this.left = left;
        this.right = right;
        this.inventory = inventory;
        this.context = context;

        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 4; ++j)
                this.addSlot(new FakeItemSlot(this.left, j + i * 4, 8 + j * 18, 16 + i * 18));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 4; ++j)
                this.addSlot(new FakeItemSlot(this.right, j + i * 4, 98 + j * 18, 16 + i * 18));
        this.addSlot(new FakeItemSlot(display, 0, 152, 72));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 7; ++j)
                this.addSlot(new Slot(this.inventory, j + i * 7, 8 + j * 18, 72 + i * 18));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 198));
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            int fake = this.left.size() + this.right.size() + 1;
            if (fake <= slot && slot < fake + this.inventory.size()) {
                if (!this.insertItem(itemStack2, fake + this.inventory.size(), this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!this.insertItem(itemStack2, fake, fake + this.inventory.size(), false))
                return ItemStack.EMPTY;
            if (itemStack2.isEmpty()) slot2.setStack(ItemStack.EMPTY);
            else slot2.markDirty();
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, NeeBlocks.TRADE_STATION.get());
    }
}
