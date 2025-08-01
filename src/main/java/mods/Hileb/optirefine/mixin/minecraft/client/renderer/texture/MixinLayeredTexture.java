package mods.Hileb.optirefine.mixin.minecraft.client.renderer.texture;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.BufferedImage;

@Mixin(LayeredTexture.class)
public class MixinLayeredTexture {

    @Unique
    private ResourceLocation textureLocation;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(String[] textureNames, CallbackInfo ci){
        if (textureNames.length > 0 && textureNames[0] != null) {
            this.textureLocation = new ResourceLocation(textureNames[0]);
        }
    }

    @WrapOperation(method = "loadTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;uploadTextureImage(ILjava/awt/image/BufferedImage;)I"))
    public int onImageLoad(int textureId, BufferedImage texture, Operation<Integer> original, @Local(argsOnly = true)IResourceManager resourceManager ){
        if (Config.isShaders()) {
            return ShadersTex.loadSimpleTexture(textureId, texture, false, false, resourceManager, this.textureLocation, this.getMultiTexID());
        } else {
            return original.call(textureId, texture);
        }
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.AbstractTexture getMultiTexID ()Lnet.optifine.shaders.MultiTexID;")
    public native MultiTexID getMultiTexID();
}
