package mods.Hileb.optirefine.library.common.utils;

import java.util.function.Function;
import java.util.function.Supplier;

public class Initializer {
    public static <T> T initialize(Supplier<T> t) {
        return t.get();
    }

    public static <T> T initializeSystemProperty(String key, Function<String, T> processor) {
        return processor.apply(System.getProperty(key));
    }

    public static <T> T initializeSystemProperty(String key, Function<String, T> processor, T defaultVal) {
        String pr = System.getProperty(key);
        if (pr != null) {
            return processor.apply(pr);
        } else return defaultVal;
    }

    public static <T> T initializeSystemProperty(String key, Function<String, T> processor, Supplier<T> defaultVal) {
        String pr = System.getProperty(key);
        if (pr != null) {
            return processor.apply(pr);
        } else return defaultVal.get();
    }
}
