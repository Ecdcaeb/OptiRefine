package rml.layer.compat.crt;

import crafttweaker.CraftTweakerAPI;
import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PrivateAPI;
import rml.jrx.announces.PublicAPI;
import rml.loader.api.RMLBus;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/3/15 17:30
 **/

@EarlyClass
@PublicAPI
public class CrTZenClassRegisterEvent {
    @PublicAPI public void register(Class<?> clazz){
        CraftTweakerAPI.registerClass(clazz);
    }

    @PrivateAPI public static void post(){
        RMLBus.BUS.post(new CrTZenClassRegisterEvent());
    }

}
