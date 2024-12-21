package com.iafenvoy.nee.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

public class SingleItemSlot extends Slot {
    public SingleItemSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }
}
