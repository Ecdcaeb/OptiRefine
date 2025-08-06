package mods.Hileb.optirefine.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OptiRefineLog {
    public static final Logger log = LogManager.getLogger();

    public static void bigWarning(String format, Object... data) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        log.warn("****************************************");
        log.warn("* {}", format, data);

        for(int i = 2; i < 8 && i < trace.length; ++i) {
            log.warn("*  at {}{}", trace[i].toString(), i == 7 ? "..." : "");
        }

        log.warn("****************************************");
    }
}
