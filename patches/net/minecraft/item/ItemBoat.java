package net.minecraft.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemBoat extends Item {
   private final EntityBoat.Type type;

   public ItemBoat(EntityBoat.Type var1) {
      this.type = ☃;
      this.maxStackSize = 1;
      this.setCreativeTab(CreativeTabs.TRANSPORTATION);
      this.setTranslationKey("boat." + ☃.getName());
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World var1, EntityPlayer var2, EnumHand var3) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      float ☃x = 1.0F;
      float ☃xx = ☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * 1.0F;
      float ☃xxx = ☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * 1.0F;
      double ☃xxxx = ☃.prevPosX + (☃.posX - ☃.prevPosX) * 1.0;
      double ☃xxxxx = ☃.prevPosY + (☃.posY - ☃.prevPosY) * 1.0 + ☃.getEyeHeight();
      double ☃xxxxxx = ☃.prevPosZ + (☃.posZ - ☃.prevPosZ) * 1.0;
      Vec3d ☃xxxxxxx = new Vec3d(☃xxxx, ☃xxxxx, ☃xxxxxx);
      float ☃xxxxxxxx = MathHelper.cos(-☃xxx * (float) (Math.PI / 180.0) - (float) Math.PI);
      float ☃xxxxxxxxx = MathHelper.sin(-☃xxx * (float) (Math.PI / 180.0) - (float) Math.PI);
      float ☃xxxxxxxxxx = -MathHelper.cos(-☃xx * (float) (Math.PI / 180.0));
      float ☃xxxxxxxxxxx = MathHelper.sin(-☃xx * (float) (Math.PI / 180.0));
      float ☃xxxxxxxxxxxx = ☃xxxxxxxxx * ☃xxxxxxxxxx;
      float ☃xxxxxxxxxxxxx = ☃xxxxxxxx * ☃xxxxxxxxxx;
      double ☃xxxxxxxxxxxxxx = 5.0;
      Vec3d ☃xxxxxxxxxxxxxxx = ☃xxxxxxx.add(☃xxxxxxxxxxxx * 5.0, ☃xxxxxxxxxxx * 5.0, ☃xxxxxxxxxxxxx * 5.0);
      RayTraceResult ☃xxxxxxxxxxxxxxxx = ☃.rayTraceBlocks(☃xxxxxxx, ☃xxxxxxxxxxxxxxx, true);
      if (☃xxxxxxxxxxxxxxxx == null) {
         return new ActionResult<>(EnumActionResult.PASS, ☃);
      } else {
         Vec3d ☃xxxxxxxxxxxxxxxxx = ☃.getLook(1.0F);
         boolean ☃xxxxxxxxxxxxxxxxxx = false;
         List<Entity> ☃xxxxxxxxxxxxxxxxxxx = ☃.getEntitiesWithinAABBExcludingEntity(
            ☃, ☃.getEntityBoundingBox().expand(☃xxxxxxxxxxxxxxxxx.x * 5.0, ☃xxxxxxxxxxxxxxxxx.y * 5.0, ☃xxxxxxxxxxxxxxxxx.z * 5.0).grow(1.0)
         );

         for (int ☃xxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxx.size(); ☃xxxxxxxxxxxxxxxxxxxx++) {
            Entity ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx.get(☃xxxxxxxxxxxxxxxxxxxx);
            if (☃xxxxxxxxxxxxxxxxxxxxx.canBeCollidedWith()) {
               AxisAlignedBB ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxx.getEntityBoundingBox().grow(☃xxxxxxxxxxxxxxxxxxxxx.getCollisionBorderSize());
               if (☃xxxxxxxxxxxxxxxxxxxxxx.contains(☃xxxxxxx)) {
                  ☃xxxxxxxxxxxxxxxxxx = true;
               }
            }
         }

         if (☃xxxxxxxxxxxxxxxxxx) {
            return new ActionResult<>(EnumActionResult.PASS, ☃);
         } else if (☃xxxxxxxxxxxxxxxx.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<>(EnumActionResult.PASS, ☃);
         } else {
            Block ☃xxxxxxxxxxxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxxxxxxxxxx.getBlockPos()).getBlock();
            boolean ☃xxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxxxx == Blocks.WATER || ☃xxxxxxxxxxxxxxxxxxxxx == Blocks.FLOWING_WATER;
            EntityBoat ☃xxxxxxxxxxxxxxxxxxxxxxx = new EntityBoat(
               ☃,
               ☃xxxxxxxxxxxxxxxx.hitVec.x,
               ☃xxxxxxxxxxxxxxxxxxxxxx ? ☃xxxxxxxxxxxxxxxx.hitVec.y - 0.12 : ☃xxxxxxxxxxxxxxxx.hitVec.y,
               ☃xxxxxxxxxxxxxxxx.hitVec.z
            );
            ☃xxxxxxxxxxxxxxxxxxxxxxx.setBoatType(this.type);
            ☃xxxxxxxxxxxxxxxxxxxxxxx.rotationYaw = ☃.rotationYaw;
            if (!☃.getCollisionBoxes(☃xxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx.getEntityBoundingBox().grow(-0.1)).isEmpty()) {
               return new ActionResult<>(EnumActionResult.FAIL, ☃);
            } else {
               if (!☃.isRemote) {
                  ☃.spawnEntity(☃xxxxxxxxxxxxxxxxxxxxxxx);
               }

               if (!☃.capabilities.isCreativeMode) {
                  ☃.shrink(1);
               }

               ☃.addStat(StatList.getObjectUseStats(this));
               return new ActionResult<>(EnumActionResult.SUCCESS, ☃);
            }
         }
      }
   }
}
