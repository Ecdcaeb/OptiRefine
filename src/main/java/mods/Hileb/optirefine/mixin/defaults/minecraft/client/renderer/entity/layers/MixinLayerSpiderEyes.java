package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.Entity;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayerSpiderEyes.class)
public abstract class MixinLayerSpiderEyes {

    @WrapOperation(method = "doRenderLayer(Lnet/minecraft/entity/monster/EntitySpider;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void wrap_render(ModelBase instance, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, Operation<Void> original){
        if (Config.isShaders()) {
            Shaders.beginSpiderEyes();
        }

        RenderGlobal_renderOverlayEyes_set(Config.getRenderGlobal(), true);
        instance.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        RenderGlobal_renderOverlayEyes_set(Config.getRenderGlobal(), false);
        if (Config.isShaders()) {
            Shaders.endSpiderEyes();
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderOverlayEyes Z")
    private static native void RenderGlobal_renderOverlayEyes_set(RenderGlobal renderGlobal, boolean val);
}
/*+++ net/minecraft/client/renderer/entity/layers/LayerSpiderEyes.java	Tue Aug 19 14:59:58 2025
@@ -3,23 +3,24 @@
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.entity.RenderSpider;
 import net.minecraft.entity.monster.EntitySpider;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.shaders.Shaders;

 public class LayerSpiderEyes<T extends EntitySpider> implements LayerRenderer<T> {
    private static final ResourceLocation SPIDER_EYES = new ResourceLocation("textures/entity/spider_eyes.png");
    private final RenderSpider<T> spiderRenderer;

    public LayerSpiderEyes(RenderSpider<T> var1) {
       this.spiderRenderer = var1;
    }

    public void doRenderLayer(T var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
-      this.spiderRenderer.bindTexture(SPIDER_EYES);
+      this.spiderRenderer.a(SPIDER_EYES);
       GlStateManager.enableBlend();
       GlStateManager.disableAlpha();
       GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
       if (var1.isInvisible()) {
          GlStateManager.depthMask(false);
       } else {
@@ -29,19 +30,29 @@
       int var9 = 61680;
       int var10 = var9 % 65536;
       int var11 = var9 / 65536;
       OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var10, var11);
       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
       Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
-      this.spiderRenderer.getMainModel().render(var1, var2, var3, var5, var6, var7, var8);
+      if (Config.isShaders()) {
+         Shaders.beginSpiderEyes();
+      }
+
+      Config.getRenderGlobal().renderOverlayEyes = true;
+      this.spiderRenderer.b().render(var1, var2, var3, var5, var6, var7, var8);
+      Config.getRenderGlobal().renderOverlayEyes = false;
+      if (Config.isShaders()) {
+         Shaders.endSpiderEyes();
+      }
+
       Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
       var9 = var1.getBrightnessForRender();
       var10 = var9 % 65536;
       var11 = var9 / 65536;
       OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var10, var11);
-      this.spiderRenderer.setLightmap((T)var1);
+      this.spiderRenderer.c(var1);
       GlStateManager.disableBlend();
       GlStateManager.enableAlpha();
    }

    public boolean shouldCombineTextures() {
       return false;

 */
