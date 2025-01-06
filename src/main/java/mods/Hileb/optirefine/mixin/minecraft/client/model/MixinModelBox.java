package mods.Hileb.optirefine.mixin.minecraft.client.model;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.NewConstructor;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ModelBox.class)
public abstract class MixinModelBox {
    @Shadow
    @SuppressWarnings("all")
    private PositionTextureVertex[] vertexPositions;
    @Shadow
    @SuppressWarnings("all")
    private TexturedQuad[] quadList;
    @Shadow
    @SuppressWarnings("all")
    public float posX1;
    @Shadow
    @SuppressWarnings("all")
    public float posY1;
    @Shadow
    @SuppressWarnings("all")
    public float posZ1;
    @Shadow
    @SuppressWarnings("all")
    public float posX2;
    @Shadow
    @SuppressWarnings("all")
    public float posY2;
    @Shadow
    @SuppressWarnings("all")
    public float posZ2;
    @Shadow
    @SuppressWarnings("all")
    public String boxName;

    @NewConstructor
    @Public
    @SuppressWarnings("all")
    public void ModelBox(ModelRenderer renderer, int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror) {
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

        PositionTextureVertex pos0 = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
        PositionTextureVertex pos1 = new PositionTextureVertex(f, y, z, 0.0F, 8.0F);
        PositionTextureVertex pos2 = new PositionTextureVertex(f, f1, z, 8.0F, 8.0F);
        PositionTextureVertex pos3 = new PositionTextureVertex(x, f1, z, 8.0F, 0.0F);
        PositionTextureVertex pos4 = new PositionTextureVertex(x, y, f2, 0.0F, 0.0F);
        PositionTextureVertex pos5 = new PositionTextureVertex(f, y, f2, 0.0F, 8.0F);
        PositionTextureVertex pos6 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
        PositionTextureVertex pos7 = new PositionTextureVertex(x, f1, f2, 8.0F, 0.0F);
        this.vertexPositions[0] = pos0;
        this.vertexPositions[1] = pos1;
        this.vertexPositions[2] = pos2;
        this.vertexPositions[3] = pos3;
        this.vertexPositions[4] = pos4;
        this.vertexPositions[5] = pos5;
        this.vertexPositions[6] = pos6;
        this.vertexPositions[7] = pos7;
        this.quadList[0] = this.makeTexturedQuad(
                new PositionTextureVertex[]{pos5, pos1, pos2, pos6}, faceUvs[4], false, renderer.textureWidth, renderer.textureHeight
        );
        this.quadList[1] = this.makeTexturedQuad(
                new PositionTextureVertex[]{pos0, pos4, pos7, pos3}, faceUvs[5], false, renderer.textureWidth, renderer.textureHeight
        );
        this.quadList[2] = this.makeTexturedQuad(
                new PositionTextureVertex[]{pos5, pos4, pos0, pos1}, faceUvs[1], true, renderer.textureWidth, renderer.textureHeight
        );
        this.quadList[3] = this.makeTexturedQuad(
                new PositionTextureVertex[]{pos2, pos3, pos7, pos6}, faceUvs[0], true, renderer.textureWidth, renderer.textureHeight
        );
        this.quadList[4] = this.makeTexturedQuad(
                new PositionTextureVertex[]{pos1, pos0, pos3, pos2}, faceUvs[2], false, renderer.textureWidth, renderer.textureHeight
        );
        this.quadList[5] = this.makeTexturedQuad(
                new PositionTextureVertex[]{pos4, pos5, pos6, pos7}, faceUvs[3], false, renderer.textureWidth, renderer.textureHeight
        );
        if (mirror) {
            for (TexturedQuad texturedquad : this.quadList) {
                texturedquad.flipFace();
            }
        }
    }

    @Unique
    private TexturedQuad makeTexturedQuad(
            PositionTextureVertex[] positionTextureVertexs, int[] faceUvs, boolean reverseUV, float textureWidth, float textureHeight
    ) {
        if (faceUvs == null) {
            return null;
        } else {
            return reverseUV
                    ? new TexturedQuad(positionTextureVertexs, faceUvs[2], faceUvs[3], faceUvs[0], faceUvs[1], textureWidth, textureHeight)
                    : new TexturedQuad(positionTextureVertexs, faceUvs[0], faceUvs[1], faceUvs[2], faceUvs[3], textureWidth, textureHeight);
        }
    }


}
