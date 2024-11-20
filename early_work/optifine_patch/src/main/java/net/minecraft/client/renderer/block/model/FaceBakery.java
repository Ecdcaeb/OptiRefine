/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  Config
 *  java.lang.Float
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.System
 *  javax.annotation.Nullable
 *  net.minecraft.client.renderer.EnumFaceDirection
 *  net.minecraft.client.renderer.EnumFaceDirection$Constants
 *  net.minecraft.client.renderer.EnumFaceDirection$VertexInformation
 *  net.minecraft.client.renderer.block.model.BakedQuad
 *  net.minecraft.client.renderer.block.model.BlockFaceUV
 *  net.minecraft.client.renderer.block.model.BlockPartFace
 *  net.minecraft.client.renderer.block.model.BlockPartRotation
 *  net.minecraft.client.renderer.block.model.FaceBakery$5
 *  net.minecraft.client.renderer.block.model.FaceBakery$Rotation
 *  net.minecraft.client.renderer.block.model.ModelRotation
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3i
 *  net.minecraftforge.common.model.ITransformation
 *  net.optifine.model.BlockModelUtils
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorMethod
 *  net.optifine.shaders.Shaders
 *  org.lwjgl.util.vector.Matrix4f
 *  org.lwjgl.util.vector.ReadableVector3f
 *  org.lwjgl.util.vector.Vector3f
 *  org.lwjgl.util.vector.Vector4f
 */
