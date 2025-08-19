package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.util.BlockRenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.BitSet;

@Mixin(CompiledChunk.class)
public abstract class MixinCompiledChunk {

    @Unique
    private BitSet[] optiRefine$animatedSprites = new BitSet[BlockRenderLayer.values().length];

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public BitSet getAnimatedSprites(BlockRenderLayer layer) {
        return this.optiRefine$animatedSprites[layer.ordinal()];
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public void setAnimatedSprites(BlockRenderLayer layer, BitSet animatedSprites) {
        this.optiRefine$animatedSprites[layer.ordinal()] = animatedSprites;
    }


}
/*
+++ net/minecraft/client/renderer/chunk/CompiledChunk.java	Tue Aug 19 14:59:58 2025
@@ -1,9 +1,10 @@
 package net.minecraft.client.renderer.chunk;

 import com.google.common.collect.Lists;
+import java.util.BitSet;
 import java.util.List;
 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.tileentity.TileEntity;
 import net.minecraft.util.BlockRenderLayer;
 import net.minecraft.util.EnumFacing;

@@ -17,19 +18,24 @@
          throw new UnsupportedOperationException();
       }

       public boolean isVisible(EnumFacing var1, EnumFacing var2) {
          return false;
       }
+
+      public void setAnimatedSprites(BlockRenderLayer var1, BitSet var2) {
+         throw new UnsupportedOperationException();
+      }
    };
-   private final boolean[] layersUsed = new boolean[BlockRenderLayer.values().length];
-   private final boolean[] layersStarted = new boolean[BlockRenderLayer.values().length];
+   private final boolean[] layersUsed = new boolean[RenderChunk.ENUM_WORLD_BLOCK_LAYERS.length];
+   private final boolean[] layersStarted = new boolean[RenderChunk.ENUM_WORLD_BLOCK_LAYERS.length];
    private boolean empty = true;
    private final List<TileEntity> tileEntities = Lists.newArrayList();
    private SetVisibility setVisibility = new SetVisibility();
    private BufferBuilder.State state;
+   private BitSet[] animatedSprites = new BitSet[RenderChunk.ENUM_WORLD_BLOCK_LAYERS.length];

    public boolean isEmpty() {
       return this.empty;
    }

    protected void setLayerUsed(BlockRenderLayer var1) {
@@ -68,8 +74,16 @@
    public BufferBuilder.State getState() {
       return this.state;
    }

    public void setState(BufferBuilder.State var1) {
       this.state = var1;
+   }
+
+   public BitSet getAnimatedSprites(BlockRenderLayer var1) {
+      return this.animatedSprites[var1.ordinal()];
+   }
+
+   public void setAnimatedSprites(BlockRenderLayer var1, BitSet var2) {
+      this.animatedSprites[var1.ordinal()] = var2;
    }
 }
 */
