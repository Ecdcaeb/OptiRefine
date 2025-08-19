package mods.Hileb.optirefine.mixin.defaults.minecraft.world.chunk.storage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.*;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ExtendedBlockStorage.class)
public abstract class MixinExtendedBlockStorage {

    @Shadow
    private int blockRefCount;

    @SuppressWarnings("unused")
    @Shadow
    private int tickRefCount;

    @Shadow @Final
    private BlockStateContainer data;

    @Unique
    private static final IBlockState OPTIREFINE_STATE_AIR = Blocks.AIR.getDefaultState();


    /**
     * @author Hileb
     * @reason makeLocals
     */
    @Overwrite
    public void recalculateRefCounts() {
        int localBlockRefCount = 0;
        int localTickRefCount = 0;
        for (int y = 0; y < 16; ++y) {
            for (int z = 0; z < 16; ++z) {
                for (int x = 0; x < 16; ++x) {
                    IBlockState bs = this.data.get(x, y, z);
                    if (bs == OPTIREFINE_STATE_AIR) continue;
                    ++localBlockRefCount;
                    Block block = bs.getBlock();
                    if (!block.getTickRandomly()) continue;
                    ++localTickRefCount;
                }
            }
        }
        this.blockRefCount = localBlockRefCount;
        this.tickRefCount = localTickRefCount;
    }

    @SuppressWarnings("unused")
    @Unique
    public int getBlockRefCount() {
        return this.blockRefCount;
    }
}

/*
--- net/minecraft/world/chunk/storage/ExtendedBlockStorage.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/world/chunk/storage/ExtendedBlockStorage.java	Tue Aug 19 14:59:58 2025
@@ -2,12 +2,13 @@

 import net.minecraft.block.Block;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.init.Blocks;
 import net.minecraft.world.chunk.BlockStateContainer;
 import net.minecraft.world.chunk.NibbleArray;
+import net.optifine.reflect.Reflector;

 public class ExtendedBlockStorage {
    private final int yBase;
    private int blockRefCount;
    private int tickRefCount;
    private final BlockStateContainer data;
@@ -25,12 +26,16 @@

    public IBlockState get(int var1, int var2, int var3) {
       return this.data.get(var1, var2, var3);
    }

    public void set(int var1, int var2, int var3, IBlockState var4) {
+      if (Reflector.IExtendedBlockState.isInstance(var4)) {
+         var4 = (IBlockState)Reflector.call(var4, Reflector.IExtendedBlockState_getClean, new Object[0]);
+      }
+
       IBlockState var5 = this.get(var1, var2, var3);
       Block var6 = var5.getBlock();
       Block var7 = var4.getBlock();
       if (var6 != Blocks.AIR) {
          this.blockRefCount--;
          if (var6.getTickRandomly()) {
@@ -74,28 +79,33 @@

    public int getBlockLight(int var1, int var2, int var3) {
       return this.blockLight.get(var1, var2, var3);
    }

    public void recalculateRefCounts() {
-      this.blockRefCount = 0;
-      this.tickRefCount = 0;
-
-      for (int var1 = 0; var1 < 16; var1++) {
-         for (int var2 = 0; var2 < 16; var2++) {
-            for (int var3 = 0; var3 < 16; var3++) {
-               Block var4 = this.get(var1, var2, var3).getBlock();
-               if (var4 != Blocks.AIR) {
-                  this.blockRefCount++;
-                  if (var4.getTickRandomly()) {
-                     this.tickRefCount++;
+      IBlockState var1 = Blocks.AIR.getDefaultState();
+      int var2 = 0;
+      int var3 = 0;
+
+      for (int var4 = 0; var4 < 16; var4++) {
+         for (int var5 = 0; var5 < 16; var5++) {
+            for (int var6 = 0; var6 < 16; var6++) {
+               IBlockState var7 = this.data.get(var6, var4, var5);
+               if (var7 != var1) {
+                  var2++;
+                  Block var8 = var7.getBlock();
+                  if (var8.getTickRandomly()) {
+                     var3++;
                   }
                }
             }
          }
       }
+
+      this.blockRefCount = var2;
+      this.tickRefCount = var3;
    }

    public BlockStateContainer getData() {
       return this.data;
    }

@@ -110,8 +120,12 @@
    public void setBlockLight(NibbleArray var1) {
       this.blockLight = var1;
    }

    public void setSkyLight(NibbleArray var1) {
       this.skyLight = var1;
+   }
+
+   public int getBlockRefCount() {
+      return this.blockRefCount;
    }
 }
 */