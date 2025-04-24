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

    /**
     * Bu mixin, EntityRenderer sınıfındaki setupFog metodunun başında inject yapar.
     * Eğer oyuncu Nether dünyasında (-1) ve Y koordinatı 127 veya üstündeyse,
     * OpenGL’deki sis render ayarları (GlStateManager.disableFog() çağrısı)
     * yapılarak sis tamamen devre dışı bırakılır ve metodun devamı iptal edilir.
     *
     * @param fogMode      Sis modu (render ayarlarında kullanılan parametre)
     * @param partialTicks Kısmi tick zamanı
     * @param ci           Callback info, metod akışını iptal etmek için kullanılır
     */
    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private void removeNetherFog(int fogMode, float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();

        if (entity != null && entity.world.provider.getDimension() == -1 && entity.posY >= 127.0D) {
            // Nether’da yüksek bölgede olduğumuz için sis efektini kapatıyoruz:
            GlStateManager.disableFog();
            // Metodun geri kalanının çalışmasını iptal ediyoruz
            ci.cancel();
        }
    }
}
