package net.minecraft.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMinecart extends Item {
   private static final IBehaviorDispenseItem MINECART_DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem() {
      private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();

      @Override
      public ItemStack dispenseStack(IBlockSource var1, ItemStack var2) {
         EnumFacing ☃ = ☃.getBlockState().getValue(BlockDispenser.FACING);
         World ☃x = ☃.getWorld();
         double ☃xx = ☃.getX() + ☃.getXOffset() * 1.125;
         double ☃xxx = Math.floor(☃.getY()) + ☃.getYOffset();
         double ☃xxxx = ☃.getZ() + ☃.getZOffset() * 1.125;
         BlockPos ☃xxxxx = ☃.getBlockPos().offset(☃);
         IBlockState ☃xxxxxx = ☃x.getBlockState(☃xxxxx);
         BlockRailBase.EnumRailDirection ☃xxxxxxx = ☃xxxxxx.getBlock() instanceof BlockRailBase
            ? ☃xxxxxx.getValue(((BlockRailBase)☃xxxxxx.getBlock()).getShapeProperty())
            : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
         double ☃xxxxxxxx;
         if (BlockRailBase.isRailBlock(☃xxxxxx)) {
            if (☃xxxxxxx.isAscending()) {
               ☃xxxxxxxx = 0.6;
            } else {
               ☃xxxxxxxx = 0.1;
            }
         } else {
            if (☃xxxxxx.getMaterial() != Material.AIR || !BlockRailBase.isRailBlock(☃x.getBlockState(☃xxxxx.down()))) {
               return this.behaviourDefaultDispenseItem.dispense(☃, ☃);
            }

            IBlockState ☃xxxxxxxxx = ☃x.getBlockState(☃xxxxx.down());
            BlockRailBase.EnumRailDirection ☃xxxxxxxxxx = ☃xxxxxxxxx.getBlock() instanceof BlockRailBase
               ? ☃xxxxxxxxx.getValue(((BlockRailBase)☃xxxxxxxxx.getBlock()).getShapeProperty())
               : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            if (☃ != EnumFacing.DOWN && ☃xxxxxxxxxx.isAscending()) {
               ☃xxxxxxxx = -0.4;
            } else {
               ☃xxxxxxxx = -0.9;
            }
         }

         EntityMinecart ☃xxxxxxxxx = EntityMinecart.create(☃x, ☃xx, ☃xxx + ☃xxxxxxxx, ☃xxxx, ((ItemMinecart)☃.getItem()).minecartType);
         if (☃.hasDisplayName()) {
            ☃xxxxxxxxx.setCustomNameTag(☃.getDisplayName());
         }

         ☃x.spawnEntity(☃xxxxxxxxx);
         ☃.shrink(1);
         return ☃;
      }

      @Override
      protected void playDispenseSound(IBlockSource var1) {
         ☃.getWorld().playEvent(1000, ☃.getBlockPos(), 0);
      }
   };
   private final EntityMinecart.Type minecartType;

   public ItemMinecart(EntityMinecart.Type var1) {
      this.maxStackSize = 1;
      this.minecartType = ☃;
      this.setCreativeTab(CreativeTabs.TRANSPORTATION);
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, MINECART_DISPENSER_BEHAVIOR);
   }

   @Override
   public EnumActionResult onItemUse(EntityPlayer var1, World var2, BlockPos var3, EnumHand var4, EnumFacing var5, float var6, float var7, float var8) {
      IBlockState ☃ = ☃.getBlockState(☃);
      if (!BlockRailBase.isRailBlock(☃)) {
         return EnumActionResult.FAIL;
      } else {
         ItemStack ☃x = ☃.getHeldItem(☃);
         if (!☃.isRemote) {
            BlockRailBase.EnumRailDirection ☃xx = ☃.getBlock() instanceof BlockRailBase
               ? ☃.getValue(((BlockRailBase)☃.getBlock()).getShapeProperty())
               : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            double ☃xxx = 0.0;
            if (☃xx.isAscending()) {
               ☃xxx = 0.5;
            }

            EntityMinecart ☃xxxx = EntityMinecart.create(☃, ☃.getX() + 0.5, ☃.getY() + 0.0625 + ☃xxx, ☃.getZ() + 0.5, this.minecartType);
            if (☃x.hasDisplayName()) {
               ☃xxxx.setCustomNameTag(☃x.getDisplayName());
            }

            ☃.spawnEntity(☃xxxx);
         }

         ☃x.shrink(1);
         return EnumActionResult.SUCCESS;
      }
   }
}
