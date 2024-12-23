package com.iafenvoy.nee.screen.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class DisplayOnlySlot extends Slot {
    public DisplayOnlySlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        this.setStack(ItemStack.EMPTY);
        return false;
    }

    @Override
    public ItemStack insertStack(ItemStack stack, int count) {
        this.setStack(stack.copy());
        return stack;
    }
}
