package rml.jrx.reflection.low;

import rml.jrx.announces.EarlyClass;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/5/24 16:41
 **/

@EarlyClass
public interface ValueAccessor<T> {
    T get();
}
