package com.lunarnether.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.lunarnether.blocks.ModBlocks;
import java.util.Random;

@Mixin(value = net.minecraft.world.gen.ChunkGeneratorHell.class)
public class NetherGenMixin {
    // Arazi yüksekliği parametreleri
    private static final int TERRAIN_MIN_HEIGHT = 127; // Bedrock seviyesi
    private static final int FLAT_TERRAIN_HEIGHT = 140; // Düz arazi yüksekliği
    private static final int MAX_HEIGHT = 256; // Maksimum yükseklik

    // Global parametreler
    private static final long WORLD_SEED = 83749837L;
    private static final double PERLIN_SCALE = 0.25; // Daha yumuşak tepeler için daha düşük değer
    private static final double PERLIN_AMPLITUDE = 0.25; // Biraz daha yüksek tepeler için artırıldı

    @Inject(method = "prepareHeights", at = @At("RETURN"))
    private void generateGrassTerrain(int chunkX, int chunkZ, ChunkPrimer primer, CallbackInfo ci) {
        IBlockState grass = Blocks.GRASS.getDefaultState();
        IBlockState dirt  = Blocks.DIRT.getDefaultState();

        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        // Daha geniş bir smoothing alanı için border'ı artırıyoruz
        int border = 4; // 2'den 4'e çıkarıldı
        int gridSize = 16 + 2 * border; // 24x24 grid

        double[][] noiseGrid = new double[gridSize][gridSize];

        // Daha yumuşak noise için octave değerini artırıyoruz
        NoiseGeneratorPerlin perlin = new NoiseGeneratorPerlin(new Random(WORLD_SEED), 6); // 4'ten 6'ya çıkarıldı

        // Grid'i doldurma
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int worldX = baseX + i - border;
                int worldZ = baseZ + j - border;
                // İkinci bir noise katmanı ekleyerek daha karmaşık arazi elde ediyoruz
                double noiseValue1 = perlin.getValue(worldX * PERLIN_SCALE, worldZ * PERLIN_SCALE);
                double noiseValue2 = perlin.getValue(worldX * PERLIN_SCALE * 2, worldZ * PERLIN_SCALE * 2) * 0.5;
                noiseGrid[i][j] = (noiseValue1 + noiseValue2) * PERLIN_AMPLITUDE;
            }
        }

        // Daha fazla smoothing geçişi
        for (int pass = 0; pass < 3; pass++) { // 2'den 3'e çıkarıldı
            double[][] smoothedGrid = new double[gridSize][gridSize];
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    double sum = 0;
                    double weightSum = 0;
                    // Daha geniş bir kernel için daha fazla smoothing
                    for (int dx = -3; dx <= 3; dx++) { // -2/+2'den -3/+3'e genişletildi
                        for (int dz = -3; dz <= 3; dz++) {
                            int ni = i + dx, nj = j + dz;
                            if (ni >= 0 && ni < gridSize && nj >= 0 && nj < gridSize) {
                                // Yumuşatılmış ağırlık fonksiyonu
                                double dist = Math.sqrt(dx*dx + dz*dz);
                                double weight = 1.0 / (dist + 1);
                                sum += noiseGrid[ni][nj] * weight;
                                weightSum += weight;
                            }
                        }
                    }
                    smoothedGrid[i][j] = sum / weightSum;
                }
            }
            noiseGrid = smoothedGrid;
        }

        // Merkezde kalan 16x16'lık bölüm
        double[][] heightMap = new double[16][16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                heightMap[x][z] = noiseGrid[x + border][z + border];
            }
        }

        // Arazi oluşturma mantığı
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Düz arazi tabanı (127-140 arası)
                for (int y = TERRAIN_MIN_HEIGHT; y <= FLAT_TERRAIN_HEIGHT; y++) {
                    IBlockState currentBlock = primer.getBlockState(x, y, z);
                    if (currentBlock.getBlock() == Blocks.AIR) {
                        primer.setBlockState(x, y, z, dirt);
                    }
                }

                // Yükseklik değeri hesaplama
                double noiseValue = heightMap[x][z];

                // Yumuşak geçişli tepeler için sigmoid benzeri bir fonksiyon kullanıyoruz
                // Bu, çok yüksek ve çok düşük değerlerin etkisini azaltır
                double modifier = Math.tanh(noiseValue * 0.4) * 0.7 + 0.3;
                int additionalHeight = (int) Math.round(noiseValue * modifier);

                // Negatif değerler için de aynı şeyi yapıyoruz (vadiler)
                if (additionalHeight < 0) {
                    additionalHeight = Math.max(-3, additionalHeight); // En fazla 3 blok aşağı
                }

                int finalHeight = FLAT_TERRAIN_HEIGHT + additionalHeight;
                finalHeight = Math.min(finalHeight, MAX_HEIGHT); // Üst sınır
                finalHeight = Math.max(finalHeight, FLAT_TERRAIN_HEIGHT - 3); // Alt sınır

                // Arazi yüksekliğine göre blokları yerleştirme
                if (finalHeight > FLAT_TERRAIN_HEIGHT) {
                    // Yükselti
                    for (int y = FLAT_TERRAIN_HEIGHT + 1; y <= finalHeight; y++) {
                        IBlockState currentBlock = primer.getBlockState(x, y, z);
                        if (currentBlock.getBlock() == Blocks.AIR) {
                            if (y == finalHeight) {
                                primer.setBlockState(x, y, z, grass); // En üst çim
                            } else {
                                primer.setBlockState(x, y, z, dirt); // İçerisi toprak
                            }
                        }
                    }
                } else if (finalHeight < FLAT_TERRAIN_HEIGHT) {
                    // Çukur - üst kısımdaki bazı blokları kaldır
                    for (int y = finalHeight + 1; y <= FLAT_TERRAIN_HEIGHT; y++) {
                        primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
                    }
                }

                // 140 seviyesindeki (düz arazi üstündeki) blokları çim yap
                if (finalHeight == FLAT_TERRAIN_HEIGHT) {
                    primer.setBlockState(x, finalHeight, z, grass);
                }
            }
        }

        // İleri düzey smoothing ve temizleme işlemleri
        // Asılı toprak bloklarını kaldırma
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = TERRAIN_MIN_HEIGHT + 1; y < MAX_HEIGHT; y++) {
                    IBlockState current = primer.getBlockState(x, y, z);
                    if (current.getBlock() == Blocks.GRASS || current.getBlock() == Blocks.DIRT) {
                        IBlockState below = primer.getBlockState(x, y - 1, z);
                        if (below.getBlock() == Blocks.AIR) {
                            // Altında hava olan bir blok var, komşularını kontrol et
                            int supportCount = 0;
                            if (x > 0 && primer.getBlockState(x - 1, y, z).getBlock() != Blocks.AIR) supportCount++;
                            if (x < 15 && primer.getBlockState(x + 1, y, z).getBlock() != Blocks.AIR) supportCount++;
                            if (z > 0 && primer.getBlockState(x, y, z - 1).getBlock() != Blocks.AIR) supportCount++;
                            if (z < 15 && primer.getBlockState(x, y, z + 1).getBlock() != Blocks.AIR) supportCount++;

                            // Yeterince komşu destek yoksa kaldır
                            if (supportCount < 2) {
                                primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
                            }
                        }
                    }
                }
            }
        }

        // Küçük havza ve göl benzeri alanlar ekleme (vadiler oluşturmak için)
        for (int x = 1; x < 15; x++) {
            for (int z = 1; z < 15; z++) {
                for (int y = FLAT_TERRAIN_HEIGHT - 3; y <= FLAT_TERRAIN_HEIGHT; y++) {
                    IBlockState current = primer.getBlockState(x, y, z);
                    if (current.getBlock() == Blocks.AIR) {
                        int solidNeighbors = 0;
                        if (primer.getBlockState(x + 1, y, z).getBlock() != Blocks.AIR) solidNeighbors++;
                        if (primer.getBlockState(x - 1, y, z).getBlock() != Blocks.AIR) solidNeighbors++;
                        if (primer.getBlockState(x, y, z + 1).getBlock() != Blocks.AIR) solidNeighbors++;
                        if (primer.getBlockState(x, y, z - 1).getBlock() != Blocks.AIR) solidNeighbors++;

                        // Çok sayıda komşu blok varsa ve muhtemelen bir çukurdaysa, blokla doldur
                        if (solidNeighbors >= 3 && y < FLAT_TERRAIN_HEIGHT) {
                            primer.setBlockState(x, y, z, dirt);
                        }
                    }
                }
            }
        }

        // Üstteki çim bloklarını son bir kez düzelt
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = TERRAIN_MIN_HEIGHT + 1; y < MAX_HEIGHT; y++) {
                    IBlockState current = primer.getBlockState(x, y, z);
                    if (current.getBlock() == Blocks.DIRT) {
                        IBlockState above = y < MAX_HEIGHT - 1 ? primer.getBlockState(x, y + 1, z) : null;
                        if (above != null && above.getBlock() == Blocks.AIR) {
                            primer.setBlockState(x, y, z, grass); // Üstü açık olan toprak bloklarını çime çevir
                        }
                    }
                }
            }
        }
    }
}