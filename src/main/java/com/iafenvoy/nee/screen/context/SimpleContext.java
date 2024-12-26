package com.iafenvoy.nee.screen.context;

import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.BiFunction;

public class SimpleContext implements ScreenHandlerContext {
    private final World world;
    private final BlockPos pos;

    protected SimpleContext(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public static SimpleContext of(World world, BlockPos pos) {
        return new SimpleContext(world, pos);
    }

    @Override
    public <T> Optional<T> get(BiFunction<World, BlockPos, T> getter) {
        return Optional.ofNullable(getter.apply(this.world, this.pos));
    }
}
