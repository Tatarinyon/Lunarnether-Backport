package com.lunarnether.common;

import com.lunarnether.client.NetherSkyRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.WorldProviderHell;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderLunarNether extends WorldProviderHell {
    private NetherSkyRenderer skyRenderer;

    public WorldProviderLunarNether() {
        super();
        skyRenderer = new NetherSkyRenderer();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public net.minecraftforge.client.IRenderHandler getSkyRenderer() {
        return skyRenderer;
    }

    @Override
    public float getSunBrightness(float partialTicks) {
        // Client tarafında ve Nether boyutunda isek
        if (world.isRemote && world.provider.getDimension() == -1) {
            // Y koordinatı 127'den büyükse özel aydınlatma kullan
            if (Minecraft.getMinecraft().player != null &&
                    Minecraft.getMinecraft().player.posY > 127.0) {
                // NetherSkyRenderer'dan light level'ı al veya hesapla
                float lightLevel = calculateLightLevel();
                return 0.2F + (lightLevel * 0.8F);
            }
        }
        // Varsayılan Nether aydınlatması
        return super.getSunBrightness(partialTicks);
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        if (world.isRemote && world.provider.getDimension() == -1) {
            // Y koordinatı 127'den büyükse özel gök açısı kullan
            if (Minecraft.getMinecraft().player != null &&
                    Minecraft.getMinecraft().player.posY > 127.0) {
                float dayProgress = (float)(worldTime % 24000L) / 24000.0F;
                return dayProgress;
            }
        }
        // Varsayılan Nether gök açısı
        return super.calculateCelestialAngle(worldTime, partialTicks);
    }

    @Override
    public boolean isDaytime() {
        if (world.isRemote && world.provider.getDimension() == -1) {
            // Y koordinatı 127'den büyükse özel gündüz kontrolü
            if (Minecraft.getMinecraft().player != null &&
                    Minecraft.getMinecraft().player.posY > 127.0) {
                long worldTime = world.getWorldTime();
                float dayProgress = (float)(worldTime % 24000L) / 24000.0F;
                float rotationAngle = dayProgress * 360.0F;
                return rotationAngle > 90.0F && rotationAngle < 270.0F;
            }
        }
        // Varsayılan Nether gündüz kontrolü
        return super.isDaytime();
    }

    // Aydınlatma seviyesini hesapla
    private float calculateLightLevel() {
        long worldTime = world.getWorldTime();
        float dayProgress = (float)(worldTime % 24000L) / 24000.0F;
        float rotationAngle = dayProgress * 360.0F;

        float lightLevel;
        if (rotationAngle <= 180.0F) {
            // 0-180 derece arası: karanlıktan aydınlığa geçiş
            lightLevel = rotationAngle / 180.0F;  // 0'dan 1'e
        } else {
            // 180-360 derece arası: aydınlıktan karanlığa geçiş
            lightLevel = (360.0F - rotationAngle) / 180.0F;  // 1'den 0'a
        }

        return lightLevel;
    }
}