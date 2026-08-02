package mods.Hileb.optirefine.mixin.defaults.minecraft.util;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessTransformer;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.util.EnumFacing;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
@Mixin(EnumFacing.class)
public abstract class MixinEnumFacing {

    @Shadow @Final
    private int opposite;

    @Shadow @Final
    @Public
    private static EnumFacing[] VALUES;

    @WrapMethod(method = "getOpposite")
    public EnumFacing getOpposite$fast(Operation<EnumFacing> original){
        return VALUES[this.opposite];
    }
}
