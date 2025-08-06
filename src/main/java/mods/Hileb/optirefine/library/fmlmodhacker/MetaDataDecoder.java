package mods.Hileb.optirefine.library.fmlmodhacker;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.versioning.VersionParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unused")
public class MetaDataDecoder {
    public static Map<String, ModMetadata> decodeMcModInfo(InputStream stream) {
        JsonElement element = JsonParser.parseReader(new InputStreamReader(stream));
        if (element instanceof JsonArray array) {
            Map<String, ModMetadata> map = HashMap.newHashMap(array.size());
            for (JsonElement element1 : array) {
                ModMetadata modMetadata = decodeMetaData(element);
                map.put(modMetadata.modId, modMetadata);
            }
            return map;
        } else {
            ModMetadata modMetadata = decodeMetaData(element);
            return Map.of(modMetadata.modId, modMetadata);
        }
    }

    
    @SuppressWarnings("deprecation")
    public static ModMetadata decodeMetaData(JsonElement jsonElement){
        JsonObject json = jsonElement.getAsJsonObject();
        ModMetadata metadata = new ModMetadata();

        //basic message
        metadata.modId = json.get("modid").getAsString();
        metadata.name = json.get("name").getAsString();

        //optional message
        if (json.has("description")) metadata.description = json.get("description").getAsString();
        if (json.has("credits")) metadata.credits = json.get("credits").getAsString();
        if (json.has("url")) metadata.url = json.get("url").getAsString();
        if (json.has("updateJSON")) metadata.updateJSON = json.get("updateJSON").getAsString();
        if (json.has("logoFile")) metadata.logoFile = json.get("logoFile").getAsString();
        if (json.has("version")) metadata.version = json.get("version").getAsString();
        if (json.has("parent")) metadata.parent = json.get("parent").getAsString();
        if (json.has("useDependencyInformation")) metadata.useDependencyInformation = json.get("useDependencyInformation").getAsBoolean();
        if (metadata.useDependencyInformation){
            if (json.has("requiredMods")){
                for(JsonElement element : json.getAsJsonArray("requiredMods")){
                    metadata.requiredMods.add(VersionParser.parseVersionReference(element.getAsString()));
                }
            }
            if (json.has("dependencies")){
                for(JsonElement element : json.getAsJsonArray("dependencies")){
                    metadata.dependencies.add(VersionParser.parseVersionReference(element.getAsString()));
                }
            }
            if (json.has("dependants")){
                for(JsonElement element : json.getAsJsonArray("dependants")){
                    metadata.dependants.add(VersionParser.parseVersionReference(element.getAsString()));
                }
            }
        }
        if (json.has("authorList")){
            for(JsonElement element : json.getAsJsonArray("authorList")){
                metadata.authorList.add(element.getAsString());
            }
        }
        if (json.has("screenshots")){ // this field was never used
            JsonArray array = json.getAsJsonArray("screenshots");
            int size = array.size();
            String[] screenshots = new String[size];
            for (int i = 0; i < size; i++){
                screenshots[i] = array.get(i).getAsString();
            }
            metadata.screenshots = screenshots;
        }else metadata.screenshots = new String[0];
        if (json.has("updateUrl")){ // this field is out of date
            metadata.updateUrl = json.get("updateUrl").getAsString();
        }

        return metadata;
    }
}
