package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class BakedQuad {
   protected final int[] vertexData;
   protected final int tintIndex;
   protected final EnumFacing face;
   protected final TextureAtlasSprite sprite;

   public BakedQuad(int[] var1, int var2, EnumFacing var3, TextureAtlasSprite var4) {
      this.vertexData = ☃;
      this.tintIndex = ☃;
      this.face = ☃;
      this.sprite = ☃;
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
}
