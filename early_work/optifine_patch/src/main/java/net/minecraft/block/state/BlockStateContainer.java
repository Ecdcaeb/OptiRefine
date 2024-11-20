/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.base.MoreObjects
 *  com.google.common.collect.ImmutableCollection
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableSortedMap
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  java.lang.CharSequence
 *  java.lang.Comparable
 *  java.lang.IllegalArgumentException
 *  java.lang.Iterable
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.LinkedHashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Optional
 *  java.util.regex.Pattern
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.BlockStateContainer$StateImplementation
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.MapPopulator
 *  net.minecraft.util.math.Cartesian
 *  net.minecraftforge.common.property.IUnlistedProperty
 */
package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.MapPopulator;
import net.minecraft.util.math.Cartesian;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockStateContainer {
    private static final Pattern NAME_PATTERN = Pattern.compile((String)"^[a-z0-9_]+$");
    private static final Function<IProperty<?>, String> GET_NAME_FUNC = new /* Unavailable Anonymous Inner Class!! */;
    private final Block block;
    private final ImmutableSortedMap<String, IProperty<?>> properties;
    private final ImmutableList<IBlockState> validStates;

    public BlockStateContainer(Block blockIn, IProperty<?> ... properties) {
        this(blockIn, properties, (ImmutableMap<IUnlistedProperty<?>, Optional<?>>)null);
    }

    protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, @Nullable ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
        return new StateImplementation(block, properties, null);
    }

    protected BlockStateContainer(Block blockIn, IProperty<?>[] properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties) {
        this.block = blockIn;
        HashMap map = Maps.newHashMap();
        for (IProperty<?> iproperty : properties) {
            BlockStateContainer.validateProperty(blockIn, iproperty);
            map.put((Object)iproperty.getName(), iproperty);
        }
        this.properties = ImmutableSortedMap.copyOf((Map)map);
        LinkedHashMap map2 = Maps.newLinkedHashMap();
        ArrayList list1 = Lists.newArrayList();
        for (List list : Cartesian.cartesianProduct(this.getAllowedValues())) {
            Map map1 = MapPopulator.createMap((Iterable)this.properties.values(), (Iterable)list);
            StateImplementation blockstatecontainer$stateimplementation = this.createState(blockIn, ImmutableMap.copyOf((Map)map1), unlistedProperties);
            map2.put((Object)map1, (Object)blockstatecontainer$stateimplementation);
            list1.add((Object)blockstatecontainer$stateimplementation);
        }
        for (StateImplementation blockstatecontainer$stateimplementation1 : list1) {
            blockstatecontainer$stateimplementation1.buildPropertyValueTable((Map)map2);
        }
        this.validStates = ImmutableList.copyOf((Collection)list1);
    }

    public static <T extends Comparable<T>> String validateProperty(Block block, IProperty<T> property) {
        String s = property.getName();
        if (!NAME_PATTERN.matcher((CharSequence)s).matches()) {
            throw new IllegalArgumentException("Block: " + block.getClass() + " has invalidly named property: " + s);
        }
        for (Comparable t : property.getAllowedValues()) {
            String s1 = property.getName(t);
            if (NAME_PATTERN.matcher((CharSequence)s1).matches()) continue;
            throw new IllegalArgumentException("Block: " + block.getClass() + " has property: " + s + " with invalidly named value: " + s1);
        }
        return s;
    }

    public ImmutableList<IBlockState> getValidStates() {
        return this.validStates;
    }

    private List<Iterable<Comparable<?>>> getAllowedValues() {
        ArrayList list = Lists.newArrayList();
        ImmutableCollection immutablecollection = this.properties.values();
        for (IProperty iproperty : immutablecollection) {
            list.add((Object)iproperty.getAllowedValues());
        }
        return list;
    }

    public IBlockState getBaseState() {
        return (IBlockState)this.validStates.get(0);
    }

    public Block getBlock() {
        return this.block;
    }

    public Collection<IProperty<?>> getProperties() {
        return this.properties.values();
    }

    public String toString() {
        return MoreObjects.toStringHelper((Object)this).add("block", Block.REGISTRY.getNameForObject((Object)this.block)).add("properties", (Object)Iterables.transform((Iterable)this.properties.values(), GET_NAME_FUNC)).toString();
    }

    @Nullable
    public IProperty<?> getProperty(String propertyName) {
        return (IProperty)this.properties.get((Object)propertyName);
    }
}
