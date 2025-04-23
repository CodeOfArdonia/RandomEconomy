package com.iafenvoy.random.economy.registry;

import com.iafenvoy.random.economy.RandomEconomy;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class NeeItemGroups {
    public static final DeferredRegister<ItemGroup> REGISTRY = DeferredRegister.create(RandomEconomy.MOD_ID, RegistryKeys.ITEM_GROUP);

    public static final List<Supplier<Item>> ITEMS = new LinkedList<>();
    public static final RegistrySupplier<ItemGroup> MAIN = register("main", () -> CreativeTabRegistry.create(builder -> builder
            .displayName(Text.translatable("itemGroup.random_economy.main"))
            .icon(() -> new ItemStack(NeeItems.CHEQUE.get()))
            .entries((context, entries) -> ITEMS.stream().map(Supplier::get).forEach(entries::add))));

    public static <T extends ItemGroup> RegistrySupplier<T> register(String id, Supplier<T> group) {
        return REGISTRY.register(id, group);
    }
}
