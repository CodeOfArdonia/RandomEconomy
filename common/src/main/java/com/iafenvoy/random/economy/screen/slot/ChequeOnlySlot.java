package com.iafenvoy.random.economy.screen.slot;

import com.iafenvoy.random.economy.registry.NeeItems;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ChequeOnlySlot extends Slot {
    public ChequeOnlySlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(NeeItems.CHEQUE.get());
    }
}
