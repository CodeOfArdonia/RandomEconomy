package com.iafenvoy.random.economy.screen.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface ImplementedInventory extends Inventory {
    static ImplementedInventory of(DefaultedList<ItemStack> items) {
        return () -> items;
    }

    static ImplementedInventory of(DefaultedList<ItemStack> items, Runnable onDirty) {
        return new ImplementedInventory() {
            @Override
            public DefaultedList<ItemStack> getItems() {
                return items;
            }

            @Override
            public void markDirty() {
                ImplementedInventory.super.markDirty();
                onDirty.run();
            }
        };
    }

    static ImplementedInventory ofSize(int size) {
        return of(DefaultedList.ofSize(size, ItemStack.EMPTY));
    }

    DefaultedList<ItemStack> getItems();

    @Override
    default int size() {
        return this.getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (int i = 0; i < this.size(); i++) {
            ItemStack stack = this.getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default ItemStack getStack(int slot) {
        return this.getItems().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(this.getItems(), slot, count);
        if (!result.isEmpty()) {
            this.markDirty();
        }
        return result;
    }

    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.getItems(), slot);
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        this.getItems().set(slot, stack);
        if (stack.getCount() > stack.getMaxCount()) {
            stack.setCount(stack.getMaxCount());
        }
    }

    @Override
    default void clear() {
        this.getItems().clear();
    }

    @Override
    default void markDirty() {
        // Override if you want behavior.
    }

    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}