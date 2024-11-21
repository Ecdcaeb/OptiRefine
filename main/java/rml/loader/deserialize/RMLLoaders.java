package rml.loader.deserialize;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.LineProcessor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import rml.jrx.utils.file.JsonHelper;
import rml.loader.ResourceModLoader;
import rml.jrx.announces.PrivateAPI;
import rml.loader.api.config.ConfigFactory;
import rml.loader.api.config.ConfigPatcher;
import rml.loader.api.event.FunctionLoadEvent;
import rml.loader.api.event.LootTableRegistryEvent;
import rml.jrx.utils.file.FileHelper;
import rml.jrx.reflection.jvm.FieldAccessor;
import rml.jrx.reflection.jvm.ReflectionHelper;
import rml.loader.api.mods.ContainerHolder;
import rml.loader.api.mods.module.ModuleType;
import rml.loader.api.world.function.FunctionExecutor;
import rml.loader.api.world.registry.remap.RemapCollection;
import rml.loader.api.world.villagers.IVillager;
import rml.loader.core.RMLFMLLoadingPlugin;
import net.minecraft.command.FunctionObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.message.FormattedMessage;
import rml.deserializer.JsonDeserializeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/19 12:56
 **/
@PrivateAPI
public class RMLLoaders {

    public static void runThrow(Throwable throwable, String msg, Object... args){
        throw new RuntimeException(new FormattedMessage(msg, args).getFormattedMessage(), throwable);
    }

    public static boolean isForced(ContainerHolder containerHolder, ModuleType moduleType){
        return containerHolder.hasModule(moduleType) && containerHolder.getModules().get(moduleType).forceLoaded;
    }

    public static void error(ModuleType moduleType, ContainerHolder containerHolder, Throwable throwable, String msg, Object... args){
        if (isForced(containerHolder, moduleType)) runThrow(throwable, msg, args);
        else RMLFMLLoadingPlugin.LOGGER.error(new FormattedMessage(msg, args).getFormattedMessage(), throwable);
    }
    /**
     * @Project ResourceModLoader
     * @Author Hileb
     * @Date 2023/12/15 12:54
     **/
    public static class OreDic {
        private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        public static void load(){
            ResourceModLoader.loadModuleFindAssets(ModuleType.valueOf(new ResourceLocation("rml", "ore_dic")), (containerHolder, module, root, file) -> {
                String relative = root.relativize(file).toString();
                if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
                    return;

                String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
                ResourceLocation key = new ResourceLocation(containerHolder.getContainer().getModId(), name);
                BufferedReader reader = null;
                try
                {
                    reader = Files.newBufferedReader(file);
                    for(TagOre tagOre : Deserializer.decode(TagOre[].class, JsonHelper.parse(reader))){
                        OreDictionary.registerOre(tagOre.ore, tagOre.item);
                    }
                }
                catch (JsonParseException | JsonDeserializeException e)
                {
                    error(Objects.requireNonNull(module, "module").moduleType, containerHolder, e, "Parsing error loading Ore dic {}", key);
                }
                catch (IOException e)
                {
                    error(Objects.requireNonNull(module, "module").moduleType, containerHolder, e, "Couldn't read ore dic {} from {}", key, file);
                }finally
                {
                    IOUtils.closeQuietly(reader);
                }
            });
        }

        public static class TagOre{
            public String ore;
            public ItemStack item;
        }
    }

    /**
     * @Project ResourceModLoader
     * @Author Hileb
     * @Date 2023/12/15 12:52
     **/
    public static class LootTable {
        public static void load(LootTableRegistryEvent event) {
                ResourceModLoader.loadModuleFindAssets(ModuleType.valueOf(new ResourceLocation("rml", "loot_tables")), (containerHolder, module, root, file) -> {
                    try{
                        String relative = root.relativize(file).toString();
                        if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
                            return;
                        String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
                        ResourceLocation key = new ResourceLocation(containerHolder.getContainer().getModId(), name);
                        event.register(key);
                    }catch (Throwable throwable){
                        error(Objects.requireNonNull(module, "module").moduleType, containerHolder, throwable, "LootTable register error.");
                    }
                });

        }
    }

    /**
     * @Project ResourceModLoader
     * @Author Hileb
     * @Date 2023/12/14 23:22
     **/

    @SuppressWarnings("all")
    public static class Function {
        public static LineProcessor<List<String>> processor(){ return new LineProcessor<List<String>>() {final List<String> result = Lists.newArrayList();@Override public boolean processLine(String line) {result.add(line);return true;}@Override public List<String> getResult() {return result;}};}

        public static void load(FunctionLoadEvent event) {
            ResourceModLoader.loadModuleFindAssets(ModuleType.valueOf(new ResourceLocation("rml", "functions")), (containerHolder, module, root, file) -> {
                String relative = root.relativize(file).toString();
                switch (FilenameUtils.getExtension(file.toString())) {
                    case "mcfunction" :
                        String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
                        ResourceLocation key = new ResourceLocation(containerHolder.getContainer().getModId(), name);
                        try {

                            FunctionObject functionObject = FunctionObject.create(
                                    event.functionManager,
                                    FileHelper.getByteSource(file).asCharSource(StandardCharsets.UTF_8)
                                            .readLines(processor())
                            );
                            event.register(key, functionObject);
                        } catch (IOException e) {
                            error(Objects.requireNonNull(module, "module").moduleType, containerHolder, e,"Couldn't read function {} from {}", key, file);
                        }
                        break;
                    default:
                        break;
                }
            });
            loadExecutors();
        }

