package com.lunarnether.blocks;

import com.lunarnether.items.ModItems;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;


import java.util.ArrayList;
import java.util.List;

public class BlockBerylliumOre extends BaseBlock {

    public BlockBerylliumOre(String name, Material material, float hardness, float resistance, String tool, int harvestLevel, SoundType sound) {
        super(name, material, hardness, resistance, tool, harvestLevel, sound);
    }

    // Normal şekilde kırıldığında vanilla demir ingotu düşsün.
    @Override
    public List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(ModItems.BERYLLIUM_INGOT, 1));  // Normal kırma: demir ingot
        return drops;
    }
}
