package com.beardlessbrady.gocurrency.blocks.vending;

import com.beardlessbrady.gocurrency.init.CommonRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by BeardlessBrady on 2021-02-22 for Currency-Mod
 * All Rights Reserved
 * https://github.com/Beardlessbrady/Currency-Mod
 */
public class VendingBlock extends ContainerBlock {

    public VendingBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return CommonRegistry.TILE_VENDING.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) return ActionResultType.SUCCESS; // Client do nothing

        INamedContainerProvider namedContainerProvider = this.getContainer(state, worldIn, pos);
        if(namedContainerProvider != null) {
            if(! (player instanceof ServerPlayerEntity)) return ActionResultType.FAIL;
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, pos);
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof VendingTile) {
                VendingTile tile = (VendingTile) tileentity;
                tile.dropAllContents(worldIn, pos);
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);  // call it last, because it removes the TileEntity
        }
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return false;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return 0;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
