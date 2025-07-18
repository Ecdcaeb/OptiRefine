package net.minecraft.client.model;

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

   public ModelBox(ModelRenderer var1, int var2, int var3, float var4, float var5, float var6, int var7, int var8, int var9, float var10) {
      this(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃.mirror);
   }

   public ModelBox(ModelRenderer var1, int var2, int var3, float var4, float var5, float var6, int var7, int var8, int var9, float var10, boolean var11) {
      this.posX1 = ☃;
      this.posY1 = ☃;
      this.posZ1 = ☃;
      this.posX2 = ☃ + ☃;
      this.posY2 = ☃ + ☃;
      this.posZ2 = ☃ + ☃;
      this.vertexPositions = new PositionTextureVertex[8];
      this.quadList = new TexturedQuad[6];
      float ☃ = ☃ + ☃;
      float ☃x = ☃ + ☃;
      float ☃xx = ☃ + ☃;
      ☃ -= ☃;
      ☃ -= ☃;
      ☃ -= ☃;
      ☃ += ☃;
      ☃x += ☃;
      ☃xx += ☃;
      if (☃) {
         float ☃xxx = ☃;
         ☃ = ☃;
         ☃ = ☃xxx;
      }

      PositionTextureVertex ☃xxx = new PositionTextureVertex(☃, ☃, ☃, 0.0F, 0.0F);
      PositionTextureVertex ☃xxxx = new PositionTextureVertex(☃, ☃, ☃, 0.0F, 8.0F);
      PositionTextureVertex ☃xxxxx = new PositionTextureVertex(☃, ☃x, ☃, 8.0F, 8.0F);
      PositionTextureVertex ☃xxxxxx = new PositionTextureVertex(☃, ☃x, ☃, 8.0F, 0.0F);
      PositionTextureVertex ☃xxxxxxx = new PositionTextureVertex(☃, ☃, ☃xx, 0.0F, 0.0F);
      PositionTextureVertex ☃xxxxxxxx = new PositionTextureVertex(☃, ☃, ☃xx, 0.0F, 8.0F);
      PositionTextureVertex ☃xxxxxxxxx = new PositionTextureVertex(☃, ☃x, ☃xx, 8.0F, 8.0F);
      PositionTextureVertex ☃xxxxxxxxxx = new PositionTextureVertex(☃, ☃x, ☃xx, 8.0F, 0.0F);
      this.vertexPositions[0] = ☃xxx;
      this.vertexPositions[1] = ☃xxxx;
      this.vertexPositions[2] = ☃xxxxx;
      this.vertexPositions[3] = ☃xxxxxx;
      this.vertexPositions[4] = ☃xxxxxxx;
      this.vertexPositions[5] = ☃xxxxxxxx;
      this.vertexPositions[6] = ☃xxxxxxxxx;
      this.vertexPositions[7] = ☃xxxxxxxxxx;
      this.quadList[0] = new TexturedQuad(
         new PositionTextureVertex[]{☃xxxxxxxx, ☃xxxx, ☃xxxxx, ☃xxxxxxxxx}, ☃ + ☃ + ☃, ☃ + ☃, ☃ + ☃ + ☃ + ☃, ☃ + ☃ + ☃, ☃.textureWidth, ☃.textureHeight
      );
      this.quadList[1] = new TexturedQuad(
         new PositionTextureVertex[]{☃xxx, ☃xxxxxxx, ☃xxxxxxxxxx, ☃xxxxxx}, ☃, ☃ + ☃, ☃ + ☃, ☃ + ☃ + ☃, ☃.textureWidth, ☃.textureHeight
      );
      this.quadList[2] = new TexturedQuad(
         new PositionTextureVertex[]{☃xxxxxxxx, ☃xxxxxxx, ☃xxx, ☃xxxx}, ☃ + ☃, ☃, ☃ + ☃ + ☃, ☃ + ☃, ☃.textureWidth, ☃.textureHeight
      );
      this.quadList[3] = new TexturedQuad(
         new PositionTextureVertex[]{☃xxxxx, ☃xxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxx}, ☃ + ☃ + ☃, ☃ + ☃, ☃ + ☃ + ☃ + ☃, ☃, ☃.textureWidth, ☃.textureHeight
      );
      this.quadList[4] = new TexturedQuad(
         new PositionTextureVertex[]{☃xxxx, ☃xxx, ☃xxxxxx, ☃xxxxx}, ☃ + ☃, ☃ + ☃, ☃ + ☃ + ☃, ☃ + ☃ + ☃, ☃.textureWidth, ☃.textureHeight
      );
      this.quadList[5] = new TexturedQuad(
         new PositionTextureVertex[]{☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx, ☃xxxxxxxxxx},
         ☃ + ☃ + ☃ + ☃,
         ☃ + ☃,
         ☃ + ☃ + ☃ + ☃ + ☃,
         ☃ + ☃ + ☃,
         ☃.textureWidth,
         ☃.textureHeight
      );
      if (☃) {
         for (TexturedQuad ☃xxxxxxxxxxx : this.quadList) {
            ☃xxxxxxxxxxx.flipFace();
         }
      }
   }

   public void render(BufferBuilder var1, float var2) {
      for (TexturedQuad ☃ : this.quadList) {
         ☃.draw(☃, ☃);
      }
   }

   public ModelBox setBoxName(String var1) {
      this.boxName = ☃;
      return this;
   }
}
