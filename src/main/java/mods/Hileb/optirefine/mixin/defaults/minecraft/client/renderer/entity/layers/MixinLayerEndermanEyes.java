package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.layers.LayerEndermanEyes;
import net.minecraft.entity.Entity;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayerEndermanEyes.class)
public abstract class MixinLayerEndermanEyes {

    @WrapOperation(method = "doRenderLayer(Lnet/minecraft/entity/monster/EntityEnderman;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelEnderman;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void wrap_render(ModelEnderman instance, Entity entity, float v0, float v1, float v2, float v3, float v4, float v5, Operation<Void> original){
        if (Config.isShaders()) {
            Shaders.beginSpiderEyes();
        }

        RenderGlobal_renderOverlayEyes_set(Config.getRenderGlobal(), true);
        instance.render(entity, v0, v1, v2, v3, v4, v5);
        RenderGlobal_renderOverlayEyes_set(Config.getRenderGlobal(), false);
        if (Config.isShaders()) {
            // SpiderEyes?
            Shaders.endSpiderEyes();
        }
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.RenderGlobal renderOverlayEyes Z")
    private static native void RenderGlobal_renderOverlayEyes_set(RenderGlobal renderGlobal, boolean val);

}
/*
+++ net/minecraft/client/renderer/entity/layers/LayerEndermanEyes.java	Tue Aug 19 14:59:58 2025
@@ -3,38 +3,49 @@
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.entity.RenderEnderman;
 import net.minecraft.entity.monster.EntityEnderman;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.shaders.Shaders;

 public class LayerEndermanEyes implements LayerRenderer<EntityEnderman> {
    private static final ResourceLocation RES_ENDERMAN_EYES = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
    private final RenderEnderman endermanRenderer;

    public LayerEndermanEyes(RenderEnderman var1) {
       this.endermanRenderer = var1;
    }

    public void doRenderLayer(EntityEnderman var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
-      this.endermanRenderer.bindTexture(RES_ENDERMAN_EYES);
+      this.endermanRenderer.a(RES_ENDERMAN_EYES);
       GlStateManager.enableBlend();
       GlStateManager.disableAlpha();
       GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
       GlStateManager.disableLighting();
       GlStateManager.depthMask(!var1.isInvisible());
       char var9 = '\uf0f0';
       char var10 = '\uf0f0';
       boolean var11 = false;
       OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
       GlStateManager.enableLighting();
       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
       Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
+      if (Config.isShaders()) {
+         Shaders.beginSpiderEyes();
+      }
+
+      Config.getRenderGlobal().renderOverlayEyes = true;
       this.endermanRenderer.getMainModel().render(var1, var2, var3, var5, var6, var7, var8);
+      Config.getRenderGlobal().renderOverlayEyes = false;
+      if (Config.isShaders()) {
+         Shaders.endSpiderEyes();
+      }
+
       Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
-      this.endermanRenderer.setLightmap(var1);
+      this.endermanRenderer.c(var1);
       GlStateManager.depthMask(true);
       GlStateManager.disableBlend();
       GlStateManager.enableAlpha();
    }

    public boolean shouldCombineTextures() { */