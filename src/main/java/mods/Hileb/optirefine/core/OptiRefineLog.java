package mods.Hileb.optirefine.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jline.utils.WriterOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class OptiRefineLog {
    public static final Logger log = LogManager.getLogger();

    public static final PrintStream logOut = new PrintStream(new WriterOutputStream(
            new Writer() {
                @Override
                public void write(char[] cbuf, int off, int len) {
                    char[] str = new char[len];
                    System.arraycopy(cbuf, off, str, 0, len);
                    log.info(String.valueOf(str));
                }

                @Override
                public void flush() {

                }

                @Override
                public void close() {
                }
            }, StandardCharsets.UTF_8
    ));

    public static void bigWarning(String format, Object... data) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        log.warn("****************************************");
        //noinspection StringConcatenationArgumentToLogCall
        log.warn("* " + format, data);

        for(int i = 2; i < 8 && i < trace.length; ++i) {
            log.warn("*  at {}{}", trace[i].toString(), i == 7 ? "..." : "");
        }

        log.warn("****************************************");
    }
}
