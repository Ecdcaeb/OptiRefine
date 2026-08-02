package mods.Hileb.optirefine.mixin.defaults.minecraft.client.model;

import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.optifine.shaders.SVertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@Mixin(TexturedQuad.class)
public abstract class MixinTexturedQuad {

    @Redirect(method = "draw", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/vertex/DefaultVertexFormats;OLDMODEL_POSITION_TEX_NORMAL:Lnet/minecraft/client/renderer/vertex/VertexFormat;"))
    public VertexFormat useCustomFormatIfShadersOpened(){
        if (Config.isShaders()) {
            return SVertexFormat.defVertexFormatTextured;
        } else {
            return DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL;
        }
    }
}
