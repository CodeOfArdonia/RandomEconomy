package com.iafenvoy.nee.screen.handler;

import com.iafenvoy.nee.registry.NeeSounds;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.sound.SoundCategory;

public class ScreenHandlerUtils {
    public static void playCheckedSound(ScreenHandlerContext context) {
        context.run((world, pos) -> world.playSound(null, pos, NeeSounds.CHECKED, SoundCategory.BLOCKS));
    }

    public static void playCoinsSound(ScreenHandlerContext context) {
        context.run((world, pos) -> world.playSound(null, pos, NeeSounds.COINS, SoundCategory.BLOCKS));
    }
}
