package mods.Hileb.optirefine.mixin.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.optifine.shaders.MultiTexID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ITextureObject.class)
public interface MixinITextureObject {
    @Unique
    MultiTexID getMultiTexID();
}
