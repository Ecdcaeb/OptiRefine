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

    public static Integer getInteger(int value) {
        if (value >= 0 && value < CACHE.length) {
            return CACHE[value];
        }
        return new Integer(value);
    }

    static {
        int j = CACHE.length;
        for (int i = 0; i < j; ++i) {
            IntegerCache.CACHE[i] = i;
        }
    }
}
