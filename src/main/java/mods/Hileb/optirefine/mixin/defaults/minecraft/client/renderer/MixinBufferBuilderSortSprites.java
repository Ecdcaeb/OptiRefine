package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.client.NothiriumQuadOrder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import java.util.Comparator;

@Mixin(value = BufferBuilder.class, priority = 1100)
public abstract class MixinBufferBuilderSortSprites {
    @WrapOperation(method = "sortVertexData", require = 0,
            at = @At(value = "INVOKE", target = "Ljava/util/Arrays;sort([Ljava/lang/Object;Ljava/util/Comparator;)V", remap = false))
    private void optiRefine$sortQuadSprites(Object[] order, Comparator<?> comparator, Operation<Void> original) {
        original.call(order, comparator);
        optiRefine$applyQuadSpriteOrder((BufferBuilder) (Object) this, order, null);
    }

    @WrapOperation(method = "sortVertexData", require = 0, at = @At(value = "INVOKE", remap = false,
            target = "Lmeldexun/nothirium/util/VertexSortUtil;sortVertexData(Lmeldexun/memoryutil/MemoryAccess;IIIFFF)V"))
    private void optiRefine$sortNothiriumQuadSprites(@Coerce Object vertexData, int vertexCount, int vertexSize, int primitiveSize,
                                                    float dx, float dy, float dz, Operation<Void> original) {
        BufferBuilder buffer = (BufferBuilder) (Object) this;
        if (vertexCount / primitiveSize < 2 || optiRefine$getQuadSprites(buffer) == null) {
            original.call(vertexData, vertexCount, vertexSize, primitiveSize, dx, dy, dz);
            return;
        }
        int[] order = NothiriumQuadOrder.sort(vertexData, vertexCount, vertexSize, primitiveSize, dx, dy, dz, original);
        if (order != null) {
            optiRefine$applyQuadSpriteOrder(buffer, null, order);
        }
    }

    @AccessibleOperation(opcode = Opcodes.GETFIELD,
            desc = "net.minecraft.client.renderer.BufferBuilder quadSprites [Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;")
    private static native TextureAtlasSprite[] optiRefine$getQuadSprites(BufferBuilder buffer);

    @AccessibleOperation(opcode = Opcodes.INVOKESPECIAL,
            desc = "net.minecraft.client.renderer.BufferBuilder optiRefine$reorderQuadSprites ([Ljava/lang/Object;[I)V")
    private static native void optiRefine$applyQuadSpriteOrder(BufferBuilder buffer, Object[] objectOrder, int[] primitiveOrder);
}
