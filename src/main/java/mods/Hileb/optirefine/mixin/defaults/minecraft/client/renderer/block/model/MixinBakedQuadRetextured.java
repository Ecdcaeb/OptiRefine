package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BakedQuadRetextured.class)
public abstract class MixinBakedQuadRetextured {
    // Shader expansion changes the array stride while Forge retains the original format.
    // Keep Forge's format-dependent UV offset and its original source-sprite field.
    @Redirect(method = "remapQuad", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/vertex/VertexFormat;getIntegerSize()I"))
    private int optiRefine$getVertexStride(VertexFormat format) {
        return ((BakedQuad) (Object) this).getVertexData().length / 4;
    }
}
