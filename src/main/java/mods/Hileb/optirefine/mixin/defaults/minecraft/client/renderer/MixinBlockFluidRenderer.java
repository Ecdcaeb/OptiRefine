package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.optifine.CustomColors;
import net.optifine.render.RenderEnv;
import net.optifine.shaders.SVertexBuilder;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(BlockFluidRenderer.class)
public abstract class MixinBlockFluidRenderer {

    @Shadow @Final
    private TextureAtlasSprite[] atlasSpritesLava;

    @Shadow @Final
    private TextureAtlasSprite[] atlasSpritesWater;

    @Shadow
    private TextureAtlasSprite atlasSpriteWaterOverlay;

    @Inject(method = "renderFluid", at = @At("HEAD"))
    public void inject_renderFluid_getRenderEnv(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder worldRendererIn, CallbackInfoReturnable<Boolean> cir, @Share("renderEnv") LocalRef<RenderEnv> env) {
        env.set(BufferBuilder_getRenderEnv(worldRendererIn, blockStateIn, blockPosIn));
    }

    
    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder getRenderEnv (Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;)Lnet/optifine/render/RenderEnv;", deobf = true)
    private static RenderEnv BufferBuilder_getRenderEnv(BufferBuilder builder, IBlockState blockStateIn, BlockPos blockPosIn){
        throw new AbstractMethodError();
    }

