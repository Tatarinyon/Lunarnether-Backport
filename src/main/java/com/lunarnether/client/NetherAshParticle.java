package com.lunarnether.client;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public class NetherAshParticle extends Particle {

    public NetherAshParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn,
                             double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        // Set longer lifetime for slower falling effect
        this.particleMaxAge = 200;  // Around 10 seconds (20 ticks = 1 second)

        // Make particles smaller
        this.particleScale = 0.8F;

        // Set motion values for slower falling
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;  // Already set to fall slower (-0.05 instead of -0.2)
        this.motionZ = zSpeedIn;

        // Add slight random motion for more natural movement
        this.motionX += (Math.random() - 0.5) * 0.01;
        this.motionZ += (Math.random() - 0.5) * 0.01;

        // Allow particles to collide with blocks
        this.canCollide = true;

        // Set particle colors to be gray
        float grayShade = rand.nextFloat() * 0.2F + 0.6F;  // between 0.6 and 0.8 for medium-dark gray
        this.particleRed = grayShade;
        this.particleGreen = grayShade;
        this.particleBlue = grayShade;
    }

    @Override
    public int getFXLayer() {
        // Use the default particle texture atlas
        return 0;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        // Gradually fade out as the particle ages
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }

        // Add slight wobble for more natural falling
        if (this.particleAge % 10 == 0) {
            this.motionX += (Math.random() - 0.5) * 0.01;
            this.motionZ += (Math.random() - 0.5) * 0.01;
        }

        // Apply gentle gravity effect
        this.motionY -= 0.001;

        // Apply movement
        this.move(this.motionX, this.motionY, this.motionZ);

        // Slow down particles if they hit something
        if (this.onGround) {
            this.motionX *= 0.7;
            this.motionZ *= 0.7;
        }
    }

    // Should this particle be affected by light
    @Override
    public boolean shouldDisableDepth() {
        return false;
    }
}