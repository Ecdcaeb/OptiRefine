package rml.loader.api.mods;

import rml.jrx.announces.PrivateAPI;
import rml.jrx.announces.PublicAPI;
import rml.loader.api.mods.module.Module;
import rml.loader.api.mods.module.ModuleType;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/1/17 12:21
 **/
@Deprecated
@PrivateAPI
public class BuffedModIDContainer {
    @PublicAPI public HashSet<String> modids = new HashSet<>();
    @PublicAPI public HashMap<String, Module[]> opinions = new HashMap<>();

    @PublicAPI public void add(String modid){
        this.add(modid, ModuleType.getAllForDefault());
    }

    @PublicAPI public void add(String modid, Module[] opinion){
        modids.add(modid);
        opinions.put(modid, opinion);
    }

    @PublicAPI public ArrayList<ContainerHolder> getHolder(){
        ArrayList<ContainerHolder> containers=new ArrayList<>();
        for(ModContainer modContainer:Loader.instance().getModList()){
            if (modids.contains(modContainer.getModId())){
                containers.add(new ContainerHolder(modContainer, opinions.get(modContainer.getModId())));
            }
        }
        return containers;
    }

    @PublicAPI public void clear(){
        modids.clear();
        opinions.clear();
    }
}
