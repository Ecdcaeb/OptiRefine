package mods.Hileb.optirefine.mixin.defaults.minecraft.block.material;

import net.minecraft.block.material.MapColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
@Mixin(MapColor.class)
public abstract class MixinMapColor {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0



    @Shadow
    @Mutable
// [AUDIT-OK] baseline member colorValue exists in target class (final in baseline; @Mutable shadow matches OF, which drops final so OF jar CustomColors can write it) - final-stripping mechanism assumes mixin @Mutable handling, verify at runtime
    public int colorValue;

}
