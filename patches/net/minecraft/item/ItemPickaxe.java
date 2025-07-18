package net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class ItemPickaxe extends ItemTool {
   private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(
      new Block[]{
         Blocks.ACTIVATOR_RAIL,
         Blocks.COAL_ORE,
         Blocks.COBBLESTONE,
         Blocks.DETECTOR_RAIL,
         Blocks.DIAMOND_BLOCK,
         Blocks.DIAMOND_ORE,
         Blocks.DOUBLE_STONE_SLAB,
         Blocks.GOLDEN_RAIL,
         Blocks.GOLD_BLOCK,
         Blocks.GOLD_ORE,
         Blocks.ICE,
         Blocks.IRON_BLOCK,
         Blocks.IRON_ORE,
         Blocks.LAPIS_BLOCK,
         Blocks.LAPIS_ORE,
         Blocks.LIT_REDSTONE_ORE,
         Blocks.MOSSY_COBBLESTONE,
         Blocks.NETHERRACK,
         Blocks.PACKED_ICE,
         Blocks.RAIL,
         Blocks.REDSTONE_ORE,
         Blocks.SANDSTONE,
         Blocks.RED_SANDSTONE,
         Blocks.STONE,
         Blocks.STONE_SLAB,
         Blocks.STONE_BUTTON,
         Blocks.STONE_PRESSURE_PLATE
      }
   );

   protected ItemPickaxe(Item.ToolMaterial var1) {
      super(1.0F, -2.8F, ☃, EFFECTIVE_ON);
   }

   @Override
   public boolean canHarvestBlock(IBlockState var1) {
      Block ☃ = ☃.getBlock();
      if (☃ == Blocks.OBSIDIAN) {
         return this.toolMaterial.getHarvestLevel() == 3;
      } else if (☃ == Blocks.DIAMOND_BLOCK || ☃ == Blocks.DIAMOND_ORE) {
         return this.toolMaterial.getHarvestLevel() >= 2;
      } else if (☃ == Blocks.EMERALD_ORE || ☃ == Blocks.EMERALD_BLOCK) {
         return this.toolMaterial.getHarvestLevel() >= 2;
      } else if (☃ == Blocks.GOLD_BLOCK || ☃ == Blocks.GOLD_ORE) {
         return this.toolMaterial.getHarvestLevel() >= 2;
      } else if (☃ == Blocks.IRON_BLOCK || ☃ == Blocks.IRON_ORE) {
         return this.toolMaterial.getHarvestLevel() >= 1;
      } else if (☃ == Blocks.LAPIS_BLOCK || ☃ == Blocks.LAPIS_ORE) {
         return this.toolMaterial.getHarvestLevel() >= 1;
      } else if (☃ != Blocks.REDSTONE_ORE && ☃ != Blocks.LIT_REDSTONE_ORE) {
         Material ☃x = ☃.getMaterial();
         if (☃x == Material.ROCK) {
            return true;
         } else {
            return ☃x == Material.IRON ? true : ☃x == Material.ANVIL;
         }
      } else {
         return this.toolMaterial.getHarvestLevel() >= 2;
      }
   }

   @Override
   public float getDestroySpeed(ItemStack var1, IBlockState var2) {
      Material ☃ = ☃.getMaterial();
      return ☃ != Material.IRON && ☃ != Material.ANVIL && ☃ != Material.ROCK ? super.getDestroySpeed(☃, ☃) : this.efficiency;
   }
}
