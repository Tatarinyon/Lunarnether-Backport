package com.lunarnether.client;

import com.lunarnether.Main;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DimensionChangeHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    public void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld().isRemote) {
            checkAndSetRenderer();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        checkAndSetRenderer();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            checkAndSetRenderer();
        }
    }

    @SideOnly(Side.CLIENT)
    private void checkAndSetRenderer() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world != null && mc.world.provider.getDimension() == -1) {
            // Nether boyutundayız, renderer'ı ayarla
            if (mc.world.provider.getSkyRenderer() == null ||
                    !(mc.world.provider.getSkyRenderer() instanceof NetherSkyRenderer)) {
                mc.world.provider.setSkyRenderer(new NetherSkyRenderer());
                Main.logger.info("Nether Skybox Renderer uygulandı.");
            }
        }
    }
}