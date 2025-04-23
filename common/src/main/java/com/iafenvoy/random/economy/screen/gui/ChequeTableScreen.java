package com.iafenvoy.random.economy.screen.gui;

import com.iafenvoy.random.economy.Constants;
import com.iafenvoy.random.economy.RandomEconomy;
import com.iafenvoy.random.economy.screen.handler.ChequeTableScreenHandler;
import com.iafenvoy.random.economy.util.PacketBufferUtils;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ChequeTableScreen extends HandledScreen<ChequeTableScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(RandomEconomy.MOD_ID, "textures/gui/cheque_table.png");

    public ChequeTableScreen(ChequeTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight++;
    }

    @Override
    protected void init() {
        super.init();
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("←"), button -> {
            assert Constants.CHEQUE_CHECK_OUT != null;
            NetworkManager.sendToServer(Constants.CHEQUE_CHECK_OUT, PacketBufferUtils.create());
        }).position(i + 104, j + 18).size(16, 16).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("→"), button -> {
            assert Constants.CHEQUE_CHECK_IN != null;
            NetworkManager.sendToServer(Constants.CHEQUE_CHECK_IN, PacketBufferUtils.create());
        }).position(i + 104, j + 54).size(16, 16).build());
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
