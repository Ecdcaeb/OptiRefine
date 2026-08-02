package mods.Hileb.optirefine.mixin.defaults.optifine;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
        SelectiveReloadStateHandler.INSTANCE.beginReload(type -> type == VanillaResourceType.TEXTURES || type == VanillaResourceType.MODELS);
        try {
            mc.refreshResources();
        } finally {
            SelectiveReloadStateHandler.INSTANCE.endReload();
        }
        return Futures.immediateFuture(null);
    }
}
