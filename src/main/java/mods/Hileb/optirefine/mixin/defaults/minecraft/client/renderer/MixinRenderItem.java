package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockRenderLayer;
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
    private void optiRefine$storeModelManager(TextureManager textureManager, ModelManager modelManager, ItemColors itemColors, CallbackInfo ci) {
        this.modelManager = modelManager;
    }

    @Shadow
    protected abstract void renderModel(IBakedModel modelIn, ItemStack stack);


    @WrapMethod(method = "renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;ILnet/minecraft/item/ItemStack;)V")
    private void optiRefine$renderModelSkipLit(IBakedModel model, int color, ItemStack stack, Operation<Void> original) {
        boolean multiTexture = Config.isMultiTexture() && optiRefine$isAtlasTextureBound(Config.getMinecraft().getTextureMapBlocks());
        BufferBuilder buffer = multiTexture ? net.minecraft.client.renderer.Tessellator.getInstance().getBuffer() : null;
        boolean allowEmissiveItems = net.minecraftforge.common.ForgeModContainer.allowEmissiveItems;
        if (multiTexture) {
            optiRefine$setBufferBlockLayer(buffer, BlockRenderLayer.SOLID);
        }
        if (Config.isShaders() || multiTexture) {
            net.minecraftforge.common.ForgeModContainer.allowEmissiveItems = false;
        }
        try {
            original.call(model, color, stack);
        } finally {
            net.minecraftforge.common.ForgeModContainer.allowEmissiveItems = allowEmissiveItems;
            if (multiTexture) {
                optiRefine$setBufferBlockLayer(buffer, null);
                optiRefine$bindGlStateCurrentTexture();
            }
        }
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap isTextureBound ()Z")
    private static native boolean optiRefine$isAtlasTextureBound(TextureMap textureMap);

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder setBlockLayer (Lnet.minecraft.util.BlockRenderLayer;)V")
    private static native void optiRefine$setBufferBlockLayer(BufferBuilder buffer, BlockRenderLayer layer);

    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.minecraft.client.renderer.GlStateManager bindCurrentTexture ()V")
    private static native void optiRefine$bindGlStateCurrentTexture();


    // ===== renderQuads: custom colors =====

    @Redirect(method = "renderQuads", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/color/ItemColors;colorMultiplier(Lnet/minecraft/item/ItemStack;I)I"))
    private int optiRefine$colorMultiplier(net.minecraft.client.renderer.color.ItemColors itemColors, ItemStack stack, int tintIndex) {
// [AUDIT-OK] target renderQuads (SRG func_191970_a) ItemColors.colorMultiplier matches baseline; CustomColors hook matches OF
        int color = itemColors.colorMultiplier(stack, tintIndex);
        if (Config.isCustomColors()) {
            color = CustomColors.getColorFromItemStack(stack, tintIndex, color);
        }
        return color;
    }


    // Keep Forge's quad pipeline for custom formats and handle emissive textures on every render path.
    @WrapOperation(method = "renderQuads", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/model/pipeline/LightUtil;renderQuadColor(Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/client/renderer/block/model/BakedQuad;I)V", remap = false))
    private void optiRefine$renderQuad(BufferBuilder renderer, BakedQuad quad, int color, Operation<Void> original) {
        BakedQuad emissive = optiRefine$getEmissiveQuad(quad);
        if (this.renderModelEmissive) {
            if (emissive == null) {
                return;
            }
            quad = emissive;
        } else if (emissive != null) {
            this.renderModelHasEmissive = true;
        }

        boolean multiTexture = optiRefine$isBufferMultiTexture(renderer);
        if (Config.isShaders() || multiTexture) {
            int[] vertexData = multiTexture ? optiRefine$getBakedQuadVertexDataSingle(quad) : quad.getVertexData();
            if (vertexData.length == renderer.getVertexFormat().getIntegerSize() * 4
                    && (quad.getFormat().equals(renderer.getVertexFormat())
                    || vertexData.length == 56 && quad.getFormat().getColorOffset() == 12
                    && quad.getFormat().getUvOffsetById(0) == 16 && quad.getFormat().getNormalOffset() == 24)) {
                renderer.addVertexData(vertexData);
                optiRefine$putBufferSprite(renderer, quad.getSprite());
                net.minecraftforge.client.ForgeHooksClient.putQuadColor(renderer, quad, color);
                EnumFacing face = quad.getFace();
                if (face != null) {
                    Vec3i vec = face.getDirectionVec();
                    renderer.putNormal((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
                }
                return;
            }
        }
        original.call(renderer, quad, color);
        optiRefine$putBufferSprite(renderer, quad.getSprite());
    }

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.BufferBuilder isMultiTexture ()Z")
// [AUDIT-OK] OF member BufferBuilder.isMultiTexture (MixinBufferBuilder @Unique public)
    private static native boolean optiRefine$isBufferMultiTexture(BufferBuilder builder);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder putSprite (Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V")
// [AUDIT-OK] OF member BufferBuilder.putSprite (MixinBufferBuilder @Unique public)
    private static native void optiRefine$putBufferSprite(BufferBuilder builder, net.minecraft.client.renderer.texture.TextureAtlasSprite sprite);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.block.model.BakedQuad getVertexDataSingle ()[I")
// [AUDIT-OK] OF member BakedQuad.getVertexDataSingle (MixinBakedQuad @Public)
    private static native int[] optiRefine$getBakedQuadVertexDataSingle(BakedQuad quad);

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/block/model/BakedQuad getQuadEmissive ()Lnet/minecraft/client/renderer/block/model/BakedQuad;")
// [AUDIT-OK] OF member BakedQuad.getQuadEmissive (MixinBakedQuad @Public)
    private static native BakedQuad optiRefine$getEmissiveQuad(BakedQuad quad);

    // ===== getItemModelWithOverrides: custom item models =====

    /**
     * OptiFine: {@code CustomItems.getCustomItemModel} integration.
     */
    @WrapMethod(method = "getItemModelWithOverrides")
    private IBakedModel optiRefine$getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entitylivingbaseIn, Operation<IBakedModel> original) {
// [AUDIT-OK] target getItemModelWithOverrides (SRG func_184393_a) matches baseline; body == OF getItemModelWithOverrides
        this.modelLocation = null;
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

    @WrapOperation(method = "renderItemModelIntoGUI", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V"))
    private void optiRefine$renderItemGui(RenderItem itemRenderer, ItemStack stack, IBakedModel model, Operation<Void> original) {
        boolean renderItemGui = this.renderItemGui;
        this.renderItemGui = true;
        try {
            original.call(itemRenderer, stack, model);
        } finally {
            this.renderItemGui = renderItemGui;
        }
    }

    // ===== renderItem: custom item model re-apply + emissive reset (OF RenderItem:148-152) =====

    @WrapOperation(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V"))
    private void optiRefine$renderModelCustomItems(RenderItem itemRenderer, IBakedModel modelIn, ItemStack stack, Operation<Void> original, @Local(argsOnly = true) LocalRef<IBakedModel> model) {
        if (Config.isCustomItems()) {
            modelIn = CustomItems.getCustomItemModel(stack, modelIn, this.modelLocation, false);
        }
        this.modelLocation = null;
        this.renderModelHasEmissive = false;
        model.set(modelIn);
        original.call(itemRenderer, modelIn, stack);
    }

    // ===== renderItemModel(4-arg) =====
    // [AUDIT-REVERTED 2026-08-09] applyTransformSide no-op redirect removed: double-transform theory
    // disproven (handheld still distorted with it disabled). cleanroom's applyTransformSide +
    // handleCameraTransforms stays as-is; root cause under investigation (state leak / unclosed GL state).
    // [RESTORED 2026-08-09] setRenderOffHand restored per three-way runtime audit (mixed vs OF):
    // OF RenderItem:338/340 sets CustomItems.setRenderOffHand(leftHanded) around renderItem and resets
    // to false after — offhand-specific custom item model variants (hand==1/2 filtering) otherwise dead.

    @WrapOperation(method = "renderItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V"))
    private void optiRefine$renderOffHand(RenderItem itemRenderer, ItemStack stack, IBakedModel model, Operation<Void> original,
                                        @Local(argsOnly = true) boolean leftHanded) {
        CustomItems.setRenderOffHand(leftHanded);
        try {
            original.call(itemRenderer, stack, model);
        } finally {
            CustomItems.setRenderOffHand(false);
        }
    }

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER))
    private void optiRefine$renderEmissive(ItemStack stack, IBakedModel modelIn, CallbackInfo ci) {
        if (this.renderModelHasEmissive) {
            float lightMapX = OpenGlHelper.lastBrightnessX;
            float lightMapY = OpenGlHelper.lastBrightnessY;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, lightMapY);
            this.renderModelEmissive = true;
            try {
                this.renderModel(modelIn, stack);
            } finally {
                this.renderModelEmissive = false;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapX, lightMapY);
            }
        }
    }

    // ===== renderItem: custom enchantment glint (OF RenderItem:165) =====
    // [RESTORED 2026-08-09] per three-way runtime audit: OF gates renderEffect on
    // `hasEffect() && (!Config.isCustomItems() || !CustomItems.renderCustomEffect(this, stack, model))`;
    // CustomItemProperties "effect" textures never rendered before (renderCustomEffect never invoked).

    @ModifyExpressionValue(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasEffect()Z"))
    private boolean optiRefine$customEffectGlint(boolean original, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) IBakedModel model) {
        if (original && Config.isCustomItems()) {
            return !CustomItems.renderCustomEffect((RenderItem) (Object) this, stack, model);
        }
        return original;
    }

    // ===== renderEffect: custom glint + shaders =====

    /**
     * OptiFine: custom item glint + shaders integration.
     */
    @WrapMethod(method = "renderEffect")
    private void optiRefine$renderEffect(IBakedModel model, Operation<Void> original) {
        if (Config.isCustomItems() && !CustomItems.isUseGlint()
                || Config.isShaders() && Shaders.isShadowPass) {
            return;
        }
        boolean shaders = Config.isShaders() && !this.renderItemGui;
        if (shaders) {
            ShadersRender.renderEnchantedGlintBegin();
        }
        try {
            original.call(model);
        } finally {
            if (shaders) {
                ShadersRender.renderEnchantedGlintEnd();
            }
        }
    }

    // Apply the resource-pack palette after Forge supplies the item's base color and durability.
    @ModifyExpressionValue(method = "renderItemOverlayIntoGUI(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getRGBDurabilityForDisplay(Lnet/minecraft/item/ItemStack;)I", remap = false))
    private int optiRefine$durabilityColor(int color, @Local double durability) {
        return Config.isCustomColors() ? CustomColors.getDurabilityColor((float) Math.max(0.0D, 1.0D - durability), color) : color;
    }
}
