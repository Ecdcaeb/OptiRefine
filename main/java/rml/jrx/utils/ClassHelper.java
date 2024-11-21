package rml.jrx.utils;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/7/30 10:41
 **/
public class ClassHelper {
    /**
     * Forces the initialization of the class pertaining to
     * the specified <tt>RClass</tt> object.
     * This method does nothing if the class is already
     * initialized prior to invocation.
     *
     * @param klass the class for which to force initialization
     * @return <tt>klass</tt>
     */
    public static <T> Class<T> forceInit(Class<T> klass) {
        try {
            Class.forName(klass.getName(), true, klass.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);  // Can't happen
        }
        return klass;
    }

    public static <T> Class<T> forceInitAll(Class<T> klass){
        if (klass.isArray()){
            forceInitAll(klass.getComponentType());
        }
        return forceInit(klass);
    }

    public static int getLineNumber(){
        int line = Thread.currentThread().getStackTrace()[2].getLineNumber();
        return Math.max(line, 0);
    }
}
