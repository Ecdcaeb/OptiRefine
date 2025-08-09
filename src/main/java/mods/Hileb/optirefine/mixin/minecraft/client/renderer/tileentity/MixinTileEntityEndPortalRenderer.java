package mods.Hileb.optirefine.mixin.minecraft.client.renderer.tileentity;

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

    @WrapMethod(method = "render(Lnet/minecraft/tileentity/TileEntityEndPortal;DDDFIF)V")
    public void renderShader(TileEntityEndPortal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, Operation<Void> original){
        if (!Config.isShaders() || !ShadersRender.renderEndPortal(te, x, y, z, partialTicks, destroyStage, this.getOffset())) {
            original.call(te, x, y, z, partialTicks, destroyStage, alpha);
        }
    }

    @Shadow
    protected native float getOffset();
}
