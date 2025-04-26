package com.lunarnether.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lunarnether.blocks.ModBlocks;

@Mixin(net.minecraft.world.gen.ChunkGeneratorHell.class)
public class NetherBedrockReplacerMixin {
    private static final int BEDROCK_CEILING_START = 127; // Tavan bedrock'un başladığı yükseklik
    private static final int BEDROCK_CEILING_END = 123; // Tavan bedrock'un bitebileceği en düşük yükseklik

    @Inject(method = "buildSurfaces", at = @At("RETURN"))
    private void replaceNetherCeilingBedrock(int chunkX, int chunkZ, ChunkPrimer primer, CallbackInfo ci) {
        IBlockState astralRock = ModBlocks.astralrock.getDefaultState();

        // Chunk'taki her bloğu kontrol et
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Tavan bölgesini kontrol et (yukarıdan aşağıya)
                for (int y = BEDROCK_CEILING_START; y >= BEDROCK_CEILING_END; y--) {
                    IBlockState currentBlock = primer.getBlockState(x, y, z);

                    // Eğer blok bedrock ise değiştir
                    if (currentBlock.getBlock() == Blocks.BEDROCK) {
                        primer.setBlockState(x, y, z, astralRock);
                    }
                }
            }
        }
    }
}