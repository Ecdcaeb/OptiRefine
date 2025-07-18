package net.minecraft.client.renderer.block.model;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class FaceBakery {
   private static final float SCALE_ROTATION_22_5 = 1.0F / (float)Math.cos((float) (Math.PI / 8)) - 1.0F;
   private static final float SCALE_ROTATION_GENERAL = 1.0F / (float)Math.cos((float) (Math.PI / 4)) - 1.0F;
   private static final FaceBakery.Rotation[] UV_ROTATIONS = new FaceBakery.Rotation[ModelRotation.values().length * EnumFacing.values().length];
   private static final FaceBakery.Rotation UV_ROTATION_0 = new FaceBakery.Rotation() {
      @Override
      BlockFaceUV makeRotatedUV(float var1, float var2, float var3, float var4) {
         return new BlockFaceUV(new float[]{☃, ☃, ☃, ☃}, 0);
      }
   };
   private static final FaceBakery.Rotation UV_ROTATION_270 = new FaceBakery.Rotation() {
      @Override
      BlockFaceUV makeRotatedUV(float var1, float var2, float var3, float var4) {
         return new BlockFaceUV(new float[]{☃, 16.0F - ☃, ☃, 16.0F - ☃}, 270);
      }
   };
   private static final FaceBakery.Rotation UV_ROTATION_INVERSE = new FaceBakery.Rotation() {
      @Override
      BlockFaceUV makeRotatedUV(float var1, float var2, float var3, float var4) {
         return new BlockFaceUV(new float[]{16.0F - ☃, 16.0F - ☃, 16.0F - ☃, 16.0F - ☃}, 0);
      }
   };
   private static final FaceBakery.Rotation UV_ROTATION_90 = new FaceBakery.Rotation() {
      @Override
      BlockFaceUV makeRotatedUV(float var1, float var2, float var3, float var4) {
         return new BlockFaceUV(new float[]{16.0F - ☃, ☃, 16.0F - ☃, ☃}, 90);
      }
   };

   public BakedQuad makeBakedQuad(
      Vector3f var1,
      Vector3f var2,
      BlockPartFace var3,
      TextureAtlasSprite var4,
      EnumFacing var5,
      ModelRotation var6,
      @Nullable BlockPartRotation var7,
      boolean var8,
      boolean var9
   ) {
      BlockFaceUV ☃ = ☃.blockFaceUV;
      if (☃) {
         ☃ = this.applyUVLock(☃.blockFaceUV, ☃, ☃);
      }

      int[] ☃x = this.makeQuadVertexData(☃, ☃, ☃, this.getPositionsDiv16(☃, ☃), ☃, ☃, ☃);
      EnumFacing ☃xx = getFacingFromVertexData(☃x);
      if (☃ == null) {
         this.applyFacing(☃x, ☃xx);
      }

      return new BakedQuad(☃x, ☃.tintIndex, ☃xx, ☃);
   }

   private BlockFaceUV applyUVLock(BlockFaceUV var1, EnumFacing var2, ModelRotation var3) {
      return UV_ROTATIONS[getIndex(☃, ☃)].rotateUV(☃);
   }

   private int[] makeQuadVertexData(
      BlockFaceUV var1, TextureAtlasSprite var2, EnumFacing var3, float[] var4, ModelRotation var5, @Nullable BlockPartRotation var6, boolean var7
   ) {
      int[] ☃ = new int[28];

      for (int ☃x = 0; ☃x < 4; ☃x++) {
         this.fillVertexData(☃, ☃x, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }

      return ☃;
   }

   private int getFaceShadeColor(EnumFacing var1) {
      float ☃ = this.getFaceBrightness(☃);
      int ☃x = MathHelper.clamp((int)(☃ * 255.0F), 0, 255);
      return 0xFF000000 | ☃x << 16 | ☃x << 8 | ☃x;
   }

   private float getFaceBrightness(EnumFacing var1) {
      switch (☃) {
         case DOWN:
            return 0.5F;
         case UP:
            return 1.0F;
         case NORTH:
         case SOUTH:
            return 0.8F;
         case WEST:
         case EAST:
            return 0.6F;
         default:
            return 1.0F;
      }
   }

   private float[] getPositionsDiv16(Vector3f var1, Vector3f var2) {
      float[] ☃ = new float[EnumFacing.values().length];
      ☃[EnumFaceDirection.Constants.WEST_INDEX] = ☃.x / 16.0F;
      ☃[EnumFaceDirection.Constants.DOWN_INDEX] = ☃.y / 16.0F;
      ☃[EnumFaceDirection.Constants.NORTH_INDEX] = ☃.z / 16.0F;
      ☃[EnumFaceDirection.Constants.EAST_INDEX] = ☃.x / 16.0F;
      ☃[EnumFaceDirection.Constants.UP_INDEX] = ☃.y / 16.0F;
      ☃[EnumFaceDirection.Constants.SOUTH_INDEX] = ☃.z / 16.0F;
      return ☃;
   }

   private void fillVertexData(
      int[] var1,
      int var2,
      EnumFacing var3,
      BlockFaceUV var4,
      float[] var5,
      TextureAtlasSprite var6,
      ModelRotation var7,
      @Nullable BlockPartRotation var8,
      boolean var9
   ) {
      EnumFacing ☃ = ☃.rotateFace(☃);
      int ☃x = ☃ ? this.getFaceShadeColor(☃) : -1;
      EnumFaceDirection.VertexInformation ☃xx = EnumFaceDirection.getFacing(☃).getVertexInformation(☃);
      Vector3f ☃xxx = new Vector3f(☃[☃xx.xIndex], ☃[☃xx.yIndex], ☃[☃xx.zIndex]);
      this.rotatePart(☃xxx, ☃);
      int ☃xxxx = this.rotateVertex(☃xxx, ☃, ☃, ☃);
      this.storeVertexData(☃, ☃xxxx, ☃, ☃xxx, ☃x, ☃, ☃);
   }

   private void storeVertexData(int[] var1, int var2, int var3, Vector3f var4, int var5, TextureAtlasSprite var6, BlockFaceUV var7) {
      int ☃ = ☃ * 7;
      ☃[☃] = Float.floatToRawIntBits(☃.x);
      ☃[☃ + 1] = Float.floatToRawIntBits(☃.y);
      ☃[☃ + 2] = Float.floatToRawIntBits(☃.z);
      ☃[☃ + 3] = ☃;
      ☃[☃ + 4] = Float.floatToRawIntBits(☃.getInterpolatedU(☃.getVertexU(☃)));
      ☃[☃ + 4 + 1] = Float.floatToRawIntBits(☃.getInterpolatedV(☃.getVertexV(☃)));
   }

   private void rotatePart(Vector3f var1, @Nullable BlockPartRotation var2) {
      if (☃ != null) {
         Matrix4f ☃ = this.getMatrixIdentity();
         Vector3f ☃x = new Vector3f(0.0F, 0.0F, 0.0F);
         switch (☃.axis) {
            case X:
               Matrix4f.rotate(☃.angle * (float) (Math.PI / 180.0), new Vector3f(1.0F, 0.0F, 0.0F), ☃, ☃);
               ☃x.set(0.0F, 1.0F, 1.0F);
               break;
            case Y:
               Matrix4f.rotate(☃.angle * (float) (Math.PI / 180.0), new Vector3f(0.0F, 1.0F, 0.0F), ☃, ☃);
               ☃x.set(1.0F, 0.0F, 1.0F);
               break;
            case Z:
               Matrix4f.rotate(☃.angle * (float) (Math.PI / 180.0), new Vector3f(0.0F, 0.0F, 1.0F), ☃, ☃);
               ☃x.set(1.0F, 1.0F, 0.0F);
         }

         if (☃.rescale) {
            if (Math.abs(☃.angle) == 22.5F) {
               ☃x.scale(SCALE_ROTATION_22_5);
            } else {
               ☃x.scale(SCALE_ROTATION_GENERAL);
            }

            Vector3f.add(☃x, new Vector3f(1.0F, 1.0F, 1.0F), ☃x);
         } else {
            ☃x.set(1.0F, 1.0F, 1.0F);
         }

         this.rotateScale(☃, new Vector3f(☃.origin), ☃, ☃x);
      }
   }

   public int rotateVertex(Vector3f var1, EnumFacing var2, int var3, ModelRotation var4) {
      if (☃ == ModelRotation.X0_Y0) {
         return ☃;
      } else {
         this.rotateScale(☃, new Vector3f(0.5F, 0.5F, 0.5F), ☃.matrix(), new Vector3f(1.0F, 1.0F, 1.0F));
         return ☃.rotateVertex(☃, ☃);
      }
   }

   private void rotateScale(Vector3f var1, Vector3f var2, Matrix4f var3, Vector3f var4) {
      Vector4f ☃ = new Vector4f(☃.x - ☃.x, ☃.y - ☃.y, ☃.z - ☃.z, 1.0F);
      Matrix4f.transform(☃, ☃, ☃);
      ☃.x = ☃.x * ☃.x;
      ☃.y = ☃.y * ☃.y;
      ☃.z = ☃.z * ☃.z;
      ☃.set(☃.x + ☃.x, ☃.y + ☃.y, ☃.z + ☃.z);
   }

   private Matrix4f getMatrixIdentity() {
      Matrix4f ☃ = new Matrix4f();
      ☃.setIdentity();
      return ☃;
   }

   public static EnumFacing getFacingFromVertexData(int[] var0) {
      Vector3f ☃ = new Vector3f(Float.intBitsToFloat(☃[0]), Float.intBitsToFloat(☃[1]), Float.intBitsToFloat(☃[2]));
      Vector3f ☃x = new Vector3f(Float.intBitsToFloat(☃[7]), Float.intBitsToFloat(☃[8]), Float.intBitsToFloat(☃[9]));
      Vector3f ☃xx = new Vector3f(Float.intBitsToFloat(☃[14]), Float.intBitsToFloat(☃[15]), Float.intBitsToFloat(☃[16]));
      Vector3f ☃xxx = new Vector3f();
      Vector3f ☃xxxx = new Vector3f();
      Vector3f ☃xxxxx = new Vector3f();
      Vector3f.sub(☃, ☃x, ☃xxx);
      Vector3f.sub(☃xx, ☃x, ☃xxxx);
      Vector3f.cross(☃xxxx, ☃xxx, ☃xxxxx);
      float ☃xxxxxx = (float)Math.sqrt(☃xxxxx.x * ☃xxxxx.x + ☃xxxxx.y * ☃xxxxx.y + ☃xxxxx.z * ☃xxxxx.z);
      ☃xxxxx.x /= ☃xxxxxx;
      ☃xxxxx.y /= ☃xxxxxx;
      ☃xxxxx.z /= ☃xxxxxx;
      EnumFacing ☃xxxxxxx = null;
      float ☃xxxxxxxx = 0.0F;

      for (EnumFacing ☃xxxxxxxxx : EnumFacing.values()) {
         Vec3i ☃xxxxxxxxxx = ☃xxxxxxxxx.getDirectionVec();
         Vector3f ☃xxxxxxxxxxx = new Vector3f(☃xxxxxxxxxx.getX(), ☃xxxxxxxxxx.getY(), ☃xxxxxxxxxx.getZ());
         float ☃xxxxxxxxxxxx = Vector3f.dot(☃xxxxx, ☃xxxxxxxxxxx);
         if (☃xxxxxxxxxxxx >= 0.0F && ☃xxxxxxxxxxxx > ☃xxxxxxxx) {
            ☃xxxxxxxx = ☃xxxxxxxxxxxx;
            ☃xxxxxxx = ☃xxxxxxxxx;
         }
      }

      return ☃xxxxxxx == null ? EnumFacing.UP : ☃xxxxxxx;
   }

   private void applyFacing(int[] var1, EnumFacing var2) {
      int[] ☃ = new int[☃.length];
      System.arraycopy(☃, 0, ☃, 0, ☃.length);
      float[] ☃x = new float[EnumFacing.values().length];
      ☃x[EnumFaceDirection.Constants.WEST_INDEX] = 999.0F;
      ☃x[EnumFaceDirection.Constants.DOWN_INDEX] = 999.0F;
      ☃x[EnumFaceDirection.Constants.NORTH_INDEX] = 999.0F;
      ☃x[EnumFaceDirection.Constants.EAST_INDEX] = -999.0F;
      ☃x[EnumFaceDirection.Constants.UP_INDEX] = -999.0F;
      ☃x[EnumFaceDirection.Constants.SOUTH_INDEX] = -999.0F;

      for (int ☃xx = 0; ☃xx < 4; ☃xx++) {
         int ☃xxx = 7 * ☃xx;
         float ☃xxxx = Float.intBitsToFloat(☃[☃xxx]);
         float ☃xxxxx = Float.intBitsToFloat(☃[☃xxx + 1]);
         float ☃xxxxxx = Float.intBitsToFloat(☃[☃xxx + 2]);
         if (☃xxxx < ☃x[EnumFaceDirection.Constants.WEST_INDEX]) {
            ☃x[EnumFaceDirection.Constants.WEST_INDEX] = ☃xxxx;
         }

         if (☃xxxxx < ☃x[EnumFaceDirection.Constants.DOWN_INDEX]) {
            ☃x[EnumFaceDirection.Constants.DOWN_INDEX] = ☃xxxxx;
         }

         if (☃xxxxxx < ☃x[EnumFaceDirection.Constants.NORTH_INDEX]) {
            ☃x[EnumFaceDirection.Constants.NORTH_INDEX] = ☃xxxxxx;
         }

         if (☃xxxx > ☃x[EnumFaceDirection.Constants.EAST_INDEX]) {
            ☃x[EnumFaceDirection.Constants.EAST_INDEX] = ☃xxxx;
         }

         if (☃xxxxx > ☃x[EnumFaceDirection.Constants.UP_INDEX]) {
            ☃x[EnumFaceDirection.Constants.UP_INDEX] = ☃xxxxx;
         }

         if (☃xxxxxx > ☃x[EnumFaceDirection.Constants.SOUTH_INDEX]) {
            ☃x[EnumFaceDirection.Constants.SOUTH_INDEX] = ☃xxxxxx;
         }
      }

      EnumFaceDirection ☃xx = EnumFaceDirection.getFacing(☃);

      for (int ☃xxxxxxx = 0; ☃xxxxxxx < 4; ☃xxxxxxx++) {
         int ☃xxxxxxxx = 7 * ☃xxxxxxx;
         EnumFaceDirection.VertexInformation ☃xxxxxxxxx = ☃xx.getVertexInformation(☃xxxxxxx);
         float ☃xxxxxxxxxx = ☃x[☃xxxxxxxxx.xIndex];
         float ☃xxxxxxxxxxx = ☃x[☃xxxxxxxxx.yIndex];
         float ☃xxxxxxxxxxxx = ☃x[☃xxxxxxxxx.zIndex];
         ☃[☃xxxxxxxx] = Float.floatToRawIntBits(☃xxxxxxxxxx);
         ☃[☃xxxxxxxx + 1] = Float.floatToRawIntBits(☃xxxxxxxxxxx);
         ☃[☃xxxxxxxx + 2] = Float.floatToRawIntBits(☃xxxxxxxxxxxx);

         for (int ☃xxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxx < 4; ☃xxxxxxxxxxxxx++) {
            int ☃xxxxxxxxxxxxxx = 7 * ☃xxxxxxxxxxxxx;
            float ☃xxxxxxxxxxxxxxx = Float.intBitsToFloat(☃[☃xxxxxxxxxxxxxx]);
            float ☃xxxxxxxxxxxxxxxx = Float.intBitsToFloat(☃[☃xxxxxxxxxxxxxx + 1]);
            float ☃xxxxxxxxxxxxxxxxx = Float.intBitsToFloat(☃[☃xxxxxxxxxxxxxx + 2]);
            if (MathHelper.epsilonEquals(☃xxxxxxxxxx, ☃xxxxxxxxxxxxxxx)
               && MathHelper.epsilonEquals(☃xxxxxxxxxxx, ☃xxxxxxxxxxxxxxxx)
               && MathHelper.epsilonEquals(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxx)) {
               ☃[☃xxxxxxxx + 4] = ☃[☃xxxxxxxxxxxxxx + 4];
               ☃[☃xxxxxxxx + 4 + 1] = ☃[☃xxxxxxxxxxxxxx + 4 + 1];
            }
         }
      }
   }

   private static void addUvRotation(ModelRotation var0, EnumFacing var1, FaceBakery.Rotation var2) {
      UV_ROTATIONS[getIndex(☃, ☃)] = ☃;
   }

   private static int getIndex(ModelRotation var0, EnumFacing var1) {
      return ModelRotation.values().length * ☃.ordinal() + ☃.ordinal();
   }

   static {
      addUvRotation(ModelRotation.X0_Y0, EnumFacing.DOWN, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y0, EnumFacing.EAST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y0, EnumFacing.NORTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y0, EnumFacing.SOUTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y0, EnumFacing.UP, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y0, EnumFacing.WEST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y90, EnumFacing.EAST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y90, EnumFacing.NORTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y90, EnumFacing.SOUTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y90, EnumFacing.WEST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y180, EnumFacing.EAST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y180, EnumFacing.NORTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y180, EnumFacing.SOUTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y180, EnumFacing.WEST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y270, EnumFacing.EAST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y270, EnumFacing.NORTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y270, EnumFacing.SOUTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y270, EnumFacing.WEST, UV_ROTATION_0);
      addUvRotation(ModelRotation.X90_Y0, EnumFacing.DOWN, UV_ROTATION_0);
      addUvRotation(ModelRotation.X90_Y0, EnumFacing.SOUTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X90_Y90, EnumFacing.DOWN, UV_ROTATION_0);
      addUvRotation(ModelRotation.X90_Y180, EnumFacing.DOWN, UV_ROTATION_0);
      addUvRotation(ModelRotation.X90_Y180, EnumFacing.NORTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X90_Y270, EnumFacing.DOWN, UV_ROTATION_0);
      addUvRotation(ModelRotation.X180_Y0, EnumFacing.DOWN, UV_ROTATION_0);
      addUvRotation(ModelRotation.X180_Y0, EnumFacing.UP, UV_ROTATION_0);
      addUvRotation(ModelRotation.X270_Y0, EnumFacing.SOUTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X270_Y0, EnumFacing.UP, UV_ROTATION_0);
      addUvRotation(ModelRotation.X270_Y90, EnumFacing.UP, UV_ROTATION_0);
      addUvRotation(ModelRotation.X270_Y180, EnumFacing.NORTH, UV_ROTATION_0);
      addUvRotation(ModelRotation.X270_Y180, EnumFacing.UP, UV_ROTATION_0);
      addUvRotation(ModelRotation.X270_Y270, EnumFacing.UP, UV_ROTATION_0);
      addUvRotation(ModelRotation.X0_Y270, EnumFacing.UP, UV_ROTATION_270);
      addUvRotation(ModelRotation.X0_Y90, EnumFacing.DOWN, UV_ROTATION_270);
      addUvRotation(ModelRotation.X90_Y0, EnumFacing.WEST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X90_Y90, EnumFacing.WEST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X90_Y180, EnumFacing.WEST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X90_Y270, EnumFacing.NORTH, UV_ROTATION_270);
      addUvRotation(ModelRotation.X90_Y270, EnumFacing.SOUTH, UV_ROTATION_270);
      addUvRotation(ModelRotation.X90_Y270, EnumFacing.WEST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X180_Y90, EnumFacing.UP, UV_ROTATION_270);
      addUvRotation(ModelRotation.X180_Y270, EnumFacing.DOWN, UV_ROTATION_270);
      addUvRotation(ModelRotation.X270_Y0, EnumFacing.EAST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X270_Y90, EnumFacing.EAST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X270_Y90, EnumFacing.NORTH, UV_ROTATION_270);
      addUvRotation(ModelRotation.X270_Y90, EnumFacing.SOUTH, UV_ROTATION_270);
      addUvRotation(ModelRotation.X270_Y180, EnumFacing.EAST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X270_Y270, EnumFacing.EAST, UV_ROTATION_270);
      addUvRotation(ModelRotation.X0_Y180, EnumFacing.DOWN, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X0_Y180, EnumFacing.UP, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X90_Y0, EnumFacing.NORTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X90_Y0, EnumFacing.UP, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X90_Y90, EnumFacing.UP, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X90_Y180, EnumFacing.SOUTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X90_Y180, EnumFacing.UP, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X90_Y270, EnumFacing.UP, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y0, EnumFacing.EAST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y0, EnumFacing.NORTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y0, EnumFacing.SOUTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y0, EnumFacing.WEST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y90, EnumFacing.EAST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y90, EnumFacing.NORTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y90, EnumFacing.SOUTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y90, EnumFacing.WEST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y180, EnumFacing.DOWN, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y180, EnumFacing.EAST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y180, EnumFacing.NORTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y180, EnumFacing.SOUTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y180, EnumFacing.UP, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y180, EnumFacing.WEST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y270, EnumFacing.EAST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y270, EnumFacing.NORTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y270, EnumFacing.SOUTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X180_Y270, EnumFacing.WEST, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X270_Y0, EnumFacing.DOWN, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X270_Y0, EnumFacing.NORTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X270_Y90, EnumFacing.DOWN, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X270_Y180, EnumFacing.DOWN, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X270_Y180, EnumFacing.SOUTH, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X270_Y270, EnumFacing.DOWN, UV_ROTATION_INVERSE);
      addUvRotation(ModelRotation.X0_Y90, EnumFacing.UP, UV_ROTATION_90);
      addUvRotation(ModelRotation.X0_Y270, EnumFacing.DOWN, UV_ROTATION_90);
      addUvRotation(ModelRotation.X90_Y0, EnumFacing.EAST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X90_Y90, EnumFacing.EAST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X90_Y90, EnumFacing.NORTH, UV_ROTATION_90);
      addUvRotation(ModelRotation.X90_Y90, EnumFacing.SOUTH, UV_ROTATION_90);
      addUvRotation(ModelRotation.X90_Y180, EnumFacing.EAST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X90_Y270, EnumFacing.EAST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X270_Y0, EnumFacing.WEST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X180_Y90, EnumFacing.DOWN, UV_ROTATION_90);
      addUvRotation(ModelRotation.X180_Y270, EnumFacing.UP, UV_ROTATION_90);
      addUvRotation(ModelRotation.X270_Y90, EnumFacing.WEST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X270_Y180, EnumFacing.WEST, UV_ROTATION_90);
      addUvRotation(ModelRotation.X270_Y270, EnumFacing.NORTH, UV_ROTATION_90);
      addUvRotation(ModelRotation.X270_Y270, EnumFacing.SOUTH, UV_ROTATION_90);
      addUvRotation(ModelRotation.X270_Y270, EnumFacing.WEST, UV_ROTATION_90);
   }

   abstract static class Rotation {
      private Rotation() {
      }

      public BlockFaceUV rotateUV(BlockFaceUV var1) {
         float ☃ = ☃.getVertexU(☃.getVertexRotatedRev(0));
         float ☃x = ☃.getVertexV(☃.getVertexRotatedRev(0));
         float ☃xx = ☃.getVertexU(☃.getVertexRotatedRev(2));
         float ☃xxx = ☃.getVertexV(☃.getVertexRotatedRev(2));
         return this.makeRotatedUV(☃, ☃x, ☃xx, ☃xxx);
      }

      abstract BlockFaceUV makeRotatedUV(float var1, float var2, float var3, float var4);
   }
}
