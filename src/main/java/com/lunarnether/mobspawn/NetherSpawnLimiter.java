package com.lunarnether.mobspawn;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetherSpawnLimiter {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        Entity entity = event.getEntity();
        World world = entity.world;

        // Nether boyutunu kontrol et (Nether dimension ID'si -1'dir)
        if (world.provider.getDimension() == -1) {
            // Y koordinatı 127'den yüksek mi kontrol et
            if (entity.posY > 127.0) {
                // Mob spawn'ını engelle
                event.setResult(Result.DENY);
                if (!world.isRemote && world.rand.nextInt(100) == 0) {
                    LOGGER.debug("Nether'da Y:127 üzerinde mob spawn engellendi: " + entity.getName());
                }
            }
        }
    }
}