package com.iafenvoy.random.economy.screen;

import com.iafenvoy.random.economy.registry.NeeSounds;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.sound.SoundCategory;

public class ScreenHandlerUtils {
    public static void playCheckedSound(ScreenHandlerContext context) {
        context.run((world, pos) -> world.playSound(null, pos, NeeSounds.CHECKED.get(), SoundCategory.BLOCKS));
    }

    public static void playCoinsSound(ScreenHandlerContext context) {
        context.run((world, pos) -> world.playSound(null, pos, NeeSounds.COINS.get(), SoundCategory.BLOCKS));
    }
}
