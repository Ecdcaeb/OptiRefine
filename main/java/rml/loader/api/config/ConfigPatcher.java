package rml.loader.api.config;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import rml.loader.ResourceModLoader;
import rml.jrx.announces.ASMInvoke;
import rml.jrx.announces.PrivateAPI;
import rml.jrx.announces.PublicAPI;
import rml.jrx.utils.file.FileHelper;
import rml.jrx.utils.file.JsonHelper;
import rml.jrx.reflection.jvm.FieldAccessor;
import rml.jrx.reflection.jvm.ReflectionHelper;
import rml.jrx.utils.ObjectHelper;
import rml.loader.api.mods.module.ModuleType;
import rml.loader.core.RMLFMLLoadingPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/5/11 13:10
 *
 * Patch the configs
 **/
public abstract class ConfigPatcher {
    public static final HashMap<Configuration, String> OWNED_CONFIGS = new HashMap<>();
    @PrivateAPI private static final Multimap<String, ConfigPatcher> cachedRedefault = HashMultimap.create();
    @PrivateAPI  private static final Multimap<String, ConfigPatcher> cachedOverrides = HashMultimap.create();

    public abstract void patch(Configuration configuration);

    @SuppressWarnings("unchecked") // target Override
    public static void handleOverride(){
        RMLFMLLoadingPlugin.Container.LOGGER.info("config count {}",((Map<String, Configuration>)ReflectionHelper.getPrivateValue(ConfigManager.class, null,"CONFIGS")).values().size());
        for(Configuration configuration:((Map<String, Configuration>)ReflectionHelper.getPrivateValue(ConfigManager.class, null,"CONFIGS")).values()){
            transformConfigOverride(configuration,FilenameUtils.getName(configuration.getConfigFile().getName()));
        }
    }
    @SuppressWarnings("unused") //target redefault
    @PrivateAPI @ASMInvoke
    public static Object registerCfg(Map<Object, Object> map, Object string, Object configuration){
        RMLFMLLoadingPlugin.Container.LOGGER.info("register a config {}", string);
        transformConfigRedefalut((Configuration) configuration, FilenameUtils.getName((String)string));
        if (Loader.instance().activeModContainer() != null){
            OWNED_CONFIGS.put((Configuration) configuration, ObjectHelper.orDefault(Loader.instance().activeModContainer().getModId(), ((String)string).substring(0, ((String)string).length()-4)));
        }
        return map.put(string, configuration);
    }
    @PrivateAPI public static void transformConfigRedefalut(Configuration configuration, String name){
        runPatches(configuration, name, cachedRedefault, "redefault");
    }
    @PrivateAPI public static void transformConfigOverride(Configuration configuration, String name){
        runPatches(configuration, name, cachedOverrides, "overrides");
    }

    @PublicAPI
    public static void runPatches(Configuration configuration, String name, Multimap<String, ConfigPatcher> patchers, String type){
        RMLFMLLoadingPlugin.Container.LOGGER.info("try apply config {} for {}",type, name);
        for(ConfigPatcher patcher : patchers.get(name)){
            patcher.patch(configuration);
            RMLFMLLoadingPlugin.Container.LOGGER.info("apply config {} for {} ",type, name);
        }
        patchers.removeAll(name);
        syncForClassesGeneratedConfigs(configuration);
    }

    @PrivateAPI public static void searchRedefault(){
        ResourceModLoader.loadModuleFindAssets(ModuleType.valueOf(new ResourceLocation("rml", "config_redefault")), (containerHolder, module, root, file) -> {
            String relative = root.relativize(file).toString();
            String extension = FilenameUtils.getExtension(file.toString());
            String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
            ResourceLocation key = new ResourceLocation(containerHolder.getContainer().getModId(), name);
            switch (extension){
                case "json":

                    BufferedReader reader = null;
                    try {
                        reader = Files.newBufferedReader(file);
                        JsonObject json = JsonHelper.getJson(reader);
                        cachedRedefault.put(name, new Json(json));
                        RMLFMLLoadingPlugin.Container.LOGGER.info("find {} {}", name, json);
                    } catch (JsonParseException e) {
                        RMLFMLLoadingPlugin.Container.LOGGER.error("Parsing error loading config redefault {}", key, e);
                    } catch (IOException e) {
                        RMLFMLLoadingPlugin.Container.LOGGER.error("Couldn't read config redefault {} from {}", key, file, e);
                    } finally {
                        IOUtils.closeQuietly(reader);
                    }
                    break;
                case "patch":
                    try {
                        cachedOverrides.put(name, new Cfg(FileHelper.getByteSource(file).read()));
                    } catch (JsonParseException e) {
                        RMLFMLLoadingPlugin.Container.LOGGER.error("Parsing error loading redefault override {}", key, e);
                    } catch (IOException e) {
                        RMLFMLLoadingPlugin.Container.LOGGER.error("Couldn't read config redefault {} from {}", key, file, e);
                    }
                    break;
                default:
                    break;
            }
        });
        RMLFMLLoadingPlugin.Container.LOGGER.info("Search {} config redefault",cachedRedefault.size());
    }

