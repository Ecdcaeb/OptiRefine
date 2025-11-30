package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer;

import mods.Hileb.optirefine.library.common.utils.Checked;
import net.minecraft.client.renderer.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;

@Checked
@Mixin(Matrix4f.class)
public abstract class MixinMatrix4f {
}
