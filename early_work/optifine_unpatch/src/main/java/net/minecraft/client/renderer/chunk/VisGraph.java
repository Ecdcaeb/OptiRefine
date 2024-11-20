/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Queues
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.util.ArrayDeque
 *  java.util.BitSet
 *  java.util.EnumSet
 *  java.util.Set
 *  net.minecraft.client.renderer.chunk.SetVisibility
 *  net.minecraft.client.renderer.chunk.VisGraph$1
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.IntegerCache
 *  net.minecraft.util.math.BlockPos
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Queues;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IntegerCache;
import net.minecraft.util.math.BlockPos;

public class VisGraph {
    private static final int DX = (int)Math.pow((double)16.0, (double)0.0);
    private static final int DZ = (int)Math.pow((double)16.0, (double)1.0);
    private static final int DY = (int)Math.pow((double)16.0, (double)2.0);
    private final BitSet bitSet = new BitSet(4096);
    private static final int[] INDEX_OF_EDGES = new int[1352];
    private int empty = 4096;

    public void setOpaqueCube(BlockPos blockPos) {
        this.bitSet.set(VisGraph.getIndex(blockPos), true);
        --this.empty;
    }

    private static int getIndex(BlockPos blockPos) {
        return VisGraph.getIndex(blockPos.p() & 0xF, blockPos.q() & 0xF, blockPos.r() & 0xF);
    }

    private static int getIndex(int n, int n2, int n3) {
        return n << 0 | n2 << 8 | n3 << 4;
    }

    public SetVisibility computeVisibility() {
        SetVisibility setVisibility = new SetVisibility();
        if (4096 - this.empty < 256) {
            setVisibility.setAllVisible(true);
        } else if (this.empty == 0) {
            setVisibility.setAllVisible(false);
        } else {
            for (int n : INDEX_OF_EDGES) {
                if (this.bitSet.get(n)) continue;
                setVisibility.setManyVisible(this.floodFill(n));
            }
        }
        return setVisibility;
    }

    public Set<EnumFacing> getVisibleFacings(BlockPos blockPos) {
        return this.floodFill(VisGraph.getIndex(blockPos));
    }

    private Set<EnumFacing> floodFill(int n) {
        EnumSet enumSet = EnumSet.noneOf(EnumFacing.class);
        ArrayDeque \u26032 = Queues.newArrayDeque();
        \u26032.add((Object)IntegerCache.getInteger((int)n));
        this.bitSet.set(n, true);
        while (!\u26032.isEmpty()) {
            int n2 = (Integer)\u26032.poll();
            this.addEdges(n2, (Set<EnumFacing>)enumSet);
            for (EnumFacing enumFacing : EnumFacing.values()) {
                int n3 = this.getNeighborIndexAtFace(n2, enumFacing);
                if (n3 < 0 || this.bitSet.get(n3)) continue;
                this.bitSet.set(n3, true);
                \u26032.add((Object)IntegerCache.getInteger((int)n3));
            }
        }
        return enumSet;
    }

    private void addEdges(int n, Set<EnumFacing> set) {
        int n2 = n >> 0 & 0xF;
        if (n2 == 0) {
            set.add((Object)EnumFacing.WEST);
        } else if (n2 == 15) {
            set.add((Object)EnumFacing.EAST);
        }
        \u2603 = n >> 8 & 0xF;
        if (\u2603 == 0) {
            set.add((Object)EnumFacing.DOWN);
        } else if (\u2603 == 15) {
            set.add((Object)EnumFacing.UP);
        }
        \u2603 = n >> 4 & 0xF;
        if (\u2603 == 0) {
            set.add((Object)EnumFacing.NORTH);
        } else if (\u2603 == 15) {
            set.add((Object)EnumFacing.SOUTH);
        }
    }

    private int getNeighborIndexAtFace(int n, EnumFacing enumFacing) {
        switch (1.field_178617_a[enumFacing.ordinal()]) {
            case 1: {
                if ((n >> 8 & 0xF) == 0) {
                    return -1;
                }
                return n - DY;
            }
            case 2: {
                if ((n >> 8 & 0xF) == 15) {
                    return -1;
                }
                return n + DY;
            }
            case 3: {
                if ((n >> 4 & 0xF) == 0) {
                    return -1;
                }
                return n - DZ;
            }
            case 4: {
                if ((n >> 4 & 0xF) == 15) {
                    return -1;
                }
                return n + DZ;
            }
            case 5: {
                if ((n >> 0 & 0xF) == 0) {
                    return -1;
                }
                return n - DX;
            }
            case 6: {
                if ((n >> 0 & 0xF) == 15) {
                    return -1;
                }
                return n + DX;
            }
        }
        return -1;
    }

    static {
        boolean bl = false;
        int \u26032 = 15;
        int \u26033 = 0;
        for (int i = 0; i < 16; ++i) {
            for (\u2603 = 0; \u2603 < 16; ++\u2603) {
                for (\u2603 = 0; \u2603 < 16; ++\u2603) {
                    if (i != 0 && i != 15 && \u2603 != 0 && \u2603 != 15 && \u2603 != 0 && \u2603 != 15) continue;
                    VisGraph.INDEX_OF_EDGES[\u26033++] = VisGraph.getIndex(i, \u2603, \u2603);
                }
            }
        }
    }
}
