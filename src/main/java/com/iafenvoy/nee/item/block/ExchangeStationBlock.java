package com.iafenvoy.nee.item.block;

import com.iafenvoy.nee.NotEnoughEconomy;
import com.iafenvoy.nee.screen.handler.ExchangeStationScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiFunction;

public class ExchangeStationBlock extends WorkStationBlock {
    public ExchangeStationBlock() {
        super(Settings.copy(Blocks.CRAFTING_TABLE));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) player.openHandledScreen(new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return Text.translatable("screen.%s.exchange_station".formatted(NotEnoughEconomy.MOD_ID));
            }

            @Override
            public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new ExchangeStationScreenHandler(syncId, playerInventory, new ScreenHandlerContext() {
                    @Override
                    public <T> Optional<T> get(BiFunction<World, BlockPos, T> getter) {
                        return Optional.of(getter.apply(world, pos));
                    }
                });
            }
        });
        return ActionResult.SUCCESS;
    }
}
