package mods.Hileb.optirefine.mixin.defaults.minecraft.util.math;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(ChunkPos.class)
public abstract class MixinChunkPos {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @SuppressWarnings("AddedMixinMembersNamePattern")
// [AUDIT-OK] OF-added field cachedHashCode (in OF ChunkPos, not in baseline), MCP name matches
    @Unique
    private int cachedHashCode;

    @WrapMethod(method = "hashCode", remap = false)
// [AUDIT-OK] hashCode caching matches OF intent (0 sentinel); note: cached VALUE differs from OF (OF uses 1664525*x+... formula, mixin caches vanilla hash) - contract-valid
    public int injectHashCode(Operation<Integer> original){
        if (cachedHashCode != 0) {
            return cachedHashCode;
        } else {
            return cachedHashCode = original.call();
        }
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    // [AUDIT-FIXED] wildcard ctor init: field-initializer injection is unreliable in cleanmix; <init>* matches all ctors without signature matching
    private void optiRefine$initFields(CallbackInfo ci) {
        this.cachedHashCode = 0;
    }
}
