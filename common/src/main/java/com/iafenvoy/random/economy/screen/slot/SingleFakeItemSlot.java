package com.iafenvoy.random.economy.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;

public class SingleFakeItemSlot extends FakeItemSlot {
    public SingleFakeItemSlot(ScreenHandler handler, Inventory inventory, int index, int x, int y) {
        super(handler, inventory, index, x, y);
    }

    @Override
    public int getMaxItemCount() {
        return 1;
    }
}
