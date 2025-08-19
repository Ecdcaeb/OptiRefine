package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

public class MixinRenderList {
}
/*
--- net/minecraft/client/renderer/RenderList.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/RenderList.java	Tue Aug 19 14:59:58 2025
@@ -1,22 +1,83 @@
 package net.minecraft.client.renderer;

+import java.nio.Buffer;
+import java.nio.IntBuffer;
 import net.minecraft.client.renderer.chunk.ListedRenderChunk;
 import net.minecraft.client.renderer.chunk.RenderChunk;
 import net.minecraft.util.BlockRenderLayer;

 public class RenderList extends ChunkRenderContainer {
+   private double viewEntityX;
+   private double viewEntityY;
+   private double viewEntityZ;
+   IntBuffer bufferLists = GLAllocation.createDirectIntBuffer(16);
+
    public void renderChunkLayer(BlockRenderLayer var1) {
       if (this.initialized) {
-         for (RenderChunk var3 : this.renderChunks) {
-            ListedRenderChunk var4 = (ListedRenderChunk)var3;
-            GlStateManager.pushMatrix();
-            this.preRenderChunk(var3);
-            GlStateManager.callList(var4.getDisplayList(var1, var4.getCompiledChunk()));
-            GlStateManager.popMatrix();
+         if (!Config.isRenderRegions()) {
+            for (RenderChunk var9 : this.renderChunks) {
+               ListedRenderChunk var10 = (ListedRenderChunk)var9;
+               GlStateManager.pushMatrix();
+               this.preRenderChunk(var9);
+               GlStateManager.callList(var10.getDisplayList(var1, var10.h()));
+               GlStateManager.popMatrix();
+            }
+         } else {
+            int var2 = Integer.MIN_VALUE;
+            int var3 = Integer.MIN_VALUE;
+
+            for (RenderChunk var5 : this.renderChunks) {
+               ListedRenderChunk var6 = (ListedRenderChunk)var5;
+               if (var2 != var5.regionX || var3 != var5.regionZ) {
+                  if (this.bufferLists.position() > 0) {
+                     this.drawRegion(var2, var3, this.bufferLists);
+                  }
+
+                  var2 = var5.regionX;
+                  var3 = var5.regionZ;
+               }
+
+               if (this.bufferLists.position() >= this.bufferLists.capacity()) {
+                  IntBuffer var7 = GLAllocation.createDirectIntBuffer(this.bufferLists.capacity() * 2);
+                  ((Buffer)this.bufferLists).flip();
+                  var7.put(this.bufferLists);
+                  this.bufferLists = var7;
+               }
+
+               this.bufferLists.put(var6.getDisplayList(var1, var6.h()));
+            }
+
+            if (this.bufferLists.position() > 0) {
+               this.drawRegion(var2, var3, this.bufferLists);
+            }
+         }
+
+         if (Config.isMultiTexture()) {
+            GlStateManager.bindCurrentTexture();
          }

          GlStateManager.resetColor();
          this.renderChunks.clear();
       }
+   }
+
+   public void initialize(double var1, double var3, double var5) {
+      this.viewEntityX = var1;
+      this.viewEntityY = var3;
+      this.viewEntityZ = var5;
+      super.initialize(var1, var3, var5);
+   }
+
+   private void drawRegion(int var1, int var2, IntBuffer var3) {
+      GlStateManager.pushMatrix();
+      this.preRenderRegion(var1, 0, var2);
+      ((Buffer)var3).flip();
+      GlStateManager.callLists(var3);
+      ((Buffer)var3).clear();
+      GlStateManager.popMatrix();
+   }
+
+   public void preRenderRegion(int var1, int var2, int var3) {
+      GlStateManager.translate((float)(var1 - this.viewEntityX), (float)(var2 - this.viewEntityY), (float)(var3 - this.viewEntityZ));
    }
 }
 */
