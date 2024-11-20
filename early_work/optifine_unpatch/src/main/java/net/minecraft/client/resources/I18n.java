/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.client.resources.Locale
 */
package net.minecraft.client.resources;

import net.minecraft.client.resources.Locale;

public class I18n {
    private static Locale i18nLocale;

    static void setLocale(Locale locale) {
        i18nLocale = locale;
    }

    public static String format(String string, Object ... objectArray) {
        return i18nLocale.formatMessage(string, objectArray);
    }

    public static boolean hasKey(String string) {
        return i18nLocale.hasKey(string);
    }
}
