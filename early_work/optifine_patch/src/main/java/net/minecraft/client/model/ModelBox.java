/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.client.model.PositionTextureVertex
 *  net.minecraft.client.model.TexturedQuad
 *  net.minecraft.client.renderer.BufferBuilder
 */
package net.minecraft.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;

public class ModelBox {
    private final PositionTextureVertex[] vertexPositions;
    private final TexturedQuad[] quadList;
    public final float posX1;
    public final float posY1;
    public final float posZ1;
    public final float posX2;
    public final float posY2;
    public final float posZ2;
    public String boxName;

    public ModelBox(ModelRenderer renderer, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta) {
        this(renderer, texU, texV, x, y, z, dx, dy, dz, delta, renderer.mirror);
    }

    public ModelBox(ModelRenderer renderer, int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror) {
        this.posX1 = x;
        this.posY1 = y;
        this.posZ1 = z;
        this.posX2 = x + dx;
        this.posY2 = y + dy;
        this.posZ2 = z + dz;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[6];
        float f = x + dx;
        float f1 = y + dy;
        float f2 = z + dz;
        x -= delta;
        y -= delta;
        z -= delta;
        f += delta;
        f1 += delta;
        f2 += delta;
        if (mirror) {
            float f3 = f;
            f = x;
            x = f3;
        }
        PositionTextureVertex pos0 = new PositionTextureVertex(x, y, z, 0.0f, 0.0f);
        PositionTextureVertex pos1 = new PositionTextureVertex(f, y, z, 0.0f, 8.0f);
        PositionTextureVertex pos2 = new PositionTextureVertex(f, f1, z, 8.0f, 8.0f);
        PositionTextureVertex pos3 = new PositionTextureVertex(x, f1, z, 8.0f, 0.0f);
        PositionTextureVertex pos4 = new PositionTextureVertex(x, y, f2, 0.0f, 0.0f);
        PositionTextureVertex pos5 = new PositionTextureVertex(f, y, f2, 0.0f, 8.0f);
        PositionTextureVertex pos6 = new PositionTextureVertex(f, f1, f2, 8.0f, 8.0f);
        PositionTextureVertex pos7 = new PositionTextureVertex(x, f1, f2, 8.0f, 0.0f);
        this.vertexPositions[0] = pos0;
        this.vertexPositions[1] = pos1;
        this.vertexPositions[2] = pos2;
        this.vertexPositions[3] = pos3;
        this.vertexPositions[4] = pos4;
        this.vertexPositions[5] = pos5;
        this.vertexPositions[6] = pos6;
        this.vertexPositions[7] = pos7;
        this.quadList[0] = this.makeTexturedQuad(new PositionTextureVertex[]{pos5, pos1, pos2, pos6}, faceUvs[4], false, renderer.textureWidth, renderer.textureHeight);
        this.quadList[1] = this.makeTexturedQuad(new PositionTextureVertex[]{pos0, pos4, pos7, pos3}, faceUvs[5], false, renderer.textureWidth, renderer.textureHeight);
        this.quadList[2] = this.makeTexturedQuad(new PositionTextureVertex[]{pos5, pos4, pos0, pos1}, faceUvs[1], true, renderer.textureWidth, renderer.textureHeight);
        this.quadList[3] = this.makeTexturedQuad(new PositionTextureVertex[]{pos2, pos3, pos7, pos6}, faceUvs[0], true, renderer.textureWidth, renderer.textureHeight);
        this.quadList[4] = this.makeTexturedQuad(new PositionTextureVertex[]{pos1, pos0, pos3, pos2}, faceUvs[2], false, renderer.textureWidth, renderer.textureHeight);
        this.quadList[5] = this.makeTexturedQuad(new PositionTextureVertex[]{pos4, pos5, pos6, pos7}, faceUvs[3], false, renderer.textureWidth, renderer.textureHeight);
        if (mirror) {
            for (TexturedQuad texturedquad : this.quadList) {
                texturedquad.flipFace();
            }
        }
    }

