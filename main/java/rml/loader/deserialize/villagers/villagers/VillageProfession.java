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
 * @Date 2023/8/16 11:45
 **/
@BeDiscovered(BeDiscovered.PRE_INIT)
public class VillageProfession implements IVillager {
    public static final AbstractDeserializer<IVillager> DESERIALIZER = Deserializer.named(IVillager.class, new ResourceLocation("minecraft","profession"))
            .record(VillageProfession.class).build();
    public ResourceLocation professionName;
    public ResourceLocation villageTexture;
    public ResourceLocation zombieTexture;

    @Record({"professionName", "villageTexture", "zombieTexture"})
    public VillageProfession(ResourceLocation professionIn, ResourceLocation villageTextureIn, ResourceLocation  zombieTextureIn){
        professionName =professionIn;
        villageTexture=villageTextureIn;
        zombieTexture=zombieTextureIn;
    }

    @Override
    public void run(IForgeRegistry<VillagerRegistry.VillagerProfession> r) {
        r.register(new VillagerRegistry.VillagerProfession(professionName.toString(),villageTexture.toString(),zombieTexture.toString()));
    }
}
