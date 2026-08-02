package mods.Hileb.optirefine.mixin.defaults.minecraft.client.resources;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import net.minecraft.client.resources.AbstractResourcePack;
import org.spongepowered.asm.mixin.Mixin;

import java.io.File;
@Mixin(AbstractResourcePack.class)
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
public abstract class MixinAbstractResourcePack {

    @AccessTransformer(name = "field_110597_b", deobf = true)
    // [AUDIT-OK] vanilla SRG field_110597_b = resourcePackFile; @AccessTransformer public, no initializer (assignment only in mixin ctor, never injected into target <init>), deobf=true (per contract)
    public final File acc__resourcePackFile;

    protected MixinAbstractResourcePack( File acc__resourcePackFile) {
        this.acc__resourcePackFile = acc__resourcePackFile;
    }
}