        public static void loadExecutors() {

            ResourceModLoader.loadModuleFindAssets(ModuleType.valueOf(new ResourceLocation("rml", "functions")), (containerHolder, module, root, file) -> {
                String relative = root.relativize(file).toString();
                switch (FilenameUtils.getExtension(file.toString())) {
                    case "executor" :
                        String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
                        ResourceLocation key = new ResourceLocation(containerHolder.getContainer().getModId(), name);
                        try {
                            JsonElement jsonElement = JsonHelper.parse(Files.newBufferedReader(file));
                            Deserializer.decode(FunctionExecutor[].class, jsonElement);
                        } catch (IOException e) {
                            error(Objects.requireNonNull(module, "module").moduleType, containerHolder, e,"Couldn't read function executor {} from {}", key, file);
                        } catch (JsonDeserializeException e) {
                            error(Objects.requireNonNull(module, "module").moduleType, containerHolder, e, "Couldn't read function executor {} from {}", key, file);
                        }
                        break;
                    default:
                        break;
                }
            });
        }
    }
    public static class MissingRemap {
        public static void load() {
            ResourceModLoader.loadModuleFindAssets(ModuleType.valueOf(new ResourceLocation("rml", "registry_remap")), (containerHolder, module, root, file) -> {
                String relative = root.relativize(file).toString();
                if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
                    return;
                try {
                    JsonElement jsonElement = JsonHelper.parse(FileHelper.getCachedFile(file));
                    Arrays.stream(Deserializer.decode(RemapCollection[].class, jsonElement)).forEach(RemapCollection.Manager::merge);
                } catch (IOException e) {
                    error(Objects.requireNonNull(module, "module").moduleType, containerHolder, e, "Could not cache the file {} ", file);
                } catch (JsonDeserializeException e) {
                    error(Objects.requireNonNull(module, "module").moduleType, containerHolder, e, "Could not deserialize registry_remap {}", file);
                }
            });
        }
    }
    /**
     * @Project CustomVillage
     * @Author Hileb
     * @Date 2023/8/16 10:18
     **/
    public static class CustomVillageLoader {

        public static List<IVillager> load() {

            final List<IVillager> list = new ArrayList<>();
            ResourceModLoader.loadModuleFindAssets(ModuleType.valueOf(new ResourceLocation("rml", "custom_villagers")), (containerHolder, module, root, file) -> {
                String relative = root.relativize(file).toString();
                if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
                    return;

                String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
                ResourceLocation key = new ResourceLocation(containerHolder.getContainer().getModId(), name);

                BufferedReader reader = null;
                try {
                    reader = Files.newBufferedReader(file);
                    JsonElement jsonElement = JsonHelper.parse(reader);
                    try {
                        IVillager IVillager = Deserializer.decode(rml.loader.api.world.villagers.IVillager.class, jsonElement);
                        list.add(IVillager);
                    } catch (Exception e) {
                        error(Objects.requireNonNull(module, "module").moduleType, containerHolder, e,"Error load village at {}", file);
                    }
                } catch (JsonParseException | JsonDeserializeException e) {
                    error(Objects.requireNonNull(module, "module").moduleType, containerHolder, e,"Parsing error loading villager {}", key);
                } catch (IOException e) {
                    error(Objects.requireNonNull(module, "module").moduleType, containerHolder, e,"Couldn't read villager {} from {}", key, file);
                } finally {
                    IOUtils.closeQuietly(reader);
                }
            });
            return list;
        }
    }

    @SuppressWarnings("unused")
    public static class MCMainScreenTextLoader {
        public static List<String> rawTexts;

        public static void load() {
            ResourceModLoader.loadModuleFindAssets(ModuleType.valueOf(new ResourceLocation("rml", "splash_text")), (containerHolder, module, root, file) -> {
                BufferedReader bufferedreader = null;
                try {
                    bufferedreader = Files.newBufferedReader(file);
                    String s;
                    while ((s = bufferedreader.readLine()) != null) {
                        s = s.trim();

                        if (!s.isEmpty()) {
                            rawTexts.add(s);
                        }
                    }
                } catch (Exception e) {
                    error(Objects.requireNonNull(module, "module").moduleType, containerHolder, e, "MCMainScreenTextLoaderError");
                } finally {
                    IOUtils.closeQuietly(bufferedreader);
                }
            });
        }

        public static ArrayList<String> inject(ArrayList<String> list) {
            rawTexts = list;
            load();
            RMLFMLLoadingPlugin.LOGGER.debug("RML has injected the splashText!");
            return list;
        }

        public static String processComponent(String raw) {
            try {
                return ITextComponent.Serializer.jsonToComponent(raw).getFormattedText();
            } catch (Throwable ignored) {
                return raw;
            }
        }
    }

    /**
     * @Project ResourceModLoader
     * @Author Hileb
     * @Date 2024/5/12 11:13
     **/
    public static class ConfigLoader {
        public static final FieldAccessor<Map<String, Multimap<Config.Type, ASMDataTable.ASMData>>, ConfigManager> asm_data = ReflectionHelper.getFieldAccessor(ConfigManager.class, "asm_data");
        public static void load(){
            ResourceModLoader.loadModuleFindAssets(ModuleType.valueOf(new ResourceLocation("rml", "config_define")), (containerHolder, module, root, file) -> {
                String relative = root.relativize(file).toString();
                if (!"cfg".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
                    return;
                String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
                ResourceLocation key = new ResourceLocation(containerHolder.getContainer().getModId(), name);
                try
                {
                    byte[] cfg = FileHelper.getByteSource(file).read();
                    ConfigPatcher.OWNED_CONFIGS.put(ConfigFactory.addConfig(name, key.getPath(), cfg), key.getNamespace());
                    asm_data.get(null).put(key.getNamespace(), HashMultimap.create());
                }
                catch (IOException e)
                {
                    FMLLog.log.error("Couldn't read config define {} from {}", key, file, e);
                }
            });
        }
    }


}






