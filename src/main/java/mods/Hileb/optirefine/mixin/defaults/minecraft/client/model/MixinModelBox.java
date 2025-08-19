package mods.Hileb.optirefine.mixin.defaults.minecraft.client.model;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.NewConstructor;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.ShadowSuper;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import org.spongepowered.asm.mixin.*;

@Mixin(ModelBox.class)
public abstract class MixinModelBox {
    @Mutable
    @Shadow @Final
    private PositionTextureVertex[] vertexPositions;
    @Mutable
    @Shadow @Final
    private TexturedQuad[] quadList;
    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
    public float posX1;
    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
    public float posY1;
    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
    public float posZ1;
    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
    public float posX2;
    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
    public float posY2;
    @Mutable
    @SuppressWarnings("unused")
    @Shadow @Final
    public float posZ2;
    @SuppressWarnings("unused")
    @Shadow
    public String boxName;

    @ShadowSuper("<init>")
    public void _Object(){}

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern", "MissingUnique"})
    @NewConstructor
    @Public
    public void _ModelBox(ModelRenderer renderer, int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror) {
        _Object();
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

    @SuppressWarnings("AddedMixinMembersNamePattern")
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

/*
+++ net/minecraft/client/model/ModelBox.java	Tue Aug 19 14:59:58 2025
@@ -14,12 +14,75 @@
    public String boxName;

    public ModelBox(ModelRenderer var1, int var2, int var3, float var4, float var5, float var6, int var7, int var8, int var9, float var10) {
       this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var1.mirror);
    }

+   public ModelBox(ModelRenderer var1, int[][] var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, boolean var10) {
+      this.posX1 = var3;
+      this.posY1 = var4;
+      this.posZ1 = var5;
+      this.posX2 = var3 + var6;
+      this.posY2 = var4 + var7;
+      this.posZ2 = var5 + var8;
+      this.vertexPositions = new PositionTextureVertex[8];
+      this.quadList = new TexturedQuad[6];
+      float var11 = var3 + var6;
+      float var12 = var4 + var7;
+      float var13 = var5 + var8;
+      var3 -= var9;
+      var4 -= var9;
+      var5 -= var9;
+      var11 += var9;
+      var12 += var9;
+      var13 += var9;
+      if (var10) {
+         float var14 = var11;
+         var11 = var3;
+         var3 = var14;
+      }
+
+      PositionTextureVertex var32 = new PositionTextureVertex(var3, var4, var5, 0.0F, 0.0F);
+      PositionTextureVertex var15 = new PositionTextureVertex(var11, var4, var5, 0.0F, 8.0F);
+      PositionTextureVertex var16 = new PositionTextureVertex(var11, var12, var5, 8.0F, 8.0F);
+      PositionTextureVertex var17 = new PositionTextureVertex(var3, var12, var5, 8.0F, 0.0F);
+      PositionTextureVertex var18 = new PositionTextureVertex(var3, var4, var13, 0.0F, 0.0F);
+      PositionTextureVertex var19 = new PositionTextureVertex(var11, var4, var13, 0.0F, 8.0F);
+      PositionTextureVertex var20 = new PositionTextureVertex(var11, var12, var13, 8.0F, 8.0F);
+      PositionTextureVertex var21 = new PositionTextureVertex(var3, var12, var13, 8.0F, 0.0F);
+      this.vertexPositions[0] = var32;
+      this.vertexPositions[1] = var15;
+      this.vertexPositions[2] = var16;
+      this.vertexPositions[3] = var17;
+      this.vertexPositions[4] = var18;
+      this.vertexPositions[5] = var19;
+      this.vertexPositions[6] = var20;
+      this.vertexPositions[7] = var21;
+      this.quadList[0] = this.makeTexturedQuad(new PositionTextureVertex[]{var19, var15, var16, var20}, var2[4], false, var1.textureWidth, var1.textureHeight);
+      this.quadList[1] = this.makeTexturedQuad(new PositionTextureVertex[]{var32, var18, var21, var17}, var2[5], false, var1.textureWidth, var1.textureHeight);
+      this.quadList[2] = this.makeTexturedQuad(new PositionTextureVertex[]{var19, var18, var32, var15}, var2[1], true, var1.textureWidth, var1.textureHeight);
+      this.quadList[3] = this.makeTexturedQuad(new PositionTextureVertex[]{var16, var17, var21, var20}, var2[0], true, var1.textureWidth, var1.textureHeight);
+      this.quadList[4] = this.makeTexturedQuad(new PositionTextureVertex[]{var15, var32, var17, var16}, var2[2], false, var1.textureWidth, var1.textureHeight);
+      this.quadList[5] = this.makeTexturedQuad(new PositionTextureVertex[]{var18, var19, var20, var21}, var2[3], false, var1.textureWidth, var1.textureHeight);
+      if (var10) {
+         for (TexturedQuad var25 : this.quadList) {
+            var25.flipFace();
+         }
+      }
+   }
+
+   private TexturedQuad makeTexturedQuad(PositionTextureVertex[] var1, int[] var2, boolean var3, float var4, float var5) {
+      if (var2 == null) {
+         return null;
+      } else {
+         return var3
+            ? new TexturedQuad(var1, var2[2], var2[3], var2[0], var2[1], var4, var5)
+            : new TexturedQuad(var1, var2[0], var2[1], var2[2], var2[3], var4, var5);
+      }
+   }
+
    public ModelBox(ModelRenderer var1, int var2, int var3, float var4, float var5, float var6, int var7, int var8, int var9, float var10, boolean var11) {
       this.posX1 = var4;
       this.posY1 = var5;
       this.posZ1 = var6;
       this.posX2 = var4 + var7;
       this.posY2 = var5 + var8;
@@ -105,13 +168,15 @@
          }
       }
    }

    public void render(BufferBuilder var1, float var2) {
       for (TexturedQuad var6 : this.quadList) {
-         var6.draw(var1, var2);
+         if (var6 != null) {
+            var6.draw(var1, var2);
+         }
       }
    }

    public ModelBox setBoxName(String var1) {
       this.boxName = var1;
       return this;
 */
