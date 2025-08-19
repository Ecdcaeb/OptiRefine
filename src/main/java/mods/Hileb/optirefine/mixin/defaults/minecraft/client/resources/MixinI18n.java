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
/*
--- net/minecraft/client/resources/I18n.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/client/resources/I18n.java	Tue Aug 19 14:59:58 2025
@@ -1,8 +1,10 @@
 package net.minecraft.client.resources;

+import java.util.Map;
+
 public class I18n {
    private static Locale i18nLocale;

    static void setLocale(Locale var0) {
       i18nLocale = var0;
    }
@@ -10,8 +12,12 @@
    public static String format(String var0, Object... var1) {
       return i18nLocale.formatMessage(var0, var1);
    }

    public static boolean hasKey(String var0) {
       return i18nLocale.hasKey(var0);
+   }
+
+   public static Map getLocaleProperties() {
+      return i18nLocale.properties;
    }
 }
 */
