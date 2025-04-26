package com.lunarnether.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class NetherFogMixin {

    @Inject(method = "setupFog", at = @At("HEAD"))
    private void modifyNetherFog(int fogMode, float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();

        if (entity != null && entity.world.provider.getDimension() == -1 && entity.posY >= 127.0D) {
            // Sisi devre dışı bırak ama metodu iptal etme
            GlStateManager.disableFog();
            // ci.cancel() satırını kaldırdık - böylece meteod devam edecek
        }
    }

    // Alternatif yaklaşım: RETURN noktasında inject yaparak orijinal işlemi yapmasına izin ver
    // ama sonra sisi tekrar kapat
    @Inject(method = "setupFog", at = @At("RETURN"))
    private void ensureNoFogInHighNether(int fogMode, float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();

        if (entity != null && entity.world.provider.getDimension() == -1 && entity.posY >= 127.0D) {
            // Metodun sonunda sisi tekrar devre dışı bırak
            // (eğer metod içinde tekrar aktif edildiyse)
            GlStateManager.disableFog();
        }
    }
}