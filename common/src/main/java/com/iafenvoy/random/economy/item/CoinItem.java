package com.iafenvoy.random.economy.item;

import com.iafenvoy.random.economy.util.ThingWithPrice;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class CoinItem extends Item implements ThingWithPrice {
    private static final List<CoinItem> COINS = new LinkedList<>();
    private final int value;

    public CoinItem(int value) {
        super(new Settings().rarity(Rarity.UNCOMMON));
        this.value = value;
        COINS.add(this);
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.random_economy.value", this.value));
    }

    public static int calculateValue(Inventory inv) {
        int value = 0;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.getItem() instanceof CoinItem coinItem)
                value += coinItem.getValue() * stack.getCount();
        }
        return value;
    }

    public static List<ItemStack> calculateCoins(int value) {
        List<ItemStack> list = new LinkedList<>();
        for (CoinItem coinItem : COINS.stream().sorted(Comparator.comparingInt(CoinItem::getValue).reversed()).toList()) {
            int cnt = value / coinItem.getValue();
            value -= cnt * coinItem.getValue();
            while (cnt > 64) {
                list.add(new ItemStack(coinItem, 64));
                cnt -= 64;
            }
            if (cnt > 0) list.add(new ItemStack(coinItem, cnt));
        }
        return list;
    }
}
