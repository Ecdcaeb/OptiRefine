package rml.deserializer;

import com.google.common.primitives.Primitives;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import rml.internal.net.minecraftforge.common.util.LazyOptional;
import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PublicAPI;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/7/14 10:55
 **/

@EarlyClass
@PublicAPI
public class DeserializerBuilder<T> {
    private DeserializerManager manager;
    private Class<T> clazz;
    private ResourceLocation resourceLocation;
    private HashSet<IAction> actions = new HashSet<>();
    private IJsonObjectFunction<T> function = (context) -> null;
    private boolean isDefault = false;
    public DeserializerBuilder(DeserializerManager manager, Class<T> clazz, ResourceLocation resourceLocation){
        this.manager = manager;
        this.clazz = DeserializerBuilder.avoidPrimitive(clazz);
        this.resourceLocation = resourceLocation;
    }

    public DeserializerBuilder<T> action(IAction action){
        this.actions.add(action);
        return this;
    }

    public DeserializerBuilder<T> require(final Class<?> clazz, final String name){
        return this.action(Argument.type(name, clazz));
    }

    public DeserializerBuilder<T> require(Argument<?> argument){
        return this.action(argument);
    }

    public DeserializerBuilder<T> check(final Function<Context, JsonDeserializeException> function){
        return this.action(((manager1, jsonObject, context) -> {
            JsonDeserializeException exception = function.apply(context);
            if (exception != null) throw exception;
        }));
    }

    public DeserializerBuilder<T> optionalWhen(final Class<?> clazz,final String name,final Context.ToBooleanFunction isNotRequired){
        final Class<?> type = DeserializerBuilder.avoidPrimitive(clazz);
        return this.action(((manager, jsonObject, context) -> {
            JsonElement element = DeserializerManager.getFromPath(jsonObject, name);
            if (element != null){
                try{
                    Object obj = manager.decode(type, element);
                    context.put(name, obj);
                }catch (JsonDeserializeException e){
                    if (!isNotRequired.apply(context)){
                        throw new JsonDeserializeException(jsonObject, "field " + name + " decoding error!", e);
                    }
                }
            }else if (!isNotRequired.apply(context)){
                throw new JsonDeserializeException(jsonObject, "field " + name +" is required");
            }
        }));
    }

    public DeserializerBuilder<T> optionalWhen(final Argument<?> argument, final Context.ToBooleanFunction isNotRequired) {
        return this.action((manager, jsonObject, context) -> {
            try{
                argument.execute(manager, jsonObject, context);
            }catch (Throwable throwable){
                if (!isNotRequired.apply(context)){
                    throw new JsonDeserializeException(jsonObject, throwable);
                }
            }
        });
    }

    public DeserializerBuilder<T> optional(Class<?> clazz, String name){
        return optionalWhen(clazz, name, Context.ToBooleanFunction.TRUE);
    }

    public <V> DeserializerBuilder<T> optionalDefaultWhen(final Class<V> clazz, final String name, final Context.ToBooleanFunction isNotRequired, final V defaultValue){
        final Class<?> type = DeserializerBuilder.avoidPrimitive(clazz);
        return this.action(((manager, jsonObject, context) -> {
            JsonElement element = DeserializerManager.getFromPath(jsonObject, name);
            if (element != null){
                try{
                    Object obj = manager.decode(type, element);
                    context.put(name, obj);
                }catch (JsonDeserializeException e){
                    if (!isNotRequired.apply(context)){
                        throw new JsonDeserializeException(jsonObject, "field " + name + " decoding error!", e);
                    }
                }
            }else if (!isNotRequired.apply(context)){
                context.put(name, defaultValue);
            }
        }));
    }

    public <V> DeserializerBuilder<T> optionalDefault(Class<V> clazz, String name, V defaultValue){
        return optionalDefaultWhen(clazz, name, Context.ToBooleanFunction.TRUE, defaultValue);
    }

    public <V> DeserializerBuilder<T> optionalDefaultLazyWhen(final Class<V> clazz, final String name, final Context.ToBooleanFunction isNotRequired, final LazyOptional<V> defaultValue){
        final Class<?> type = DeserializerBuilder.avoidPrimitive(clazz);
        return this.action(((manager, jsonObject, context) -> {
            JsonElement element = DeserializerManager.getFromPath(jsonObject, name);
            if (element != null){
                try{
                    Object obj = manager.decode(type, element);
                    context.put(name, obj);
                }catch (JsonDeserializeException e){
                    if (!isNotRequired.apply(context)){
                        throw new JsonDeserializeException(jsonObject, "field " + name + " decoding error!", e);
                    }
                }
            }else if (!isNotRequired.apply(context)){
                if (defaultValue.isPresent()) defaultValue.ifPresent((value)->context.put(name, value));
                else throw new JsonDeserializeException(jsonObject, "field " + name + " is absent, and it's default value suppler, LazyOptional, works not well.");
            }
        }));
    }

