package com.iafenvoy.nee.screen.context;

import net.minecraft.entity.player.PlayerEntity;

public class PlayerContext extends SimpleContext {
    protected PlayerContext(PlayerEntity player) {
        super(player.getWorld(), player.getBlockPos());
    }

    public static PlayerContext of(PlayerEntity player) {
        return new PlayerContext(player);
    }
}
