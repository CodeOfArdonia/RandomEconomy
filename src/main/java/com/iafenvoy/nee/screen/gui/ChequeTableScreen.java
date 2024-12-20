package com.iafenvoy.nee.screen.gui;

import com.iafenvoy.nee.NotEnoughEconomy;
import com.iafenvoy.nee.screen.handler.ChequeTableScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ChequeTableScreen extends HandledScreen<ChequeTableScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(NotEnoughEconomy.MOD_ID, "textures/gui/cheque_table.png");

    public ChequeTableScreen(ChequeTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        ++this.backgroundHeight;
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
