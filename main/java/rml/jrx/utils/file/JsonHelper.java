package rml.jrx.utils.file;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import rml.deserializer.JsonDeserializeException;
import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PublicAPI;
import rml.jrx.utils.IteratorHelper;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/2/7 1:42
 **/
@EarlyClass
@PublicAPI
public class JsonHelper {


    @PublicAPI public static <T> T[] getAsArray(JsonElement element, JsonReader<T> singleReader){
        JsonArray array = element.getAsJsonArray();
        return getAsArray(element, singleReader);
    }

    @SuppressWarnings("unchecked")
    @PublicAPI public static <T> T[] getAsArray(JsonArray array, JsonReader<T> singleReader){
        return (T[])IteratorHelper.stream(array.iterator()).map(singleReader::read).toArray();
    }

    @PublicAPI public static JsonObject[] getJsonObjectArray(JsonArray array){
        int size = array.size();
        JsonObject[] result = new JsonObject[size];
        for(int i=0 ; i < size; i++){
            result[i] = array.get(i).getAsJsonObject();
        }
        return result;
    }

    @PublicAPI public static boolean getBoolean(JsonElement element){
        return element.getAsBoolean();
    }

    @PublicAPI public static boolean[] getBooleanArray(JsonElement element){
        JsonArray array = element.getAsJsonArray();
        int size=array.size();
        boolean[] result = new boolean[size];
        for(int i=0 ; i<size; i++){
            result[i] = getBoolean(array.get(i));
        }
        return result;
    }
    @PublicAPI public static int getInteger(JsonElement element){return element.getAsInt();}
    @PublicAPI public static int[] getIntegerArray(JsonElement element){
        JsonArray array = element.getAsJsonArray();
        int size = array.size();
        int[] result = new int[size];
        for(int i=0; i < size; i++){
            result[i] = getInteger(array.get(i));
        }
        return result;
    }
    @PublicAPI public static char getChar(JsonElement element){return element.getAsCharacter();}
    @PublicAPI public static char[] getCharArray(JsonElement element){
        JsonArray array = element.getAsJsonArray();
        int size = array.size();
        char[] result = new char[size];
        for(int i=0; i<size;i++){
            result[i] = getChar(array.get(i));
        }
        return result;
    }
    @PublicAPI public static String getString(JsonElement element){return element.getAsString();}
    @PublicAPI public static String[] getStringArray(JsonElement element){
        JsonArray array = element.getAsJsonArray();
        int size = array.size();
        String[] result = new String[size];
        for(int i = 0; i<size; i++){
            result[i] = getString(array.get(i));
        }
        return result;
    }
    @PublicAPI public static short getShort(JsonElement element){return element.getAsShort();}
    @PublicAPI public static short[] getShortArray(JsonElement element){
        JsonArray array = element.getAsJsonArray();
        int size = array.size();
        short[] result = new short[size];
        for(int i=0; i<size; i++){
            result[i] = getShort(array.get(i));
        }
        return result;
    }
    @PublicAPI public static float getFloat(JsonElement element){return element.getAsFloat();}
    @PublicAPI  public static float[] getFloatArray(JsonElement element){
        JsonArray array = element.getAsJsonArray();
        int size = array.size();
        float[] result = new float[size];
        for(int i = 0; i<size; i++){
            result[i] = getFloat(array.get(i));
        }
        return result;
    }
    @PublicAPI public static byte getByte(JsonElement element){return element.getAsByte();}
    @PublicAPI public static byte[] getByteArray(JsonElement element){
        JsonArray array = element.getAsJsonArray();
        int size = array.size();
        byte[] result = new byte[size];
        for(int i=0; i<size; i++){
            result[i] = getByte(array.get(i));
        }
        return result;
    }
    @PublicAPI public static double getDouble(JsonElement element){return element.getAsDouble();}
    @PublicAPI public static double[] getDoubleArray(JsonElement element){
        JsonArray array = element.getAsJsonArray();
        int size = array.size();
        double[] result = new double[size];
        for(int i=0; i<size; i++){
            result[i] = getDouble(array.get(i));
        }
        return result;
    }
    @PublicAPI public static long getLong(JsonElement element){return element.getAsLong();}
    @PublicAPI public static long[] getLongArray(JsonElement element){
        JsonArray array = element.getAsJsonArray();
        int size = array.size();
        long[] result = new long[size];
        for(int i=0; i<size; i++){
            result[i] = getLong(array.get(i));
        }
        return result;
    }
    @PublicAPI
    @FunctionalInterface
    public interface JsonReader<T>{
        T read(JsonElement jsonElement);
    }

    public static List<String> walkAndGetAllEntryPath(JsonObject json){
        LinkedList<String> list=new LinkedList<>();
        for(Map.Entry<String,JsonElement> elementEntry:json.entrySet()){
            if (elementEntry.getValue().isJsonObject()){
                //merge the path
                List<String> newList=walkAndGetAllEntryPath(elementEntry.getValue().getAsJsonObject());
                ListIterator<String> iterator= newList.listIterator();
                String activeString;
                while (iterator.hasNext()){
                    activeString=iterator.next();
                    list.add(elementEntry.getKey()+"."+activeString);
                }
                //end merge.
            }else {
                list.add(elementEntry.getKey());
            }
        }
        return list;
    }
    @Nullable
    public static JsonElement walkAndGetTheElement(JsonObject json, String path){
        String[] strings=path.split("\\.");
        if (strings.length==1) return json.get(strings[0]);
        else {
            return walkAndGetTheElement(json.getAsJsonObject(strings[0]),path.substring(strings[0].length()+1));
        }
    }
    public static boolean isList(JsonElement element){
        return element.isJsonArray();
    }
    public static String getRawValue(JsonElement element){
        return element.toString();
    }
    public static String[] getAsRawList(JsonElement element){
        return getAsArray(element, JsonHelper::getRawValue);
    }

    public static JsonObject getJson(String s){
        return (JsonObject) JSON_PARSER.parse(s);
    }
    public static JsonObject getJson(Reader s){
        return (JsonObject) JSON_PARSER.parse(s);
    }


    public static final JsonParser JSON_PARSER = new JsonParser();
    public static JsonArray getArray(Reader s){
        return (JsonArray) JSON_PARSER.parse(s);
    }

    public static JsonElement parse(Reader reader) throws JsonDeserializeException {
        try {
            return JSON_PARSER.parse(reader);
        }catch (JsonSyntaxException e){
            try {
                throw new JsonDeserializeException(null, new String(IOUtils.toByteArray(reader, StandardCharsets.UTF_8)), e);
            } catch (IOException ex) {
                throw new JsonDeserializeException(null, e);
            }
        }
    }
}