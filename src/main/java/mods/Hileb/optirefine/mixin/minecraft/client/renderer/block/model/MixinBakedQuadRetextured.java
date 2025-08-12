package mods.Hileb.optirefine.mixin.minecraft.client.renderer.block.model;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BakedQuadRetextured.class)
public abstract class MixinBakedQuadRetextured {

    @Unique
    private TextureAtlasSprite spriteOld;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(BakedQuad quad, TextureAtlasSprite p_i46217_2, CallbackInfo ci){
        this.spriteOld = quad.getSprite();
        BakedQuad_fixVertexData(this);
    }

    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.block.model.BakedQuad fixVertexData ()V")
    private static native void BakedQuad_fixVertexData(Object bakedQuad);

    @Redirect(method = "remapQuad", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/block/model/BakedQuadRetextured;sprite:Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
    public TextureAtlasSprite applySpriteOld(BakedQuadRetextured instance){
        return spriteOld;
    }

}
