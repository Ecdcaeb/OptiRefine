package net.minecraft.client.model;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;

public class TexturedQuad {
   public PositionTextureVertex[] vertexPositions;
   public int nVertices;
   private boolean invertNormal;

   public TexturedQuad(PositionTextureVertex[] var1) {
      this.vertexPositions = ☃;
      this.nVertices = ☃.length;
   }

   public TexturedQuad(PositionTextureVertex[] var1, int var2, int var3, int var4, int var5, float var6, float var7) {
      this(☃);
      float ☃ = 0.0F / ☃;
      float ☃x = 0.0F / ☃;
      ☃[0] = ☃[0].setTexturePosition(☃ / ☃ - ☃, ☃ / ☃ + ☃x);
      ☃[1] = ☃[1].setTexturePosition(☃ / ☃ + ☃, ☃ / ☃ + ☃x);
      ☃[2] = ☃[2].setTexturePosition(☃ / ☃ + ☃, ☃ / ☃ - ☃x);
      ☃[3] = ☃[3].setTexturePosition(☃ / ☃ - ☃, ☃ / ☃ - ☃x);
   }

   public void flipFace() {
      PositionTextureVertex[] ☃ = new PositionTextureVertex[this.vertexPositions.length];

      for (int ☃x = 0; ☃x < this.vertexPositions.length; ☃x++) {
         ☃[☃x] = this.vertexPositions[this.vertexPositions.length - ☃x - 1];
      }

      this.vertexPositions = ☃;
   }

   public void draw(BufferBuilder var1, float var2) {
      Vec3d ☃ = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[0].vector3D);
      Vec3d ☃x = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[2].vector3D);
      Vec3d ☃xx = ☃x.crossProduct(☃).normalize();
      float ☃xxx = (float)☃xx.x;
      float ☃xxxx = (float)☃xx.y;
      float ☃xxxxx = (float)☃xx.z;
      if (this.invertNormal) {
         ☃xxx = -☃xxx;
         ☃xxxx = -☃xxxx;
         ☃xxxxx = -☃xxxxx;
      }

      ☃.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);

      for (int ☃xxxxxx = 0; ☃xxxxxx < 4; ☃xxxxxx++) {
         PositionTextureVertex ☃xxxxxxx = this.vertexPositions[☃xxxxxx];
         ☃.pos(☃xxxxxxx.vector3D.x * ☃, ☃xxxxxxx.vector3D.y * ☃, ☃xxxxxxx.vector3D.z * ☃)
            .tex(☃xxxxxxx.texturePositionX, ☃xxxxxxx.texturePositionY)
            .normal(☃xxx, ☃xxxx, ☃xxxxx)
            .endVertex();
      }

      Tessellator.getInstance().draw();
   }
}
