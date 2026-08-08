package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.tileentity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.optifine.EmissiveTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(TileEntityRendererDispatcher.class)
public abstract class MixinTileEntityRendererDispatcher {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
// [AUDIT-FIXED] OF-added member (OF TileEntityRendererDispatcher:58 public TileEntity tileEntityRendered); read by
// net.optifine.RandomEntities.getRandomEntityRendered() — NoSuchFieldError risk without it; no initializer (defaults null)
    private TileEntity tileEntityRendered;

    @Inject(method = "render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer;render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V", shift = At.Shift.BEFORE))
    // [AUDIT-FIXED] OF:164 sets tileEntityRendered = var1 at dispatch start (slow TESR.render branch)
    private void optiRefine$setTileEntityRendered(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage, float p_192841_7_, CallbackInfo ci) {
        this.tileEntityRendered = tileEntityIn;
    }

    @Inject(method = "render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer;render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V", shift = At.Shift.AFTER))
    // [AUDIT-FIXED] OF:171 clears tileEntityRendered after dispatch (slow branch)
    private void optiRefine$clearTileEntityRendered(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage, float p_192841_7_, CallbackInfo ci) {
        this.tileEntityRendered = null;
    }

    @Inject(method = "render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer;renderTileEntityFast(Lnet/minecraft/tileentity/TileEntity;DDDFIFLnet/minecraft/client/renderer/BufferBuilder;)V", shift = At.Shift.BEFORE))
    // [AUDIT-FIXED] OF:164 sets tileEntityRendered = var1 at dispatch start (fast renderTileEntityFast branch)
    private void optiRefine$setTileEntityRenderedFast(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage, float p_192841_7_, CallbackInfo ci) {
        this.tileEntityRendered = tileEntityIn;
    }

    @Inject(method = "render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer;renderTileEntityFast(Lnet/minecraft/tileentity/TileEntity;DDDFIFLnet/minecraft/client/renderer/BufferBuilder;)V", shift = At.Shift.AFTER))
    // [AUDIT-FIXED] OF:171 clears tileEntityRendered after dispatch (fast branch)
    private void optiRefine$clearTileEntityRenderedFast(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage, float p_192841_7_, CallbackInfo ci) {
        this.tileEntityRendered = null;
    }

    @WrapOperation(method = "render(Lnet/minecraft/tileentity/TileEntity;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V"))
    // [AUDIT-OK] 3-arg render -> 7-arg render INVOKE unique (deobf:116-129); emissive begin/end wrap matches OF:136-148
    public void render$(TileEntityRendererDispatcher instance, TileEntity v0, double v1, double v2, double v3, float v4, int v5, float v6, Operation<Void> original){
        if (EmissiveTextures.isActive()) {
            EmissiveTextures.beginRender();
        }

        original.call(instance, v0, v1, v2, v3, v4, v5, v6);
        if (EmissiveTextures.isActive()) {
            if (EmissiveTextures.hasEmissive()) {
                EmissiveTextures.beginRenderEmissive();
                original.call(instance, v0, v1, v2, v3, v4, v5, v6);
                EmissiveTextures.endRenderEmissive();
            }

            EmissiveTextures.endRender();
        }
    }
}
