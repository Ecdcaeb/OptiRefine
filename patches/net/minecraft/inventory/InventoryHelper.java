package net.minecraft.inventory;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InventoryHelper {
   private static final Random RANDOM = new Random();

   public static void dropInventoryItems(World var0, BlockPos var1, IInventory var2) {
      dropInventoryItems(☃, ☃.getX(), ☃.getY(), ☃.getZ(), ☃);
   }

   public static void dropInventoryItems(World var0, Entity var1, IInventory var2) {
      dropInventoryItems(☃, ☃.posX, ☃.posY, ☃.posZ, ☃);
   }

   private static void dropInventoryItems(World var0, double var1, double var3, double var5, IInventory var7) {
      for (int ☃ = 0; ☃ < ☃.getSizeInventory(); ☃++) {
         ItemStack ☃x = ☃.getStackInSlot(☃);
         if (!☃x.isEmpty()) {
            spawnItemStack(☃, ☃, ☃, ☃, ☃x);
         }
      }
   }

   public static void spawnItemStack(World var0, double var1, double var3, double var5, ItemStack var7) {
      float ☃ = RANDOM.nextFloat() * 0.8F + 0.1F;
      float ☃x = RANDOM.nextFloat() * 0.8F + 0.1F;
      float ☃xx = RANDOM.nextFloat() * 0.8F + 0.1F;

      while (!☃.isEmpty()) {
         EntityItem ☃xxx = new EntityItem(☃, ☃ + ☃, ☃ + ☃x, ☃ + ☃xx, ☃.splitStack(RANDOM.nextInt(21) + 10));
         float ☃xxxx = 0.05F;
         ☃xxx.motionX = RANDOM.nextGaussian() * 0.05F;
         ☃xxx.motionY = RANDOM.nextGaussian() * 0.05F + 0.2F;
         ☃xxx.motionZ = RANDOM.nextGaussian() * 0.05F;
         ☃.spawnEntity(☃xxx);
      }
   }
}
