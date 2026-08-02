package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import mods.Hileb.optirefine.library.common.utils.Checked;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Checked
@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase {

    @Shadow
    private boolean skipRenderGlint;

    @WrapOperation(method = "renderArmorLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/LayerArmorBase;getArmorResource(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/EntityEquipmentSlot;Ljava/lang/String;)Lnet/minecraft/util/ResourceLocation;"))
    public ResourceLocation getResources(LayerArmorBase instance, Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type, Operation<ResourceLocation> original){
        if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(stack, slot, type)) {
            return original.call(instance, entity, stack, slot, type);
        }
        return null;
    }

    @WrapWithCondition(method = "renderArmorLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    public boolean replace_renderer_bindTexture_this_getArmorResource(RenderLivingBase instance, ResourceLocation location) {
        return location != null;
    }

    /**
     * @author OptiRefine
     * @reason OptiFine: shaders glint (isShadowPass skip + renderEnchantedGlintBegin/End)
     */
    @WrapMethod(method = "renderEnchantedGlint")
    private static void optiRefine$renderEnchantedGlint(RenderLivingBase<?> renderer, EntityLivingBase entityLivingBaseIn, ModelBase modelBaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Operation<Void> original) {
        if (!Config.isShaders() || !Shaders.isShadowPass) {
            float f = entityLivingBaseIn.ticksExisted + ageInTicks;
            renderer.bindTexture(LayerArmorBase.ENCHANTED_ITEM_GLINT_RES);
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintBegin();
            }
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            GlStateManager.enableBlend();
            GlStateManager.depthFunc(514);
            GlStateManager.depthMask(false);
            float f1 = 0.5F;
            GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);

            for (int i = 0; i < 2; ++i) {
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
                float f2 = 0.76F;
                GlStateManager.color(0.38F, 0.19F, 0.608F, 1.0F);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                float f3 = 0.33333334F;
                GlStateManager.scale(0.33333334F, 0.33333334F, 0.33333334F);
                GlStateManager.rotate(30.0F - i * 60.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.translate(0.0F, f * (0.001F + i * 0.003F) * 20.0F, 0.0F);
                GlStateManager.matrixMode(5888);
                modelBaseIn.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            }

            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            GlStateManager.disableBlend();
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintEnd();
            }
        }
    }
}
