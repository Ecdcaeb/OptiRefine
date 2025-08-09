package mods.Hileb.optirefine.mixin.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.library.common.Counter;
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
        original.call(instance, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn)
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
