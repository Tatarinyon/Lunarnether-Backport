package com.lunarnether.blocks;

import com.lunarnether.Main;
import com.lunarnether.tabs.CreativeTabLunarNether;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class ModBlocks {
    // List to keep track of all our blocks
    private static final List<Block> BLOCKS = new ArrayList<>();
    private static final List<Item> ITEMS = new ArrayList<>();

    // Define all block instances
    public static Block lunarStone;
    public static Block smoothLunarStone;
    public static Block polishedLunarStone;
    public static Block cutPolishedLunarStone;
    public static Block lunarDust;
    public static Block astralrock;
    public static Block ilmeniteOre;
    public static Block rawIlmeniteBlock;
    public static Block titaniumBlock;
    public static Block chiseledTitanium;
    public static Block cutTitanium;

    // Yeni merdiven blokları
    public static Block lunarStoneStairs;
    public static Block smoothLunarStoneStairs;
    public static Block polishedLunarStoneStairs;
    public static Block cutPolishedLunarStoneStairs;
    public static Block titaniumStairs;
    public static Block chiseledTitaniumStairs;
    public static Block cutTitaniumStairs;

    // Yeni duvar blokları
    public static Block lunarStoneWall;
    public static Block smoothLunarStoneWall;
    public static Block polishedLunarStoneWall;
    public static Block titaniumWall;
    public static Block cutTitaniumWall;

    // Yeni slab blokları
    public static BlockSlab lunarStoneSlab;
    public static BlockSlab lunarStoneDoubleSlab;
    public static BlockSlab smoothLunarStoneSlab;
    public static BlockSlab smoothLunarStoneDoubleSlab;
    public static BlockSlab polishedLunarStoneSlab;
    public static BlockSlab polishedLunarStoneDoubleSlab;
    public static BlockSlab titaniumSlab;
    public static BlockSlab titaniumDoubleSlab;
    public static BlockSlab cutTitaniumSlab;
    public static BlockSlab cutTitaniumDoubleSlab;

    public static void init() {
        // Create all blocks
        lunarStone = registerBlock(new BaseBlock("lunar_stone", Material.ROCK, 1.5F, 10.0F, "pickaxe", 1, SoundType.STONE));
        smoothLunarStone = registerBlock(new BaseBlock("smooth_lunar_stone", Material.ROCK, 1.5F, 10.0F, "pickaxe", 1, SoundType.STONE));
        polishedLunarStone = registerBlock(new BaseBlock("polished_lunar_stone", Material.ROCK, 1.5F, 10.0F, "pickaxe", 1, SoundType.STONE));
        cutPolishedLunarStone = registerBlock(new BaseBlock("cut_polished_lunar_stone", Material.ROCK, 1.5F, 10.0F, "pickaxe", 1, SoundType.STONE));
        lunarDust = registerBlock(new BaseBlock("lunar_dust", Material.SAND, 0.5F, 2.5F, "shovel", 0, SoundType.SAND));
        astralrock = registerBlock(new BaseBlock("astral_rock", Material.ROCK, 15.0F, 15.0F, "pickaxe", 2, SoundType.STONE));
        ilmeniteOre = registerBlock(new BaseBlock("ilmenite_ore", Material.ROCK, 3.0F, 15.0F, "pickaxe", 2, SoundType.STONE));
        rawIlmeniteBlock = registerBlock(new BaseBlock("raw_ilmenite_block", Material.ROCK, 5.0F, 30.0F, "pickaxe", 2, SoundType.STONE));
        titaniumBlock = registerBlock(new BaseBlock("titanium_block", Material.IRON, 5.0F, 30.0F, "pickaxe", 2, SoundType.METAL));
        chiseledTitanium = registerBlock(new BaseBlock("chiseled_titanium", Material.IRON, 5.0F, 30.0F, "pickaxe", 2, SoundType.METAL));
        cutTitanium = registerBlock(new BaseBlock("cut_titanium", Material.IRON, 5.0F, 30.0F, "pickaxe", 2, SoundType.METAL));

        // Merdiven blokları oluşturma
        lunarStoneStairs = registerBlock(new BlockStairsBase(lunarStone.getDefaultState(), "lunar_stone_stairs"));
        smoothLunarStoneStairs = registerBlock(new BlockStairsBase(smoothLunarStone.getDefaultState(), "smooth_lunar_stone_stairs"));
        polishedLunarStoneStairs = registerBlock(new BlockStairsBase(polishedLunarStone.getDefaultState(), "polished_lunar_stone_stairs"));
        cutPolishedLunarStoneStairs = registerBlock(new BlockStairsBase(cutPolishedLunarStone.getDefaultState(), "cut_polished_lunar_stone_stairs"));
        titaniumStairs = registerBlock(new BlockStairsBase(titaniumBlock.getDefaultState(), "titanium_stairs"));
        chiseledTitaniumStairs = registerBlock(new BlockStairsBase(chiseledTitanium.getDefaultState(), "chiseled_titanium_stairs"));
        cutTitaniumStairs = registerBlock(new BlockStairsBase(cutTitanium.getDefaultState(), "cut_titanium_stairs"));

        // Duvar blokları oluşturma
        lunarStoneWall = registerBlock(new BlockWallBase(lunarStone, "lunar_stone_wall"));
        smoothLunarStoneWall = registerBlock(new BlockWallBase(smoothLunarStone, "smooth_lunar_stone_wall"));
        polishedLunarStoneWall = registerBlock(new BlockWallBase(polishedLunarStone, "polished_lunar_stone_wall"));
        titaniumWall = registerBlock(new BlockWallBase(titaniumBlock, "titanium_wall"));
        cutTitaniumWall = registerBlock(new BlockWallBase(cutTitanium, "cut_titanium_wall"));

        // Slab blokları oluşturma
        lunarStoneSlab = registerBlock(new BlockSlabBase.Half("lunar_stone_slab", Material.ROCK, SoundType.STONE, 1.5F, 10.0F, "pickaxe", 1));
        lunarStoneDoubleSlab = registerBlock(new BlockSlabBase.Double("lunar_stone_double_slab", Material.ROCK, SoundType.STONE, 1.5F, 10.0F, "pickaxe", 1));

        smoothLunarStoneSlab = registerBlock(new BlockSlabBase.Half("smooth_lunar_stone_slab", Material.ROCK, SoundType.STONE, 1.5F, 10.0F, "pickaxe", 1));
        smoothLunarStoneDoubleSlab = registerBlock(new BlockSlabBase.Double("smooth_lunar_stone_double_slab", Material.ROCK, SoundType.STONE, 1.5F, 10.0F, "pickaxe", 1));

        polishedLunarStoneSlab = registerBlock(new BlockSlabBase.Half("polished_lunar_stone_slab", Material.ROCK, SoundType.STONE, 1.5F, 10.0F, "pickaxe", 1));
        polishedLunarStoneDoubleSlab = registerBlock(new BlockSlabBase.Double("polished_lunar_stone_double_slab", Material.ROCK, SoundType.STONE, 1.5F, 10.0F, "pickaxe", 1));

        titaniumSlab = registerBlock(new BlockSlabBase.Half("titanium_slab", Material.IRON, SoundType.METAL, 5.0F, 30.0F, "pickaxe", 2));
        titaniumDoubleSlab = registerBlock(new BlockSlabBase.Double("titanium_double_slab", Material.IRON, SoundType.METAL, 5.0F, 30.0F, "pickaxe", 2));

        cutTitaniumSlab = registerBlock(new BlockSlabBase.Half("cut_titanium_slab", Material.IRON, SoundType.METAL, 5.0F, 30.0F, "pickaxe", 2));
        cutTitaniumDoubleSlab = registerBlock(new BlockSlabBase.Double("cut_titanium_double_slab", Material.IRON, SoundType.METAL, 5.0F, 30.0F, "pickaxe", 2));
    }


    private static <T extends Block> T registerBlock(T block) {
        BLOCKS.add(block);
        return block;
    }

    private static <T extends Item> T registerItem(T item) {
        ITEMS.add(item);
        return item;
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        init();
        for (Block block : BLOCKS) {
            event.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        for (Block block : BLOCKS) {
            if (block instanceof BlockSlab && ((BlockSlab) block).isDouble()) {
                // Çift slablar için item kaydetme - bunların itemi olmaz
                continue;
            } else if (block instanceof BlockSlab && !((BlockSlab) block).isDouble()) {
                // Tek slablar için ItemSlab kullanarak item kaydetme
                BlockSlab singleSlab = (BlockSlab) block;
                BlockSlab doubleSlab = findDoubleSlab(singleSlab);
                if (doubleSlab != null) {
                    registerItem(new ItemSlab(singleSlab, singleSlab, doubleSlab).setRegistryName(block.getRegistryName()));
                    event.getRegistry().register(ITEMS.get(ITEMS.size() - 1));
                    continue;
                }
            }
            // Normal bloklar ve diğer durumlar için standart ItemBlock kaydetme
            event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    // Slab blokları için yardımcı metod
    private static BlockSlab findDoubleSlab(BlockSlab singleSlab) {
        String singleName = singleSlab.getRegistryName().toString();
        String doubleName = singleName.replace("_slab", "_double_slab");

        for (Block block : BLOCKS) {
            if (block instanceof BlockSlab && ((BlockSlab) block).isDouble() &&
                    block.getRegistryName().toString().equals(doubleName)) {
                return (BlockSlab) block;
            }
        }
        return null;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        for (Block block : BLOCKS) {
            if (block instanceof BlockSlab && ((BlockSlab) block).isDouble()) {
                // Çift slabların modeli kaydetmeye gerek yok
                continue;
            }
            registerModel(block);
        }
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Block block) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
                new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }
}