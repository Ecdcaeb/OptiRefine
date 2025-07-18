package net.minecraft.item;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemKnowledgeBook extends Item {
   private static final Logger LOGGER = LogManager.getLogger();

   public ItemKnowledgeBook() {
      this.setMaxStackSize(1);
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      NBTTagCompound ☃x = ☃.getTagCompound();
      if (!☃.capabilities.isCreativeMode) {
         ☃.setHeldItem(☃, ItemStack.EMPTY);
      }

      if (☃x != null && ☃x.hasKey("Recipes", 9)) {
         if (!☃.isRemote) {
            NBTTagList ☃xx = ☃x.getTagList("Recipes", 8);
            List<IRecipe> ☃xxx = Lists.newArrayList();

            for (int ☃xxxx = 0; ☃xxxx < ☃xx.tagCount(); ☃xxxx++) {
               String ☃xxxxx = ☃xx.getStringTagAt(☃xxxx);
               IRecipe ☃xxxxxx = CraftingManager.getRecipe(new ResourceLocation(☃xxxxx));
               if (☃xxxxxx == null) {
                  LOGGER.error("Invalid recipe: " + ☃xxxxx);
                  return new ActionResult<>(EnumActionResult.FAIL, ☃);
               }

               ☃xxx.add(☃xxxxxx);
            }

            ☃.unlockRecipes(☃xxx);
            ☃.addStat(StatList.getObjectUseStats(this));
         }

         return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
      } else {
         LOGGER.error("Tag not valid: " + ☃x);
         return new ActionResult<>(EnumActionResult.FAIL, ☃);
      }
   }
}
