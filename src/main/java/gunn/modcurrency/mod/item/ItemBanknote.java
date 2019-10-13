package gunn.modcurrency.mod.item;

import gunn.modcurrency.mod.ModConfig;
import gunn.modcurrency.mod.ModCurrency;
import gunn.modcurrency.mod.TabCurrency;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Distributed with the Currency-Mod for Minecraft.
 * Copyright (C) 2016  Brady Gunn
 *
 * File Created on 2016-10-28.
 */
public class ItemBanknote extends Item {
    public static final int noteLength = 6;

    public ItemBanknote() {
        setHasSubtypes(true);
        setRegistryName("banknote");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        switch (ModConfig.textureType) {
            default:
            case 0:
                for (int i = 0; i < noteLength; i++) {
                    ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(getRegistryName() + "_" + i, "inventory"));
                }
                break;
            case 1:
                for (int i = 0; i < noteLength; i++) {
                    ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(getRegistryName() + "fool" + "_" + i, "inventory"));
                }
                break;
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + this.getRegistryName().toString() + "_" + stack.getItemDamage();
    }

    @Override
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
        if(isInCreativeTab(itemIn)){
            for (int i = 1; i < noteLength; i++) {
                ItemStack stack = new ItemStack(this, 1, i);
                tab.add(stack);
            }
        }
    }
}