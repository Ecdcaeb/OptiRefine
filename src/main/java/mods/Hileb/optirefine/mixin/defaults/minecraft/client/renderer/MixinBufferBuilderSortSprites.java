package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.client.renderer.BufferBuilder;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Comparator;

@Mixin(value = BufferBuilder.class, priority = 1100)
public abstract class MixinBufferBuilderSortSprites {
    // Nothirium replaces sortVertexData with a sort that has no Arrays.sort call.
    @WrapOperation(method = "sortVertexData", require = 0,
            at = @At(value = "INVOKE", target = "Ljava/util/Arrays;sort([Ljava/lang/Object;Ljava/util/Comparator;)V", remap = false))
    private void optiRefine$sortQuadSprites(Object[] order, Comparator<?> comparator, Operation<Void> original) {
        original.call(order, comparator);
        optiRefine$applyQuadSpriteOrder((BufferBuilder) (Object) this, order);
    }

    @AccessibleOperation(opcode = Opcodes.INVOKESPECIAL,
            desc = "net.minecraft.client.renderer.BufferBuilder optiRefine$reorderQuadSprites ([Ljava/lang/Object;)V")
    private static native void optiRefine$applyQuadSpriteOrder(BufferBuilder buffer, Object[] order);
}
