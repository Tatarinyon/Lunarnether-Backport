package com.lunarnether.blocks;

import com.lunarnether.Main;
import com.lunarnether.tabs.CreativeTabLunarNether;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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

    // Define all block instances
    public static Block lunarStone;
    public static Block smoothLunarStone;
    public static Block polishedLunarStone;
    public static Block cutPolishedLunarStone;
    public static Block lunarDust;
    public static Block astralith;
    public static Block ilmeniteOre;
    public static Block rawIlmeniteBlock;
    public static Block titaniumBlock;
    public static Block chiseledTitanium;
    public static Block cutTitanium;

    public static void init() {
        // Create all blocks
        lunarStone = registerBlock(new BaseBlock("lunar_stone", Material.ROCK, 1.5F, 10.0F, "pickaxe", 1, SoundType.STONE));
        smoothLunarStone = registerBlock(new BaseBlock("smooth_lunar_stone", Material.ROCK, 1.5F, 10.0F, "pickaxe", 1, SoundType.STONE));
        polishedLunarStone = registerBlock(new BaseBlock("polished_lunar_stone", Material.ROCK, 1.5F, 10.0F, "pickaxe", 1, SoundType.STONE));
        cutPolishedLunarStone = registerBlock(new BaseBlock("cut_polished_lunar_stone", Material.ROCK, 1.5F, 10.0F, "pickaxe", 1, SoundType.STONE));
        lunarDust = registerBlock(new BaseBlock("lunar_dust", Material.SAND, 0.5F, 2.5F, "shovel", 0, SoundType.SAND));
        astralith = registerBlock(new BaseBlock("astralith", Material.ROCK, 3.0F, 15.0F, "pickaxe", 2, SoundType.STONE));
        ilmeniteOre = registerBlock(new BaseBlock("ilmenite_ore", Material.ROCK, 3.0F, 15.0F, "pickaxe", 2, SoundType.STONE));
        rawIlmeniteBlock = registerBlock(new BaseBlock("raw_ilmenite_block", Material.ROCK, 5.0F, 30.0F, "pickaxe", 2, SoundType.STONE));
        titaniumBlock = registerBlock(new BaseBlock("titanium_block", Material.IRON, 5.0F, 30.0F, "pickaxe", 2, SoundType.METAL));
        chiseledTitanium = registerBlock(new BaseBlock("chiseled_titanium", Material.IRON, 5.0F, 30.0F, "pickaxe", 2, SoundType.METAL));
        cutTitanium = registerBlock(new BaseBlock("cut_titanium", Material.IRON, 5.0F, 30.0F, "pickaxe", 2, SoundType.METAL));
    }


    private static Block registerBlock(Block block) {
        BLOCKS.add(block);
        return block;
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
            event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        for (Block block : BLOCKS) {
            registerModel(block);
        }
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Block block) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
                new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }
}
