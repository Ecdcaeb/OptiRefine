/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  java.lang.Object
 *  java.util.List
 *  net.minecraft.client.renderer.BufferBuilder$State
 *  net.minecraft.client.renderer.chunk.SetVisibility
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.EnumFacing
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;

public class CompiledChunk {
    public static final CompiledChunk DUMMY = new /* Unavailable Anonymous Inner Class!! */;
    private final boolean[] layersUsed = new boolean[BlockRenderLayer.values().length];
    private final boolean[] layersStarted = new boolean[BlockRenderLayer.values().length];
    private boolean empty = true;
    private final List<TileEntity> tileEntities = Lists.newArrayList();
    private SetVisibility setVisibility = new SetVisibility();
    private BufferBuilder.State state;

    public boolean isEmpty() {
        return this.empty;
    }

    protected void setLayerUsed(BlockRenderLayer blockRenderLayer) {
        this.empty = false;
        this.layersUsed[blockRenderLayer.ordinal()] = true;
    }

    public boolean isLayerEmpty(BlockRenderLayer blockRenderLayer) {
        return !this.layersUsed[blockRenderLayer.ordinal()];
    }

    public void setLayerStarted(BlockRenderLayer blockRenderLayer) {
        this.layersStarted[blockRenderLayer.ordinal()] = true;
    }

    public boolean isLayerStarted(BlockRenderLayer blockRenderLayer) {
        return this.layersStarted[blockRenderLayer.ordinal()];
    }

    public List<TileEntity> getTileEntities() {
        return this.tileEntities;
    }

    public void addTileEntity(TileEntity tileEntity) {
        this.tileEntities.add((Object)tileEntity);
    }

    public boolean isVisible(EnumFacing enumFacing, EnumFacing enumFacing2) {
        return this.setVisibility.isVisible(enumFacing, enumFacing2);
    }

    public void setVisibility(SetVisibility setVisibility) {
        this.setVisibility = setVisibility;
    }

    public BufferBuilder.State getState() {
        return this.state;
    }

    public void setState(BufferBuilder.State state) {
        this.state = state;
    }
}
