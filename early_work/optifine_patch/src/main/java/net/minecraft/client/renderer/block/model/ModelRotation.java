/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.util.Map
 *  java.util.Optional
 *  javax.vecmath.Matrix4f
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumFacing$Axis
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.common.model.IModelPart
 *  net.minecraftforge.common.model.IModelState
 *  net.minecraftforge.common.model.ITransformation
 *  net.minecraftforge.common.model.TRSRTransformation
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorMethod
 *  org.lwjgl.util.vector.Matrix4f
 *  org.lwjgl.util.vector.Vector3f
 */
package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import javax.vecmath.Matrix4f;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.ITransformation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;
import org.lwjgl.util.vector.Vector3f;

public enum ModelRotation implements IModelState,
ITransformation
{
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

    private static final Map<Integer, ModelRotation> MAP_ROTATIONS;
    private final int combinedXY;
    private final org.lwjgl.util.vector.Matrix4f matrix;
    private final int quartersX;
    private final int quartersY;

    private static int combineXY(int p_177521_0_, int p_177521_1_) {
        return p_177521_0_ * 360 + p_177521_1_;
    }

    private ModelRotation(int x, int y) {
        this.combinedXY = ModelRotation.combineXY(x, y);
        this.matrix = new org.lwjgl.util.vector.Matrix4f();
        org.lwjgl.util.vector.Matrix4f matrix4f = new org.lwjgl.util.vector.Matrix4f();
        matrix4f.setIdentity();
        org.lwjgl.util.vector.Matrix4f.rotate((float)((float)(-x) * ((float)Math.PI / 180)), (Vector3f)new Vector3f(1.0f, 0.0f, 0.0f), (org.lwjgl.util.vector.Matrix4f)matrix4f, (org.lwjgl.util.vector.Matrix4f)matrix4f);
        this.quartersX = MathHelper.abs((int)(x / 90));
        org.lwjgl.util.vector.Matrix4f matrix4f1 = new org.lwjgl.util.vector.Matrix4f();
        matrix4f1.setIdentity();
        org.lwjgl.util.vector.Matrix4f.rotate((float)((float)(-y) * ((float)Math.PI / 180)), (Vector3f)new Vector3f(0.0f, 1.0f, 0.0f), (org.lwjgl.util.vector.Matrix4f)matrix4f1, (org.lwjgl.util.vector.Matrix4f)matrix4f1);
        this.quartersY = MathHelper.abs((int)(y / 90));
        org.lwjgl.util.vector.Matrix4f.mul((org.lwjgl.util.vector.Matrix4f)matrix4f1, (org.lwjgl.util.vector.Matrix4f)matrix4f, (org.lwjgl.util.vector.Matrix4f)this.matrix);
    }

    public org.lwjgl.util.vector.Matrix4f matrix() {
        return this.matrix;
    }

    public EnumFacing rotateFace(EnumFacing facing) {
        EnumFacing enumfacing = facing;
        for (int i = 0; i < this.quartersX; ++i) {
            enumfacing = enumfacing.rotateAround(EnumFacing.Axis.X);
        }
        if (enumfacing.getAxis() != EnumFacing.Axis.Y) {
            for (int j = 0; j < this.quartersY; ++j) {
                enumfacing = enumfacing.rotateAround(EnumFacing.Axis.Y);
            }
        }
        return enumfacing;
    }

    public int rotateVertex(EnumFacing facing, int vertexIndex) {
        int i = vertexIndex;
        if (facing.getAxis() == EnumFacing.Axis.X) {
            i = (vertexIndex + this.quartersX) % 4;
        }
        EnumFacing enumfacing = facing;
        for (int j = 0; j < this.quartersX; ++j) {
            enumfacing = enumfacing.rotateAround(EnumFacing.Axis.X);
        }
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            i = (i + this.quartersY) % 4;
        }
        return i;
    }

    public static ModelRotation getModelRotation(int x, int y) {
        return (ModelRotation)((Object)MAP_ROTATIONS.get((Object)ModelRotation.combineXY(MathHelper.normalizeAngle((int)x, (int)360), MathHelper.normalizeAngle((int)y, (int)360))));
    }

    public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part) {
        if (Reflector.ForgeHooksClient_applyTransform_MR.exists()) {
            return (Optional)Reflector.call((ReflectorMethod)Reflector.ForgeHooksClient_applyTransform_MR, (Object[])new Object[]{this, part});
        }
        return (Optional)Reflector.call((ReflectorMethod)Reflector.ForgeHooksClient_applyTransform_M4, (Object[])new Object[]{this.getMatrix(), part});
    }

    public Matrix4f getMatrix() {
        if (Reflector.ForgeHooksClient_applyTransform_MR.exists()) {
            return TRSRTransformation.from((ModelRotation)this).getMatrix();
        }
        if (Reflector.ForgeHooksClient_getMatrix.exists()) {
            return (Matrix4f)Reflector.call((ReflectorMethod)Reflector.ForgeHooksClient_getMatrix, (Object[])new Object[]{this});
        }
        return new Matrix4f(this.matrix());
    }

    public EnumFacing rotate(EnumFacing facing) {
        return this.rotateFace(facing);
    }

    public int rotate(EnumFacing facing, int vertexIndex) {
        return this.rotateVertex(facing, vertexIndex);
    }

    static {
        MAP_ROTATIONS = Maps.newHashMap();
        for (ModelRotation modelrotation : ModelRotation.values()) {
            MAP_ROTATIONS.put((Object)modelrotation.combinedXY, (Object)modelrotation);
        }
    }
}
