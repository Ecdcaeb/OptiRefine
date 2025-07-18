package net.minecraft.item;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class ItemAxe extends ItemTool {
   private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(
      new Block[]{
         Blocks.PLANKS,
         Blocks.BOOKSHELF,
         Blocks.LOG,
         Blocks.LOG2,
         Blocks.CHEST,
         Blocks.PUMPKIN,
         Blocks.LIT_PUMPKIN,
         Blocks.MELON_BLOCK,
         Blocks.LADDER,
         Blocks.WOODEN_BUTTON,
         Blocks.WOODEN_PRESSURE_PLATE
      }
   );
   private static final float[] ATTACK_DAMAGES = new float[]{6.0F, 8.0F, 8.0F, 8.0F, 6.0F};
   private static final float[] ATTACK_SPEEDS = new float[]{-3.2F, -3.2F, -3.1F, -3.0F, -3.0F};

   protected ItemAxe(Item.ToolMaterial var1) {
      super(☃, EFFECTIVE_ON);
      this.attackDamage = ATTACK_DAMAGES[☃.ordinal()];
      this.attackSpeed = ATTACK_SPEEDS[☃.ordinal()];
   }

   @Override
   public float getDestroySpeed(ItemStack var1, IBlockState var2) {
      Material ☃ = ☃.getMaterial();
      return ☃ != Material.WOOD && ☃ != Material.PLANTS && ☃ != Material.VINE ? super.getDestroySpeed(☃, ☃) : this.efficiency;
   }
}
