package com.iafenvoy.random.economy.screen.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class FakeItemSlot extends Slot {
    private final ScreenHandler handler;

    public FakeItemSlot(ScreenHandler handler, Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.handler = handler;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        this.setStack(ItemStack.EMPTY);
        this.handler.onContentChanged(this.inventory);
        return false;
    }

    @Override
    public ItemStack insertStack(ItemStack stack, int count) {
        this.setStack(stack.copyWithCount(1));
        this.handler.onContentChanged(this.inventory);
        return stack;
    }
}
