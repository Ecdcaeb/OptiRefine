package mods.Hileb.optirefine.mixin.defaults.minecraft.client;

import mods.Hileb.optirefine.library.common.utils.Checked;
import net.minecraft.client.LoadingScreenRenderer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * see {@link mods.Hileb.optirefine.mixin.defaults.minecraftforge.fml.MixinFMLClientHandler}
 */
@Checked
@Mixin(LoadingScreenRenderer.class)
public abstract class MixinLoadingScreenRenderer {
    //NO-OPS
}
