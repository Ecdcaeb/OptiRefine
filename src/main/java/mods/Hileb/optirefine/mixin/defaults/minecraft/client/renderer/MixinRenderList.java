package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.common.utils.Checked;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderList;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.nio.Buffer;
import java.nio.IntBuffer;

@Checked

@Mixin(RenderList.class)
public abstract class MixinRenderList extends ChunkRenderContainer {
    @Unique
    private double optiRefine$viewEntityX;
    @Unique
    private double optiRefine$viewEntityY;
    @Unique
    private double optiRefine$viewEntityZ;

    @Unique
    IntBuffer bufferLists = GLAllocation.createDirectIntBuffer(16);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk regionX I")
    private static native int RenderChunk_regionX(RenderChunk r);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk regionZ I")
    private static native int RenderChunk_regionZ(RenderChunk r);

    @WrapMethod(method = "renderChunkLayer")
    private void addElseForRender(BlockRenderLayer layer, Operation<Void> original){
        if (this.initialized) {
            if (!Config.isRenderRegions()) {
                for (RenderChunk renderChunk : this.renderChunks) {
                    ListedRenderChunk listedRenderChunk = (ListedRenderChunk) renderChunk;
                    GlStateManager.pushMatrix();
                    this.preRenderChunk(renderChunk);
                    GlStateManager.callList(listedRenderChunk.getDisplayList(layer, listedRenderChunk.getCompiledChunk()));
                    GlStateManager.popMatrix();
                }
            } else {
                int var2 = Integer.MIN_VALUE;
                int var3 = Integer.MIN_VALUE;
                for (RenderChunk var5 : this.renderChunks) {
                    ListedRenderChunk var6 = (ListedRenderChunk)var5;
                    if (var2 != RenderChunk_regionX(var5) || var3 != RenderChunk_regionZ(var5)) {
                        if (this.bufferLists.position() > 0) {
                            this.optiRefine$drawRegion(var2, var3, this.bufferLists);
                        }
                        var2 = RenderChunk_regionX(var5);
                        var3 = RenderChunk_regionZ(var5);
                    }
                    if (this.bufferLists.position() >= this.bufferLists.capacity()) {
                        IntBuffer var7 = GLAllocation.createDirectIntBuffer(this.bufferLists.capacity() * 2);
                        ((Buffer)this.bufferLists).flip();
                        var7.put(this.bufferLists);
                        this.bufferLists = var7;
                    }
                    this.bufferLists.put(var6.getDisplayList(layer, var6.getCompiledChunk()));
                }
                if (this.bufferLists.position() > 0) {
                    this.optiRefine$drawRegion(var2, var3, this.bufferLists);
                }
            } if (Config.isMultiTexture()) {
                GlStateManager_bindCurrentTexture();
            }


            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }

    @Override
    public void initialize(double var1, double var3, double var5) {
        this.optiRefine$viewEntityX = var1;
        this.optiRefine$viewEntityY = var3;
        this.optiRefine$viewEntityZ = var5;
        super.initialize(var1, var3, var5);
    }

    @Unique
    private void optiRefine$drawRegion(int var1, int var2, IntBuffer var3) {
        GlStateManager.pushMatrix();
        this.preRenderRegion(var1, 0, var2);
        ((Buffer)var3).flip();
        GlStateManager_callLists(var3);
        ((Buffer)var3).clear();
        GlStateManager.popMatrix();
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public void preRenderRegion(int var1, int var2, int var3) {
        GlStateManager.translate((float)(var1 - this.optiRefine$viewEntityX), (float)(var2 - this.optiRefine$viewEntityY), (float)(var3 - this.optiRefine$viewEntityZ));
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation.Reference(GlStateManager.class)
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager bindCurrentTexture ()V")
    private static native void GlStateManager_bindCurrentTexture();

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation.Reference(GlStateManager.class)
    @AccessibleOperation.Reference(IntBuffer.class)
    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager callLists (Ljava.nio.IntBuffer;)V")
    private static native void GlStateManager_callLists(IntBuffer intBuffer);
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
