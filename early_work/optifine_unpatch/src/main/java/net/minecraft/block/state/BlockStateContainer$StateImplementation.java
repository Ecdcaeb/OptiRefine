/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableTable
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Table
 *  java.lang.Comparable
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.EnumPushReaction
 *  net.minecraft.block.material.MapColor
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.BlockFaceShape
 *  net.minecraft.block.state.BlockStateBase
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumBlockRenderType
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.Mirror
 *  net.minecraft.util.Rotation
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.block.state;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public static class BlockStateContainer.StateImplementation
extends BlockStateBase {
    private final Block block;
    private final ImmutableMap<IProperty<?>, Comparable<?>> properties;
    protected ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> propertyValueTable;

    protected BlockStateContainer.StateImplementation(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
        this.block = blockIn;
        this.properties = propertiesIn;
    }

    protected BlockStateContainer.StateImplementation(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn, ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> propertyValueTable) {
        this.block = blockIn;
        this.properties = propertiesIn;
        this.propertyValueTable = propertyValueTable;
    }

    public Collection<IProperty<?>> getPropertyKeys() {
        return Collections.unmodifiableCollection((Collection)this.properties.keySet());
    }

    public <T extends Comparable<T>> T getValue(IProperty<T> property) {
        Comparable comparable = (Comparable)this.properties.get(property);
        if (comparable == null) {
            throw new IllegalArgumentException("Cannot get property " + String.valueOf(property) + " as it does not exist in " + String.valueOf((Object)this.block.getBlockState()));
        }
        return (T)((Comparable)property.getValueClass().cast((Object)comparable));
    }

    public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
        Comparable comparable = (Comparable)this.properties.get(property);
        if (comparable == null) {
            throw new IllegalArgumentException("Cannot set property " + String.valueOf(property) + " as it does not exist in " + String.valueOf((Object)this.block.getBlockState()));
        }
        if (comparable == value) {
            return this;
        }
        IBlockState iblockstate = (IBlockState)this.propertyValueTable.get(property, value);
        if (iblockstate == null) {
            throw new IllegalArgumentException("Cannot set property " + String.valueOf(property) + " to " + String.valueOf(value) + " on block " + String.valueOf((Object)Block.REGISTRY.getNameForObject((Object)this.block)) + ", it is not an allowed value");
        }
        return iblockstate;
    }

    public ImmutableMap<IProperty<?>, Comparable<?>> getProperties() {
        return this.properties;
    }

    public Block getBlock() {
        return this.block;
    }

    public boolean equals(Object p_equals_1_) {
        return this == p_equals_1_;
    }

    public int hashCode() {
        return this.properties.hashCode();
    }

    public void buildPropertyValueTable(Map<Map<IProperty<?>, Comparable<?>>, BlockStateContainer.StateImplementation> map) {
        if (this.propertyValueTable != null) {
            throw new IllegalStateException();
        }
        HashBasedTable table = HashBasedTable.create();
        for (Map.Entry entry : this.properties.entrySet()) {
            IProperty iproperty = (IProperty)entry.getKey();
            for (Comparable comparable : iproperty.getAllowedValues()) {
                if (comparable == entry.getValue()) continue;
                table.put((Object)iproperty, (Object)comparable, (Object)((IBlockState)map.get(this.getPropertiesWithValue(iproperty, comparable))));
            }
        }
        this.propertyValueTable = ImmutableTable.copyOf((Table)table);
    }

    private Map<IProperty<?>, Comparable<?>> getPropertiesWithValue(IProperty<?> property, Comparable<?> value) {
        HashMap map = Maps.newHashMap(this.properties);
        map.put(property, value);
        return map;
    }

    public Material getMaterial() {
        return this.block.getMaterial((IBlockState)this);
    }

    public boolean isFullBlock() {
        return this.block.isFullBlock((IBlockState)this);
    }

    public boolean canEntitySpawn(Entity entityIn) {
        return this.block.canEntitySpawn((IBlockState)this, entityIn);
    }

    public int getLightOpacity() {
        return this.block.getLightOpacity((IBlockState)this);
    }

    public int getLightValue() {
        return this.block.getLightValue((IBlockState)this);
    }

    @SideOnly(value=Side.CLIENT)
    public boolean isTranslucent() {
        return this.block.isTranslucent((IBlockState)this);
    }

    public boolean useNeighborBrightness() {
        return this.block.getUseNeighborBrightness((IBlockState)this);
    }

    public MapColor getMapColor(IBlockAccess p_185909_1_, BlockPos p_185909_2_) {
        return this.block.getMapColor((IBlockState)this, p_185909_1_, p_185909_2_);
    }

    public IBlockState withRotation(Rotation rot) {
        return this.block.withRotation((IBlockState)this, rot);
    }

    public IBlockState withMirror(Mirror mirrorIn) {
        return this.block.withMirror((IBlockState)this, mirrorIn);
    }

    public boolean isFullCube() {
        return this.block.isFullCube((IBlockState)this);
    }

    @SideOnly(value=Side.CLIENT)
    public boolean hasCustomBreakingProgress() {
        return this.block.hasCustomBreakingProgress((IBlockState)this);
    }

    public EnumBlockRenderType getRenderType() {
        return this.block.getRenderType((IBlockState)this);
    }

    @SideOnly(value=Side.CLIENT)
    public int getPackedLightmapCoords(IBlockAccess source, BlockPos pos) {
        return this.block.getPackedLightmapCoords((IBlockState)this, source, pos);
    }

    @SideOnly(value=Side.CLIENT)
    public float getAmbientOcclusionLightValue() {
        return this.block.getAmbientOcclusionLightValue((IBlockState)this);
    }

    public boolean isBlockNormalCube() {
        return this.block.isBlockNormalCube((IBlockState)this);
    }

    public boolean isNormalCube() {
        return this.block.isNormalCube((IBlockState)this);
    }

    public boolean canProvidePower() {
        return this.block.canProvidePower((IBlockState)this);
    }

    public int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return this.block.getWeakPower((IBlockState)this, blockAccess, pos, side);
    }

    public boolean hasComparatorInputOverride() {
        return this.block.hasComparatorInputOverride((IBlockState)this);
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return this.block.getComparatorInputOverride((IBlockState)this, worldIn, pos);
    }

    public float getBlockHardness(World worldIn, BlockPos pos) {
        return this.block.getBlockHardness((IBlockState)this, worldIn, pos);
    }

    public float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos) {
        return this.block.getPlayerRelativeBlockHardness((IBlockState)this, player, worldIn, pos);
    }

    public int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return this.block.getStrongPower((IBlockState)this, blockAccess, pos, side);
    }

    public EnumPushReaction getPushReaction() {
        return this.block.getPushReaction((IBlockState)this);
    }

    public IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos) {
        return this.block.getActualState((IBlockState)this, blockAccess, pos);
    }

    @SideOnly(value=Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        return this.block.getSelectedBoundingBox((IBlockState)this, worldIn, pos);
    }

    @SideOnly(value=Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing facing) {
        return this.block.shouldSideBeRendered((IBlockState)this, blockAccess, pos, facing);
    }

    public boolean isOpaqueCube() {
        return this.block.isOpaqueCube((IBlockState)this);
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockAccess worldIn, BlockPos pos) {
        return this.block.getCollisionBoundingBox((IBlockState)this, worldIn, pos);
    }

    public void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185908_6_) {
        this.block.addCollisionBoxToList((IBlockState)this, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185908_6_);
    }

    public AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos) {
        return this.block.getBoundingBox((IBlockState)this, blockAccess, pos);
    }

    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return this.block.collisionRayTrace((IBlockState)this, worldIn, pos, start, end);
    }

    public boolean isTopSolid() {
        return this.block.isTopSolid((IBlockState)this);
    }

    public Vec3d getOffset(IBlockAccess access, BlockPos pos) {
        return this.block.getOffset((IBlockState)this, access, pos);
    }

    public boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param) {
        return this.block.eventReceived((IBlockState)this, worldIn, pos, id, param);
    }

    public void neighborChanged(World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        this.block.neighborChanged((IBlockState)this, worldIn, pos, blockIn, fromPos);
    }

    public boolean causesSuffocation() {
        return this.block.causesSuffocation((IBlockState)this);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
        return this.block.getBlockFaceShape(worldIn, (IBlockState)this, pos, facing);
    }

    public ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> getPropertyValueTable() {
        return this.propertyValueTable;
    }

    public int getLightOpacity(IBlockAccess world, BlockPos pos) {
        return this.block.getLightOpacity((IBlockState)this, world, pos);
    }

    public int getLightValue(IBlockAccess world, BlockPos pos) {
        return this.block.getLightValue((IBlockState)this, world, pos);
    }

    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return this.block.isSideSolid((IBlockState)this, world, pos, side);
    }

    public boolean doesSideBlockChestOpening(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return this.block.doesSideBlockChestOpening((IBlockState)this, world, pos, side);
    }

    public boolean doesSideBlockRendering(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return this.block.doesSideBlockRendering((IBlockState)this, world, pos, side);
    }
}
