package rml.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PublicAPI;
import rml.jrx.utils.ClassHelper;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/7/14 10:03
 **/

@EarlyClass
@PublicAPI
public class DeserializerManager {
    public final String defaultDomain;
    public final HashMap<Class<?>, AbstractDeserializer<?>> defaults = new HashMap<>();
    public final HashMap<Class<?>, HashMap<ResourceLocation, AbstractDeserializer<?>>> registry = new HashMap<>();


    /**
     * Add a deserializer
     *
     * @param abstractDeserializer the deserializer
     * @param <T> type
     *
     * @return the deserializer you input.
     */
    public <T> AbstractDeserializer<T> addEntry(AbstractDeserializer<T> abstractDeserializer){
        if (!registry.containsKey(abstractDeserializer.getResultTarget())){
            registry.put(abstractDeserializer.getResultTarget(), new HashMap<>());
        }
        registry.get(abstractDeserializer.getResultTarget()).put(abstractDeserializer.getRegisterName(), abstractDeserializer);
        return abstractDeserializer;
    }

    /**
     * make a deserializer default
     *
     * @param abstractDeserializer the deserializer
     * @param <T> type
     *
     * @return the deserializer you input.
     */
    public <T> AbstractDeserializer<T> markDefault(AbstractDeserializer<T> abstractDeserializer){
        defaults.put(abstractDeserializer.getResultTarget(), abstractDeserializer);
        return abstractDeserializer;
    }

    /**
     * Add a deserializer and mark it as default.
     *
     * @param abstractDeserializer the deserializer
     * @param <T> type
     *
     * @return the deserializer you input.
     */
    public <T> AbstractDeserializer<T>  addDefaultEntry(AbstractDeserializer<T> abstractDeserializer){
        addEntry(abstractDeserializer);
        markDefault(abstractDeserializer);
        return abstractDeserializer;
    }

