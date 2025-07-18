package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIEatGrass extends EntityAIBase {
   private static final Predicate<IBlockState> IS_TALL_GRASS = BlockStateMatcher.forBlock(Blocks.TALLGRASS)
      .where(BlockTallGrass.TYPE, Predicates.equalTo(BlockTallGrass.EnumType.GRASS));
   private final EntityLiving grassEaterEntity;
   private final World entityWorld;
   int eatingGrassTimer;

   public EntityAIEatGrass(EntityLiving var1) {
      this.grassEaterEntity = ☃;
      this.entityWorld = ☃.world;
      this.setMutexBits(7);
   }

   @Override
   public boolean shouldExecute() {
      if (this.grassEaterEntity.getRNG().nextInt(this.grassEaterEntity.isChild() ? 50 : 1000) != 0) {
         return false;
      } else {
         BlockPos ☃ = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
         return IS_TALL_GRASS.apply(this.entityWorld.getBlockState(☃)) ? true : this.entityWorld.getBlockState(☃.down()).getBlock() == Blocks.GRASS;
      }
   }

   @Override
   public void startExecuting() {
      this.eatingGrassTimer = 40;
      this.entityWorld.setEntityState(this.grassEaterEntity, (byte)10);
      this.grassEaterEntity.getNavigator().clearPath();
   }

   @Override
   public void resetTask() {
      this.eatingGrassTimer = 0;
   }

   @Override
   public boolean shouldContinueExecuting() {
      return this.eatingGrassTimer > 0;
   }

   public int getEatingGrassTimer() {
      return this.eatingGrassTimer;
   }

   @Override
   public void updateTask() {
      this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
      if (this.eatingGrassTimer == 4) {
         BlockPos ☃ = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
         if (IS_TALL_GRASS.apply(this.entityWorld.getBlockState(☃))) {
            if (this.entityWorld.getGameRules().getBoolean("mobGriefing")) {
               this.entityWorld.destroyBlock(☃, false);
            }

            this.grassEaterEntity.eatGrassBonus();
         } else {
            BlockPos ☃x = ☃.down();
            if (this.entityWorld.getBlockState(☃x).getBlock() == Blocks.GRASS) {
               if (this.entityWorld.getGameRules().getBoolean("mobGriefing")) {
                  this.entityWorld.playEvent(2001, ☃x, Block.getIdFromBlock(Blocks.GRASS));
                  this.entityWorld.setBlockState(☃x, Blocks.DIRT.getDefaultState(), 2);
               }

               this.grassEaterEntity.eatGrassBonus();
            }
         }
      }
   }
}
