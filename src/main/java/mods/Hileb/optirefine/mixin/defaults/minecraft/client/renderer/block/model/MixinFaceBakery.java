package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.block.model;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.optifine.model.BlockModelUtils;
import net.optifine.shaders.Shaders;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(FaceBakery.class)
public abstract class MixinFaceBakery {

    @WrapOperation(method = "fillVertexData([IILnet/minecraft/util/EnumFacing;Lnet/minecraft/client/renderer/block/model/BlockFaceUV;[FLnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraftforge/common/model/ITransformation;Lnet/minecraft/client/renderer/block/model/BlockPartRotation;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/model/FaceBakery;storeVertexData([IIILorg/lwjgl/util/vector/Vector3f;ILnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/block/model/BlockFaceUV;)V"))
    public void snapVertexPosition(FaceBakery instance, int[] faceData, int storeIndex, int vertexIndex, Vector3f position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV, Operation<Void> original, @Local(ordinal = 0) Vector3f vector3f){
        BlockModelUtils.snapVertexPosition(vector3f);
        original.call(instance, faceData, storeIndex, vertexIndex, position, shadeColor, sprite, faceUV);
    }

    @ModifyConstant(method = "makeQuadVertexData(Lnet/minecraft/client/renderer/block/model/BlockFaceUV;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/util/EnumFacing;[FLnet/minecraftforge/common/model/ITransformation;Lnet/minecraft/client/renderer/block/model/BlockPartRotation;Z)[I", constant = @Constant(intValue = 28))
    public int wideShaderSize_makeQuadVertexData(int constant){
        return  Config.isShaders() ? 56 : constant;
    }

    @ModifyConstant(method = "getFaceBrightness", constant = @Constant(floatValue = 0.5F))
    public float wideShaderSize05_getFaceBrightness(float constant){
        if (Config.isShaders()) {
            return Shaders.blockLightLevel05;
        } else return constant;
    }

    @ModifyConstant(method = "getFaceBrightness", constant = @Constant(floatValue = 0.8F))
    public float wideShaderSize08_getFaceBrightness(float constant){
        if (Config.isShaders()) {
            return Shaders.blockLightLevel08;
        } else return constant;
    }

    @ModifyConstant(method = "getFaceBrightness", constant = @Constant(floatValue = 0.6F))
    public float wideShaderSize06_getFaceBrightness(float constant){
        if (Config.isShaders()) {
            return Shaders.blockLightLevel06;
        } else return constant;
    }


}
/*
+++ net/minecraft/client/renderer/block/model/FaceBakery.java	Tue Aug 19 14:59:58 2025
@@ -1,21 +1,28 @@
 package net.minecraft.client.renderer.block.model;

 import javax.annotation.Nullable;
 import net.minecraft.client.renderer.EnumFaceDirection;
+import net.minecraft.client.renderer.EnumFaceDirection.Constants;
+import net.minecraft.client.renderer.EnumFaceDirection.VertexInformation;
 import net.minecraft.client.renderer.texture.TextureAtlasSprite;
+import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.util.math.Vec3i;
+import net.minecraftforge.common.model.ITransformation;
+import net.optifine.model.BlockModelUtils;
+import net.optifine.reflect.Reflector;
+import net.optifine.shaders.Shaders;
 import org.lwjgl.util.vector.Matrix4f;
 import org.lwjgl.util.vector.Vector3f;
 import org.lwjgl.util.vector.Vector4f;

 public class FaceBakery {
    private static final float SCALE_ROTATION_22_5 = 1.0F / (float)Math.cos((float) (Math.PI / 8)) - 1.0F;
-   private static final float SCALE_ROTATION_GENERAL = 1.0F / (float)Math.cos((float) (Math.PI / 4)) - 1.0F;
+   private static final float SCALE_ROTATION_GENERAL = 1.0F / (float)Math.cos(Math.PI / 4) - 1.0F;
    private static final FaceBakery.Rotation[] UV_ROTATIONS = new FaceBakery.Rotation[ModelRotation.values().length * EnumFacing.values().length];
    private static final FaceBakery.Rotation UV_ROTATION_0 = new FaceBakery.Rotation() {
       BlockFaceUV makeRotatedUV(float var1, float var2, float var3, float var4) {
          return new BlockFaceUV(new float[]{var1, var2, var3, var4}, 0);
       }
    };
@@ -43,104 +50,147 @@
       EnumFacing var5,
       ModelRotation var6,
       @Nullable BlockPartRotation var7,
       boolean var8,
       boolean var9
    ) {
+      return this.makeBakedQuad(var1, var2, var3, var4, var5, (ITransformation)var6, var7, var8, var9);
+   }
+
+   public BakedQuad makeBakedQuad(
+      Vector3f var1,
+      Vector3f var2,
+      BlockPartFace var3,
+      TextureAtlasSprite var4,
+      EnumFacing var5,
+      ITransformation var6,
+      BlockPartRotation var7,
+      boolean var8,
+      boolean var9
+   ) {
       BlockFaceUV var10 = var3.blockFaceUV;
       if (var8) {
-         var10 = this.applyUVLock(var3.blockFaceUV, var5, var6);
+         if (Reflector.ForgeHooksClient_applyUVLock.exists()) {
+            var10 = (BlockFaceUV)Reflector.call(Reflector.ForgeHooksClient_applyUVLock, new Object[]{var3.blockFaceUV, var5, var6});
+         } else {
+            var10 = this.applyUVLock(var3.blockFaceUV, var5, (ModelRotation)var6);
+         }
       }

-      int[] var11 = this.makeQuadVertexData(var10, var4, var5, this.getPositionsDiv16(var1, var2), var6, var7, var9);
-      EnumFacing var12 = getFacingFromVertexData(var11);
+      boolean var11 = var9 && !Reflector.ForgeHooksClient_fillNormal.exists();
+      int[] var12 = this.makeQuadVertexData(var10, var4, var5, this.getPositionsDiv16(var1, var2), var6, var7, var11);
+      EnumFacing var13 = getFacingFromVertexData(var12);
       if (var7 == null) {
-         this.applyFacing(var11, var12);
+         this.applyFacing(var12, var13);
       }

-      return new BakedQuad(var11, var3.tintIndex, var12, var4);
+      if (Reflector.ForgeHooksClient_fillNormal.exists()) {
+         Reflector.call(Reflector.ForgeHooksClient_fillNormal, new Object[]{var12, var13});
+         return new BakedQuad(var12, var3.tintIndex, var13, var4, var9, DefaultVertexFormats.ITEM);
+      } else {
+         return new BakedQuad(var12, var3.tintIndex, var13, var4);
+      }
    }

    private BlockFaceUV applyUVLock(BlockFaceUV var1, EnumFacing var2, ModelRotation var3) {
       return UV_ROTATIONS[getIndex(var3, var2)].rotateUV(var1);
    }

    private int[] makeQuadVertexData(
-      BlockFaceUV var1, TextureAtlasSprite var2, EnumFacing var3, float[] var4, ModelRotation var5, @Nullable BlockPartRotation var6, boolean var7
+      BlockFaceUV var1, TextureAtlasSprite var2, EnumFacing var3, float[] var4, ITransformation var5, @Nullable BlockPartRotation var6, boolean var7
    ) {
-      int[] var8 = new int[28];
+      byte var8 = 28;
+      if (Config.isShaders()) {
+         var8 = 56;
+      }
+
+      int[] var9 = new int[var8];

-      for (int var9 = 0; var9 < 4; var9++) {
-         this.fillVertexData(var8, var9, var3, var1, var4, var2, var5, var6, var7);
+      for (int var10 = 0; var10 < 4; var10++) {
+         this.fillVertexData(var9, var10, var3, var1, var4, var2, var5, var6, var7);
       }

-      return var8;
+      return var9;
    }

    private int getFaceShadeColor(EnumFacing var1) {
-      float var2 = this.getFaceBrightness(var1);
+      float var2 = getFaceBrightness(var1);
       int var3 = MathHelper.clamp((int)(var2 * 255.0F), 0, 255);
       return 0xFF000000 | var3 << 16 | var3 << 8 | var3;
    }

-   private float getFaceBrightness(EnumFacing var1) {
-      switch (var1) {
+   public static float getFaceBrightness(EnumFacing var0) {
+      switch (var0) {
          case DOWN:
+            if (Config.isShaders()) {
+               return Shaders.blockLightLevel05;
+            }
+
             return 0.5F;
          case UP:
             return 1.0F;
          case NORTH:
          case SOUTH:
+            if (Config.isShaders()) {
+               return Shaders.blockLightLevel08;
+            }
+
             return 0.8F;
          case WEST:
          case EAST:
+            if (Config.isShaders()) {
+               return Shaders.blockLightLevel06;
+            }
+
             return 0.6F;
          default:
             return 1.0F;
       }
    }

    private float[] getPositionsDiv16(Vector3f var1, Vector3f var2) {
       float[] var3 = new float[EnumFacing.values().length];
-      var3[EnumFaceDirection.Constants.WEST_INDEX] = var1.x / 16.0F;
-      var3[EnumFaceDirection.Constants.DOWN_INDEX] = var1.y / 16.0F;
-      var3[EnumFaceDirection.Constants.NORTH_INDEX] = var1.z / 16.0F;
-      var3[EnumFaceDirection.Constants.EAST_INDEX] = var2.x / 16.0F;
-      var3[EnumFaceDirection.Constants.UP_INDEX] = var2.y / 16.0F;
-      var3[EnumFaceDirection.Constants.SOUTH_INDEX] = var2.z / 16.0F;
+      var3[Constants.WEST_INDEX] = var1.x / 16.0F;
+      var3[Constants.DOWN_INDEX] = var1.y / 16.0F;
+      var3[Constants.NORTH_INDEX] = var1.z / 16.0F;
+      var3[Constants.EAST_INDEX] = var2.x / 16.0F;
+      var3[Constants.UP_INDEX] = var2.y / 16.0F;
+      var3[Constants.SOUTH_INDEX] = var2.z / 16.0F;
       return var3;
    }

    private void fillVertexData(
       int[] var1,
       int var2,
       EnumFacing var3,
       BlockFaceUV var4,
       float[] var5,
       TextureAtlasSprite var6,
-      ModelRotation var7,
+      ITransformation var7,
       @Nullable BlockPartRotation var8,
       boolean var9
    ) {
-      EnumFacing var10 = var7.rotateFace(var3);
+      EnumFacing var10 = var7.rotate(var3);
       int var11 = var9 ? this.getFaceShadeColor(var10) : -1;
-      EnumFaceDirection.VertexInformation var12 = EnumFaceDirection.getFacing(var3).getVertexInformation(var2);
+      VertexInformation var12 = EnumFaceDirection.getFacing(var3).getVertexInformation(var2);
       Vector3f var13 = new Vector3f(var5[var12.xIndex], var5[var12.yIndex], var5[var12.zIndex]);
       this.rotatePart(var13, var8);
       int var14 = this.rotateVertex(var13, var3, var2, var7);
+      BlockModelUtils.snapVertexPosition(var13);
       this.storeVertexData(var1, var14, var2, var13, var11, var6, var4);
    }

    private void storeVertexData(int[] var1, int var2, int var3, Vector3f var4, int var5, TextureAtlasSprite var6, BlockFaceUV var7) {
-      int var8 = var2 * 7;
-      var1[var8] = Float.floatToRawIntBits(var4.x);
-      var1[var8 + 1] = Float.floatToRawIntBits(var4.y);
-      var1[var8 + 2] = Float.floatToRawIntBits(var4.z);
-      var1[var8 + 3] = var5;
-      var1[var8 + 4] = Float.floatToRawIntBits(var6.getInterpolatedU(var7.getVertexU(var3)));
-      var1[var8 + 4 + 1] = Float.floatToRawIntBits(var6.getInterpolatedV(var7.getVertexV(var3)));
+      int var8 = var1.length / 4;
+      int var9 = var2 * var8;
+      var1[var9] = Float.floatToRawIntBits(var4.x);
+      var1[var9 + 1] = Float.floatToRawIntBits(var4.y);
+      var1[var9 + 2] = Float.floatToRawIntBits(var4.z);
+      var1[var9 + 3] = var5;
+      var1[var9 + 4] = Float.floatToRawIntBits(var6.getInterpolatedU(var7.getVertexU(var3) * 0.999 + var7.getVertexU((var3 + 2) % 4) * 0.001));
+      var1[var9 + 4 + 1] = Float.floatToRawIntBits(var6.getInterpolatedV(var7.getVertexV(var3) * 0.999 + var7.getVertexV((var3 + 2) % 4) * 0.001));
    }

    private void rotatePart(Vector3f var1, @Nullable BlockPartRotation var2) {
       if (var2 != null) {
          Matrix4f var3 = this.getMatrixIdentity();
          Vector3f var4 = new Vector3f(0.0F, 0.0F, 0.0F);
@@ -172,17 +222,26 @@

          this.rotateScale(var1, new Vector3f(var2.origin), var3, var4);
       }
    }

    public int rotateVertex(Vector3f var1, EnumFacing var2, int var3, ModelRotation var4) {
+      return this.rotateVertex(var1, var2, var3, (ITransformation)var4);
+   }
+
+   public int rotateVertex(Vector3f var1, EnumFacing var2, int var3, ITransformation var4) {
       if (var4 == ModelRotation.X0_Y0) {
          return var3;
       } else {
-         this.rotateScale(var1, new Vector3f(0.5F, 0.5F, 0.5F), var4.matrix(), new Vector3f(1.0F, 1.0F, 1.0F));
-         return var4.rotateVertex(var2, var3);
+         if (Reflector.ForgeHooksClient_transform.exists()) {
+            Reflector.call(Reflector.ForgeHooksClient_transform, new Object[]{var1, var4.getMatrix()});
+         } else {
+            this.rotateScale(var1, new Vector3f(0.5F, 0.5F, 0.5F), ((ModelRotation)var4).matrix(), new Vector3f(1.0F, 1.0F, 1.0F));
+         }
+
+         return var4.rotate(var2, var3);
       }
    }

    private void rotateScale(Vector3f var1, Vector3f var2, Matrix4f var3, Vector3f var4) {
       Vector4f var5 = new Vector4f(var1.x - var2.x, var1.y - var2.y, var1.z - var2.z, 1.0F);
       Matrix4f.transform(var3, var5, var5);
@@ -196,102 +255,105 @@
       Matrix4f var1 = new Matrix4f();
       var1.setIdentity();
       return var1;
    }

    public static EnumFacing getFacingFromVertexData(int[] var0) {
-      Vector3f var1 = new Vector3f(Float.intBitsToFloat(var0[0]), Float.intBitsToFloat(var0[1]), Float.intBitsToFloat(var0[2]));
-      Vector3f var2 = new Vector3f(Float.intBitsToFloat(var0[7]), Float.intBitsToFloat(var0[8]), Float.intBitsToFloat(var0[9]));
-      Vector3f var3 = new Vector3f(Float.intBitsToFloat(var0[14]), Float.intBitsToFloat(var0[15]), Float.intBitsToFloat(var0[16]));
-      Vector3f var4 = new Vector3f();
-      Vector3f var5 = new Vector3f();
+      int var1 = var0.length / 4;
+      int var2 = var1 * 2;
+      Vector3f var3 = new Vector3f(Float.intBitsToFloat(var0[0]), Float.intBitsToFloat(var0[1]), Float.intBitsToFloat(var0[2]));
+      Vector3f var4 = new Vector3f(Float.intBitsToFloat(var0[var1]), Float.intBitsToFloat(var0[var1 + 1]), Float.intBitsToFloat(var0[var1 + 2]));
+      Vector3f var5 = new Vector3f(Float.intBitsToFloat(var0[var2]), Float.intBitsToFloat(var0[var2 + 1]), Float.intBitsToFloat(var0[var2 + 2]));
       Vector3f var6 = new Vector3f();
-      Vector3f.sub(var1, var2, var4);
-      Vector3f.sub(var3, var2, var5);
-      Vector3f.cross(var5, var4, var6);
-      float var7 = (float)Math.sqrt(var6.x * var6.x + var6.y * var6.y + var6.z * var6.z);
-      var6.x /= var7;
-      var6.y /= var7;
-      var6.z /= var7;
-      EnumFacing var8 = null;
-      float var9 = 0.0F;
-
-      for (EnumFacing var13 : EnumFacing.values()) {
-         Vec3i var14 = var13.getDirectionVec();
-         Vector3f var15 = new Vector3f(var14.getX(), var14.getY(), var14.getZ());
-         float var16 = Vector3f.dot(var6, var15);
-         if (var16 >= 0.0F && var16 > var9) {
-            var9 = var16;
-            var8 = var13;
+      Vector3f var7 = new Vector3f();
+      Vector3f var8 = new Vector3f();
+      Vector3f.sub(var3, var4, var6);
+      Vector3f.sub(var5, var4, var7);
+      Vector3f.cross(var7, var6, var8);
+      float var9 = (float)Math.sqrt(var8.x * var8.x + var8.y * var8.y + var8.z * var8.z);
+      var8.x /= var9;
+      var8.y /= var9;
+      var8.z /= var9;
+      EnumFacing var10 = null;
+      float var11 = 0.0F;
+
+      for (EnumFacing var15 : EnumFacing.values()) {
+         Vec3i var16 = var15.getDirectionVec();
+         Vector3f var17 = new Vector3f(var16.getX(), var16.getY(), var16.getZ());
+         float var18 = Vector3f.dot(var8, var17);
+         if (var18 >= 0.0F && var18 > var11) {
+            var11 = var18;
+            var10 = var15;
          }
       }

-      return var8 == null ? EnumFacing.UP : var8;
+      return var10 == null ? EnumFacing.UP : var10;
    }

    private void applyFacing(int[] var1, EnumFacing var2) {
       int[] var3 = new int[var1.length];
       System.arraycopy(var1, 0, var3, 0, var1.length);
       float[] var4 = new float[EnumFacing.values().length];
-      var4[EnumFaceDirection.Constants.WEST_INDEX] = 999.0F;
-      var4[EnumFaceDirection.Constants.DOWN_INDEX] = 999.0F;
-      var4[EnumFaceDirection.Constants.NORTH_INDEX] = 999.0F;
-      var4[EnumFaceDirection.Constants.EAST_INDEX] = -999.0F;
-      var4[EnumFaceDirection.Constants.UP_INDEX] = -999.0F;
-      var4[EnumFaceDirection.Constants.SOUTH_INDEX] = -999.0F;
-
-      for (int var5 = 0; var5 < 4; var5++) {
-         int var6 = 7 * var5;
-         float var7 = Float.intBitsToFloat(var3[var6]);
-         float var8 = Float.intBitsToFloat(var3[var6 + 1]);
-         float var9 = Float.intBitsToFloat(var3[var6 + 2]);
-         if (var7 < var4[EnumFaceDirection.Constants.WEST_INDEX]) {
-            var4[EnumFaceDirection.Constants.WEST_INDEX] = var7;
+      var4[Constants.WEST_INDEX] = 999.0F;
+      var4[Constants.DOWN_INDEX] = 999.0F;
+      var4[Constants.NORTH_INDEX] = 999.0F;
+      var4[Constants.EAST_INDEX] = -999.0F;
+      var4[Constants.UP_INDEX] = -999.0F;
+      var4[Constants.SOUTH_INDEX] = -999.0F;
+      int var5 = var1.length / 4;
+
+      for (int var6 = 0; var6 < 4; var6++) {
+         int var7 = var5 * var6;
+         float var8 = Float.intBitsToFloat(var3[var7]);
+         float var9 = Float.intBitsToFloat(var3[var7 + 1]);
+         float var10 = Float.intBitsToFloat(var3[var7 + 2]);
+         if (var8 < var4[Constants.WEST_INDEX]) {
+            var4[Constants.WEST_INDEX] = var8;
          }

-         if (var8 < var4[EnumFaceDirection.Constants.DOWN_INDEX]) {
-            var4[EnumFaceDirection.Constants.DOWN_INDEX] = var8;
+         if (var9 < var4[Constants.DOWN_INDEX]) {
+            var4[Constants.DOWN_INDEX] = var9;
          }

-         if (var9 < var4[EnumFaceDirection.Constants.NORTH_INDEX]) {
-            var4[EnumFaceDirection.Constants.NORTH_INDEX] = var9;
+         if (var10 < var4[Constants.NORTH_INDEX]) {
+            var4[Constants.NORTH_INDEX] = var10;
          }

-         if (var7 > var4[EnumFaceDirection.Constants.EAST_INDEX]) {
-            var4[EnumFaceDirection.Constants.EAST_INDEX] = var7;
+         if (var8 > var4[Constants.EAST_INDEX]) {
+            var4[Constants.EAST_INDEX] = var8;
          }

-         if (var8 > var4[EnumFaceDirection.Constants.UP_INDEX]) {
-            var4[EnumFaceDirection.Constants.UP_INDEX] = var8;
+         if (var9 > var4[Constants.UP_INDEX]) {
+            var4[Constants.UP_INDEX] = var9;
          }

-         if (var9 > var4[EnumFaceDirection.Constants.SOUTH_INDEX]) {
-            var4[EnumFaceDirection.Constants.SOUTH_INDEX] = var9;
+         if (var10 > var4[Constants.SOUTH_INDEX]) {
+            var4[Constants.SOUTH_INDEX] = var10;
          }
       }

-      EnumFaceDirection var17 = EnumFaceDirection.getFacing(var2);
-
-      for (int var18 = 0; var18 < 4; var18++) {
-         int var19 = 7 * var18;
-         EnumFaceDirection.VertexInformation var20 = var17.getVertexInformation(var18);
-         float var21 = var4[var20.xIndex];
-         float var10 = var4[var20.yIndex];
-         float var11 = var4[var20.zIndex];
-         var1[var19] = Float.floatToRawIntBits(var21);
-         var1[var19 + 1] = Float.floatToRawIntBits(var10);
-         var1[var19 + 2] = Float.floatToRawIntBits(var11);
+      EnumFaceDirection var18 = EnumFaceDirection.getFacing(var2);

-         for (int var12 = 0; var12 < 4; var12++) {
-            int var13 = 7 * var12;
-            float var14 = Float.intBitsToFloat(var3[var13]);
-            float var15 = Float.intBitsToFloat(var3[var13 + 1]);
-            float var16 = Float.intBitsToFloat(var3[var13 + 2]);
-            if (MathHelper.epsilonEquals(var21, var14) && MathHelper.epsilonEquals(var10, var15) && MathHelper.epsilonEquals(var11, var16)) {
-               var1[var19 + 4] = var3[var13 + 4];
-               var1[var19 + 4 + 1] = var3[var13 + 4 + 1];
+      for (int var19 = 0; var19 < 4; var19++) {
+         int var20 = var5 * var19;
+         VertexInformation var21 = var18.getVertexInformation(var19);
+         float var22 = var4[var21.xIndex];
+         float var11 = var4[var21.yIndex];
+         float var12 = var4[var21.zIndex];
+         var1[var20] = Float.floatToRawIntBits(var22);
+         var1[var20 + 1] = Float.floatToRawIntBits(var11);
+         var1[var20 + 2] = Float.floatToRawIntBits(var12);
+
+         for (int var13 = 0; var13 < 4; var13++) {
+            int var14 = var5 * var13;
+            float var15 = Float.intBitsToFloat(var3[var14]);
+            float var16 = Float.intBitsToFloat(var3[var14 + 1]);
+            float var17 = Float.intBitsToFloat(var3[var14 + 2]);
+            if (MathHelper.epsilonEquals(var22, var15) && MathHelper.epsilonEquals(var11, var16) && MathHelper.epsilonEquals(var12, var17)) {
+               var1[var20 + 4] = var3[var14 + 4];
+               var1[var20 + 4 + 1] = var3[var14 + 4 + 1];
             }
          }
       }
    }

    private static void addUvRotation(ModelRotation var0, EnumFacing var1, FaceBakery.Rotation var2) {
 */
