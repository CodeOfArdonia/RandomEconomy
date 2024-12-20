package com.iafenvoy.nee.screen.handler;

import com.iafenvoy.nee.registry.NeeBlocks;
import com.iafenvoy.nee.registry.NeeScreenHandlers;
import com.iafenvoy.nee.screen.inventory.ChequeOnlySlot;
import com.iafenvoy.nee.screen.inventory.MoneyOnlySlot;
import com.iafenvoy.nee.screen.inventory.TakeOnlySlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class ChequeTableScreenHandler extends ScreenHandler {
    private final Inventory coinInv, chequeInputInv, chequeOutputInv;
    private final ScreenHandlerContext context;
    private final PropertyDelegate delegate;

    public ChequeTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public ChequeTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(NeeScreenHandlers.CHEQUE_TABLE, syncId);
        this.coinInv = new SimpleInventory(15);
        this.chequeInputInv = new SimpleInventory(1);
        this.chequeOutputInv = new SimpleInventory(1);
        this.context = context;
        this.delegate = new PropertyDelegate() {
            private int in, out;

            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> this.in;
                    case 1 -> this.out;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                if (index == 0 && value > 0 && context.get((world, pos) -> {
                    if (world.isClient) return true;
                    ChequeTableScreenHandler.this.doCheckIn();
                    return false;
                }).orElse(true)) this.in = value;
                if (index == 1 && value > 0 && context.get((world, pos) -> {
                    if (world.isClient) return true;
                    ChequeTableScreenHandler.this.doCheckOut();
                    return false;
                }).orElse(true)) this.out = value;
            }

            @Override
            public int size() {
                return 2;
            }
        };
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 5; ++j)
                this.addSlot(new MoneyOnlySlot(this.coinInv, j + i * 5, 8 + j * 18, 18 + i * 18));
        this.addSlot(new ChequeOnlySlot(this.chequeInputInv, 0, 134, 18));
        this.addSlot(new TakeOnlySlot(this, this.chequeOutputInv, 0, 134, 54, amount -> {
            //TODO
            NeeHandlerUtils.playCheckedSound(this.context);
        }));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, NeeBlocks.CHEQUE_TABLE);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        for (int i = 0; i < this.coinInv.size(); i++)
            player.giveItemStack(this.coinInv.getStack(i));
        player.giveItemStack(this.chequeInputInv.getStack(0));
    }

    //Client only
    public void checkIn() {
        this.delegate.set(0, 1);
    }

    public void checkOut() {
        this.delegate.set(1, 1);
    }

    //Server only
    public void doCheckIn() {

    }

    public void doCheckOut() {

    }
}
