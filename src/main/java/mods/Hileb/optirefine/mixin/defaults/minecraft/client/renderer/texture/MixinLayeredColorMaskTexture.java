package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.optifine.shaders.MultiTexID;
import net.optifine.shaders.ShadersTex;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.image.BufferedImage;
@Mixin(LayeredColorMaskTexture.class)
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
public abstract class MixinLayeredColorMaskTexture {

    @Shadow @Final
    // [AUDIT-OK] baseline member textureLocation exists in LayeredColorMaskTexture
    private ResourceLocation textureLocation;

    @WrapOperation(method = "loadTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;uploadTextureImage(ILjava/awt/image/BufferedImage;)I"))
    // [AUDIT-OK] target loadTexture(IResourceManager)V matches baseline; wrap of uploadTextureImage INVOKE mirrors OF shaders branch
    public int onImageLoad(int textureId, BufferedImage texture, Operation<Integer> original, @Local(argsOnly = true) IResourceManager resourceManager ){
        if (Config.isShaders()) {
            return ShadersTex.loadSimpleTexture(textureId, texture, false, false, resourceManager, this.textureLocation, this.getMultiTexID());
        } else {
            return original.call(textureId, texture);
        }
    }

    @SuppressWarnings({"MissingUnique", "AddedMixinMembersNamePattern"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.AbstractTexture getMultiTexID ()Lnet.optifine.shaders.MultiTexID;")
    // [AUDIT-OK] OF member AbstractTexture.getMultiTexID(), MCP name
    public native MultiTexID getMultiTexID();
}
