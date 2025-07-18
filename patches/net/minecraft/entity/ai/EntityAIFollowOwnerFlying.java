package net.minecraft.entity.ai;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.BlockPos;

public class EntityAIFollowOwnerFlying extends EntityAIFollowOwner {
   public EntityAIFollowOwnerFlying(EntityTameable var1, double var2, float var4, float var5) {
      super(☃, ☃, ☃, ☃);
   }

   @Override
   protected boolean isTeleportFriendlyBlock(int var1, int var2, int var3, int var4, int var5) {
      IBlockState ☃ = this.world.getBlockState(new BlockPos(☃ + ☃, ☃ - 1, ☃ + ☃));
      return (☃.isTopSolid() || ☃.getMaterial() == Material.LEAVES)
         && this.world.isAirBlock(new BlockPos(☃ + ☃, ☃, ☃ + ☃))
         && this.world.isAirBlock(new BlockPos(☃ + ☃, ☃ + 1, ☃ + ☃));
   }
}
