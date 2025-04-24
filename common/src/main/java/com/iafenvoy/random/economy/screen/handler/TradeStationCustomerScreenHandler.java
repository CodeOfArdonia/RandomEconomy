package com.iafenvoy.random.economy.screen.handler;

import com.iafenvoy.random.economy.Constants;
import com.iafenvoy.random.economy.registry.NeeBlocks;
import com.iafenvoy.random.economy.registry.NeeScreenHandlers;
import com.iafenvoy.random.library.inventory.InventoryUtil;
import com.iafenvoy.random.library.inventory.slot.DisplayOnlySlot;
import dev.architectury.networking.NetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class TradeStationCustomerScreenHandler extends ScreenHandler {
    private final Inventory left, right;
    private final ScreenHandlerContext context;
    private final Inventory inventory;

    public TradeStationCustomerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(12), new SimpleInventory(12), new SimpleInventory(1), ScreenHandlerContext.EMPTY);
    }

    public TradeStationCustomerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory left, Inventory right, Inventory inventory, ScreenHandlerContext context) {
        super(NeeScreenHandlers.TRADE_STATION_CUSTOMER.get(), syncId);
        checkSize(left, 12);
        checkSize(right, 12);
        this.left = left;
        this.right = right;
        this.inventory = inventory;
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
        return canUse(this.context, player, NeeBlocks.TRADE_STATION.get());
    }

    public void trade(PlayerEntity player) {
        PlayerInventory inventory = player.getInventory();
        if (!InventoryUtil.hasAllItems(this.inventory, this.right)) {
            player.sendMessage(Text.translatable("screen.random_economy.failure.no_enough_goods"));
            return;
        }
        if (!InventoryUtil.hasAllItems(inventory, this.left)) {
            player.sendMessage(Text.translatable("screen.random_economy.failure.no_enough_money"));
            return;
        }
        if (!InventoryUtil.canFitItems(this.inventory, this.left, this.right)) {
            player.sendMessage(Text.translatable("screen.random_economy.failure.no_enough_space"));
            return;
        }
        InventoryUtil.removeItems(inventory, this.left);
        InventoryUtil.removeItems(this.inventory, this.right);
        InventoryUtil.insertItems(inventory, this.right);
        InventoryUtil.insertItems(this.inventory, this.left);
        ScreenHandlerUtils.playCheckedSound(this.context);
    }

    static {
        assert Constants.TRADE != null;
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, Constants.TRADE, (buf, ctx) -> {
            if (ctx.getPlayer().currentScreenHandler instanceof TradeStationCustomerScreenHandler h)
                ctx.queue(() -> h.trade(ctx.getPlayer()));
        });
    }
}
