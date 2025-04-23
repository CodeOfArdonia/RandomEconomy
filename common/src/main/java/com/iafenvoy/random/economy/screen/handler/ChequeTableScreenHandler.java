package com.iafenvoy.random.economy.screen.handler;

import com.iafenvoy.random.economy.Constants;
import com.iafenvoy.random.economy.item.ChequeItem;
import com.iafenvoy.random.economy.item.CoinItem;
import com.iafenvoy.random.economy.registry.NeeBlocks;
import com.iafenvoy.random.economy.registry.NeeItems;
import com.iafenvoy.random.economy.registry.NeeScreenHandlers;
import com.iafenvoy.random.economy.screen.slot.ChequeOnlySlot;
import com.iafenvoy.random.economy.screen.slot.MoneyOnlySlot;
import com.iafenvoy.random.economy.screen.slot.TakeOnlySlot;
import dev.architectury.networking.NetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

import java.util.List;

public class ChequeTableScreenHandler extends ScreenHandler {
    private final Inventory coins, chequesInput, chequesOutput;
    private final ScreenHandlerContext context;

    public ChequeTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public ChequeTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(NeeScreenHandlers.CHEQUE_TABLE.get(), syncId);
        this.coins = new SimpleInventory(15);
        this.chequesInput = new SimpleInventory(1);
        this.chequesOutput = new SimpleInventory(1);
        this.context = context;
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 5; ++j)
                this.addSlot(new MoneyOnlySlot(this.coins, j + i * 5, 8 + j * 18, 18 + i * 18));
        this.addSlot(new ChequeOnlySlot(this.chequesInput, 0, 134, 18));
        this.addSlot(new TakeOnlySlot(this, this.chequesOutput, 0, 134, 54, amount -> {
        }));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot < this.coins.size() + 2) {
                if (!this.insertItem(itemStack2, this.coins.size() + 2, this.slots.size(), true))
                    return ItemStack.EMPTY;
                slot2.onTakeItem(player, itemStack);
            } else if (!this.insertItem(itemStack2, 0, this.coins.size() + 1, false))
                return ItemStack.EMPTY;
            if (itemStack2.isEmpty()) slot2.setStack(ItemStack.EMPTY);
            else slot2.markDirty();
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, NeeBlocks.CHEQUE_TABLE.get());
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.dropInventory(player, this.coins);
        this.dropInventory(player, this.chequesInput);
    }

    public void checkIn(PlayerEntity player) {
        ItemStack emptyCheque = this.chequesInput.getStack(0);
        if (!emptyCheque.isOf(NeeItems.CHEQUE.get()) || emptyCheque.getNbt() != null) return;
        this.chequesOutput.setStack(0, ChequeItem.create(CoinItem.calculateValue(this.coins), player.getGameProfile().getName()));
        emptyCheque.decrement(1);
        this.coins.clear();
        ScreenHandlerUtils.playCheckedSound(this.context);
    }

    public void checkOut(PlayerEntity player) {
        ItemStack cheque = this.chequesInput.getStack(0);
        if (!cheque.isOf(NeeItems.CHEQUE.get()) || cheque.getNbt() == null) return;
        int value = ChequeItem.getValue(cheque);
        for (int i = 0; i < this.coins.size(); i++) {
            ItemStack stack = this.coins.getStack(i);
            while (stack.getItem() instanceof CoinItem coinItem && value >= coinItem.getValue() && stack.getCount() < stack.getMaxCount()) {
                stack.increment(1);
                value -= coinItem.getValue();
            }
        }
        int j = 0;
        List<ItemStack> remains = CoinItem.calculateCoins(value);
        for (int i = 0; i < this.coins.size() && j < remains.size(); i++)
            if (this.coins.getStack(i).isEmpty()) {
                this.coins.setStack(i, remains.get(j));
                j++;
            }
        for (; j < remains.size(); j++) player.getInventory().offerOrDrop(remains.get(j));
        cheque.decrement(1);
        ScreenHandlerUtils.playCheckedSound(this.context);
    }

    static {
        assert Constants.CHEQUE_CHECK_IN != null;
        assert Constants.CHEQUE_CHECK_OUT != null;
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, Constants.CHEQUE_CHECK_IN, (buf, ctx) -> {
            if (ctx.getPlayer().currentScreenHandler instanceof ChequeTableScreenHandler h)
                ctx.queue(() -> h.checkIn(ctx.getPlayer()));
        });
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, Constants.CHEQUE_CHECK_OUT, (buf, ctx) -> {
            if (ctx.getPlayer().currentScreenHandler instanceof ChequeTableScreenHandler h)
                ctx.queue(() -> h.checkOut(ctx.getPlayer()));
        });
    }
}
