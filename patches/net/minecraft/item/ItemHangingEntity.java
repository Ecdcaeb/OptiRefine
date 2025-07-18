package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHangingEntity extends Item {
   private final Class<? extends EntityHanging> hangingEntityClass;

   public ItemHangingEntity(Class<? extends EntityHanging> var1) {
      this.hangingEntityClass = ☃;
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      BlockPos ☃x = ☃.offset(☃);
      if (☃ != EnumFacing.DOWN && ☃ != EnumFacing.UP && ☃.canPlayerEdit(☃x, ☃, ☃)) {
         EntityHanging ☃xx = this.createEntity(☃, ☃x, ☃);
         if (☃xx != null && ☃xx.onValidSurface()) {
            if (!☃.isRemote) {
               ☃xx.playPlaceSound();
               ☃.spawnEntity(☃xx);
            }

            ☃.shrink(1);
         }

         return EnumActionResult.SUCCESS;
      } else {
         return EnumActionResult.FAIL;
      }
   }

   @Nullable
   private EntityHanging createEntity(World var1, BlockPos var2, EnumFacing var3) {
      if (this.hangingEntityClass == EntityPainting.class) {
         return new EntityPainting(☃, ☃, ☃);
      } else {
         return this.hangingEntityClass == EntityItemFrame.class ? new EntityItemFrame(☃, ☃, ☃) : null;
      }
   }
}
