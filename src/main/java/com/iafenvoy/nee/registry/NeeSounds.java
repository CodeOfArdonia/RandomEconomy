package com.iafenvoy.nee.registry;

import com.iafenvoy.nee.NotEnoughEconomy;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class NeeSounds {
    public static final SoundEvent CHECKED = register("checked");
    public static final SoundEvent COINS = register("coins");

    public static SoundEvent register(String id) {
        Identifier i = Identifier.of(NotEnoughEconomy.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, i, SoundEvent.of(i));
    }

    public static void init() {
    }
}
