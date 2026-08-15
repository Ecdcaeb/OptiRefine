package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.chunk;


import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@Mixin(value = VisGraph.class, priority = 1100)
public abstract class MixinVisGraph {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
// [AUDIT-FIXED 2026-08-14] VintageFix 0.7.0's org.embeddedt.vintagefix.mixin.allocation_rate.MixinVisGraph
// @Overwrites floodFill (func_178604_a) at priority 1000 and its body uses EnumFacing.VALUES directly
// (no values() call). Same priority 1000 -> VintageFix applied first -> our @Redirect target INVOKE no
// longer exists -> InvalidInjectionException at apply -> NoClassDefFoundError for VisGraph. priority 1100
// makes our @Redirect apply against the vanilla body first; VintageFix's @Overwrite then replaces the
// whole method (our redirect goes with it). Runtime floodFill = VintageFix's (already VALUES-based),
// OF semantics preserved, no crash.

    @Redirect(method = "floodFill", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/EnumFacing;values()[Lnet/minecraft/util/EnumFacing;"))
    // [AUDIT-OK] floodFill EnumFacing.values() INVOKE unique (deobf:83); VALUES swap matches OF:63
    public EnumFacing[] redirectEnumFacing_values(){
        return EnumFacing.VALUES;
    }

}
