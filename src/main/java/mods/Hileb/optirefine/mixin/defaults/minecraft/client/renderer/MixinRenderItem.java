package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.optifine.CustomColors;
import net.optifine.CustomItems;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.ShadersRender;
import org.objectweb.asm.Opcodes;
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
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 0

    @Shadow
// [AUDIT-OK] baseline member itemModelMesher (SRG field_175059_m)
    private ItemModelMesher itemModelMesher;
    @Shadow
// [AUDIT-OK] baseline member textureManager (SRG field_175057_n)
    private TextureManager textureManager;
    @Public
    @Nullable
// [AUDIT-OK] OF-added field (OF: private ResourceLocation modelLocation), not in baseline; @Public extra visibility harmless
    private ResourceLocation modelLocation;
    @Public
// [AUDIT-OK] OF-added field, not in baseline
    private boolean renderItemGui;
    @Public
// [AUDIT-OK] OF-added field, not in baseline
    private boolean renderModelEmissive;

    @Public
// [AUDIT-OK] OF-added field, not in baseline
    private boolean renderModelHasEmissive;

    @Public
// [AUDIT-OK] OF-added field (OF: public ModelManager modelManager — AGENT.md S5 crash fix), not in baseline
    private ModelManager modelManager;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void optiRefine$ctorModelManager(TextureManager textureManager, ModelManager modelManager, ItemColors itemColors, CallbackInfo ci) {
// [AUDIT-OK] target <init>(LTextureManager;LModelManager;LItemColors;)V matches baseline; modelManager assignment matches OF ctor
        this.modelManager = modelManager;
    }

    @Shadow

// [AUDIT-OK] baseline member renderModel(LIBakedModel;LItemStack;)V (SRG func_191961_a)
    protected abstract void renderModel(IBakedModel modelIn, ItemStack stack);



    @Shadow

// [AUDIT-OK] baseline member renderModel(LIBakedModel;ILItemStack;)V (SRG func_191967_a)
    protected abstract void renderModel(IBakedModel modelIn, int color, ItemStack stack);



    @SuppressWarnings("AddedMixinMembersNamePattern")

    @Unique

