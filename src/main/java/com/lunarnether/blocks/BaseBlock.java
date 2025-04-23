package com.lunarnether.blocks;

import com.lunarnether.tabs.CreativeTabLunarNether;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;

public class BaseBlock extends Block {

    public BaseBlock(String name, Material material, float hardness, float resistance, String tool, int harvestLevel, SoundType sound) {
        super(material);
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setHarvestLevel(tool, harvestLevel);
        this.setCreativeTab(CreativeTabLunarNether.LUNAR_NETHER_TAB);
        this.setSoundType(sound); // Assign the sound type here
    }
}
