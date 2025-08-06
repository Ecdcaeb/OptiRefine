package mods.Hileb.optirefine.mixin.minecraft.client.renderer.texture;

import mods.Hileb.optirefine.library.api.DeprecatedHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.optifine.shaders.MultiTexID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ITextureObject.class)
public interface MixinITextureObject {

    @SuppressWarnings("unused")
    @Unique
    default MultiTexID getMultiTexID(){
        DeprecatedHelper.deprecated(this.getClass(), "getMultiTexID()V", "method were not implemented.");
        return new MultiTexID(0, 0, 0);
    }
}
