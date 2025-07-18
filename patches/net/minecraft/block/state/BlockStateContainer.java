package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MapPopulator;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Cartesian;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStateContainer {
   private static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
   private static final Function<IProperty<?>, String> GET_NAME_FUNC = new Function<IProperty<?>, String>() {
      @Nullable
      public String apply(@Nullable IProperty<?> var1) {
         return ☃ == null ? "<NULL>" : ☃.getName();
      }
   };
   private final Block block;
   private final ImmutableSortedMap<String, IProperty<?>> properties;
   private final ImmutableList<IBlockState> validStates;

   public BlockStateContainer(Block var1, IProperty<?>... var2) {
      this.block = ☃;
      Map<String, IProperty<?>> ☃ = Maps.newHashMap();

      for (IProperty<?> ☃x : ☃) {
         validateProperty(☃, ☃x);
         ☃.put(☃x.getName(), ☃x);
      }

      this.properties = ImmutableSortedMap.copyOf(☃);
      Map<Map<IProperty<?>, Comparable<?>>, BlockStateContainer.StateImplementation> ☃x = Maps.newLinkedHashMap();
      List<BlockStateContainer.StateImplementation> ☃xx = Lists.newArrayList();

      for (List<Comparable<?>> ☃xxx : Cartesian.cartesianProduct(this.getAllowedValues())) {
         Map<IProperty<?>, Comparable<?>> ☃xxxx = MapPopulator.createMap(this.properties.values(), ☃xxx);
         BlockStateContainer.StateImplementation ☃xxxxx = new BlockStateContainer.StateImplementation(☃, ImmutableMap.copyOf(☃xxxx));
         ☃x.put(☃xxxx, ☃xxxxx);
         ☃xx.add(☃xxxxx);
      }

      for (BlockStateContainer.StateImplementation ☃xxx : ☃xx) {
         ☃xxx.buildPropertyValueTable(☃x);
      }

      this.validStates = ImmutableList.copyOf(☃xx);
   }

   public static <T extends Comparable<T>> String validateProperty(Block var0, IProperty<T> var1) {
      String ☃ = ☃.getName();
      if (!NAME_PATTERN.matcher(☃).matches()) {
         throw new IllegalArgumentException("Block: " + ☃.getClass() + " has invalidly named property: " + ☃);
      } else {
         for (T ☃x : ☃.getAllowedValues()) {
            String ☃xx = ☃.getName(☃x);
            if (!NAME_PATTERN.matcher(☃xx).matches()) {
               throw new IllegalArgumentException("Block: " + ☃.getClass() + " has property: " + ☃ + " with invalidly named value: " + ☃xx);
            }
         }

         return ☃;
      }
   }

   public ImmutableList<IBlockState> getValidStates() {
      return this.validStates;
   }

   private List<Iterable<Comparable<?>>> getAllowedValues() {
      List<Iterable<Comparable<?>>> ☃ = Lists.newArrayList();
      ImmutableCollection<IProperty<?>> ☃x = this.properties.values();
      UnmodifiableIterator var3 = ☃x.iterator();

      while (var3.hasNext()) {
         IProperty<?> ☃xx = (IProperty<?>)var3.next();
         ☃.add((Iterable<Comparable<?>>)☃xx.getAllowedValues());
      }

      return ☃;
   }

   public IBlockState getBaseState() {
      return (IBlockState)this.validStates.get(0);
   }

   public Block getBlock() {
      return this.block;
   }

   public Collection<IProperty<?>> getProperties() {
      return this.properties.values();
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
         .add("block", Block.REGISTRY.getNameForObject(this.block))
         .add("properties", Iterables.transform(this.properties.values(), GET_NAME_FUNC))
         .toString();
   }

   @Nullable
   public IProperty<?> getProperty(String var1) {
      return (IProperty<?>)this.properties.get(☃);
   }

   static class StateImplementation extends BlockStateBase {
      private final Block block;
      private final ImmutableMap<IProperty<?>, Comparable<?>> properties;
      private ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> propertyValueTable;

      private StateImplementation(Block var1, ImmutableMap<IProperty<?>, Comparable<?>> var2) {
         this.block = ☃;
         this.properties = ☃;
      }

      @Override
      public Collection<IProperty<?>> getPropertyKeys() {
         return Collections.unmodifiableCollection(this.properties.keySet());
      }

      @Override
      public <T extends Comparable<T>> T getValue(IProperty<T> var1) {
         Comparable<?> ☃ = (Comparable<?>)this.properties.get(☃);
         if (☃ == null) {
            throw new IllegalArgumentException("Cannot get property " + ☃ + " as it does not exist in " + this.block.getBlockState());
         } else {
            return ☃.getValueClass().cast(☃);
         }
      }

      @Override
      public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> var1, V var2) {
         Comparable<?> ☃ = (Comparable<?>)this.properties.get(☃);
         if (☃ == null) {
            throw new IllegalArgumentException("Cannot set property " + ☃ + " as it does not exist in " + this.block.getBlockState());
         } else if (☃ == ☃) {
            return this;
         } else {
            IBlockState ☃x = (IBlockState)this.propertyValueTable.get(☃, ☃);
            if (☃x == null) {
               throw new IllegalArgumentException(
                  "Cannot set property " + ☃ + " to " + ☃ + " on block " + Block.REGISTRY.getNameForObject(this.block) + ", it is not an allowed value"
               );
            } else {
               return ☃x;
            }
         }
      }

      @Override
      public ImmutableMap<IProperty<?>, Comparable<?>> getProperties() {
         return this.properties;
      }

      @Override
      public Block getBlock() {
         return this.block;
      }

      @Override
      public boolean equals(Object var1) {
         return this == ☃;
      }

      @Override
      public int hashCode() {
         return this.properties.hashCode();
      }

      public void buildPropertyValueTable(Map<Map<IProperty<?>, Comparable<?>>, BlockStateContainer.StateImplementation> var1) {
         if (this.propertyValueTable != null) {
            throw new IllegalStateException();
         } else {
            Table<IProperty<?>, Comparable<?>, IBlockState> ☃ = HashBasedTable.create();
            UnmodifiableIterator var3 = this.properties.entrySet().iterator();

            while (var3.hasNext()) {
               Entry<IProperty<?>, Comparable<?>> ☃x = (Entry<IProperty<?>, Comparable<?>>)var3.next();
               IProperty<?> ☃xx = ☃x.getKey();

               for (Comparable<?> ☃xxx : ☃xx.getAllowedValues()) {
                  if (☃xxx != ☃x.getValue()) {
                     ☃.put(☃xx, ☃xxx, ☃.get(this.getPropertiesWithValue(☃xx, ☃xxx)));
                  }
               }
            }

            this.propertyValueTable = ImmutableTable.copyOf(☃);
         }
      }

      private Map<IProperty<?>, Comparable<?>> getPropertiesWithValue(IProperty<?> var1, Comparable<?> var2) {
         Map<IProperty<?>, Comparable<?>> ☃ = Maps.newHashMap(this.properties);
         ☃.put(☃, ☃);
         return ☃;
      }

      @Override
      public Material getMaterial() {
         return this.block.getMaterial(this);
      }

      @Override
      public boolean isFullBlock() {
         return this.block.isFullBlock(this);
      }

      @Override
      public boolean canEntitySpawn(Entity var1) {
         return this.block.canEntitySpawn(this, ☃);
      }

      @Override
      public int getLightOpacity() {
         return this.block.getLightOpacity(this);
      }

      @Override
      public int getLightValue() {
         return this.block.getLightValue(this);
      }

      @Override
      public boolean isTranslucent() {
         return this.block.isTranslucent(this);
      }

      @Override
      public boolean useNeighborBrightness() {
         return this.block.getUseNeighborBrightness(this);
      }

      @Override
      public MapColor getMapColor(IBlockAccess var1, BlockPos var2) {
         return this.block.getMapColor(this, ☃, ☃);
      }

      @Override
      public IBlockState withRotation(Rotation var1) {
         return this.block.withRotation(this, ☃);
      }

      @Override
      public IBlockState withMirror(Mirror var1) {
         return this.block.withMirror(this, ☃);
      }

      @Override
      public boolean isFullCube() {
         return this.block.isFullCube(this);
      }

      @Override
      public boolean hasCustomBreakingProgress() {
         return this.block.hasCustomBreakingProgress(this);
      }

      @Override
      public EnumBlockRenderType getRenderType() {
         return this.block.getRenderType(this);
      }

      @Override
      public int getPackedLightmapCoords(IBlockAccess var1, BlockPos var2) {
         return this.block.getPackedLightmapCoords(this, ☃, ☃);
      }

      @Override
      public float getAmbientOcclusionLightValue() {
         return this.block.getAmbientOcclusionLightValue(this);
      }

      @Override
      public boolean isBlockNormalCube() {
         return this.block.isBlockNormalCube(this);
      }

      @Override
      public boolean isNormalCube() {
         return this.block.isNormalCube(this);
      }

      @Override
      public boolean canProvidePower() {
         return this.block.canProvidePower(this);
      }

      @Override
      public int getWeakPower(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
         return this.block.getWeakPower(this, ☃, ☃, ☃);
      }

      @Override
      public boolean hasComparatorInputOverride() {
         return this.block.hasComparatorInputOverride(this);
      }

      @Override
      public int getComparatorInputOverride(World var1, BlockPos var2) {
         return this.block.getComparatorInputOverride(this, ☃, ☃);
      }

      @Override
      public float getBlockHardness(World var1, BlockPos var2) {
         return this.block.getBlockHardness(this, ☃, ☃);
      }

      @Override
      public float getPlayerRelativeBlockHardness(EntityPlayer var1, World var2, BlockPos var3) {
         return this.block.getPlayerRelativeBlockHardness(this, ☃, ☃, ☃);
      }

      @Override
      public int getStrongPower(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
         return this.block.getStrongPower(this, ☃, ☃, ☃);
      }

      @Override
      public EnumPushReaction getPushReaction() {
         return this.block.getPushReaction(this);
      }

      @Override
      public IBlockState getActualState(IBlockAccess var1, BlockPos var2) {
         return this.block.getActualState(this, ☃, ☃);
      }

      @Override
      public AxisAlignedBB getSelectedBoundingBox(World var1, BlockPos var2) {
         return this.block.getSelectedBoundingBox(this, ☃, ☃);
      }

      @Override
      public boolean shouldSideBeRendered(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
         return this.block.shouldSideBeRendered(this, ☃, ☃, ☃);
      }

      @Override
      public boolean isOpaqueCube() {
         return this.block.isOpaqueCube(this);
      }

      @Nullable
      @Override
      public AxisAlignedBB getCollisionBoundingBox(IBlockAccess var1, BlockPos var2) {
         return this.block.getCollisionBoundingBox(this, ☃, ☃);
      }

      @Override
      public void addCollisionBoxToList(World var1, BlockPos var2, AxisAlignedBB var3, List<AxisAlignedBB> var4, @Nullable Entity var5, boolean var6) {
         this.block.addCollisionBoxToList(this, ☃, ☃, ☃, ☃, ☃, ☃);
      }

      @Override
      public AxisAlignedBB getBoundingBox(IBlockAccess var1, BlockPos var2) {
         return this.block.getBoundingBox(this, ☃, ☃);
      }

      @Override
      public RayTraceResult collisionRayTrace(World var1, BlockPos var2, Vec3d var3, Vec3d var4) {
         return this.block.collisionRayTrace(this, ☃, ☃, ☃, ☃);
      }

      @Override
      public boolean isTopSolid() {
         return this.block.isTopSolid(this);
      }

      @Override
      public Vec3d getOffset(IBlockAccess var1, BlockPos var2) {
         return this.block.getOffset(this, ☃, ☃);
      }

      @Override
      public boolean onBlockEventReceived(World var1, BlockPos var2, int var3, int var4) {
         return this.block.eventReceived(this, ☃, ☃, ☃, ☃);
      }

      @Override
      public void neighborChanged(World var1, BlockPos var2, Block var3, BlockPos var4) {
         this.block.neighborChanged(this, ☃, ☃, ☃, ☃);
      }

      @Override
      public boolean causesSuffocation() {
         return this.block.causesSuffocation(this);
      }

      @Override
      public BlockFaceShape getBlockFaceShape(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
         return this.block.getBlockFaceShape(☃, this, ☃, ☃);
      }
   }
}
