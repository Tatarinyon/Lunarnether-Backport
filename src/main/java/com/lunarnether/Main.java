package com.lunarnether;

import com.lunarnether.mobspawn.NetherSpawnLimiter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {

    public static final String MODID = "lunarnether";
    public static final String NAME = "Lunar Nether";
    public static final String VERSION = "1.0";

    public static Logger logger;

    /**
     * Ön Yükleme (Pre-Initialization) aşaması.
     * Logger burada ayarlanır.
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info(NAME + " preInit aşamasında.");
    }

    /**
     * Yükleme (Initialization) aşaması.
     * Olaylar burada kaydedilir.
     */
    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new NetherSpawnLimiter());
        logger.info(NAME + " init aşamasında yükleniyor.");
    }
}
