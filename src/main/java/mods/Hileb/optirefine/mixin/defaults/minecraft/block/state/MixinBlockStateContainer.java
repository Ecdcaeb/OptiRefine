package mods.Hileb.optirefine.mixin.defaults.minecraft.block.state;

import net.minecraft.block.state.BlockStateContainer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockStateContainer.class)
public abstract class MixinBlockStateContainer {
 //NO-OPS
}
/*
+++ net/minecraft/block/state/BlockStateContainer.java	Tue Aug 19 14:59:58 2025
@@ -16,16 +16,19 @@
 import java.util.Collection;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.LinkedHashMap;
 import java.util.List;
 import java.util.Map;
+import java.util.Optional;
 import java.util.Map.Entry;
 import java.util.regex.Pattern;
 import javax.annotation.Nullable;
 import net.minecraft.block.Block;
+import net.minecraft.block.BlockFlower;
+import net.minecraft.block.Block.EnumOffsetType;
 import net.minecraft.block.material.EnumPushReaction;
 import net.minecraft.block.material.MapColor;
 import net.minecraft.block.material.Material;
 import net.minecraft.block.properties.IProperty;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.player.EntityPlayer;
@@ -38,12 +41,15 @@
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.Cartesian;
 import net.minecraft.util.math.RayTraceResult;
 import net.minecraft.util.math.Vec3d;
 import net.minecraft.world.IBlockAccess;
 import net.minecraft.world.World;
+import net.minecraftforge.common.property.IUnlistedProperty;
+import net.optifine.model.BlockModelUtils;
+import net.optifine.reflect.Reflector;

 public class BlockStateContainer {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
    private static final Function<IProperty<?>, String> GET_NAME_FUNC = new Function<IProperty<?>, String>() {
       @Nullable
       public String apply(@Nullable IProperty<?> var1) {
@@ -52,27 +58,37 @@
    };
    private final Block block;
    private final ImmutableSortedMap<String, IProperty<?>> properties;
    private final ImmutableList<IBlockState> validStates;

    public BlockStateContainer(Block var1, IProperty<?>... var2) {
+      this(var1, var2, null);
+   }
+
+   protected BlockStateContainer.StateImplementation createState(
+      Block var1, ImmutableMap<IProperty<?>, Comparable<?>> var2, @Nullable ImmutableMap<IUnlistedProperty<?>, Optional<?>> var3
+   ) {
+      return new BlockStateContainer.StateImplementation(var1, var2);
+   }
+
+   protected BlockStateContainer(Block var1, IProperty<?>[] var2, ImmutableMap<IUnlistedProperty<?>, Optional<?>> var3) {
       this.block = var1;
-      HashMap var3 = Maps.newHashMap();
+      HashMap var4 = Maps.newHashMap();

-      for (IProperty var7 : var2) {
-         validateProperty(var1, var7);
-         var3.put(var7.getName(), var7);
+      for (IProperty var8 : var2) {
+         validateProperty(var1, var8);
+         var4.put(var8.getName(), var8);
       }

-      this.properties = ImmutableSortedMap.copyOf(var3);
+      this.properties = ImmutableSortedMap.copyOf(var4);
       LinkedHashMap var11 = Maps.newLinkedHashMap();
       ArrayList var12 = Lists.newArrayList();

-      for (List var8 : Cartesian.cartesianProduct(this.getAllowedValues())) {
-         Map var9 = MapPopulator.createMap(this.properties.values(), var8);
-         BlockStateContainer.StateImplementation var10 = new BlockStateContainer.StateImplementation(var1, ImmutableMap.copyOf(var9));
+      for (List var15 : Cartesian.cartesianProduct(this.getAllowedValues())) {
+         Map var9 = MapPopulator.createMap(this.properties.values(), var15);
+         BlockStateContainer.StateImplementation var10 = this.createState(var1, ImmutableMap.copyOf(var9), var3);
          var11.put(var9, var10);
          var12.add(var10);
       }

       for (BlockStateContainer.StateImplementation var16 : var12) {
          var16.buildPropertyValueTable(var11);
@@ -84,13 +100,13 @@
    public static <T extends Comparable<T>> String validateProperty(Block var0, IProperty<T> var1) {
       String var2 = var1.getName();
       if (!NAME_PATTERN.matcher(var2).matches()) {
          throw new IllegalArgumentException("Block: " + var0.getClass() + " has invalidly named property: " + var2);
       } else {
          for (Comparable var4 : var1.getAllowedValues()) {
-            String var5 = var1.getName((T)var4);
+            String var5 = var1.getName(var4);
             if (!NAME_PATTERN.matcher(var5).matches()) {
                throw new IllegalArgumentException("Block: " + var0.getClass() + " has property: " + var2 + " with invalidly named value: " + var5);
             }
          }

          return var2;
@@ -135,22 +151,66 @@

    @Nullable
    public IProperty<?> getProperty(String var1) {
       return (IProperty<?>)this.properties.get(var1);
    }

+   public static class Builder {
+      private final Block block;
+      private final List<IProperty<?>> listed = Lists.newArrayList();
+      private final List<IUnlistedProperty<?>> unlisted = Lists.newArrayList();
+
+      public Builder(Block var1) {
+         this.block = var1;
+      }
+
+      public BlockStateContainer.Builder add(IProperty<?>... var1) {
+         for (IProperty var5 : var1) {
+            this.listed.add(var5);
+         }
+
+         return this;
+      }
+
+      public BlockStateContainer.Builder add(IUnlistedProperty<?>... var1) {
+         for (IUnlistedProperty var5 : var1) {
+            this.unlisted.add(var5);
+         }
+
+         return this;
+      }
+
+      public BlockStateContainer build() {
+         IProperty[] var1 = new IProperty[this.listed.size()];
+         var1 = this.listed.toArray(var1);
+         if (this.unlisted.size() == 0) {
+            return new BlockStateContainer(this.block, var1);
+         } else {
+            IUnlistedProperty[] var2 = new IUnlistedProperty[this.unlisted.size()];
+            var2 = this.unlisted.toArray(var2);
+            return (BlockStateContainer)Reflector.newInstance(Reflector.ExtendedBlockState_Constructor, new Object[]{this.block, var1, var2});
+         }
+      }
+   }
+
    static class StateImplementation extends BlockStateBase {
       private final Block block;
       private final ImmutableMap<IProperty<?>, Comparable<?>> properties;
       private ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> propertyValueTable;

       private StateImplementation(Block var1, ImmutableMap<IProperty<?>, Comparable<?>> var2) {
          this.block = var1;
          this.properties = var2;
       }

+      protected StateImplementation(Block var1, ImmutableMap<IProperty<?>, Comparable<?>> var2, ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> var3) {
+         this.block = var1;
+         this.properties = var2;
+         this.propertyValueTable = var3;
+      }
+
       public Collection<IProperty<?>> getPropertyKeys() {
          return Collections.unmodifiableCollection(this.properties.keySet());
       }

       public <T extends Comparable<T>> T getValue(IProperty<T> var1) {
          Comparable var2 = (Comparable)this.properties.get(var1);
@@ -346,13 +406,19 @@

       public void addCollisionBoxToList(World var1, BlockPos var2, AxisAlignedBB var3, List<AxisAlignedBB> var4, @Nullable Entity var5, boolean var6) {
          this.block.addCollisionBoxToList(this, var1, var2, var3, var4, var5, var6);
       }

       public AxisAlignedBB getBoundingBox(IBlockAccess var1, BlockPos var2) {
-         return this.block.getBoundingBox(this, var1, var2);
+         EnumOffsetType var3 = this.block.getOffsetType();
+         if (var3 != EnumOffsetType.NONE && !(this.block instanceof BlockFlower)) {
+            AxisAlignedBB var4 = this.block.getBoundingBox(this, var1, var2);
+            return BlockModelUtils.getOffsetBoundingBox(var4, var3, var2);
+         } else {
+            return this.block.getBoundingBox(this, var1, var2);
+         }
       }

       public RayTraceResult collisionRayTrace(World var1, BlockPos var2, Vec3d var3, Vec3d var4) {
          return this.block.collisionRayTrace(this, var1, var2, var3, var4);
       }

@@ -371,12 +437,36 @@
       public void neighborChanged(World var1, BlockPos var2, Block var3, BlockPos var4) {
          this.block.neighborChanged(this, var1, var2, var3, var4);
       }

       public boolean causesSuffocation() {
          return this.block.causesSuffocation(this);
+      }
+
+      public ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> getPropertyValueTable() {
+         return this.propertyValueTable;
+      }
+
+      public int getLightOpacity(IBlockAccess var1, BlockPos var2) {
+         return Reflector.callInt(this.block, Reflector.ForgeBlock_getLightOpacity, new Object[]{this, var1, var2});
+      }
+
+      public int getLightValue(IBlockAccess var1, BlockPos var2) {
+         return Reflector.callInt(this.block, Reflector.ForgeBlock_getLightValue, new Object[]{this, var1, var2});
+      }
+
+      public boolean isSideSolid(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
+         return Reflector.callBoolean(this.block, Reflector.ForgeBlock_isSideSolid, new Object[]{this, var1, var2, var3});
+      }
+
+      public boolean doesSideBlockChestOpening(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
+         return Reflector.callBoolean(this.block, Reflector.ForgeBlock_doesSideBlockChestOpening, new Object[]{this, var1, var2, var3});
+      }
+
+      public boolean doesSideBlockRendering(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
+         return Reflector.callBoolean(this.block, Reflector.ForgeBlock_doesSideBlockRendering, new Object[]{this, var1, var2, var3});
       }

       public BlockFaceShape getBlockFaceShape(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
          return this.block.getBlockFaceShape(var1, this, var2, var3);
       }
    }
*/
