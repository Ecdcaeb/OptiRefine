package rml.loader.api.event;

import rml.jrx.announces.PrivateAPI;
import rml.jrx.announces.PublicAPI;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/5 22:57
 **/
@PublicAPI
public class CraftingHelperInitEvent extends Event {
    @PrivateAPI public CraftingHelperInitEvent(){}
    @PrivateAPI public static void post(){
        MinecraftForge.EVENT_BUS.post(new CraftingHelperInitEvent());
    }
    @PublicAPI public void register(ResourceLocation name, IConditionFactory fac) {
        CraftingHelper.register(name,fac);
    }
    @PublicAPI public void register(ResourceLocation name, IRecipeFactory fac) {
        CraftingHelper.register(name,fac);
    }
    @PublicAPI public void register(ResourceLocation name, IIngredientFactory fac) {
        CraftingHelper.register(name,fac);
    }
}
