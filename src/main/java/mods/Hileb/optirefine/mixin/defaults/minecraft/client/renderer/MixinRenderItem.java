package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.optifine.CustomColors;
import net.optifine.CustomItems;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

/**
 * OptiFine additions to {@link RenderItem}.
 *
 * <p>Implements the OptiFine-specific item rendering hooks: custom item colors,
 * custom item models, off-hand rendering, enchanted glint shaders integration,
 * emissive model rendering and custom durability colors.</p>
 *
 * <p>The Forge-provided changes (ItemModelMesherForge, renderLitItem/allowEmissiveItems,
 * LightUtil.renderQuadColor, handleItemState, handleCameraTransforms,
 * showDurabilityBar/getDurabilityForDisplay/getRGBDurabilityForDisplay) are already
 * present via the Cleanroom patches and thus skipped here.</p>
 */
@Mixin(RenderItem.class)
public abstract class MixinRenderItem {

    @Shadow
    private ItemModelMesher itemModelMesher;
    @Shadow
    private TextureManager textureManager;
    @Unique @Public
    @Nullable
    private ResourceLocation modelLocation;
    @Unique @Public
    private boolean renderItemGui;
    @Unique @Public
    private boolean renderModelEmissive;

    @Unique @Public
    private boolean renderModelHasEmissive;

    @Shadow

    protected abstract void renderModel(IBakedModel modelIn, ItemStack stack);



    @Shadow

    protected abstract void renderModel(IBakedModel modelIn, int color, ItemStack stack);



    @SuppressWarnings("AddedMixinMembersNamePattern")

    @Unique

    private void optiRefine$renderModel(IBakedModel modelIn, int color) {

        this.renderModel(modelIn, color, ItemStack.EMPTY);

    }

    // ===== renderQuads: custom colors =====

    @Redirect(method = "renderQuads", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/color/ItemColors;colorMultiplier(Lnet/minecraft/item/ItemStack;I)I"))
    private int optiRefine$colorMultiplier(net.minecraft.client.renderer.color.ItemColors instance, ItemStack stack, int tintIndex) {
        int color = instance.colorMultiplier(stack, tintIndex);
        if (Config.isCustomColors()) {
            color = CustomColors.getColorFromItemStack(stack, tintIndex, color);
        }
        return color;
    }

    // ===== getItemModelWithOverrides: custom item models =====

    /**
     * OptiFine: {@code CustomItems.getCustomItemModel} integration.
     */
    @WrapMethod(method = "getItemModelWithOverrides")
    private IBakedModel optiRefine$getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entitylivingbaseIn, Operation<IBakedModel> original) {
        IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
        Item item = stack.getItem();
        if (Config.isCustomItems()) {
            if (item != null && item.hasCustomProperties()) {
                this.modelLocation = ibakedmodel.getOverrides().applyOverride(stack, worldIn, entitylivingbaseIn);
            }
            IBakedModel ibakedmodel1 = CustomItems.getCustomItemModel(stack, ibakedmodel, this.modelLocation, true);
            if (ibakedmodel1 != ibakedmodel) {
                return ibakedmodel1;
            }
        }
        return ibakedmodel.getOverrides().handleItemState(ibakedmodel, stack, worldIn, entitylivingbaseIn);
    }

    // ===== renderItemModel: off-hand + emissive =====

    @Inject(method = "renderItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift = At.Shift.BEFORE))
    private void optiRefine$setRenderOffHand(ItemStack stack, IBakedModel bakedModel, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        CustomItems.setRenderOffHand(leftHanded);
    }

    @Inject(method = "renderItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift = At.Shift.AFTER))
    private void optiRefine$clearRenderOffHand(ItemStack stack, IBakedModel bakedModel, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        CustomItems.setRenderOffHand(false);
    }

    @Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER))

    private void optiRefine$renderEmissive(ItemStack stack, IBakedModel modelIn, CallbackInfo ci) {

        if (this.renderModelHasEmissive) {

            float f = OpenGlHelper.lastBrightnessX;

            float f1 = OpenGlHelper.lastBrightnessY;

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, f1);

            this.renderModelEmissive = true;

            this.renderModel(modelIn, stack);

            this.renderModelEmissive = false;

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);

        }

    }

    // ===== renderEffect: custom glint + shaders =====

    /**
     * OptiFine: custom item glint + shaders integration.
     */
    @WrapMethod(method = "renderEffect")
    private void optiRefine$renderEffect(IBakedModel model, Operation<Void> original) {
        if (!Config.isCustomItems() || CustomItems.isUseGlint()) {
            if (!Config.isShaders() || !Shaders.isShadowPass) {
                GlStateManager.depthMask(false);
                GlStateManager.depthFunc(514);
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
                this.textureManager.bindTexture(RenderItem.RES_ITEM_GLINT);
                if (Config.isShaders() && !this.renderItemGui) {
                    ShadersRender.renderEnchantedGlintBegin();
                }
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.scale(8.0F, 8.0F, 8.0F);
                float f = (float) (net.minecraft.client.Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
                GlStateManager.translate(f, 0.0F, 0.0F);
                GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
                this.optiRefine$renderModel(model, -8372020);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(8.0F, 8.0F, 8.0F);
                float f1 = (float) (net.minecraft.client.Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
                GlStateManager.translate(-f1, 0.0F, 0.0F);
                GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
                this.optiRefine$renderModel(model, -8372020);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.enableLighting();
                GlStateManager.depthFunc(515);
                GlStateManager.depthMask(true);
                this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                if (Config.isShaders() && !this.renderItemGui) {
                    ShadersRender.renderEnchantedGlintEnd();
                }
            }
        }
    }

    // ===== renderItemOverlayIntoGUI: custom durability color =====

    @Redirect(method = "renderItemOverlayIntoGUI", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;hsvToRGB(FFF)I"))
    private int optiRefine$durabilityColor(float h, float s, float v) {
        int color = net.minecraft.util.math.MathHelper.hsvToRGB(h, s, v);
        if (Config.isCustomColors()) {
            color = CustomColors.getDurabilityColor(h, color);
        }
        return color;
    }
}
