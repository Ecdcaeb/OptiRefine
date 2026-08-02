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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


// [AUDIT-OK] target draw(Lnet/minecraft/client/renderer/BufferBuilder;F)V matches baseline; GETSTATIC OLDMODEL_POSITION_TEX_NORMAL present; static-field redirect, no-arg handler OK
    @Redirect(method = "draw", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/vertex/DefaultVertexFormats;OLDMODEL_POSITION_TEX_NORMAL:Lnet/minecraft/client/renderer/vertex/VertexFormat;"))
    public VertexFormat useCustomFormatIfShadersOpened(){
        if (Config.isShaders()) {
            return SVertexFormat.defVertexFormatTextured;
        } else {
            return DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL;
        }
    }
}
