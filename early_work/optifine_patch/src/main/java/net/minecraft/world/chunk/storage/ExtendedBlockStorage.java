/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.world.chunk.BlockStateContainer
 *  net.minecraft.world.chunk.NibbleArray
 *  net.optifine.reflect.Reflector
 *  net.optifine.reflect.ReflectorMethod
 */
package net.minecraft.world.chunk.storage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.NibbleArray;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorMethod;

public class ExtendedBlockStorage {
    private final int yBase;
    private int blockRefCount;
    private int tickRefCount;
    private final BlockStateContainer data;
    private NibbleArray blockLight;
    private NibbleArray skyLight;

    public ExtendedBlockStorage(int y, boolean storeSkylight) {
        this.yBase = y;
        this.data = new BlockStateContainer();
        this.blockLight = new NibbleArray();
        if (storeSkylight) {
            this.skyLight = new NibbleArray();
        }
    }

    public IBlockState get(int x, int y, int z) {
        return this.data.get(x, y, z);
    }

    public void set(int x, int y, int z, IBlockState state) {
        if (Reflector.IExtendedBlockState.isInstance((Object)state)) {
            state = (IBlockState)Reflector.call((Object)state, (ReflectorMethod)Reflector.IExtendedBlockState_getClean, (Object[])new Object[0]);
        }
        IBlockState iblockstate = this.get(x, y, z);
        Block block = iblockstate.getBlock();
        Block block1 = state.getBlock();
        if (block != Blocks.AIR) {
            --this.blockRefCount;
            if (block.getTickRandomly()) {
                --this.tickRefCount;
            }
        }
        if (block1 != Blocks.AIR) {
            ++this.blockRefCount;
            if (block1.getTickRandomly()) {
                ++this.tickRefCount;
            }
        }
        this.data.set(x, y, z, state);
    }

    public boolean isEmpty() {
        return this.blockRefCount == 0;
    }

    public boolean needsRandomTick() {
        return this.tickRefCount > 0;
    }

    public int getYLocation() {
        return this.yBase;
    }

    public void setSkyLight(int x, int y, int z, int value) {
        this.skyLight.set(x, y, z, value);
    }

    public int getSkyLight(int x, int y, int z) {
        return this.skyLight.get(x, y, z);
    }

    public void setBlockLight(int x, int y, int z, int value) {
        this.blockLight.set(x, y, z, value);
    }

    public int getBlockLight(int x, int y, int z) {
        return this.blockLight.get(x, y, z);
    }

    public void recalculateRefCounts() {
        IBlockState STATE_AIR = Blocks.AIR.getDefaultState();
        int localBlockRefCount = 0;
        int localTickRefCount = 0;
        for (int y = 0; y < 16; ++y) {
            for (int z = 0; z < 16; ++z) {
                for (int x = 0; x < 16; ++x) {
                    IBlockState bs = this.data.get(x, y, z);
                    if (bs == STATE_AIR) continue;
                    ++localBlockRefCount;
                    Block block = bs.getBlock();
                    if (!block.getTickRandomly()) continue;
                    ++localTickRefCount;
                }
            }
        }
        this.blockRefCount = localBlockRefCount;
        this.tickRefCount = localTickRefCount;
    }

    public BlockStateContainer getData() {
        return this.data;
    }

    public NibbleArray getBlockLight() {
        return this.blockLight;
    }

    public NibbleArray getSkyLight() {
        return this.skyLight;
    }

    public void setBlockLight(NibbleArray newBlocklightArray) {
        this.blockLight = newBlocklightArray;
    }

    public void setSkyLight(NibbleArray newSkylightArray) {
        this.skyLight = newSkylightArray;
    }

    public int getBlockRefCount() {
        return this.blockRefCount;
    }
}
