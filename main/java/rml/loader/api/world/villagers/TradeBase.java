package rml.loader.api.world.villagers;

import com.google.gson.JsonObject;
import rml.jrx.announces.BeDiscovered;
import rml.jrx.reflection.jvm.ReflectionHelper;
import rml.jrx.utils.values.RandomIntSupplier;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import rml.deserializer.AbstractDeserializer;
import rml.loader.deserialize.Deserializer;
import rml.deserializer.DeserializerBuilder;
import rml.deserializer.JsonDeserializeException;

import java.util.List;

/**
 * @Project CustomVillage
 * @Author Hileb
 * @Date 2023/8/16 10:44
 **/
@SuppressWarnings("all")
public class TradeBase {
    @BeDiscovered(BeDiscovered.PRE_INIT)
    public static class TradeHandlers implements IVillager {
        public static final AbstractDeserializer<IVillager> DESERIALIZER = Deserializer.named(IVillager.class, new ResourceLocation("rml", "trade"))
                .optionalDefault(Integer.class, "level", 1)
                .require(ResourceLocation.class, "profession")
                .require(String.class, "career")
                .require(EntityVillager.ITradeList[].class, "trade")
                .decode((context)-> new TradeHandlers(
                                context.get(ResourceLocation.class, "profession"),
                                context.get(String.class, "career"),
                                context.get(Integer.class, "level"),
                                context.get(EntityVillager.ITradeList[].class, "trade")))
                .build();

        public ResourceLocation profession;
        public String career;
        public EntityVillager.ITradeList[] list;
        public int level;

        public TradeHandlers(ResourceLocation professionIn, String careerIn, int levelIn, EntityVillager.ITradeList[] listIn){
            profession = professionIn;
            list = listIn;
            level = levelIn;
            career = careerIn;
        }
        @Override
        public void run(IForgeRegistry<VillagerRegistry.VillagerProfession> r) {
            VillagerRegistry.VillagerProfession profession_v = r.getValue(this.profession);

            for(VillagerRegistry.VillagerCareer career1 : (List<VillagerRegistry.VillagerCareer>) ReflectionHelper.getPrivateValue(VillagerRegistry.VillagerProfession.class, profession_v, "careers")){
                if (career1.getName().equals(career)){
                    career1.addTrade(level,this.list);
                }
            }
        }
    }

    public static ItemStack loadItemStack(JsonObject jsonObject){
        try {
            return Deserializer.decode(ItemStack.class, jsonObject);
        } catch (JsonDeserializeException e) {
            throw new RuntimeException(e);
        }
    }
    public static RandomIntSupplier loadPrice(JsonObject jsonObject){
        try {
            return Deserializer.decode(RandomIntSupplier.class, jsonObject);
        } catch (JsonDeserializeException e) {
            throw new RuntimeException(e);
        }
    }
}
