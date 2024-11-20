/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Deprecated
 *  java.lang.Object
 *  java.lang.String
 *  net.minecraft.util.text.translation.LanguageMap
 */
package net.minecraft.util.text.translation;

import net.minecraft.util.text.translation.LanguageMap;

@Deprecated
public class I18n {
    private static final LanguageMap localizedName = LanguageMap.getInstance();
    private static final LanguageMap fallbackTranslator = new LanguageMap();

    @Deprecated
    public static String translateToLocal(String string) {
        return localizedName.translateKey(string);
    }

    @Deprecated
    public static String translateToLocalFormatted(String string, Object ... objectArray) {
        return localizedName.translateKeyFormat(string, objectArray);
    }

    @Deprecated
    public static String translateToFallback(String string) {
        return fallbackTranslator.translateKey(string);
    }

    @Deprecated
    public static boolean canTranslate(String string) {
        return localizedName.isKeyTranslated(string);
    }

    public static long getLastTranslationUpdateTimeInMilliseconds() {
        return localizedName.getLastUpdateTimeInMilliseconds();
    }
}
