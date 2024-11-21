package rml.loader.deserialize.craft.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import rml.jrx.announces.PrivateAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/4 12:27
 **/
@PrivateAPI
@SuppressWarnings("all")
public class SimpleBrewRecipe extends NamedEmptyRecipeImpl implements IBrewingRecipe {
    public ItemStack output;
    public Ingredient input;
    public Ingredient ingredient;
    public SimpleBrewRecipe(ItemStack output,Ingredient input,Ingredient ingredient){
        this.output=output;
        this.input=input;
        this.ingredient=ingredient;
        BrewingRecipeRegistry.addRecipe(this);
    }
    @Override
    public boolean isInput(@Nonnull ItemStack input) {
        return this.input.apply(input);
    }

    @Override
    public boolean isIngredient(@Nonnull ItemStack ingredient) {
        return this.ingredient.apply(ingredient);
    }

    @Nonnull
    @Override
    public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
        return output.copy();
    }

    @SuppressWarnings("unused")
    public static class Factory implements IRecipeFactory{

        @Override
        public IRecipe parse(JsonContext context, JsonObject json) {
            try{
                ItemStack output= CraftingHelper.getItemStack(json.getAsJsonObject("output"),context);
                Ingredient input= CraftingHelper.getIngredient(json.getAsJsonObject("input"),context);
                Ingredient ingredient= CraftingHelper.getIngredient(json.getAsJsonObject("ingredient"),context);
                return new SimpleBrewRecipe(output,input,ingredient);
            }catch (Exception e){
                throw new JsonSyntaxException(e);
            }
        }
    }
}
