package com.beardlessbrady.gocurrency.blocks.vending;

import com.beardlessbrady.gocurrency.init.CommonRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by BeardlessBrady on 2021-03-01 for Currency-Mod
 * All Rights Reserved
 * https://github.com/Beardlessbrady/Currency-Mod
 */
public class VendingTile extends TileEntity implements INamedContainerProvider, ITickableTileEntity {
    private static final String CONTENTS_INVENTORY_TAG = "contents";

    public static final int STOCK_ROW_COUNT = 4;
    public static final int STOCK_COLUMN_COUNT = 4;
    public static final int STOCK_SLOT_COUNT = STOCK_ROW_COUNT * STOCK_COLUMN_COUNT;
    public static final int INPUT_SLOTS_COUNT = 1;
    public static final int OUTPUT_SLOTS_COUNT = 3;
    public static final int TOTAL_SLOTS_COUNT = STOCK_SLOT_COUNT + INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

    private VendingStockContents stockContents;
    private VendingContents inputContents;
    private VendingContents outputContents;

    private final VendingStateData vendingStateData= new VendingStateData();

    public VendingTile() {
        super(CommonRegistry.TILE_VENDING.get());
        stockContents = new VendingStockContents(STOCK_SLOT_COUNT, this::canPlayerUse, this::markDirty);
        inputContents = new VendingContents(INPUT_SLOTS_COUNT, this::canPlayerUse, this::markDirty);
        outputContents = new VendingContents(OUTPUT_SLOTS_COUNT, this::canPlayerUse, this::markDirty);
    }

    @Override
    public void tick() {
        if(world.isRemote){
      //\\      System.out.println("CLIENT: " + vendingStateData.get(VendingStateData.MODE_INDEX));
        } else {
      //      System.out.println("SERVER: " + vendingStateData.get(VendingStateData.MODE_INDEX));
        }
    }

    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) return false;
        final double X_CENTRE_OFFSET = 0.5;
        final double Y_CENTRE_OFFSET = 0.5;
        final double Z_CENTRE_OFFSET = 0.5;
        final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
        return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
    }

    public void dropAllContents(World world, BlockPos blockPos){
        InventoryHelper.dropInventoryItems(world, blockPos, stockContents);
        InventoryHelper.dropInventoryItems(world, blockPos, inputContents);
        InventoryHelper.dropInventoryItems(world, blockPos, outputContents);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.gocurrency.vending");
    }

    @Nullable
    @Override
    // Server side creation of Container
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return VendingContainer.createContainerServer(windowID, playerInventory, stockContents, inputContents, outputContents, vendingStateData, this);
    }

    public int getVendingStateData(int index){
        return vendingStateData.get(index);
    }

    public void setVendingStateData(int index, int value){
        this.vendingStateData.set(index, value);
    }


    // ---- NBT Stuff ----
    private final String STOCK_SLOTS_NBT = "stockSlots";
    private final String INPUT_SLOTS_NBT = "inputSlots";
    private final String OUTPUT_SLOTS_NBT = "outputSlots";

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put(STOCK_SLOTS_NBT, stockContents.serializeNBT());
        compound.put(INPUT_SLOTS_NBT, inputContents.serializeNBT());
        compound.put(OUTPUT_SLOTS_NBT, outputContents.serializeNBT());
        vendingStateData.putIntoNBT(compound);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        CompoundNBT stockNBT = nbt.getCompound(STOCK_SLOTS_NBT);
        stockContents.deserializeNBT(stockNBT);

        CompoundNBT inputNBT = nbt.getCompound(INPUT_SLOTS_NBT);
        inputContents.deserializeNBT(inputNBT);

        CompoundNBT outputNBT = nbt.getCompound(OUTPUT_SLOTS_NBT);
        outputContents.deserializeNBT(outputNBT);

        vendingStateData.readFromNBT(nbt);

        if (stockContents.getSizeInventory() != STOCK_SLOT_COUNT || inputContents.getSizeInventory() != INPUT_SLOTS_COUNT
                || outputContents.getSizeInventory() != OUTPUT_SLOTS_COUNT)
            throw new IllegalArgumentException("Corrupted NBT: Number of inventory slots did not match expected.");
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT updateTag = getUpdateTag();
        return new SUpdateTileEntityPacket(this.pos, 42, updateTag); //Type in # is arbitrary
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT updateTag = pkt.getNbtCompound();
        BlockState blockState = world.getBlockState(pos);
        handleUpdateTag(blockState, updateTag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT compoundNBT = new CompoundNBT();
        write(compoundNBT);
        return compoundNBT;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }
}