    @PrivateAPI public static void searchOverride(){
        ResourceModLoader.loadModuleFindAssets(ModuleType.valueOf(new ResourceLocation("rml", "config_override")), (containerHolder, module, root, file) -> {
            String relative = root.relativize(file).toString();
            String extension = FilenameUtils.getExtension(file.toString());
            String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
            ResourceLocation key = new ResourceLocation(containerHolder.getContainer().getModId(), name);
            switch (extension){
                case "json":
                    BufferedReader reader = null;
                    try {
                        reader = Files.newBufferedReader(file);
                        JsonObject json = JsonHelper.getJson(reader);
                        cachedOverrides.put(name, new Json(json));
                        RMLFMLLoadingPlugin.Container.LOGGER.info("find {} {}", name, json);
                    } catch (JsonParseException e) {
                        RMLFMLLoadingPlugin.Container.LOGGER.error("Parsing error loading config override {}", key, e);
                    } catch (IOException e) {
                        RMLFMLLoadingPlugin.Container.LOGGER.error("Couldn't read config override {} from {}", key, file, e);
                    } finally {
                        IOUtils.closeQuietly(reader);
                    }
                    break;
                case "patch":
                    try {
                        cachedOverrides.put(name, new Cfg(FileHelper.getByteSource(file).read()));
                    } catch (JsonParseException e) {
                        RMLFMLLoadingPlugin.Container.LOGGER.error("Parsing error loading config override {}", key, e);
                    } catch (IOException e) {
                        RMLFMLLoadingPlugin.Container.LOGGER.error("Couldn't read config override {} from {}", key, file, e);
                    }
                    break;
                default:
                    break;
            }
        });
        RMLFMLLoadingPlugin.Container.LOGGER.info("Search {} config overrides",cachedOverrides.size());
    }

    public static void syncForClassesGeneratedConfigs(Configuration configuration){
        if (OWNED_CONFIGS.containsKey(configuration)) MinecraftForge.EVENT_BUS.post(new ConfigChangedEvent.OnConfigChangedEvent(OWNED_CONFIGS.get(configuration), null, isWorldRunning(), false));
    }

