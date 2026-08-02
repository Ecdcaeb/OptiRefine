package mods.Hileb.optirefine.mixin.defaults.minecraft.client.resources;

import mods.Hileb.optirefine.library.common.utils.Checked;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Checked
@Mixin(ResourcePackRepository.class)
public abstract class MixinResourcePackRepository {
    @Unique
    @AccessTransformer(name = "field_110617_f", deobf = true)
    public final List<ResourcePackRepository.Entry> acc_field_110617_f = null;


}
