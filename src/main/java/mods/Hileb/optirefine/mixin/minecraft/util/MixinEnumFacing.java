package mods.Hileb.optirefine.mixin.minecraft.util;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import net.minecraft.util.EnumFacing;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnumFacing.class)
public abstract class MixinEnumFacing {

    @SuppressWarnings("all")
    @AccessTransformer(access = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL, name = "field_82609_l", deobf = true)
    public static EnumFacing[] _ACC_VALUES;
}
