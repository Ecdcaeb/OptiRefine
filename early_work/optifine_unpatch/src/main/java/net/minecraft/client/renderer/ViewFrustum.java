/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  javax.annotation.Nullable
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.chunk.IRenderChunkFactory
 *  net.minecraft.client.renderer.chunk.RenderChunk
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 */
package net.minecraft.client.renderer;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ViewFrustum {
    protected final RenderGlobal renderGlobal;
    protected final World world;
    protected int countChunksY;
    protected int countChunksX;
    protected int countChunksZ;
    public RenderChunk[] renderChunks;

    public ViewFrustum(World world, int n, RenderGlobal renderGlobal, IRenderChunkFactory iRenderChunkFactory) {
        this.renderGlobal = renderGlobal;
        this.world = world;
        this.setCountChunksXYZ(n);
        this.createRenderChunks(iRenderChunkFactory);
    }

    protected void createRenderChunks(IRenderChunkFactory iRenderChunkFactory) {
        int n = this.countChunksX * this.countChunksY * this.countChunksZ;
        this.renderChunks = new RenderChunk[n];
        \u2603 = 0;
        for (\u2603 = 0; \u2603 < this.countChunksX; ++\u2603) {
            for (\u2603 = 0; \u2603 < this.countChunksY; ++\u2603) {
                for (\u2603 = 0; \u2603 < this.countChunksZ; ++\u2603) {
                    \u2603 = (\u2603 * this.countChunksY + \u2603) * this.countChunksX + \u2603;
                    this.renderChunks[\u2603] = iRenderChunkFactory.create(this.world, this.renderGlobal, \u2603++);
                    this.renderChunks[\u2603].setPosition(\u2603 * 16, \u2603 * 16, \u2603 * 16);
                }
            }
        }
    }

    public void deleteGlResources() {
        for (RenderChunk renderChunk : this.renderChunks) {
            renderChunk.deleteGlResources();
        }
    }

    protected void setCountChunksXYZ(int n) {
        this.countChunksX = \u2603 = n * 2 + 1;
        this.countChunksY = 16;
        this.countChunksZ = \u2603;
    }

    public void updateChunkPositions(double d, double d2) {
        int n = MathHelper.floor((double)d) - 8;
        \u2603 = MathHelper.floor((double)d2) - 8;
        \u2603 = this.countChunksX * 16;
        for (\u2603 = 0; \u2603 < this.countChunksX; ++\u2603) {
            \u2603 = this.getBaseCoordinate(n, \u2603, \u2603);
            for (\u2603 = 0; \u2603 < this.countChunksZ; ++\u2603) {
                \u2603 = this.getBaseCoordinate(\u2603, \u2603, \u2603);
                for (\u2603 = 0; \u2603 < this.countChunksY; ++\u2603) {
                    \u2603 = \u2603 * 16;
                    RenderChunk renderChunk = this.renderChunks[(\u2603 * this.countChunksY + \u2603) * this.countChunksX + \u2603];
                    renderChunk.setPosition(\u2603, \u2603, \u2603);
                }
            }
        }
    }

    private int getBaseCoordinate(int n, int n2, int n3) {
        \u2603 = n3 * 16;
        \u2603 = \u2603 - n + n2 / 2;
        if (\u2603 < 0) {
            \u2603 -= n2 - 1;
        }
        return \u2603 - \u2603 / n2 * n2;
    }

    public void markBlocksForUpdate(int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        int n7 = MathHelper.intFloorDiv((int)n, (int)16);
        \u2603 = MathHelper.intFloorDiv((int)n2, (int)16);
        \u2603 = MathHelper.intFloorDiv((int)n3, (int)16);
        \u2603 = MathHelper.intFloorDiv((int)n4, (int)16);
        \u2603 = MathHelper.intFloorDiv((int)n5, (int)16);
        \u2603 = MathHelper.intFloorDiv((int)n6, (int)16);
        for (\u2603 = n7; \u2603 <= \u2603; ++\u2603) {
            \u2603 = \u2603 % this.countChunksX;
            if (\u2603 < 0) {
                \u2603 += this.countChunksX;
            }
            for (\u2603 = \u2603; \u2603 <= \u2603; ++\u2603) {
                \u2603 = \u2603 % this.countChunksY;
                if (\u2603 < 0) {
                    \u2603 += this.countChunksY;
                }
                for (\u2603 = \u2603; \u2603 <= \u2603; ++\u2603) {
                    \u2603 = \u2603 % this.countChunksZ;
                    if (\u2603 < 0) {
                        \u2603 += this.countChunksZ;
                    }
                    \u2603 = (\u2603 * this.countChunksY + \u2603) * this.countChunksX + \u2603;
                    RenderChunk renderChunk = this.renderChunks[\u2603];
                    renderChunk.setNeedsUpdate(bl);
                }
            }
        }
    }

    @Nullable
    protected RenderChunk getRenderChunk(BlockPos blockPos) {
        int n = MathHelper.intFloorDiv((int)blockPos.p(), (int)16);
        \u2603 = MathHelper.intFloorDiv((int)blockPos.q(), (int)16);
        \u2603 = MathHelper.intFloorDiv((int)blockPos.r(), (int)16);
        if (\u2603 < 0 || \u2603 >= this.countChunksY) {
            return null;
        }
        if ((n %= this.countChunksX) < 0) {
            n += this.countChunksX;
        }
        if ((\u2603 %= this.countChunksZ) < 0) {
            \u2603 += this.countChunksZ;
        }
        \u2603 = (\u2603 * this.countChunksY + \u2603) * this.countChunksX + n;
        return this.renderChunks[\u2603];
    }
}
