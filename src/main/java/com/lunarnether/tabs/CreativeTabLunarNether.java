package com.lunarnether.tabs;

import com.lunarnether.blocks.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabLunarNether extends CreativeTabs {

    public static final CreativeTabs LUNAR_NETHER_TAB = new CreativeTabLunarNether("lunarnether");

    public CreativeTabLunarNether(String label) {
        super(label);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModBlocks.lunarStone);
    }
}