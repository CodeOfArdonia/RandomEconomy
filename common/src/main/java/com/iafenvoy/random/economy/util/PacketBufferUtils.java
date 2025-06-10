package com.iafenvoy.random.economy.util;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;

public class PacketBufferUtils {
    public static PacketByteBuf create() {
        return new PacketByteBuf(Unpooled.buffer());
    }
}
