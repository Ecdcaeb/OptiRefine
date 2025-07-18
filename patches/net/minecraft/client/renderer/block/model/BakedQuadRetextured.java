package net.minecraft.client.renderer.block.model;

import java.util.Arrays;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BakedQuadRetextured extends BakedQuad {
   private final TextureAtlasSprite texture;

   public BakedQuadRetextured(BakedQuad var1, TextureAtlasSprite var2) {
      super(Arrays.copyOf(☃.getVertexData(), ☃.getVertexData().length), ☃.tintIndex, FaceBakery.getFacingFromVertexData(☃.getVertexData()), ☃.getSprite());
      this.texture = ☃;
      this.remapQuad();
   }

   private void remapQuad() {
      for (int ☃ = 0; ☃ < 4; ☃++) {
         int ☃x = 7 * ☃;
         this.vertexData[☃x + 4] = Float.floatToRawIntBits(
            this.texture.getInterpolatedU(this.sprite.getUnInterpolatedU(Float.intBitsToFloat(this.vertexData[☃x + 4])))
         );
         this.vertexData[☃x + 4 + 1] = Float.floatToRawIntBits(
            this.texture.getInterpolatedV(this.sprite.getUnInterpolatedV(Float.intBitsToFloat(this.vertexData[☃x + 4 + 1])))
         );
      }
   }
}
