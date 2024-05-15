package gg.clerisy.alch.common;

import gg.clerisy.alch.common.init.BlockInit;
import gg.clerisy.alch.common.init.MenuTypeInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class StillMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess containerAccessLevel;

    // Client menu constructor
    public StillMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, new ItemStackHandler(3), BlockPos.ZERO);
    }

    // Server menu constructor
    public StillMenu(int containerId, Inventory playerInv, IItemHandler slots, BlockPos blockPos) {
        super(MenuTypeInit.STILL.get(), containerId);
        this.containerAccessLevel = ContainerLevelAccess.create(playerInv.player.getLevel(), blockPos);

        final int slotSizePlus2 = 18, startX = 8, startY = 84, hotbarY = 142, inventoryY = 10;

        // Player Inventory Slots
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 3; row++) {
                addSlot(new Slot(playerInv, 9 + row * 9 + col, startX + col * slotSizePlus2, startY + row * slotSizePlus2));
            }

            addSlot(new Slot(playerInv, col, startX + col * slotSizePlus2, hotbarY));
        }

        // Still slots
        for (int col = 0; col < 3; col++) {
            addSlot(new SlotItemHandler(slots, col, startX + col * slotSizePlus2, inventoryY));
        }

    }

    // Assume we have a data inventory of size 3
    // The inventory has 3 inputs (index 0 - 2)
    // We also have the 27 player inventory slots and the 9 hotbar slots
    // As such, the actual slots are indexed like so:
    //   - Data Inventory: Inputs (0 - 2)
    //   - Player Inventory (3 - 29)
    //   - Player Hotbar (30 - 38)
    @Override
    public ItemStack quickMoveStack(Player player, int quickMovedSlotIndex) {
        // The quick moved slot stack
        ItemStack quickMovedStack = ItemStack.EMPTY;
        // The quick moved slot
        Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex);

        // If the slot is in the valid range and the slot is not empty
        if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
            // Get the raw stack to move
            ItemStack rawStack = quickMovedSlot.getItem();
            // Set the slot stack to a copy of the raw stack
            quickMovedStack = rawStack.copy();

            /*
            The following quick move logic can be simplified to if in data inventory,
            try to move to player inventory/hotbar and vice versa for containers
            that cannot transform data (e.g. chests).
            */

            // if the quick move was performed on the player inventory or hotbar slot
            if (quickMovedSlotIndex >= 3 && quickMovedSlotIndex < 39) {
                // Try to move the inventory/hotbar slot into the data inventory input slots
                if (!this.moveItemStackTo(rawStack, 0, 2,  false)) {
                    // If cannot move and in player inventory slot, try to move to hotbar
                    if (quickMovedSlotIndex < 30) {
                        if (!this.moveItemStackTo(rawStack, 30, 39, false)) {
                            // If cannot move, no longer quick move
                            return ItemStack.EMPTY;
                        }
                    }
                    // Else try to move hotbar into player inventory slot
                    else if (!this.moveItemStackTo(rawStack, 3, 30, false)) {
                        // If cannot move, no longer quick move
                        return ItemStack.EMPTY;
                    }
                }
            }
            // Else if the quick move was performed on the data inventory input slots, try to move to player inventory/hotbar
            else if (!this.moveItemStackTo(rawStack, 3, 39, false)) {
                // If cannot move, no longer quick move
                return ItemStack.EMPTY;
            }

            if (rawStack.isEmpty()) {
                // If the raw stack has completely moved out of the slot, set the slot to the empty stack
                quickMovedSlot.set(ItemStack.EMPTY);
            } else {
                // Otherwise, notify the slot that that the stack count has changed
                quickMovedSlot.setChanged();
            }

            /*
            The following if statement and Slot#onTake call can be removed if the
            menu does not represent a container that can transform stacks (e.g.
            chests).
            */
            if (rawStack.getCount() == quickMovedStack.getCount()) {
                // If the raw stack was not able to be moved to another slot, no longer quick move
                return ItemStack.EMPTY;
            }
            // Execute logic on what to do post move with the remaining stack
            quickMovedSlot.onTake(player, rawStack);
        }

        return quickMovedStack; // Return the slot stack
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
