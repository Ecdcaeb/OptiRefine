package rml.layer.compat.justenoughdimensions;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import fi.dy.masa.justenoughdimensions.config.DimensionConfig;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FilenameUtils;
import rml.jrx.reflection.jvm.MethodAccessor;
import rml.jrx.reflection.jvm.ReflectionHelper;
import rml.jrx.utils.file.JsonHelper;
import rml.loader.ResourceModLoader;
import rml.loader.api.mods.module.ModuleType;
import rml.loader.deserialize.RMLLoaders;

import java.io.IOException;
import java.nio.file.Files;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/8/13 20:27
 **/
public class JEDLoader {
    public static final MethodAccessor<Void, fi.dy.masa.justenoughdimensions.config.DimensionConfig> parseDimensionConfig = ReflectionHelper.getMethodAccessor(DimensionConfig.class, "parseDimensionConfig", "parseDimensionConfig", JsonElement.class);

    public static void load(){
        ResourceModLoader.loadModuleFindAssets(ModuleType.valueOf(new ResourceLocation("rml", "justenoughdimensions")), (containerHolder, module, root, file) -> {
            try{
                String relative = root.relativize(file).toString();
                if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
                    return;
                JsonElement jsonElement = JsonHelper.getJson(Files.newBufferedReader(file));
                fromJson(jsonElement);
            } catch (IOException e) {
                RMLLoaders.error(module.moduleType, containerHolder, e, "Could not read {}", file.toString());
            } catch (JsonSyntaxException e){
                RMLLoaders.error(module.moduleType, containerHolder, e, "Could not read as Json {}", file.toString());
            }
        });
    }

    public static void fromJson(JsonElement jsonElement){
        parseDimensionConfig.invoke(DimensionConfig.instance(), jsonElement);
    }
}
