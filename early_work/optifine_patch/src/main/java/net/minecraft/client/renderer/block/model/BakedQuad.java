/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Float
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.block.model.BakedQuadRetextured
 *  net.minecraft.client.renderer.block.model.FaceBakery
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.util.EnumFacing
 *  net.minecraftforge.client.model.pipeline.IVertexConsumer
 *  net.minecraftforge.client.model.pipeline.IVertexProducer
 *  net.optifine.model.QuadBounds
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorMethod
 */
package net.minecraft.client.renderer.block.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.IVertexProducer;
import net.optifine.model.QuadBounds;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;

public class BakedQuad
implements IVertexProducer {
    protected int[] vertexData;
    protected final int tintIndex;
    protected EnumFacing face;
    protected TextureAtlasSprite sprite;
    private int[] vertexDataSingle = null;
    protected boolean applyDiffuseLighting = Reflector.ForgeHooksClient_fillNormal.exists();
    protected VertexFormat format = DefaultVertexFormats.ITEM;
    private QuadBounds quadBounds;
    private boolean quadEmissiveChecked;
    private BakedQuad quadEmissive;

    public BakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn, boolean applyDiffuseLighting, VertexFormat format) {
        this.vertexData = vertexDataIn;
        this.tintIndex = tintIndexIn;
        this.face = faceIn;
        this.sprite = spriteIn;
        this.applyDiffuseLighting = applyDiffuseLighting;
        this.format = format;
        this.fixVertexData();
    }

    public BakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn) {
        this.vertexData = vertexDataIn;
        this.tintIndex = tintIndexIn;
        this.face = faceIn;
        this.sprite = spriteIn;
        this.fixVertexData();
    }

    public TextureAtlasSprite getSprite() {
        if (this.sprite == null) {
            this.sprite = BakedQuad.getSpriteByUv(this.getVertexData());
        }
        return this.sprite;
    }

    public int[] getVertexData() {
        this.fixVertexData();
        return this.vertexData;
    }

    public boolean hasTintIndex() {
        return this.tintIndex != -1;
    }

    public int getTintIndex() {
        return this.tintIndex;
    }

    public EnumFacing getFace() {
        if (this.face == null) {
            this.face = FaceBakery.getFacingFromVertexData((int[])this.getVertexData());
        }
        return this.face;
    }

    public int[] getVertexDataSingle() {
        if (this.vertexDataSingle == null) {
            this.vertexDataSingle = BakedQuad.makeVertexDataSingle(this.getVertexData(), this.getSprite());
        }
        return this.vertexDataSingle;
    }

    private static int[] makeVertexDataSingle(int[] vd, TextureAtlasSprite sprite) {
        int[] vdSingle = (int[])vd.clone();
        int step = vdSingle.length / 4;
        for (int i = 0; i < 4; ++i) {
            int pos = i * step;
            float tu = Float.intBitsToFloat((int)vdSingle[pos + 4]);
            float tv = Float.intBitsToFloat((int)vdSingle[pos + 4 + 1]);
            float u = sprite.toSingleU(tu);
            float v = sprite.toSingleV(tv);
            vdSingle[pos + 4] = Float.floatToRawIntBits((float)u);
            vdSingle[pos + 4 + 1] = Float.floatToRawIntBits((float)v);
        }
        return vdSingle;
    }

    public void pipe(IVertexConsumer consumer) {
        Reflector.callVoid((ReflectorMethod)Reflector.LightUtil_putBakedQuad, (Object[])new Object[]{consumer, this});
    }

    public VertexFormat getFormat() {
        return this.format;
    }

    public boolean shouldApplyDiffuseLighting() {
        return this.applyDiffuseLighting;
    }

    private static TextureAtlasSprite getSpriteByUv(int[] vertexData) {
        float uMin = 1.0f;
        float vMin = 1.0f;
        float uMax = 0.0f;
        float vMax = 0.0f;
        int step = vertexData.length / 4;
        for (int i = 0; i < 4; ++i) {
            int pos = i * step;
            float tu = Float.intBitsToFloat((int)vertexData[pos + 4]);
            float tv = Float.intBitsToFloat((int)vertexData[pos + 4 + 1]);
            uMin = Math.min((float)uMin, (float)tu);
            vMin = Math.min((float)vMin, (float)tv);
            uMax = Math.max((float)uMax, (float)tu);
            vMax = Math.max((float)vMax, (float)tv);
        }
        float uMid = (uMin + uMax) / 2.0f;
        float vMid = (vMin + vMax) / 2.0f;
        TextureAtlasSprite spriteUv = Minecraft.getMinecraft().getTextureMapBlocks().getIconByUV((double)uMid, (double)vMid);
        return spriteUv;
    }

    protected void fixVertexData() {
        if (Config.isShaders()) {
            if (this.vertexData.length == 28) {
                this.vertexData = BakedQuad.expandVertexData(this.vertexData);
            }
        } else if (this.vertexData.length == 56) {
            this.vertexData = BakedQuad.compactVertexData(this.vertexData);
        }
    }

    private static int[] expandVertexData(int[] vd) {
        int step = vd.length / 4;
        int stepNew = step * 2;
        int[] vdNew = new int[stepNew * 4];
        for (int i = 0; i < 4; ++i) {
            System.arraycopy((Object)vd, (int)(i * step), (Object)vdNew, (int)(i * stepNew), (int)step);
        }
        return vdNew;
    }

    private static int[] compactVertexData(int[] vd) {
        int step = vd.length / 4;
        int stepNew = step / 2;
        int[] vdNew = new int[stepNew * 4];
        for (int i = 0; i < 4; ++i) {
            System.arraycopy((Object)vd, (int)(i * step), (Object)vdNew, (int)(i * stepNew), (int)stepNew);
        }
        return vdNew;
    }

    public QuadBounds getQuadBounds() {
        if (this.quadBounds == null) {
            this.quadBounds = new QuadBounds(this.getVertexData());
        }
        return this.quadBounds;
    }

    public float getMidX() {
        QuadBounds qb = this.getQuadBounds();
        return (qb.getMaxX() + qb.getMinX()) / 2.0f;
    }

    public double getMidY() {
        QuadBounds qb = this.getQuadBounds();
        return (qb.getMaxY() + qb.getMinY()) / 2.0f;
    }

    public double getMidZ() {
        QuadBounds qb = this.getQuadBounds();
        return (qb.getMaxZ() + qb.getMinZ()) / 2.0f;
    }

    public boolean isFaceQuad() {
        QuadBounds qb = this.getQuadBounds();
        return qb.isFaceQuad(this.face);
    }

    public boolean isFullQuad() {
        QuadBounds qb = this.getQuadBounds();
        return qb.isFullQuad(this.face);
    }

    public boolean isFullFaceQuad() {
        return this.isFullQuad() && this.isFaceQuad();
    }

    public BakedQuad getQuadEmissive() {
        if (this.quadEmissiveChecked) {
            return this.quadEmissive;
        }
        if (this.quadEmissive == null && this.sprite != null && this.sprite.spriteEmissive != null) {
            this.quadEmissive = new BakedQuadRetextured(this, this.sprite.spriteEmissive);
        }
        this.quadEmissiveChecked = true;
        return this.quadEmissive;
    }

    public String toString() {
        return "vertex: " + this.vertexData.length / 7 + ", tint: " + this.tintIndex + ", facing: " + this.face + ", sprite: " + this.sprite;
    }
}
