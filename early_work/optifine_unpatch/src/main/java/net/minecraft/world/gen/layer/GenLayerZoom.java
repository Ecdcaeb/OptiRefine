/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  net.minecraft.world.gen.layer.GenLayer
 *  net.minecraft.world.gen.layer.IntCache
 */
package net.minecraft.world.gen.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerZoom
extends GenLayer {
    public GenLayerZoom(long l, GenLayer genLayer) {
        super(l);
        this.parent = genLayer;
    }

    public int[] getInts(int n, int n2, int n32, int n4) {
        int n32;
        \u2603 = n >> 1;
        \u2603 = n2 >> 1;
        \u2603 = (n32 >> 1) + 2;
        \u2603 = (n4 >> 1) + 2;
        int[] nArray = this.parent.getInts(\u2603, \u2603, \u2603, \u2603);
        int \u26032 = \u2603 - 1 << 1;
        int \u26033 = \u2603 - 1 << 1;
        \u2603 = IntCache.getIntCache((int)(\u26032 * \u26033));
        for (int i = 0; i < \u2603 - 1; ++i) {
            \u2603 = (i << 1) * \u26032;
            \u2603 = nArray[\u2603 + 0 + (i + 0) * \u2603];
            \u2603 = nArray[\u2603 + 0 + (i + 1) * \u2603];
            for (\u2603 = 0; \u2603 < \u2603 - 1; ++\u2603) {
                this.initChunkSeed(\u2603 + \u2603 << 1, i + \u2603 << 1);
                \u2603 = nArray[\u2603 + 1 + (i + 0) * \u2603];
                \u2603 = nArray[\u2603 + 1 + (i + 1) * \u2603];
                \u2603[\u2603] = \u2603;
                \u2603[\u2603++ + \u26032] = this.selectRandom(new int[]{\u2603, \u2603});
                \u2603[\u2603] = this.selectRandom(new int[]{\u2603, \u2603});
                \u2603[\u2603++ + \u26032] = this.selectModeOrRandom(\u2603, \u2603, \u2603, \u2603);
                \u2603 = \u2603;
                \u2603 = \u2603;
            }
        }
        int[] \u26034 = IntCache.getIntCache((int)(n32 * n4));
        for (\u2603 = 0; \u2603 < n4; ++\u2603) {
            System.arraycopy((Object)\u2603, (int)((\u2603 + (n2 & 1)) * \u26032 + (n & 1)), (Object)\u26034, (int)(\u2603 * n32), (int)n32);
        }
        return \u26034;
    }

    public static GenLayer magnify(long l, GenLayer genLayer, int n) {
        GenLayer genLayer2 = genLayer;
        for (int i = 0; i < n; ++i) {
            genLayer2 = new GenLayerZoom(l + (long)i, genLayer2);
        }
        return genLayer2;
    }
}
