package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
@Mixin(targets = "net.minecraft.util.IntegerCache")
@SuppressWarnings("unused")
public abstract class MixinIntegerCache {
// [AUDIT] 2026-08-09 - three-way audit: OF getInteger caches index 0 (var0 >= 0) and returns new Integer(var0) out of range; vanilla uses var0 > 0 and returns the autoboxed value

    @Shadow
// [AUDIT-OK] baseline member CACHE exists in target class (private static final Integer[65535]; non-final shadow, log-only warning)
    private static Integer[] CACHE;

    @WrapMethod(method = "getInteger")
// [AUDIT-FIXED] OF IntegerCache.getInteger: include index 0 in the cache, return new Integer(var0) outside the cache range
    private static Integer optiRefine$getInteger(int value, Operation<Integer> original) {
        if (value >= 0 && value < CACHE.length) {
            return CACHE[value];
        }
        return new Integer(value);
    }
}