    private TexturedQuad makeTexturedQuad(PositionTextureVertex[] positionTextureVertexs, int[] faceUvs, boolean reverseUV, float textureWidth, float textureHeight) {
        if (faceUvs == null) {
            return null;
        }
        if (reverseUV) {
            return new TexturedQuad(positionTextureVertexs, faceUvs[2], faceUvs[3], faceUvs[0], faceUvs[1], textureWidth, textureHeight);
        }
        return new TexturedQuad(positionTextureVertexs, faceUvs[0], faceUvs[1], faceUvs[2], faceUvs[3], textureWidth, textureHeight);
    }

    public ModelBox(ModelRenderer renderer, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta, boolean mirror) {
        this.posX1 = x;
        this.posY1 = y;
        this.posZ1 = z;
        this.posX2 = x + (float)dx;
        this.posY2 = y + (float)dy;
        this.posZ2 = z + (float)dz;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[6];
        float f = x + (float)dx;
        float f1 = y + (float)dy;
        float f2 = z + (float)dz;
        x -= delta;
        y -= delta;
        z -= delta;
        f += delta;
        f1 += delta;
        f2 += delta;
        if (mirror) {
            float f3 = f;
            f = x;
            x = f3;
        }
        PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(x, y, z, 0.0f, 0.0f);
        PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f, y, z, 0.0f, 8.0f);
        PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f, f1, z, 8.0f, 8.0f);
        PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(x, f1, z, 8.0f, 0.0f);
        PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(x, y, f2, 0.0f, 0.0f);
        PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f, y, f2, 0.0f, 8.0f);
        PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f, f1, f2, 8.0f, 8.0f);
        PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(x, f1, f2, 8.0f, 0.0f);
        this.vertexPositions[0] = positiontexturevertex7;
        this.vertexPositions[1] = positiontexturevertex;
        this.vertexPositions[2] = positiontexturevertex1;
        this.vertexPositions[3] = positiontexturevertex2;
        this.vertexPositions[4] = positiontexturevertex3;
        this.vertexPositions[5] = positiontexturevertex4;
        this.vertexPositions[6] = positiontexturevertex5;
        this.vertexPositions[7] = positiontexturevertex6;
        this.quadList[0] = new TexturedQuad(new PositionTextureVertex[]{positiontexturevertex4, positiontexturevertex, positiontexturevertex1, positiontexturevertex5}, texU + dz + dx, texV + dz, texU + dz + dx + dz, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[1] = new TexturedQuad(new PositionTextureVertex[]{positiontexturevertex7, positiontexturevertex3, positiontexturevertex6, positiontexturevertex2}, texU, texV + dz, texU + dz, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[2] = new TexturedQuad(new PositionTextureVertex[]{positiontexturevertex4, positiontexturevertex3, positiontexturevertex7, positiontexturevertex}, texU + dz, texV, texU + dz + dx, texV + dz, renderer.textureWidth, renderer.textureHeight);
        this.quadList[3] = new TexturedQuad(new PositionTextureVertex[]{positiontexturevertex1, positiontexturevertex2, positiontexturevertex6, positiontexturevertex5}, texU + dz + dx, texV + dz, texU + dz + dx + dx, texV, renderer.textureWidth, renderer.textureHeight);
        this.quadList[4] = new TexturedQuad(new PositionTextureVertex[]{positiontexturevertex, positiontexturevertex7, positiontexturevertex2, positiontexturevertex1}, texU + dz, texV + dz, texU + dz + dx, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        this.quadList[5] = new TexturedQuad(new PositionTextureVertex[]{positiontexturevertex3, positiontexturevertex4, positiontexturevertex5, positiontexturevertex6}, texU + dz + dx + dz, texV + dz, texU + dz + dx + dz + dx, texV + dz + dy, renderer.textureWidth, renderer.textureHeight);
        if (mirror) {
            for (TexturedQuad texturedquad : this.quadList) {
                texturedquad.flipFace();
            }
        }
    }

    public void render(BufferBuilder renderer, float scale) {
        for (TexturedQuad texturedquad : this.quadList) {
            if (texturedquad == null) continue;
            texturedquad.draw(renderer, scale);
        }
    }

    public ModelBox setBoxName(String name) {
        this.boxName = name;
        return this;
    }
}
