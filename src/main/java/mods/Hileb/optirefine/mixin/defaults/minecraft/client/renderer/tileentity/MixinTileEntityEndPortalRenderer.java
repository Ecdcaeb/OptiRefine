package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.tileentity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.optifine.shaders.ShadersRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileEntityEndPortalRenderer.class)
public abstract class MixinTileEntityEndPortalRenderer {

    @WrapMethod(method = "render(Lnet/minecraft/tileentity/TileEntityEndPortal;DDDFIF)V")
    public void renderShader(TileEntityEndPortal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, Operation<Void> original){
        if (!Config.isShaders() || !ShadersRender.renderEndPortal(te, x, y, z, partialTicks, destroyStage, this.getOffset())) {
            original.call(te, x, y, z, partialTicks, destroyStage, alpha);
        }
    }

    @Shadow
    protected native float getOffset();
}
/*
--- net/minecraft/client/renderer/tileentity/TileEntityEndPortalRenderer.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/tileentity/TileEntityEndPortalRenderer.java	Tue Aug 19 14:59:58 2025
@@ -9,134 +9,137 @@
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.tileentity.TileEntityEndPortal;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.shaders.ShadersRender;

 public class TileEntityEndPortalRenderer extends TileEntitySpecialRenderer<TileEntityEndPortal> {
    private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);

    public void render(TileEntityEndPortal var1, double var2, double var4, double var6, float var8, int var9, float var10) {
-      GlStateManager.disableLighting();
-      RANDOM.setSeed(31100L);
-      GlStateManager.getFloat(2982, MODELVIEW);
-      GlStateManager.getFloat(2983, PROJECTION);
-      double var11 = var2 * var2 + var4 * var4 + var6 * var6;
-      int var13 = this.getPasses(var11);
-      float var14 = this.getOffset();
-      boolean var15 = false;
-
-      for (int var16 = 0; var16 < var13; var16++) {
-         GlStateManager.pushMatrix();
-         float var17 = 2.0F / (18 - var16);
-         if (var16 == 0) {
+      if (!Config.isShaders() || !ShadersRender.renderEndPortal(var1, var2, var4, var6, var8, var9, this.getOffset())) {
+         GlStateManager.disableLighting();
+         RANDOM.setSeed(31100L);
+         GlStateManager.getFloat(2982, MODELVIEW);
+         GlStateManager.getFloat(2983, PROJECTION);
+         double var11 = var2 * var2 + var4 * var4 + var6 * var6;
+         int var13 = this.getPasses(var11);
+         float var14 = this.getOffset();
+         boolean var15 = false;
+
+         for (int var16 = 0; var16 < var13; var16++) {
+            GlStateManager.pushMatrix();
+            float var17 = 2.0F / (18 - var16);
+            if (var16 == 0) {
+               this.bindTexture(END_SKY_TEXTURE);
+               var17 = 0.15F;
+               GlStateManager.enableBlend();
+               GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
+            }
+
+            if (var16 >= 1) {
+               this.bindTexture(END_PORTAL_TEXTURE);
+               var15 = true;
+               Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
+            }
+
+            if (var16 == 1) {
+               GlStateManager.enableBlend();
+               GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
+            }
+
+            GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
+            GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
+            GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
+            GlStateManager.texGen(GlStateManager.TexGen.S, 9474, this.getBuffer(1.0F, 0.0F, 0.0F, 0.0F));
+            GlStateManager.texGen(GlStateManager.TexGen.T, 9474, this.getBuffer(0.0F, 1.0F, 0.0F, 0.0F));
+            GlStateManager.texGen(GlStateManager.TexGen.R, 9474, this.getBuffer(0.0F, 0.0F, 1.0F, 0.0F));
+            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
+            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
+            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
+            GlStateManager.popMatrix();
+            GlStateManager.matrixMode(5890);
+            GlStateManager.pushMatrix();
+            GlStateManager.loadIdentity();
+            GlStateManager.translate(0.5F, 0.5F, 0.0F);
+            GlStateManager.scale(0.5F, 0.5F, 1.0F);
+            float var18 = var16 + 1;
+            GlStateManager.translate(17.0F / var18, (2.0F + var18 / 1.5F) * ((float)Minecraft.getSystemTime() % 800000.0F / 800000.0F), 0.0F);
+            GlStateManager.rotate((var18 * var18 * 4321.0F + var18 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
+            GlStateManager.scale(4.5F - var18 / 4.0F, 4.5F - var18 / 4.0F, 1.0F);
+            GlStateManager.multMatrix(PROJECTION);
+            GlStateManager.multMatrix(MODELVIEW);
+            Tessellator var19 = Tessellator.getInstance();
+            BufferBuilder var20 = var19.getBuffer();
+            var20.begin(7, DefaultVertexFormats.POSITION_COLOR);
+            float var21 = (RANDOM.nextFloat() * 0.5F + 0.1F) * var17;
+            float var22 = (RANDOM.nextFloat() * 0.5F + 0.4F) * var17;
+            float var23 = (RANDOM.nextFloat() * 0.5F + 0.5F) * var17;
+            if (var1.shouldRenderFace(EnumFacing.SOUTH)) {
+               var20.pos(var2, var4, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2 + 1.0, var4, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2 + 1.0, var4 + 1.0, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2, var4 + 1.0, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
+            }
+
+            if (var1.shouldRenderFace(EnumFacing.NORTH)) {
+               var20.pos(var2, var4 + 1.0, var6).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2 + 1.0, var4 + 1.0, var6).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2 + 1.0, var4, var6).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2, var4, var6).color(var21, var22, var23, 1.0F).endVertex();
+            }
+
+            if (var1.shouldRenderFace(EnumFacing.EAST)) {
+               var20.pos(var2 + 1.0, var4 + 1.0, var6).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2 + 1.0, var4 + 1.0, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2 + 1.0, var4, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2 + 1.0, var4, var6).color(var21, var22, var23, 1.0F).endVertex();
+            }
+
+            if (var1.shouldRenderFace(EnumFacing.WEST)) {
+               var20.pos(var2, var4, var6).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2, var4, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2, var4 + 1.0, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2, var4 + 1.0, var6).color(var21, var22, var23, 1.0F).endVertex();
+            }
+
+            if (var1.shouldRenderFace(EnumFacing.DOWN)) {
+               var20.pos(var2, var4, var6).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2 + 1.0, var4, var6).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2 + 1.0, var4, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2, var4, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
+            }
+
+            if (var1.shouldRenderFace(EnumFacing.UP)) {
+               var20.pos(var2, var4 + var14, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2 + 1.0, var4 + var14, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2 + 1.0, var4 + var14, var6).color(var21, var22, var23, 1.0F).endVertex();
+               var20.pos(var2, var4 + var14, var6).color(var21, var22, var23, 1.0F).endVertex();
+            }
+
+            var19.draw();
+            GlStateManager.popMatrix();
+            GlStateManager.matrixMode(5888);
             this.bindTexture(END_SKY_TEXTURE);
-            var17 = 0.15F;
-            GlStateManager.enableBlend();
-            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
          }

-         if (var16 >= 1) {
-            this.bindTexture(END_PORTAL_TEXTURE);
-            var15 = true;
-            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
+         GlStateManager.disableBlend();
+         GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
+         GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
+         GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
+         GlStateManager.enableLighting();
+         if (var15) {
+            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
          }
-
-         if (var16 == 1) {
-            GlStateManager.enableBlend();
-            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
-         }
-
-         GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
-         GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
-         GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
-         GlStateManager.texGen(GlStateManager.TexGen.S, 9474, this.getBuffer(1.0F, 0.0F, 0.0F, 0.0F));
-         GlStateManager.texGen(GlStateManager.TexGen.T, 9474, this.getBuffer(0.0F, 1.0F, 0.0F, 0.0F));
-         GlStateManager.texGen(GlStateManager.TexGen.R, 9474, this.getBuffer(0.0F, 0.0F, 1.0F, 0.0F));
-         GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
-         GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
-         GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
-         GlStateManager.popMatrix();
-         GlStateManager.matrixMode(5890);
-         GlStateManager.pushMatrix();
-         GlStateManager.loadIdentity();
-         GlStateManager.translate(0.5F, 0.5F, 0.0F);
-         GlStateManager.scale(0.5F, 0.5F, 1.0F);
-         float var18 = var16 + 1;
-         GlStateManager.translate(17.0F / var18, (2.0F + var18 / 1.5F) * ((float)Minecraft.getSystemTime() % 800000.0F / 800000.0F), 0.0F);
-         GlStateManager.rotate((var18 * var18 * 4321.0F + var18 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
-         GlStateManager.scale(4.5F - var18 / 4.0F, 4.5F - var18 / 4.0F, 1.0F);
-         GlStateManager.multMatrix(PROJECTION);
-         GlStateManager.multMatrix(MODELVIEW);
-         Tessellator var19 = Tessellator.getInstance();
-         BufferBuilder var20 = var19.getBuffer();
-         var20.begin(7, DefaultVertexFormats.POSITION_COLOR);
-         float var21 = (RANDOM.nextFloat() * 0.5F + 0.1F) * var17;
-         float var22 = (RANDOM.nextFloat() * 0.5F + 0.4F) * var17;
-         float var23 = (RANDOM.nextFloat() * 0.5F + 0.5F) * var17;
-         if (var1.shouldRenderFace(EnumFacing.SOUTH)) {
-            var20.pos(var2, var4, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2 + 1.0, var4, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2 + 1.0, var4 + 1.0, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2, var4 + 1.0, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
-         }
-
-         if (var1.shouldRenderFace(EnumFacing.NORTH)) {
-            var20.pos(var2, var4 + 1.0, var6).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2 + 1.0, var4 + 1.0, var6).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2 + 1.0, var4, var6).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2, var4, var6).color(var21, var22, var23, 1.0F).endVertex();
-         }
-
-         if (var1.shouldRenderFace(EnumFacing.EAST)) {
-            var20.pos(var2 + 1.0, var4 + 1.0, var6).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2 + 1.0, var4 + 1.0, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2 + 1.0, var4, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2 + 1.0, var4, var6).color(var21, var22, var23, 1.0F).endVertex();
-         }
-
-         if (var1.shouldRenderFace(EnumFacing.WEST)) {
-            var20.pos(var2, var4, var6).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2, var4, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2, var4 + 1.0, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2, var4 + 1.0, var6).color(var21, var22, var23, 1.0F).endVertex();
-         }
-
-         if (var1.shouldRenderFace(EnumFacing.DOWN)) {
-            var20.pos(var2, var4, var6).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2 + 1.0, var4, var6).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2 + 1.0, var4, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2, var4, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
-         }
-
-         if (var1.shouldRenderFace(EnumFacing.UP)) {
-            var20.pos(var2, var4 + var14, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2 + 1.0, var4 + var14, var6 + 1.0).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2 + 1.0, var4 + var14, var6).color(var21, var22, var23, 1.0F).endVertex();
-            var20.pos(var2, var4 + var14, var6).color(var21, var22, var23, 1.0F).endVertex();
-         }
-
-         var19.draw();
-         GlStateManager.popMatrix();
-         GlStateManager.matrixMode(5888);
-         this.bindTexture(END_SKY_TEXTURE);
-      }
-
-      GlStateManager.disableBlend();
-      GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
-      GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
-      GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
-      GlStateManager.enableLighting();
-      if (var15) {
-         Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
       }
    }

    protected int getPasses(double var1) {
       byte var3;
       if (var1 > 36864.0) {
 */
