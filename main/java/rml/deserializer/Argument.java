package rml.deserializer;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PublicAPI;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/7/27 12:49
 **/

@EarlyClass
@PublicAPI
public abstract class Argument<T> implements DeserializerBuilder.IAction {
    public final String name;

    public Argument(String name) {
        this.name = name;
    }

    @SuppressWarnings("unchecked")
    public T cast(DeserializerBuilder.Context context) {
        return (T) context.get(this.name);
    }

    public static <E> Argument<E> type(String name, Class<E> clazz) {
        return new Argument<E>(name) {
            @Override
            public void execute(DeserializerManager manager, JsonObject jsonObject, DeserializerBuilder.Context context) throws JsonDeserializeException {
                JsonElement element = DeserializerManager.getFromPath(jsonObject, name);
                if (element != null){
                    try {
                        final Class<?> clazz2 = DeserializerBuilder.avoidPrimitive(clazz);
                        Object obj = manager.decode(clazz2, element);
                        context.put(name, obj);
                    } catch (JsonDeserializeException e) {
                        throw new JsonDeserializeException(jsonObject, "field " + name + " decoding error!", e);
                    }
                } else {
                    throw new JsonDeserializeException(jsonObject, "field " + name + " is required");
                }
            }
        };
    }

    public static Argument<Integer> integer(String name, Function<Integer, Throwable> predicate) {
        return new Argument<Integer>(name) {
            @Override
            public void execute(DeserializerManager manager, JsonObject jsonObject, DeserializerBuilder.Context context) throws JsonDeserializeException {
                JsonElement element = DeserializerManager.getFromPath(jsonObject, name);
                if (element != null){
                    Integer obj = null;
                    try {
                        obj = manager.decode(Integer.class, element);
                    } catch (JsonDeserializeException e) {
                        throw new JsonDeserializeException(jsonObject, "field " + name + " decoding error!", e);
                    } finally {
                        if (obj != null) {
                            Throwable throwable = predicate.apply(obj);
                            if (throwable != null) throw new JsonDeserializeException(jsonObject, throwable);
                            context.put(name, obj);
                        }
                    }
                } else {
                    throw new JsonDeserializeException(jsonObject, "field " + name + " is required");
                }
            }
        };
    }

    public static Argument<Integer> integer(String name) {
        return integer(name, (i) -> null);
    }

    public static Argument<Integer> integer(String name, int min, int max) {
        return integer(name, (i) -> (i > max || i < min) ? new IllegalArgumentException("integer must in range [" + min + "," + max + "], current value : " + i) : null);
    }

    public static Argument<Integer> positiveInteger(String name) {
        return integer(name, 1, Integer.MAX_VALUE);
    }

    public static Argument<Float> floatArgument(String name, Function<Float, Throwable> predicate){
        return new Argument<Float>(name) {
            @Override
            public void execute(DeserializerManager manager, JsonObject jsonObject, DeserializerBuilder.Context context) throws JsonDeserializeException {
                JsonElement element = DeserializerManager.getFromPath(jsonObject, name);
                if (element != null){
                    Float obj = null;
                    try{
                        obj = manager.decode(Float.class, element);
                    }catch (JsonDeserializeException e){
                        throw new JsonDeserializeException(jsonObject, "field " + name + " decoding error!", e);
                    }finally {
                        if (obj != null){
                            Throwable throwable = predicate.apply(obj);
                            if (throwable != null) throw new JsonDeserializeException(jsonObject, throwable);
                            context.put(name, obj);
                        }
                    }
                }else {
                    throw new JsonDeserializeException(jsonObject, "field " + name +" is required");
                }
            }
        };
    }

    public static Argument<Float> floatArgument(String name){
        return floatArgument(name, (i)->null);
    }

    public static Argument<Float> floatArgument(String name, float min, float max){
        return floatArgument(name, (i)->(i > max || i < min) ? new IllegalArgumentException("float must in range ["+min+","+max+"], current value : "+i) : null);
    }

    public static Argument<Float> positiveFloat(String name){
        return floatArgument(name, 0, Float.MAX_VALUE);
    }

    public static Argument<Boolean> bool(String name, Function<Boolean, Throwable> predicate){
        return new Argument<Boolean>(name) {
            @Override
            public void execute(DeserializerManager manager, JsonObject jsonObject, DeserializerBuilder.Context context) throws JsonDeserializeException {
                JsonElement element = DeserializerManager.getFromPath(jsonObject, name);
                if (element != null){
                    Boolean obj = null;
                    try{
                        obj = manager.decode(Boolean.class, element);
                    }catch (JsonDeserializeException e){
                        throw new JsonDeserializeException(jsonObject, "field " + name + " decoding error!", e);
                    }finally {
                        if (obj != null){
                            Throwable throwable = predicate.apply(obj);
                            if (throwable != null) throw new JsonDeserializeException(jsonObject, throwable);
                            context.put(name, obj);
                        }
                    }
                }else {
                    throw new JsonDeserializeException(jsonObject, "field " + name +" is required");
                }
            }
        };
    }

    public static<T> Argument<Map<String, T>> map(String name, final Class<T> clazz){
        return new Argument<Map<String, T>>(name) {
            @Override
            public void execute(DeserializerManager manager, JsonObject jsonObject, DeserializerBuilder.Context context) throws JsonDeserializeException {
                JsonElement element = DeserializerManager.getFromPath(jsonObject, name);
                if (element != null){
                    if (!element.isJsonObject()) throw new JsonDeserializeException(jsonObject, "field" + name + "is required be json object.");
                    try {
                        Map<String, T> map = element.getAsJsonObject().entrySet().stream().collect(Collectors.toMap((entry) -> entry.getKey(), (entry) -> manager.decodeSilently(clazz, entry.getValue())));
                        context.put(name, map);
                    }catch (Exception e){
                        throw new JsonDeserializeException(element, e);
                    }
                }else {
                    throw new JsonDeserializeException(jsonObject, "field " + name +" is required");
                }
            }
        };
    }

    public static<T> Argument<List<T>> list(String name, final Class<T> clazz){
        return new Argument<List<T>>(name) {
            @Override
            public void execute(DeserializerManager manager, JsonObject jsonObject, DeserializerBuilder.Context context) throws JsonDeserializeException {
                JsonElement element = DeserializerManager.getFromPath(jsonObject, name);
                if (element != null){
                    try {
                        Class<?> arrayCls = Array.newInstance(clazz, 0).getClass();
                        @SuppressWarnings("unchecked")
                        List<T> list = Lists.newArrayList((T[])manager.decode(arrayCls, element));
                        context.put(name, list);
                    }catch (Exception e){
                        throw new JsonDeserializeException(element, e);
                    }
                }else {
                    throw new JsonDeserializeException(jsonObject, "field " + name +" is required");
                }
            }
        };
    }

}
