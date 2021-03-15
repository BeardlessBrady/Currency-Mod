package com.beardlessbrady.gocurrency.init;

import com.beardlessbrady.gocurrency.GOCurrency;
import com.beardlessbrady.gocurrency.blocks.vending.VendingBlock;
import com.beardlessbrady.gocurrency.blocks.vending.VendingContainer;
import com.beardlessbrady.gocurrency.blocks.vending.VendingTile;
import com.beardlessbrady.gocurrency.items.CurrencyItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by BeardlessBrady on 2021-02-22 for Currency-Mod
 * All Rights Reserved
 * https://github.com/Beardlessbrady/Currency-Mod
 */
public class CommonRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GOCurrency.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GOCurrency.MODID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, GOCurrency.MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, GOCurrency.MODID);

    // Items
    public static final RegistryObject<Item> ITEM_CURRENCY = ITEMS.register("currency", () ->
            new CurrencyItem(new Item.Properties().group(GOCurrency.GOC_ITEM_GROUP)));

    // Blocks
    public static final RegistryObject<Block> BLOCK_VENDING = BLOCKS.register("vending", () ->
            new VendingBlock(Block.Properties.create(Material.ROCK)));

    // ItemBlocks
    public static final RegistryObject<Item> BLOCKITEM_VENDING = ITEMS.register("vending", () ->
            new BlockItem(BLOCK_VENDING.get(), new Item.Properties().group(GOCurrency.GOC_ITEM_GROUP)));

    // Tiles
    public static final RegistryObject<TileEntityType<VendingTile>> TILE_VENDING = TILES.register("vending_te", () ->
            TileEntityType.Builder.create(VendingTile::new, BLOCK_VENDING.get()).build(null));

    // Containers
    public static final RegistryObject<ContainerType<VendingContainer>> CONTAINER_VENDING = CONTAINERS.register("vending_con", () ->
            IForgeContainerType.create(VendingContainer::createContainerClient));

    /**
     * Initializes registry
     */
    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