    public static boolean isWorldRunning(){
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT){
            return Minecraft.getMinecraft().world != null;
        }else {
            MinecraftServer server = FMLServerHandler.instance().getServer();
            if (server != null){
                return server.isServerRunning();
            }
        }
        return false;
    }

    /**
     * @Project ResourceModLoader
     * @Author Hileb
     * @Date 2024/2/6 12:58
     **/
    @PrivateAPI
    @SuppressWarnings("all")
    public static class Json extends ConfigPatcher{
        private static Map<String, Configuration> CONFIGS = ReflectionHelper.getPrivateValue(ConfigManager.class,  null,"CONFIGS");
        private static Map<String, Set<Class<?>>> MOD_CONFIG_CLASSES = ReflectionHelper.getPrivateValue(ConfigManager.class,  null,"MOD_CONFIG_CLASSES");

        public JsonObject json;

        public Json(JsonObject json){
            this.json = json;
        }
        @Override
        public void patch(Configuration configuration) {
            transformTheConfiguration(configuration, this.json);
        }

        public static void transformTheConfiguration(Configuration config, JsonObject json){
            Multimap<String,String> multimap = HashMultimap.create();
            for(String s: JsonHelper.walkAndGetAllEntryPath(json)){
                String[] s1=s.split("\\.");
                String s2=s1[s1.length-1];
                multimap.put(s.substring(0,s.length()-s2.length()-1),s2);
            }
            for(String cateName:multimap.keys()){
                ConfigElement configElement=new ConfigElement(config.getCategory(cateName));
                for(IConfigElement iConfigElement:configElement.getChildElements()){
                    for(String childName:multimap.get(cateName)){
                        if (childName.equals(iConfigElement.getName())){
                            JsonElement jsonElement=JsonHelper.walkAndGetTheElement(json,cateName+"."+childName);
                            if (JsonHelper.isList(jsonElement)){
                                iConfigElement.set(JsonHelper.getAsRawList(jsonElement));
                            }else iConfigElement.set(JsonHelper.getRawValue(jsonElement));
                        }
                    }
                }
            }
            config.save();//not save auto. Since we break the loading rules.
        }
    }

    /**
     * @Project ResourceModLoader
     * @Author Hileb
     * @Date 2024/5/10 12:40
     **/
    @PrivateAPI
    public static class Cfg extends ConfigPatcher{
        public final byte[] file;
        public Cfg(byte[] file){
            this.file = file;
        }

        @Override
        public void patch(Configuration configuration) {
            process(configuration, this.file, configuration.defaultEncoding);
            configuration.save();//not save auto. Since we break the loading rules.
        }

        private static final String CONFIG_VERSION_MARKER = "~CONFIG_VERSION";
        private static final Pattern CONFIG_START = Pattern.compile("START: \"([^\\\"]+)\"");
        private static final Pattern CONFIG_END = Pattern.compile("END: \"([^\\\"]+)\"");
        private static final FieldAccessor<String, Configuration> loadedConfigVersion = ReflectionHelper.getFieldAccessor(Configuration.class, "loadedConfigVersion");
        private static final FieldAccessor<String, Configuration> definedConfigVersion = ReflectionHelper.getFieldAccessor(Configuration.class, "definedConfigVersion");
        private static final FieldAccessor<String, Configuration> fileName = ReflectionHelper.getFieldAccessor(Configuration.class, "fileName");
        private static final FieldAccessor<Map<String, ConfigCategory>, Configuration> categories = ReflectionHelper.getFieldAccessor(Configuration.class, "categories");
        private static final FieldAccessor<Map<String, Configuration>, Configuration> children = ReflectionHelper.getFieldAccessor(Configuration.class, "children");
        private static final FieldAccessor<Boolean, Configuration> caseSensitiveCustomCategories = ReflectionHelper.getFieldAccessor(Configuration.class, "caseSensitiveCustomCategories");
        public static void process(Configuration configuration, byte[] file, String defaultEncoding){
            try {
                Configuration.UnicodeInputStreamReader inputStreamReader = new Configuration.UnicodeInputStreamReader(new ByteArrayInputStream(file), defaultEncoding);
                BufferedReader buffer = new BufferedReader(inputStreamReader);

                String line;
                ConfigCategory currentCat = null;
                Property.Type type = null;
                ArrayList<String> tmpList = null;
                int lineNum = 0;
                String name = null;
                loadedConfigVersion.set(configuration, null);

                while (true)
                {
                    lineNum++;
                    line = buffer.readLine();

                    if (line == null)
                    {
                        if (lineNum == 1)
                            loadedConfigVersion.set(configuration, definedConfigVersion.get(configuration));
                        break;
                    }

                    Matcher start = CONFIG_START.matcher(line);
                    Matcher end = CONFIG_END.matcher(line);

                    if (start.matches())
                    {
                        fileName.set(configuration, start.group(1));
                        categories.set(configuration, new TreeMap<String, ConfigCategory>());
                        continue;
                    }
                    else if (end.matches())
                    {
                        fileName.set(configuration, end.group(1));
                        Configuration child = new Configuration();
                        categories.set(child, categories.get(configuration));
                        children.get(configuration).put(fileName.get(configuration), child);
                        continue;
                    }

                    int nameStart = -1, nameEnd = -1;
                    boolean skip = false;
                    boolean quoted = false;
                    boolean isFirstNonWhitespaceCharOnLine = true;

                    for (int i = 0; i < line.length() && !skip; ++i)
                    {
                        if (Character.isLetterOrDigit(line.charAt(i)) || Configuration.ALLOWED_CHARS.indexOf(line.charAt(i)) != -1 || (quoted && line.charAt(i) != '"'))
                        {
                            if (nameStart == -1)
                            {
                                nameStart = i;
                            }

                            nameEnd = i;
                            isFirstNonWhitespaceCharOnLine = false;
                        }
                        else if (Character.isWhitespace(line.charAt(i)))
                        {
                            // ignore space characters
                        }
                        else
                        {
                            switch (line.charAt(i))
                            {
                                case '#':
                                    if (tmpList != null) // allow special characters as part of string lists
                                        break;
                                    skip = true;
                                    continue;

                                case '"':
                                    if (tmpList != null) // allow special characters as part of string lists
                                        break;
                                    if (quoted)
                                    {
                                        quoted = false;
                                    }
                                    if (!quoted && nameStart == -1)
                                    {
                                        quoted = true;
                                    }
                                    break;

                                case '{':
                                    if (tmpList != null) // allow special characters as part of string lists
                                        break;
                                    name = line.substring(nameStart, nameEnd + 1);
                                    if (!caseSensitiveCustomCategories.get(configuration))
                                        name = name.toLowerCase(Locale.ENGLISH);
                                    String qualifiedName = ConfigCategory.getQualifiedName(name, currentCat);

                                    ConfigCategory cat = categories.get(configuration).get(qualifiedName);
                                    if (cat == null)
                                    {
                                        currentCat = new ConfigCategory(name, currentCat);
                                        categories.get(configuration).put(qualifiedName, currentCat);
                                    }
                                    else
                                    {
                                        currentCat = cat;
                                    }
                                    name = null;

                                    break;

                                case '}':
                                    if (tmpList != null) // allow special characters as part of string lists
                                        break;
                                    if (currentCat == null)
                                    {
                                        throw new RuntimeException(String.format("Config file corrupt, attempted to close to many categories '%s:%d'", fileName.get(configuration), lineNum));
                                    }
                                    currentCat = currentCat.parent;
                                    break;

                                case '=':
                                    if (tmpList != null) // allow special characters as part of string lists
                                        break;
                                    name = line.substring(nameStart, nameEnd + 1);

                                    if (currentCat == null)
                                    {
                                        throw new RuntimeException(String.format("'%s' has no scope in '%s:%d'", name, fileName.get(configuration), lineNum));
                                    }

                                    Property prop = new Property(name, line.substring(i + 1), type, true);
                                    i = line.length();

                                    currentCat.put(name, prop);

                                    break;

                                case ':':
                                    if (tmpList != null) // allow special characters as part of string lists
                                        break;
                                    type = Property.Type.tryParse(line.substring(nameStart, nameEnd + 1).charAt(0));
                                    nameStart = nameEnd = -1;
                                    break;

                                case '<':
                                    if ((tmpList != null && i + 1 == line.length()) || (tmpList == null && i + 1 != line.length()))
                                    {
                                        throw new RuntimeException(String.format("Malformed list property \"%s:%d\"", fileName.get(configuration), lineNum));
                                    }
                                    else if (i + 1 == line.length())
                                    {
                                        name = line.substring(nameStart, nameEnd + 1);

                                        if (currentCat == null)
                                        {
                                            throw new RuntimeException(String.format("'%s' has no scope in '%s:%d'", name, fileName.get(configuration), lineNum));
                                        }

                                        tmpList = new ArrayList<String>();

                                        skip = true;
                                    }

                                    break;

                                case '>':
                                    if (tmpList == null)
                                    {
                                        throw new RuntimeException(String.format("Malformed list property \"%s:%d\"", fileName.get(configuration), lineNum));
                                    }

                                    if (isFirstNonWhitespaceCharOnLine)
                                    {
                                        currentCat.put(name, new Property(name, tmpList.toArray(new String[tmpList.size()]), type));
                                        name = null;
                                        tmpList = null;
                                        type = null;
                                    } // else allow special characters as part of string lists
                                    break;

                                case '~':
                                    if (tmpList != null) // allow special characters as part of string lists
                                        break;

                                    if (line.startsWith(CONFIG_VERSION_MARKER))
                                    {
                                        int colon = line.indexOf(':');
                                        if (colon != -1)
                                            loadedConfigVersion.set(configuration, line.substring(colon + 1).trim());

                                        skip = true;
                                    }
                                    break;

                                default:
                                    if (tmpList != null) // allow special characters as part of string lists
                                        break;
                                    throw new RuntimeException(String.format("Unknown character '%s' in '%s:%d'", line.charAt(i), fileName.get(configuration), lineNum));
                            }
                            isFirstNonWhitespaceCharOnLine = false;
                        }
                    }

                    if (quoted)
                    {
                        throw new RuntimeException(String.format("Unmatched quote in '%s:%d'", fileName.get(configuration), lineNum));
                    }
                    else if (tmpList != null && !skip)
                    {
                        tmpList.add(line.trim());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("configuration could not be read!" + configuration, e);
            }
        }
    }
}
