package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.block.model;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BakedQuadRetextured.class)
public abstract class MixinBakedQuadRetextured {

    @Unique
    private TextureAtlasSprite spriteOld;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(BakedQuad quad, TextureAtlasSprite p_i46217_2, CallbackInfo ci){
        this.spriteOld = quad.getSprite();
        BakedQuad_fixVertexData(this);
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.block.model.BakedQuad fixVertexData ()V")
    private static native void BakedQuad_fixVertexData(Object bakedQuad);

    @Redirect(method = "remapQuad", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/block/model/BakedQuadRetextured;sprite:Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
    public TextureAtlasSprite applySpriteOld(BakedQuadRetextured instance){
        return spriteOld == null ? instance.getSprite() : spriteOld;
    }

}
/*

--- net/minecraft/client/renderer/block/model/BakedQuadRetextured.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/block/model/BakedQuadRetextured.java	Tue Aug 19 14:59:58 2025
@@ -2,30 +2,42 @@

 import java.util.Arrays;
 import net.minecraft.client.renderer.texture.TextureAtlasSprite;

 public class BakedQuadRetextured extends BakedQuad {
    private final TextureAtlasSprite texture;
+   private final TextureAtlasSprite spriteOld;

    public BakedQuadRetextured(BakedQuad var1, TextureAtlasSprite var2) {
       super(
          Arrays.copyOf(var1.getVertexData(), var1.getVertexData().length),
          var1.tintIndex,
          FaceBakery.getFacingFromVertexData(var1.getVertexData()),
-         var1.getSprite()
+         var2,
+         var1.applyDiffuseLighting,
+         var1.format
       );
       this.texture = var2;
+      this.format = var1.format;
+      this.applyDiffuseLighting = var1.applyDiffuseLighting;
+      this.spriteOld = var1.getSprite();
       this.remapQuad();
+      this.fixVertexData();
    }

    private void remapQuad() {
       for (int var1 = 0; var1 < 4; var1++) {
-         int var2 = 7 * var1;
-         this.vertexData[var2 + 4] = Float.floatToRawIntBits(
-            this.texture.getInterpolatedU(this.sprite.getUnInterpolatedU(Float.intBitsToFloat(this.vertexData[var2 + 4])))
+         int var2 = this.format.getIntegerSize() * var1;
+         int var3 = this.format.getUvOffsetById(0) / 4;
+         this.vertexData[var2 + var3] = Float.floatToRawIntBits(
+            this.texture.getInterpolatedU(this.spriteOld.getUnInterpolatedU(Float.intBitsToFloat(this.vertexData[var2 + var3])))
          );
-         this.vertexData[var2 + 4 + 1] = Float.floatToRawIntBits(
-            this.texture.getInterpolatedV(this.sprite.getUnInterpolatedV(Float.intBitsToFloat(this.vertexData[var2 + 4 + 1])))
+         this.vertexData[var2 + var3 + 1] = Float.floatToRawIntBits(
+            this.texture.getInterpolatedV(this.spriteOld.getUnInterpolatedV(Float.intBitsToFloat(this.vertexData[var2 + var3 + 1])))
          );
       }
+   }
+
+   public TextureAtlasSprite getSprite() {
+      return this.texture;
    }
 }
 */
