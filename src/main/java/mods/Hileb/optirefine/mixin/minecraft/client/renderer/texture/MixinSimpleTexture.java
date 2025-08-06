package mods.Hileb.optirefine.mixin.minecraft.client.renderer.texture;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.optifine.EmissiveTextures;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.image.BufferedImage;

@Mixin(SimpleTexture.class)
public class MixinSimpleTexture {

    @Shadow @Final
    protected ResourceLocation textureLocation;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public ResourceLocation locationEmissive;
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    public boolean isEmissive;

    @WrapOperation(method = "loadTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;uploadTextureImageAllocate(ILjava/awt/image/BufferedImage;ZZ)I"))
    public int _uploadTextureImageAllocate(int textureId, BufferedImage texture, boolean blur, boolean clamp, Operation<Integer> original, @Local(argsOnly = true)IResourceManager resourceManager){
        if (Config.isShaders()) {
            ShadersTex.loadSimpleTexture(textureId, texture, blur, clamp, resourceManager, this.textureLocation, this.getMultiTexID());
        } else {
            return original.call(textureId, texture, blur, clamp);
        }

        if (EmissiveTextures.isActive()) {
            EmissiveTextures.loadTexture(this.textureLocation, (SimpleTexture)(Object) this);
        }
        return 0;
    }

    @SuppressWarnings({"MissingUnique", "AddedMixinMembersNamePattern"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.AbstractTexture getMultiTexID ()Lnet.optifine.shaders.MultiTexID;")
    public native MultiTexID getMultiTexID();
}
