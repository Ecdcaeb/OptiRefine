package rml.deserializer;

import com.google.gson.JsonElement;
import net.minecraft.util.ResourceLocation;
import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PublicAPI;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/7/14 9:57
 **/
@EarlyClass
@PublicAPI
public class AbstractDeserializer<T>{
    private final ResourceLocation resourceLocation;
    private final Class<T> target;
    private final IDeserializer<T> function;
    public AbstractDeserializer(ResourceLocation registerName, Class<T> target, IDeserializer<T> function){
        this.resourceLocation = registerName;
        this.target = DeserializerBuilder.avoidPrimitive(target);
        this.function = function;
    }
    public Class<T> getResultTarget(){
        return this.target;
    }
    public T deserialize(JsonElement json) throws JsonDeserializeException {
        return this.function.apply(json);
    }

    public ResourceLocation getRegisterName(){
        return this.resourceLocation;
    }

    @FunctionalInterface
    public interface IDeserializer<V>{
        V apply(JsonElement jsonElement) throws JsonDeserializeException;
    }

    public static <T> IDeserializer<T> safeRun(IDeserializer<T> deserializer){
        return (element)->{
            try{
                return deserializer.apply(element);
            }catch (Exception e){
                if (e instanceof JsonDeserializeException) throw e;
                else{
                    throw new JsonDeserializeException(element, e);
                }
            }
        };
    }
}