    @Redirect(method = "renderFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/color/BlockColors;colorMultiplier(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;I)I"))
    public int getCustomColor(BlockColors instance, IBlockState blockStateIn, IBlockAccess blockAccess, BlockPos blockPosIn, int val, @Share("renderEnv") LocalRef<RenderEnv> env){
        return CustomColors.getFluidColor(blockAccess, blockStateIn, blockPosIn, env.get());
    }

    @Inject(method = "renderFluid", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/BlockFluidRenderer;atlasSpriteWaterOverlay:Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
    public void onSet(IBlockAccess p_178270_1_, IBlockState p_178270_2_, BlockPos p_178270_3_, BufferBuilder p_178270_4_, CallbackInfoReturnable<Boolean> cir){
        BufferBuilder_setSprite(p_178270_4_, this.atlasSpriteWaterOverlay);
    }

    
    @SuppressWarnings("MissingUnique")
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net/minecraft/client/renderer/BufferBuilder setSprite (Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V")
    private static void BufferBuilder_setSprite(BufferBuilder builder, TextureAtlasSprite sprite){
        throw new AbstractMethodError();
    }


    @Inject(method = "renderFluid", at = @At("HEAD"))
    public void init_renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder bufferBuilderIn, CallbackInfoReturnable<Boolean> cir,
                                 @Share(namespace = "optirefine", value = "renderEnv") LocalRef<RenderEnv> renderEnv){
        renderEnv.set(BufferBuilder_getRenderEnv(bufferBuilderIn, blockStateIn, blockPosIn));
    }

    @Definition(id = "textureatlassprite", local = @Local(type = TextureAtlasSprite.class))
    @Expression("textureatlassprite = @(?)")
    @ModifyExpressionValue(method = "renderFluid", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    public TextureAtlasSprite hookAtSetTextureAtlasSprite_renderFluid(TextureAtlasSprite original, @Local(argsOnly = true) BufferBuilder builder) {
        BufferBuilder_setSprite(builder, original);
        return original;
    }

    @Definition(id = " atextureatlassprite", local = @Local(type = TextureAtlasSprite[].class))
    @Expression("@(atextureatlassprite[0]).?")
    @ModifyExpressionValue(method = "renderFluid", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    public TextureAtlasSprite hookAtUseZeroTextureAtlasSprite_renderFluid(TextureAtlasSprite or,
                                                            @Local(argsOnly = true) BufferBuilder builder,
                                                            @Share(namespace = "optirefine", value = "fbr") LocalRef<Float> fbr) {
        BufferBuilder_setSprite(builder, or);
        fbr.set(0.5F);
        return or;
    }

    @Redirect(method = "renderFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;color(FFFF)Lnet/minecraft/client/renderer/BufferBuilder;"))
    public BufferBuilder remapColor_renderFluid(BufferBuilder instance, float red, float green, float blue, float alpha,  @Share(namespace = "optirefine", value = "fbr") LocalRef<Float> fbr){
        if (fbr.get() == null) {
            return instance.color(red, green, blue, alpha);
        } else {
            float fbrf = fbr.get();
            return instance.color(red * fbrf, green * fbrf, blue * fbrf, alpha);
        }
    }


    @WrapMethod(method = "renderFluid")
    public boolean wrap_renderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder worldRendererIn, Operation<Boolean> original){
        boolean value;
        try {
            if (Config.isShaders()) {
                SVertexBuilder.pushEntity(blockStateIn, blockPosIn, blockAccess, worldRendererIn);
            }
            value = original.call(blockAccess, blockStateIn, blockPosIn, worldRendererIn);
        } finally {
            if (Config.isShaders()) {
                SVertexBuilder.popEntity(worldRendererIn);
            }
        }
        return value;
    }

}
/*
--- net/minecraft/client/renderer/BlockFluidRenderer.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/renderer/BlockFluidRenderer.java	Tue Aug 19 14:59:58 2025
@@ -1,21 +1,27 @@
 package net.minecraft.client.renderer;

 import net.minecraft.block.Block;
 import net.minecraft.block.BlockLiquid;
+import net.minecraft.block.BlockSlab;
+import net.minecraft.block.BlockSlab.EnumBlockHalf;
 import net.minecraft.block.material.Material;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.client.Minecraft;
+import net.minecraft.client.renderer.block.model.FaceBakery;
 import net.minecraft.client.renderer.color.BlockColors;
 import net.minecraft.client.renderer.texture.TextureAtlasSprite;
 import net.minecraft.client.renderer.texture.TextureMap;
 import net.minecraft.init.Blocks;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.world.IBlockAccess;
+import net.optifine.CustomColors;
+import net.optifine.render.RenderEnv;
+import net.optifine.shaders.SVertexBuilder;

 public class BlockFluidRenderer {
    private final BlockColors blockColors;
    private final TextureAtlasSprite[] atlasSpritesLava = new TextureAtlasSprite[2];
    private final TextureAtlasSprite[] atlasSpritesWater = new TextureAtlasSprite[2];
    private TextureAtlasSprite atlasSpriteWaterOverlay;
@@ -32,234 +38,297 @@
       this.atlasSpritesWater[0] = var1.getAtlasSprite("minecraft:blocks/water_still");
       this.atlasSpritesWater[1] = var1.getAtlasSprite("minecraft:blocks/water_flow");
       this.atlasSpriteWaterOverlay = var1.getAtlasSprite("minecraft:blocks/water_overlay");
    }

    public boolean renderFluid(IBlockAccess var1, IBlockState var2, BlockPos var3, BufferBuilder var4) {
-      BlockLiquid var5 = (BlockLiquid)var2.getBlock();
-      boolean var6 = var2.getMaterial() == Material.LAVA;
-      TextureAtlasSprite[] var7 = var6 ? this.atlasSpritesLava : this.atlasSpritesWater;
-      int var8 = this.blockColors.colorMultiplier(var2, var1, var3, 0);
-      float var9 = (var8 >> 16 & 0xFF) / 255.0F;
-      float var10 = (var8 >> 8 & 0xFF) / 255.0F;
-      float var11 = (var8 & 0xFF) / 255.0F;
-      boolean var12 = var2.shouldSideBeRendered(var1, var3, EnumFacing.UP);
-      boolean var13 = var2.shouldSideBeRendered(var1, var3, EnumFacing.DOWN);
-      boolean[] var14 = new boolean[]{
-         var2.shouldSideBeRendered(var1, var3, EnumFacing.NORTH),
-         var2.shouldSideBeRendered(var1, var3, EnumFacing.SOUTH),
-         var2.shouldSideBeRendered(var1, var3, EnumFacing.WEST),
-         var2.shouldSideBeRendered(var1, var3, EnumFacing.EAST)
-      };
-      if (!var12 && !var13 && !var14[0] && !var14[1] && !var14[2] && !var14[3]) {
-         return false;
-      } else {
-         boolean var15 = false;
-         float var16 = 0.5F;
-         float var17 = 1.0F;
-         float var18 = 0.8F;
-         float var19 = 0.6F;
-         Material var20 = var2.getMaterial();
-         float var21 = this.getFluidHeight(var1, var3, var20);
-         float var22 = this.getFluidHeight(var1, var3.south(), var20);
-         float var23 = this.getFluidHeight(var1, var3.east().south(), var20);
-         float var24 = this.getFluidHeight(var1, var3.east(), var20);
-         double var25 = var3.getX();
-         double var27 = var3.getY();
-         double var29 = var3.getZ();
-         float var31 = 0.001F;
-         if (var12) {
-            var15 = true;
-            float var32 = BlockLiquid.getSlopeAngle(var1, var3, var20, var2);
-            TextureAtlasSprite var33 = var32 > -999.0F ? var7[1] : var7[0];
-            var21 -= 0.001F;
-            var22 -= 0.001F;
-            var23 -= 0.001F;
-            var24 -= 0.001F;
-            float var34;
-            float var35;
-            float var36;
-            float var37;
-            float var38;
-            float var39;
-            float var40;
-            float var41;
-            if (var32 < -999.0F) {
-               var34 = var33.getInterpolatedU(0.0);
-               var38 = var33.getInterpolatedV(0.0);
-               var35 = var34;
-               var39 = var33.getInterpolatedV(16.0);
-               var36 = var33.getInterpolatedU(16.0);
-               var40 = var39;
-               var37 = var36;
-               var41 = var38;
-            } else {
-               float var42 = MathHelper.sin(var32) * 0.25F;
-               float var43 = MathHelper.cos(var32) * 0.25F;
-               float var44 = 8.0F;
-               var34 = var33.getInterpolatedU(8.0F + (-var43 - var42) * 16.0F);
-               var38 = var33.getInterpolatedV(8.0F + (-var43 + var42) * 16.0F);
-               var35 = var33.getInterpolatedU(8.0F + (-var43 + var42) * 16.0F);
-               var39 = var33.getInterpolatedV(8.0F + (var43 + var42) * 16.0F);
-               var36 = var33.getInterpolatedU(8.0F + (var43 + var42) * 16.0F);
-               var40 = var33.getInterpolatedV(8.0F + (var43 - var42) * 16.0F);
-               var37 = var33.getInterpolatedU(8.0F + (var43 - var42) * 16.0F);
-               var41 = var33.getInterpolatedV(8.0F + (-var43 - var42) * 16.0F);
-            }
-
-            int var76 = var2.getPackedLightmapCoords(var1, var3);
-            int var77 = var76 >> 16 & 65535;
-            int var79 = var76 & 65535;
-            float var45 = 1.0F * var9;
-            float var46 = 1.0F * var10;
-            float var47 = 1.0F * var11;
-            var4.pos(var25 + 0.0, var27 + var21, var29 + 0.0).color(var45, var46, var47, 1.0F).tex(var34, var38).lightmap(var77, var79).endVertex();
-            var4.pos(var25 + 0.0, var27 + var22, var29 + 1.0).color(var45, var46, var47, 1.0F).tex(var35, var39).lightmap(var77, var79).endVertex();
-            var4.pos(var25 + 1.0, var27 + var23, var29 + 1.0).color(var45, var46, var47, 1.0F).tex(var36, var40).lightmap(var77, var79).endVertex();
-            var4.pos(var25 + 1.0, var27 + var24, var29 + 0.0).color(var45, var46, var47, 1.0F).tex(var37, var41).lightmap(var77, var79).endVertex();
-            if (var5.shouldRenderSides(var1, var3.up())) {
-               var4.pos(var25 + 0.0, var27 + var21, var29 + 0.0).color(var45, var46, var47, 1.0F).tex(var34, var38).lightmap(var77, var79).endVertex();
-               var4.pos(var25 + 1.0, var27 + var24, var29 + 0.0).color(var45, var46, var47, 1.0F).tex(var37, var41).lightmap(var77, var79).endVertex();
-               var4.pos(var25 + 1.0, var27 + var23, var29 + 1.0).color(var45, var46, var47, 1.0F).tex(var36, var40).lightmap(var77, var79).endVertex();
-               var4.pos(var25 + 0.0, var27 + var22, var29 + 1.0).color(var45, var46, var47, 1.0F).tex(var35, var39).lightmap(var77, var79).endVertex();
-            }
+      boolean var16;
+      try {
+         if (Config.isShaders()) {
+            SVertexBuilder.pushEntity(var2, var3, var1, var4);
          }

-         if (var13) {
-            float var59 = var7[0].getMinU();
-            float var61 = var7[0].getMaxU();
-            float var63 = var7[0].getMinV();
-            float var65 = var7[0].getMaxV();
-            int var67 = var2.getPackedLightmapCoords(var1, var3.down());
-            int var69 = var67 >> 16 & 65535;
-            int var72 = var67 & 65535;
-            var4.pos(var25, var27, var29 + 1.0).color(0.5F, 0.5F, 0.5F, 1.0F).tex(var59, var65).lightmap(var69, var72).endVertex();
-            var4.pos(var25, var27, var29).color(0.5F, 0.5F, 0.5F, 1.0F).tex(var59, var63).lightmap(var69, var72).endVertex();
-            var4.pos(var25 + 1.0, var27, var29).color(0.5F, 0.5F, 0.5F, 1.0F).tex(var61, var63).lightmap(var69, var72).endVertex();
-            var4.pos(var25 + 1.0, var27, var29 + 1.0).color(0.5F, 0.5F, 0.5F, 1.0F).tex(var61, var65).lightmap(var69, var72).endVertex();
-            var15 = true;
-         }
+         BlockLiquid var5 = (BlockLiquid)var2.getBlock();
+         boolean var6 = var2.a() == Material.LAVA;
+         TextureAtlasSprite[] var7 = var6 ? this.atlasSpritesLava : this.atlasSpritesWater;
+         RenderEnv var8 = var4.getRenderEnv(var2, var3);
+         int var9 = CustomColors.getFluidColor(var1, var2, var3, var8);
+         float var10 = (var9 >> 16 & 0xFF) / 255.0F;
+         float var11 = (var9 >> 8 & 0xFF) / 255.0F;
+         float var12 = (var9 & 0xFF) / 255.0F;
+         boolean var13 = var2.c(var1, var3, EnumFacing.UP);
+         boolean var14 = var2.c(var1, var3, EnumFacing.DOWN);
+         boolean[] var15 = var8.getBorderFlags();
+         var15[0] = var2.c(var1, var3, EnumFacing.NORTH);
+         var15[1] = var2.c(var1, var3, EnumFacing.SOUTH);
+         var15[2] = var2.c(var1, var3, EnumFacing.WEST);
+         var15[3] = var2.c(var1, var3, EnumFacing.EAST);
+         if (var13 || var14 || var15[0] || var15[1] || var15[2] || var15[3]) {
+            var16 = false;
+            float var17 = 0.5F;
+            float var18 = 1.0F;
+            float var19 = 0.8F;
+            float var20 = 0.6F;
+            Material var21 = var2.a();
+            float var22 = this.getFluidHeight(var1, var3, var21);
+            float var23 = this.getFluidHeight(var1, var3.south(), var21);
+            float var24 = this.getFluidHeight(var1, var3.east().south(), var21);
+            float var25 = this.getFluidHeight(var1, var3.east(), var21);
+            double var26 = var3.getX();
+            double var28 = var3.getY();
+            double var30 = var3.getZ();
+            float var32 = 0.001F;
+            if (var13) {
+               var16 = true;
+               float var33 = BlockLiquid.getSlopeAngle(var1, var3, var21, var2);
+               TextureAtlasSprite var34 = var33 > -999.0F ? var7[1] : var7[0];
+               var4.setSprite(var34);
+               var22 -= 0.001F;
+               var23 -= 0.001F;
+               var24 -= 0.001F;
+               var25 -= 0.001F;
+               float var35;
+               float var36;
+               float var37;
+               float var38;
+               float var39;
+               float var40;
+               float var41;
+               float var42;
+               if (var33 < -999.0F) {
+                  var35 = var34.getInterpolatedU(0.0);
+                  var39 = var34.getInterpolatedV(0.0);
+                  var36 = var35;
+                  var40 = var34.getInterpolatedV(16.0);
+                  var37 = var34.getInterpolatedU(16.0);
+                  var41 = var40;
+                  var38 = var37;
+                  var42 = var39;
+               } else {
+                  float var43 = MathHelper.sin(var33) * 0.25F;
+                  float var44 = MathHelper.cos(var33) * 0.25F;
+                  float var45 = 8.0F;
+                  var35 = var34.getInterpolatedU(8.0F + (-var44 - var43) * 16.0F);
+                  var39 = var34.getInterpolatedV(8.0F + (-var44 + var43) * 16.0F);
+                  var36 = var34.getInterpolatedU(8.0F + (-var44 + var43) * 16.0F);
+                  var40 = var34.getInterpolatedV(8.0F + (var44 + var43) * 16.0F);
+                  var37 = var34.getInterpolatedU(8.0F + (var44 + var43) * 16.0F);
+                  var41 = var34.getInterpolatedV(8.0F + (var44 - var43) * 16.0F);
+                  var38 = var34.getInterpolatedU(8.0F + (var44 - var43) * 16.0F);
+                  var42 = var34.getInterpolatedV(8.0F + (-var44 - var43) * 16.0F);
+               }

-         for (int var60 = 0; var60 < 4; var60++) {
-            int var62 = 0;
-            int var64 = 0;
-            if (var60 == 0) {
-               var64--;
+               int var92 = var2.b(var1, var3);
+               int var93 = var92 >> 16 & 65535;
+               int var95 = var92 & 65535;
+               float var46 = 1.0F * var10;
+               float var47 = 1.0F * var11;
+               float var48 = 1.0F * var12;
+               var4.pos(var26 + 0.0, var28 + var22, var30 + 0.0).color(var46, var47, var48, 1.0F).tex(var35, var39).lightmap(var93, var95).endVertex();
+               var4.pos(var26 + 0.0, var28 + var23, var30 + 1.0).color(var46, var47, var48, 1.0F).tex(var36, var40).lightmap(var93, var95).endVertex();
+               var4.pos(var26 + 1.0, var28 + var24, var30 + 1.0).color(var46, var47, var48, 1.0F).tex(var37, var41).lightmap(var93, var95).endVertex();
+               var4.pos(var26 + 1.0, var28 + var25, var30 + 0.0).color(var46, var47, var48, 1.0F).tex(var38, var42).lightmap(var93, var95).endVertex();
+               if (var5.shouldRenderSides(var1, var3.up())) {
+                  var4.pos(var26 + 0.0, var28 + var22, var30 + 0.0).color(var46, var47, var48, 1.0F).tex(var35, var39).lightmap(var93, var95).endVertex();
+                  var4.pos(var26 + 1.0, var28 + var25, var30 + 0.0).color(var46, var47, var48, 1.0F).tex(var38, var42).lightmap(var93, var95).endVertex();
+                  var4.pos(var26 + 1.0, var28 + var24, var30 + 1.0).color(var46, var47, var48, 1.0F).tex(var37, var41).lightmap(var93, var95).endVertex();
+                  var4.pos(var26 + 0.0, var28 + var23, var30 + 1.0).color(var46, var47, var48, 1.0F).tex(var36, var40).lightmap(var93, var95).endVertex();
+               }
             }

-            if (var60 == 1) {
-               var64++;
+            if (var14) {
+               var4.setSprite(var7[0]);
+               float var68 = var7[0].getMinU();
+               float var71 = var7[0].getMaxU();
+               float var73 = var7[0].getMinV();
+               float var75 = var7[0].getMaxV();
+               int var77 = var2.b(var1, var3.down());
+               int var79 = var77 >> 16 & 65535;
+               int var82 = var77 & 65535;
+               float var85 = FaceBakery.getFaceBrightness(EnumFacing.DOWN);
+               var4.pos(var26, var28, var30 + 1.0)
+                  .color(var10 * var85, var11 * var85, var12 * var85, 1.0F)
+                  .tex(var68, var75)
+                  .lightmap(var79, var82)
+                  .endVertex();
+               var4.pos(var26, var28, var30).color(var10 * var85, var11 * var85, var12 * var85, 1.0F).tex(var68, var73).lightmap(var79, var82).endVertex();
+               var4.pos(var26 + 1.0, var28, var30)
+                  .color(var10 * var85, var11 * var85, var12 * var85, 1.0F)
+                  .tex(var71, var73)
+                  .lightmap(var79, var82)
+                  .endVertex();
+               var4.pos(var26 + 1.0, var28, var30 + 1.0)
+                  .color(var10 * var85, var11 * var85, var12 * var85, 1.0F)
+                  .tex(var71, var75)
+                  .lightmap(var79, var82)
+                  .endVertex();
+               var16 = true;
             }

-            if (var60 == 2) {
-               var62--;
-            }
+            for (int var69 = 0; var69 < 4; var69++) {
+               int var72 = 0;
+               int var74 = 0;
+               if (var69 == 0) {
+                  var74--;
+               }

-            if (var60 == 3) {
-               var62++;
-            }
+               if (var69 == 1) {
+                  var74++;
+               }

-            BlockPos var66 = var3.add(var62, 0, var64);
-            TextureAtlasSprite var68 = var7[1];
-            if (!var6) {
-               Block var70 = var1.getBlockState(var66).getBlock();
-               if (var70 == Blocks.GLASS || var70 == Blocks.STAINED_GLASS) {
-                  var68 = this.atlasSpriteWaterOverlay;
+               if (var69 == 2) {
+                  var72--;
                }
-            }

-            if (var14[var60]) {
-               float var71;
-               float var73;
-               double var74;
-               double var75;
-               double var78;
-               double var80;
-               if (var60 == 0) {
-                  var71 = var21;
-                  var73 = var24;
-                  var74 = var25;
-                  var78 = var25 + 1.0;
-                  var75 = var29 + 0.001F;
-                  var80 = var29 + 0.001F;
-               } else if (var60 == 1) {
-                  var71 = var23;
-                  var73 = var22;
-                  var74 = var25 + 1.0;
-                  var78 = var25;
-                  var75 = var29 + 1.0 - 0.001F;
-                  var80 = var29 + 1.0 - 0.001F;
-               } else if (var60 == 2) {
-                  var71 = var22;
-                  var73 = var21;
-                  var74 = var25 + 0.001F;
-                  var78 = var25 + 0.001F;
-                  var75 = var29 + 1.0;
-                  var80 = var29;
-               } else {
-                  var71 = var24;
-                  var73 = var23;
-                  var74 = var25 + 1.0 - 0.001F;
-                  var78 = var25 + 1.0 - 0.001F;
-                  var75 = var29;
-                  var80 = var29 + 1.0;
-               }
-
-               var15 = true;
-               float var81 = var68.getInterpolatedU(0.0);
-               float var48 = var68.getInterpolatedU(8.0);
-               float var49 = var68.getInterpolatedV((1.0F - var71) * 16.0F * 0.5F);
-               float var50 = var68.getInterpolatedV((1.0F - var73) * 16.0F * 0.5F);
-               float var51 = var68.getInterpolatedV(8.0);
-               int var52 = var2.getPackedLightmapCoords(var1, var66);
-               int var53 = var52 >> 16 & 65535;
-               int var54 = var52 & 65535;
-               float var55 = var60 < 2 ? 0.8F : 0.6F;
-               float var56 = 1.0F * var55 * var9;
-               float var57 = 1.0F * var55 * var10;
-               float var58 = 1.0F * var55 * var11;
-               var4.pos(var74, var27 + var71, var75).color(var56, var57, var58, 1.0F).tex(var81, var49).lightmap(var53, var54).endVertex();
-               var4.pos(var78, var27 + var73, var80).color(var56, var57, var58, 1.0F).tex(var48, var50).lightmap(var53, var54).endVertex();
-               var4.pos(var78, var27 + 0.0, var80).color(var56, var57, var58, 1.0F).tex(var48, var51).lightmap(var53, var54).endVertex();
-               var4.pos(var74, var27 + 0.0, var75).color(var56, var57, var58, 1.0F).tex(var81, var51).lightmap(var53, var54).endVertex();
-               if (var68 != this.atlasSpriteWaterOverlay) {
-                  var4.pos(var74, var27 + 0.0, var75).color(var56, var57, var58, 1.0F).tex(var81, var51).lightmap(var53, var54).endVertex();
-                  var4.pos(var78, var27 + 0.0, var80).color(var56, var57, var58, 1.0F).tex(var48, var51).lightmap(var53, var54).endVertex();
-                  var4.pos(var78, var27 + var73, var80).color(var56, var57, var58, 1.0F).tex(var48, var50).lightmap(var53, var54).endVertex();
-                  var4.pos(var74, var27 + var71, var75).color(var56, var57, var58, 1.0F).tex(var81, var49).lightmap(var53, var54).endVertex();
+               if (var69 == 3) {
+                  var72++;
+               }
+
+               BlockPos var76 = var3.add(var72, 0, var74);
+               TextureAtlasSprite var78 = var7[1];
+               var4.setSprite(var78);
+               float var80 = 0.0F;
+               float var83 = 0.0F;
+               if (!var6) {
+                  IBlockState var86 = var1.getBlockState(var76);
+                  Block var88 = var86.getBlock();
+                  if (var88 == Blocks.GLASS || var88 == Blocks.STAINED_GLASS || var88 == Blocks.BEACON || var88 == Blocks.SLIME_BLOCK) {
+                     var78 = this.atlasSpriteWaterOverlay;
+                     var4.setSprite(var78);
+                  }
+
+                  if (var88 == Blocks.FARMLAND || var88 == Blocks.GRASS_PATH) {
+                     var80 = 0.9375F;
+                     var83 = 0.9375F;
+                  }
+
+                  if (var88 instanceof BlockSlab) {
+                     BlockSlab var90 = (BlockSlab)var88;
+                     if (!var90.isDouble() && var86.getValue(BlockSlab.HALF) == EnumBlockHalf.BOTTOM) {
+                        var80 = 0.5F;
+                        var83 = 0.5F;
+                     }
+                  }
+               }
+
+               if (var15[var69]) {
+                  float var87;
+                  float var89;
+                  double var91;
+                  double var94;
+                  double var96;
+                  double var97;
+                  if (var69 == 0) {
+                     var87 = var22;
+                     var89 = var25;
+                     var91 = var26;
+                     var96 = var26 + 1.0;
+                     var94 = var30 + 0.001F;
+                     var97 = var30 + 0.001F;
+                  } else if (var69 == 1) {
+                     var87 = var24;
+                     var89 = var23;
+                     var91 = var26 + 1.0;
+                     var96 = var26;
+                     var94 = var30 + 1.0 - 0.001F;
+                     var97 = var30 + 1.0 - 0.001F;
+                  } else if (var69 == 2) {
+                     var87 = var23;
+                     var89 = var22;
+                     var91 = var26 + 0.001F;
+                     var96 = var26 + 0.001F;
+                     var94 = var30 + 1.0;
+                     var97 = var30;
+                  } else {
+                     var87 = var25;
+                     var89 = var24;
+                     var91 = var26 + 1.0 - 0.001F;
+                     var96 = var26 + 1.0 - 0.001F;
+                     var94 = var30;
+                     var97 = var30 + 1.0;
+                  }
+
+                  if (!(var87 <= var80) || !(var89 <= var83)) {
+                     var80 = Math.min(var80, var87);
+                     var83 = Math.min(var83, var89);
+                     if (var80 > var32) {
+                        var80 -= var32;
+                     }
+
+                     if (var83 > var32) {
+                        var83 -= var32;
+                     }
+
+                     var16 = true;
+                     float var50 = var78.getInterpolatedU(0.0);
+                     float var51 = var78.getInterpolatedU(8.0);
+                     float var52 = var78.getInterpolatedV((1.0F - var87) * 16.0F * 0.5F);
+                     float var53 = var78.getInterpolatedV((1.0F - var89) * 16.0F * 0.5F);
+                     float var54 = var78.getInterpolatedV(8.0);
+                     float var55 = var78.getInterpolatedV((1.0F - var80) * 16.0F * 0.5F);
+                     float var56 = var78.getInterpolatedV((1.0F - var83) * 16.0F * 0.5F);
+                     int var57 = var2.b(var1, var76);
+                     int var58 = var57 >> 16 & 65535;
+                     int var59 = var57 & 65535;
+                     float var60 = var69 < 2 ? FaceBakery.getFaceBrightness(EnumFacing.NORTH) : FaceBakery.getFaceBrightness(EnumFacing.WEST);
+                     float var61 = 1.0F * var60 * var10;
+                     float var62 = 1.0F * var60 * var11;
+                     float var63 = 1.0F * var60 * var12;
+                     var4.pos(var91, var28 + var87, var94).color(var61, var62, var63, 1.0F).tex(var50, var52).lightmap(var58, var59).endVertex();
+                     var4.pos(var96, var28 + var89, var97).color(var61, var62, var63, 1.0F).tex(var51, var53).lightmap(var58, var59).endVertex();
+                     var4.pos(var96, var28 + var83, var97).color(var61, var62, var63, 1.0F).tex(var51, var56).lightmap(var58, var59).endVertex();
+                     var4.pos(var91, var28 + var80, var94).color(var61, var62, var63, 1.0F).tex(var50, var55).lightmap(var58, var59).endVertex();
+                     if (var78 != this.atlasSpriteWaterOverlay) {
+                        var4.pos(var91, var28 + var80, var94).color(var61, var62, var63, 1.0F).tex(var50, var55).lightmap(var58, var59).endVertex();
+                        var4.pos(var96, var28 + var83, var97).color(var61, var62, var63, 1.0F).tex(var51, var56).lightmap(var58, var59).endVertex();
+                        var4.pos(var96, var28 + var89, var97).color(var61, var62, var63, 1.0F).tex(var51, var53).lightmap(var58, var59).endVertex();
+                        var4.pos(var91, var28 + var87, var94).color(var61, var62, var63, 1.0F).tex(var50, var52).lightmap(var58, var59).endVertex();
+                     }
+                  }
                }
             }
+
+            var4.setSprite(null);
+            return var16;
          }

-         return var15;
+         var16 = false;
+      } finally {
+         if (Config.isShaders()) {
+            SVertexBuilder.popEntity(var4);
+         }
       }
+
+      return var16;
    }

    private float getFluidHeight(IBlockAccess var1, BlockPos var2, Material var3) {
       int var4 = 0;
       float var5 = 0.0F;

       for (int var6 = 0; var6 < 4; var6++) {
          BlockPos var7 = var2.add(-(var6 & 1), 0, -(var6 >> 1 & 1));
-         if (var1.getBlockState(var7.up()).getMaterial() == var3) {
+         if (var1.getBlockState(var7.up()).a() == var3) {
             return 1.0F;
          }

          IBlockState var8 = var1.getBlockState(var7);
-         Material var9 = var8.getMaterial();
-         if (var9 == var3) {
-            int var10 = var8.getValue(BlockLiquid.LEVEL);
+         Material var9 = var8.a();
+         if (var9 != var3) {
+            if (!var9.isSolid()) {
+               var5++;
+               var4++;
+            }
+         } else {
+            int var10 = (Integer)var8.getValue(BlockLiquid.LEVEL);
             if (var10 >= 8 || var10 == 0) {
                var5 += BlockLiquid.getLiquidHeightPercent(var10) * 10.0F;
                var4 += 10;
             }

             var5 += BlockLiquid.getLiquidHeightPercent(var10);
-            var4++;
-         } else if (!var9.isSolid()) {
-            var5++;
             var4++;
          }
       }

       return 1.0F - var5 / var4;
    }
 */
