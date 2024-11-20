/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  net.minecraft.network.datasync.DataParameter
 */
package net.minecraft.network.datasync;

import net.minecraft.network.datasync.DataParameter;

public static class EntityDataManager.DataEntry<T> {
    private final DataParameter<T> key;
    private T value;
    private boolean dirty;

    public EntityDataManager.DataEntry(DataParameter<T> keyIn, T valueIn) {
        this.key = keyIn;
        this.value = valueIn;
        this.dirty = true;
    }

    public DataParameter<T> getKey() {
        return this.key;
    }

    public void setValue(T valueIn) {
        this.value = valueIn;
    }

    public T getValue() {
        return this.value;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean dirtyIn) {
        this.dirty = dirtyIn;
    }

    public EntityDataManager.DataEntry<T> copy() {
        return new EntityDataManager.DataEntry<Object>(this.key, this.key.getSerializer().copyValue(this.value));
    }

    static /* synthetic */ boolean access$002(EntityDataManager.DataEntry x0, boolean x1) {
        x0.dirty = x1;
        return x0.dirty;
    }
}
