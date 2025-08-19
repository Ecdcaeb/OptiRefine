package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VisGraph.class)
public abstract class MixinVisGraph {

    @Redirect(method = "floodFill", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/EnumFacing;values()[Lnet/minecraft/util/EnumFacing;"))
    public EnumFacing[] redirectEnumFacing_values(){
        return EnumFacing.VALUES;
    }

}
/*
+++ net/minecraft/client/renderer/chunk/VisGraph.java	Tue Aug 19 14:59:58 2025
@@ -1,9 +1,8 @@
 package net.minecraft.client.renderer.chunk;

-import com.google.common.collect.Queues;
 import java.util.ArrayDeque;
 import java.util.BitSet;
 import java.util.EnumSet;
 import java.util.Set;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.IntegerCache;
@@ -50,21 +49,21 @@
    public Set<EnumFacing> getVisibleFacings(BlockPos var1) {
       return this.floodFill(getIndex(var1));
    }

    private Set<EnumFacing> floodFill(int var1) {
       EnumSet var2 = EnumSet.noneOf(EnumFacing.class);
-      ArrayDeque var3 = Queues.newArrayDeque();
+      ArrayDeque var3 = new ArrayDeque(384);
       var3.add(IntegerCache.getInteger(var1));
       this.bitSet.set(var1, true);

       while (!var3.isEmpty()) {
          int var4 = (Integer)var3.poll();
          this.addEdges(var4, var2);

-         for (EnumFacing var8 : EnumFacing.values()) {
+         for (EnumFacing var8 : EnumFacing.VALUES) {
             int var9 = this.getNeighborIndexAtFace(var4, var8);
             if (var9 >= 0 && !this.bitSet.get(var9)) {
                this.bitSet.set(var9, true);
                var3.add(IntegerCache.getInteger(var9));
             }
          }
 */
