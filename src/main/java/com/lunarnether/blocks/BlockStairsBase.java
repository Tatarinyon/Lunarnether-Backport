package com.lunarnether.blocks;

import com.lunarnether.Main;
import com.lunarnether.tabs.CreativeTabLunarNether;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class BlockStairsBase extends BlockStairs {

    public BlockStairsBase(IBlockState modelState, String name) {
        super(modelState);
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setCreativeTab(CreativeTabLunarNether.LUNAR_NETHER_TAB);
        this.useNeighborBrightness = true; // Merdivenler için parlaklık ayarı
    }
}