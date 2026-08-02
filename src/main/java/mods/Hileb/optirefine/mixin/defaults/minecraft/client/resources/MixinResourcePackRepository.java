package mods.Hileb.optirefine.mixin.defaults.minecraft.client.resources;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
@Mixin(ResourcePackRepository.class)
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
public abstract class MixinResourcePackRepository {
    @Unique
    @AccessTransformer(name = "field_110617_f", deobf = true)
    // [AUDIT-OK] vanilla SRG field_110617_f = repositoryEntries; @AccessTransformer public, no initializer (no dangling putfield), deobf=true (per contract)
    public List<ResourcePackRepository.Entry> acc_field_110617_f;


}