    /**
     * @param clazz the class. Should not be primitive.
     * @param jsonElement the json element.
     * @param <T> the type.
     *
     * @return the value obj.
     *
     * @throws JsonDeserializeException the exception. Error format? Unexpected context?
     */
    public <T> T decode(Class<T> clazz, JsonElement jsonElement) throws JsonDeserializeException {
        ClassHelper.forceInitAll(clazz);
        clazz = DeserializerBuilder.avoidPrimitive(clazz);

        try {
            if (clazz.isArray()){
                Class<?> clazzComponentType = clazz.getComponentType();
                if (jsonElement.isJsonArray()){
                    JsonArray array = jsonElement.getAsJsonArray();
                    Object arrayToReturn = Array.newInstance(clazzComponentType, array.size());
                    for(int index = 0 ; index < array.size() ; index++){
                        Array.set(arrayToReturn, index, decode(clazzComponentType, array.get(index)));
                    }
                    return clazz.cast(arrayToReturn);
                }else {
                    Object arrayToReturn = Array.newInstance(clazzComponentType, 1);
                    Array.set(arrayToReturn, 0, decode(clazzComponentType, jsonElement));
                    return clazz.cast(arrayToReturn);
                }
            } else {
                if (jsonElement.isJsonObject()){
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has("type")){
                        ResourceLocation decoderName = parseLocation(jsonObject.get("type").getAsString());
                        if (!registry.containsKey(clazz)) throw new JsonDeserializeException(jsonObject, "Deserializer for " + clazz +" , is not registered.");
                        HashMap<ResourceLocation, AbstractDeserializer<?>> typedRegistry = registry.get(clazz);
                        if (!typedRegistry.containsKey(decoderName)) throw new JsonDeserializeException(jsonObject, "Deserializer for " + clazz +" named " + decoderName + " could not be found.");
                        return clazz.cast(typedRegistry.get(decoderName).deserialize(jsonObject));
                    }else {
                        if (defaults.containsKey(clazz)){
                            return clazz.cast(defaults.get(clazz).deserialize(jsonObject));
                        }else {
                            throw new JsonDeserializeException(jsonObject, "Required Field \"type\" is not found in such a JsonObject. No default founded for Type " + clazz + ".");
                        }
                    }
                }else {
                    if (defaults.containsKey(clazz)){
                        return clazz.cast(defaults.get(clazz).deserialize(jsonElement));
                    }else {
                        throw new JsonDeserializeException(jsonElement, "Required Field \"type\" is not found in such a JsonObject. No default founded for Type " + clazz + ".");
                    }
                }
            }
        }catch (Throwable throwable){
            if (throwable instanceof JsonDeserializeException) throw throwable;
            else throw new JsonDeserializeException(jsonElement, "unexpected Exception when decoding json, ", throwable);
        }
    }


    public <T> T decodeSilently(Class<T> clazz, JsonElement jsonElement){
        JsonDeserializeException exception = null;
        try {
            return decode(clazz, jsonElement);
        } catch (JsonDeserializeException e) {
            exception = e;
        }
        throw new UnsupportedOperationException(exception);
    }

    /**
     * @param defaultDomain the default domain of one deserializer manager
     *
     * Constructor, with build-in default deserializers.
     */
    public DeserializerManager(String defaultDomain){
        this(defaultDomain, (manager)->{});
    }


    /**
     * @param defaultDomain the default domain of one deserializer manager
     * @param consumer register the buildIn deserializer
     *
     * Constructor, with build-in default deserializers.
     */
    public DeserializerManager(String defaultDomain, Consumer<DeserializerManager> consumer){
        this.defaultDomain = defaultDomain;
        ResourceLocation GSON = new ResourceLocation("google", "primitive");
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, Integer.class, AbstractDeserializer.safeRun(JsonElement::getAsInt)));
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, Float.class, AbstractDeserializer.safeRun(JsonElement::getAsFloat)));
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, Double.class, AbstractDeserializer.safeRun(JsonElement::getAsDouble)));
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, Long.class, AbstractDeserializer.safeRun(JsonElement::getAsLong)));
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, Character.class, AbstractDeserializer.safeRun(JsonElement::getAsCharacter)));
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, Byte.class, AbstractDeserializer.safeRun(JsonElement::getAsByte)));
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, String.class, AbstractDeserializer.safeRun(JsonElement::getAsString)));
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, Boolean.class, AbstractDeserializer.safeRun(JsonElement::getAsBoolean)));
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, Short.class, AbstractDeserializer.safeRun(JsonElement::getAsShort)));
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, BigInteger.class, AbstractDeserializer.safeRun(JsonElement::getAsBigInteger)));
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, BigDecimal.class, AbstractDeserializer.safeRun(JsonElement::getAsBigDecimal)));
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, Number.class, AbstractDeserializer.safeRun(JsonElement::getAsNumber)));
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, Void.class, AbstractDeserializer.safeRun((jsonElement)->null)));
        this.addDefaultEntry(new AbstractDeserializer<>(GSON, JsonObject.class, AbstractDeserializer.safeRun(JsonElement::getAsJsonObject)));
        consumer.accept(this);
    }

    public DeserializerManager(){
        this("minecraft");
    }

    public ResourceLocation parseLocation(String toSplit)
    {
        String[] astring = new String[] {defaultDomain, toSplit};
        int i = toSplit.indexOf(':');

        if (i >= 0)
        {
            astring[1] = toSplit.substring(i + 1);

            if (i > 1)
            {
                astring[0] = toSplit.substring(0, i);
            }
        }

        return new ResourceLocation(astring[0], astring[1]);
    }

    public <T, F> AbstractDeserializer<T> map(final Class<T> tClass, final Class<F> fClass, final ResourceLocation resourceLocation, final Function<F, T> function){
        return new AbstractDeserializer<T>(resourceLocation, tClass, jsonElement -> function.apply(DeserializerManager.this.decode(fClass, jsonElement)));
    }

    public static JsonElement getFromPath(JsonObject jsonObject, String path){
        int i = path.indexOf('.');
        if (i == -1){
            return jsonObject.get(path);
        }else {
            String first = path.substring(0, i-1);
            JsonElement element1 = jsonObject.get(first);
            if (element1 instanceof JsonObject){
                return getFromPath((JsonObject)element1, path.substring(i+1));
            }else return null;
        }
    }


    /**
     * @param clazz the class, you should not use primitive type, although it is supported by the auto-boxing by rml.
     * @param name the register name of a named Deserializer
     * @param <T> the type of decoded. Yes, it could not be primitive.
     *
     * @return the builder.
     */
    public <T> DeserializerBuilder<T> named(Class<T> clazz, ResourceLocation name){
        return new DeserializerBuilder<>(this, clazz, name);
    }
}
