package mods.Hileb.optirefine.validation;

import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

final class CrashReportContracts {
    private CrashReportContracts() {}

    static void verify(Minecraft mc) throws Exception {
        FutureTask<String> details = new FutureTask<>(() -> graphicsDetails(mc));
        Thread worker = new Thread(details, "Crash report contract worker");
        worker.setDaemon(true);
        worker.start();
        String workerReport = details.get(5, TimeUnit.SECONDS);
        if (!workerReport.contains("OpenGL: Unavailable (no OpenGL context on this thread)")
                || !workerReport.contains("GL Caps:")) {
            throw new AssertionError("Missing worker crash details: " + workerReport);
        }

        String renderReport = graphicsDetails(mc);
        if (!renderReport.contains("OpenGL: " + GL11.glGetString(GL11.GL_RENDERER))
                || !renderReport.contains(GL11.glGetString(GL11.GL_VERSION))) {
            throw new AssertionError("Missing active OpenGL renderer and version: " + renderReport);
        }
    }

    private static String graphicsDetails(Minecraft mc) {
        Throwable cause = new IllegalStateException("Chunk batching failed");
        CrashReport report = mc.addGraphicsAndWorldToCrashReport(CrashReport.makeCrashReport(cause, "Batching chunks"));
        if (report.getCrashCause() != cause) throw new AssertionError("Lost the original chunk batching error");
        StringBuilder details = new StringBuilder();
        report.getCategory().appendToStringBuilder(details);
        return details.toString();
    }
}
