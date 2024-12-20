package com.iafenvoy.nee.registry;

import com.iafenvoy.nee.NotEnoughEconomy;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public final class NeeItemGroups {
    public static final List<Item> ITEMS = new LinkedList<>();
    public static final ItemGroup MAIN = register("main", FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.not_enough_economy.main"))
            .icon(() -> new ItemStack(NeeItems.CHEQUE))
            .entries((context, entries) -> ITEMS.forEach(entries::add))
            .build());

    public static <T extends ItemGroup> T register(String id, T group) {
        return Registry.register(Registries.ITEM_GROUP, Identifier.of(NotEnoughEconomy.MOD_ID, id), group);
    }

    public static void init() {
    }
}
