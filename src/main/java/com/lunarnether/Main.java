package com.lunarnether;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import com.lunarnether.blocks.ModBlocks;
import com.lunarnether.tabs.CreativeTabLunarNether;

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
        logger.info(NAME + " is initializing!");
    }
}