package rml.jrx.reflection.jvm;

import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PublicAPI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/3/31 10:23
 **/

@PublicAPI
@EarlyClass
public class MethodAccessor<RETURN, INSTANCE> {
    private final Method method;
    public MethodAccessor(Method method){
        method.setAccessible(true);
        this.method = method;
    }

    @SuppressWarnings("unchecked")
    public RETURN invoke(INSTANCE instance, Object... args){
        if (method.getReturnType()==void.class){
            try {
                method.invoke(instance, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            return null;
        }else {
            try {
                return (RETURN) method.invoke(instance, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Method getMethod() {
        return method;
    }
}
