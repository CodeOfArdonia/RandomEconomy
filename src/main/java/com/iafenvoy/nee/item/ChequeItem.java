package com.iafenvoy.nee.item;

import com.iafenvoy.nee.registry.NeeItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChequeItem extends Item {
    private static final String VALUE_KEY = "cheque_value";
    private static final String OWNER_KEY = "cheque_owner";

    public ChequeItem() {
        super(new Item.Settings().rarity(Rarity.RARE));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        int value = getValue(stack);
        String owner = getOwner(stack);
        if (value > 0) tooltip.add(Text.translatable("item.not_enough_economy.value", value));
        if (owner != null) tooltip.add(Text.translatable("item.not_enough_economy.owner", owner));
    }

    public static int getValue(ItemStack stack) {
        if (stack.getNbt() == null || !stack.getNbt().contains(VALUE_KEY, NbtElement.INT_TYPE)) return 0;
        return stack.getNbt().getInt(VALUE_KEY);
    }

    @Nullable
    public static String getOwner(ItemStack stack) {
        if (stack.getNbt() == null || !stack.getNbt().contains(OWNER_KEY, NbtElement.STRING_TYPE)) return null;
        return stack.getNbt().getString(OWNER_KEY);
    }

    public static ItemStack create(int value, String owner) {
        ItemStack stack = new ItemStack(NeeItems.CHEQUE);
        if (value > 0) stack.getOrCreateNbt().putInt(VALUE_KEY, value);
        if (owner != null) stack.getOrCreateNbt().putString(OWNER_KEY, owner);
        return stack;
    }
}
