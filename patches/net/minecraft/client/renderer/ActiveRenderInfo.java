package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.util.glu.GLU;

public class ActiveRenderInfo {
   private static final IntBuffer VIEWPORT = GLAllocation.createDirectIntBuffer(16);
   private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
   private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
   private static final FloatBuffer OBJECTCOORDS = GLAllocation.createDirectFloatBuffer(3);
   private static Vec3d position = new Vec3d(0.0, 0.0, 0.0);
   private static float rotationX;
   private static float rotationXZ;
   private static float rotationZ;
   private static float rotationYZ;
   private static float rotationXY;

   public static void updateRenderInfo(EntityPlayer var0, boolean var1) {
      GlStateManager.getFloat(2982, MODELVIEW);
      GlStateManager.getFloat(2983, PROJECTION);
      GlStateManager.glGetInteger(2978, VIEWPORT);
      float ☃ = (VIEWPORT.get(0) + VIEWPORT.get(2)) / 2;
      float ☃x = (VIEWPORT.get(1) + VIEWPORT.get(3)) / 2;
      GLU.gluUnProject(☃, ☃x, 0.0F, MODELVIEW, PROJECTION, VIEWPORT, OBJECTCOORDS);
      position = new Vec3d(OBJECTCOORDS.get(0), OBJECTCOORDS.get(1), OBJECTCOORDS.get(2));
      int ☃xx = ☃ ? 1 : 0;
      float ☃xxx = ☃.rotationPitch;
      float ☃xxxx = ☃.rotationYaw;
      rotationX = MathHelper.cos(☃xxxx * (float) (Math.PI / 180.0)) * (1 - ☃xx * 2);
      rotationZ = MathHelper.sin(☃xxxx * (float) (Math.PI / 180.0)) * (1 - ☃xx * 2);
      rotationYZ = -rotationZ * MathHelper.sin(☃xxx * (float) (Math.PI / 180.0)) * (1 - ☃xx * 2);
      rotationXY = rotationX * MathHelper.sin(☃xxx * (float) (Math.PI / 180.0)) * (1 - ☃xx * 2);
      rotationXZ = MathHelper.cos(☃xxx * (float) (Math.PI / 180.0));
   }

   public static Vec3d projectViewFromEntity(Entity var0, double var1) {
      double ☃ = ☃.prevPosX + (☃.posX - ☃.prevPosX) * ☃;
      double ☃x = ☃.prevPosY + (☃.posY - ☃.prevPosY) * ☃;
      double ☃xx = ☃.prevPosZ + (☃.posZ - ☃.prevPosZ) * ☃;
      double ☃xxx = ☃ + position.x;
      double ☃xxxx = ☃x + position.y;
      double ☃xxxxx = ☃xx + position.z;
      return new Vec3d(☃xxx, ☃xxxx, ☃xxxxx);
   }

   public static IBlockState getBlockStateAtEntityViewpoint(World var0, Entity var1, float var2) {
      Vec3d ☃ = projectViewFromEntity(☃, ☃);
      BlockPos ☃x = new BlockPos(☃);
      IBlockState ☃xx = ☃.getBlockState(☃x);
      if (☃xx.getMaterial().isLiquid()) {
         float ☃xxx = 0.0F;
         if (☃xx.getBlock() instanceof BlockLiquid) {
            ☃xxx = BlockLiquid.getLiquidHeightPercent(☃xx.getValue(BlockLiquid.LEVEL)) - 0.11111111F;
         }

         float ☃xxxx = ☃x.getY() + 1 - ☃xxx;
         if (☃.y >= ☃xxxx) {
            ☃xx = ☃.getBlockState(☃x.up());
         }
      }

      return ☃xx;
   }

   public static float getRotationX() {
      return rotationX;
   }

   public static float getRotationXZ() {
      return rotationXZ;
   }

   public static float getRotationZ() {
      return rotationZ;
   }

   public static float getRotationYZ() {
      return rotationYZ;
   }

   public static float getRotationXY() {
      return rotationXY;
   }
}
