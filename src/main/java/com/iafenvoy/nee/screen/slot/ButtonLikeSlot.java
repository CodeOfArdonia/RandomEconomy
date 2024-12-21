package com.iafenvoy.nee.screen.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ButtonLikeSlot extends Slot {
    private final SlotClickCallback onClick;

    public ButtonLikeSlot(Inventory inventory, int index, int x, int y, SlotClickCallback onClick) {
        super(inventory, index, x, y);
        this.onClick = onClick;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        this.onClick.onClick(this, playerEntity);
        return false;
    }

    @FunctionalInterface
    public interface SlotClickCallback {
        void onClick(ButtonLikeSlot slot, PlayerEntity player);
    }
}
