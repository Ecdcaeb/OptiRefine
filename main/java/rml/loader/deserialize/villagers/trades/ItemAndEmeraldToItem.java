package rml.loader.deserialize.villagers.trades;

import com.google.gson.JsonObject;
import rml.deserializer.Record;
import rml.jrx.announces.BeDiscovered;
import rml.jrx.utils.values.RandomIntSupplier;
import rml.loader.api.world.villagers.TradeBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
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
 * @Date 2023/8/16 14:20
 * {@link EntityVillager.ItemAndEmeraldToItem}
 **/
@BeDiscovered(BeDiscovered.PRE_INIT)
public class ItemAndEmeraldToItem implements EntityVillager.ITradeList{

    public static final AbstractDeserializer<EntityVillager.ITradeList> DESERIALIZER = Deserializer.named(EntityVillager.ITradeList.class,new ResourceLocation("minecraft","list_item_for_emeralds"))
            .record(ItemAndEmeraldToItem.class).build();

    /**
     * The itemstack to buy with an emerald. The Item and damage value is used only, any tag data is not
     * retained.
     */
    public ItemStack buyingItemStack;
    /** The price info defining the amount of the buying item required with 1 emerald to match the selling item. */
    public RandomIntSupplier buyingPriceInfo;
    public RandomIntSupplier buyingPriceInfo2;
    /** The itemstack to sell. The item and damage value are used only, any tag data is not retained. */
    public ItemStack sellingItemstack;
    public RandomIntSupplier sellingPriceInfo;


    @Record({"from.item", "from.price", "from.count", "to.item", "to.count"})
    public ItemAndEmeraldToItem(ItemStack buyingItemStack, RandomIntSupplier buyingPriceInfo2, RandomIntSupplier buyingPriceInfo, ItemStack sellingItemstack, RandomIntSupplier sellingPriceInfo){
        this.buyingItemStack = buyingItemStack;
        this.buyingPriceInfo2 = buyingPriceInfo2;
        this.buyingPriceInfo = buyingPriceInfo;
        this.sellingItemstack = sellingItemstack;
        this.sellingPriceInfo = sellingPriceInfo;
    }
    public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
    {
        int i = this.buyingPriceInfo.get(random);
        int j = this.sellingPriceInfo.get(random);
        int k = this.buyingPriceInfo.get(random);
        ItemStack stack1=this.buyingItemStack.copy();
        stack1.setCount(i);
        ItemStack stack2=this.sellingItemstack.copy();
        ItemStack stack=new ItemStack(Items.EMERALD);
        stack.setCount(k);
        recipeList.add(new MerchantRecipe(stack1, stack, stack2));
    }

}
