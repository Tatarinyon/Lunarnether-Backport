package com.lunarnether.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class NetherHighSkyRenderer {

    private static final ResourceLocation NORTH = new ResourceLocation("lunarnether:textures/environment/skybox_north.png");
    private static final ResourceLocation SOUTH = new ResourceLocation("lunarnether:textures/environment/skybox_south.png");
    private static final ResourceLocation UP = new ResourceLocation("lunarnether:textures/environment/skybox_up.png");
    private static final ResourceLocation DOWN = new ResourceLocation("lunarnether:textures/environment/skybox_down.png");
    private static final ResourceLocation EAST = new ResourceLocation("lunarnether:textures/environment/skybox_east.png");
    private static final ResourceLocation WEST = new ResourceLocation("lunarnether:textures/environment/skybox_west.png");

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        WorldClient world = mc.world;

        // Nether'da ve y=127'nin üzerinde olduğunu kontrol et
        if (world != null && mc.player != null &&
                world.provider.getDimensionType() == DimensionType.NETHER &&
                mc.player.posY > 127) {

            // Skybox'ı çiz
            renderSkybox(event.getPartialTicks(), world, mc);
        }
    }

    private void renderSkybox(float partialTicks, WorldClient world, Minecraft mc) {
        // Oyun durumunu kaydet
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        // Skybox renderlaması için gerekli ayarlar
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        RenderHelper.disableStandardItemLighting();

        // Dünyanın koordinatlarından bağımsız olmalı
        GlStateManager.loadIdentity();

        // Private setupCameraTransform fonksiyonu yerine mc.entityRenderer.setupView kullanılabilir
        // veya kamera rotasyonunu manuel olarak ayarlayabilirsiniz
        float viewerYaw = mc.getRenderViewEntity().rotationYaw;
        float viewerPitch = mc.getRenderViewEntity().rotationPitch;

        GlStateManager.rotate(viewerPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(viewerYaw + 180.0F, 0.0F, 1.0F, 0.0F);

        // Yavaş dönüş için oyun zamanını kullan
        float rotationSpeed = 0.05F;
        float rotation = (world.getWorldTime() % 24000) * rotationSpeed;
        GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        float size = 100.0F;

        // Kuzey yüzü
        mc.getTextureManager().bindTexture(NORTH);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-size, -size, -size).tex(0, 0).endVertex();
        bufferbuilder.pos(-size, size, -size).tex(0, 1).endVertex();
        bufferbuilder.pos(size, size, -size).tex(1, 1).endVertex();
        bufferbuilder.pos(size, -size, -size).tex(1, 0).endVertex();
        tessellator.draw();

        // Güney yüzü
        mc.getTextureManager().bindTexture(SOUTH);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(size, -size, size).tex(0, 0).endVertex();
        bufferbuilder.pos(size, size, size).tex(0, 1).endVertex();
        bufferbuilder.pos(-size, size, size).tex(1, 1).endVertex();
        bufferbuilder.pos(-size, -size, size).tex(1, 0).endVertex();
        tessellator.draw();

        // Üst yüzü
        mc.getTextureManager().bindTexture(UP);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-size, size, -size).tex(0, 0).endVertex();
        bufferbuilder.pos(-size, size, size).tex(0, 1).endVertex();
        bufferbuilder.pos(size, size, size).tex(1, 1).endVertex();
        bufferbuilder.pos(size, size, -size).tex(1, 0).endVertex();
        tessellator.draw();

        // Alt yüzü
        mc.getTextureManager().bindTexture(DOWN);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-size, -size, size).tex(0, 0).endVertex();
        bufferbuilder.pos(-size, -size, -size).tex(0, 1).endVertex();
        bufferbuilder.pos(size, -size, -size).tex(1, 1).endVertex();
        bufferbuilder.pos(size, -size, size).tex(1, 0).endVertex();
        tessellator.draw();

        // Doğu yüzü
        mc.getTextureManager().bindTexture(EAST);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(size, -size, -size).tex(0, 0).endVertex();
        bufferbuilder.pos(size, size, -size).tex(0, 1).endVertex();
        bufferbuilder.pos(size, size, size).tex(1, 1).endVertex();
        bufferbuilder.pos(size, -size, size).tex(1, 0).endVertex();
        tessellator.draw();

        // Batı yüzü
        mc.getTextureManager().bindTexture(WEST);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-size, -size, size).tex(0, 0).endVertex();
        bufferbuilder.pos(-size, size, size).tex(0, 1).endVertex();
        bufferbuilder.pos(-size, size, -size).tex(1, 1).endVertex();
        bufferbuilder.pos(-size, -size, -size).tex(1, 0).endVertex();
        tessellator.draw();

        // Oyun durumunu geri yükle
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}