package com.lunarnether;

import com.lunarnether.client.DimensionChangeHandler;
import com.lunarnether.common.WorldProviderLunarNether;
import com.lunarnether.mobspawn.NetherSpawnLimiter;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixins;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {

    public static final String MODID = "lunarnether";
    public static final String NAME = "Lunar Nether";
    public static final String VERSION = "1.0";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info(NAME + " preInit aşamasında.");

        // Mixin'leri yükle
        Mixins.addConfiguration("mixins.example.json");
        logger.info("Lunar Nether Mixin'leri yüklendi.");

        // Nether boyutunu özelleştirilmiş WorldProvider ile değiştir
        try {
            // Önce mevcut Nether boyutunu kaldır
            DimensionManager.unregisterDimension(-1);

            // Sonra özelleştirilmiş WorldProvider ile kaydet
            DimensionType netherType = DimensionType.register(
                    "LunarNether",
                    "_lunar_nether",
                    -1,
                    WorldProviderLunarNether.class,
                    true
            );
            DimensionManager.registerDimension(-1, netherType);

            logger.info("Özelleştirilmiş Nether WorldProvider başarıyla kaydedildi!");
        } catch (Exception e) {
            logger.error("Nether WorldProvider kaydedilirken hata oluştu:", e);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Mob spawn limiter'ı kaydet
        MinecraftForge.EVENT_BUS.register(new NetherSpawnLimiter());

        // Client tarafı işlemleri
        if (event.getSide() == Side.CLIENT) {
            registerClientHandlers();
        }

        logger.info(NAME + " init aşamasında yükleniyor.");
    }

    @SideOnly(Side.CLIENT)
    private void registerClientHandlers() {
        // Dimension değişikliklerini izleyen handler'ı kaydet
        MinecraftForge.EVENT_BUS.register(new DimensionChangeHandler());
        logger.info("Nether Skybox Renderer kaydedildi.");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info(NAME + " postInit aşamasında.");
        logger.info(NAME + " " + VERSION + " başarıyla yüklendi!");

    }
}