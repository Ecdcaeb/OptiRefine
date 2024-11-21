package mods.Hileb.optirefine.mixin.minecraft.client.model;

import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.optifine.shaders.SVertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TexturedQuad.class)
public class MixinTexturedQuad {

    @Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V"))
    public void injectDraw(BufferBuilder instance, int p_181668_1_, VertexFormat p_181668_2_){
        if (DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL == p_181668_2_ && Config.isShaders()) {
            instance.begin(p_181668_1_, SVertexFormat.defVertexFormatTextured);
        }
    }
}
