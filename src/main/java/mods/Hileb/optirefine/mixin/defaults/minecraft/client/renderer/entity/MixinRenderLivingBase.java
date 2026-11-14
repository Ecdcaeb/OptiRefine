package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
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
import org.objectweb.asm.Opcodes;
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
    @AccessTransformer(name = "field_77045_g", deobf = true, access = org.objectweb.asm.Opcodes.ACC_PUBLIC)
    // Publicize mainModel in both production SRG and development MCP namespaces.
    public ModelBase optiRefine$mainModelAccess;


    @Shadow
    // [AUDIT-OK] baseline member mainModel (protected)
    protected ModelBase mainModel;

    @Shadow
    protected FloatBuffer brightnessBuffer;


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
    public void optiRefine$applyLivingModelAnimation(Operation<Void> original, @Local(argsOnly = true) EntityLivingBase entity){
        if (animateModelLiving) {
            entity.limbSwingAmount = 1.0F;
        }
        original.call();
    }

    @WrapOperation(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V", ordinal = 0))
    // @Local ordinals count values of the captured type: entityYaw is float 0, partialTicks is float 1.
    public void optiRefine$captureCustomModelRenderState(ModelBase model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, Operation<Void> original, @Local(argsOnly = true, ordinal = 1) float partialTicks){
        original.call(model, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
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

    @WrapMethod(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V")
    private void optiRefine$renderContext(EntityLivingBase entity, double x, double y, double z, float yaw, float partialTicks, Operation<Void> original) {
        if (!CustomEntityModels.isActive()) {
            original.call(entity, x, y, z, yaw, partialTicks);
            return;
        }
        EntityLivingBase previousEntity = this.renderEntity;
        float previousLimbSwing = this.renderLimbSwing;
        float previousLimbSwingAmount = this.renderLimbSwingAmount;
        float previousAgeInTicks = this.renderAgeInTicks;
        float previousHeadYaw = this.renderHeadYaw;
        float previousHeadPitch = this.renderHeadPitch;
        float previousScale = this.renderScaleFactor;
        float previousPartialTicks = this.renderPartialTicks;
        try {
            original.call(entity, x, y, z, yaw, partialTicks);
        } finally {
            this.renderEntity = previousEntity;
            this.renderLimbSwing = previousLimbSwing;
            this.renderLimbSwingAmount = previousLimbSwingAmount;
            this.renderAgeInTicks = previousAgeInTicks;
            this.renderHeadYaw = previousHeadYaw;
            this.renderHeadPitch = previousHeadPitch;
            this.renderScaleFactor = previousScale;
            this.renderPartialTicks = previousPartialTicks;
        }
    }

    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.optifine.EmissiveTextures optiRefine$isRendering ()Z")
    private static native boolean optiRefine$isEmissiveTexturesRendering();

    @Inject(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;enableOutlineMode(I)V", shift = At.Shift.AFTER))
    private void optiRefine$cullOutlineBackFaces(CallbackInfo ci) {
        // The through-wall glow pass disables depth testing. Living models normally also disable
        // culling, so a box's later back faces overwrite its visible faces (issue #2).
        // Cull only inside the outline branch; leave the ordinary two-sided model pass intact.
        GlStateManager.enableCull();
    }

    @Inject(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;disableOutlineMode()V"))
    private void optiRefine$restoreOutlineCulling(CallbackInfo ci) {
        GlStateManager.disableCull();
    }

    @WrapOperation(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;renderModel(Lnet/minecraft/entity/EntityLivingBase;FFFFFF)V", ordinal = 1))
    // [AUDIT-OK] renderModel ordinal 1 = else-branch call; emissive double-render matches OF:180-200
    public void optiRefine$renderEmissiveModel(RenderLivingBase<EntityLivingBase> renderer, EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Operation<Void> original){
        boolean emissive = EmissiveTextures.isActive() && !optiRefine$isEmissiveTexturesRendering();
        if (emissive) {
            EmissiveTextures.beginRender();
        }
        try {
            boolean pushMatrix = this.renderModelPushMatrix;
            if (pushMatrix) {
                GlStateManager.pushMatrix();
            }
            try {
                original.call(renderer, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            } finally {
                if (pushMatrix) {
                    GlStateManager.popMatrix();
                }
            }
            if (emissive && EmissiveTextures.hasEmissive()) {
                this.renderModelPushMatrix = true;
                EmissiveTextures.beginRenderEmissive();
                try {
                    GlStateManager.pushMatrix();
                    try {
                        original.call(renderer, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                    } finally {
                        GlStateManager.popMatrix();
                    }
                } finally {
                    EmissiveTextures.endRenderEmissive();
                }
            }
        } finally {
            if (emissive) {
                EmissiveTextures.endRender();
            }
        }
    }

    @Inject(method = "unsetBrightness", at = @At("TAIL"))
    // [AUDIT-OK] unsetBrightness baseline; Shaders.setEntityColor(0,0,0,0) matches OF
    public void optiRefine$clearShaderEntityColor(CallbackInfo ci){
        if (Config.isShaders()) {
            Shaders.setEntityColor(0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    @WrapOperation(method = "renderLayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/LayerRenderer;doRenderLayer(Lnet/minecraft/entity/EntityLivingBase;FFFFFFF)V"))
    // [AUDIT-OK] renderLayers LayerRenderer.doRenderLayer wrap matches OF renderLayers emissive structure
    public void optiRefine$renderEmissiveLayer(LayerRenderer<EntityLivingBase> layer, EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, Operation<Void> original){
        boolean emissive = EmissiveTextures.isActive() && !optiRefine$isEmissiveTexturesRendering();
        if (emissive) {
            EmissiveTextures.beginRender();
        }
        try {
            boolean pushMatrix = this.renderLayersPushMatrix;
            if (pushMatrix) {
                GlStateManager.pushMatrix();
            }
            try {
                original.call(layer, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            } finally {
                if (pushMatrix) {
                    GlStateManager.popMatrix();
                }
            }
            if (emissive && EmissiveTextures.hasEmissive()) {
                this.renderLayersPushMatrix = true;
                EmissiveTextures.beginRenderEmissive();
                try {
                    GlStateManager.pushMatrix();
                    try {
                        original.call(layer, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                    } finally {
                        GlStateManager.popMatrix();
                    }
                } finally {
                    EmissiveTextures.endRenderEmissive();
                }
            }
        } finally {
            if (emissive) {
                EmissiveTextures.endRender();
            }
        }
    }

    @Inject(method = "setBrightness", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;glTexEnv(IILjava/nio/FloatBuffer;)V"))
    private void optiRefine$setShaderEntityColor(EntityLivingBase entitylivingbaseIn, float partialTicks, boolean combineTextures, CallbackInfoReturnable<Boolean> cir) {
        // Both hurt and color-multiplier branches have populated and flipped this existing buffer.
        // Absolute reads preserve its position and avoid per-render counters and color arrays.
        if (Config.isShaders()) {
            Shaders.setEntityColor(this.brightnessBuffer.get(0), this.brightnessBuffer.get(1), this.brightnessBuffer.get(2), this.brightnessBuffer.get(3));
        }
    }


    @Shadow
    // [AUDIT-OK] baseline member layerRenderers (protected, deobf:32)
    protected List<LayerRenderer<T>> layerRenderers;

    @Unique
    // [AUDIT-OK] OF-added member (OF:530)
    public List<LayerRenderer<T>> getLayerRenderers() {
        return this.layerRenderers;
    }
}
