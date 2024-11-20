/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
 *  java.io.File
 *  java.io.FileOutputStream
 *  java.io.OutputStream
 *  java.io.OutputStreamWriter
 *  java.io.PrintWriter
 *  java.io.StringWriter
 *  java.io.Writer
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.OutOfMemoryError
 *  java.lang.StackOverflowError
 *  java.lang.StackTraceElement
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.nio.charset.StandardCharsets
 *  java.text.SimpleDateFormat
 *  java.util.Date
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Set
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.crash.ICrashReportDetail
 *  net.minecraft.util.ReportedException
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.relauncher.CoreModManager
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.commons.io.IOUtils
 *  org.apache.commons.lang3.ArrayUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.spongepowered.asm.mixin.extensibility.IMixinInfo
 *  org.spongepowered.asm.mixin.transformer.ClassInfo
 */
package net.minecraft.crash;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

public class CrashReport {
    private static final Logger LOGGER = LogManager.getLogger();
    private final String description;
    private final Throwable cause;
    private final CrashReportCategory systemDetailsCategory = new CrashReportCategory(this, "System Details");
    private final List<CrashReportCategory> crashReportSections = Lists.newArrayList();
    private File crashReportFile;
    private boolean firstCategoryInCrashReport = true;
    private StackTraceElement[] stacktrace = new StackTraceElement[0];

    public CrashReport(String descriptionIn, Throwable causeThrowable) {
        this.description = descriptionIn;
        this.cause = causeThrowable;
        this.populateEnvironment();
    }

