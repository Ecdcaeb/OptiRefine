/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Float
 *  java.lang.Object
 *  java.util.Arrays
 *  net.minecraft.client.renderer.block.model.BakedQuad
 *  net.minecraft.client.renderer.block.model.FaceBakery
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.minecraft.client.renderer.block.model;

import java.util.Arrays;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class BakedQuadRetextured
extends BakedQuad {
    private final TextureAtlasSprite texture;

    public BakedQuadRetextured(BakedQuad quad, TextureAtlasSprite textureIn) {
        super(Arrays.copyOf((int[])quad.getVertexData(), (int)quad.getVertexData().length), quad.tintIndex, FaceBakery.getFacingFromVertexData((int[])quad.getVertexData()), quad.getSprite(), quad.applyDiffuseLighting, quad.format);
        this.texture = textureIn;
        this.remapQuad();
    }

    private void remapQuad() {
        for (int i = 0; i < 4; ++i) {
            int j = this.format.getIntegerSize() * i;
            int uvIndex = this.format.getUvOffsetById(0) / 4;
            this.vertexData[j + uvIndex] = Float.floatToRawIntBits((float)this.texture.getInterpolatedU((double)this.sprite.getUnInterpolatedU(Float.intBitsToFloat((int)this.vertexData[j + uvIndex]))));
            this.vertexData[j + uvIndex + 1] = Float.floatToRawIntBits((float)this.texture.getInterpolatedV((double)this.sprite.getUnInterpolatedV(Float.intBitsToFloat((int)this.vertexData[j + uvIndex + 1]))));
        }
    }

    public TextureAtlasSprite getSprite() {
        return this.texture;
    }
}