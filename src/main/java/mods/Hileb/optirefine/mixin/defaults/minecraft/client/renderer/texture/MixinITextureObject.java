package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.optifine.shaders.MultiTexID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
@Mixin(ITextureObject.class)
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
public interface MixinITextureObject {

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    // Implementations must provide real texture IDs; AbstractTexture supplies the shader-backed implementation.
    MultiTexID getMultiTexID();
}
