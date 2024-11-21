package rml.loader.deserialize.craft.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import rml.jrx.announces.PrivateAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/3 23:24
 **/
@PrivateAPI
public class SmeltRecipe extends NamedEmptyRecipeImpl {
    public SmeltRecipe(@Nonnull ItemStack input, @Nonnull ItemStack output, float xp)
    {
        GameRegistry.addSmelting(input, output, xp);
    }
    @SuppressWarnings("unused")
    public static class Factory implements IRecipeFactory {
        @Override
        public IRecipe parse(JsonContext context, JsonObject json) {
            try{
                ItemStack outPut= CraftingHelper.getItemStack(json.getAsJsonObject("output"),context);
                float xp=json.get("xp").getAsFloat();
                ItemStack input=CraftingHelper.getItemStack(json.getAsJsonObject("input"),context);
                return new SmeltRecipe(input,outPut,xp);
            }catch (Exception e){
                throw new JsonSyntaxException(e);
            }
        }
    }
}
