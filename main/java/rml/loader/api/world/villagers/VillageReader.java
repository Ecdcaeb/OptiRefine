package rml.loader.api.world.villagers;

import com.google.gson.JsonObject;
import rml.jrx.announces.PublicAPI;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * @Project CustomVillage
 * @Author Hileb
 * @Date 2023/8/16 10:23
 **/
@PublicAPI
public abstract class VillageReader extends IForgeRegistryEntry.Impl<VillageReader> {
    public VillageReader(){
    }
    public abstract IVillager load(JsonObject json);
}
