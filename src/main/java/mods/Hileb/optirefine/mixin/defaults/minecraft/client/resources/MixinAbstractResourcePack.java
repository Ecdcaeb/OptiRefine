package mods.Hileb.optirefine.mixin.defaults.minecraft.client.resources;

import mods.Hileb.optirefine.library.common.utils.Checked;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import net.minecraft.client.resources.AbstractResourcePack;
import org.spongepowered.asm.mixin.Mixin;

import java.io.File;

@Checked
@Mixin(AbstractResourcePack.class)
public abstract class MixinAbstractResourcePack {

    @AccessTransformer(name = "field_110597_b", deobf = true)
    public final File acc__resourcePackFile;

    protected MixinAbstractResourcePack( File acc__resourcePackFile) {
        this.acc__resourcePackFile = acc__resourcePackFile;
    }
}
