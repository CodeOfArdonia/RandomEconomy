package com.iafenvoy.nee.item.block;

import com.iafenvoy.nee.item.block.entity.TradeStationBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TradeStationBlock extends WorkStationBlock implements BlockEntityProvider {
    public TradeStationBlock() {
        super(Settings.copy(Blocks.CRAFTING_TABLE));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TradeStationBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof TradeStationBlockEntity blockEntity) {
            player.openHandledScreen(blockEntity);
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.getBlockEntity(pos) instanceof TradeStationBlockEntity blockEntity && placer instanceof PlayerEntity player)
            blockEntity.setOwner(player.getUuid());
    }
}