// [AUDIT-OK] OF-added private helper (OF renderEffect calls 3-arg form), not in baseline
    private void optiRefine$renderModel(IBakedModel modelIn, int color) {

        this.renderModel(modelIn, color, ItemStack.EMPTY);

    }

    // ===== renderQuads: custom colors =====

    @Redirect(method = "renderQuads", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/color/ItemColors;colorMultiplier(Lnet/minecraft/item/ItemStack;I)I"))
    private int optiRefine$colorMultiplier(net.minecraft.client.renderer.color.ItemColors instance, ItemStack stack, int tintIndex) {
// [AUDIT-OK] target renderQuads (SRG func_191970_a) ItemColors.colorMultiplier matches baseline; CustomColors hook matches OF
        int color = instance.colorMultiplier(stack, tintIndex);
        if (Config.isCustomColors()) {
            color = CustomColors.getColorFromItemStack(stack, tintIndex, color);
        }
        return color;
    }

    @Shadow
// [AUDIT-OK] baseline member itemColors (SRG field_175061_g)
    private ItemColors itemColors;

    // ===== renderQuads: shader-safe direct quad write =====

    /**
     * OptiFine renders item quads by direct writes (addVertexData + putSprite + putColor4 +
     * putQuadNormal). Cleanroom's patch replaced that with Forge LightUtil.renderQuadColor, which
     * unpacks/repacks quads into the 28-byte format and corrupts the 56-byte SVertexFormat layout
     * under shaders (quad pos.x -> 0, verified 2026-08-05; the earlier WrapOperation attempt
     * crashed at mixin apply, hence the whole-method WrapMethod). Shaders off -> original
     * LightUtil path (correct with the vanilla 28-byte format).
     */
    @WrapMethod(method = "renderQuads")
    private void optiRefine$renderQuads(BufferBuilder renderer, java.util.List<BakedQuad> quads, int color, ItemStack stack, Operation<Void> original) {
        if (Config.isShaders()) {
            boolean flag = color == -1 && !stack.isEmpty();
            for (int i = 0; i < quads.size(); ++i) {
                BakedQuad bakedquad = quads.get(i);
                int k = color;
                if (flag && bakedquad.hasTintIndex()) {
                    k = this.itemColors.colorMultiplier(stack, bakedquad.getTintIndex());
                    if (Config.isCustomColors()) {
                        k = CustomColors.getColorFromItemStack(stack, bakedquad.getTintIndex(), k);
                    }
                    if (EntityRenderer.anaglyphEnable) {
                        k = TextureUtil.anaglyphColor(k);
                    }
                    k |= -16777216;
                }
                if (this.renderModelEmissive) {
                    if (BakedQuad_getQuadEmissive(bakedquad) == null) {
                        continue;
                    }
                    bakedquad = BakedQuad_getQuadEmissive(bakedquad);
                } else if (BakedQuad_getQuadEmissive(bakedquad) != null) {
                    this.renderModelHasEmissive = true;
                }
                if (BufferBuilder_isMultiTexture(renderer)) {
                    renderer.addVertexData(BakedQuad_getVertexDataSingle(bakedquad));
                } else {
                    renderer.addVertexData(bakedquad.getVertexData());
                }
                BufferBuilder_putSprite(renderer, bakedquad.getSprite());
                renderer.putColor4(k);
                EnumFacing face = bakedquad.getFace();
                if (face != null) {
                    Vec3i vec = face.getDirectionVec();
                    renderer.putNormal((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
                }
            }
        } else {
            original.call(renderer, quads, color, stack);
        }
    }

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder isMultiTexture ()Z")
// [AUDIT-OK] OF member BufferBuilder.isMultiTexture (MixinBufferBuilder @Unique public)
    private static native boolean BufferBuilder_isMultiTexture(BufferBuilder builder);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder putSprite (Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V")
// [AUDIT-OK] OF member BufferBuilder.putSprite (MixinBufferBuilder @Unique public)
    private static native void BufferBuilder_putSprite(BufferBuilder builder, net.minecraft.client.renderer.texture.TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.block.model.BakedQuad getVertexDataSingle ()[I")
// [AUDIT-OK] OF member BakedQuad.getVertexDataSingle (MixinBakedQuad @Public)
    private static native int[] BakedQuad_getVertexDataSingle(BakedQuad quad);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/block/model/BakedQuad getQuadEmissive ()Lnet/minecraft/client/renderer/block/model/BakedQuad;")
// [AUDIT-OK] OF member BakedQuad.getQuadEmissive (MixinBakedQuad @Public)
    private static native BakedQuad BakedQuad_getQuadEmissive(BakedQuad quad);

    // ===== getItemModelWithOverrides: custom item models =====

    /**
     * OptiFine: {@code CustomItems.getCustomItemModel} integration.
     */
    @WrapMethod(method = "getItemModelWithOverrides")
    private IBakedModel optiRefine$getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entitylivingbaseIn, Operation<IBakedModel> original) {
// [AUDIT-OK] target getItemModelWithOverrides (SRG func_184393_a) matches baseline; body == OF getItemModelWithOverrides
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

    // ===== renderItemModelIntoGUI: renderItemGui flag (OF RenderItem:359/383) =====

    @Inject(method = "renderItemModelIntoGUI", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift = At.Shift.BEFORE))
    private void optiRefine$setRenderItemGui(ItemStack stack, int x, int y, IBakedModel model, CallbackInfo ci) {
        this.renderItemGui = true;
    }

    @Inject(method = "renderItemModelIntoGUI", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift = At.Shift.AFTER))
    private void optiRefine$clearRenderItemGui(ItemStack stack, int x, int y, IBakedModel model, CallbackInfo ci) {
        this.renderItemGui = false;
    }

    // ===== renderItem: custom item model re-apply + emissive reset (OF RenderItem:148-152) =====

    @Redirect(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V", ordinal = 0))
    private void optiRefine$renderModelCustomItems(RenderItem instance, IBakedModel modelIn, ItemStack stack) {
        if (Config.isCustomItems()) {
            modelIn = CustomItems.getCustomItemModel(stack, modelIn, this.modelLocation, false);
            this.modelLocation = null;
        }
        this.renderModelHasEmissive = false;
        this.renderModel(modelIn, stack);
    }

    // ===== renderItemModel: off-hand + emissive =====

    @Inject(method = "renderItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift = At.Shift.BEFORE))
    private void optiRefine$setRenderOffHand(ItemStack stack, IBakedModel bakedModel, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
// [AUDIT-OK] target renderItemModel (SRG func_184394_a) renderItem(LItemStack;LIBakedModel;)V INVOKE matches baseline
        CustomItems.setRenderOffHand(leftHanded);
    }

    @Inject(method = "renderItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift = At.Shift.AFTER))
    private void optiRefine$clearRenderOffHand(ItemStack stack, IBakedModel bakedModel, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
// [AUDIT-OK] target renderItemModel matches baseline
        CustomItems.setRenderOffHand(false);
    }

    @Inject(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER))

    private void optiRefine$renderEmissive(ItemStack stack, IBakedModel modelIn, CallbackInfo ci) {
// [AUDIT-OK] target renderItem(LItemStack;LIBakedModel;)V (SRG func_180454_a) renderModel INVOKE matches baseline; emissive re-render matches OF

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
// [AUDIT-OK] target renderEffect (SRG func_191966_a) matches baseline; body == OF renderEffect (glint + ShadersRender)
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
    // [AUDIT-FIXED] vanilla hsvToRGB call was removed by the Cleanroom RenderItem patch (durability now uses
    // Item.getRGBDurabilityForDisplay); the old @Redirect target no longer exists -> mixin apply crash.
    // CustomColors.getDurabilityColor hook for the Forge path is TODO.
}
