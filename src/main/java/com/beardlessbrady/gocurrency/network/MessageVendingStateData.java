package com.beardlessbrady.gocurrency.network;

import com.beardlessbrady.gocurrency.blocks.vending.VendingStateData;
import com.beardlessbrady.gocurrency.blocks.vending.VendingTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.security.Permission;
import java.util.function.Supplier;

/**
 * Created by BeardlessBrady on 2021-03-06 for Currency-Mod
 * All Rights Reserved
 * https://github.com/Beardlessbrady/Currency-Mod
 */
public class MessageVendingStateData{
    private final BlockPos pos;
    private final int id;

    public MessageVendingStateData(BlockPos pos, int id){
        this.pos = pos;
        this.id = id;
    }

    public static MessageVendingStateData decode(PacketBuffer buf){
        BlockPos pos = buf.readBlockPos();
        int id = buf.readInt();

        return new MessageVendingStateData(pos, id);
    }

    public static void encode(MessageVendingStateData message, PacketBuffer buf){
        buf.writeBlockPos(message.pos);
        buf.writeInt(message.id);
    }

    public static void handle(MessageVendingStateData message, Supplier<NetworkEvent.Context> ctx){
        PlayerEntity playerEntity = ctx.get().getSender();

        if(playerEntity != null){
            ctx.get().enqueueWork(() -> {
                TileEntity tile = playerEntity.getEntityWorld().getTileEntity(message.pos);

                if(tile instanceof VendingTile){
                    switch (message.id) {
                        case VendingStateData.MODE_INDEX:
                            System.out.println(((VendingTile) tile).getVendingStateData(message.id));
                            ((VendingTile) tile).setVendingStateData(message.id, 1);
                            System.out.println(((VendingTile) tile).getVendingStateData(message.id));
                          //  System.out.println(message.id);
                           // ((VendingTile) tile).setVendingStateData(message.id, ((VendingTile) tile).getVendingStateData(message.id) == 0 ? 1 : 0);
                            break;
                    }
                }
            });
        }
    }
}
