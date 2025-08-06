package mods.Hileb.optirefine.library.api;

import mods.Hileb.optirefine.core.OptiRefineLog;

public class DeprecatedHelper {
    public static boolean enabled = true;
    public static void deprecated(Class<?> cls, String symbol, String advice) {
        if (enabled) {
            OptiRefineLog.bigWarning(new DeprecatedException(cls.getName(), symbol, "never", advice).toString());
        }
    }
}
