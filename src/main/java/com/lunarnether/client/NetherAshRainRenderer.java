package com.lunarnether.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = "lunarnether")
public class NetherAshRainRenderer {

    private static final int PARTICLE_SPAWN_RATE = 2; // Lower number for less frequent particles
    private static int tickCount = 0;

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        World world = event.world;

        // Only run in the Nether (dimension -1)
        if (world.provider.getDimension() == -1) {
            Minecraft mc = Minecraft.getMinecraft();

            // Check for player object
            if (mc.player == null)
                return;

            // Only spawn particles every few ticks for slower rate
            tickCount++;
            if (tickCount < PARTICLE_SPAWN_RATE) {
                return;
            }
            tickCount = 0;

            ParticleManager particleManager = mc.effectRenderer;

            // If player is above Y level 127
            if (mc.player.posY >= 127) {
                // Reduced number of particles (5 instead of 20)
                for (int i = 0; i < 5; i++) {
                    double x = mc.player.posX + (Math.random() - 0.5) * 20.0; // Wider area
                    double y = mc.player.posY + Math.random() * 15.0;
                    double z = mc.player.posZ + (Math.random() - 0.5) * 20.0; // Wider area

                    // Create and add the new lava particle effect with slower falling speed
                    particleManager.addEffect(new NetherAshParticle(world, x, y, z, 0.0, -0.05, 0.0));
                }
            }
        }
    }
}