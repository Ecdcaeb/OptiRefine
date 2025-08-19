package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.library.common.utils.Counter;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.optifine.EmissiveTextures;
import net.optifine.entity.model.CustomEntityModels;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.FloatBuffer;
import java.util.List;

@Mixin(RenderLivingBase.class)
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> {
    @Unique
    public EntityLivingBase renderEntity;
    @Unique
    public float renderLimbSwing;
    @Unique
    public float renderLimbSwingAmount;
    @Unique
    public float renderAgeInTicks;
    @Unique
    public float renderHeadYaw;
    @Unique
    public float renderHeadPitch;
    @Unique
    public float renderScaleFactor;
    @Unique
    public float renderPartialTicks;
    @Unique
    private boolean renderModelPushMatrix;
    @Unique
    private boolean renderLayersPushMatrix;
    @Unique @Public
    private static final boolean animateModelLiving = Boolean.getBoolean("animate.model.living");

    @WrapOperation(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V", ordinal = 0))
    public void beforeLivingRendered(Operation<Void> original, @Local(argsOnly = true) EntityLivingBase entity){
        if (animateModelLiving) {
            entity.limbSwingAmount = 1.0F;
        }
        original.call();
    }

    @WrapOperation(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V", ordinal = 0))
    public void customEntityModelsAction(ModelBase instance, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, Operation<Void> original, @Local(argsOnly = true, ordinal = 1) float partialTicks){
        original.call(instance, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        if (CustomEntityModels.isActive()) {
            this.renderEntity = (EntityLivingBase) entityIn;
            this.renderLimbSwing = limbSwing;
            this.renderLimbSwingAmount = limbSwingAmount;
            this.renderAgeInTicks = ageInTicks;
            this.renderHeadYaw = netHeadYaw;
            this.renderHeadPitch = headPitch;
            this.renderScaleFactor = scaleFactor;
            this.renderPartialTicks = partialTicks;
        }
    }

    @WrapOperation(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;renderModel(Lnet/minecraft/entity/EntityLivingBase;FFFFFF)V", ordinal = 1))
    public void customEmissiveTextures(RenderLivingBase<EntityLivingBase> instance, EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Operation<Void> original){
        if (EmissiveTextures.isActive()) {
            EmissiveTextures.beginRender();
        }

        if (this.renderModelPushMatrix) {
            GlStateManager.pushMatrix();
        }

        original.call(instance, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        if (this.renderModelPushMatrix) {
            GlStateManager.popMatrix();
        }

        if (EmissiveTextures.isActive()) {
            if (EmissiveTextures.hasEmissive()) {
                this.renderModelPushMatrix = true;
                EmissiveTextures.beginRenderEmissive();
                GlStateManager.pushMatrix();
                original.call(instance, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                GlStateManager.popMatrix();
                EmissiveTextures.endRenderEmissive();
            }

            EmissiveTextures.endRender();
        }
    }

    @Inject(method = "unsetBrightness", at = @At("TAIL"))
    public void onunsetBrightness(CallbackInfo ci){
        if (Config.isShaders()) {
            Shaders.setEntityColor(0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    @WrapOperation(method = "renderLayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/LayerRenderer;doRenderLayer(Lnet/minecraft/entity/EntityLivingBase;FFFFFFF)V"))
    public void onRenderLayers(LayerRenderer<EntityLivingBase> instance, EntityLivingBase e, float v1, float v2, float v3, float v4, float v5, float v6, float v7, Operation<Void> original){
        if (EmissiveTextures.isActive()) {
            EmissiveTextures.beginRender();
        }

        if (this.renderLayersPushMatrix) {
            GlStateManager.pushMatrix();
        }

        original.call(instance, e, v1, v2, v3, v4, v5, v6, v7);
        if (this.renderLayersPushMatrix) {
            GlStateManager.popMatrix();
        }

        if (EmissiveTextures.isActive()) {
            if (EmissiveTextures.hasEmissive()) {
                this.renderLayersPushMatrix = true;
                EmissiveTextures.beginRenderEmissive();
                GlStateManager.pushMatrix();
                original.call(instance, e, v1, v2, v3, v4, v5, v6, v7);
                GlStateManager.popMatrix();
                EmissiveTextures.endRenderEmissive();
            }

            EmissiveTextures.endRender();
        }
    }

    @Inject(method = "setBrightness", at = @At("HEAD"))
    public void initBrightnessSetting(EntityLivingBase entitylivingbaseIn, float partialTicks, boolean combineTextures, CallbackInfoReturnable<Boolean> cir,
                                      @Share(namespace = "optirefine", value = "bufferCounter")LocalRef<Counter> bufferCounter,
                                      @Share(namespace = "optirefine", value = "colorVec3f") LocalRef<float[]> vec3fColor){

        bufferCounter.set(new Counter());
        vec3fColor.set(new float[3]);
    }

    @WrapOperation(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;"))
    public FloatBuffer afterBrightnessSetting(FloatBuffer instance, float v, Operation<FloatBuffer> original,
                                              @Share(namespace = "optirefine", value = "bufferCounter")LocalRef<Counter> bufferCounter,
                                              @Share(namespace = "optirefine", value = "colorVec3f") LocalRef<float[]> vec3fColor){
        original.call(instance, v);
        bufferCounter.get().add();
        int count = bufferCounter.get().count();
        if (count == 4) {
            if (Config.isShaders()) {
                float[] vec3f = vec3fColor.get();
                Shaders.setEntityColor(vec3f[0], vec3f[1], vec3f[2], v);
            }
        } else {
            vec3fColor.get()[count - 1] = v;
        }
        return instance;
    }

    /*
    * this.brightnessBuffer.put(f2);
      this.brightnessBuffer.put(f3);
      this.brightnessBuffer.put(f4);
      this.brightnessBuffer.put(1.0F - f1);
      * -->
      this.myOperationHandler(f2, f3, f4, 1.0F - f1)

    * */
    /**
     * if (Config.isShaders()) {
     *           Shaders.setEntityColor(f2, f3, f4, 1.0F - f1);
     *       }
     * */

    @Shadow
    protected List<LayerRenderer<T>> layerRenderers;

    @Unique
    public List<LayerRenderer<T>> getLayerRenderers() {
        return this.layerRenderers;
    }
}

/*
--- net/minecraft/client/renderer/entity/RenderLivingBase.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/entity/RenderLivingBase.java	Tue Aug 19 14:59:58 2025
@@ -4,41 +4,61 @@
 import java.nio.Buffer;
 import java.nio.FloatBuffer;
 import java.util.List;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.client.model.ModelBase;
+import net.minecraft.client.model.ModelSpider;
 import net.minecraft.client.renderer.GLAllocation;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
 import net.minecraft.client.renderer.entity.layers.LayerRenderer;
 import net.minecraft.client.renderer.texture.DynamicTexture;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.entity.player.EnumPlayerModelParts;
 import net.minecraft.scoreboard.Team;
+import net.minecraft.scoreboard.Team.EnumVisible;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.util.text.TextFormatting;
+import net.optifine.EmissiveTextures;
+import net.optifine.entity.model.CustomEntityModels;
+import net.optifine.reflect.Reflector;
+import net.optifine.shaders.Shaders;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public abstract class RenderLivingBase<T extends EntityLivingBase> extends Render<T> {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DynamicTexture TEXTURE_BRIGHTNESS = new DynamicTexture(16, 16);
-   protected ModelBase mainModel;
+   public ModelBase mainModel;
    protected FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
    protected List<LayerRenderer<T>> layerRenderers = Lists.newArrayList();
    protected boolean renderMarker;
+   public static float NAME_TAG_RANGE = 64.0F;
+   public static float NAME_TAG_RANGE_SNEAK = 32.0F;
+   public EntityLivingBase renderEntity;
+   public float renderLimbSwing;
+   public float renderLimbSwingAmount;
+   public float renderAgeInTicks;
+   public float renderHeadYaw;
+   public float renderHeadPitch;
+   public float renderScaleFactor;
+   public float renderPartialTicks;
+   private boolean renderModelPushMatrix;
+   private boolean renderLayersPushMatrix;
+   public static final boolean animateModelLiving = Boolean.getBoolean("animate.model.living");

    public RenderLivingBase(RenderManager var1, ModelBase var2, float var3) {
       super(var1);
       this.mainModel = var2;
       this.shadowSize = var3;
+      this.renderModelPushMatrix = this.mainModel instanceof ModelSpider;
    }

-   protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U var1) {
+   public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U var1) {
       return this.layerRenderers.add(var1);
    }

    public ModelBase getMainModel() {
       return this.mainModel;
    }
@@ -58,106 +78,162 @@
    }

    public void transformHeldFull3DItemLayer() {
    }

    public void doRender(T var1, double var2, double var4, double var6, float var8, float var9) {
-      GlStateManager.pushMatrix();
-      GlStateManager.disableCull();
-      this.mainModel.swingProgress = this.getSwingProgress((T)var1, var9);
-      this.mainModel.isRiding = var1.isRiding();
-      this.mainModel.isChild = var1.isChild();
-
-      try {
-         float var10 = this.interpolateRotation(var1.prevRenderYawOffset, var1.renderYawOffset, var9);
-         float var11 = this.interpolateRotation(var1.prevRotationYawHead, var1.rotationYawHead, var9);
-         float var12 = var11 - var10;
-         if (var1.isRiding() && var1.getRidingEntity() instanceof EntityLivingBase) {
-            EntityLivingBase var13 = (EntityLivingBase)var1.getRidingEntity();
-            var10 = this.interpolateRotation(var13.prevRenderYawOffset, var13.renderYawOffset, var9);
-            var12 = var11 - var10;
-            float var14 = MathHelper.wrapDegrees(var12);
-            if (var14 < -85.0F) {
-               var14 = -85.0F;
-            }
-
-            if (var14 >= 85.0F) {
-               var14 = 85.0F;
-            }
-
-            var10 = var11 - var14;
-            if (var14 * var14 > 2500.0F) {
-               var10 += var14 * 0.2F;
-            }
-
-            var12 = var11 - var10;
-         }
-
-         float var22 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var9;
-         this.renderLivingAt((T)var1, var2, var4, var6);
-         float var23 = this.handleRotationFloat((T)var1, var9);
-         this.applyRotations((T)var1, var23, var10, var9);
-         float var15 = this.prepareScale((T)var1, var9);
-         float var16 = 0.0F;
-         float var17 = 0.0F;
-         if (!var1.isRiding()) {
-            var16 = var1.prevLimbSwingAmount + (var1.limbSwingAmount - var1.prevLimbSwingAmount) * var9;
-            var17 = var1.limbSwing - var1.limbSwingAmount * (1.0F - var9);
-            if (var1.isChild()) {
-               var17 *= 3.0F;
-            }
-
-            if (var16 > 1.0F) {
-               var16 = 1.0F;
-            }
-         }
+      if (!Reflector.RenderLivingEvent_Pre_Constructor.exists()
+         || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, new Object[]{var1, this, var9, var2, var4, var6})) {
+         if (animateModelLiving) {
+            var1.limbSwingAmount = 1.0F;
+         }
+
+         GlStateManager.pushMatrix();
+         GlStateManager.disableCull();
+         this.mainModel.swingProgress = this.getSwingProgress((T)var1, var9);
+         this.mainModel.isRiding = var1.isRiding();
+         if (Reflector.ForgeEntity_shouldRiderSit.exists()) {
+            this.mainModel.isRiding = var1.isRiding()
+               && var1.getRidingEntity() != null
+               && Reflector.callBoolean(var1.getRidingEntity(), Reflector.ForgeEntity_shouldRiderSit, new Object[0]);
+         }
+
+         this.mainModel.isChild = var1.isChild();
+
+         try {
+            float var10 = this.interpolateRotation(var1.prevRenderYawOffset, var1.renderYawOffset, var9);
+            float var11 = this.interpolateRotation(var1.prevRotationYawHead, var1.rotationYawHead, var9);
+            float var12 = var11 - var10;
+            if (this.mainModel.isRiding && var1.getRidingEntity() instanceof EntityLivingBase) {
+               EntityLivingBase var13 = (EntityLivingBase)var1.getRidingEntity();
+               var10 = this.interpolateRotation(var13.prevRenderYawOffset, var13.renderYawOffset, var9);
+               var12 = var11 - var10;
+               float var14 = MathHelper.wrapDegrees(var12);
+               if (var14 < -85.0F) {
+                  var14 = -85.0F;
+               }
+
+               if (var14 >= 85.0F) {
+                  var14 = 85.0F;
+               }
+
+               var10 = var11 - var14;
+               if (var14 * var14 > 2500.0F) {
+                  var10 += var14 * 0.2F;
+               }
+
+               var12 = var11 - var10;
+            }
+
+            float var22 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var9;
+            this.renderLivingAt((T)var1, var2, var4, var6);
+            float var23 = this.handleRotationFloat((T)var1, var9);
+            this.applyRotations((T)var1, var23, var10, var9);
+            float var15 = this.prepareScale((T)var1, var9);
+            float var16 = 0.0F;
+            float var17 = 0.0F;
+            if (!var1.isRiding()) {
+               var16 = var1.prevLimbSwingAmount + (var1.limbSwingAmount - var1.prevLimbSwingAmount) * var9;
+               var17 = var1.limbSwing - var1.limbSwingAmount * (1.0F - var9);
+               if (var1.isChild()) {
+                  var17 *= 3.0F;
+               }
+
+               if (var16 > 1.0F) {
+                  var16 = 1.0F;
+               }
+            }
+
+            GlStateManager.enableAlpha();
+            this.mainModel.setLivingAnimations(var1, var17, var16, var9);
+            this.mainModel.setRotationAngles(var17, var16, var23, var12, var22, var15, var1);
+            if (CustomEntityModels.isActive()) {
+               this.renderEntity = var1;
+               this.renderLimbSwing = var17;
+               this.renderLimbSwingAmount = var16;
+               this.renderAgeInTicks = var23;
+               this.renderHeadYaw = var12;
+               this.renderHeadPitch = var22;
+               this.renderScaleFactor = var15;
+               this.renderPartialTicks = var9;
+            }
+
+            if (this.renderOutlines) {
+               boolean var24 = this.setScoreTeamColor((T)var1);
+               GlStateManager.enableColorMaterial();
+               GlStateManager.enableOutlineMode(this.getTeamColor((T)var1));
+               if (!this.renderMarker) {
+                  this.renderModel((T)var1, var17, var16, var23, var12, var22, var15);
+               }
+
+               if (!(var1 instanceof EntityPlayer) || !((EntityPlayer)var1).isSpectator()) {
+                  this.renderLayers((T)var1, var17, var16, var9, var23, var12, var22, var15);
+               }
+
+               GlStateManager.disableOutlineMode();
+               GlStateManager.disableColorMaterial();
+               if (var24) {
+                  this.unsetScoreTeamColor();
+               }
+            } else {
+               boolean var18 = this.setDoRenderBrightness((T)var1, var9);
+               if (EmissiveTextures.isActive()) {
+                  EmissiveTextures.beginRender();
+               }
+
+               if (this.renderModelPushMatrix) {
+                  GlStateManager.pushMatrix();
+               }

-         GlStateManager.enableAlpha();
-         this.mainModel.setLivingAnimations(var1, var17, var16, var9);
-         this.mainModel.setRotationAngles(var17, var16, var23, var12, var22, var15, var1);
-         if (this.renderOutlines) {
-            boolean var24 = this.setScoreTeamColor((T)var1);
-            GlStateManager.enableColorMaterial();
-            GlStateManager.enableOutlineMode(this.getTeamColor((T)var1));
-            if (!this.renderMarker) {
                this.renderModel((T)var1, var17, var16, var23, var12, var22, var15);
-            }
-
-            if (!(var1 instanceof EntityPlayer) || !((EntityPlayer)var1).isSpectator()) {
-               this.renderLayers((T)var1, var17, var16, var9, var23, var12, var22, var15);
-            }
-
-            GlStateManager.disableOutlineMode();
-            GlStateManager.disableColorMaterial();
-            if (var24) {
-               this.unsetScoreTeamColor();
-            }
-         } else {
-            boolean var18 = this.setDoRenderBrightness((T)var1, var9);
-            this.renderModel((T)var1, var17, var16, var23, var12, var22, var15);
-            if (var18) {
-               this.unsetBrightness();
-            }
-
-            GlStateManager.depthMask(true);
-            if (!(var1 instanceof EntityPlayer) || !((EntityPlayer)var1).isSpectator()) {
-               this.renderLayers((T)var1, var17, var16, var9, var23, var12, var22, var15);
-            }
+               if (this.renderModelPushMatrix) {
+                  GlStateManager.popMatrix();
+               }
+
+               if (EmissiveTextures.isActive()) {
+                  if (EmissiveTextures.hasEmissive()) {
+                     this.renderModelPushMatrix = true;
+                     EmissiveTextures.beginRenderEmissive();
+                     GlStateManager.pushMatrix();
+                     this.renderModel((T)var1, var17, var16, var23, var12, var22, var15);
+                     GlStateManager.popMatrix();
+                     EmissiveTextures.endRenderEmissive();
+                  }
+
+                  EmissiveTextures.endRender();
+               }
+
+               if (var18) {
+                  this.unsetBrightness();
+               }
+
+               GlStateManager.depthMask(true);
+               if (!(var1 instanceof EntityPlayer) || !((EntityPlayer)var1).isSpectator()) {
+                  this.renderLayers((T)var1, var17, var16, var9, var23, var12, var22, var15);
+               }
+            }
+
+            if (CustomEntityModels.isActive()) {
+               this.renderEntity = null;
+            }
+
+            GlStateManager.disableRescaleNormal();
+         } catch (Exception var19) {
+            LOGGER.error("Couldn't render entity", var19);
          }

-         GlStateManager.disableRescaleNormal();
-      } catch (Exception var19) {
-         LOGGER.error("Couldn't render entity", var19);
+         GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
+         GlStateManager.enableTexture2D();
+         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
+         GlStateManager.enableCull();
+         GlStateManager.popMatrix();
+         super.doRender((T)var1, var2, var4, var6, var8, var9);
+         if (Reflector.RenderLivingEvent_Post_Constructor.exists()) {
+            Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, new Object[]{var1, this, var9, var2, var4, var6});
+         }
       }
-
-      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
-      GlStateManager.enableTexture2D();
-      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
-      GlStateManager.enableCull();
-      GlStateManager.popMatrix();
-      super.doRender((T)var1, var2, var4, var6, var8, var9);
    }

    public float prepareScale(T var1, float var2) {
       GlStateManager.enableRescaleNormal();
       GlStateManager.scale(-1.0F, -1.0F, 1.0F);
       this.preRenderCallback((T)var1, var2);
@@ -245,21 +321,27 @@
          ((Buffer)this.brightnessBuffer).position(0);
          if (var7) {
             this.brightnessBuffer.put(1.0F);
             this.brightnessBuffer.put(0.0F);
             this.brightnessBuffer.put(0.0F);
             this.brightnessBuffer.put(0.3F);
+            if (Config.isShaders()) {
+               Shaders.setEntityColor(1.0F, 0.0F, 0.0F, 0.3F);
+            }
          } else {
             float var8 = (var5 >> 24 & 0xFF) / 255.0F;
             float var9 = (var5 >> 16 & 0xFF) / 255.0F;
             float var10 = (var5 >> 8 & 0xFF) / 255.0F;
             float var11 = (var5 & 0xFF) / 255.0F;
             this.brightnessBuffer.put(var9);
             this.brightnessBuffer.put(var10);
             this.brightnessBuffer.put(var11);
             this.brightnessBuffer.put(1.0F - var8);
+            if (Config.isShaders()) {
+               Shaders.setEntityColor(var9, var10, var11, 1.0F - var8);
+            }
          }

          ((Buffer)this.brightnessBuffer).flip();
          GlStateManager.glTexEnv(8960, 8705, this.brightnessBuffer);
          GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
          GlStateManager.enableTexture2D();
@@ -313,12 +395,15 @@
       GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
       GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
       GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
       GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
       GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
       GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
+      if (Config.isShaders()) {
+         Shaders.setEntityColor(0.0F, 0.0F, 0.0F, 0.0F);
+      }
    }

    protected void renderLivingAt(T var1, double var2, double var4, double var6) {
       GlStateManager.translate((float)var2, (float)var4, (float)var6);
    }

@@ -351,13 +436,38 @@
       return var1.ticksExisted + var2;
    }

    protected void renderLayers(T var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
       for (LayerRenderer var10 : this.layerRenderers) {
          boolean var11 = this.setBrightness((T)var1, var4, var10.shouldCombineTextures());
+         if (EmissiveTextures.isActive()) {
+            EmissiveTextures.beginRender();
+         }
+
+         if (this.renderLayersPushMatrix) {
+            GlStateManager.pushMatrix();
+         }
+
          var10.doRenderLayer(var1, var2, var3, var4, var5, var6, var7, var8);
+         if (this.renderLayersPushMatrix) {
+            GlStateManager.popMatrix();
+         }
+
+         if (EmissiveTextures.isActive()) {
+            if (EmissiveTextures.hasEmissive()) {
+               this.renderLayersPushMatrix = true;
+               EmissiveTextures.beginRenderEmissive();
+               GlStateManager.pushMatrix();
+               var10.doRenderLayer(var1, var2, var3, var4, var5, var6, var7, var8);
+               GlStateManager.popMatrix();
+               EmissiveTextures.endRenderEmissive();
+            }
+
+            EmissiveTextures.endRender();
+         }
+
          if (var11) {
             this.unsetBrightness();
          }
       }
    }

@@ -370,31 +480,38 @@
    }

    protected void preRenderCallback(T var1, float var2) {
    }

    public void renderName(T var1, double var2, double var4, double var6) {
-      if (this.canRenderName(var1)) {
-         double var8 = var1.getDistanceSq(this.renderManager.renderViewEntity);
-         float var10 = var1.isSneaking() ? 32.0F : 64.0F;
-         if (!(var8 >= var10 * var10)) {
-            String var11 = var1.getDisplayName().getFormattedText();
-            GlStateManager.alphaFunc(516, 0.1F);
-            this.renderEntityName((T)var1, var2, var4, var6, var11, var8);
+      if (!Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists()
+         || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Pre_Constructor, new Object[]{var1, this, var2, var4, var6})) {
+         if (this.canRenderName((T)var1)) {
+            double var8 = var1.getDistanceSq(this.renderManager.renderViewEntity);
+            float var10 = var1.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;
+            if (var8 < var10 * var10) {
+               String var11 = var1.getDisplayName().getFormattedText();
+               GlStateManager.alphaFunc(516, 0.1F);
+               this.renderEntityName((T)var1, var2, var4, var6, var11, var8);
+            }
+         }
+
+         if (Reflector.RenderLivingEvent_Specials_Post_Constructor.exists()) {
+            Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Post_Constructor, new Object[]{var1, this, var2, var4, var6});
          }
       }
    }

    protected boolean canRenderName(T var1) {
       EntityPlayerSP var2 = Minecraft.getMinecraft().player;
       boolean var3 = !var1.isInvisibleToPlayer(var2);
       if (var1 != var2) {
          Team var4 = var1.getTeam();
          Team var5 = var2.getTeam();
          if (var4 != null) {
-            Team.EnumVisible var6 = var4.getNameTagVisibility();
+            EnumVisible var6 = var4.getNameTagVisibility();
             switch (var6) {
                case ALWAYS:
                   return var3;
                case NEVER:
                   return false;
                case HIDE_FOR_OTHER_TEAMS:
@@ -405,12 +522,16 @@
                   return true;
             }
          }
       }

       return Minecraft.isGuiEnabled() && var1 != this.renderManager.renderViewEntity && var3 && !var1.isBeingRidden();
+   }
+
+   public List<LayerRenderer<T>> getLayerRenderers() {
+      return this.layerRenderers;
    }

    static {
       int[] var0 = TEXTURE_BRIGHTNESS.getTextureData();

       for (int var1 = 0; var1 < 256; var1++) {
 */
