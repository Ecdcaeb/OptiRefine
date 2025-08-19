package mods.Hileb.optirefine.mixin.defaults.minecraft.profiler;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.library.common.utils.Checked;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.profiler.Profiler;
import net.optifine.Lagometer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Checked
@Mixin(Profiler.class)
public abstract class MixinProfiler {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    public boolean profilerGlobalEnabled = true;
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private boolean profilerLocalEnabled = this.profilerGlobalEnabled;
    @SuppressWarnings("unused")
    @Unique
    private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    private static final String TICK = "tick";
    @SuppressWarnings("unused")
    @Unique
    private static final String PRE_RENDER_ERRORS = "preRenderErrors";
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    private static final String RENDER = "render";
    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    private static final String DISPLAY = "display";
    @Unique
    private static final int HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
    @Unique
    private static final int HASH_TICK = "tick".hashCode();
    @Unique
    private static final int HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
    @Unique
    private static final int HASH_RENDER = "render".hashCode();
    @Unique
    private static final int HASH_DISPLAY = "display".hashCode();

    @Inject(method = "clearProfiling", at = @At("RETURN"))
    public void injectClearProfiling(CallbackInfo ci){
        this.profilerLocalEnabled = this.profilerGlobalEnabled;
    }

    @Inject(method = "startSection(Ljava/lang/String;)V", at = @At("HEAD"))
    public void injectStartSection(String name, CallbackInfo ci){
        if (Lagometer.isActive()) {
            int hashName = name.hashCode();
            if (hashName == HASH_SCHEDULED_EXECUTABLES && name.equals("scheduledExecutables")) {
                Lagometer.timerScheduledExecutables.start();
            } else if (hashName == HASH_TICK && name.equals("tick") && Config.isMinecraftThread()) {
                Lagometer.timerScheduledExecutables.end();
                Lagometer.timerTick.start();
            } else if (hashName == HASH_PRE_RENDER_ERRORS && name.equals("preRenderErrors")) {
                Lagometer.timerTick.end();
            }
        }

        if (Config.isFastRender()) {
            int hashName = name.hashCode();
            if (hashName == HASH_RENDER && name.equals("render")) {
                _set_GlStateManager_clearEnabled(false);
            } else if (hashName == HASH_DISPLAY && name.equals("display")) {
                _set_GlStateManager_clearEnabled(true);
            }
        }
    }


    @WrapMethod(method = "func_194340_a")
    public void inject_func_194340_a(Supplier<String> p_194340_1_, Operation<Void> original){
        if (this.profilerLocalEnabled) {
            original.call(p_194340_1_);
        }
    }

    @WrapMethod(method = "endSection")
    public void injectEndSection(Operation<Void> original){
        if (profilerGlobalEnabled) original.call();
    }

    @WrapMethod(method = "getProfilingData")
    public List<Profiler.Result> injectGetProfilingData(String p_76321_1_, Operation<List<Profiler.Result>> original){
        if (profilerGlobalEnabled) return original.call(p_76321_1_);
        else return Collections.emptyList();
    }

    @WrapMethod(method = "endStartSection")
    public void injectEndStartSection(String p_76318_1_, Operation<Void> original){
        if (this.profilerLocalEnabled) {
            original.call(p_76318_1_);
        }
    }

