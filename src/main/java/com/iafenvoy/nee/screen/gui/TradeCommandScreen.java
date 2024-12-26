package com.iafenvoy.nee.screen.gui;

import com.iafenvoy.nee.Constants;
import com.iafenvoy.nee.NotEnoughEconomy;
import com.iafenvoy.nee.screen.handler.TradeCommandScreenHandler;
import com.iafenvoy.nee.trade.TradeMessageType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TradeCommandScreen extends HandledScreen<TradeCommandScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(NotEnoughEconomy.MOD_ID, "textures/gui/trade_command.png");
    private ButtonWidget self, another;
    private boolean accepted;

    public TradeCommandScreen(TradeCommandScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 175;
        this.backgroundHeight = 221;
        this.playerInventoryTitleY = 128;
    }

    @Override
    protected void init() {
        super.init();
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.self = this.addDrawableChild(ButtonWidget.builder(Text.translatable("screen.not_enough_economy.trade.button.accept"), button -> {
            this.accepted ^= true;
            assert Constants.TRADE_STATE_CHANGE != null;
            ClientPlayNetworking.send(Constants.TRADE_STATE_CHANGE, PacketByteBufs.create().writeEnumConstant(this.accepted ? TradeMessageType.SELF_ACCEPT : TradeMessageType.SELF_CANCEL_ACCEPT));
            this.self.setMessage(Text.translatable("screen.not_enough_economy.trade.button."+(this.accepted ? "accepted" : "accept")));
        }).position(i + 7, j + 110).size(72, 16).build());
        this.another = this.addDrawableChild(ButtonWidget.builder(Text.translatable("screen.not_enough_economy.trade.button.waiting"), button -> {
        }).position(i + 97, j + 110).size(72, 16).build());
        this.another.active = false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);
        context.drawText(this.textRenderer, this.handler.getAnotherPlayerName(), this.titleX + 90, this.titleY, 4210752, false);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void close() {
        super.close();
        assert Constants.TRADE_STATE_CHANGE != null;
        ClientPlayNetworking.send(Constants.TRADE_STATE_CHANGE, PacketByteBufs.create().writeEnumConstant(TradeMessageType.SELF_CLOSE_SCREEN));
    }

    static {
        assert Constants.TRADE_STATE_CHANGE != null;
        ClientPlayNetworking.registerGlobalReceiver(Constants.TRADE_STATE_CHANGE, (client, handler, buf, responseSender) -> {
            TradeMessageType type = buf.readEnumConstant(TradeMessageType.class);
            if (!(client.currentScreen instanceof TradeCommandScreen trade)) return;
            client.execute(switch (type) {
                case ANOTHER_ACCEPT ->
                        (Runnable) () -> trade.another.setMessage(Text.translatable("screen.not_enough_economy.trade.button.accepted"));
                case ANOTHER_CANCEL_ACCEPT ->
                        (Runnable) () -> trade.another.setMessage(Text.translatable("screen.not_enough_economy.trade.button.waiting"));
                case ANOTHER_CLOSE_SCREEN -> (Runnable) () -> client.setScreen(null);
                default -> TradeMessageType.EMPTY;
            });
        });
    }
}
