package com.iafenvoy.nee.item;

import com.iafenvoy.nee.util.ThingWithPrice;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoinItem extends Item implements ThingWithPrice {
    private final int value;

    public CoinItem(int value) {
        super(new Settings().rarity(Rarity.UNCOMMON));
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.not_enough_economy.value", this.value));
    }
}
