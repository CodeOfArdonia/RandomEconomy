package com.iafenvoy.nee.screen.slot;

import com.iafenvoy.nee.util.ThingWithPrice;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class MoneyOnlySlot extends Slot {
    public MoneyOnlySlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof ThingWithPrice;
    }
}
