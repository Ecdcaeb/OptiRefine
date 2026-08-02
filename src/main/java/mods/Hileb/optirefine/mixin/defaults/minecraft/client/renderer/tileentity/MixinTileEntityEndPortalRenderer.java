package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.tileentity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.optifine.shaders.ShadersRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
@Mixin(TileEntityEndPortalRenderer.class)
public abstract class MixinTileEntityEndPortalRenderer {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @WrapMethod(method = "render(Lnet/minecraft/tileentity/TileEntityEndPortal;DDDFIF)V")
    // [AUDIT-OK] render(TileEntityEndPortal;DDDFIF) baseline; ShadersRender.renderEndPortal guard matches OF:25-26
    public void renderShader(TileEntityEndPortal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, Operation<Void> original){
        if (!Config.isShaders() || !ShadersRender.renderEndPortal(te, x, y, z, partialTicks, destroyStage, this.getOffset())) {
            original.call(te, x, y, z, partialTicks, destroyStage, alpha);
        }
    }

    @Shadow
    // [AUDIT-OK] baseline member getOffset (protected, deobf:202); nit: 'native' on @Shadow — 'abstract' is conventional
    protected native float getOffset();
}
