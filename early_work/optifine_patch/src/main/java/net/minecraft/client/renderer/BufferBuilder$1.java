/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.primitives.Floats
 *  java.lang.Integer
 *  java.lang.Object
 *  java.util.Comparator
 */
package net.minecraft.client.renderer;

import com.google.common.primitives.Floats;
import java.util.Comparator;

class BufferBuilder.1
implements Comparator<Integer> {
    final /* synthetic */ float[] val$afloat;

    BufferBuilder.1(float[] fArray) {
        this.val$afloat = fArray;
    }

    public int compare(Integer p_compare_1_, Integer p_compare_2_) {
        return Floats.compare((float)this.val$afloat[p_compare_2_], (float)this.val$afloat[p_compare_1_]);
    }
}
