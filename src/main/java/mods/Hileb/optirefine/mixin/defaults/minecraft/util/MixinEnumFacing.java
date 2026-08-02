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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


    @Shadow @Final
// [AUDIT-OK] baseline member opposite exists in target class
    private int opposite;

    @Shadow @Final
    @Public
// [AUDIT-OK] baseline member VALUES exists in target class (@Public on shadow = no-op)
    private static EnumFacing[] VALUES;

    @WrapMethod(method = "getOpposite")
// [AUDIT-OK] getOpposite -> VALUES[opposite] matches OF (baseline byIndex(opposite) is equivalent for 0..5)
    public EnumFacing getOpposite$fast(Operation<EnumFacing> original){
        return VALUES[this.opposite];
    }
}
