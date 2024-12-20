package com.iafenvoy.nee.screen.handler;

import com.iafenvoy.nee.registry.NeeBlocks;
import com.iafenvoy.nee.registry.NeeScreenHandlers;
import com.iafenvoy.nee.screen.inventory.InventoryWithCallback;
import com.iafenvoy.nee.screen.inventory.MoneyOnlySlot;
import com.iafenvoy.nee.screen.inventory.TakeOnlySlot;
import com.iafenvoy.nee.util.ExchangeHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class ExchangeStationScreenHandler extends ScreenHandler {
    private final Inventory inputInv, outputInv;
    private final ScreenHandlerContext context;
    private final Slot input, leftOutput, rightOutput;
    @Nullable
    private ExchangeHolder currentHolder = null;

    public ExchangeStationScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public ExchangeStationScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(NeeScreenHandlers.EXCHANGE_STATION, syncId);
        this.inputInv = new InventoryWithCallback(1, this);
        this.outputInv = new SimpleInventory(2);
        this.context = context;
        this.input = this.addSlot(new MoneyOnlySlot(this.inputInv, 0, 80, 20));
        this.leftOutput = this.addSlot(new TakeOnlySlot(this, this.outputInv, 0, 22, 20, amount -> {
            if (this.currentHolder == null) return;
            int ratio = this.currentHolder.leftRatio();
            this.input.getStack().decrement(amount / ratio);
            NeeHandlerUtils.playCheckedSound(this.context);
        }));
        this.rightOutput = this.addSlot(new TakeOnlySlot(this, this.outputInv, 1, 138, 20, amount -> {
            if (this.currentHolder == null) return;
            int ratio = this.currentHolder.rightRatio();
            this.input.getStack().decrement(amount * ratio);
            NeeHandlerUtils.playCheckedSound(this.context);
        }));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, i * 18 + 51));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        ItemStack in = this.input.getStack();
        this.currentHolder = ExchangeHolder.get(in.getItem());
        this.leftOutput.setStackNoCallbacks(this.currentHolder == null ? ItemStack.EMPTY : this.currentHolder.parseLeft(in));
        this.rightOutput.setStackNoCallbacks(this.currentHolder == null ? ItemStack.EMPTY : this.currentHolder.parseRight(in));
        super.onContentChanged(inventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot < this.inputInv.size() + this.outputInv.size()) {
                if (!this.insertItem(itemStack2, this.inputInv.size() + this.outputInv.size(), this.slots.size(), true))
                    return ItemStack.EMPTY;
                slot2.onTakeItem(player, itemStack);
            } else if (!this.insertItem(itemStack2, 0, this.inputInv.size(), false))
                return ItemStack.EMPTY;
            if (itemStack2.isEmpty()) slot2.setStack(ItemStack.EMPTY);
            else slot2.markDirty();
            this.onContentChanged(this.inputInv);
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, NeeBlocks.EXCHANGE_STATION);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        player.giveItemStack(this.inputInv.getStack(0));
    }
}