package net.minecraft.client.renderer.block.model;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.model.ITransformation;
import net.optifine.model.BlockModelUtils;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;
import net.optifine.shaders.Shaders;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class FaceBakery {
    private static final float SCALE_ROTATION_22_5 = 1.0f / (float)Math.cos((double)0.3926991f) - 1.0f;
    private static final float SCALE_ROTATION_GENERAL = 1.0f / (float)Math.cos((double)0.7853981633974483) - 1.0f;
    private static final Rotation[] UV_ROTATIONS = new Rotation[ModelRotation.values().length * EnumFacing.values().length];
    private static final Rotation UV_ROTATION_0 = new /* Unavailable Anonymous Inner Class!! */;
    private static final Rotation UV_ROTATION_270 = new /* Unavailable Anonymous Inner Class!! */;
    private static final Rotation UV_ROTATION_INVERSE = new /* Unavailable Anonymous Inner Class!! */;
    private static final Rotation UV_ROTATION_90 = new /* Unavailable Anonymous Inner Class!! */;

    public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn, @Nullable BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
        return this.makeBakedQuad(posFrom, posTo, face, sprite, facing, (ITransformation)modelRotationIn, partRotation, uvLocked, shade);
    }

    public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, ITransformation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
        BlockFaceUV blockfaceuv = face.blockFaceUV;
        if (uvLocked) {
            blockfaceuv = Reflector.ForgeHooksClient_applyUVLock.exists() ? (BlockFaceUV)Reflector.call((ReflectorMethod)Reflector.ForgeHooksClient_applyUVLock, (Object[])new Object[]{face.blockFaceUV, facing, modelRotationIn}) : this.applyUVLock(face.blockFaceUV, facing, (ModelRotation)modelRotationIn);
        }
        boolean quadShade = shade && !Reflector.ForgeHooksClient_fillNormal.exists();
        int[] aint = this.makeQuadVertexData(blockfaceuv, sprite, facing, this.getPositionsDiv16(posFrom, posTo), modelRotationIn, partRotation, quadShade);
        EnumFacing enumfacing = FaceBakery.getFacingFromVertexData(aint);
        if (partRotation == null) {
            this.applyFacing(aint, enumfacing);
        }
        if (Reflector.ForgeHooksClient_fillNormal.exists()) {
            Reflector.call((ReflectorMethod)Reflector.ForgeHooksClient_fillNormal, (Object[])new Object[]{aint, enumfacing});
            return new BakedQuad(aint, face.tintIndex, enumfacing, sprite, shade, DefaultVertexFormats.ITEM);
        }
        return new BakedQuad(aint, face.tintIndex, enumfacing, sprite);
    }

    private BlockFaceUV applyUVLock(BlockFaceUV p_188010_1_, EnumFacing p_188010_2_, ModelRotation p_188010_3_) {
        return UV_ROTATIONS[FaceBakery.getIndex(p_188010_3_, p_188010_2_)].rotateUV(p_188010_1_);
    }

    private int[] makeQuadVertexData(BlockFaceUV uvs, TextureAtlasSprite sprite, EnumFacing orientation, float[] p_188012_4_, ITransformation rotationIn, @Nullable BlockPartRotation partRotation, boolean shade) {
        int vertexSize = 28;
        if (Config.isShaders()) {
            vertexSize = 56;
        }
        int[] aint = new int[vertexSize];
        for (int i = 0; i < 4; ++i) {
            this.fillVertexData(aint, i, orientation, uvs, p_188012_4_, sprite, rotationIn, partRotation, shade);
        }
        return aint;
    }

    private int getFaceShadeColor(EnumFacing facing) {
        float f = FaceBakery.getFaceBrightness(facing);
        int i = MathHelper.clamp((int)((int)(f * 255.0f)), (int)0, (int)255);
        return 0xFF000000 | i << 16 | i << 8 | i;
    }

    public static float getFaceBrightness(EnumFacing facing) {
        switch (5.$SwitchMap$net$minecraft$util$EnumFacing[facing.ordinal()]) {
            case 1: {
                if (Config.isShaders()) {
                    return Shaders.blockLightLevel05;
                }
                return 0.5f;
            }
            case 2: {
                return 1.0f;
            }
            case 3: 
            case 4: {
                if (Config.isShaders()) {
                    return Shaders.blockLightLevel08;
                }
                return 0.8f;
            }
            case 5: 
            case 6: {
                if (Config.isShaders()) {
                    return Shaders.blockLightLevel06;
                }
                return 0.6f;
            }
        }
        return 1.0f;
    }

    private float[] getPositionsDiv16(Vector3f pos1, Vector3f pos2) {
        float[] afloat = new float[EnumFacing.values().length];
        afloat[EnumFaceDirection.Constants.WEST_INDEX] = pos1.x / 16.0f;
        afloat[EnumFaceDirection.Constants.DOWN_INDEX] = pos1.y / 16.0f;
        afloat[EnumFaceDirection.Constants.NORTH_INDEX] = pos1.z / 16.0f;
        afloat[EnumFaceDirection.Constants.EAST_INDEX] = pos2.x / 16.0f;
        afloat[EnumFaceDirection.Constants.UP_INDEX] = pos2.y / 16.0f;
        afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = pos2.z / 16.0f;
        return afloat;
    }

    private void fillVertexData(int[] p_188015_1_, int p_188015_2_, EnumFacing p_188015_3_, BlockFaceUV p_188015_4_, float[] p_188015_5_, TextureAtlasSprite p_188015_6_, ITransformation p_188015_7_, @Nullable BlockPartRotation p_188015_8_, boolean p_188015_9_) {
        EnumFacing enumfacing = p_188015_7_.rotate(p_188015_3_);
        int i = p_188015_9_ ? this.getFaceShadeColor(enumfacing) : -1;
        EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = EnumFaceDirection.getFacing((EnumFacing)p_188015_3_).getVertexInformation(p_188015_2_);
        Vector3f vector3f = new Vector3f(p_188015_5_[enumfacedirection$vertexinformation.xIndex], p_188015_5_[enumfacedirection$vertexinformation.yIndex], p_188015_5_[enumfacedirection$vertexinformation.zIndex]);
        this.rotatePart(vector3f, p_188015_8_);
        int j = this.rotateVertex(vector3f, p_188015_3_, p_188015_2_, p_188015_7_);
        BlockModelUtils.snapVertexPosition((Vector3f)vector3f);
        this.storeVertexData(p_188015_1_, j, p_188015_2_, vector3f, i, p_188015_6_, p_188015_4_);
    }

    private void storeVertexData(int[] faceData, int storeIndex, int vertexIndex, Vector3f position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV) {
        int step = faceData.length / 4;
        int i = storeIndex * step;
        faceData[i] = Float.floatToRawIntBits((float)position.x);
        faceData[i + 1] = Float.floatToRawIntBits((float)position.y);
        faceData[i + 2] = Float.floatToRawIntBits((float)position.z);
        faceData[i + 3] = shadeColor;
        faceData[i + 4] = Float.floatToRawIntBits((float)sprite.getInterpolatedU((double)faceUV.getVertexU(vertexIndex) * 0.999 + (double)faceUV.getVertexU((vertexIndex + 2) % 4) * 0.001));
        faceData[i + 4 + 1] = Float.floatToRawIntBits((float)sprite.getInterpolatedV((double)faceUV.getVertexV(vertexIndex) * 0.999 + (double)faceUV.getVertexV((vertexIndex + 2) % 4) * 0.001));
    }

    private void rotatePart(Vector3f p_178407_1_, @Nullable BlockPartRotation partRotation) {
        if (partRotation != null) {
            Matrix4f matrix4f = this.getMatrixIdentity();
            Vector3f vector3f = new Vector3f(0.0f, 0.0f, 0.0f);
            switch (5.$SwitchMap$net$minecraft$util$EnumFacing$Axis[partRotation.axis.ordinal()]) {
                case 1: {
                    Matrix4f.rotate((float)(partRotation.angle * ((float)Math.PI / 180)), (Vector3f)new Vector3f(1.0f, 0.0f, 0.0f), (Matrix4f)matrix4f, (Matrix4f)matrix4f);
                    vector3f.set(0.0f, 1.0f, 1.0f);
                    break;
                }
                case 2: {
                    Matrix4f.rotate((float)(partRotation.angle * ((float)Math.PI / 180)), (Vector3f)new Vector3f(0.0f, 1.0f, 0.0f), (Matrix4f)matrix4f, (Matrix4f)matrix4f);
                    vector3f.set(1.0f, 0.0f, 1.0f);
                    break;
                }
                case 3: {
                    Matrix4f.rotate((float)(partRotation.angle * ((float)Math.PI / 180)), (Vector3f)new Vector3f(0.0f, 0.0f, 1.0f), (Matrix4f)matrix4f, (Matrix4f)matrix4f);
                    vector3f.set(1.0f, 1.0f, 0.0f);
                }
            }
            if (partRotation.rescale) {
                if (Math.abs((float)partRotation.angle) == 22.5f) {
                    vector3f.scale(SCALE_ROTATION_22_5);
                } else {
                    vector3f.scale(SCALE_ROTATION_GENERAL);
                }
                Vector3f.add((Vector3f)vector3f, (Vector3f)new Vector3f(1.0f, 1.0f, 1.0f), (Vector3f)vector3f);
            } else {
                vector3f.set(1.0f, 1.0f, 1.0f);
            }
            this.rotateScale(p_178407_1_, new Vector3f((ReadableVector3f)partRotation.origin), matrix4f, vector3f);
        }
    }

    public int rotateVertex(Vector3f p_188011_1_, EnumFacing p_188011_2_, int p_188011_3_, ModelRotation p_188011_4_) {
        return this.rotateVertex(p_188011_1_, p_188011_2_, p_188011_3_, (ITransformation)p_188011_4_);
    }

    public int rotateVertex(Vector3f position, EnumFacing facing, int vertexIndex, ITransformation modelRotationIn) {
        if (modelRotationIn == ModelRotation.X0_Y0) {
            return vertexIndex;
        }
        if (Reflector.ForgeHooksClient_transform.exists()) {
            Reflector.call((ReflectorMethod)Reflector.ForgeHooksClient_transform, (Object[])new Object[]{position, modelRotationIn.getMatrix()});
        } else {
            this.rotateScale(position, new Vector3f(0.5f, 0.5f, 0.5f), ((ModelRotation)modelRotationIn).matrix(), new Vector3f(1.0f, 1.0f, 1.0f));
        }
        return modelRotationIn.rotate(facing, vertexIndex);
    }

    private void rotateScale(Vector3f position, Vector3f rotationOrigin, Matrix4f rotationMatrix, Vector3f scale) {
        Vector4f vector4f = new Vector4f(position.x - rotationOrigin.x, position.y - rotationOrigin.y, position.z - rotationOrigin.z, 1.0f);
        Matrix4f.transform((Matrix4f)rotationMatrix, (Vector4f)vector4f, (Vector4f)vector4f);
        vector4f.x *= scale.x;
        vector4f.y *= scale.y;
        vector4f.z *= scale.z;
        position.set(vector4f.x + rotationOrigin.x, vector4f.y + rotationOrigin.y, vector4f.z + rotationOrigin.z);
    }

    private Matrix4f getMatrixIdentity() {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        return matrix4f;
    }

    public static EnumFacing getFacingFromVertexData(int[] faceData) {
        int step = faceData.length / 4;
        int step2 = step * 2;
        Vector3f vector3f = new Vector3f(Float.intBitsToFloat((int)faceData[0]), Float.intBitsToFloat((int)faceData[1]), Float.intBitsToFloat((int)faceData[2]));
        Vector3f vector3f1 = new Vector3f(Float.intBitsToFloat((int)faceData[step]), Float.intBitsToFloat((int)faceData[step + 1]), Float.intBitsToFloat((int)faceData[step + 2]));
        Vector3f vector3f2 = new Vector3f(Float.intBitsToFloat((int)faceData[step2]), Float.intBitsToFloat((int)faceData[step2 + 1]), Float.intBitsToFloat((int)faceData[step2 + 2]));
        Vector3f vector3f3 = new Vector3f();
        Vector3f vector3f4 = new Vector3f();
        Vector3f vector3f5 = new Vector3f();
        Vector3f.sub((Vector3f)vector3f, (Vector3f)vector3f1, (Vector3f)vector3f3);
        Vector3f.sub((Vector3f)vector3f2, (Vector3f)vector3f1, (Vector3f)vector3f4);
        Vector3f.cross((Vector3f)vector3f4, (Vector3f)vector3f3, (Vector3f)vector3f5);
        float f = (float)Math.sqrt((double)(vector3f5.x * vector3f5.x + vector3f5.y * vector3f5.y + vector3f5.z * vector3f5.z));
        vector3f5.x /= f;
        vector3f5.y /= f;
        vector3f5.z /= f;
        EnumFacing enumfacing = null;
        float f1 = 0.0f;
        for (EnumFacing enumfacing1 : EnumFacing.values()) {
            Vec3i vec3i = enumfacing1.getDirectionVec();
            Vector3f vector3f6 = new Vector3f((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            float f2 = Vector3f.dot((Vector3f)vector3f5, (Vector3f)vector3f6);
            if (!(f2 >= 0.0f) || !(f2 > f1)) continue;
            f1 = f2;
            enumfacing = enumfacing1;
        }
        if (enumfacing == null) {
            return EnumFacing.UP;
        }
        return enumfacing;
    }

    private void applyFacing(int[] p_178408_1_, EnumFacing p_178408_2_) {
        int[] aint = new int[p_178408_1_.length];
        System.arraycopy((Object)p_178408_1_, (int)0, (Object)aint, (int)0, (int)p_178408_1_.length);
        float[] afloat = new float[EnumFacing.values().length];
        afloat[EnumFaceDirection.Constants.WEST_INDEX] = 999.0f;
        afloat[EnumFaceDirection.Constants.DOWN_INDEX] = 999.0f;
        afloat[EnumFaceDirection.Constants.NORTH_INDEX] = 999.0f;
        afloat[EnumFaceDirection.Constants.EAST_INDEX] = -999.0f;
        afloat[EnumFaceDirection.Constants.UP_INDEX] = -999.0f;
        afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = -999.0f;
        int step = p_178408_1_.length / 4;
        for (int i = 0; i < 4; ++i) {
            int j = step * i;
            float f = Float.intBitsToFloat((int)aint[j]);
            float f1 = Float.intBitsToFloat((int)aint[j + 1]);
            float f2 = Float.intBitsToFloat((int)aint[j + 2]);
            if (f < afloat[EnumFaceDirection.Constants.WEST_INDEX]) {
                afloat[EnumFaceDirection.Constants.WEST_INDEX] = f;
            }
            if (f1 < afloat[EnumFaceDirection.Constants.DOWN_INDEX]) {
                afloat[EnumFaceDirection.Constants.DOWN_INDEX] = f1;
            }
            if (f2 < afloat[EnumFaceDirection.Constants.NORTH_INDEX]) {
                afloat[EnumFaceDirection.Constants.NORTH_INDEX] = f2;
            }
            if (f > afloat[EnumFaceDirection.Constants.EAST_INDEX]) {
                afloat[EnumFaceDirection.Constants.EAST_INDEX] = f;
            }
            if (f1 > afloat[EnumFaceDirection.Constants.UP_INDEX]) {
                afloat[EnumFaceDirection.Constants.UP_INDEX] = f1;
            }
            if (!(f2 > afloat[EnumFaceDirection.Constants.SOUTH_INDEX])) continue;
            afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = f2;
        }
        EnumFaceDirection enumfacedirection = EnumFaceDirection.getFacing((EnumFacing)p_178408_2_);
        for (int i1 = 0; i1 < 4; ++i1) {
            int j1 = step * i1;
            EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = enumfacedirection.getVertexInformation(i1);
            float f8 = afloat[enumfacedirection$vertexinformation.xIndex];
            float f3 = afloat[enumfacedirection$vertexinformation.yIndex];
            float f4 = afloat[enumfacedirection$vertexinformation.zIndex];
            p_178408_1_[j1] = Float.floatToRawIntBits((float)f8);
            p_178408_1_[j1 + 1] = Float.floatToRawIntBits((float)f3);
            p_178408_1_[j1 + 2] = Float.floatToRawIntBits((float)f4);
            for (int k = 0; k < 4; ++k) {
                int l = step * k;
                float f5 = Float.intBitsToFloat((int)aint[l]);
                float f6 = Float.intBitsToFloat((int)aint[l + 1]);
                float f7 = Float.intBitsToFloat((int)aint[l + 2]);
                if (!MathHelper.epsilonEquals((float)f8, (float)f5) || !MathHelper.epsilonEquals((float)f3, (float)f6) || !MathHelper.epsilonEquals((float)f4, (float)f7)) continue;
                p_178408_1_[j1 + 4] = aint[l + 4];
                p_178408_1_[j1 + 4 + 1] = aint[l + 4 + 1];
            }
        }
    }

    private static void addUvRotation(ModelRotation p_188013_0_, EnumFacing p_188013_1_, Rotation p_188013_2_) {
        FaceBakery.UV_ROTATIONS[FaceBakery.getIndex((ModelRotation)p_188013_0_, (EnumFacing)p_188013_1_)] = p_188013_2_;
    }

    private static int getIndex(ModelRotation p_188014_0_, EnumFacing p_188014_1_) {
        return ModelRotation.values().length * p_188014_1_.ordinal() + p_188014_0_.ordinal();
    }

    static {
        FaceBakery.addUvRotation(ModelRotation.X0_Y0, EnumFacing.DOWN, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y0, EnumFacing.EAST, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y0, EnumFacing.NORTH, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y0, EnumFacing.SOUTH, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y0, EnumFacing.UP, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y0, EnumFacing.WEST, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y90, EnumFacing.EAST, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y90, EnumFacing.NORTH, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y90, EnumFacing.SOUTH, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y90, EnumFacing.WEST, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y180, EnumFacing.EAST, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y180, EnumFacing.NORTH, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y180, EnumFacing.SOUTH, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y180, EnumFacing.WEST, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y270, EnumFacing.EAST, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y270, EnumFacing.NORTH, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y270, EnumFacing.SOUTH, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y270, EnumFacing.WEST, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X90_Y0, EnumFacing.DOWN, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X90_Y0, EnumFacing.SOUTH, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X90_Y90, EnumFacing.DOWN, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X90_Y180, EnumFacing.DOWN, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X90_Y180, EnumFacing.NORTH, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X90_Y270, EnumFacing.DOWN, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X180_Y0, EnumFacing.DOWN, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X180_Y0, EnumFacing.UP, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X270_Y0, EnumFacing.SOUTH, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X270_Y0, EnumFacing.UP, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X270_Y90, EnumFacing.UP, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X270_Y180, EnumFacing.NORTH, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X270_Y180, EnumFacing.UP, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X270_Y270, EnumFacing.UP, UV_ROTATION_0);
        FaceBakery.addUvRotation(ModelRotation.X0_Y270, EnumFacing.UP, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X0_Y90, EnumFacing.DOWN, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X90_Y0, EnumFacing.WEST, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X90_Y90, EnumFacing.WEST, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X90_Y180, EnumFacing.WEST, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X90_Y270, EnumFacing.NORTH, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X90_Y270, EnumFacing.SOUTH, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X90_Y270, EnumFacing.WEST, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X180_Y90, EnumFacing.UP, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X180_Y270, EnumFacing.DOWN, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X270_Y0, EnumFacing.EAST, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X270_Y90, EnumFacing.EAST, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X270_Y90, EnumFacing.NORTH, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X270_Y90, EnumFacing.SOUTH, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X270_Y180, EnumFacing.EAST, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X270_Y270, EnumFacing.EAST, UV_ROTATION_270);
        FaceBakery.addUvRotation(ModelRotation.X0_Y180, EnumFacing.DOWN, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X0_Y180, EnumFacing.UP, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X90_Y0, EnumFacing.NORTH, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X90_Y0, EnumFacing.UP, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X90_Y90, EnumFacing.UP, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X90_Y180, EnumFacing.SOUTH, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X90_Y180, EnumFacing.UP, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X90_Y270, EnumFacing.UP, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y0, EnumFacing.EAST, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y0, EnumFacing.NORTH, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y0, EnumFacing.SOUTH, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y0, EnumFacing.WEST, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y90, EnumFacing.EAST, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y90, EnumFacing.NORTH, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y90, EnumFacing.SOUTH, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y90, EnumFacing.WEST, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y180, EnumFacing.DOWN, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y180, EnumFacing.EAST, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y180, EnumFacing.NORTH, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y180, EnumFacing.SOUTH, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y180, EnumFacing.UP, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y180, EnumFacing.WEST, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y270, EnumFacing.EAST, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y270, EnumFacing.NORTH, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y270, EnumFacing.SOUTH, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X180_Y270, EnumFacing.WEST, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X270_Y0, EnumFacing.DOWN, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X270_Y0, EnumFacing.NORTH, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X270_Y90, EnumFacing.DOWN, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X270_Y180, EnumFacing.DOWN, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X270_Y180, EnumFacing.SOUTH, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X270_Y270, EnumFacing.DOWN, UV_ROTATION_INVERSE);
        FaceBakery.addUvRotation(ModelRotation.X0_Y90, EnumFacing.UP, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X0_Y270, EnumFacing.DOWN, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X90_Y0, EnumFacing.EAST, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X90_Y90, EnumFacing.EAST, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X90_Y90, EnumFacing.NORTH, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X90_Y90, EnumFacing.SOUTH, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X90_Y180, EnumFacing.EAST, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X90_Y270, EnumFacing.EAST, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X270_Y0, EnumFacing.WEST, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X180_Y90, EnumFacing.DOWN, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X180_Y270, EnumFacing.UP, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X270_Y90, EnumFacing.WEST, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X270_Y180, EnumFacing.WEST, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X270_Y270, EnumFacing.NORTH, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X270_Y270, EnumFacing.SOUTH, UV_ROTATION_90);
        FaceBakery.addUvRotation(ModelRotation.X270_Y270, EnumFacing.WEST, UV_ROTATION_90);
    }
}
