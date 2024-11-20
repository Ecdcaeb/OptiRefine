/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  java.lang.Class
 *  java.lang.Deprecated
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.Collections
 *  java.util.List
 *  java.util.Map
 *  java.util.function.Supplier
 *  net.minecraft.profiler.Profiler$Result
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.profiler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profiler {
    private static final Logger LOGGER = LogManager.getLogger();
    private final List<String> sectionList = Lists.newArrayList();
    private final List<Long> timestampList = Lists.newArrayList();
    public boolean profilingEnabled;
    private String profilingSection = "";
    private final Map<String, Long> profilingMap = Maps.newHashMap();

    public void clearProfiling() {
        this.profilingMap.clear();
        this.profilingSection = "";
        this.sectionList.clear();
    }

    public void startSection(String name) {
        if (this.profilingEnabled) {
            if (!this.profilingSection.isEmpty()) {
                this.profilingSection = this.profilingSection + ".";
            }
            this.profilingSection = this.profilingSection + name;
            this.sectionList.add((Object)this.profilingSection);
            this.timestampList.add((Object)System.nanoTime());
        }
    }

    public void func_194340_a(Supplier<String> p_194340_1_) {
        if (this.profilingEnabled) {
            this.startSection((String)p_194340_1_.get());
        }
    }

    public void endSection() {
        if (this.profilingEnabled) {
            long i = System.nanoTime();
            long j = (Long)this.timestampList.remove(this.timestampList.size() - 1);
            this.sectionList.remove(this.sectionList.size() - 1);
            long k = i - j;
            if (this.profilingMap.containsKey((Object)this.profilingSection)) {
                this.profilingMap.put((Object)this.profilingSection, (Object)((Long)this.profilingMap.get((Object)this.profilingSection) + k));
            } else {
                this.profilingMap.put((Object)this.profilingSection, (Object)k);
            }
            if (k > 100000000L) {
                LOGGER.warn("Something's taking too long! '{}' took aprox {} ms", (Object)this.profilingSection, (Object)((double)k / 1000000.0));
            }
            this.profilingSection = this.sectionList.isEmpty() ? "" : (String)this.sectionList.get(this.sectionList.size() - 1);
        }
    }

    public List<Result> getProfilingData(String profilerName) {
        if (!this.profilingEnabled) {
            return Collections.emptyList();
        }
        long i = this.profilingMap.containsKey((Object)"root") ? (Long)this.profilingMap.get((Object)"root") : 0L;
        long j = this.profilingMap.containsKey((Object)profilerName) ? (Long)this.profilingMap.get((Object)profilerName) : -1L;
        ArrayList list = Lists.newArrayList();
        if (!profilerName.isEmpty()) {
            profilerName = profilerName + ".";
        }
        long k = 0L;
        for (String s : this.profilingMap.keySet()) {
            if (s.length() <= profilerName.length() || !s.startsWith(profilerName) || s.indexOf(".", profilerName.length() + 1) >= 0) continue;
            k += ((Long)this.profilingMap.get((Object)s)).longValue();
        }
        float f = k;
        if (k < j) {
            k = j;
        }
        if (i < k) {
            i = k;
        }
        for (String s1 : this.profilingMap.keySet()) {
            if (s1.length() <= profilerName.length() || !s1.startsWith(profilerName) || s1.indexOf(".", profilerName.length() + 1) >= 0) continue;
            long l = (Long)this.profilingMap.get((Object)s1);
            double d0 = (double)l * 100.0 / (double)k;
            double d1 = (double)l * 100.0 / (double)i;
            String s2 = s1.substring(profilerName.length());
            list.add((Object)new Result(s2, d0, d1));
        }
        for (String s3 : this.profilingMap.keySet()) {
            this.profilingMap.put((Object)s3, (Object)((Long)this.profilingMap.get((Object)s3) * 999L / 1000L));
        }
        if ((float)k > f) {
            list.add((Object)new Result("unspecified", (double)((float)k - f) * 100.0 / (double)k, (double)((float)k - f) * 100.0 / (double)i));
        }
        Collections.sort((List)list);
        list.add(0, (Object)new Result(profilerName, 100.0, (double)k * 100.0 / (double)i));
        return list;
    }

    public void endStartSection(String name) {
        this.endSection();
        this.startSection(name);
    }

    public String getNameOfLastSection() {
        return this.sectionList.isEmpty() ? "[UNKNOWN]" : (String)this.sectionList.get(this.sectionList.size() - 1);
    }

    @SideOnly(value=Side.CLIENT)
    public void func_194339_b(Supplier<String> p_194339_1_) {
        this.endSection();
        this.func_194340_a(p_194339_1_);
    }

    @Deprecated
    public void startSection(Class<?> profiledClass) {
        if (this.profilingEnabled) {
            this.startSection(profiledClass.getSimpleName());
        }
    }
}
