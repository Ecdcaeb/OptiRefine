package rml.layer.compat.kubejs;

import dev.latvian.kubejs.KubeJS;
import dev.latvian.kubejs.script.BindingsEvent;
import dev.latvian.kubejs.script.ScriptFile;
import dev.latvian.kubejs.script.ScriptManager;
import dev.latvian.kubejs.script.ScriptPack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import rml.jrx.announces.PrivateAPI;
import rml.jrx.reflection.jvm.MethodAccessor;
import rml.jrx.reflection.jvm.ReflectionHelper;
import rml.loader.ResourceModLoader;
import rml.loader.api.mods.module.ModuleType;
import rml.loader.core.RMLFMLLoadingPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/3 11:33
 **/
@PrivateAPI
public class RMKKubeJs {
    @PrivateAPI
    @SubscribeEvent
    public static void onJSLoad(BindingsEvent event){
        RMLFMLLoadingPlugin.Container.LOGGER.info("Inject KubeJS");
        ResourceModLoader.loadModuleFindAssets(ModuleType.valueOf(new ResourceLocation("rml", "mod_kubejs")),
                (module, containerHolder) -> {
                    if (!packs.containsKey(containerHolder.getContainer().getModId())) {
                     packs.put(containerHolder.getContainer().getModId(), newPack.invoke(ScriptManager.instance, containerHolder.getContainer().getModId()));
                    }
                },
                (containerHolder, module, root, file) -> {
                    String relative = root.relativize(file).toString();
                    if (!"js".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
                        return;
                    BufferedReader bufferedReader=null;
                    try {
                        char[] fileBytes;
                        bufferedReader=Files.newBufferedReader(file);
                        fileBytes=IOUtils.toCharArray(bufferedReader);
                        load(ScriptManager.instance, file.toUri().toString(), fileBytes, containerHolder.getContainer().getModId());

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }finally {
                        IOUtils.closeQuietly(bufferedReader);
                    }
                }
        );
    }
    @PrivateAPI
    private static void load(ScriptManager manager, String name, char[] file, String modid) {
        KubeJS.LOGGER.info("Found script at " + name);
        int weight;
        weight = 0;
        if (name.endsWith("/init.js")) {
            weight = -100;
        }
        ScriptFile scriptFile = new BuffedJSFile(packs.get(modid), name, weight, file);
        manager.scripts.put(name, scriptFile);
        KubeJS.LOGGER.info("Load script at " + name);
    }
    @PrivateAPI public static final MethodAccessor<ScriptPack, ScriptManager> newPack = ReflectionHelper.getMethodAccessor(ScriptManager.class, "newPack", null, String.class);
    @PrivateAPI public static final Map<String, ScriptPack> packs;

    static {
        packs = ReflectionHelper.getPrivateValue(ScriptManager.class,ScriptManager.instance,"packs");
    }

}



