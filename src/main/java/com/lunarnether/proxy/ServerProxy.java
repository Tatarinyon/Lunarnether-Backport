package com.lunarnether.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        // Server tarafı preInit işlemleri
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        // Server tarafı init işlemleri
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        // Server tarafı postInit işlemleri
    }
}