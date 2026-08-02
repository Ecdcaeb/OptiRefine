package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import mods.Hileb.optirefine.library.api.DeprecatedHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.optifine.shaders.MultiTexID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
@Mixin(ITextureObject.class)
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
public interface MixinITextureObject {

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    // [AUDIT-OK] OF-added interface member getMultiTexID (OF declares abstract; mixin provides @Unique default stub -> returns dummy MultiTexID(0,0,0) via DeprecatedHelper)
    default MultiTexID getMultiTexID(){
        DeprecatedHelper.deprecated(this.getClass(), "getMultiTexID()V", "method were not implemented.");
        return new MultiTexID(0, 0, 0);
    }
}
