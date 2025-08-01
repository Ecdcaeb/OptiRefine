package mods.Hileb.optirefine.library.api;

public class DeprecatedHelper {
    public static boolean enabled = true;
    public static void deprecated(Class<?> cls, String symbol, String advice) {
        if (enabled) {
            new DeprecatedException(cls.getName(), symbol, "never", advice).printStackTrace();
        }
    }
}
