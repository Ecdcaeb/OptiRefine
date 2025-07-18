package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class BlockMatcher implements Predicate<IBlockState> {
   private final Block block;

   private BlockMatcher(Block var1) {
      this.block = ☃;
   }

   public static BlockMatcher forBlock(Block var0) {
      return new BlockMatcher(☃);
   }

   public boolean apply(@Nullable IBlockState var1) {
      return ☃ != null && ☃.getBlock() == this.block;
   }
}