    private void populateEnvironment() {
        this.systemDetailsCategory.addDetail("Minecraft Version", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
        this.systemDetailsCategory.addDetail("Cleanroom Version", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
        this.systemDetailsCategory.addDetail("Operating System", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
        this.systemDetailsCategory.addDetail("Java Version", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
        this.systemDetailsCategory.addDetail("Java VM Version", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
        this.systemDetailsCategory.addDetail("Memory", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
        this.systemDetailsCategory.addDetail("JVM Flags", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
        this.systemDetailsCategory.addDetail("IntCache", (ICrashReportDetail)new /* Unavailable Anonymous Inner Class!! */);
        FMLCommonHandler.instance().enhanceCrashReport(this, this.systemDetailsCategory);
    }

    public String getDescription() {
        return this.description;
    }

    public Throwable getCrashCause() {
        return this.cause;
    }

    public void getSectionsInStringBuilder(StringBuilder builder) {
        if (!(this.stacktrace != null && this.stacktrace.length > 0 || this.crashReportSections.isEmpty())) {
            this.stacktrace = (StackTraceElement[])ArrayUtils.subarray((Object[])((CrashReportCategory)this.crashReportSections.get(0)).getStackTrace(), (int)0, (int)1);
        }
        if (this.stacktrace != null && this.stacktrace.length > 0) {
            builder.append("-- Head --\n");
            builder.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
            builder.append("Stacktrace:\n");
            for (StackTraceElement stacktraceelement : this.stacktrace) {
                builder.append("\t").append("at ").append((Object)stacktraceelement);
                builder.append("\n");
            }
            builder.append("\n");
        }
        for (CrashReportCategory crashreportcategory : this.crashReportSections) {
            crashreportcategory.appendToStringBuilder(builder);
            builder.append("\n\n");
        }
        this.systemDetailsCategory.appendToStringBuilder(builder);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getCauseStackTraceOrString() {
        StringWriter stringwriter = null;
        PrintWriter printwriter = null;
        Throwable throwable = this.cause;
        if (throwable.getMessage() == null) {
            if (throwable instanceof NullPointerException) {
                throwable = new NullPointerException(this.description);
            } else if (throwable instanceof StackOverflowError) {
                throwable = new StackOverflowError(this.description);
            } else if (throwable instanceof OutOfMemoryError) {
                throwable = new OutOfMemoryError(this.description);
            }
            throwable.setStackTrace(this.cause.getStackTrace());
        }
        String s = throwable.toString();
        try {
            stringwriter = new StringWriter();
            printwriter = new PrintWriter((Writer)stringwriter);
            throwable.printStackTrace(printwriter);
            s = stringwriter.toString();
        }
        catch (Throwable throwable2) {
            IOUtils.closeQuietly((Writer)stringwriter);
            IOUtils.closeQuietly(printwriter);
            throw throwable2;
        }
        IOUtils.closeQuietly((Writer)stringwriter);
        IOUtils.closeQuietly((Writer)printwriter);
        Iterator stacktrace = throwable.getStackTrace();
        if (((StackTraceElement[])stacktrace).length > 0) {
            try {
                StringBuilder mixinMetadataBuilder = null;
                ObjectOpenHashSet classes = new ObjectOpenHashSet();
                for (StackTraceElement stackTraceElement : stacktrace) {
                    classes.add((Object)stackTraceElement.getClassName());
                }
                for (String className : classes) {
                    Set mixinInfos;
                    ClassInfo classInfo = ClassInfo.fromCache((String)className);
                    if (classInfo == null || (mixinInfos = classInfo.getMixins()).isEmpty()) continue;
                    if (mixinMetadataBuilder == null) {
                        mixinMetadataBuilder = new StringBuilder("\n(MixinBooter) Mixins in Stacktrace:");
                    }
                    mixinMetadataBuilder.append("\n\t");
                    mixinMetadataBuilder.append(className);
                    mixinMetadataBuilder.append(":");
                    for (IMixinInfo mixinInfo : mixinInfos) {
                        mixinMetadataBuilder.append("\n\t\t");
                        mixinMetadataBuilder.append(mixinInfo.getClassName());
                        mixinMetadataBuilder.append(" (");
                        mixinMetadataBuilder.append(mixinInfo.getConfig().getName());
                        mixinMetadataBuilder.append(")");
                    }
                }
                if (mixinMetadataBuilder == null) {
                    return s + "\nNo Mixin Metadata is found in the Stacktrace.\n";
                }
                return s + String.valueOf(mixinMetadataBuilder);
            }
            catch (Throwable t) {
                return s + "\nFailed to find Mixin Metadata in Stacktrace:\n" + String.valueOf((Object)((Object)t));
            }
        }
        return s;
    }

    public String getCompleteReport() {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("---- Minecraft Crash Report ----\n");
        CoreModManager.onCrash((StringBuilder)stringbuilder);
        stringbuilder.append("// ");
        stringbuilder.append(CrashReport.getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time: ");
        stringbuilder.append(new SimpleDateFormat().format(new Date()));
        stringbuilder.append("\n");
        stringbuilder.append("Description: ");
        stringbuilder.append(this.description);
        stringbuilder.append("\n\n");
        stringbuilder.append(this.getCauseStackTraceOrString());
        stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
        for (int i = 0; i < 87; ++i) {
            stringbuilder.append("-");
        }
        stringbuilder.append("\n\n");
        this.getSectionsInStringBuilder(stringbuilder);
        return stringbuilder.toString();
    }

    @SideOnly(value=Side.CLIENT)
    public File getFile() {
        return this.crashReportFile;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean saveToFile(File toFile) {
        boolean bl;
        if (this.crashReportFile != null) {
            return false;
        }
        if (toFile.getParentFile() != null) {
            toFile.getParentFile().mkdirs();
        }
        OutputStreamWriter writer = null;
        try {
            boolean lvt_3_1_;
            writer = new OutputStreamWriter((OutputStream)new FileOutputStream(toFile), StandardCharsets.UTF_8);
            writer.write(this.getCompleteReport());
            this.crashReportFile = toFile;
            bl = lvt_3_1_ = true;
        }
        catch (Throwable throwable) {
            boolean flag1;
            try {
                LOGGER.error("Could not save crash report to {}", (Object)toFile, (Object)throwable);
                flag1 = false;
            }
            catch (Throwable throwable2) {
                IOUtils.closeQuietly(writer);
                throw throwable2;
            }
            IOUtils.closeQuietly((Writer)writer);
            return flag1;
        }
        IOUtils.closeQuietly((Writer)writer);
        return bl;
    }

    public CrashReportCategory getCategory() {
        return this.systemDetailsCategory;
    }

    public CrashReportCategory makeCategory(String name) {
        return this.makeCategoryDepth(name, 1);
    }

    public CrashReportCategory makeCategoryDepth(String categoryName, int stacktraceLength) {
        CrashReportCategory crashreportcategory = new CrashReportCategory(this, categoryName);
        if (this.firstCategoryInCrashReport) {
            int i = crashreportcategory.getPrunedStackTrace(stacktraceLength);
            StackTraceElement[] astacktraceelement = this.cause.getStackTrace();
            StackTraceElement stacktraceelement = null;
            StackTraceElement stacktraceelement1 = null;
            int j = astacktraceelement.length - i;
            if (j < 0) {
                System.out.println("Negative index in crash report handler (" + astacktraceelement.length + "/" + i + ")");
            }
            if (astacktraceelement != null && 0 <= j && j < astacktraceelement.length) {
                stacktraceelement = astacktraceelement[j];
                if (astacktraceelement.length + 1 - i < astacktraceelement.length) {
                    stacktraceelement1 = astacktraceelement[astacktraceelement.length + 1 - i];
                }
            }
            this.firstCategoryInCrashReport = crashreportcategory.firstTwoElementsOfStackTraceMatch(stacktraceelement, stacktraceelement1);
            if (i > 0 && !this.crashReportSections.isEmpty()) {
                CrashReportCategory crashreportcategory1 = (CrashReportCategory)this.crashReportSections.get(this.crashReportSections.size() - 1);
                crashreportcategory1.trimStackTraceEntriesFromBottom(i);
            } else if (astacktraceelement != null && astacktraceelement.length >= i && 0 <= j && j < astacktraceelement.length) {
                this.stacktrace = new StackTraceElement[j];
                System.arraycopy((Object)astacktraceelement, (int)0, (Object)this.stacktrace, (int)0, (int)this.stacktrace.length);
            } else {
                this.firstCategoryInCrashReport = false;
            }
        }
        this.crashReportSections.add((Object)crashreportcategory);
        return crashreportcategory;
    }

    private static String getWittyComment() {
        String[] astring = new String[]{"Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine."};
        try {
            return astring[(int)(System.nanoTime() % (long)astring.length)];
        }
        catch (Throwable var2) {
            return "Witty comment unavailable :(";
        }
    }

    public static CrashReport makeCrashReport(Throwable causeIn, String descriptionIn) {
        CrashReport crashreport = causeIn instanceof ReportedException ? ((ReportedException)causeIn).getCrashReport() : new CrashReport(descriptionIn, causeIn);
        return crashreport;
    }
}
