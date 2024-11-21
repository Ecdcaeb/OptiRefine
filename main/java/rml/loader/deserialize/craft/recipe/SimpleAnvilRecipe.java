package rml.loader.deserialize.craft.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import rml.jrx.announces.PrivateAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/4 12:36
 **/
@PrivateAPI
public class SimpleAnvilRecipe extends NamedEmptyRecipeImpl {
    public static final HashSet<SimpleAnvilRecipe> recipes=new HashSet<>();
    public Ingredient left;
    public Ingredient right;
    public ItemStack output;
    public int level;
    public SimpleAnvilRecipe(Ingredient slot1, Ingredient slot2, ItemStack output, int level){
        this.level=level;
        this.left=slot1;
        this.right=slot2;
        this.output=output;
        recipes.add(this);
    }
    @SubscribeEvent
    public static void onRecipe(AnvilUpdateEvent event){
        for(SimpleAnvilRecipe recipe:recipes){
            if (recipe.left.apply(event.getLeft()) && recipe.right.apply(event.getRight())){
                event.setOutput(recipe.output.copy());
                event.setCost(recipe.level);
                return;
            }
        }
    }
    @SuppressWarnings("unused")
    public static class Factory implements IRecipeFactory {
        @Override
        public IRecipe parse(JsonContext context, JsonObject json) {
            try{
                Ingredient a1= CraftingHelper.getIngredient(json.getAsJsonObject("right"),context);
                Ingredient a2= CraftingHelper.getIngredient(json.getAsJsonObject("left"),context);
                ItemStack output= CraftingHelper.getItemStack(json.getAsJsonObject("output"),context);
                int level=json.get("level").getAsInt();
                return new SimpleAnvilRecipe(a2,a1,output,level);
            }catch (Exception e){
                throw new JsonSyntaxException(e);
            }
        }
    }
}
