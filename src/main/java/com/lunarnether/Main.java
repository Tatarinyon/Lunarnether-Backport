package com.lunarnether;

import com.lunarnether.client.NetherFogHandler;
import com.lunarnether.mobspawn.NetherSpawnLimiter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;


@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
    public static final String MODID = "lunarnether";
    public static final String NAME = "Lunar Nether";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        // Creative tab will be initialized when referenced
        // ModBlocks.init() will be called by the event system
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Mob spawn limiter'ı kaydet
        MinecraftForge.EVENT_BUS.register(new NetherSpawnLimiter());

        // Client tarafı için sis handler'ı kaydet
        if (event.getSide() == Side.CLIENT) {
            registerClientEvents();
        }

        logger.info(NAME + " is initializing!");
    }

    @SideOnly(Side.CLIENT)
    private void registerClientEvents() {
        MinecraftForge.EVENT_BUS.register(new NetherFogHandler());
    }
}