package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.tileentity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.optifine.EmissiveTextures;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(TileEntityRendererDispatcher.class)
public abstract class MixinTileEntityRendererDispatcher {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    @Public
// [AUDIT-FIXED] OF-added member (OF TileEntityRendererDispatcher:58 public TileEntity tileEntityRendered); read by
// net.optifine.RandomEntities.getRandomEntityRendered() — NoSuchFieldError risk without it; no initializer (defaults null)
    private TileEntity tileEntityRendered;

    @WrapMethod(method = "render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V")
    private void optiRefine$renderWithTileEntityContext(TileEntity tileEntity, double x, double y, double z,
                                                      float partialTicks, int destroyStage, float alpha, Operation<Void> original) {
        TileEntity previous = this.tileEntityRendered;
        this.tileEntityRendered = tileEntity;
        try {
            original.call(tileEntity, x, y, z, partialTicks, destroyStage, alpha);
        } finally {
            this.tileEntityRendered = previous;
        }
    }

    @AccessibleOperation(opcode = Opcodes.INVOKESTATIC, desc = "net.optifine.EmissiveTextures optiRefine$isRendering ()Z")
    private static native boolean EmissiveTextures_isRendering();

    @WrapOperation(method = "render(Lnet/minecraft/tileentity/TileEntity;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V"))
    // [AUDIT-OK] 3-arg render -> 7-arg render INVOKE unique (deobf:116-129); emissive begin/end wrap matches OF:136-148
    public void render$(TileEntityRendererDispatcher instance, TileEntity v0, double v1, double v2, double v3, float v4, int v5, float v6, Operation<Void> original){
        if (!EmissiveTextures.isActive() || EmissiveTextures_isRendering()) {
            original.call(instance, v0, v1, v2, v3, v4, v5, v6);
            return;
        }
        EmissiveTextures.beginRender();
        try {
            original.call(instance, v0, v1, v2, v3, v4, v5, v6);
            if (EmissiveTextures.hasEmissive()) {
                EmissiveTextures.beginRenderEmissive();
                try {
                    original.call(instance, v0, v1, v2, v3, v4, v5, v6);
                } finally {
                    EmissiveTextures.endRenderEmissive();
                }
            }
        } finally {
            EmissiveTextures.endRender();
        }
    }
}
