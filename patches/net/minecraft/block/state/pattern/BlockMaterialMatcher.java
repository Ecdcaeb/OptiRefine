package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockMaterialMatcher implements Predicate<IBlockState> {
   private final Material material;

   private BlockMaterialMatcher(Material var1) {
      this.material = ☃;
   }

   public static BlockMaterialMatcher forMaterial(Material var0) {
      return new BlockMaterialMatcher(☃);
   }

   public boolean apply(@Nullable IBlockState var1) {
      return ☃ != null && ☃.getMaterial() == this.material;
   }
}
