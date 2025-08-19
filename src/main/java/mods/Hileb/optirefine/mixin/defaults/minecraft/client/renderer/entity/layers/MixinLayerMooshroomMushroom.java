package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.entity.RenderMooshroom;
import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerMooshroomMushroom.class)
public abstract class MixinLayerMooshroomMushroom {
    @Shadow @Final
    private RenderMooshroom mooshroomRenderer;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private ModelRenderer modelRendererMushroom;
    @Unique
    private static final ResourceLocation LOCATION_MUSHROOM_RED = new ResourceLocation("textures/entity/cow/mushroom_red.png");
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private static boolean hasTextureMushroom = false;


    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    private static void update() {
        hasTextureMushroom = Config.hasResource(LOCATION_MUSHROOM_RED);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(RenderMooshroom p_i46114_1, CallbackInfo ci){
        this.modelRendererMushroom = new ModelRenderer(this.mooshroomRenderer.getMainModel());
        this.modelRendererMushroom.setTextureSize(16, 16);
        this.modelRendererMushroom.rotationPointX = -6.0F;
        this.modelRendererMushroom.rotationPointZ = -8.0F;
        this.modelRendererMushroom.rotateAngleY = (float) Math.PI / 4.0F;
        int[][] faceUvs = new int[][]{null, null, {16, 16, 0, 0}, {16, 16, 0, 0}, null, null};
        ModelRenderer_addBox(this.modelRendererMushroom, faceUvs, 0.0F, 0.0F, 10.0F, 20.0F, 16.0F, 0.0F, 0.0F);
        int[][] faceUvs2 = new int[][]{null, null, null, null, {16, 16, 0, 0}, {16, 16, 0, 0}};
        ModelRenderer_addBox(this.modelRendererMushroom, faceUvs2, 10.0F, 0.0F, 0.0F, 0.0F, 16.0F, 20.0F, 0.0F);
    }

    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.model.ModelRenderer addBox ([[IFFFFFFF)V")
    private static native void ModelRenderer_addBox(ModelRenderer modelRendererMushroom, int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta);

    @Redirect(method = "doRenderLayer(Lnet/minecraft/entity/passive/EntityMooshroom;FFFFFFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/texture/TextureMap;LOCATION_BLOCKS_TEXTURE:Lnet/minecraft/util/ResourceLocation;"))
    public ResourceLocation texture(){
        if (hasTextureMushroom) {
            return (LOCATION_MUSHROOM_RED);
        } else {
            return (TextureMap.LOCATION_BLOCKS_TEXTURE);
        }
    }

    @WrapOperation(method = "doRenderLayer(Lnet/minecraft/entity/passive/EntityMooshroom;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockRendererDispatcher;renderBlockBrightness(Lnet/minecraft/block/state/IBlockState;F)V"))
    public void renderBlockBrightness(BlockRendererDispatcher instance, IBlockState blockState, float state, Operation<Void> original){
        if (hasTextureMushroom) {
            this.modelRendererMushroom.render(0.0625F);
        } else {
            original.call(instance, blockState, state);
        }
    }
}
/*
+++ net/minecraft/client/renderer/entity/layers/LayerMooshroomMushroom.java	Tue Aug 19 14:59:58 2025
@@ -1,51 +1,90 @@
 package net.minecraft.client.renderer.entity.layers;

 import net.minecraft.client.Minecraft;
+import net.minecraft.client.model.ModelRenderer;
 import net.minecraft.client.renderer.BlockRendererDispatcher;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.entity.RenderMooshroom;
 import net.minecraft.client.renderer.texture.TextureMap;
 import net.minecraft.entity.passive.EntityMooshroom;
 import net.minecraft.init.Blocks;
+import net.minecraft.util.ResourceLocation;
+import net.minecraft.util.math.MathHelper;

 public class LayerMooshroomMushroom implements LayerRenderer<EntityMooshroom> {
    private final RenderMooshroom mooshroomRenderer;
+   private ModelRenderer modelRendererMushroom;
+   private static final ResourceLocation LOCATION_MUSHROOM_RED = new ResourceLocation("textures/entity/cow/mushroom_red.png");
+   private static boolean hasTextureMushroom = false;
+
+   public static void update() {
+      hasTextureMushroom = Config.hasResource(LOCATION_MUSHROOM_RED);
+   }

    public LayerMooshroomMushroom(RenderMooshroom var1) {
       this.mooshroomRenderer = var1;
+      this.modelRendererMushroom = new ModelRenderer(this.mooshroomRenderer.f);
+      this.modelRendererMushroom.setTextureSize(16, 16);
+      this.modelRendererMushroom.rotationPointX = -6.0F;
+      this.modelRendererMushroom.rotationPointZ = -8.0F;
+      this.modelRendererMushroom.rotateAngleY = MathHelper.PI / 4.0F;
+      int[][] var2 = new int[][]{null, null, {16, 16, 0, 0}, {16, 16, 0, 0}, null, null};
+      this.modelRendererMushroom.addBox(var2, 0.0F, 0.0F, 10.0F, 20.0F, 16.0F, 0.0F, 0.0F);
+      int[][] var3 = new int[][]{null, null, null, null, {16, 16, 0, 0}, {16, 16, 0, 0}};
+      this.modelRendererMushroom.addBox(var3, 10.0F, 0.0F, 0.0F, 0.0F, 16.0F, 20.0F, 0.0F);
    }

    public void doRenderLayer(EntityMooshroom var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
-      if (!var1.isChild() && !var1.isInvisible()) {
+      if (!var1.l_() && !var1.aX()) {
          BlockRendererDispatcher var9 = Minecraft.getMinecraft().getBlockRendererDispatcher();
-         this.mooshroomRenderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
+         if (hasTextureMushroom) {
+            this.mooshroomRenderer.a(LOCATION_MUSHROOM_RED);
+         } else {
+            this.mooshroomRenderer.a(TextureMap.LOCATION_BLOCKS_TEXTURE);
+         }
+
          GlStateManager.enableCull();
          GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
          GlStateManager.pushMatrix();
          GlStateManager.scale(1.0F, -1.0F, 1.0F);
          GlStateManager.translate(0.2F, 0.35F, 0.5F);
          GlStateManager.rotate(42.0F, 0.0F, 1.0F, 0.0F);
          GlStateManager.pushMatrix();
          GlStateManager.translate(-0.5F, -0.5F, 0.5F);
-         var9.renderBlockBrightness(Blocks.RED_MUSHROOM.getDefaultState(), 1.0F);
+         if (hasTextureMushroom) {
+            this.modelRendererMushroom.render(0.0625F);
+         } else {
+            var9.renderBlockBrightness(Blocks.RED_MUSHROOM.getDefaultState(), 1.0F);
+         }
+
          GlStateManager.popMatrix();
          GlStateManager.pushMatrix();
          GlStateManager.translate(0.1F, 0.0F, -0.6F);
          GlStateManager.rotate(42.0F, 0.0F, 1.0F, 0.0F);
          GlStateManager.translate(-0.5F, -0.5F, 0.5F);
-         var9.renderBlockBrightness(Blocks.RED_MUSHROOM.getDefaultState(), 1.0F);
+         if (hasTextureMushroom) {
+            this.modelRendererMushroom.render(0.0625F);
+         } else {
+            var9.renderBlockBrightness(Blocks.RED_MUSHROOM.getDefaultState(), 1.0F);
+         }
+
          GlStateManager.popMatrix();
          GlStateManager.popMatrix();
          GlStateManager.pushMatrix();
-         this.mooshroomRenderer.getMainModel().head.postRender(0.0625F);
+         this.mooshroomRenderer.getMainModel().modelTextureMap.postRender(0.0625F);
          GlStateManager.scale(1.0F, -1.0F, 1.0F);
          GlStateManager.translate(0.0F, 0.7F, -0.2F);
          GlStateManager.rotate(12.0F, 0.0F, 1.0F, 0.0F);
          GlStateManager.translate(-0.5F, -0.5F, 0.5F);
-         var9.renderBlockBrightness(Blocks.RED_MUSHROOM.getDefaultState(), 1.0F);
+         if (hasTextureMushroom) {
+            this.modelRendererMushroom.render(0.0625F);
+         } else {
+            var9.renderBlockBrightness(Blocks.RED_MUSHROOM.getDefaultState(), 1.0F);
+         }
+
          GlStateManager.popMatrix();
          GlStateManager.cullFace(GlStateManager.CullFace.BACK);
          GlStateManager.disableCull();
       }
    }

 */
