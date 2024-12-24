package com.iafenvoy.nee.util;

import net.minecraft.nbt.NbtCompound;

public interface SyncBlockEntity {
    void readFromNbt(NbtCompound nbt);

    void writeToNbt(NbtCompound nbt);
}
