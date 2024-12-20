package com.iafenvoy.nee.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public record ExchangeHolder(Item leftItem, int leftRatio, Item rightItem, int rightRatio) {
    private static final Map<Item, ExchangeHolder> HOLDERS = new LinkedHashMap<>();

    public static void register(Item item, ExchangeHolder holder) {
        HOLDERS.put(item, holder);
    }

    @Nullable
    public static ExchangeHolder get(Item item) {
        return HOLDERS.get(item);
    }

    public ItemStack parseLeft(ItemStack source) {
        return new ItemStack(this.leftItem, this.leftRatio);
    }

    public ItemStack parseRight(ItemStack source) {
        if (source.getCount() < this.rightRatio) return ItemStack.EMPTY;
        return new ItemStack(this.rightItem);
    }
}
