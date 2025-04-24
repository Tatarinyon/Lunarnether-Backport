package com.lunarnether.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class NetherAmbientSoundHandler {

    // Define your custom sound
    public static final ResourceLocation NETHER_HIGH_AMBIENT = new ResourceLocation("lunarnether", "ambient.nether_high");
    public static SoundEvent NETHER_HIGH_AMBIENT_EVENT;

    private static boolean isSoundPlaying = false;
    private static ISound currentSound = null;

    // Register the sound event
    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        NETHER_HIGH_AMBIENT_EVENT = new SoundEvent(NETHER_HIGH_AMBIENT).setRegistryName(NETHER_HIGH_AMBIENT);
        event.getRegistry().register(NETHER_HIGH_AMBIENT_EVENT);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        // Check if player exists and is in the game
        if (player == null || mc.world == null) {
            stopSound();
            return;
        }

        // Check if player is in nether and above Y 127
        boolean inNether = player.dimension == -1;
        boolean aboveY127 = player.posY >= 127.0;

        if (inNether && aboveY127) {
            // Start playing sound if not already playing
            if (!isSoundPlaying) {
                playHighNetherSound();
            }
        } else {
            // Stop sound if playing and conditions are not met
            if (isSoundPlaying) {
                stopSound();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private static void playHighNetherSound() {
        Minecraft mc = Minecraft.getMinecraft();
        // Create a looping ambient sound
        currentSound = new PositionedSoundRecord(NETHER_HIGH_AMBIENT_EVENT.getSoundName(),
                SoundCategory.AMBIENT, 1.0F, 1.0F, true, 0, ISound.AttenuationType.NONE,
                0, 0, 0);

        mc.getSoundHandler().playSound(currentSound);
        isSoundPlaying = true;
    }

    @SideOnly(Side.CLIENT)
    private static void stopSound() {
        if (currentSound != null) {
            Minecraft.getMinecraft().getSoundHandler().stopSound(currentSound);
            currentSound = null;
            isSoundPlaying = false;
        }
    }
}