    public <V> DeserializerBuilder<T> optionalDefaultLazy(Class<V> clazz, String name, final LazyOptional<V> defaultValue){
        return optionalDefaultLazyWhen(clazz, name, Context.ToBooleanFunction.TRUE, defaultValue);
    }

    public DeserializerBuilder<T> decode(IJsonObjectFunction<T> function){
        this.function = function;
        return this;
    }

    public DeserializerBuilder<T> record(Class<? extends T> clazzIn){
        for (Constructor<?> method : clazzIn.getConstructors()) {
            if (method.isAnnotationPresent(Record.class)) {
                Record record = method.getAnnotation(Record.class);
                if (record.value().length != method.getParameters().length)
                    throw new IllegalArgumentException("args list length not fit.");
                final String[] names = record.value();
                final Class<?>[] types = method.getParameterTypes();

                this.decode((context) -> {
                    Object[] toParameters = new Object[types.length];
                    for (int index = 0; index < types.length; index++) {
                        toParameters[index] = context.get(types[index], names[index]);
                    }
                    try {
                        return clazzIn.cast(method.newInstance(toParameters));
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new JsonDeserializeException(context.getJsonObject(), e);
                    }
                });
                return this;
            }
        }

        throw new IllegalArgumentException("Class is not a record.");
    }

    public DeserializerBuilder<T> markDefault(){
        this.isDefault = true;
        return this;
    }

    public AbstractDeserializer<T> build(){
        final DeserializerManager managerIn = this.manager;
        final IJsonObjectFunction<T> function = this.function;
        final HashSet<IAction> actions = this.actions;
        AbstractDeserializer<T> deserializer = new AbstractDeserializer<>(resourceLocation, clazz,
                (jsonElement)->{
                    if (!jsonElement.isJsonObject()) throw new JsonDeserializeException(jsonElement, "Only Support JsonObject.");
                    else {
                        final JsonObject jsonObject = jsonElement.getAsJsonObject();
                        final Context context = new Context(jsonObject);
                        for(IAction action : actions){
                            action.execute(managerIn, jsonObject, context);
                        }
                        return function.apply(context);
                    }
                }
        );
        if (this.isDefault) managerIn.addDefaultEntry(deserializer);
        else managerIn.addEntry(deserializer);
        return deserializer;
    }

    public static class Context{
        public JsonObject jsonObject;
        public HashMap<String, Object> environments;
        public Context(JsonObject jsonObject){
            this.jsonObject = jsonObject;
            this.environments = new HashMap<>();
        }

        public JsonObject getJsonObject(){
            return jsonObject;
        }

        public HashMap<String, Object> getEnvironments() {
            return environments;
        }

        public boolean ifPresent(String s){
            return environments.containsKey(s);
        }

        public boolean ifPresent(Class<?> clazz,String s){
            return environments.containsKey(s) && clazz.isInstance(environments.get(s));
        }

        public Object get(String s){
            return environments.get(s);
        }

        public <T> T get(Class<T> clazz, String s){
            return clazz.cast(environments.get(s));
        }

        public <T> T get(Class<T> clazz, String s, T defaultValue){
            if (ifPresent(clazz, s)) return get(clazz, s);
            return defaultValue;
        }

        public void put(String s, Object obj){
            environments.put(s, obj);
        }

        @FunctionalInterface
        public interface ToBooleanFunction{
            ToBooleanFunction TRUE = (o)->true;
            ToBooleanFunction FALSE = (o)->false;
            boolean apply(Context context);
        }

    }


    @FunctionalInterface
    public interface IAction{
        void execute(DeserializerManager manager, JsonObject jsonObject, Context context) throws JsonDeserializeException;
    }

    @FunctionalInterface
    public interface IJsonObjectFunction<T>{
        T apply(Context context) throws JsonDeserializeException;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> avoidPrimitive(Class<?> cls){
        return cls.isPrimitive() ? (Class<T>) Primitives.wrap(cls) : (Class<T>) cls;
    }
}
