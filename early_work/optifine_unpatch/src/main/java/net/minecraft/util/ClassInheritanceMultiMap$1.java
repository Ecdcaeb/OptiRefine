/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Iterators
 *  java.lang.Class
 *  java.lang.Iterable
 *  java.lang.Object
 *  java.util.Collections
 *  java.util.Iterator
 *  java.util.List
 */
package net.minecraft.util;

import com.google.common.collect.Iterators;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class ClassInheritanceMultiMap.1
implements Iterable<S> {
    final /* synthetic */ Class val$clazz;

    ClassInheritanceMultiMap.1() {
        this.val$clazz = clazz;
    }

    public Iterator<S> iterator() {
        List list = (List)ClassInheritanceMultiMap.this.map.get((Object)ClassInheritanceMultiMap.this.initializeClassLookup(this.val$clazz));
        if (list == null) {
            return Collections.emptyIterator();
        }
        Iterator iterator = list.iterator();
        return Iterators.filter((Iterator)iterator, (Class)this.val$clazz);
    }
}
