/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Iterable
 *  java.lang.Object
 *  java.lang.String
 *  java.util.AbstractSet
 *  java.util.Collections
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  java.util.concurrent.ConcurrentHashMap
 */
package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClassInheritanceMultiMap<T>
extends AbstractSet<T> {
    private static final Set<Class<?>> ALL_KNOWN = Collections.newSetFromMap((Map)new ConcurrentHashMap());
    private final Map<Class<?>, List<T>> map = Maps.newHashMap();
    private final Set<Class<?>> knownKeys = Sets.newIdentityHashSet();
    private final Class<T> baseClass;
    private final List<T> values = Lists.newArrayList();

    public ClassInheritanceMultiMap(Class<T> baseClassIn) {
        this.baseClass = baseClassIn;
        this.knownKeys.add(baseClassIn);
        this.map.put(baseClassIn, this.values);
        for (Class oclass : ALL_KNOWN) {
            this.createLookup(oclass);
        }
    }

    protected void createLookup(Class<?> clazz) {
        ALL_KNOWN.add(clazz);
        for (Object t : this.values) {
            if (!clazz.isAssignableFrom(t.getClass())) continue;
            this.addForClass(t, clazz);
        }
        this.knownKeys.add(clazz);
    }

    protected Class<?> initializeClassLookup(Class<?> clazz) {
        if (this.baseClass.isAssignableFrom(clazz)) {
            if (!this.knownKeys.contains(clazz)) {
                this.createLookup(clazz);
            }
            return clazz;
        }
        throw new IllegalArgumentException("Don't know how to search for " + String.valueOf(clazz));
    }

    public boolean add(T p_add_1_) {
        for (Class oclass : this.knownKeys) {
            if (!oclass.isAssignableFrom(p_add_1_.getClass())) continue;
            this.addForClass(p_add_1_, oclass);
        }
        return true;
    }

    private void addForClass(T value, Class<?> parentClass) {
        List list = (List)this.map.get(parentClass);
        if (list == null) {
            this.map.put(parentClass, (Object)Lists.newArrayList((Object[])new Object[]{value}));
        } else {
            list.add(value);
        }
    }

    public boolean remove(Object p_remove_1_) {
        Object t = p_remove_1_;
        boolean flag = false;
        for (Class oclass : this.knownKeys) {
            List list;
            if (!oclass.isAssignableFrom(t.getClass()) || (list = (List)this.map.get((Object)oclass)) == null || !list.remove(t)) continue;
            flag = true;
        }
        return flag;
    }

    public boolean contains(Object p_contains_1_) {
        return Iterators.contains((Iterator)this.getByClass(p_contains_1_.getClass()).iterator(), (Object)p_contains_1_);
    }

    public <S> Iterable<S> getByClass(Class<S> clazz) {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public Iterator<T> iterator() {
        return this.values.isEmpty() ? Collections.emptyIterator() : Iterators.unmodifiableIterator((Iterator)this.values.iterator());
    }

    public int size() {
        return this.values.size();
    }
}
