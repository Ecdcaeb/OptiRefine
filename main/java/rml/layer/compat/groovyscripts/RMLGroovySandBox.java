package rml.layer.compat.groovyscripts;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.sandbox.GroovySandbox;
import com.cleanroommc.groovyscript.sandbox.GroovyScriptSandbox;
import com.cleanroommc.groovyscript.sandbox.LoadStage;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import net.minecraft.util.ResourceLocation;
import org.codehaus.groovy.runtime.InvokerHelper;
import rml.jrx.announces.PrivateAPI;
import rml.jrx.announces.RewriteWhenCleanroom;
import rml.jrx.reflection.jvm.FieldAccessor;
import rml.jrx.reflection.jvm.MethodAccessor;
import rml.jrx.reflection.jvm.ReflectionHelper;
import rml.jrx.utils.file.FileHelper;
import rml.loader.api.mods.ContainerHolder;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/7/30 16:49
 **/
@PrivateAPI
@RewriteWhenCleanroom
public class RMLGroovySandBox {
    public static final MethodAccessor<Void, GroovySandbox> m_GroovySandbox$runScript = ReflectionHelper.getMethodAccessor(GroovySandbox.class, "runScript", "runScript", Script.class);
    public static final FieldAccessor<LoadStage, GroovyScriptSandbox> f_GroovyScriptSandbox$currentLoadStage = ReflectionHelper.getFieldAccessor(GroovyScriptSandbox.class, "currentLoadStage");
    public static void load(GroovySandbox sandbox, GroovyScriptEngine engine, Binding binding, Set<File> unused, boolean run){
        final String GROOVY_ROOT = GroovyScript.getScriptPath() + "/";
        HashSet<ResourceLocation> executedClasses = new HashSet<>();
        String loader = f_GroovyScriptSandbox$currentLoadStage.get(GroovyScript.getSandbox()).getName();
        HashSet<NamedScript> files = new HashSet<>();
        for(Map.Entry<ContainerHolder, RMLGrsLoader.RunConfig> entry : RMLGrsLoader.MOD.entrySet()){
            for(String classPath : entry.getValue().getClasses(loader)){
                ResourceLocation name = new ResourceLocation(entry.getKey().getContainer().getModId(), classPath);
                byte[] file = FileHelper.findFile(entry.getKey().getContainer(), classPath);
                if (Preprocessor.validatePreprocessor(name, file))
                    files.add(new NamedScript(new ResourceLocation(entry.getKey().getContainer().getModId(), classPath), FileHelper.findFile(entry.getKey().getContainer(), classPath)));

            }
        }

        GroovyClassLoader groovyClassLoader = engine.getGroovyClassLoader();

        // load and run any configured class files
        //loadClassScripts
        for(NamedScript file : files){
            Class<?> clazz = compile(makeFakeRelativePath(file.getName(), GROOVY_ROOT), file, groovyClassLoader);
            if (clazz.getSuperclass() != Script.class){
                executedClasses.add(file.getName());
                Script script = InvokerHelper.createScript(clazz, binding);
                if (run) runScript(script);
            }
        }

        // now run all script files
        //loadScripts
        for(NamedScript file : files){
            if (!executedClasses.contains(file.getName())){
                Class<?> clazz = compile(makeFakeRelativePath(file.getName(), GROOVY_ROOT), file, groovyClassLoader);
                if (clazz == GroovyLog.class) continue; // preprocessor returned false
                if (clazz == null) {
                    GroovyLog.get().errorMC("Error loading script for {}", file.getName());
                    GroovyLog.get().errorMC("Did you forget to register your class file in your run config?");
                    continue;
                }
                if (clazz.getSuperclass() != Script.class) {
                    GroovyLog.get().errorMC("RClass file '{}' should be defined in the runConfig in the classes property!", file.getName());
                    continue;
                }
                Script script = InvokerHelper.createScript(clazz, binding);
                if (run) runScript(script);
            }
        }
    }

    public static String makeFakeRelativePath(ResourceLocation resourceLocation, String root){
        return root + resourceLocation.toString().replace(':', '/');
    }

    private static String fixPathSeparatorChar(String path) {
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
        }

        if (File.separatorChar != '/') {
            path = path.replace('/', File.separatorChar);
        }
        return path;
    }

    public static void runScript(Script script){
        m_GroovySandbox$runScript.invoke(GroovyScript.getSandbox(), script);
    }

    public static Class<?> compile(String name, NamedScript namedScript, GroovyClassLoader classLoader){
        return namedScript.compile(name, classLoader);
    }
}
