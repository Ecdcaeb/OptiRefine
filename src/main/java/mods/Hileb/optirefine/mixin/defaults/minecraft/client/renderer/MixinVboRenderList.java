package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.VboRenderList;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.optifine.render.VboRegion;
import net.optifine.shaders.ShadersRender;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VboRenderList.class)
public abstract class MixinVboRenderList extends ChunkRenderContainer {
    @Unique
    private double optiRefine$viewEntityX;
    @Unique
    private double optiRefine$viewEntityY;
    @Unique
    private double optiRefine$viewEntityZ;

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.vertex.VertexBuffer getVboRegion ()Lnet.optifine.render.VboRegion;")
    private static native VboRegion VertexBuffer_getVboRegion(VertexBuffer vertexBuffer);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk regionX I")
    private static native int RenderChunk_regionX(RenderChunk renderChunk);

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.chunk.RenderChunk regionZ I")
    private static native int RenderChunk_regionZ(RenderChunk renderChunk);

    @ModifyExpressionValue(method = "renderChunkLayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/VboRenderList;initialized:Z"))
    public boolean shouldRender(boolean original, @Local(argsOnly = true) BlockRenderLayer layer){
        if (original) {
            if (Config.isRenderRegions()){
                int regionX = Integer.MIN_VALUE;
                int regionZ = Integer.MIN_VALUE;
                VboRegion lastVboRegion = null;

                for (RenderChunk renderchunk : this.renderChunks) {
                    VertexBuffer vertexbuffer = renderchunk.getVertexBufferByLayer(layer.ordinal());
                    VboRegion vboRegion = VertexBuffer_getVboRegion(vertexbuffer);
                    if (vboRegion != lastVboRegion || regionX != RenderChunk_regionX(renderchunk) || regionZ != RenderChunk_regionZ(renderchunk)) {
                        if (lastVboRegion != null) {
                            this.optiRefine$drawRegion(regionX, regionZ, lastVboRegion);
                        }

                        regionX = RenderChunk_regionX(renderchunk);
                        regionZ = RenderChunk_regionZ(renderchunk);
                        lastVboRegion = vboRegion;
                    }

                    vertexbuffer.drawArrays(7);
                }

                if (lastVboRegion != null) {
                    this.optiRefine$drawRegion(regionX, regionZ, lastVboRegion);
                }
            }
        }
        return false;
    }

    @Inject(method = "renderChunkLayer", at = @At("TAIL"))
    public void afterRender(BlockRenderLayer layer, CallbackInfo ci){
        if (this.initialized) {
            OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }

    @WrapMethod(method = "setupArrayPointers")
    public void setupArrayPointersForShaders(Operation<Void> original){
        if (Config.isShaders()) {
            ShadersRender.setupArrayPointersVbo();
        } else {
            original.call();
        }
    }

    @Override
    public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
        this.optiRefine$viewEntityX = viewEntityXIn;
        this.optiRefine$viewEntityY = viewEntityYIn;
        this.optiRefine$viewEntityZ = viewEntityZIn;
        super.initialize(viewEntityXIn, viewEntityYIn, viewEntityZIn);
    }

    @Unique
    private void optiRefine$drawRegion(int regionX, int regionZ, VboRegion vboRegion) {
        GlStateManager.pushMatrix();
        this.optiRefine$preRenderRegion(regionX, 0, regionZ);
        vboRegion.finishDraw((VboRenderList)(Object)this);
        GlStateManager.popMatrix();
    }

    @Unique
    public void optiRefine$preRenderRegion(int x, int y, int z) {
        GlStateManager.translate((float)(x - this.optiRefine$viewEntityX), (float)(y - this.optiRefine$viewEntityY), (float)(z - this.optiRefine$viewEntityZ));
    }
}
/*

--- net/minecraft/client/renderer/VboRenderList.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/VboRenderList.java	Tue Aug 19 14:59:58 2025
@@ -1,35 +1,89 @@
 package net.minecraft.client.renderer;

 import net.minecraft.client.renderer.chunk.RenderChunk;
 import net.minecraft.client.renderer.vertex.VertexBuffer;
 import net.minecraft.util.BlockRenderLayer;
+import net.optifine.render.VboRegion;
+import net.optifine.shaders.ShadersRender;

 public class VboRenderList extends ChunkRenderContainer {
+   private double viewEntityX;
+   private double viewEntityY;
+   private double viewEntityZ;
+
    public void renderChunkLayer(BlockRenderLayer var1) {
       if (this.initialized) {
-         for (RenderChunk var3 : this.renderChunks) {
-            VertexBuffer var4 = var3.getVertexBufferByLayer(var1.ordinal());
-            GlStateManager.pushMatrix();
-            this.preRenderChunk(var3);
-            var3.multModelviewMatrix();
-            var4.bindBuffer();
-            this.setupArrayPointers();
-            var4.drawArrays(7);
-            GlStateManager.popMatrix();
+         if (!Config.isRenderRegions()) {
+            for (RenderChunk var10 : this.renderChunks) {
+               VertexBuffer var11 = var10.getVertexBufferByLayer(var1.ordinal());
+               GlStateManager.pushMatrix();
+               this.preRenderChunk(var10);
+               var10.multModelviewMatrix();
+               var11.bindBuffer();
+               this.setupArrayPointers();
+               var11.drawArrays(7);
+               GlStateManager.popMatrix();
+            }
+         } else {
+            int var2 = Integer.MIN_VALUE;
+            int var3 = Integer.MIN_VALUE;
+            VboRegion var4 = null;
+
+            for (RenderChunk var6 : this.renderChunks) {
+               VertexBuffer var7 = var6.getVertexBufferByLayer(var1.ordinal());
+               VboRegion var8 = var7.getVboRegion();
+               if (var8 != var4 || var2 != var6.regionX || var3 != var6.regionZ) {
+                  if (var4 != null) {
+                     this.drawRegion(var2, var3, var4);
+                  }
+
+                  var2 = var6.regionX;
+                  var3 = var6.regionZ;
+                  var4 = var8;
+               }
+
+               var7.drawArrays(7);
+            }
+
+            if (var4 != null) {
+               this.drawRegion(var2, var3, var4);
+            }
          }

          OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
          GlStateManager.resetColor();
          this.renderChunks.clear();
       }
    }

-   private void setupArrayPointers() {
-      GlStateManager.glVertexPointer(3, 5126, 28, 0);
-      GlStateManager.glColorPointer(4, 5121, 28, 12);
-      GlStateManager.glTexCoordPointer(2, 5126, 28, 16);
-      OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
-      GlStateManager.glTexCoordPointer(2, 5122, 28, 24);
-      OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
+   public void setupArrayPointers() {
+      if (Config.isShaders()) {
+         ShadersRender.setupArrayPointersVbo();
+      } else {
+         GlStateManager.glVertexPointer(3, 5126, 28, 0);
+         GlStateManager.glColorPointer(4, 5121, 28, 12);
+         GlStateManager.glTexCoordPointer(2, 5126, 28, 16);
+         OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
+         GlStateManager.glTexCoordPointer(2, 5122, 28, 24);
+         OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
+      }
+   }
+
+   public void initialize(double var1, double var3, double var5) {
+      this.viewEntityX = var1;
+      this.viewEntityY = var3;
+      this.viewEntityZ = var5;
+      super.initialize(var1, var3, var5);
+   }
+
+   private void drawRegion(int var1, int var2, VboRegion var3) {
+      GlStateManager.pushMatrix();
+      this.preRenderRegion(var1, 0, var2);
+      var3.finishDraw(this);
+      GlStateManager.popMatrix();
+   }
+
+   public void preRenderRegion(int var1, int var2, int var3) {
+      GlStateManager.translate((float)(var1 - this.viewEntityX), (float)(var2 - this.viewEntityY), (float)(var3 - this.viewEntityZ));
    }
 }
 */