    @WrapMethod(method = "func_194339_b")
    public void inject_func_194339_b(Supplier<String> p_194339_1_, Operation<Void> original) {
        if (this.profilerLocalEnabled) {
            original.call(p_194339_1_);
        }
    }

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.PUTSTATIC, desc = "net/minecraft/client/renderer/GlStateManager clearEnabled Z")
    private static void _set_GlStateManager_clearEnabled(boolean val){
        throw new AbstractMethodError();
    }
}
/*
--- net/minecraft/profiler/Profiler.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/profiler/Profiler.java	Tue Aug 19 14:59:58 2025
@@ -4,132 +4,183 @@
 import com.google.common.collect.Maps;
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.List;
 import java.util.Map;
 import java.util.function.Supplier;
+import net.minecraft.client.renderer.GlStateManager;
+import net.optifine.Lagometer;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;

 public class Profiler {
    private static final Logger LOGGER = LogManager.getLogger();
    private final List<String> sectionList = Lists.newArrayList();
    private final List<Long> timestampList = Lists.newArrayList();
    public boolean profilingEnabled;
    private String profilingSection = "";
    private final Map<String, Long> profilingMap = Maps.newHashMap();
+   public boolean profilerGlobalEnabled = true;
+   private boolean profilerLocalEnabled = this.profilerGlobalEnabled;
+   private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
+   private static final String TICK = "tick";
+   private static final String PRE_RENDER_ERRORS = "preRenderErrors";
+   private static final String RENDER = "render";
+   private static final String DISPLAY = "display";
+   private static final int HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
+   private static final int HASH_TICK = "tick".hashCode();
+   private static final int HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
+   private static final int HASH_RENDER = "render".hashCode();
+   private static final int HASH_DISPLAY = "display".hashCode();

    public void clearProfiling() {
       this.profilingMap.clear();
       this.profilingSection = "";
       this.sectionList.clear();
+      this.profilerLocalEnabled = this.profilerGlobalEnabled;
    }

    public void startSection(String var1) {
-      if (this.profilingEnabled) {
-         if (!this.profilingSection.isEmpty()) {
-            this.profilingSection = this.profilingSection + ".";
+      if (Lagometer.isActive()) {
+         int var2 = var1.hashCode();
+         if (var2 == HASH_SCHEDULED_EXECUTABLES && var1.equals("scheduledExecutables")) {
+            Lagometer.timerScheduledExecutables.start();
+         } else if (var2 == HASH_TICK && var1.equals("tick") && Config.isMinecraftThread()) {
+            Lagometer.timerScheduledExecutables.end();
+            Lagometer.timerTick.start();
+         } else if (var2 == HASH_PRE_RENDER_ERRORS && var1.equals("preRenderErrors")) {
+            Lagometer.timerTick.end();
+         }
+      }
+
+      if (Config.isFastRender()) {
+         int var3 = var1.hashCode();
+         if (var3 == HASH_RENDER && var1.equals("render")) {
+            GlStateManager.clearEnabled = false;
+         } else if (var3 == HASH_DISPLAY && var1.equals("display")) {
+            GlStateManager.clearEnabled = true;
          }
+      }
+
+      if (this.profilerLocalEnabled) {
+         if (this.profilingEnabled) {
+            if (!this.profilingSection.isEmpty()) {
+               this.profilingSection = this.profilingSection + ".";
+            }

-         this.profilingSection = this.profilingSection + var1;
-         this.sectionList.add(this.profilingSection);
-         this.timestampList.add(System.nanoTime());
+            this.profilingSection = this.profilingSection + var1;
+            this.sectionList.add(this.profilingSection);
+            this.timestampList.add(System.nanoTime());
+         }
       }
    }

    public void func_194340_a(Supplier<String> var1) {
-      if (this.profilingEnabled) {
-         this.startSection((String)var1.get());
+      if (this.profilerLocalEnabled) {
+         if (this.profilingEnabled) {
+            this.startSection((String)var1.get());
+         }
       }
    }

    public void endSection() {
-      if (this.profilingEnabled) {
-         long var1 = System.nanoTime();
-         long var3 = this.timestampList.remove(this.timestampList.size() - 1);
-         this.sectionList.remove(this.sectionList.size() - 1);
-         long var5 = var1 - var3;
-         if (this.profilingMap.containsKey(this.profilingSection)) {
-            this.profilingMap.put(this.profilingSection, this.profilingMap.get(this.profilingSection) + var5);
-         } else {
-            this.profilingMap.put(this.profilingSection, var5);
-         }
+      if (this.profilerLocalEnabled) {
+         if (this.profilingEnabled) {
+            long var1 = System.nanoTime();
+            long var3 = this.timestampList.remove(this.timestampList.size() - 1);
+            this.sectionList.remove(this.sectionList.size() - 1);
+            long var5 = var1 - var3;
+            if (this.profilingMap.containsKey(this.profilingSection)) {
+               this.profilingMap.put(this.profilingSection, this.profilingMap.get(this.profilingSection) + var5);
+            } else {
+               this.profilingMap.put(this.profilingSection, var5);
+            }

-         if (var5 > 100000000L) {
-            LOGGER.warn("Something's taking too long! '{}' took aprox {} ms", this.profilingSection, var5 / 1000000.0);
-         }
+            if (var5 > 100000000L) {
+               LOGGER.warn("Something's taking too long! '{}' took aprox {} ms", this.profilingSection, var5 / 1000000.0);
+            }

-         this.profilingSection = this.sectionList.isEmpty() ? "" : this.sectionList.get(this.sectionList.size() - 1);
+            this.profilingSection = this.sectionList.isEmpty() ? "" : this.sectionList.get(this.sectionList.size() - 1);
+         }
       }
    }

    public List<Profiler.Result> getProfilingData(String var1) {
       if (!this.profilingEnabled) {
          return Collections.emptyList();
       } else {
-         String var2 = var1;
-         long var3 = this.profilingMap.containsKey("root") ? this.profilingMap.get("root") : 0L;
-         long var5 = this.profilingMap.containsKey(var1) ? this.profilingMap.get(var1) : -1L;
-         ArrayList var7 = Lists.newArrayList();
+         long var2 = this.profilingMap.containsKey("root") ? this.profilingMap.get("root") : 0L;
+         long var4 = this.profilingMap.containsKey(var1) ? this.profilingMap.get(var1) : -1L;
+         ArrayList var6 = Lists.newArrayList();
          if (!var1.isEmpty()) {
             var1 = var1 + ".";
          }

-         long var8 = 0L;
+         long var7 = 0L;

-         for (String var11 : this.profilingMap.keySet()) {
-            if (var11.length() > var1.length() && var11.startsWith(var1) && var11.indexOf(".", var1.length() + 1) < 0) {
-               var8 += this.profilingMap.get(var11);
+         for (String var10 : this.profilingMap.keySet()) {
+            if (var10.length() > var1.length() && var10.startsWith(var1) && var10.indexOf(".", var1.length() + 1) < 0) {
+               var7 += this.profilingMap.get(var10);
             }
          }

-         float var20 = (float)var8;
-         if (var8 < var5) {
-            var8 = var5;
-         }
-
-         if (var3 < var8) {
-            var3 = var8;
-         }
-
-         for (String var12 : this.profilingMap.keySet()) {
-            if (var12.length() > var1.length() && var12.startsWith(var1) && var12.indexOf(".", var1.length() + 1) < 0) {
-               long var13 = this.profilingMap.get(var12);
-               double var15 = var13 * 100.0 / var8;
-               double var17 = var13 * 100.0 / var3;
-               String var19 = var12.substring(var1.length());
-               var7.add(new Profiler.Result(var19, var15, var17));
+         float var19 = (float)var7;
+         if (var7 < var4) {
+            var7 = var4;
+         }
+
+         if (var2 < var7) {
+            var2 = var7;
+         }
+
+         for (String var11 : this.profilingMap.keySet()) {
+            if (var11.length() > var1.length() && var11.startsWith(var1) && var11.indexOf(".", var1.length() + 1) < 0) {
+               long var12 = this.profilingMap.get(var11);
+               double var14 = var12 * 100.0 / var7;
+               double var16 = var12 * 100.0 / var2;
+               String var18 = var11.substring(var1.length());
+               var6.add(new Profiler.Result(var18, var14, var16));
             }
          }

-         for (String var23 : this.profilingMap.keySet()) {
-            this.profilingMap.put(var23, this.profilingMap.get(var23) * 999L / 1000L);
+         for (String var22 : this.profilingMap.keySet()) {
+            this.profilingMap.put(var22, this.profilingMap.get(var22) * 950L / 1000L);
          }

-         if ((float)var8 > var20) {
-            var7.add(new Profiler.Result("unspecified", ((float)var8 - var20) * 100.0 / var8, ((float)var8 - var20) * 100.0 / var3));
+         if ((float)var7 > var19) {
+            var6.add(new Profiler.Result("unspecified", ((float)var7 - var19) * 100.0 / var7, ((float)var7 - var19) * 100.0 / var2));
          }

-         Collections.sort(var7);
-         var7.add(0, new Profiler.Result(var2, 100.0, var8 * 100.0 / var3));
-         return var7;
+         Collections.sort(var6);
+         var6.add(0, new Profiler.Result(var1, 100.0, var7 * 100.0 / var2));
+         return var6;
       }
    }

    public void endStartSection(String var1) {
-      this.endSection();
-      this.startSection(var1);
+      if (this.profilerLocalEnabled) {
+         this.endSection();
+         this.startSection(var1);
+      }
    }

    public void func_194339_b(Supplier<String> var1) {
-      this.endSection();
-      this.func_194340_a(var1);
+      if (this.profilerLocalEnabled) {
+         this.endSection();
+         this.func_194340_a(var1);
+      }
    }

    public String getNameOfLastSection() {
       return this.sectionList.isEmpty() ? "[UNKNOWN]" : this.sectionList.get(this.sectionList.size() - 1);
+   }
+
+   public void startSection(Class<?> var1) {
+      if (this.profilingEnabled) {
+         this.startSection(var1.getSimpleName());
+      }
    }

    public static final class Result implements Comparable<Profiler.Result> {
       public double usePercentage;
       public double totalUsePercentage;
       public String profilerName;
 */
