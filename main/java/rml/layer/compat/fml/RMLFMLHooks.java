package rml.layer.compat.fml;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.JsonContext;
import rml.deserializer.JsonDeserializeException;
import rml.jrx.announces.RewriteWhenCleanroom;
import rml.loader.api.RMLBus;
import rml.loader.api.event.early.FMLBeforeStageEvent;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.LoaderState;
import rml.loader.deserialize.Deserializer;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/2/10 0:13
 **/

@RewriteWhenCleanroom
public class RMLFMLHooks {
    @SuppressWarnings("unused")
    public static void beforeFMLBusEventSending(LoadController controller, LoaderState state, Object[] args){
        if (state.hasEvent()){
            RMLBus.BUS.post(new FMLBeforeStageEvent(state, args));
        }
    }

    public static class LateHooks{
        public static ItemStack getItemStack(JsonObject jsonObject, JsonContext jsonContext){
            try{
                if (jsonObject.has("item") && jsonObject.get("item").isJsonPrimitive()){
                    jsonObject.addProperty("item", jsonContext.appendModId(jsonObject.get("item").getAsString()));
                }
                return Deserializer.decode(ItemStack.class, jsonObject);
            }catch (JsonDeserializeException e){
                throw new JsonSyntaxException(e);
            }
        }
    }
}
