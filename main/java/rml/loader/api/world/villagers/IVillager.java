package rml.loader.api.world.villagers;

import rml.jrx.announces.PublicAPI;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @Project CustomVillage
 * @Author Hileb
 * @Date 2023/8/16 10:19
 **/
@PublicAPI
public interface IVillager {
    void run(IForgeRegistry<VillagerRegistry.VillagerProfession> r);
}
