package rml.loader.deserialize.villagers.trades;

import rml.deserializer.DeserializerBuilder;
import rml.deserializer.JsonDeserializeException;
import rml.jrx.announces.BeDiscovered;
import rml.jrx.utils.values.RandomIntSupplier;
import com.google.gson.JsonObject;
import rml.loader.api.world.villagers.TradeBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import rml.deserializer.AbstractDeserializer;
import rml.loader.deserialize.Deserializer;

import java.util.Random;

/**
 * @Project CustomVillage
 * @Author Hileb
 * @Date 2023/8/20 10:27
 **/
@BeDiscovered(BeDiscovered.PRE_INIT)
public class SlotRecipe implements EntityVillager.ITradeList {
    public static final AbstractDeserializer<EntityVillager.ITradeList> DESERIALIZER = Deserializer.named(EntityVillager.ITradeList.class, new ResourceLocation("cvh","slots"))
            .require(ItemStack.class, "form.slot1.item")
            .require(ItemStack.class, "from.slot2.item")
            .require(ItemStack.class, "to.slot3.item")
            .optionalDefault(RandomIntSupplier.class, "from.slot1.price", RandomIntSupplier.RangeConstant.ONE)
            .optionalDefault(RandomIntSupplier.class, "from.slot2.price", RandomIntSupplier.RangeConstant.ONE)
            .optionalDefault(RandomIntSupplier.class, "to.slot3.price", RandomIntSupplier.RangeConstant.ONE)
            .decode(context -> new SlotRecipe(
                    context.get(ItemStack.class, "form.slot1.item"),
                    context.get(ItemStack.class, "from.slot2.item"),
                    context.get(ItemStack.class, "to.slot3.item"),
                    context.get(RandomIntSupplier.class, "from.slot1.price"),
                    context.get(RandomIntSupplier.class, "from.slot2.price"),
                    context.get(RandomIntSupplier.class, "to.slot3.price")
                )
            ).build();
    public RandomIntSupplier range1;
    public RandomIntSupplier range2;
    public RandomIntSupplier range3;

    public ItemStack stack1;
    public ItemStack stack2;
    public ItemStack stack3;
    public SlotRecipe(ItemStack stack1, ItemStack stack2, ItemStack stack3, RandomIntSupplier range1, RandomIntSupplier range2, RandomIntSupplier range3){
        this.range1 = range1;
        this.range2 = range2;
        this.range3 = range3;
        this.stack2 = stack2;
        this.stack1 = stack1;
        this.stack3 = stack3;
    }
    @Override
    public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
        ItemStack stack_1=stack1.copy();
        ItemStack stack_2=stack2.copy();
        ItemStack stack_3=stack3.copy();
        stack_1.setCount(range1.get(random));
        stack_2.setCount(range2.get(random));
        stack_3.setCount(range3.get(random));

        recipeList.add(new MerchantRecipe(stack_1, stack_2, stack_3));
    }
}
