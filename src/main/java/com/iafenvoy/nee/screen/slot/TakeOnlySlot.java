package com.iafenvoy.nee.screen.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.function.IntConsumer;

public class TakeOnlySlot extends Slot {
    private final ScreenHandler handler;
    private final IntConsumer onTake;

    public TakeOnlySlot(ScreenHandler handler, Inventory inventory, int index, int x, int y, IntConsumer onTake) {
        super(inventory, index, x, y);
        this.handler = handler;
        this.onTake = onTake;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    protected void onTake(int amount) {
        this.onTake.accept(amount);
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        super.onTakeItem(player, stack);
        this.onTake(stack.getCount());
        this.handler.onContentChanged(this.inventory);
    }
}
