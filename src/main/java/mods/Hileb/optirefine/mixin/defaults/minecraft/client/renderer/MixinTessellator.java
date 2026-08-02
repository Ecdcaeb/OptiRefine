package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.optifine.SmartAnimations;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;
@Mixin(Tessellator.class)
public abstract class MixinTessellator {
// [AUDIT] 2026-08-03 — see AGENT.md; issues: 0

    @Shadow @Final
// [AUDIT-OK] baseline member buffer (SRG field_178183_a)
    private BufferBuilder buffer;

    @Inject(method = "draw", at = @At("HEAD"))
    public void ijdraw(CallbackInfo ci) {
// [AUDIT-OK] target Tessellator.draw()V (SRG func_78381_a) matches baseline
        if (BufferBuilder_animatedSprites_get(this.buffer) != null) {
            SmartAnimations.spritesRendered(BufferBuilder_animatedSprites_get(this.buffer));
        }
    }

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.renderer.BufferBuilder animatedSprites Ljava.util.BitSet;")
// [AUDIT-OK] OF field BufferBuilder.animatedSprites (MixinBufferBuilder @Public provides), not in baseline
    private static native BitSet BufferBuilder_animatedSprites_get(BufferBuilder buffer);
}
