package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.EntityCreature;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityAIWanderAvoidWaterFlying extends EntityAIWanderAvoidWater {
   public EntityAIWanderAvoidWaterFlying(EntityCreature var1, double var2) {
      super(☃, ☃);
   }

   @Nullable
   @Override
   protected Vec3d getPosition() {
      Vec3d ☃ = null;
      if (this.entity.isInWater() || this.entity.isOverWater()) {
         ☃ = RandomPositionGenerator.getLandPos(this.entity, 15, 15);
      }

      if (this.entity.getRNG().nextFloat() >= this.probability) {
         ☃ = this.getTreePos();
      }

      return ☃ == null ? super.getPosition() : ☃;
   }

   @Nullable
   private Vec3d getTreePos() {
      BlockPos ☃ = new BlockPos(this.entity);
      BlockPos.MutableBlockPos ☃x = new BlockPos.MutableBlockPos();
      BlockPos.MutableBlockPos ☃xx = new BlockPos.MutableBlockPos();

      for (BlockPos ☃xxx : BlockPos.MutableBlockPos.getAllInBoxMutable(
         MathHelper.floor(this.entity.posX - 3.0),
         MathHelper.floor(this.entity.posY - 6.0),
         MathHelper.floor(this.entity.posZ - 3.0),
         MathHelper.floor(this.entity.posX + 3.0),
         MathHelper.floor(this.entity.posY + 6.0),
         MathHelper.floor(this.entity.posZ + 3.0)
      )) {
         if (!☃.equals(☃xxx)) {
            Block ☃xxxx = this.entity.world.getBlockState(☃xx.setPos(☃xxx).move(EnumFacing.DOWN)).getBlock();
            boolean ☃xxxxx = ☃xxxx instanceof BlockLeaves || ☃xxxx == Blocks.LOG || ☃xxxx == Blocks.LOG2;
            if (☃xxxxx && this.entity.world.isAirBlock(☃xxx) && this.entity.world.isAirBlock(☃x.setPos(☃xxx).move(EnumFacing.UP))) {
               return new Vec3d(☃xxx.getX(), ☃xxx.getY(), ☃xxx.getZ());
            }
         }
      }

      return null;
   }
}
