/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 */
package net.minecraft.util;

public class IntegerCache {
    private static final Integer[] CACHE = new Integer[65535];

    public static Integer getInteger(int n) {
        if (n > 0 && n < CACHE.length) {
            return CACHE[n];
        }
        return n;
    }

    static {
        int n = CACHE.length;
        for (\u2603 = 0; \u2603 < n; ++\u2603) {
            IntegerCache.CACHE[\u2603] = \u2603;
        }
    }
}
