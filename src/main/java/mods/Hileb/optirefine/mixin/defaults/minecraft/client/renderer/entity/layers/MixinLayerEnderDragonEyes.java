package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.layers.LayerEnderDragonEyes;
import net.minecraft.entity.Entity;
import net.optifine.shaders.Shaders;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayerEnderDragonEyes.class)
public abstract class MixinLayerEnderDragonEyes {
    @WrapOperation(method = "doRenderLayer(Lnet/minecraft/entity/boss/EntityDragon;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void wrap_render(ModelBase instance, Entity entity, float v0, float v1, float v2, float v3, float v4, float v5, Operation<Void> original){
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
+++ net/minecraft/client/renderer/entity/layers/LayerEnderDragonEyes.java	Tue Aug 19 14:59:58 2025
@@ -3,38 +3,49 @@
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.entity.RenderDragon;
 import net.minecraft.entity.boss.EntityDragon;
 import net.minecraft.util.ResourceLocation;
+import net.optifine.shaders.Shaders;

 public class LayerEnderDragonEyes implements LayerRenderer<EntityDragon> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");
    private final RenderDragon dragonRenderer;

    public LayerEnderDragonEyes(RenderDragon var1) {
       this.dragonRenderer = var1;
    }

    public void doRenderLayer(EntityDragon var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
-      this.dragonRenderer.bindTexture(TEXTURE);
+      this.dragonRenderer.a(TEXTURE);
       GlStateManager.enableBlend();
       GlStateManager.disableAlpha();
       GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
       GlStateManager.disableLighting();
       GlStateManager.depthFunc(514);
       char var9 = '\uf0f0';
       char var10 = '\uf0f0';
       boolean var11 = false;
       OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680.0F, 0.0F);
       GlStateManager.enableLighting();
       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
       Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
-      this.dragonRenderer.getMainModel().render(var1, var2, var3, var5, var6, var7, var8);
+      if (Config.isShaders()) {
+         Shaders.beginSpiderEyes();
+      }
+
+      Config.getRenderGlobal().renderOverlayEyes = true;
+      this.dragonRenderer.b().render(var1, var2, var3, var5, var6, var7, var8);
+      Config.getRenderGlobal().renderOverlayEyes = false;
+      if (Config.isShaders()) {
+         Shaders.endSpiderEyes();
+      }
+
       Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
-      this.dragonRenderer.setLightmap(var1);
+      this.dragonRenderer.c(var1);
       GlStateManager.disableBlend();
       GlStateManager.enableAlpha();
       GlStateManager.depthFunc(515);
    }

    public boolean shouldCombineTextures() {
     */
