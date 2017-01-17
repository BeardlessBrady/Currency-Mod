package gunn.modcurrency.common.containers;

import gunn.modcurrency.client.guis.GuiWallet;
import gunn.modcurrency.common.core.util.SlotBank;
import gunn.modcurrency.common.items.ItemWallet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Distributed with the Currency-Mod for Minecraft
 * Copyright (C) 2017  Brady Gunn
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * File Created on 2017-01-16
 */
public class ContainerWallet extends Container{
    //Slot Index's
    //0-35 = Player Inventory's
    //36-Onwards = Wallet Slots
    private final int HOTBAR_SLOT_COUNT = 9;
    private final int PLAYER_INV_ROW_COUNT = 3;
    private final int PLAYER_INV_COLUMN_COUNT = 9;
    private final int PLAYER_INV_TOTAL_COUNT = PLAYER_INV_COLUMN_COUNT * PLAYER_INV_ROW_COUNT;
    private final int PLAYER_TOTAL_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INV_TOTAL_COUNT;

    private final int WALLET_COLUMN_COUNT = ItemWallet.WALLET_COLUMN_COUNT;
    private final int WALLET_ROW_COUNT = ItemWallet.WALLET_ROW_COUNT;
    private final int WALLET_TOTAL_COUNT = WALLET_COLUMN_COUNT * WALLET_ROW_COUNT;

    private final int PLAYER_FIRST_SLOT_INDEX = 0;
    private final int WALLET_FIRST_SLOT_INDEX = PLAYER_FIRST_SLOT_INDEX + PLAYER_TOTAL_COUNT;

    private final int GUI_XPOS_OFFPUT = GuiWallet.GUI_XPOS_OFFPUT;

    public ContainerWallet(InventoryPlayer invPlayer, ItemStack wallet){
        setupPlayerInv(invPlayer);
        setupWalletInv(wallet);
    }

    private void setupPlayerInv(InventoryPlayer invPlayer){
        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int HOTBAR_XPOS = 27;
        final int HOTBAR_YPOS = 178;

        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) addSlotToContainer(new Slot(invPlayer, x, (HOTBAR_XPOS + SLOT_X_SPACING * x) + GUI_XPOS_OFFPUT, HOTBAR_YPOS));

        final int PLAYER_INV_XPOS = 27;
        final int PLAYER_INV_YPOS = 120;

        for (int y = 0; y < PLAYER_INV_ROW_COUNT; y++){
            for (int x = 0; x < PLAYER_INV_COLUMN_COUNT; x++){
                int slotNum = HOTBAR_SLOT_COUNT + y * PLAYER_INV_COLUMN_COUNT + x;
                int xpos = PLAYER_INV_XPOS + x * SLOT_X_SPACING;
                int ypos = PLAYER_INV_YPOS + y * SLOT_Y_SPACING;
                addSlotToContainer(new Slot(invPlayer, slotNum, xpos + GUI_XPOS_OFFPUT, ypos));
            }
        }
    }

    private void setupWalletInv(ItemStack wallet){
        System.out.println(wallet.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));

        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int WALLET_INV_XPOS = 44;
        final int WALLET_INV_YPOS = 32;

        for (int y = 0; y < WALLET_COLUMN_COUNT; y++){
            for (int x = 0; x < WALLET_ROW_COUNT; x++){
                int slotNum = 1 + y * WALLET_ROW_COUNT + x;
                int xpos = WALLET_INV_XPOS + x * SLOT_X_SPACING;
                int ypos = WALLET_INV_YPOS + y * SLOT_Y_SPACING;
               // addSlotToContainer(new SlotBank(itemHandler, slotNum, xpos, ypos));
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    public ItemStack[] getWalletInventory(ItemStack wallet){
        NBTTagCompound inventoryNBT = wallet.getTagCompound().getCompoundTag("inventory");
        ItemStack[] inventoryStacks = new ItemStack[inventoryNBT.getSize()];
        for (int i = 0; i < inventoryNBT.getSize(); i++) inventoryStacks[i].deserializeNBT((NBTTagCompound) inventoryNBT.getTag(Integer.toString(i)));

        return inventoryStacks;
    }
}
