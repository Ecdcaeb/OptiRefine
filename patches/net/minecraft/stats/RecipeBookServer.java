package net.minecraft.stats;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RecipeBookServer extends RecipeBook {
   private static final Logger LOGGER = LogManager.getLogger();

   public void add(List<IRecipe> var1, EntityPlayerMP var2) {
      List<IRecipe> ☃ = Lists.newArrayList();

      for (IRecipe ☃x : ☃) {
         if (!this.recipes.get(getRecipeId(☃x)) && !☃x.isDynamic()) {
            this.unlock(☃x);
            this.markNew(☃x);
            ☃.add(☃x);
            CriteriaTriggers.RECIPE_UNLOCKED.trigger(☃, ☃x);
         }
      }

      this.sendPacket(SPacketRecipeBook.State.ADD, ☃, ☃);
   }

   public void remove(List<IRecipe> var1, EntityPlayerMP var2) {
      List<IRecipe> ☃ = Lists.newArrayList();

      for (IRecipe ☃x : ☃) {
         if (this.recipes.get(getRecipeId(☃x))) {
            this.lock(☃x);
            ☃.add(☃x);
         }
      }

      this.sendPacket(SPacketRecipeBook.State.REMOVE, ☃, ☃);
   }

   private void sendPacket(SPacketRecipeBook.State var1, EntityPlayerMP var2, List<IRecipe> var3) {
      ☃.connection.sendPacket(new SPacketRecipeBook(☃, ☃, Collections.emptyList(), this.isGuiOpen, this.isFilteringCraftable));
   }

   public NBTTagCompound write() {
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.setBoolean("isGuiOpen", this.isGuiOpen);
      ☃.setBoolean("isFilteringCraftable", this.isFilteringCraftable);
      NBTTagList ☃x = new NBTTagList();

      for (IRecipe ☃xx : this.getRecipes()) {
         ☃x.appendTag(new NBTTagString(CraftingManager.REGISTRY.getNameForObject(☃xx).toString()));
      }

      ☃.setTag("recipes", ☃x);
      NBTTagList ☃xx = new NBTTagList();

      for (IRecipe ☃xxx : this.getDisplayedRecipes()) {
         ☃xx.appendTag(new NBTTagString(CraftingManager.REGISTRY.getNameForObject(☃xxx).toString()));
      }

      ☃.setTag("toBeDisplayed", ☃xx);
      return ☃;
   }

   public void read(NBTTagCompound var1) {
      this.isGuiOpen = ☃.getBoolean("isGuiOpen");
      this.isFilteringCraftable = ☃.getBoolean("isFilteringCraftable");
      NBTTagList ☃ = ☃.getTagList("recipes", 8);

      for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
         ResourceLocation ☃xx = new ResourceLocation(☃.getStringTagAt(☃x));
         IRecipe ☃xxx = CraftingManager.getRecipe(☃xx);
         if (☃xxx == null) {
            LOGGER.info("Tried to load unrecognized recipe: {} removed now.", ☃xx);
         } else {
            this.unlock(☃xxx);
         }
      }

      NBTTagList ☃xx = ☃.getTagList("toBeDisplayed", 8);

      for (int ☃xxx = 0; ☃xxx < ☃xx.tagCount(); ☃xxx++) {
         ResourceLocation ☃xxxx = new ResourceLocation(☃xx.getStringTagAt(☃xxx));
         IRecipe ☃xxxxx = CraftingManager.getRecipe(☃xxxx);
         if (☃xxxxx == null) {
            LOGGER.info("Tried to load unrecognized recipe: {} removed now.", ☃xxxx);
         } else {
            this.markNew(☃xxxxx);
         }
      }
   }

   private List<IRecipe> getRecipes() {
      List<IRecipe> ☃ = Lists.newArrayList();

      for (int ☃x = this.recipes.nextSetBit(0); ☃x >= 0; ☃x = this.recipes.nextSetBit(☃x + 1)) {
         ☃.add(CraftingManager.REGISTRY.getObjectById(☃x));
      }

      return ☃;
   }

   private List<IRecipe> getDisplayedRecipes() {
      List<IRecipe> ☃ = Lists.newArrayList();

      for (int ☃x = this.newRecipes.nextSetBit(0); ☃x >= 0; ☃x = this.newRecipes.nextSetBit(☃x + 1)) {
         ☃.add(CraftingManager.REGISTRY.getObjectById(☃x));
      }

      return ☃;
   }

   public void init(EntityPlayerMP var1) {
      ☃.connection
         .sendPacket(
            new SPacketRecipeBook(SPacketRecipeBook.State.INIT, this.getRecipes(), this.getDisplayedRecipes(), this.isGuiOpen, this.isFilteringCraftable)
         );
   }
}
