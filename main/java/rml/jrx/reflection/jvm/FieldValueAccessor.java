package rml.jrx.reflection.jvm;

import rml.jrx.reflection.low.ValueAccessor;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/5/24 16:57
 **/
public class FieldValueAccessor<T, E> implements ValueAccessor<T> {
    public final FieldAccessor<T, E> accessor;
    public final E instance;
    public FieldValueAccessor(FieldAccessor<T, E> accessor, E instance){
        this.accessor = accessor;
        this.instance = instance;
    }

    @Override
    public T get() {
        return accessor.get(instance);
    }
}
