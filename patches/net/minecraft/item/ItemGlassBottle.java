package net.minecraft.item;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemGlassBottle extends Item {
   public ItemGlassBottle() {
      this.setCreativeTab(CreativeTabs.BREWING);
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      List<EntityAreaEffectCloud> ☃ = ☃.getEntitiesWithinAABB(
         EntityAreaEffectCloud.class, ☃.getEntityBoundingBox().grow(2.0), new Predicate<EntityAreaEffectCloud>() {
            public boolean apply(@Nullable EntityAreaEffectCloud var1) {
               return ☃ != null && ☃.isEntityAlive() && ☃.getOwner() instanceof EntityDragon;
            }
         }
      );
      ItemStack ☃x = ☃.getHeldItem(☃);
      if (!☃.isEmpty()) {
         EntityAreaEffectCloud ☃xx = ☃.get(0);
         ☃xx.setRadius(☃xx.getRadius() - 0.5F);
         ☃.playSound(null, ☃.posX, ☃.posY, ☃.posZ, SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
         return new ActionResult<>(EnumActionResult.SUCCESS, this.turnBottleIntoItem(☃x, ☃, new ItemStack(Items.DRAGON_BREATH)));
      } else {
         RayTraceResult ☃xx = this.rayTrace(☃, ☃, true);
         if (☃xx == null) {
            return new ActionResult<>(EnumActionResult.PASS, ☃x);
         } else {
            if (☃xx.typeOfHit == RayTraceResult.Type.BLOCK) {
               BlockPos ☃xxx = ☃xx.getBlockPos();
               if (!☃.isBlockModifiable(☃, ☃xxx) || !☃.canPlayerEdit(☃xxx.offset(☃xx.sideHit), ☃xx.sideHit, ☃x)) {
                  return new ActionResult<>(EnumActionResult.PASS, ☃x);
               }

               if (☃.getBlockState(☃xxx).getMaterial() == Material.WATER) {
                  ☃.playSound(☃, ☃.posX, ☃.posY, ☃.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                  return new ActionResult<>(
                     EnumActionResult.SUCCESS,
                     this.turnBottleIntoItem(☃x, ☃, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER))
                  );
               }
            }

            return new ActionResult<>(EnumActionResult.PASS, ☃x);
         }
      }
   }

   protected ItemStack turnBottleIntoItem(ItemStack var1, EntityPlayer var2, ItemStack var3) {
      ☃.shrink(1);
      ☃.addStat(StatList.getObjectUseStats(this));
      if (☃.isEmpty()) {
         return ☃;
      } else {
         if (!☃.inventory.addItemStackToInventory(☃)) {
            ☃.dropItem(☃, false);
         }

         return ☃;
      }
   }
}
