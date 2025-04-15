package com.iafenvoy.nee.registry;

import com.iafenvoy.nee.NotEnoughEconomy;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class NeeSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(NotEnoughEconomy.MOD_ID, RegistryKeys.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> CHECKED = register("checked");
    public static final RegistrySupplier<SoundEvent> COINS = register("coins");

    public static RegistrySupplier<SoundEvent> register(String id) {
        return REGISTRY.register(id, () -> SoundEvent.of(Identifier.of(NotEnoughEconomy.MOD_ID, id)));
    }
}
