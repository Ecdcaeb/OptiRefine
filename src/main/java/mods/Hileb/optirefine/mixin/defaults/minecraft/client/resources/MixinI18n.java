package mods.Hileb.optirefine.mixin.defaults.minecraft.client.resources;

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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
public abstract class MixinI18n {
    @Shadow
    // [AUDIT-OK] baseline member i18nLocale (field_135054_a) exists in I18n
    private static Locale i18nLocale;

    @SuppressWarnings("unused")
    @Public
    // [AUDIT-OK] OF-added static method getLocaleProperties (private static + @Public per mixin rule); matches OF I18n.getLocaleProperties
    private static Map<String, String> getLocaleProperties() {
        return _acc_Locale_properties(i18nLocale);
    }

    @AccessibleOperation(opcode = Opcodes.GETFIELD, desc = "net.minecraft.client.resources.Locale field_135032_a Ljava.util.Map;", deobf = true)
    // [AUDIT-OK] vanilla SRG field_135032_a = Locale.properties, deobf=true (per contract)
    private static native Map<String, String> _acc_Locale_properties(Locale locale);
}
