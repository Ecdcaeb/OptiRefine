package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.optifine.render.VboRegion;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("SpellCheckingInspection")
@Mixin(ViewFrustum.class)
public abstract class MixinViewFrustum {

    @Shadow
    public RenderChunk[] renderChunks;

    @Unique
    private Map<ChunkPos, VboRegion[]> optiRefine$mapVboRegions = new HashMap<>();

    @WrapOperation(method = "createRenderChunks", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;setPosition(III)V"))
    public void updateMapVboRegions(RenderChunk instance, int i, int x, int y, Operation<Void> original){
        original.call(instance, i, x, y);
        if (Config.isVbo() && Config.isRenderRegions()) {
            this.optiRefine$updateVboRegion(instance);
        }
    }

    @Inject(method = "createRenderChunks", at = @At("TAIL"))
    public void postUpdateVboRegion(IRenderChunkFactory renderChunkFactory, CallbackInfo ci){
        for (RenderChunk renderChunk : this.renderChunks) {
            for (int l = 0; l < EnumFacing.VALUES.length; l++) {
                EnumFacing facing = EnumFacing.VALUES[l];
                BlockPos posOffset16 = renderChunk.getBlockPosOffset16(facing);
                RenderChunk neighbour = this.getRenderChunk(posOffset16);
                RenderChunk_setRenderChunkNeighbour(renderChunk, facing, neighbour);
            }
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.chunk.RenderChunk setRenderChunkNeighbour (Lnet.minecraft.util.EnumFacing;Lnet.minecraft.client.renderer.chunk.RenderChunk;)V")
    private static native void RenderChunk_setRenderChunkNeighbour(RenderChunk renderChunk, EnumFacing enumFacing, RenderChunk neighbour);

    @Shadow
    protected abstract RenderChunk getRenderChunk(BlockPos pos);

    @Inject(method = "deleteGlResources", at = @At("TAIL"))
    public void extraDeleteVboRegions(CallbackInfo ci){
        this.deleteVboRegions();
    }

    @Unique
    private void optiRefine$updateVboRegion(RenderChunk renderChunk) {
        BlockPos pos = renderChunk.getPosition();
        int rx = pos.getX() >> 8 << 8;
        int rz = pos.getZ() >> 8 << 8;
        ChunkPos cp = new ChunkPos(rx, rz);
        BlockRenderLayer[] layers = BlockRenderLayer.values();
        VboRegion[] regions = this.optiRefine$mapVboRegions.get(cp);
        if (regions == null) {
            regions = new VboRegion[layers.length];

            for (int ix = 0; ix < layers.length; ix++) {
                regions[ix] = new VboRegion(layers[ix]);
            }

            this.optiRefine$mapVboRegions.put(cp, regions);
        }

        for (int ix = 0; ix < layers.length; ix++) {
            VboRegion vr = regions[ix];
            if (vr != null) {
                VertexBuffer_setVboRegion( renderChunk.getVertexBufferByLayer(ix), vr);
            }
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.vertex.VertexBuffer setVboRegion (Lnet.optifine.render.VboRegion;)V")
    private static native void VertexBuffer_setVboRegion(VertexBuffer vertexBuffer, VboRegion vboRegion);

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public void deleteVboRegions() {
        for (ChunkPos cp : this.optiRefine$mapVboRegions.keySet()) {
            VboRegion[] vboRegions = this.optiRefine$mapVboRegions.get(cp);

            for (int i = 0; i < vboRegions.length; i++) {
                VboRegion vboRegion = vboRegions[i];
                if (vboRegion != null) {
                    vboRegion.deleteGlBuffers();
                }

                vboRegions[i] = null;
            }
        }

        this.optiRefine$mapVboRegions.clear();
    }
}
/*

--- net/minecraft/client/renderer/ViewFrustum.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/ViewFrustum.java	Tue Aug 19 14:59:58 2025
@@ -1,22 +1,29 @@
 package net.minecraft.client.renderer;

+import java.util.HashMap;
+import java.util.Map;
 import javax.annotation.Nullable;
 import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
 import net.minecraft.client.renderer.chunk.RenderChunk;
+import net.minecraft.util.BlockRenderLayer;
+import net.minecraft.util.EnumFacing;
 import net.minecraft.util.math.BlockPos;
+import net.minecraft.util.math.ChunkPos;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.world.World;
+import net.optifine.render.VboRegion;

 public class ViewFrustum {
    protected final RenderGlobal renderGlobal;
    protected final World world;
    protected int countChunksY;
    protected int countChunksX;
    protected int countChunksZ;
    public RenderChunk[] renderChunks;
+   private Map<ChunkPos, VboRegion[]> mapVboRegions = new HashMap<>();

    public ViewFrustum(World var1, int var2, RenderGlobal var3, IRenderChunkFactory var4) {
       this.renderGlobal = var3;
       this.world = var1;
       this.setCountChunksXYZ(var2);
       this.createRenderChunks(var4);
@@ -30,21 +37,37 @@
       for (int var4 = 0; var4 < this.countChunksX; var4++) {
          for (int var5 = 0; var5 < this.countChunksY; var5++) {
             for (int var6 = 0; var6 < this.countChunksZ; var6++) {
                int var7 = (var6 * this.countChunksY + var5) * this.countChunksX + var4;
                this.renderChunks[var7] = var1.create(this.world, this.renderGlobal, var3++);
                this.renderChunks[var7].setPosition(var4 * 16, var5 * 16, var6 * 16);
+               if (Config.isVbo() && Config.isRenderRegions()) {
+                  this.updateVboRegion(this.renderChunks[var7]);
+               }
             }
          }
       }
+
+      for (int var10 = 0; var10 < this.renderChunks.length; var10++) {
+         RenderChunk var11 = this.renderChunks[var10];
+
+         for (int var12 = 0; var12 < EnumFacing.VALUES.length; var12++) {
+            EnumFacing var13 = EnumFacing.VALUES[var12];
+            BlockPos var8 = var11.getBlockPosOffset16(var13);
+            RenderChunk var9 = this.getRenderChunk(var8);
+            var11.setRenderChunkNeighbour(var13, var9);
+         }
+      }
    }

    public void deleteGlResources() {
       for (RenderChunk var4 : this.renderChunks) {
          var4.deleteGlResources();
       }
+
+      this.deleteVboRegions();
    }

    protected void setCountChunksXYZ(int var1) {
       int var2 = var1 * 2 + 1;
       this.countChunksX = var2;
       this.countChunksY = 16;
@@ -113,16 +136,16 @@
             }
          }
       }
    }

    @Nullable
-   protected RenderChunk getRenderChunk(BlockPos var1) {
-      int var2 = MathHelper.intFloorDiv(var1.getX(), 16);
-      int var3 = MathHelper.intFloorDiv(var1.getY(), 16);
-      int var4 = MathHelper.intFloorDiv(var1.getZ(), 16);
+   public RenderChunk getRenderChunk(BlockPos var1) {
+      int var2 = var1.getX() >> 4;
+      int var3 = var1.getY() >> 4;
+      int var4 = var1.getZ() >> 4;
       if (var3 >= 0 && var3 < this.countChunksY) {
          var2 %= this.countChunksX;
          if (var2 < 0) {
             var2 += this.countChunksX;
          }

@@ -133,8 +156,50 @@

          int var5 = (var4 * this.countChunksY + var3) * this.countChunksX + var2;
          return this.renderChunks[var5];
       } else {
          return null;
       }
+   }
+
+   private void updateVboRegion(RenderChunk var1) {
+      BlockPos var2 = var1.getPosition();
+      int var3 = var2.getX() >> 8 << 8;
+      int var4 = var2.getZ() >> 8 << 8;
+      ChunkPos var5 = new ChunkPos(var3, var4);
+      BlockRenderLayer[] var6 = BlockRenderLayer.values();
+      VboRegion[] var7 = this.mapVboRegions.get(var5);
+      if (var7 == null) {
+         var7 = new VboRegion[var6.length];
+
+         for (int var8 = 0; var8 < var6.length; var8++) {
+            var7[var8] = new VboRegion(var6[var8]);
+         }
+
+         this.mapVboRegions.put(var5, var7);
+      }
+
+      for (int var10 = 0; var10 < var6.length; var10++) {
+         VboRegion var9 = var7[var10];
+         if (var9 != null) {
+            var1.getVertexBufferByLayer(var10).setVboRegion(var9);
+         }
+      }
+   }
+
+   public void deleteVboRegions() {
+      for (ChunkPos var3 : this.mapVboRegions.keySet()) {
+         VboRegion[] var4 = this.mapVboRegions.get(var3);
+
+         for (int var5 = 0; var5 < var4.length; var5++) {
+            VboRegion var6 = var4[var5];
+            if (var6 != null) {
+               var6.deleteGlBuffers();
+            }
+
+            var4[var5] = null;
+         }
+      }
+
+      this.mapVboRegions.clear();
    }
 }
 */
