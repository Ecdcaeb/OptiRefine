package mods.Hileb.optirefine.mixin.defaults.minecraft.util.math;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChunkPos.class)
public abstract class MixinChunkPos {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private int cachedHashCode = 0;

    @WrapMethod(method = "hashCode", remap = false)
    public int injectHashCode(Operation<Integer> original){
        if (cachedHashCode != 0) {
            return cachedHashCode;
        } else {
            return cachedHashCode = original.call();
        }
    }
}
/*
--- net/minecraft/util/math/ChunkPos.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/util/math/ChunkPos.java	Tue Aug 19 14:59:58 2025
@@ -2,12 +2,13 @@

 import net.minecraft.entity.Entity;

 public class ChunkPos {
    public final int x;
    public final int z;
+   private int cachedHashCode = 0;

    public ChunkPos(int var1, int var2) {
       this.x = var1;
       this.z = var2;
    }

@@ -18,15 +19,20 @@

    public static long asLong(int var0, int var1) {
       return var0 & 4294967295L | (var1 & 4294967295L) << 32;
    }

    public int hashCode() {
-      int var1 = 1664525 * this.x + 1013904223;
-      int var2 = 1664525 * (this.z ^ -559038737) + 1013904223;
-      return var1 ^ var2;
+      if (this.cachedHashCode != 0) {
+         return this.cachedHashCode;
+      } else {
+         int var1 = 1664525 * this.x + 1013904223;
+         int var2 = 1664525 * (this.z ^ -559038737) + 1013904223;
+         this.cachedHashCode = var1 ^ var2;
+         return this.cachedHashCode;
+      }
    }

    public boolean equals(Object var1) {
       if (this == var1) {
          return true;
       } else if (!(var1 instanceof ChunkPos)) {
 */
