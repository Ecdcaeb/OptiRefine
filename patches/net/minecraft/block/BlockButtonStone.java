package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockButtonStone extends BlockButton {
   protected BlockButtonStone() {
      super(false);
   }

   @Override
   protected void playClickSound(@Nullable EntityPlayer var1, World var2, BlockPos var3) {
      ☃.playSound(☃, ☃, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
   }

   @Override
   protected void playReleaseSound(World var1, BlockPos var2) {
      ☃.playSound(null, ☃, SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
   }
}
