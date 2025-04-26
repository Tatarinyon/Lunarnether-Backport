package com.lunarnether.blocks;

import com.lunarnether.Main;
import com.lunarnether.tabs.CreativeTabLunarNether;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockSlabBase extends BlockSlab {

    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    public BlockSlabBase(String name, Material material, SoundType sound, float hardness, float resistance, String tool, int harvestLevel) {
        super(material);
        IBlockState blockState = this.blockState.getBaseState().withProperty(VARIANT, Variant.DEFAULT);

        if (!this.isDouble()) {
            blockState = blockState.withProperty(HALF, EnumBlockHalf.BOTTOM);
        }

        setDefaultState(blockState);
        setTranslationKey(name);  // Changed from setUnlocalizedName
        setRegistryName(name);
        setHardness(hardness);
        setResistance(resistance);
        setHarvestLevel(tool, harvestLevel);
        setSoundType(sound);
        setCreativeTab(CreativeTabLunarNether.LUNAR_NETHER_TAB);
        useNeighborBrightness = true;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this);
    }

    @Override
    public String getTranslationKey(int meta) {  // Changed from getUnlocalizedName
        return this.getTranslationKey();  // Changed from getUnlocalizedName
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return Variant.DEFAULT;
    }

    @Override
    public abstract boolean isDouble();  // Made abstract to force implementation

    @Override
    public final IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState().withProperty(VARIANT, Variant.DEFAULT);

        if (!this.isDouble()) {
            state = state.withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }

        return state;
    }

    @Override
    public final int getMetaFromState(IBlockState state) {
        int meta = 0;

        if (!this.isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP) {
            meta |= 8;
        }

        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if (this.isDouble()) {
            return new BlockStateContainer(this, VARIANT);
        } else {
            return new BlockStateContainer(this, HALF, VARIANT);
        }
    }

    public enum Variant implements IStringSerializable {
        DEFAULT;

        @Override
        public String getName() {
            return "default";
        }
    }

    // Half and Double slab subclasses
    public static class Half extends BlockSlabBase {
        public Half(String name, Material material, SoundType sound, float hardness, float resistance, String tool, int harvestLevel) {
            super(name, material, sound, hardness, resistance, tool, harvestLevel);
        }

        @Override
        public boolean isDouble() {
            return false;
        }
    }

    public static class Double extends BlockSlabBase {
        public Double(String name, Material material, SoundType sound, float hardness, float resistance, String tool, int harvestLevel) {
            super(name, material, sound, hardness, resistance, tool, harvestLevel);
        }

        @Override
        public boolean isDouble() {
            return true;
        }
    }
}