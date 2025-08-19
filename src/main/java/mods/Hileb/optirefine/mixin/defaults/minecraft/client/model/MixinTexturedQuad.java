package mods.Hileb.optirefine.mixin.defaults.minecraft.client.model;

import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.optifine.shaders.SVertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TexturedQuad.class)
public abstract class MixinTexturedQuad {

    @Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V"))
    public void injectDraw(BufferBuilder instance, int p_181668_1_, VertexFormat p_181668_2_){
        if (DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL == p_181668_2_ && Config.isShaders()) {
            instance.begin(p_181668_1_, SVertexFormat.defVertexFormatTextured);
        }
    }
}
/*
+++ net/minecraft/client/model/TexturedQuad.java	Tue Aug 19 14:59:58 2025
@@ -1,12 +1,13 @@
 package net.minecraft.client.model;

 import net.minecraft.client.renderer.BufferBuilder;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.util.math.Vec3d;
+import net.optifine.shaders.SVertexFormat;

 public class TexturedQuad {
    public PositionTextureVertex[] vertexPositions;
    public int nVertices;
    private boolean invertNormal;

@@ -45,13 +46,17 @@
       if (this.invertNormal) {
          var6 = -var6;
          var7 = -var7;
          var8 = -var8;
       }

-      var1.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
+      if (Config.isShaders()) {
+         var1.begin(7, SVertexFormat.defVertexFormatTextured);
+      } else {
+         var1.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
+      }

       for (int var9 = 0; var9 < 4; var9++) {
          PositionTextureVertex var10 = this.vertexPositions[var9];
          var1.pos(var10.vector3D.x * var2, var10.vector3D.y * var2, var10.vector3D.z * var2)
             .tex(var10.texturePositionX, var10.texturePositionY)
             .normal(var6, var7, var8)
 */
