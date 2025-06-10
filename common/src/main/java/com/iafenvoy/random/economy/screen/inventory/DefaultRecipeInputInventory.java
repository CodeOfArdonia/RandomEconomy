package com.iafenvoy.random.economy.screen.inventory;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public interface DefaultRecipeInputInventory extends ImplementedInventory, RecipeInputInventory {
    static DefaultRecipeInputInventory of(DefaultedList<ItemStack> items) {
        return () -> items;
    }

    static DefaultRecipeInputInventory ofSize(int size) {
        return of(DefaultedList.ofSize(size, ItemStack.EMPTY));
    }

    default int getWidth() {
        return 3;
    }

    default int getHeight() {
        return 3;
    }

    default List<ItemStack> getInputStacks() {
        return this.getItems();
    }

    default void provideRecipeInputs(RecipeMatcher finder) {
        for (ItemStack itemStack : this.getItems())
            finder.addUnenchantedInput(itemStack);
    }
}
