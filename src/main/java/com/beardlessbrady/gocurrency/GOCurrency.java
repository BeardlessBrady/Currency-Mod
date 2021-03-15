package com.beardlessbrady.gocurrency;

import com.beardlessbrady.gocurrency.init.ClientRegistry;
import com.beardlessbrady.gocurrency.init.CommonRegistry;
import com.beardlessbrady.gocurrency.init.GenerateResourcePack;
import com.beardlessbrady.gocurrency.init.ModItemGroup;
import com.beardlessbrady.gocurrency.items.CurrencyItem;
import com.beardlessbrady.gocurrency.network.NetworkHandler;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.*;
import java.util.List;

/**
 * Created by BeardlessBrady on 2021-02-22 for Currency-Mod
 * All Rights Reserved
 * https://github.com/Beardlessbrady/Currency-Mod
 */
@Mod("gocurrency")
public class GOCurrency {
    public final static String MODID = "gocurrency";
    public static final ItemGroup
            GOC_ITEM_GROUP = new ModItemGroup(MODID, () -> CurrencyItem.getTabItem());

    public static final NetworkHandler NETWORK_HANDLER = new NetworkHandler();

    public GOCurrency() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        NETWORK_HANDLER.register();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        CommonRegistry.init();

        // Register Client Side only events
        final ClientRegistry clientRegistry = new ClientRegistry(MinecraftForge.EVENT_BUS);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> clientRegistry::registerClientOnlyEvents);
    }


    public static CurrencyItem.CurrencyObject[] currencyList;
    public static List<? extends String> currNames;
    public static List<? extends Double> currValues;

    /**
     * @param event
     */
    private void setup(final FMLCommonSetupEvent event) {
        currencyList = new CurrencyItem.CurrencyObject[ConfigHandler.configCurrencyName.get().size()];
        currNames = ConfigHandler.configCurrencyName.get();
        currValues = ConfigHandler.configCurrencyValue.get();
        for (byte i = 0; i < currNames.size(); i++) {
            currencyList[i] = new CurrencyItem.CurrencyObject(i, currNames.get(i), currValues.get(i));
        }

        try {
            GenerateResourcePack.init();
        } catch (IOException e) {
            System.out.println("Good Ol' Currency: Error with creating resource pack - " + e);
        }

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientRegistry.doClientStuff();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        // InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        /*
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
         */
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
       /* @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        } */
    }
}
