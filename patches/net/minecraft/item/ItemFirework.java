package net.minecraft.item;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemFirework extends Item {
   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      if (!☃.isRemote) {
         ItemStack ☃ = ☃.getHeldItem(☃);
         EntityFireworkRocket ☃x = new EntityFireworkRocket(☃, ☃.getX() + ☃, ☃.getY() + ☃, ☃.getZ() + ☃, ☃);
         ☃.spawnEntity(☃x);
         if (!☃.capabilities.isCreativeMode) {
            ☃.shrink(1);
         }
      }

      return EnumActionResult.SUCCESS;
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      if (☃.isElytraFlying()) {
         ItemStack ☃ = ☃.getHeldItem(☃);
         if (!☃.isRemote) {
            EntityFireworkRocket ☃x = new EntityFireworkRocket(☃, ☃, ☃);
            ☃.spawnEntity(☃x);
            if (!☃.capabilities.isCreativeMode) {
               ☃.shrink(1);
            }
         }

         return new ActionResult<>(EnumActionResult.SUCCESS, ☃.getHeldItem(☃));
      } else {
         return new ActionResult<>(EnumActionResult.PASS, ☃.getHeldItem(☃));
      }
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      NBTTagCompound ☃ = ☃.getSubCompound("Fireworks");
      if (☃ != null) {
         if (☃.hasKey("Flight", 99)) {
            ☃.add(I18n.translateToLocal("item.fireworks.flight") + " " + ☃.getByte("Flight"));
         }

         NBTTagList ☃x = ☃.getTagList("Explosions", 10);
         if (!☃x.isEmpty()) {
            for (int ☃xx = 0; ☃xx < ☃x.tagCount(); ☃xx++) {
               NBTTagCompound ☃xxx = ☃x.getCompoundTagAt(☃xx);
               List<String> ☃xxxx = Lists.newArrayList();
               ItemFireworkCharge.addExplosionInfo(☃xxx, ☃xxxx);
               if (!☃xxxx.isEmpty()) {
                  for (int ☃xxxxx = 1; ☃xxxxx < ☃xxxx.size(); ☃xxxxx++) {
                     ☃xxxx.set(☃xxxxx, "  " + ☃xxxx.get(☃xxxxx));
                  }

                  ☃.addAll(☃xxxx);
               }
            }
         }
      }
   }
}
