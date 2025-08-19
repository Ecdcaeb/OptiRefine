package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.block.model.ModelRotation;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ModelRotation.class)
public abstract class MixinModelRotation {

}
/*
+++ net/minecraft/client/renderer/block/model/ModelRotation.java	Tue Aug 19 14:59:58 2025
@@ -1,16 +1,22 @@
 package net.minecraft.client.renderer.block.model;

 import com.google.common.collect.Maps;
 import java.util.Map;
+import java.util.Optional;
 import net.minecraft.util.EnumFacing;
 import net.minecraft.util.math.MathHelper;
+import net.minecraftforge.common.model.IModelPart;
+import net.minecraftforge.common.model.IModelState;
+import net.minecraftforge.common.model.ITransformation;
+import net.minecraftforge.common.model.TRSRTransformation;
+import net.optifine.reflect.Reflector;
 import org.lwjgl.util.vector.Matrix4f;
 import org.lwjgl.util.vector.Vector3f;

-public enum ModelRotation {
+public enum ModelRotation implements IModelState, ITransformation {
    X0_Y0(0, 0),
    X0_Y90(0, 90),
    X0_Y180(0, 180),
    X0_Y270(0, 270),
    X90_Y0(90, 0),
    X90_Y90(90, 90),
@@ -87,12 +93,36 @@

       return var3;
    }

    public static ModelRotation getModelRotation(int var0, int var1) {
       return MAP_ROTATIONS.get(combineXY(MathHelper.normalizeAngle(var0, 360), MathHelper.normalizeAngle(var1, 360)));
+   }
+
+   public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> var1) {
+      return Reflector.ForgeHooksClient_applyTransform_MR.exists()
+         ? (Optional)Reflector.call(Reflector.ForgeHooksClient_applyTransform_MR, new Object[]{this, var1})
+         : (Optional)Reflector.call(Reflector.ForgeHooksClient_applyTransform_M4, new Object[]{this.getMatrix(), var1});
+   }
+
+   public javax.vecmath.Matrix4f getMatrix() {
+      if (Reflector.ForgeHooksClient_applyTransform_MR.exists()) {
+         return TRSRTransformation.from(this).getMatrix();
+      } else {
+         return Reflector.ForgeHooksClient_getMatrix.exists()
+            ? (javax.vecmath.Matrix4f)Reflector.call(Reflector.ForgeHooksClient_getMatrix, new Object[]{this})
+            : new javax.vecmath.Matrix4f(this.matrix());
+      }
+   }
+
+   public EnumFacing rotate(EnumFacing var1) {
+      return this.rotateFace(var1);
+   }
+
+   public int rotate(EnumFacing var1, int var2) {
+      return this.rotateVertex(var1, var2);
    }

    static {
       for (ModelRotation var3 : values()) {
          MAP_ROTATIONS.put(var3.combinedXY, var3);
       }
 */
