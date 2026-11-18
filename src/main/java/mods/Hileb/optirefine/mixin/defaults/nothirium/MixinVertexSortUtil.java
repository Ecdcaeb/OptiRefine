package mods.Hileb.optirefine.mixin.defaults.nothirium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mods.Hileb.optirefine.optifine.client.NothiriumQuadOrder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets = "meldexun.nothirium.util.VertexSortUtil", remap = false)
public abstract class MixinVertexSortUtil {
    @ModifyExpressionValue(method = "sortVertexData(Lmeldexun/memoryutil/MemoryAccess;IIIFFF)V", require = 0,
            at = @At(value = "INVOKE", target = "Lmeldexun/nothirium/util/VertexSortUtil;calculatePrimitiveOrder(Lmeldexun/memoryutil/MemoryAccess;IIIFFF)[I"))
    private static int[] optiRefine$captureQuadOrder(int[] order, @Coerce Object vertexData) {
        NothiriumQuadOrder.capture(vertexData, order);
        return order;
    }
}
