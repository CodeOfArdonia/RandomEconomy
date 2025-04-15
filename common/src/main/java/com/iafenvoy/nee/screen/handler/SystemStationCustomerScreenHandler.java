package com.iafenvoy.nee.screen.handler;

import com.iafenvoy.nee.Constants;
import com.iafenvoy.nee.registry.NeeBlocks;
import com.iafenvoy.nee.registry.NeeScreenHandlers;
import com.iafenvoy.nee.screen.slot.DisplayOnlySlot;
import com.iafenvoy.nee.util.InventoryUtil;
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

public class SystemStationCustomerScreenHandler extends ScreenHandler {
    private final Inventory left, right;
    private final ScreenHandlerContext context;

    public SystemStationCustomerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(12), new SimpleInventory(12), ScreenHandlerContext.EMPTY);
    }

    public SystemStationCustomerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory left, Inventory right, ScreenHandlerContext context) {
        super(NeeScreenHandlers.SYSTEM_STATION_CUSTOMER.get(), syncId);
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
        return canUse(this.context, player, NeeBlocks.SYSTEM_STATION.get());
    }

    public void trade(PlayerEntity player) {
        PlayerInventory inventory = player.getInventory();
        if (!InventoryUtil.hasAllItems(inventory, this.left)) {
            player.sendMessage(Text.translatable("screen.not_enough_economy.failure.no_enough_money"));
            return;
        }
        InventoryUtil.removeItems(inventory, this.left);
        InventoryUtil.insertItems(inventory, this.right);
        ScreenHandlerUtils.playCheckedSound(this.context);
    }

    static {
        assert Constants.SYSTEM_TRADE != null;
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, Constants.SYSTEM_TRADE, (buf, ctx) -> {
            if (ctx.getPlayer().currentScreenHandler instanceof SystemStationCustomerScreenHandler h)
                ctx.queue(() -> h.trade(ctx.getPlayer()));
        });
    }
}
