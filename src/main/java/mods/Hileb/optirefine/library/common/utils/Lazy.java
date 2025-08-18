package mods.Hileb.optirefine.library.common.utils;

import java.util.function.Supplier;

public final class Lazy<T> implements Supplier<T>{
    private T instance = null;
    private Supplier<T> supplier = null;

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    private Lazy(T instance) {
        this.instance = instance;
    }

    public boolean isInitialized() {
        return this.instance != null;
    }

    public boolean isEmpty() {
        return this.instance == null && this.supplier == null;
    }

    @Override
    public T get(){
        if (!this.isInitialized()) {
            if (this.supplier != null) this.instance = supplier.get();
        }
        return this.instance;
    }

    public static<E> Lazy<E> of(E instance) {
        return new Lazy<>(instance);
    }

    public static<E> Lazy<E> of(Supplier<E> instance) {
        return new Lazy<>(instance);
    }

}
