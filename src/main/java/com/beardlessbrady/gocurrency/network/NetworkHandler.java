package com.beardlessbrady.gocurrency.network;

import com.beardlessbrady.gocurrency.GOCurrency;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.core.jmx.Server;

/**
 * Created by BeardlessBrady on 2021-03-06 for Currency-Mod
 * All Rights Reserved
 * https://github.com/Beardlessbrady/Currency-Mod
 */
public class NetworkHandler {
    private final String protocolVersion = Integer.toString(1);
    private final SimpleChannel handler = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(GOCurrency.MODID, "main_channel"))
            .clientAcceptedVersions(protocolVersion::equals)
            .serverAcceptedVersions(protocolVersion::equals)
            .networkProtocolVersion(() -> protocolVersion)
            .simpleChannel();

    public void register() {
        int id = 0;

        handler.registerMessage(id++, MessageVendingStateData.class, MessageVendingStateData::encode, MessageVendingStateData::decode, MessageVendingStateData::handle);

    }

    public void sendToServer(Object message){
        handler.sendToServer(message);
    }

    public void sendTo(ServerPlayerEntity playerEntity, Object message){
        if (!(playerEntity instanceof FakePlayer)) {
            handler.sendTo(message, playerEntity.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }
}
