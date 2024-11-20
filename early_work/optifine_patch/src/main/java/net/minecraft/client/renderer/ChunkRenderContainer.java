/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  java.lang.Object
 *  java.util.BitSet
 *  java.util.List
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.chunk.RenderChunk
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.math.BlockPos
 *  net.optifine.SmartAnimations
 */
package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import java.util.BitSet;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.optifine.SmartAnimations;

public abstract class ChunkRenderContainer {
    private double viewEntityX;
    private double viewEntityY;
    private double viewEntityZ;
    protected List<RenderChunk> renderChunks = Lists.newArrayListWithCapacity((int)17424);
    protected boolean initialized;
    private BitSet animatedSpritesRendered;
    private final BitSet animatedSpritesCached = new BitSet();

    public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
        this.initialized = true;
        this.renderChunks.clear();
        this.viewEntityX = viewEntityXIn;
        this.viewEntityY = viewEntityYIn;
        this.viewEntityZ = viewEntityZIn;
        if (SmartAnimations.isActive()) {
            if (this.animatedSpritesRendered != null) {
                SmartAnimations.spritesRendered((BitSet)this.animatedSpritesRendered);
            } else {
                this.animatedSpritesRendered = this.animatedSpritesCached;
            }
            this.animatedSpritesRendered.clear();
        } else if (this.animatedSpritesRendered != null) {
            SmartAnimations.spritesRendered((BitSet)this.animatedSpritesRendered);
            this.animatedSpritesRendered = null;
        }
    }

    public void preRenderChunk(RenderChunk renderChunkIn) {
        BlockPos blockpos = renderChunkIn.getPosition();
        GlStateManager.translate((float)((float)((double)blockpos.p() - this.viewEntityX)), (float)((float)((double)blockpos.q() - this.viewEntityY)), (float)((float)((double)blockpos.r() - this.viewEntityZ)));
    }

    public void addRenderChunk(RenderChunk renderChunkIn, BlockRenderLayer layer) {
        BitSet animatedSprites;
        this.renderChunks.add((Object)renderChunkIn);
        if (this.animatedSpritesRendered != null && (animatedSprites = renderChunkIn.compiledChunk.getAnimatedSprites(layer)) != null) {
            this.animatedSpritesRendered.or(animatedSprites);
        }
    }

    public abstract void renderChunkLayer(BlockRenderLayer var1);
}
