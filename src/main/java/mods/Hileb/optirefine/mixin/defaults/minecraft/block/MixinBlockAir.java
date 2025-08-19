package mods.Hileb.optirefine.mixin.defaults.minecraft.block;

import mods.Hileb.optirefine.library.common.utils.Checked;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.IdentityHashMap;
import java.util.Map;

@Checked
@Mixin(BlockAir.class)
public abstract class MixinBlockAir{

    @SuppressWarnings({"unchecked", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
    private static Map<Block, Integer> mapOriginalOpacity = new IdentityHashMap();

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private static void setLightOpacity(Block block, int opacity) {
         if (!mapOriginalOpacity.containsKey(block)) {
             mapOriginalOpacity.put(block, block.lightOpacity);
         }
        block.lightOpacity = opacity;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    @Public
    private static void restoreLightOpacity(Block block) {
         if (!mapOriginalOpacity.containsKey(block)) {
             return;
         }
         int opacity = mapOriginalOpacity.get(block);
         setLightOpacity(block, opacity);
    }
}
/*

+++ net/minecraft/block/BlockAir.java	Tue Aug 19 14:59:58 2025
@@ -1,20 +1,24 @@
 package net.minecraft.block;

+import java.util.IdentityHashMap;
+import java.util.Map;
 import javax.annotation.Nullable;
 import net.minecraft.block.material.Material;
 import net.minecraft.block.state.BlockFaceShape;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.util.EnumBlockRenderType;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.math.AxisAlignedBB;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.world.IBlockAccess;
 import net.minecraft.world.World;

 public class BlockAir extends Block {
+   private static Map mapOriginalOpacity = new IdentityHashMap();
+
    protected BlockAir() {
       super(Material.AIR);
    }

    public EnumBlockRenderType getRenderType(IBlockState var1) {
       return EnumBlockRenderType.INVISIBLE;
@@ -39,12 +43,27 @@
    public boolean isReplaceable(IBlockAccess var1, BlockPos var2) {
       return true;
    }

    public boolean isFullCube(IBlockState var1) {
       return false;
+   }
+
+   public static void setLightOpacity(Block var0, int var1) {
+      if (!mapOriginalOpacity.containsKey(var0)) {
+         mapOriginalOpacity.put(var0, var0.lightOpacity);
+      }
+
+      var0.lightOpacity = var1;
+   }
+
+   public static void restoreLightOpacity(Block var0) {
+      if (mapOriginalOpacity.containsKey(var0)) {
+         int var1 = (Integer)mapOriginalOpacity.get(var0);
+         setLightOpacity(var0, var1);
+      }
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
       return BlockFaceShape.UNDEFINED;
    }
 }

*/
