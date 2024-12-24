package com.iafenvoy.nee.screen.gui;

import com.iafenvoy.nee.NotEnoughEconomy;
import com.iafenvoy.nee.screen.handler.TradeStationOwnerScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TradeStationOwnerScreen extends HandledScreen<TradeStationOwnerScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(NotEnoughEconomy.MOD_ID, "textures/gui/trade_station_owner.png");

    public TradeStationOwnerScreen(TradeStationOwnerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 175;
        this.backgroundHeight = 221;
        this.playerInventoryTitleY = 128;
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
