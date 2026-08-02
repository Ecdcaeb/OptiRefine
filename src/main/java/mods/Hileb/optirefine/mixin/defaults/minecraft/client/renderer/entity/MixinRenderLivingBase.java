package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.library.common.utils.Counter;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSpider;
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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1

    @SuppressWarnings("unused")
    @AccessTransformer(name = "mainModel", access = org.objectweb.asm.Opcodes.ACC_PUBLIC)
    // [AUDIT-OK] AT mainModel: vanilla protected (deobf:30) -> public, matches OF:33
    public ModelBase acc_mainModel;

    @SuppressWarnings("unused")
    @AccessTransformer(name = "addLayer", access = org.objectweb.asm.Opcodes.ACC_PUBLIC)
    // [AUDIT-OK] addLayer is already public in baseline (deobf:42) — AT no-op (field-based processor only widens fields); harmless
    public boolean acc_addLayer;

    @Shadow
    // [AUDIT-OK] baseline member mainModel (protected)
    protected ModelBase mainModel;


    // [AUDIT-OK] OF-added fields renderEntity/renderLimbSwing/renderLimbSwingAmount/renderAgeInTicks/renderHeadYaw/renderHeadPitch/renderScaleFactor/renderPartialTicks/renderModelPushMatrix/renderLayersPushMatrix (OF:39-48)
    public EntityLivingBase renderEntity;
    @Unique
    public float renderLimbSwing;
    public float renderLimbSwingAmount;
    @Unique
    public float renderAgeInTicks;
    public float renderHeadYaw;
    @Unique
    public float renderHeadPitch;
    public float renderScaleFactor;
    @Unique
    public float renderPartialTicks;
    private boolean renderModelPushMatrix;
    @Unique
    private boolean renderLayersPushMatrix;
    @Public
    // [AUDIT-OK] OF-added static final (OF:49 public); @Public private static correct
    private static final boolean animateModelLiving = Boolean.getBoolean("animate.model.living");

    @Inject(method = "<init>", at = @At("RETURN"))
    // [AUDIT-OK] ctor init renderModelPushMatrix = mainModel instanceof ModelSpider matches OF:55
    private void optiRefine$initPushMatrix(CallbackInfo ci) {
        this.renderModelPushMatrix = this.mainModel instanceof ModelSpider;
    }

    @WrapOperation(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V", ordinal = 0))
    // [AUDIT-OK] doRender pushMatrix ordinal 0 matches OF:90; animateModelLiving limbSwingAmount=1 matches OF:86
    public void beforeLivingRendered(Operation<Void> original, @Local(argsOnly = true) EntityLivingBase entity){
        if (animateModelLiving) {
            entity.limbSwingAmount = 1.0F;
        }
        original.call();
    }

    @WrapOperation(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V", ordinal = 0))
    // [AUDIT-ISSUE] @Local(argsOnly=true, ordinal=1) resolves to doRender arg1 (double x), NOT partialTicks (arg5) — float local binding fails/wrong value at apply; change ordinal to 5
    public void customEntityModelsAction(ModelBase instance, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, Operation<Void> original, @Local(argsOnly = true, ordinal = 5) float partialTicks){
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
    // [AUDIT-OK] renderModel ordinal 1 = else-branch call; emissive double-render matches OF:180-200
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
    // [AUDIT-OK] unsetBrightness baseline; Shaders.setEntityColor(0,0,0,0) matches OF
    public void onunsetBrightness(CallbackInfo ci){
        if (Config.isShaders()) {
            Shaders.setEntityColor(0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    @WrapOperation(method = "renderLayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/LayerRenderer;doRenderLayer(Lnet/minecraft/entity/EntityLivingBase;FFFFFFF)V"))
    // [AUDIT-OK] renderLayers LayerRenderer.doRenderLayer wrap matches OF renderLayers emissive structure
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
    // [AUDIT-OK] setBrightness baseline; 4x FloatBuffer.put(F) wrap, 4th put -> setEntityColor matches OF:328/340 (incl. hurt-time branch)
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
    // [AUDIT-OK] baseline member layerRenderers (protected, deobf:32)
    protected List<LayerRenderer<T>> layerRenderers;

    @Unique
    // [AUDIT-OK] OF-added member (OF:530)
    public List<LayerRenderer<T>> getLayerRenderers() {
        return this.layerRenderers;
    }
}
