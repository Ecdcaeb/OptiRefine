package rml.layer.compat.groovyscripts;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.registry.ReloadableRegistryManager;
import com.google.common.io.ByteSource;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import rml.jrx.announces.PrivateAPI;
import rml.jrx.announces.RewriteWhenCleanroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.BiPredicate;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/7/31 15:29
 **/
@PrivateAPI
@RewriteWhenCleanroom
public class Preprocessor {
    private static final Object2ObjectArrayMap<String, BiPredicate<ResourceLocation, String[]>> PREPROCESSORS = new Object2ObjectArrayMap<>();
    private static final String[] NO_ARGS = new String[0];

    public static void registerPreprocessor(String name, BiPredicate<ResourceLocation, String[]> test) {
        PREPROCESSORS.put(name.toUpperCase(Locale.ROOT), test);
    }

    static {
        registerPreprocessor("NO_RUN", (file, args) -> false);
        registerPreprocessor("DEBUG_ONLY", (file, args) -> GroovyScript.getRunConfig().isDebug());
        registerPreprocessor("NO_RELOAD", (file, args) -> !ReloadableRegistryManager.isFirstLoad());
        registerPreprocessor("MODS_LOADED",Preprocessor::checkModsLoaded);
        registerPreprocessor("SIDE", Preprocessor::checkSide);
    }

    public static List<String> parsePreprocessors(byte[] file) {
        List<String> preprocessors = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(ByteSource.wrap(file).openBufferedStream()))) {
            boolean isComment = false;
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("/*")) {
                    isComment = true;
                    line = line.substring(2).trim();
                    if (line.isEmpty()) continue;
                }
                if (line.startsWith("//")) {
                    line = line.substring(2).trim();
                    if (line.isEmpty()) continue;
                } else if (!isComment) {
                    return preprocessors.isEmpty() ? Collections.emptyList() : preprocessors;
                }
                if (isComment && line.endsWith("*/")) {
                    isComment = false;
                }

                if (isPreprocessor(line)) {
                    preprocessors.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return preprocessors.isEmpty() ? Collections.emptyList() : preprocessors;
    }

    public static boolean validatePreprocessor(ResourceLocation name, byte[] file) {
        for (String pp : parsePreprocessors(file)) {
            if (!processPreprocessor(name, pp)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPreprocessor(String line) {
        String s = line.split(":", 2)[0];
        return PREPROCESSORS.containsKey(s.toUpperCase(Locale.ROOT));
    }

    private static boolean processPreprocessor(ResourceLocation name, String line) {
        String[] parts = line.split(":", 2);
        String[] args = NO_ARGS;
        if (parts.length > 1) {
            args = parts[1].split(",");
            for (int i = 0; i < args.length; i++) {
                args[i] = args[i].trim();
            }
        }
        String s = parts[0];
        BiPredicate<ResourceLocation, String[]> preprocessor = PREPROCESSORS.get(s.toUpperCase(Locale.ROOT));
        return preprocessor.test(name, args);
    }

    private static boolean checkModsLoaded(ResourceLocation name, String[] mods) {
        for (String mod : mods) {
            if (!Loader.isModLoaded(mod)) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkSide(ResourceLocation name, String[] sides) {
        if (sides.length != 1) {
            GroovyLog.get().error("Side preprocessor in file '{}' should have exactly one argument, but found {}", name, Arrays.asList(sides));
            return true;
        }
        String side = sides[0].toUpperCase();
        if ("CLIENT".equals(side)) {
            return FMLCommonHandler.instance().getSide().isClient();
        }
        if ("SERVER".equals(side)) {
            return FMLCommonHandler.instance().getSide().isServer();
        }
        GroovyLog.get().error("Side processor argument in file '{}' must be CLIENT or SERVER (lower case is allowed too)", name);
        return true;
    }

}
