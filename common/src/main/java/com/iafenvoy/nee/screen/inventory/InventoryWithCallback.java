package com.iafenvoy.nee.screen.inventory;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;

public class InventoryWithCallback extends SimpleInventory {
    private final ScreenHandler handler;

    public InventoryWithCallback(int size, ScreenHandler handler) {
        super(size);
        this.handler = handler;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.handler.onContentChanged(this);
    }
}
