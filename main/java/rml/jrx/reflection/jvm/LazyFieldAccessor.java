package rml.jrx.reflection.jvm;


import rml.internal.net.minecraftforge.common.util.LazyOptional;

import java.lang.reflect.Field;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/5/24 16:45
 **/
public class LazyFieldAccessor<T, E> extends FieldAccessor<T, E> {
    public final String className;
    public final String fieldName;
    private LazyOptional<FieldAccessor<T, E>> wrapped;

    @SuppressWarnings("all")
    public LazyFieldAccessor(String clazz, String fieldName) {
        super(null);
        this.fieldName = fieldName;
        this.className = clazz;
        wrapped = LazyOptional.of(()->{
            try {
                return new FieldAccessor<>(Class.forName(LazyFieldAccessor.this.className).getField(LazyFieldAccessor.this.fieldName));
            } catch (NoSuchFieldException | ClassNotFoundException e) {
                return null;
            }
        });
    }

    @Override
    public void set(E instance, T value) {
        wrapped.orElseThrow(()-> new RuntimeException(new NoSuchFieldException("Could not found " + className + " "+ fieldName))).set(instance, value);
    }

    @Override
    public T get(E instance) {
        return wrapped.orElseThrow(()-> new RuntimeException(new NoSuchFieldException("Could not found " + className + " "+ fieldName))).get(instance);
    }

    @Override
    public Field getField() {
        return wrapped.orElseThrow(()-> new RuntimeException(new NoSuchFieldException("Could not found " + className + " "+ fieldName))).getField();
    }
}
