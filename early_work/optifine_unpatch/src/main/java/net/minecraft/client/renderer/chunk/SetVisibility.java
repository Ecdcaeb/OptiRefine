/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.BitSet
 *  java.util.Set
 *  net.minecraft.util.EnumFacing
 */
package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import java.util.Set;
import net.minecraft.util.EnumFacing;

public class SetVisibility {
    private static final int COUNT_FACES = EnumFacing.values().length;
    private final BitSet bitSet = new BitSet(COUNT_FACES * COUNT_FACES);

    public void setManyVisible(Set<EnumFacing> set) {
        for (EnumFacing enumFacing : set) {
            for (EnumFacing enumFacing2 : set) {
                this.setVisible(enumFacing, enumFacing2, true);
            }
        }
    }

    public void setVisible(EnumFacing enumFacing, EnumFacing enumFacing2, boolean bl) {
        this.bitSet.set(enumFacing.ordinal() + enumFacing2.ordinal() * COUNT_FACES, bl);
        this.bitSet.set(enumFacing2.ordinal() + enumFacing.ordinal() * COUNT_FACES, bl);
    }

    public void setAllVisible(boolean bl) {
        this.bitSet.set(0, this.bitSet.size(), bl);
    }

    public boolean isVisible(EnumFacing enumFacing, EnumFacing enumFacing2) {
        return this.bitSet.get(enumFacing.ordinal() + enumFacing2.ordinal() * COUNT_FACES);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(' ');
        for (EnumFacing enumFacing : EnumFacing.values()) {
            stringBuilder.append(' ').append(enumFacing.toString().toUpperCase().charAt(0));
        }
        stringBuilder.append('\n');
        for (EnumFacing enumFacing : EnumFacing.values()) {
            stringBuilder.append(enumFacing.toString().toUpperCase().charAt(0));
            for (EnumFacing enumFacing2 : EnumFacing.values()) {
                if (enumFacing == enumFacing2) {
                    stringBuilder.append("  ");
                    continue;
                }
                boolean bl = this.isVisible(enumFacing, enumFacing2);
                stringBuilder.append(' ').append(bl ? (char)'Y' : 'n');
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }
}
