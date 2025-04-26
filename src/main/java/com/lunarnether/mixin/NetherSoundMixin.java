package com.lunarnether.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeHell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class NetherSoundMixin {

    private static final int MAX_GHAST_SOUND_HEIGHT = 126; // Bu yükseklikten sonra ghast sesleri kesilecek

    /**
     * World'daki ses oynatma fonksiyonuna injection yaparak,
     * belirli bir yüksekliğin üzerinde Ghast seslerini engeller.
     */
    @Inject(method = "playSound(Lnet/minecraft/entity/player/EntityPlayer;DDDLnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FF)V",
            at = @At("HEAD"),
            cancellable = true)
    private void preventGhastSoundsAboveHeight(EntityPlayer player, double x, double y, double z,
                                               SoundEvent soundIn, SoundCategory category,
                                               float volume, float pitch, CallbackInfo ci) {
        World world = (World)(Object)this;

        // Sadece Nether'da işlem yapıyoruz
        if (world.provider.getDimension() == -1) {
            // Eğer ses konumu 127'den yüksekse ve ses kategorisi HOSTILE veya AMBIENT ise
            if (y > MAX_GHAST_SOUND_HEIGHT) {
                // Ghast seslerini kontrol et (ses olayının registryName'inde "ghast" geçiyorsa)
                String soundName = soundIn.getRegistryName().toString();
                if (soundName.contains("ghast") || soundName.contains("entity.ghast")) {
                    ci.cancel(); // Sesi iptal et, oynatma
                }
            }
        }
    }

}