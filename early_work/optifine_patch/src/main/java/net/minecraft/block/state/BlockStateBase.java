/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.base.Joiner
 *  com.google.common.collect.ImmutableTable
 *  com.google.common.collect.Iterables
 *  java.lang.Comparable
 *  java.lang.Iterable
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.Map$Entry
 *  net.minecraft.block.Block
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.ResourceLocation
 */
package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public abstract class BlockStateBase
implements IBlockState {
    private static final Joiner COMMA_JOINER = Joiner.on((char)',');
    private static final Function<Map.Entry<IProperty<?>, Comparable<?>>, String> MAP_ENTRY_TO_STRING = new /* Unavailable Anonymous Inner Class!! */;
    private int blockId = -1;
    private int blockStateId = -1;
    private int metadata = -1;
    private ResourceLocation blockLocation = null;

    public int getBlockId() {
        if (this.blockId < 0) {
            this.blockId = Block.getIdFromBlock((Block)this.u());
        }
        return this.blockId;
    }

    public int getBlockStateId() {
        if (this.blockStateId < 0) {
            this.blockStateId = Block.getStateId((IBlockState)this);
        }
        return this.blockStateId;
    }

    public int getMetadata() {
        if (this.metadata < 0) {
            this.metadata = this.u().getMetaFromState((IBlockState)this);
        }
        return this.metadata;
    }

    public ResourceLocation getBlockLocation() {
        if (this.blockLocation == null) {
            this.blockLocation = (ResourceLocation)Block.REGISTRY.getNameForObject((Object)this.u());
        }
        return this.blockLocation;
    }

    public ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> getPropertyValueTable() {
        return null;
    }

    public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property) {
        return this.a(property, BlockStateBase.cyclePropertyValue(property.getAllowedValues(), this.c(property)));
    }

    protected static <T> T cyclePropertyValue(Collection<T> values, T currentValue) {
        Iterator iterator = values.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().equals(currentValue)) continue;
            if (iterator.hasNext()) {
                return (T)iterator.next();
            }
            return (T)values.iterator().next();
        }
        return (T)iterator.next();
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(Block.REGISTRY.getNameForObject((Object)this.u()));
        if (!this.t().isEmpty()) {
            stringbuilder.append("[");
            COMMA_JOINER.appendTo(stringbuilder, Iterables.transform((Iterable)this.t().entrySet(), MAP_ENTRY_TO_STRING));
            stringbuilder.append("]");
        }
        return stringbuilder.toString();
    }
}
