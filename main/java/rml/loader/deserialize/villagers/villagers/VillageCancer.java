package rml.loader.deserialize.villagers.villagers;

import rml.jrx.announces.BeDiscovered;
import rml.loader.api.world.villagers.IVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import rml.deserializer.AbstractDeserializer;
import rml.loader.deserialize.Deserializer;
import rml.deserializer.Record;

/**
 * @Project CustomVillage
 * @Author Hileb
 * @Date 2023/8/16 11:39
 **/
@BeDiscovered(BeDiscovered.PRE_INIT)
public class VillageCancer implements IVillager {
    public static final AbstractDeserializer<IVillager> DESERIALIZER = Deserializer.named(IVillager.class, new ResourceLocation("minecraft","cancer"))
            .record(VillageCancer.class).build();
    public ResourceLocation profession;
    public String name;
    @Record({"profession", "career"})
    public VillageCancer(ResourceLocation professionIn, String nameIn){
        profession=professionIn;
        name=nameIn;
    }

    @Override
    public void run(IForgeRegistry<VillagerRegistry.VillagerProfession> r) {
        VillagerRegistry.VillagerProfession profession_v = r.getValue(this.profession);
        new VillagerRegistry.VillagerCareer(profession_v, name);
    }
}
