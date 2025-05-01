package com.lunarnether.items;

import net.minecraft.item.Item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

public class BerylliumIngot extends Item {

    public BerylliumIngot() {
        // Mod id’niz "lunarnether" ise:
        this.setRegistryName(new ResourceLocation("lunarnether", "beryllium_ingot"));
        this.setTranslationKey("beryllium_ingot");
        setCreativeTab(CreativeTabs.MATERIALS); // Veya modunuza ait başka bir creative tab
    }
}
