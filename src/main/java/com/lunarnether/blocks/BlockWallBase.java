package com.lunarnether.blocks;

import com.lunarnether.Main;
import com.lunarnether.tabs.CreativeTabLunarNether;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BlockWallBase extends BlockWall {

    public BlockWallBase(Block modelBlock, String name) {
        super(modelBlock);
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setCreativeTab(CreativeTabLunarNether.LUNAR_NETHER_TAB);
    }

    // Tüm duvar varyantlarının Creative Tab'da görünmesi için
    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this));
    }
}