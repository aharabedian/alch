package gg.clerisy.alch.common;

import gg.clerisy.alch.common.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class StillBlock extends Block implements EntityBlock {
    public StillBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BlockEntityInit.STILL.get().create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return blockEntityType == BlockEntityInit.STILL.get() ? StillBlockEntity::tick : null;
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider(
                (containerId, playerInv, player) -> new StillMenu(containerId, playerInv),
                Component.literal("Herro Still!")
        );
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide() && interactionHand == InteractionHand.MAIN_HAND) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof StillBlockEntity) {

                NetworkHooks.openScreen((ServerPlayer) player, this.getMenuProvider(blockState, level, blockPos));

                ((StillBlockEntity) blockEntity).toggle();
                return InteractionResult.SUCCESS;
            }
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
}
