package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public enum ModelRotation {
   X0_Y0(0, 0),
   X0_Y90(0, 90),
   X0_Y180(0, 180),
   X0_Y270(0, 270),
   X90_Y0(90, 0),
   X90_Y90(90, 90),
   X90_Y180(90, 180),
   X90_Y270(90, 270),
   X180_Y0(180, 0),
   X180_Y90(180, 90),
   X180_Y180(180, 180),
   X180_Y270(180, 270),
   X270_Y0(270, 0),
   X270_Y90(270, 90),
   X270_Y180(270, 180),
   X270_Y270(270, 270);

   private static final Map<Integer, ModelRotation> MAP_ROTATIONS = Maps.newHashMap();
   private final int combinedXY;
   private final Matrix4f matrix;
   private final int quartersX;
   private final int quartersY;

   private static int combineXY(int var0, int var1) {
      return ☃ * 360 + ☃;
   }

   private ModelRotation(int var3, int var4) {
      this.combinedXY = combineXY(☃, ☃);
      this.matrix = new Matrix4f();
      Matrix4f ☃ = new Matrix4f();
      ☃.setIdentity();
      Matrix4f.rotate(-☃ * (float) (Math.PI / 180.0), new Vector3f(1.0F, 0.0F, 0.0F), ☃, ☃);
      this.quartersX = MathHelper.abs(☃ / 90);
      Matrix4f ☃x = new Matrix4f();
      ☃x.setIdentity();
      Matrix4f.rotate(-☃ * (float) (Math.PI / 180.0), new Vector3f(0.0F, 1.0F, 0.0F), ☃x, ☃x);
      this.quartersY = MathHelper.abs(☃ / 90);
      Matrix4f.mul(☃x, ☃, this.matrix);
   }

   public Matrix4f matrix() {
      return this.matrix;
   }

   public EnumFacing rotateFace(EnumFacing var1) {
      EnumFacing ☃ = ☃;

      for (int ☃x = 0; ☃x < this.quartersX; ☃x++) {
         ☃ = ☃.rotateAround(EnumFacing.Axis.X);
      }

      if (☃.getAxis() != EnumFacing.Axis.Y) {
         for (int ☃x = 0; ☃x < this.quartersY; ☃x++) {
            ☃ = ☃.rotateAround(EnumFacing.Axis.Y);
         }
      }

      return ☃;
   }

   public int rotateVertex(EnumFacing var1, int var2) {
      int ☃ = ☃;
      if (☃.getAxis() == EnumFacing.Axis.X) {
         ☃ = (☃ + this.quartersX) % 4;
      }

      EnumFacing ☃x = ☃;

      for (int ☃xx = 0; ☃xx < this.quartersX; ☃xx++) {
         ☃x = ☃x.rotateAround(EnumFacing.Axis.X);
      }

      if (☃x.getAxis() == EnumFacing.Axis.Y) {
         ☃ = (☃ + this.quartersY) % 4;
      }

      return ☃;
   }

   public static ModelRotation getModelRotation(int var0, int var1) {
      return MAP_ROTATIONS.get(combineXY(MathHelper.normalizeAngle(☃, 360), MathHelper.normalizeAngle(☃, 360)));
   }

   static {
      for (ModelRotation ☃ : values()) {
         MAP_ROTATIONS.put(☃.combinedXY, ☃);
      }
   }
}
