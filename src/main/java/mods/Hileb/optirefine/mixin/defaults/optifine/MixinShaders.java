package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import org.objectweb.asm.Opcodes;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.resource.SelectiveReloadStateHandler;
import net.minecraftforge.client.resource.VanillaResourceType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.optifine.shaders.Shaders")
public abstract class MixinShaders {
// [AUDIT] 2026-08-03 — OptiFine's full ResourceManager reload on shaderpack switch is mostly unnecessary;
// redirect to a selective reload (TEXTURES + MODELS) so sounds/languages aren't restarted.

    @WrapOperation(method = "loadShaderPack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;func_175603_A()Lcom/google/common/util/concurrent/ListenableFuture;"))
    private static ListenableFuture<Object> optiRefine$selectiveRefresh(Minecraft mc, Operation<ListenableFuture<Object>> original) {
        // SelectiveReloadStateHandler state is read at reload time, so run the reload
        // synchronously inside begin/end (async submission would lose the predicate).
        // [AUDIT-FIXED] three-way audit (P27): OF's loadShaderPack already calls
        // DefaultVertexFormats.updateVertexFormats() earlier (javap offset 379), so the explicit call
        // here was a redundant second invocation — removed. SHADERS listener added to the predicate
        // (OF's full reload re-arms the entity-renderer vanilla shaders on shaderpack switch).
        SelectiveReloadStateHandler.INSTANCE.beginReload(type -> type == VanillaResourceType.TEXTURES || type == VanillaResourceType.MODELS || type == VanillaResourceType.SHADERS);
        try {
            mc.refreshResources();
        } finally {
            SelectiveReloadStateHandler.INSTANCE.endReload();
        }
        mods.Hileb.optirefine.core.OptiRefineLog.log.info("[DIAG] post shaderpack reload: BLOCK.size={}", net.minecraft.client.renderer.vertex.DefaultVertexFormats.BLOCK.getSize());
        return Futures.immediateFuture(null);
    }
}
