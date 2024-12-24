package com.iafenvoy.nee.screen.gui;

import com.iafenvoy.nee.NotEnoughEconomy;
import com.iafenvoy.nee.screen.handler.TradeStationCustomerScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TradeStationCustomerScreen extends HandledScreen<TradeStationCustomerScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(NotEnoughEconomy.MOD_ID, "textures/gui/trade_station_customer.png");

    public TradeStationCustomerScreen(TradeStationCustomerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight++;
    }

    @Override
    protected void init() {
        super.init();
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("→"), button -> {

        }).position(i + 80, j + 36).size(16, 16).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }
}
