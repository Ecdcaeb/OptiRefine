package rml.jrx.reflection.jvm;

import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PublicAPI;

import java.lang.reflect.Field;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/3/31 10:23
 **/

@PublicAPI
@EarlyClass
@SuppressWarnings("all")
public class FieldAccessor<T, E> {
    private final Field field;
    public FieldAccessor(Field field){
        field.setAccessible(true);
        this.field = field;
    }

    public T get(E instance){
        try {
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void set(E instance, T value){
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Field getField() {
        return field;
    }
}
