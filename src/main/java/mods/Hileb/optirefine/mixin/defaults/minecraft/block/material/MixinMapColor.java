package mods.Hileb.optirefine.mixin.defaults.minecraft.block.material;

import net.minecraft.block.material.MapColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
@Mixin(MapColor.class)
public abstract class MixinMapColor {


    @Shadow
    @Mutable
    public int colorValue;

}
