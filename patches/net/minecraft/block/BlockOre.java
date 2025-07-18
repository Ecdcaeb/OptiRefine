package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockOre extends Block {
   public BlockOre() {
      this(Material.ROCK.getMaterialMapColor());
   }

   public BlockOre(MapColor var1) {
      super(Material.ROCK, ☃);
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      if (this == Blocks.COAL_ORE) {
         return Items.COAL;
      } else if (this == Blocks.DIAMOND_ORE) {
         return Items.DIAMOND;
      } else if (this == Blocks.LAPIS_ORE) {
         return Items.DYE;
      } else if (this == Blocks.EMERALD_ORE) {
         return Items.EMERALD;
      } else {
         return this == Blocks.QUARTZ_ORE ? Items.QUARTZ : Item.getItemFromBlock(this);
      }
   }

   @Override
   public int quantityDropped(Random var1) {
      return this == Blocks.LAPIS_ORE ? 4 + ☃.nextInt(5) : 1;
   }

   @Override
   public int quantityDroppedWithBonus(int var1, Random var2) {
      if (☃ > 0 && Item.getItemFromBlock(this) != this.getItemDropped((IBlockState)this.getBlockState().getValidStates().iterator().next(), ☃, ☃)) {
         int ☃ = ☃.nextInt(☃ + 2) - 1;
         if (☃ < 0) {
            ☃ = 0;
         }

         return this.quantityDropped(☃) * (☃ + 1);
      } else {
         return this.quantityDropped(☃);
      }
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      super.dropBlockAsItemWithChance(☃, ☃, ☃, ☃, ☃);
      if (this.getItemDropped(☃, ☃.rand, ☃) != Item.getItemFromBlock(this)) {
         int ☃ = 0;
         if (this == Blocks.COAL_ORE) {
            ☃ = MathHelper.getInt(☃.rand, 0, 2);
         } else if (this == Blocks.DIAMOND_ORE) {
            ☃ = MathHelper.getInt(☃.rand, 3, 7);
         } else if (this == Blocks.EMERALD_ORE) {
            ☃ = MathHelper.getInt(☃.rand, 3, 7);
         } else if (this == Blocks.LAPIS_ORE) {
            ☃ = MathHelper.getInt(☃.rand, 2, 5);
         } else if (this == Blocks.QUARTZ_ORE) {
            ☃ = MathHelper.getInt(☃.rand, 2, 5);
         }

         this.dropXpOnBlockBreak(☃, ☃, ☃);
      }
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(this);
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return this == Blocks.LAPIS_ORE ? EnumDyeColor.BLUE.getDyeDamage() : 0;
   }
}
