package mods.Hileb.optirefine.mixin.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VisGraph.class)
public abstract class MixinVisGraph {

    @Redirect(method = "floodFill", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/EnumFacing;values()[Lnet/minecraft/util/EnumFacing;"))
    public EnumFacing[] redirectEnumFacing_values(){
        return EnumFacing.VALUES;
    }

}
