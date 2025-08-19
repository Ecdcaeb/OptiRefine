package mods.Hileb.optirefine.mixin.defaults.minecraft.crash;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.optifine.CrashReporter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrashReport.class)
public abstract class MixinCrashReport {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private boolean reported = false;

    @Shadow
    @Final
    private CrashReportCategory systemDetailsCategory;



    @Inject(method = "getCompleteReport", at = @At("HEAD"))
    public void injectGetCompleteReport(CallbackInfoReturnable<String> cir){
        if (!this.reported) {
            this.reported = true;
            CrashReporter.onCrashReport((CrashReport)(Object)this, this.systemDetailsCategory);
        }
    }

}
/*
--- net/minecraft/crash/CrashReport.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/crash/CrashReport.java	Tue Aug 19 14:59:58 2025
@@ -11,12 +11,14 @@
 import java.nio.charset.StandardCharsets;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.List;
 import net.minecraft.util.ReportedException;
 import net.minecraft.world.gen.layer.IntCache;
+import net.optifine.CrashReporter;
+import net.optifine.reflect.Reflector;
 import org.apache.commons.io.IOUtils;
 import org.apache.commons.lang3.ArrayUtils;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public class CrashReport {
@@ -25,12 +27,13 @@
    private final Throwable cause;
    private final CrashReportCategory systemDetailsCategory = new CrashReportCategory(this, "System Details");
    private final List<CrashReportCategory> crashReportSections = Lists.newArrayList();
    private File crashReportFile;
    private boolean firstCategoryInCrashReport = true;
    private StackTraceElement[] stacktrace = new StackTraceElement[0];
+   private boolean reported = false;

    public CrashReport(String var1, Throwable var2) {
       this.description = var1;
       this.cause = var2;
       this.populateEnvironment();
    }
@@ -90,12 +93,16 @@
       });
       this.systemDetailsCategory.addDetail("IntCache", new ICrashReportDetail<String>() {
          public String call() throws Exception {
             return IntCache.getCacheSizes();
          }
       });
+      if (Reflector.FMLCommonHandler_enhanceCrashReport.exists()) {
+         Object var1 = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
+         Reflector.callString(var1, Reflector.FMLCommonHandler_enhanceCrashReport, new Object[]{this, this.systemDetailsCategory});
+      }
    }

    public String getDescription() {
       return this.description;
    }

@@ -158,14 +165,20 @@
       }

       return var4;
    }

    public String getCompleteReport() {
+      if (!this.reported) {
+         this.reported = true;
+         CrashReporter.onCrashReport(this, this.systemDetailsCategory);
+      }
+
       StringBuilder var1 = new StringBuilder();
       var1.append("---- Minecraft Crash Report ----\n");
+      Reflector.call(Reflector.CoreModManager_onCrash, new Object[]{var1});
       var1.append("// ");
       var1.append(getWittyComment());
       var1.append("\n\n");
       var1.append("Time: ");
       var1.append(new SimpleDateFormat().format(new Date()));
       var1.append("\n");
@@ -195,26 +208,26 @@
          if (var1.getParentFile() != null) {
             var1.getParentFile().mkdirs();
          }

          OutputStreamWriter var2 = null;

-         boolean var4;
+         boolean var3;
          try {
             var2 = new OutputStreamWriter(new FileOutputStream(var1), StandardCharsets.UTF_8);
             var2.write(this.getCompleteReport());
             this.crashReportFile = var1;
             return true;
-         } catch (Throwable var8) {
-            LOGGER.error("Could not save crash report to {}", var1, var8);
-            var4 = false;
+         } catch (Throwable var9) {
+            LOGGER.error("Could not save crash report to {}", var1, var9);
+            var3 = false;
          } finally {
             IOUtils.closeQuietly(var2);
          }

-         return var4;
+         return var3;
       }
    }

    public CrashReportCategory getCategory() {
       return this.systemDetailsCategory;
    }
 */
