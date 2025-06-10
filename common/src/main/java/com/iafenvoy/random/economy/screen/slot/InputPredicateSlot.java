package com.iafenvoy.random.economy.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.function.Predicate;

public class InputPredicateSlot extends Slot {
    private final Predicate<ItemStack> insertPredicate;

    public InputPredicateSlot(Inventory inventory, int index, int x, int y, Predicate<ItemStack> insertPredicate) {
        super(inventory, index, x, y);
        this.insertPredicate = insertPredicate;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return this.insertPredicate.test(stack);
    }
}
