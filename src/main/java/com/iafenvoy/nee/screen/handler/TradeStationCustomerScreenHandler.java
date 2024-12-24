package com.iafenvoy.nee.screen.handler;

import com.iafenvoy.nee.Constants;
import com.iafenvoy.nee.registry.NeeBlocks;
import com.iafenvoy.nee.registry.NeeScreenHandlers;
import com.iafenvoy.nee.screen.slot.DisplayOnlySlot;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class TradeStationCustomerScreenHandler extends ScreenHandler {
    private final Inventory left, right;
    private final ScreenHandlerContext context;

    public TradeStationCustomerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(12), new SimpleInventory(12), ScreenHandlerContext.EMPTY);
    }

    public TradeStationCustomerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory left, Inventory right, ScreenHandlerContext context) {
        super(NeeScreenHandlers.TRADE_STATION_CUSTOMER, syncId);
        checkSize(left, 12);
        checkSize(right, 12);
        this.left = left;
        this.right = right;
        this.context = context;
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 4; ++j)
                this.addSlot(new DisplayOnlySlot(this.left, j + i * 4, 8 + j * 18, 18 + i * 18));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 4; ++j)
                this.addSlot(new DisplayOnlySlot(this.right, j + i * 4, 98 + j * 18, 18 + i * 18));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, NeeBlocks.TRADE_STATION);
    }

    public void trade(PlayerEntity player){

    }

    static {
        assert Constants.TRADE != null;
        ServerPlayNetworking.registerGlobalReceiver(Constants.TRADE, (server, player, handler, buf, sender) -> {
            if (player.currentScreenHandler instanceof TradeStationCustomerScreenHandler h)
                server.execute(() -> h.trade(player));
        });
    }
}
