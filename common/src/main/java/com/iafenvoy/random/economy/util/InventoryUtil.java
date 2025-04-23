package com.iafenvoy.random.economy.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryUtil {
    public static boolean hasAllItems(Inventory target, Inventory items) {
        target = copy(target);
        Map<ItemStack, Integer> itemsMap = new HashMap<>();
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.getStack(i).copy();
            itemsMap.put(stack, itemsMap.getOrDefault(stack, 0) + stack.getCount());
        }
        for (int i = 0; i < target.size(); i++) {
            ItemStack targetStack = target.getStack(i);
            for (Map.Entry<ItemStack, Integer> entry : itemsMap.entrySet()) {
                ItemStack stack = entry.getKey();
                int neededCount = entry.getValue();
                if (ItemStack.canCombine(stack, targetStack)) {
                    int count = targetStack.getCount();
                    if (count >= neededCount)
                        itemsMap.put(stack, 0);
                    else
                        itemsMap.put(stack, neededCount - count);
                    break;
                }
            }
        }
        for (int count : itemsMap.values())
            if (count > 0)
                return false;
        return true;
    }

    public static boolean removeItems(Inventory target, Inventory items) {
        Map<ItemStack, Integer> itemsMap = new HashMap<>();
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.getStack(i);
            itemsMap.put(stack.copyWithCount(1), itemsMap.getOrDefault(stack, 0) + stack.getCount());
        }
        for (int i = 0; i < target.size(); i++) {
            ItemStack targetStack = target.getStack(i);
            for (Map.Entry<ItemStack, Integer> entry : itemsMap.entrySet()) {
                ItemStack stack = entry.getKey();
                int neededCount = entry.getValue();
                if (ItemStack.canCombine(stack, targetStack)) {
                    int count = targetStack.getCount();
                    if (count >= neededCount) {
                        targetStack.decrement(neededCount);
                        itemsMap.put(stack, 0);
                        break;
                    } else {
                        itemsMap.put(stack, neededCount - count);
                        targetStack.setCount(0);
                    }
                }
            }
        }

        for (Map.Entry<ItemStack, Integer> entry : itemsMap.entrySet())
            if (entry.getValue() > 0)
                return false;
        return true;
    }

    public static boolean canFitItems(Inventory inventory, Inventory in, Inventory out) {
        inventory = copy(inventory);
        return removeItems(inventory, out) && insertItems(inventory, in);
    }

    public static boolean insertItems(Inventory inventory, Inventory insert) {
        for (int i = 0; i < insert.size(); i++) {
            ItemStack insertStack = insert.getStack(i);
            if (insertStack != null && !tryAddItemToInventory(inventory, insertStack.copy()))
                return false;
        }
        return true;
    }

    private static boolean tryAddItemToInventory(Inventory inventory, ItemStack stack) {
        if (stack.isEmpty()) return true;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack inventoryStack = inventory.getStack(i);
            if (inventoryStack == null || ItemStack.canCombine(inventoryStack, stack)) {
                if (stack.getMaxCount() - (inventoryStack != null ? inventoryStack.getCount() : 0) > 0) {
                    int countToAdd = Math.min(stack.getCount(), stack.getMaxCount() - (inventoryStack != null ? inventoryStack.getCount() : 0));
                    if (inventoryStack == null) inventory.setStack(i, stack.copy());
                    else inventoryStack.increment(countToAdd);
                    stack.decrement(countToAdd);
                    if (stack.getCount() == 0)
                        return true;
                }
            }
        }
        if (inventory instanceof PlayerInventory playerInventory) {
            playerInventory.offerOrDrop(stack);
            return true;
        } else for (int i = 0; i < inventory.size(); i++)
            if (inventory.getStack(i).isEmpty()) {
                inventory.setStack(i, stack);
                return true;
            }
        return false;
    }

    public static Inventory copy(Inventory another) {
        Inventory inventory = new SimpleInventory(another.size());
        for (int i = 0; i < another.size(); i++)
            inventory.setStack(i, another.getStack(i).copy());
        return inventory;
    }
}
