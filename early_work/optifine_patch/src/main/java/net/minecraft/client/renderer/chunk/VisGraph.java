/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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

    public void setOpaqueCube(BlockPos pos) {
        this.bitSet.set(VisGraph.getIndex(pos), true);
        --this.empty;
    }

    private static int getIndex(BlockPos pos) {
        return VisGraph.getIndex(pos.p() & 0xF, pos.q() & 0xF, pos.r() & 0xF);
    }

    private static int getIndex(int x, int y, int z) {
        return x << 0 | y << 8 | z << 4;
    }

    public SetVisibility computeVisibility() {
        SetVisibility setvisibility = new SetVisibility();
        if (4096 - this.empty < 256) {
            setvisibility.setAllVisible(true);
        } else if (this.empty == 0) {
            setvisibility.setAllVisible(false);
        } else {
            for (int i : INDEX_OF_EDGES) {
                if (this.bitSet.get(i)) continue;
                setvisibility.setManyVisible(this.floodFill(i));
            }
        }
        return setvisibility;
    }

    public Set<EnumFacing> getVisibleFacings(BlockPos pos) {
        return this.floodFill(VisGraph.getIndex(pos));
    }

    private Set<EnumFacing> floodFill(int pos) {
        EnumSet set = EnumSet.noneOf(EnumFacing.class);
        ArrayDeque queue = new ArrayDeque(384);
        queue.add((Object)IntegerCache.getInteger((int)pos));
        this.bitSet.set(pos, true);
        while (!queue.isEmpty()) {
            int i = (Integer)queue.poll();
            this.addEdges(i, (Set<EnumFacing>)set);
            for (EnumFacing enumfacing : EnumFacing.VALUES) {
                int j = this.getNeighborIndexAtFace(i, enumfacing);
                if (j < 0 || this.bitSet.get(j)) continue;
                this.bitSet.set(j, true);
                queue.add((Object)IntegerCache.getInteger((int)j));
            }
        }
        return set;
    }

    private void addEdges(int pos, Set<EnumFacing> p_178610_2_) {
        int i = pos >> 0 & 0xF;
        if (i == 0) {
            p_178610_2_.add((Object)EnumFacing.WEST);
        } else if (i == 15) {
            p_178610_2_.add((Object)EnumFacing.EAST);
        }
        int j = pos >> 8 & 0xF;
        if (j == 0) {
            p_178610_2_.add((Object)EnumFacing.DOWN);
        } else if (j == 15) {
            p_178610_2_.add((Object)EnumFacing.UP);
        }
        int k = pos >> 4 & 0xF;
        if (k == 0) {
            p_178610_2_.add((Object)EnumFacing.NORTH);
        } else if (k == 15) {
            p_178610_2_.add((Object)EnumFacing.SOUTH);
        }
    }

    private int getNeighborIndexAtFace(int pos, EnumFacing facing) {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[facing.ordinal()]) {
            case 1: {
                if ((pos >> 8 & 0xF) == 0) {
                    return -1;
                }
                return pos - DY;
            }
            case 2: {
                if ((pos >> 8 & 0xF) == 15) {
                    return -1;
                }
                return pos + DY;
            }
            case 3: {
                if ((pos >> 4 & 0xF) == 0) {
                    return -1;
                }
                return pos - DZ;
            }
            case 4: {
                if ((pos >> 4 & 0xF) == 15) {
                    return -1;
                }
                return pos + DZ;
            }
            case 5: {
                if ((pos >> 0 & 0xF) == 0) {
                    return -1;
                }
                return pos - DX;
            }
            case 6: {
                if ((pos >> 0 & 0xF) == 15) {
                    return -1;
                }
                return pos + DX;
            }
        }
        return -1;
    }

    static {
        boolean i = false;
        int j = 15;
        int k = 0;
        for (int l = 0; l < 16; ++l) {
            for (int i1 = 0; i1 < 16; ++i1) {
                for (int j1 = 0; j1 < 16; ++j1) {
                    if (l != 0 && l != 15 && i1 != 0 && i1 != 15 && j1 != 0 && j1 != 15) continue;
                    VisGraph.INDEX_OF_EDGES[k++] = VisGraph.getIndex(l, i1, j1);
                }
            }
        }
    }
}
