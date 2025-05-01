package com.lunarnether.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;

public class NetherSkyRenderer extends IRenderHandler {
    private static final ResourceLocation NORTH = new ResourceLocation("lunarnether:environment/sky1_north.png");
    private static final ResourceLocation SOUTH = new ResourceLocation("lunarnether:environment/sky1_south.png");
    private static final ResourceLocation EAST = new ResourceLocation("lunarnether:environment/sky1_east.png");
    private static final ResourceLocation WEST = new ResourceLocation("lunarnether:environment/sky1_west.png");
    private static final ResourceLocation UP = new ResourceLocation("lunarnether:environment/sky1_up.png");
    private static final ResourceLocation DOWN = new ResourceLocation("lunarnether:environment/sky1_down.png");

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        // Nether'da olduğumuzu ve oyuncunun var olduğunu kontrol et
        if (mc.player == null || world.provider.getDimension() != -1) {
            return; // Nether değilse veya oyuncu yoksa çizme
        }

        // Y koordinatı 127'den büyükse skybox'ı çiz
        if (mc.player.posY > 127.0) {
            renderCustomSkybox(partialTicks, world, mc);
        }
    }
    public float getCurrentLightLevel(WorldClient world) {
        // Dünya zamanını al
        long worldTime = world.getWorldTime();
        float dayProgress = (float)(worldTime % 24000L) / 24000.0F;

        // Rotasyon açısı
        float rotationAngle = dayProgress * 360.0F;

        // Aydınlatma seviyesini hesapla
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
    private void renderCustomSkybox(float partialTicks, WorldClient world, Minecraft mc) {
        // OpenGL durumlarını hazırla
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask(false);

        // Z-buffer kullanımını devre dışı bırak
        GlStateManager.disableDepth();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        // Skybox büyüklüğü
        float size = 100.0F;

        // Dünya zamanını kullanarak skybox rotasyonu hesapla
        long worldTime = world.getWorldTime();
        float dayProgress = (float)(worldTime % 24000L) / 24000.0F;

        // Rotasyon açısı (X ekseni etrafında dönüş için)
        float rotationAngle = dayProgress * 360.0F;

        // Aydınlatma seviyesini hesapla (0-180 derece arasında aydınlık, 180-360 arasında karanlık)
        float lightLevel;
        if (rotationAngle <= 180.0F) {
            // 0-180 derece arası: karanlıktan aydınlığa geçiş
            lightLevel = rotationAngle / 180.0F;  // 0'dan 1'e
        } else {
            // 180-360 derece arası: aydınlıktan karanlığa geçiş
            lightLevel = (360.0F - rotationAngle) / 180.0F;  // 1'den 0'a
        }

        // Aydınlatma seviyesini uygula
        float ambientLight = 0.5F + (lightLevel * 1.0F);  // Minimum 0.2, maksimum 1.0
        GlStateManager.color(ambientLight, ambientLight, ambientLight, 1.0F);

        // Ana skybox matrisi
        GlStateManager.pushMatrix();

        // Skybox'ı X ekseni etrafında döndür (dikey dönüş)
        GlStateManager.rotate(rotationAngle, 1.0F, 0.0F, 0.0F);

        // Yüzleri çiz
        for (int i = 0; i < 6; ++i) {
            GlStateManager.pushMatrix();

            // Dönüş ve çizim için yüze göre ayarlar
            switch (i) {
                case 0: // NORTH
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(NORTH);
                    break;
                case 1: // SOUTH
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(SOUTH);
                    break;
                case 2: // UP
                    GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(UP);
                    break;
                case 3: // DOWN
                    GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(DOWN);
                    break;
                case 4: // EAST
                    GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(EAST);
                    break;
                case 5: // WEST
                    GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(WEST);
                    break;
            }

            // Kareyi çiz
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(-size, -size, -size).tex(0.0D, 0.0D).endVertex();
            bufferbuilder.pos(-size, -size, size).tex(0.0D, 1.0D).endVertex();
            bufferbuilder.pos(size, -size, size).tex(1.0D, 1.0D).endVertex();
            bufferbuilder.pos(size, -size, -size).tex(1.0D, 0.0D).endVertex();
            tessellator.draw();

            GlStateManager.popMatrix();
        }

        // Skybox matrisini kapat
        GlStateManager.popMatrix();

        // OpenGL durumlarını geri yükle
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();

        // Renk durumunu normal hale getir
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}