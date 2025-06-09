package mods.Hileb.optirefine.mixin.minecraft.block.material;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import net.minecraft.block.material.MapColor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MapColor.class)
public abstract class MixinMapColor {

    @SuppressWarnings("all")
    @AccessTransformer(access = Opcodes.ACC_PUBLIC, name = "field_76291_p", deobf = true)
    public int access_colorValue;

}
