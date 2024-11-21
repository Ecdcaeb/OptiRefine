package rml.loader.deserialize.villagers.trades;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import rml.deserializer.AbstractDeserializer;
import rml.jrx.announces.BeDiscovered;
import rml.jrx.utils.values.RandomIntSupplier;
import rml.loader.deserialize.Deserializer;

import java.util.Random;

/**
 * @Project CustomVillage
 * @Author Hileb
 * @Date 2023/8/16 14:40
 * {@link EntityVillager.EmeraldForItems}
 **/

@BeDiscovered(BeDiscovered.PRE_INIT)
public class EmeraldForItems implements EntityVillager.ITradeList
{
    public static final AbstractDeserializer<EntityVillager.ITradeList> DESERIALIZER = Deserializer.named(EntityVillager.ITradeList.class, new ResourceLocation("minecraft","emerald_for_items"))
            .require(ItemStack.class, "item")
            .require(RandomIntSupplier.class, "price")
            .decode((context -> new EmeraldForItems(context.get(ItemStack.class, "item"), context.get(RandomIntSupplier.class, "price"))
            )).build();
    public ItemStack buyingItem;
    public RandomIntSupplier price;

    public EmeraldForItems(ItemStack itemIn, RandomIntSupplier priceIn)
    {
        this.buyingItem = itemIn;
        this.price = priceIn;
    }
    public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
    {
        recipeList.add(new MerchantRecipe(buyingItem.copy(), new ItemStack(Items.EMERALD, this.price.get(random))));
    }
}
