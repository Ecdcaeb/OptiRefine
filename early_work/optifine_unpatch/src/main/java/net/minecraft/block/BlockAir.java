/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.BlockFaceShape
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.EnumBlockRenderType
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAir
extends Block {
    protected BlockAir() {
        super(Material.AIR);
    }

    public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        return NULL_AABB;
    }

    public boolean isOpaqueCube(IBlockState iBlockState) {
        return false;
    }

    public boolean canCollideCheck(IBlockState iBlockState, boolean bl) {
        return false;
    }

    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState iBlockState, float f, int n) {
    }

    public boolean isReplaceable(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return true;
    }

    public boolean isFullCube(IBlockState iBlockState) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iBlockAccess, IBlockState iBlockState, BlockPos blockPos, EnumFacing enumFacing) {
        return BlockFaceShape.UNDEFINED;
    }
}
