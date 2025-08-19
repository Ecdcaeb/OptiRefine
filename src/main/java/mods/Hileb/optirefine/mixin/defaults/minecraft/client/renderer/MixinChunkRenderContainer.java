package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import net.optifine.SmartAnimations;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;

@Mixin(ChunkRenderContainer.class)
public abstract class MixinChunkRenderContainer {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private BitSet animatedSpritesRendered;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private final BitSet animatedSpritesCached = new BitSet();

    @Inject(method = "<init>", at = @At("RETURN"))
    public void logicOfinit(CallbackInfo ci){
        if (SmartAnimations.isActive()) {
            if (this.animatedSpritesRendered != null) {
                SmartAnimations.spritesRendered(this.animatedSpritesRendered);
            } else {
                this.animatedSpritesRendered = this.animatedSpritesCached;
            }

            this.animatedSpritesRendered.clear();
        } else if (this.animatedSpritesRendered != null) {
            SmartAnimations.spritesRendered(this.animatedSpritesRendered);
            this.animatedSpritesRendered = null;
        }
    }

    @Inject(method = "addRenderChunk", at = @At("RETURN"))
    public void afterAddRenderChunk(RenderChunk renderChunkIn, BlockRenderLayer layer, CallbackInfo ci){
        if (this.animatedSpritesRendered != null) {
            BitSet animatedSprites = CompiledChunk_getAnimatedSprites(renderChunkIn.compiledChunk, layer);
            if (animatedSprites != null) {
                this.animatedSpritesRendered.or(animatedSprites);
            }
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.chunk.CompiledChunk getAnimatedSprites (Lnet.minecraft.util.BlockRenderLayer;)Ljava.util.BitSet;")
    private native static BitSet CompiledChunk_getAnimatedSprites(CompiledChunk instance, BlockRenderLayer arg0) ;
}

/*
-- net/minecraft/client/renderer/ChunkRenderContainer.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/ChunkRenderContainer.java	Tue Aug 19 14:59:58 2025
@@ -1,34 +1,56 @@
 package net.minecraft.client.renderer;

 import com.google.common.collect.Lists;
+import java.util.BitSet;
 import java.util.List;
 import net.minecraft.client.renderer.chunk.RenderChunk;
 import net.minecraft.util.BlockRenderLayer;
 import net.minecraft.util.math.BlockPos;
+import net.optifine.SmartAnimations;

 public abstract class ChunkRenderContainer {
    private double viewEntityX;
    private double viewEntityY;
    private double viewEntityZ;
    protected List<RenderChunk> renderChunks = Lists.newArrayListWithCapacity(17424);
    protected boolean initialized;
+   private BitSet animatedSpritesRendered;
+   private final BitSet animatedSpritesCached = new BitSet();

    public void initialize(double var1, double var3, double var5) {
       this.initialized = true;
       this.renderChunks.clear();
       this.viewEntityX = var1;
       this.viewEntityY = var3;
       this.viewEntityZ = var5;
+      if (SmartAnimations.isActive()) {
+         if (this.animatedSpritesRendered != null) {
+            SmartAnimations.spritesRendered(this.animatedSpritesRendered);
+         } else {
+            this.animatedSpritesRendered = this.animatedSpritesCached;
+         }
+
+         this.animatedSpritesRendered.clear();
+      } else if (this.animatedSpritesRendered != null) {
+         SmartAnimations.spritesRendered(this.animatedSpritesRendered);
+         this.animatedSpritesRendered = null;
+      }
    }

    public void preRenderChunk(RenderChunk var1) {
       BlockPos var2 = var1.getPosition();
       GlStateManager.translate((float)(var2.getX() - this.viewEntityX), (float)(var2.getY() - this.viewEntityY), (float)(var2.getZ() - this.viewEntityZ));
    }

    public void addRenderChunk(RenderChunk var1, BlockRenderLayer var2) {
       this.renderChunks.add(var1);
+      if (this.animatedSpritesRendered != null) {
+         BitSet var3 = var1.compiledChunk.getAnimatedSprites(var2);
+         if (var3 != null) {
+            this.animatedSpritesRendered.or(var3);
+         }
+      }
    }

    public abstract void renderChunkLayer(BlockRenderLayer var1);
 }
 */
