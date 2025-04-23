package com.iafenvoy.random.economy.render;

import com.iafenvoy.random.economy.item.block.entity.SystemStationBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class SystemStationBlockEntityRenderer implements BlockEntityRenderer<SystemStationBlockEntity> {
    private final ItemRenderer itemRenderer;

    public SystemStationBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(SystemStationBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getDisplayStack().isEmpty()) return;
        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        this.renderItem(entity, tickDelta, matrices, vertexConsumers, overlay);
        matrices.pop();
    }

    private void renderItem(SystemStationBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int overlay) {
        matrices.push();
        assert entity.getWorld() != null;
        matrices.translate(0, 0.75, 0);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.getWorld().getTime() + tickDelta) * 4));
        matrices.scale(1.5f, 1.5f, 1.5f);
        this.itemRenderer.renderItem(entity.getDisplayStack(), ModelTransformationMode.GROUND, 15728848, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
        matrices.pop();
    }
}
