package mods.Hileb.optirefine.mixin.minecraft.client.renderer.block.model;

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
