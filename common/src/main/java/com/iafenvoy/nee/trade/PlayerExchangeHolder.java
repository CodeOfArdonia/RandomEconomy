package com.iafenvoy.nee.trade;

import com.iafenvoy.nee.Constants;
import com.iafenvoy.nee.screen.handler.TradeCommandScreenHandler;
import com.iafenvoy.nee.util.InventoryUtil;
import com.iafenvoy.nee.util.PacketBufferUtils;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class PlayerExchangeHolder {
    private static final List<PlayerExchangeHolder> PENDING = new LinkedList<>();
    private static final Map<PlayerEntity, PlayerExchangeHolder> HOLDER_BY_PLAYER = new HashMap<>();
    private static final Map<PlayerEntity, Single> SINGLE_BY_PLAYER = new HashMap<>();
    private final Single user1, user2;

    private PlayerExchangeHolder(PlayerEntity user1, PlayerEntity user2) {
        this.user1 = new Single(user1, new SimpleInventory(20));
        this.user2 = new Single(user2, new SimpleInventory(20));
        PENDING.add(this);
        HOLDER_BY_PLAYER.put(user1, this);
        HOLDER_BY_PLAYER.put(user2, this);
        SINGLE_BY_PLAYER.put(user1, this.user1);
        SINGLE_BY_PLAYER.put(user2, this.user2);
    }

    public void onCancel(Text canceller) {
        InventoryUtil.insertItems(this.user1.player.getInventory(), this.user1.inventory);
        InventoryUtil.insertItems(this.user2.player.getInventory(), this.user2.inventory);
        this.user1.player.sendMessage(Text.translatable("command.not_enough_economy.trade_cancel", canceller));
        this.user2.player.sendMessage(Text.translatable("command.not_enough_economy.trade_cancel", canceller));
        this.close();
    }

    public void onConfirm() {
        InventoryUtil.insertItems(this.user1.player.getInventory(), this.user2.inventory);
        InventoryUtil.insertItems(this.user2.player.getInventory(), this.user1.inventory);
        this.user1.player.sendMessage(Text.translatable("command.not_enough_economy.trade_success"));
        this.user2.player.sendMessage(Text.translatable("command.not_enough_economy.trade_success"));
        this.close();
    }

    public void close() {
        PENDING.remove(this);
        this.closeInternal();
    }

    private void closeInternal() {
        HOLDER_BY_PLAYER.remove(this.user1.player);
        HOLDER_BY_PLAYER.remove(this.user2.player);
        SINGLE_BY_PLAYER.remove(this.user1.player);
        SINGLE_BY_PLAYER.remove(this.user2.player);
        closeTradeScreen(this.user1.player);
        closeTradeScreen(this.user2.player);
    }

    public void checkTrade() {
        if (this.user1.accepted && this.user2.accepted) this.onConfirm();
    }

    public void openScreen() {
        this.user1.player.openHandledScreen(this.createScreen(this.user1, this.user2));
        this.user2.player.openHandledScreen(this.createScreen(this.user2, this.user1));
    }

    private NamedScreenHandlerFactory createScreen(Single current, Single another) {
        Text anotherName = another.player.getDisplayName();
        return new ExtendedMenuProvider() {
            @Override
            public void saveExtraData(PacketByteBuf buf) {
                buf.writeText(anotherName);
            }

            @Override
            public Text getDisplayName() {
                return Text.translatable("screen.not_enough_economy.trade");
            }

            @Override
            public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new TradeCommandScreenHandler(syncId, playerInventory, current.inventory, another.inventory, anotherName);
            }
        };
    }

    private Single getAnother(Single self) {
        return self == this.user1 ? this.user2 : this.user1;
    }

    private boolean isIn(Single single) {
        return single == this.user1 || single == this.user2;
    }

    public static void closeTradeScreen(PlayerEntity player) {
        assert Constants.TRADE_STATE_CHANGE != null;
        if (player instanceof ServerPlayerEntity serverPlayer)
            NetworkManager.sendToPlayer(serverPlayer, Constants.TRADE_STATE_CHANGE, PacketBufferUtils.create().writeEnumConstant(TradeMessageType.ANOTHER_CLOSE_SCREEN));
    }

    public static void sendAcceptState(PlayerEntity player, boolean accepted) {
        assert Constants.TRADE_STATE_CHANGE != null;
        if (player instanceof ServerPlayerEntity serverPlayer)
            NetworkManager.sendToPlayer(serverPlayer, Constants.TRADE_STATE_CHANGE, PacketBufferUtils.create().writeEnumConstant(accepted ? TradeMessageType.ANOTHER_ACCEPT : TradeMessageType.ANOTHER_CANCEL_ACCEPT));
    }

    public static void launchTrade(PlayerEntity user1, PlayerEntity user2) {
        new PlayerExchangeHolder(user1, user2).openScreen();
    }

    static {
        assert Constants.TRADE_STATE_CHANGE != null;
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, Constants.TRADE_STATE_CHANGE, (buf, ctx) -> {
            PlayerExchangeHolder holder = HOLDER_BY_PLAYER.get(ctx.getPlayer());
            Single single = SINGLE_BY_PLAYER.get(ctx.getPlayer());
            if (holder == null || !holder.isIn(single)) return;
            Single another = holder.getAnother(single);
            TradeMessageType type = buf.readEnumConstant(TradeMessageType.class);
            ctx.queue(switch (type) {
                case SELF_ACCEPT -> (Runnable) () -> {
                    single.accepted = true;
                    sendAcceptState(another.player, true);
                    holder.checkTrade();
                };
                case SELF_CANCEL_ACCEPT -> (Runnable) () -> {
                    single.accepted = false;
                    sendAcceptState(another.player, false);
                };
                case SELF_CLOSE_SCREEN ->
                        (Runnable) () -> holder.onCancel(single.player.getDisplayName().copyContentOnly().formatted(Formatting.RED));
                default -> TradeMessageType.EMPTY;
            });
        });
        LifecycleEvent.SERVER_STOPPING.register(server -> PENDING.forEach(PlayerExchangeHolder::closeInternal));
    }

    private static class Single {
        private final PlayerEntity player;
        private final Inventory inventory;
        private boolean accepted;

        private Single(PlayerEntity player, Inventory inventory) {
            this.player = player;
            this.inventory = inventory;
        }
    }
}
