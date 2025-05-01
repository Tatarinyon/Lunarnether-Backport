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

    // Krater parametreleri
    private static final double CRATER_CHANCE = 0.007; // Her blok için krater oluşma şansı
    private static final int MIN_CRATER_RADIUS = 3; // En küçük krater yarıçapı
    private static final int MAX_CRATER_RADIUS = 12; // En büyük krater yarıçapı
    private static final double beryllium_CHANCE = 0.3; // Kraterlerde beryllium oluşma şansı
    private static final double beryllium_VEIN_CHANCE = 0.05; // Normal arazide beryllium damarı oluşma şansı

    // Yeni üretilen Random nesnesi
    private static final Random randomGenerator = new Random(WORLD_SEED);

    @Inject(method = "prepareHeights", at = @At("RETURN"))
    private void generateGrassTerrain(int chunkX, int chunkZ, ChunkPrimer primer, CallbackInfo ci) {
        // Özel blokları tanımlıyoruz - doğru değişken adlarıyla
        IBlockState lunarDust = ModBlocks.lunarDust.getDefaultState();
        IBlockState lunarStone = ModBlocks.lunarStone.getDefaultState();
        IBlockState berylliumOre = ModBlocks.berylliumOre.getDefaultState();

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
                                double dist = Math.sqrt(dx * dx + dz * dz);
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
                        primer.setBlockState(x, y, z, lunarStone);
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
                                primer.setBlockState(x, y, z, lunarDust); // En üst Lunar Dust
                            } else {
                                primer.setBlockState(x, y, z, lunarStone); // İçerisi Lunar Stone
                            }
                        }
                    }
                } else if (finalHeight < FLAT_TERRAIN_HEIGHT) {
                    // Çukur - üst kısımdaki bazı blokları kaldır
                    for (int y = finalHeight + 1; y <= FLAT_TERRAIN_HEIGHT; y++) {
                        primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
                    }
                }

                // 140 seviyesindeki (düz arazi üstündeki) blokları Lunar Dust yap
                if (finalHeight == FLAT_TERRAIN_HEIGHT) {
                    primer.setBlockState(x, finalHeight, z, lunarDust);
                }
            }
        }

        // İleri düzey smoothing ve temizleme işlemleri
        // Asılı toprak bloklarını kaldırma
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = TERRAIN_MIN_HEIGHT + 1; y < MAX_HEIGHT; y++) {
                    IBlockState current = primer.getBlockState(x, y, z);
                    if (current.getBlock() == ModBlocks.lunarDust || current.getBlock() == ModBlocks.lunarStone) {
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
                            primer.setBlockState(x, y, z, lunarStone);
                        }
                    }
                }
            }
        }

        // Üstteki blokları son bir kez düzelt
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = TERRAIN_MIN_HEIGHT + 1; y < MAX_HEIGHT; y++) {
                    IBlockState current = primer.getBlockState(x, y, z);
                    if (current.getBlock() == ModBlocks.lunarStone) {
                        IBlockState above = y < MAX_HEIGHT - 1 ? primer.getBlockState(x, y + 1, z) : null;
                        if (above != null && above.getBlock() == Blocks.AIR) {
                            primer.setBlockState(x, y, z, lunarDust); // Üstü açık olan Lunar Stone bloklarını Lunar Dust'a çevir
                        }
                    }
                }
            }
        }

        // beryllium cevheri damarları oluştur (normal arazide)
        generateberylliumVeins(primer, randomGenerator, berylliumOre, lunarStone);

        // Krater oluşturma işlemi
        generateCratersWithberyllium(chunkX, chunkZ, primer, randomGenerator, berylliumOre, lunarDust, lunarStone);
    }

    /**
     * Krater oluşturma fonksiyonu
     */
    private void generateCratersWithberyllium(int chunkX, int chunkZ, ChunkPrimer primer, Random random,
                                             IBlockState berylliumOre, IBlockState lunarDust, IBlockState lunarStone) {
        int baseX = chunkX * 16;
        int baseZ = chunkZ * 16;

        // Chunk başına test edilecek potansiyel krater noktası sayısını azaltalım
        int craterCenters = 2; // 5'ten 2'ye düşürüldü

        for (int i = 0; i < craterCenters; i++) {
            // Chunk içinde rastgele bir nokta seç
            int centerX = random.nextInt(16);
            int centerZ = random.nextInt(16);

            // Krater oluşturma şansını düşürelim
            if (random.nextDouble() < CRATER_CHANCE * 0.5) { // Şansı yarıya indirdik
                // Krater yarıçapını sınırlayalım
                int radius = MIN_CRATER_RADIUS + random.nextInt(MAX_CRATER_RADIUS - MIN_CRATER_RADIUS);
                // Çok büyük kraterleri önlemek için radius'u sınırla
                radius = Math.min(radius, 8); // Max 8 radius olacak şekilde sınırlandırıldı

                // Krater derinliğini sınırla
                int depth = Math.max(2, radius / 3); // Depth daha az olacak
                depth = Math.min(depth, 5); // Max 5 derinlik

                // Krater merkezinin Y pozisyonunu bul
                int centerY = findSurfaceHeight(primer, centerX, centerZ);
                if (centerY < FLAT_TERRAIN_HEIGHT) {
                    centerY = FLAT_TERRAIN_HEIGHT; // Minimum yükseklik
                }

                // Radius'u kontrol et - eğer çok büyükse atlayalım
                if (radius > 8) {
                    continue;
                }

                // Krater oluştur
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        // Nokta krater çemberinin içinde mi?
                        double distance = Math.sqrt(x * x + z * z);
                        if (distance <= radius) {
                            // Chunk sınırları içinde mi kontrol et
                            int worldX = centerX + x;
                            int worldZ = centerZ + z;

                            if (worldX >= 0 && worldX < 16 && worldZ >= 0 && worldZ < 16) {
                                // Krater derinliğini hesapla - kenarlardan merkeze doğru derinleşir
                                double depthFactor = 1.0 - (distance / radius);
                                int craterDepth = (int) Math.round(depth * depthFactor * depthFactor);

                                // Krater içindeki derinliği uygula
                                for (int y = 0; y <= craterDepth; y++) {
                                    int targetY = centerY - y;
                                    if (targetY >= TERRAIN_MIN_HEIGHT && targetY < MAX_HEIGHT) {
                                        try {
                                            primer.setBlockState(worldX, targetY, worldZ, Blocks.AIR.getDefaultState());
                                        } catch (IndexOutOfBoundsException e) {
                                            // Koordinat sınırları dışındaysa atla
                                            continue;
                                        }
                                    }
                                }

                                // Krater kenarlarını oluştur (rim) - boyutu sınırla
                                if (distance > radius * 10.0 && distance <= radius) {
                                    double rimHeight = (radius - distance) / (radius * 0.2);
                                    // Yüksekliği sınırlandır
                                    rimHeight = Math.min(rimHeight, 1.5); // En fazla 1.5 kat
                                    int rimBlocks = (int) Math.ceil(rimHeight);
                                    rimBlocks = Math.min(rimBlocks, 2); // En fazla 2 blok yükseklik

                                    for (int y = 1; y <= rimBlocks; y++) {
                                        int targetY = centerY + y;
                                        if (targetY >= TERRAIN_MIN_HEIGHT && targetY < MAX_HEIGHT) {
                                            try {
                                                if (y == rimBlocks) {
                                                    primer.setBlockState(worldX, targetY, worldZ, lunarDust);
                                                } else {
                                                    primer.setBlockState(worldX, targetY, worldZ, lunarStone);
                                                }
                                            } catch (IndexOutOfBoundsException e) {
                                                // Koordinat sınırları dışındaysa atla
                                                continue;
                                            }
                                        }
                                    }
                                }

                                // Krater tabanını oluştur ve beryllium yerleştir
                                int baseY = centerY - craterDepth;
                                if (baseY >= TERRAIN_MIN_HEIGHT && baseY < MAX_HEIGHT) {
                                    try {
                                        // Krater tabanına LunarDust yerleştir
                                        primer.setBlockState(worldX, baseY, worldZ, lunarDust);

                                        // Crater içinde beryllium cevheri oluşturma şansı
                                        if (random.nextDouble() < beryllium_CHANCE) {
                                            // beryllium cevheri yerleştir, bazıları yüzeyde, bazıları gömülü olsun
                                            if (random.nextDouble() < 0.5) {
                                                // Yüzeyde
                                                primer.setBlockState(worldX, baseY, worldZ, berylliumOre);
                                            } else if (baseY - 1 >= TERRAIN_MIN_HEIGHT) {
                                                // Gömülü, sadece 1 blok derinlikte (daha derine gitme)
                                                primer.setBlockState(worldX, baseY - 1, worldZ, berylliumOre);
                                            }
                                        }
                                    } catch (IndexOutOfBoundsException e) {
                                        // Koordinat sınırları dışındaysa atla
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Yüzey yüksekliğini bulma yardımcı fonksiyonu - sınır kontrolleriyle
     */
    private int findSurfaceHeight(ChunkPrimer primer, int x, int z) {
        // Koordinatları kontrol et
        if (x < 0 || x >= 16 || z < 0 || z >= 16) {
            return FLAT_TERRAIN_HEIGHT; // Sınırlar dışındaysa varsayılan yükseklik döndür
        }

        for (int y = MAX_HEIGHT - 1; y >= TERRAIN_MIN_HEIGHT; y--) {
            try {
                IBlockState state = primer.getBlockState(x, y, z);
                if (state.getBlock() != Blocks.AIR) {
                    return y;
                }
            } catch (IndexOutOfBoundsException e) {
                // Herhangi bir sınır hatası olursa varsayılan değer döndür
                return FLAT_TERRAIN_HEIGHT;
            }
        }
        return FLAT_TERRAIN_HEIGHT; // Hiçbir şey bulunamazsa varsayılan düz arazi yüksekliği
    }

    /**
     * beryllium cevheri damarları oluşturma - sınır kontrolleriyle
     */
    private void generateberylliumVeins(ChunkPrimer primer, Random random, IBlockState berylliumOre, IBlockState lunarStone) {
        // Chunk başına kaç damar oluşturulacağını belirle - sayıyı azalt
        int veinsPerChunk = 5 + random.nextInt(10); // 2-3 damar (önceki 3-5)

        for (int vein = 0; vein < veinsPerChunk; vein++) {
            if (random.nextDouble() < beryllium_VEIN_CHANCE) {
                // Damar başlangıç konumu
                int startX = random.nextInt(16);
                int startZ = random.nextInt(16);
                int startY = TERRAIN_MIN_HEIGHT + random.nextInt(FLAT_TERRAIN_HEIGHT - TERRAIN_MIN_HEIGHT);

                // Damar büyüklüğü - daha küçük
                int veinSize = 10 + random.nextInt(20); // 2-4 blok (önceki 3-7)

                // Damar yönü (rastgele bir yön)
                double dirX = random.nextDouble() - 0.5;
                double dirZ = random.nextDouble() - 0.5;
                double dirY = (random.nextDouble() - 0.5) * 0.5; // Y yönünde daha az değişim

                // Damar yönlendiricisini normalize et
                double length = Math.sqrt(dirX * dirX + dirZ * dirZ + dirY * dirY);
                dirX /= length;
                dirZ /= length;
                dirY /= length;

                // Damarı oluştur
                double x = startX;
                double y = startY;
                double z = startZ;

                for (int i = 0; i < veinSize; i++) {
                    int blockX = (int) Math.round(x);
                    int blockY = (int) Math.round(y);
                    int blockZ = (int) Math.round(z);

                    // Chunk sınırları içinde kontrol et
                    if (blockX >= 0 && blockX < 16 && blockZ >= 0 && blockZ < 16 &&
                            blockY >= TERRAIN_MIN_HEIGHT && blockY < MAX_HEIGHT) {

                        try {
                            IBlockState currentBlock = primer.getBlockState(blockX, blockY, blockZ);
                            // Sadece LunarStone'u beryllium ile değiştir
                            if (currentBlock.getBlock() == ModBlocks.lunarStone) {
                                primer.setBlockState(blockX, blockY, blockZ, berylliumOre);

                                // Küçük bir küme oluştur (aynı noktada birkaç blok) - küme boyutunu azalt
                                for (int dx = -1; dx <= 1; dx++) {
                                    for (int dy = -1; dy <= 1; dy++) {
                                        for (int dz = -1; dz <= 1; dz++) {
                                            if ((dx == 0 && dy == 0 && dz == 0) ||
                                                    Math.abs(dx) + Math.abs(dy) + Math.abs(dz) > 2) {
                                                continue; // Merkez bloğunu ve uzak köşeleri atla
                                            }

                                            int nx = blockX + dx;
                                            int ny = blockY + dy;
                                            int nz = blockZ + dz;

                                            // Chunk sınırları içinde kontrol et
                                            if (nx >= 0 && nx < 16 && nz >= 0 && nz < 16 &&
                                                    ny >= TERRAIN_MIN_HEIGHT && ny < MAX_HEIGHT) {

                                                // Küçük bir şansa göre komşu bloğu da dönüştür - şansı azalt
                                                if (random.nextDouble() < 0.2) { // 0.3'ten 0.2'ye düşürüldü
                                                    try {
                                                        IBlockState neighborBlock = primer.getBlockState(nx, ny, nz);
                                                        if (neighborBlock.getBlock() == ModBlocks.lunarStone) {
                                                            primer.setBlockState(nx, ny, nz, berylliumOre);
                                                        }
                                                    } catch (IndexOutOfBoundsException e) {
                                                        // Sınır hatası olursa atla
                                                        continue;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            // Sınır hatası olursa atla
                            continue;
                        }
                    }

                    // Damarı hafifçe rastgele yönde ilerlet
                    x += dirX + (random.nextDouble() - 0.5) * 0.3; // Daha az sapma
                    y += dirY + (random.nextDouble() - 0.5) * 0.3;
                    z += dirZ + (random.nextDouble() - 0.5) * 0.3;
                }
            }
        }
    }
}