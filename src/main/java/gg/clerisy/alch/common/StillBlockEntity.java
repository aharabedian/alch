package gg.clerisy.alch.common;

import gg.clerisy.alch.AlchMod;
import gg.clerisy.alch.common.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StillBlockEntity extends BlockEntity {

    public LazyOptional<ItemStackHandler> handler;
    public final int size = 3;
    private boolean isActive;
    public static final Component TITLE = Component.translatable("container." + AlchMod.MODID + ".still");

    private final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            StillBlockEntity.this.setChanged();
            super.onContentsChanged(slot);
        }
    };
    public StillBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.STILL.get(), blockPos, blockState);
        AlchMod.LOGGER.info(this.toString());
        this.isActive = false;
        this.handler = LazyOptional.of(() -> this.inventory);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T blockEntity) {
        if (level.getGameTime() % 20 == 0) {
            StillBlockEntity stillBlockEntity = (StillBlockEntity) blockEntity;
            AlchMod.LOGGER.info("Still: " + stillBlockEntity.worldPosition.toShortString() + " State: " + stillBlockEntity.isActive);
        }
    }

    public void toggle() {
        this.isActive = !this.isActive;
        if (level == null) {
            AlchMod.LOGGER.error("Error! Level is null!");
            return;
        }
        this.setChanged();
        level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putBoolean("isActive", this.isActive);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.isActive = tag.getBoolean("isActive");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("isActive", this.isActive);
    }

    public void update() {
        requestModelDataUpdate();
        setChanged();
        if (this.level != null) {
            this.level.setBlockAndUpdate(this.worldPosition, getBlockState());
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? handler.cast() : super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        handler.invalidate();
    }

    public ItemStackHandler getInventory() { return this.inventory; }
}
