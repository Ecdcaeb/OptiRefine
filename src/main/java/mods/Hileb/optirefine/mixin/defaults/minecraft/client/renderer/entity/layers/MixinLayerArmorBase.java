package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItems;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1


    @WrapOperation(method = "renderArmorLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/LayerArmorBase;getArmorResource(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/EntityEquipmentSlot;Ljava/lang/String;)Lnet/minecraft/util/ResourceLocation;"))
    // [AUDIT-OK] 4-arg getArmorResource(Entity,ItemStack,EntityEquipmentSlot,String) added by cleanroom patch (LayerArmorBase.java.patch); bindCustomArmorTexture->null->skip-bind matches OF:65-67/81-82
    public ResourceLocation optiRefine$bindCustomArmorTexture(LayerArmorBase armorLayer, Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type, Operation<ResourceLocation> original){
        if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(stack, slot, type)) {
            return original.call(armorLayer, entity, stack, slot, type);
        }
        return null;
    }

    @WrapWithCondition(method = "renderArmorLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    // [AUDIT-OK] bindTexture skip-when-null complements the null-returning getArmorResource wrap (OF pattern)
    public boolean optiRefine$bindAvailableArmorTexture(RenderLivingBase renderer, ResourceLocation location) {
        return location != null;
    }

    @ModifyExpressionValue(method = "renderArmorLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasEffect()Z"))
    // [AUDIT-FIXED] OF:90/118 gates renderEnchantedGlint on `!skipRenderGlint && stack.hasEffect() &&
    // (!Config.isCustomItems() || !CustomItems.renderCustomArmorEffect(entity, stack, model, limbSwing,
    // limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale))`; custom armor effect textures
    // (CustomItemProperties "effect") never rendered before (renderCustomArmorEffect never invoked). Runtime
    // cleanroom merged the Forge/vanilla glint branches into the single `!skipRenderGlint && itemstack.hasEffect()`.
    private boolean optiRefine$customArmorEffectGlint(boolean original,
                                                      @Local(argsOnly = true) EntityLivingBase entityLivingBaseIn,
                                                      @Local(argsOnly = true, ordinal = 0) float limbSwing,
                                                      @Local(argsOnly = true, ordinal = 1) float limbSwingAmount,
                                                      @Local(argsOnly = true, ordinal = 2) float partialTicks,
                                                      @Local(argsOnly = true, ordinal = 3) float ageInTicks,
                                                      @Local(argsOnly = true, ordinal = 4) float netHeadYaw,
                                                      @Local(argsOnly = true, ordinal = 5) float headPitch,
                                                      @Local(argsOnly = true, ordinal = 6) float scale,
                                                      @Local(argsOnly = true) EntityEquipmentSlot slot,
                                                      @Local ModelBase model) {
        if (original && Config.isCustomItems()) {
            ItemStack itemstack = entityLivingBaseIn.getItemStackFromSlot(slot);
            return !CustomItems.renderCustomArmorEffect(entityLivingBaseIn, itemstack, model, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        }
        return original;
    }

    /**
     * @author OptiRefine
     * @reason OptiFine: shaders glint (isShadowPass skip + renderEnchantedGlintBegin/End)
     */
    @WrapMethod(method = "renderEnchantedGlint")
    // Keep Cleanroom's fog, texture transforms and GL cleanup; only bracket its glint shader.
    private static void optiRefine$renderEnchantedGlint(RenderLivingBase<?> renderer, EntityLivingBase entityLivingBaseIn, ModelBase modelBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, Operation<Void> original) {
        if (Config.isShaders() && Shaders.isShadowPass) {
            return;
        }
        boolean shaders = Config.isShaders();
        if (shaders) {
            ShadersRender.renderEnchantedGlintBegin();
        }
        try {
            original.call(renderer, entityLivingBaseIn, modelBaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        } finally {
            if (shaders) {
                ShadersRender.renderEnchantedGlintEnd();
            }
        }
    }
}
