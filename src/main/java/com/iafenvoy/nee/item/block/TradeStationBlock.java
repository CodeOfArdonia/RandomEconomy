package com.iafenvoy.nee.item.block;

import com.iafenvoy.nee.item.block.entity.TradeStationBlockEntity;
import com.iafenvoy.nee.registry.NeeBlocks;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
        NbtCompound compound = BlockItem.getBlockEntityNbt(itemStack);
        if (world.getBlockEntity(pos) instanceof TradeStationBlockEntity blockEntity)
            if (compound != null && !compound.isEmpty())
                blockEntity.readNbt(compound);
            else if (placer instanceof PlayerEntity player)
                blockEntity.setOwner(player.getUuid());
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof TradeStationBlockEntity blockEntity) {
            if (Objects.equals(player.getUuid(), blockEntity.getOwner()))
                blockEntity.dropInventory();
            else if (player.hasPermissionLevel(2) && player.isCreative())
                blockEntity.dropFullBlock();
        }
        super.onBreak(world, pos, state, player);
    }

    static {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (!world.getBlockState(pos).isOf(NeeBlocks.TRADE_STATION) || !(blockEntity instanceof TradeStationBlockEntity be))
                return true;
            if (player.hasPermissionLevel(2) && player.isCreative()) return true;
            return Objects.equals(player.getUuid(), be.getOwner());
        });
    }
}
