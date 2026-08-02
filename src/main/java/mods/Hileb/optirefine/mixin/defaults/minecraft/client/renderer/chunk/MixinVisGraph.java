package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.chunk;


import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@Mixin(VisGraph.class)
public abstract class MixinVisGraph {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0

    @Redirect(method = "floodFill", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/EnumFacing;values()[Lnet/minecraft/util/EnumFacing;"))
    // [AUDIT-OK] floodFill EnumFacing.values() INVOKE unique (deobf:83); VALUES swap matches OF:63
    public EnumFacing[] redirectEnumFacing_values(){
        return EnumFacing.VALUES;
    }

}
