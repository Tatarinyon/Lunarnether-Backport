package com.lunarnether.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        // Server ve client için ortak ön yükleme işlemleri
    }

    public void init(FMLInitializationEvent event) {
        // Server ve client için ortak yükleme işlemleri
    }

    public void postInit(FMLPostInitializationEvent event) {
        // Server ve client için ortak son yükleme işlemleri
    }
}