package rml.loader.deserialize.craft.recipe;

import rml.jrx.announces.PrivateAPI;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashSet;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/3 23:20
 **/
@PrivateAPI
public class NamedEmptyRecipeImpl extends IForgeRegistryEntry.Impl<IRecipe>  implements IRecipe {
    public static HashSet<NamedEmptyRecipeImpl> removeCaches=new HashSet<>();
    public NamedEmptyRecipeImpl(){
        removeCaches.add(this);
    }
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
}
