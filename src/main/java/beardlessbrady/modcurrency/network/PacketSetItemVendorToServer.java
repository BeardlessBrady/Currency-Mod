package beardlessbrady.modcurrency.network;

import beardlessbrady.modcurrency.block.TileEconomyBase;
import beardlessbrady.modcurrency.block.vending.TileVending;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This class was created by BeardlessBrady. It is distributed as
 * part of The Currency-Mod. Source Code located on github:
 * https://github.com/BeardlessBrady/Currency-Mod
 * -
 * Copyright (C) All Rights Reserved
 * File Created 2019-02-22
 */

public class PacketSetItemVendorToServer implements IMessage {
    private BlockPos blockPos;
    private int data, field, element;

    public static final int FIELD_ITEMMAX = 0;
    public static final int FIELD_TIMERAISE = 1;

    public PacketSetItemVendorToServer(){}

    public void setData(int element, int data, int field, BlockPos pos) {
        this.blockPos = pos;
        this.data = data;
        this.element = element;
        this.field = field;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        data = buf.readInt();
        field = buf.readInt();
        element = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
        buf.writeInt(data);
        buf.writeInt(field);
        buf.writeInt(element);
    }

    public static class Handler implements IMessageHandler<PacketSetItemVendorToServer, IMessage> {

        @Override
        public IMessage onMessage(final PacketSetItemVendorToServer message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message,ctx));
            return null;
        }

        private void handle(PacketSetItemVendorToServer message, MessageContext ctx){
            EntityPlayerMP playerEntity = ctx.getServerHandler().player;
            World world = playerEntity.world;

            if(world.getTileEntity(message.blockPos) instanceof TileVending){
                TileVending tile = (TileVending) world.getTileEntity(message.blockPos);
                switch(message.field){
                    case FIELD_ITEMMAX: //Restock
                        tile.setItemVendor(message.element, tile.getItemVendor(message.element).setItemMax(message.data));
                        break;
                    case FIELD_TIMERAISE:
                        tile.setItemVendor(message.element, tile.getItemVendor(message.element).setTimeRaise(message.data));
                        break;
                }
            }
        }
    }
}