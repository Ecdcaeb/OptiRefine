package net.minecraft.client.model;

import net.minecraft.util.math.Vec3d;

public class PositionTextureVertex {
   public Vec3d vector3D;
   public float texturePositionX;
   public float texturePositionY;

   public PositionTextureVertex(float var1, float var2, float var3, float var4, float var5) {
      this(new Vec3d(☃, ☃, ☃), ☃, ☃);
   }

   public PositionTextureVertex setTexturePosition(float var1, float var2) {
      return new PositionTextureVertex(this, ☃, ☃);
   }

   public PositionTextureVertex(PositionTextureVertex var1, float var2, float var3) {
      this.vector3D = ☃.vector3D;
      this.texturePositionX = ☃;
      this.texturePositionY = ☃;
   }

   public PositionTextureVertex(Vec3d var1, float var2, float var3) {
      this.vector3D = ☃;
      this.texturePositionX = ☃;
      this.texturePositionY = ☃;
   }
}
