package com.iafenvoy.nee.render;

import com.iafenvoy.nee.item.block.entity.TradeStationBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class TradeStationBlockEntityRenderer implements BlockEntityRenderer<TradeStationBlockEntity> {
    private final ItemRenderer itemRenderer;
    private final BlockEntityRenderDispatcher dispatcher;
    private final TextRenderer textRenderer;

    public TradeStationBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
        this.dispatcher = ctx.getRenderDispatcher();
        this.textRenderer = ctx.getTextRenderer();
    }

    @Override
    public void render(TradeStationBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getDisplayStack().isEmpty()) return;
        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        this.renderItem(entity, tickDelta, matrices, vertexConsumers, overlay);
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.crosshairTarget instanceof BlockHitResult blockHitResult && client.world != null && client.world.getBlockEntity(blockHitResult.getBlockPos()) == entity)
            this.renderLabelIfPresent(entity, entity.getFloatingName(), matrices, vertexConsumers);
        matrices.pop();
    }

    private void renderItem(TradeStationBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int overlay) {
        matrices.push();
        assert entity.getWorld() != null;
        matrices.translate(0, 0.75, 0);
        matrices.scale(1.5f, 1.5f, 1.5f);
        this.itemRenderer.renderItem(entity.getDisplayStack(), ModelTransformationMode.GROUND, 15728848, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
        matrices.pop();
    }

    private void renderLabelIfPresent(TradeStationBlockEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        double yOffset = 1.5f;
        double d = this.dispatcher.camera.getPos().squaredDistanceTo(entity.getPos().toCenterPos().add(0, yOffset, 0));
        if (d <= 4096.0) {
            matrices.push();
            matrices.translate(0, yOffset, 0);
            matrices.multiply(this.dispatcher.camera.getRotation());
            matrices.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int j = (int) (g * 255.0F) << 24;
            float h = (float) (-this.textRenderer.getWidth(text) / 2);
            this.textRenderer.draw(text, h, 0, 553648127, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, j, 15728848);
            this.textRenderer.draw(text, h, 0, -1, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 15728848);
            matrices.pop();
        }
    }
}
