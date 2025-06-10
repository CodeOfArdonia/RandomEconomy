package com.iafenvoy.random.economy.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryUtil {
    public static boolean hasAllItems(Inventory target, Inventory items) {
        target = copy(target);
        Map<StackHolder, Integer> itemsMap = new HashMap<>();
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.getStack(i);
            if (stack.isEmpty()) continue;
            StackHolder holder = new StackHolder(stack);
            itemsMap.put(holder, itemsMap.getOrDefault(holder, 0) + stack.getCount());
        }
        for (int i = 0; i < target.size(); i++) {
            ItemStack targetStack = target.getStack(i);
            for (Map.Entry<StackHolder, Integer> entry : itemsMap.entrySet()) {
                StackHolder stack = entry.getKey();
                int neededCount = entry.getValue();
                if (stack.areEqual(targetStack)) {
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
        Map<StackHolder, Integer> itemsMap = new HashMap<>();
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.getStack(i);
            if (stack.isEmpty()) continue;
            StackHolder holder = new StackHolder(stack);
            itemsMap.put(holder, itemsMap.getOrDefault(holder, 0) + stack.getCount());
        }
        for (int i = 0; i < target.size(); i++) {
            ItemStack targetStack = target.getStack(i);
            for (Map.Entry<StackHolder, Integer> entry : itemsMap.entrySet()) {
                StackHolder stack = entry.getKey();
                int neededCount = entry.getValue();
                if (stack.areEqual(targetStack)) {
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
        for (int count : itemsMap.values())
            if (count > 0)
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

    public record StackHolder(Item item, @Nullable NbtCompound nbt) {
        public StackHolder(ItemStack stack) {
            this(stack.getItem(), stack.getNbt());
        }

        public boolean areEqual(ItemStack stack) {
            return stack.isOf(this.item) && Objects.equals(stack.getNbt(), this.nbt);
        }
    }
}
