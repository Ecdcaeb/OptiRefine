/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Deprecated
 *  java.lang.Object
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.util.EnumFacing
 *  net.minecraftforge.client.model.pipeline.IVertexConsumer
 *  net.minecraftforge.client.model.pipeline.IVertexProducer
 *  net.minecraftforge.client.model.pipeline.LightUtil
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.IVertexProducer;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BakedQuad
implements IVertexProducer {
    protected final int[] vertexData;
    protected final int tintIndex;
    protected final EnumFacing face;
    protected final TextureAtlasSprite sprite;
    protected final VertexFormat format;
    protected final boolean applyDiffuseLighting;

    @Deprecated
    public BakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn) {
        this(vertexDataIn, tintIndexIn, faceIn, spriteIn, true, DefaultVertexFormats.ITEM);
    }

    public BakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn, boolean applyDiffuseLighting, VertexFormat format) {
        this.format = format;
        this.applyDiffuseLighting = applyDiffuseLighting;
        this.vertexData = vertexDataIn;
        this.tintIndex = tintIndexIn;
        this.face = faceIn;
        this.sprite = spriteIn;
    }

    public TextureAtlasSprite getSprite() {
        return this.sprite;
    }

    public int[] getVertexData() {
        return this.vertexData;
    }

    public boolean hasTintIndex() {
        return this.tintIndex != -1;
    }

    public int getTintIndex() {
        return this.tintIndex;
    }

    public EnumFacing getFace() {
        return this.face;
    }

    public void pipe(IVertexConsumer consumer) {
        LightUtil.putBakedQuad((IVertexConsumer)consumer, (BakedQuad)this);
    }

    public VertexFormat getFormat() {
        return this.format;
    }

    public boolean shouldApplyDiffuseLighting() {
        return this.applyDiffuseLighting;
    }
}
