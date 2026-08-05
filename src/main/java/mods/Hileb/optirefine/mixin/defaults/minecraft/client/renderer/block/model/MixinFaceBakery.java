package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.block.model;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.optifine.model.BlockModelUtils;
import net.optifine.shaders.Shaders;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
@Mixin(FaceBakery.class)
public abstract class MixinFaceBakery {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @WrapOperation(method = "fillVertexData([IILnet/minecraft/util/EnumFacing;Lnet/minecraft/client/renderer/block/model/BlockFaceUV;[FLnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraftforge/common/model/ITransformation;Lnet/minecraft/client/renderer/block/model/BlockPartRotation;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/model/FaceBakery;storeVertexData([IIILorg/lwjgl/util/vector/Vector3f;ILnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/block/model/BlockFaceUV;)V"))
    // [AUDIT-OK] fillVertexData 9-arg ITransformation desc matches cleanroom patch (FaceBakery.java.patch); storeVertexData desc baseline; snapVertexPosition matches OF:178
    public void snapVertexPosition(FaceBakery instance, int[] faceData, int storeIndex, int vertexIndex, Vector3f position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV, Operation<Void> original, @Local(ordinal = 0) Vector3f vector3f){
        BlockModelUtils.snapVertexPosition(vector3f);
        original.call(instance, faceData, storeIndex, vertexIndex, position, shadeColor, sprite, faceUV);
    }

    @ModifyConstant(method = "makeQuadVertexData(Lnet/minecraft/client/renderer/block/model/BlockFaceUV;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/util/EnumFacing;[FLnet/minecraftforge/common/model/ITransformation;Lnet/minecraft/client/renderer/block/model/BlockPartRotation;Z)[I", constant = @Constant(intValue = 28))
    // [AUDIT-OK] makeQuadVertexData ITransformation [I matches patch; 28->56 shader size matches OF
    public int wideShaderSize_makeQuadVertexData(int constant){
        return  Config.isShaders() ? 56 : constant;
    }

    @ModifyConstant(method = "getFaceBrightness", constant = @Constant(floatValue = 0.5F))
    // [AUDIT-OK] getFaceBrightness baseline; 0.5/0.8/0.6 -> Shaders.blockLightLevel05/08/06 match OF:125/134/141
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

    // ===== shader-wide stride support =====
    // [AUDIT-FIXED] 2026-08-05: OF computes the vertex stride as data.length/4 in storeVertexData
    // (OF:183-184), applyFacing (OF:302) and getFacingFromVertexData (OF:261-262); the runtime
    // baseline hard-codes the ITEM stride 7 in all three (the cleanroom patch only added the
    // .999/.001 UV blend and the ITransformation overloads). With makeQuadVertexData widened to
    // 56 under shaders, stride-7 writes misalign the BLOCK-format layout read at render time -
    // suspected root cause of shader terrain triangle tearing.

    @WrapMethod(method = "storeVertexData")
    private void optiRefine$storeVertexData(int[] faceData, int storeIndex, int vertexIndex, Vector3f position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV, Operation<Void> original){
        int stride = faceData.length / 4;
        int base = storeIndex * stride;
        faceData[base] = Float.floatToRawIntBits(position.x);
        faceData[base + 1] = Float.floatToRawIntBits(position.y);
        faceData[base + 2] = Float.floatToRawIntBits(position.z);
        faceData[base + 3] = shadeColor;
        faceData[base + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(faceUV.getVertexU(vertexIndex) * 0.999 + faceUV.getVertexU((vertexIndex + 2) % 4) * 0.001));
        faceData[base + 4 + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV(faceUV.getVertexV(vertexIndex) * 0.999 + faceUV.getVertexV((vertexIndex + 2) % 4) * 0.001));
    }

    @WrapMethod(method = "applyFacing")
    private void optiRefine$applyFacing(int[] faceData, EnumFacing facing, Operation<Void> original){
        int[] aint = new int[faceData.length];
        System.arraycopy(faceData, 0, aint, 0, faceData.length);
        float[] afloat = new float[EnumFacing.values().length];
        afloat[EnumFaceDirection.Constants.WEST_INDEX] = 999.0F;
        afloat[EnumFaceDirection.Constants.DOWN_INDEX] = 999.0F;
        afloat[EnumFaceDirection.Constants.NORTH_INDEX] = 999.0F;
        afloat[EnumFaceDirection.Constants.EAST_INDEX] = -999.0F;
        afloat[EnumFaceDirection.Constants.UP_INDEX] = -999.0F;
        afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = -999.0F;
        int stride = faceData.length / 4;

        for (int i = 0; i < 4; i++) {
            int base = stride * i;
            float f = Float.intBitsToFloat(aint[base]);
            float f1 = Float.intBitsToFloat(aint[base + 1]);
            float f2 = Float.intBitsToFloat(aint[base + 2]);
            if (f < afloat[EnumFaceDirection.Constants.WEST_INDEX]) afloat[EnumFaceDirection.Constants.WEST_INDEX] = f;
            if (f1 < afloat[EnumFaceDirection.Constants.DOWN_INDEX]) afloat[EnumFaceDirection.Constants.DOWN_INDEX] = f1;
            if (f2 < afloat[EnumFaceDirection.Constants.NORTH_INDEX]) afloat[EnumFaceDirection.Constants.NORTH_INDEX] = f2;
            if (f > afloat[EnumFaceDirection.Constants.EAST_INDEX]) afloat[EnumFaceDirection.Constants.EAST_INDEX] = f;
            if (f1 > afloat[EnumFaceDirection.Constants.UP_INDEX]) afloat[EnumFaceDirection.Constants.UP_INDEX] = f1;
            if (f2 > afloat[EnumFaceDirection.Constants.SOUTH_INDEX]) afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = f2;
        }

        EnumFaceDirection enumfacedirection = EnumFaceDirection.getFacing(facing);
        for (int i1 = 0; i1 < 4; i1++) {
            int base = stride * i1;
            EnumFaceDirection.VertexInformation vi = enumfacedirection.getVertexInformation(i1);
            float f8 = afloat[vi.xIndex];
            float f3 = afloat[vi.yIndex];
            float f4 = afloat[vi.zIndex];
            faceData[base] = Float.floatToRawIntBits(f8);
            faceData[base + 1] = Float.floatToRawIntBits(f3);
            faceData[base + 2] = Float.floatToRawIntBits(f4);
            for (int k = 0; k < 4; k++) {
                int src = stride * k;
                float f5 = Float.intBitsToFloat(aint[src]);
                float f6 = Float.intBitsToFloat(aint[src + 1]);
                float f7 = Float.intBitsToFloat(aint[src + 2]);
                if (MathHelper.epsilonEquals(f8, f5) && MathHelper.epsilonEquals(f3, f6) && MathHelper.epsilonEquals(f4, f7)) {
                    faceData[base + 4] = aint[src + 4];
                    faceData[base + 4 + 1] = aint[src + 4 + 1];
                }
            }
        }
    }

    @WrapMethod(method = "getFacingFromVertexData")
    private static EnumFacing optiRefine$getFacingFromVertexData(int[] data, Operation<EnumFacing> original){
        int stride = data.length / 4;
        int stride2 = stride * 2;
        Vector3f v0 = new Vector3f(Float.intBitsToFloat(data[0]), Float.intBitsToFloat(data[1]), Float.intBitsToFloat(data[2]));
        Vector3f v1 = new Vector3f(Float.intBitsToFloat(data[stride]), Float.intBitsToFloat(data[stride + 1]), Float.intBitsToFloat(data[stride + 2]));
        Vector3f v2 = new Vector3f(Float.intBitsToFloat(data[stride2]), Float.intBitsToFloat(data[stride2 + 1]), Float.intBitsToFloat(data[stride2 + 2]));
        Vector3f vec3 = new Vector3f();
        Vector3f vec4 = new Vector3f();
        Vector3f vec5 = new Vector3f();
        Vector3f.sub(v0, v1, vec3);
        Vector3f.sub(v2, v1, vec4);
        Vector3f.cross(vec4, vec3, vec5);
        float f = (float) Math.sqrt(vec5.x * vec5.x + vec5.y * vec5.y + vec5.z * vec5.z);
        vec5.x /= f;
        vec5.y /= f;
        vec5.z /= f;
        EnumFacing enumfacing = null;
        float f1 = 0.0F;
        for (EnumFacing dir : EnumFacing.values()) {
            net.minecraft.util.math.Vec3i vec3i = dir.getDirectionVec();
            Vector3f vdir = new Vector3f((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
            float f2 = Vector3f.dot(vec5, vdir);
            if (f2 >= 0.0F && f2 > f1) {
                f1 = f2;
                enumfacing = dir;
            }
        }
        return enumfacing == null ? EnumFacing.UP : enumfacing;
    }


}
