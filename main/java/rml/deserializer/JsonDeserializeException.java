package rml.deserializer;

import com.google.gson.JsonElement;
import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PublicAPI;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/7/14 10:29
 **/

@EarlyClass
@PublicAPI
public class JsonDeserializeException extends Exception{
    public JsonDeserializeException(JsonElement element, Throwable throwable){
        super(String.valueOf(element), throwable);
    }

    public JsonDeserializeException(JsonElement element, String s){
        super(s + ", " + element);
    }
    public JsonDeserializeException(JsonElement element){
        super(String.valueOf(element));
    }
    public JsonDeserializeException(JsonElement element, String s, Throwable throwable){
        super(s + ", " + element, throwable);
    }
}
