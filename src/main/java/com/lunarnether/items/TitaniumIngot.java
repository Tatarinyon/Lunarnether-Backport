package com.lunarnether.items;

import net.minecraft.item.Item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

public class TitaniumIngot extends Item {

    public TitaniumIngot() {
        // Mod id’niz "lunarnether" ise:
        this.setRegistryName(new ResourceLocation("lunarnether", "titanium_ingot"));
        this.setTranslationKey("titanium_ingot");
        setCreativeTab(CreativeTabs.MATERIALS); // Veya modunuza ait başka bir creative tab
    }
}
