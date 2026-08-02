package mods.Hileb.optirefine.mixin.defaults.minecraft.client;

import net.minecraft.client.LoadingScreenRenderer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * see {@link mods.Hileb.optirefine.mixin.defaults.minecraftforge.fml.MixinFMLClientHandler}
 */
@Mixin(LoadingScreenRenderer.class)
public abstract class MixinLoadingScreenRenderer {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
// [AUDIT-NOTE] NO-OP mixin: no shadows/injections; target LoadingScreenRenderer exists in baseline; see MixinFMLClientHandler

    //NO-OPS
}
