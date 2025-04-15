package com.iafenvoy.nee.screen.handler;

import com.iafenvoy.nee.registry.NeeScreenHandlers;
import com.iafenvoy.nee.screen.slot.DisplayOnlySlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class TradeCommandScreenHandler extends ScreenHandler {
    private final Inventory left, right;
    private final Text anotherPlayerName;

    public TradeCommandScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(20), new SimpleInventory(20), buf.readText());
    }

    public TradeCommandScreenHandler(int syncId, PlayerInventory playerInventory, Inventory left, Inventory right, Text anotherPlayerName) {
        super(NeeScreenHandlers.TRADE_COMMAND.get(), syncId);
        checkSize(left, 20);
        checkSize(right, 20);
        this.left = left;
        this.right = right;
        this.anotherPlayerName = anotherPlayerName;

        for (int j = 0; j < 5; ++j)
            for (int k = 0; k < 4; ++k)
                this.addSlot(new Slot(this.left, k + j * 4, 8 + k * 18, 18 + j * 18));
        for (int j = 0; j < 5; ++j)
            for (int k = 0; k < 4; ++k)
                this.addSlot(new DisplayOnlySlot(this.right, k + j * 4, 98 + k * 18, 18 + j * 18));
        for (int j = 0; j < 3; ++j)
            for (int k = 0; k < 9; ++k)
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 139 + j * 18));
        for (int j = 0; j < 9; ++j)
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 197));
    }

    public @Nullable Text getAnotherPlayerName() {
        return this.anotherPlayerName;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot < this.left.size()) {
                if (!this.insertItem(itemStack2, this.left.size() + this.right.size(), this.slots.size(), true))
                    return ItemStack.EMPTY;
                slot2.onTakeItem(player, itemStack);
            } else if (!this.insertItem(itemStack2, 0, this.left.size(), false))
                return ItemStack.EMPTY;
            if (itemStack2.isEmpty()) slot2.setStack(ItemStack.EMPTY);
            else slot2.markDirty();
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
