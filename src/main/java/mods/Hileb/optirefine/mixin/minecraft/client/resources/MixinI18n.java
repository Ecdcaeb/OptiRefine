package mods.Hileb.optirefine.mixin.minecraft.client.resources;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Locale;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@SuppressWarnings("ALL")
@Mixin(I18n.class)
public abstract class MixinI18n {
    @Shadow
    private static Locale i18nLocale;

    @SuppressWarnings("unused")
    @Unique @Public
    private static Map<String, String> getLocaleProperties() {
        return _acc_Locale_properties(i18nLocale);
    }

    @SuppressWarnings("unused")
    @Unique @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.resources.Locale field_135032_a Ljava.util.Map;", deobf = true)
    private static Map<String, String> _acc_Locale_properties(Locale locale){
        throw new AbstractMethodError();
    }
}
