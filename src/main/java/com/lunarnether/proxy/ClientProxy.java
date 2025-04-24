package com.lunarnether.proxy;

import com.lunarnether.client.NetherHighSkyRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        // Client tarafı preInit işlemleri
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        // Nether skybox event handler'ını kaydedelim
        MinecraftForge.EVENT_BUS.register(new NetherHighSkyRenderer());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        // Client tarafı postInit işlemleri
    }
}