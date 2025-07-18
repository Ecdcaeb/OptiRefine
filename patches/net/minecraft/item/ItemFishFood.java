package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemFishFood extends ItemFood {
   private final boolean cooked;

   public ItemFishFood(boolean var1) {
      super(0, 0.0F, false);
      this.cooked = ☃;
   }

   @Override
   public int getHealAmount(ItemStack var1) {
      ItemFishFood.FishType ☃ = ItemFishFood.FishType.byItemStack(☃);
      return this.cooked && ☃.canCook() ? ☃.getCookedHealAmount() : ☃.getUncookedHealAmount();
   }

   @Override
   public float getSaturationModifier(ItemStack var1) {
      ItemFishFood.FishType ☃ = ItemFishFood.FishType.byItemStack(☃);
      return this.cooked && ☃.canCook() ? ☃.getCookedSaturationModifier() : ☃.getUncookedSaturationModifier();
   }

   @Override
   protected void onFoodEaten(ItemStack var1, World var2, EntityPlayer var3) {
      ItemFishFood.FishType ☃ = ItemFishFood.FishType.byItemStack(☃);
      if (☃ == ItemFishFood.FishType.PUFFERFISH) {
         ☃.addPotionEffect(new PotionEffect(MobEffects.POISON, 1200, 3));
         ☃.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 300, 2));
         ☃.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 300, 1));
      }

      super.onFoodEaten(☃, ☃, ☃);
   }

   @Override
   public void getSubItems(CreativeTabs var1, NonNullList<ItemStack> var2) {
      if (this.isInCreativeTab(☃)) {
         for (ItemFishFood.FishType ☃ : ItemFishFood.FishType.values()) {
            if (!this.cooked || ☃.canCook()) {
               ☃.add(new ItemStack(this, 1, ☃.getMetadata()));
            }
         }
      }
   }

   @Override
   public String getTranslationKey(ItemStack var1) {
      ItemFishFood.FishType ☃ = ItemFishFood.FishType.byItemStack(☃);
      return this.getTranslationKey() + "." + ☃.getTranslationKey() + "." + (this.cooked && ☃.canCook() ? "cooked" : "raw");
   }

   public static enum FishType {
      COD(0, "cod", 2, 0.1F, 5, 0.6F),
      SALMON(1, "salmon", 2, 0.1F, 6, 0.8F),
      CLOWNFISH(2, "clownfish", 1, 0.1F),
      PUFFERFISH(3, "pufferfish", 1, 0.1F);

      private static final Map<Integer, ItemFishFood.FishType> META_LOOKUP = Maps.newHashMap();
      private final int meta;
      private final String translationKey;
      private final int uncookedHealAmount;
      private final float uncookedSaturationModifier;
      private final int cookedHealAmount;
      private final float cookedSaturationModifier;
      private final boolean cookable;

      private FishType(int var3, String var4, int var5, float var6, int var7, float var8) {
         this.meta = ☃;
         this.translationKey = ☃;
         this.uncookedHealAmount = ☃;
         this.uncookedSaturationModifier = ☃;
         this.cookedHealAmount = ☃;
         this.cookedSaturationModifier = ☃;
         this.cookable = true;
      }

      private FishType(int var3, String var4, int var5, float var6) {
         this.meta = ☃;
         this.translationKey = ☃;
         this.uncookedHealAmount = ☃;
         this.uncookedSaturationModifier = ☃;
         this.cookedHealAmount = 0;
         this.cookedSaturationModifier = 0.0F;
         this.cookable = false;
      }

      public int getMetadata() {
         return this.meta;
      }

      public String getTranslationKey() {
         return this.translationKey;
      }

      public int getUncookedHealAmount() {
         return this.uncookedHealAmount;
      }

      public float getUncookedSaturationModifier() {
         return this.uncookedSaturationModifier;
      }

      public int getCookedHealAmount() {
         return this.cookedHealAmount;
      }

      public float getCookedSaturationModifier() {
         return this.cookedSaturationModifier;
      }

      public boolean canCook() {
         return this.cookable;
      }

      public static ItemFishFood.FishType byMetadata(int var0) {
         ItemFishFood.FishType ☃ = META_LOOKUP.get(☃);
         return ☃ == null ? COD : ☃;
      }

      public static ItemFishFood.FishType byItemStack(ItemStack var0) {
         return ☃.getItem() instanceof ItemFishFood ? byMetadata(☃.getMetadata()) : COD;
      }

      static {
         for (ItemFishFood.FishType ☃ : values()) {
            META_LOOKUP.put(☃.getMetadata(), ☃);
         }
      }
   }
}
