package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPumpkin extends BlockHorizontal {
   private BlockPattern snowmanBasePattern;
   private BlockPattern snowmanPattern;
   private BlockPattern golemBasePattern;
   private BlockPattern golemPattern;
   private static final Predicate<IBlockState> IS_PUMPKIN = new Predicate<IBlockState>() {
      public boolean apply(@Nullable IBlockState var1) {
         return ☃ != null && (☃.getBlock() == Blocks.PUMPKIN || ☃.getBlock() == Blocks.LIT_PUMPKIN);
      }
   };

   protected BlockPumpkin() {
      super(Material.GOURD, MapColor.ADOBE);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      super.onBlockAdded(☃, ☃, ☃);
      this.trySpawnGolem(☃, ☃);
   }

   public boolean canDispenserPlace(World var1, BlockPos var2) {
      return this.getSnowmanBasePattern().match(☃, ☃) != null || this.getGolemBasePattern().match(☃, ☃) != null;
   }

   private void trySpawnGolem(World var1, BlockPos var2) {
      BlockPattern.PatternHelper ☃ = this.getSnowmanPattern().match(☃, ☃);
      if (☃ != null) {
         for (int ☃x = 0; ☃x < this.getSnowmanPattern().getThumbLength(); ☃x++) {
            BlockWorldState ☃xx = ☃.translateOffset(0, ☃x, 0);
            ☃.setBlockState(☃xx.getPos(), Blocks.AIR.getDefaultState(), 2);
         }

         EntitySnowman ☃x = new EntitySnowman(☃);
         BlockPos ☃xx = ☃.translateOffset(0, 2, 0).getPos();
         ☃x.setLocationAndAngles(☃xx.getX() + 0.5, ☃xx.getY() + 0.05, ☃xx.getZ() + 0.5, 0.0F, 0.0F);
         ☃.spawnEntity(☃x);

         for (EntityPlayerMP ☃xxx : ☃.getEntitiesWithinAABB(EntityPlayerMP.class, ☃x.getEntityBoundingBox().grow(5.0))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(☃xxx, ☃x);
         }

         for (int ☃xxx = 0; ☃xxx < 120; ☃xxx++) {
            ☃.spawnParticle(
               EnumParticleTypes.SNOW_SHOVEL,
               ☃xx.getX() + ☃.rand.nextDouble(),
               ☃xx.getY() + ☃.rand.nextDouble() * 2.5,
               ☃xx.getZ() + ☃.rand.nextDouble(),
               0.0,
               0.0,
               0.0
            );
         }

         for (int ☃xxx = 0; ☃xxx < this.getSnowmanPattern().getThumbLength(); ☃xxx++) {
            BlockWorldState ☃xxxx = ☃.translateOffset(0, ☃xxx, 0);
            ☃.notifyNeighborsRespectDebug(☃xxxx.getPos(), Blocks.AIR, false);
         }
      } else {
         ☃ = this.getGolemPattern().match(☃, ☃);
         if (☃ != null) {
            for (int ☃x = 0; ☃x < this.getGolemPattern().getPalmLength(); ☃x++) {
               for (int ☃xx = 0; ☃xx < this.getGolemPattern().getThumbLength(); ☃xx++) {
                  ☃.setBlockState(☃.translateOffset(☃x, ☃xx, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
               }
            }

            BlockPos ☃x = ☃.translateOffset(1, 2, 0).getPos();
            EntityIronGolem ☃xx = new EntityIronGolem(☃);
            ☃xx.setPlayerCreated(true);
            ☃xx.setLocationAndAngles(☃x.getX() + 0.5, ☃x.getY() + 0.05, ☃x.getZ() + 0.5, 0.0F, 0.0F);
            ☃.spawnEntity(☃xx);

            for (EntityPlayerMP ☃xxx : ☃.getEntitiesWithinAABB(EntityPlayerMP.class, ☃xx.getEntityBoundingBox().grow(5.0))) {
               CriteriaTriggers.SUMMONED_ENTITY.trigger(☃xxx, ☃xx);
            }

            for (int ☃xxx = 0; ☃xxx < 120; ☃xxx++) {
               ☃.spawnParticle(
                  EnumParticleTypes.SNOWBALL,
                  ☃x.getX() + ☃.rand.nextDouble(),
                  ☃x.getY() + ☃.rand.nextDouble() * 3.9,
                  ☃x.getZ() + ☃.rand.nextDouble(),
                  0.0,
                  0.0,
                  0.0
               );
            }

            for (int ☃xxx = 0; ☃xxx < this.getGolemPattern().getPalmLength(); ☃xxx++) {
               for (int ☃xxxx = 0; ☃xxxx < this.getGolemPattern().getThumbLength(); ☃xxxx++) {
                  BlockWorldState ☃xxxxx = ☃.translateOffset(☃xxx, ☃xxxx, 0);
                  ☃.notifyNeighborsRespectDebug(☃xxxxx.getPos(), Blocks.AIR, false);
               }
            }
         }
      }
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return ☃.getBlockState(☃).getBlock().material.isReplaceable() && ☃.getBlockState(☃.down()).isTopSolid();
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      return ☃.withProperty(FACING, ☃.rotate(☃.getValue(FACING)));
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      return ☃.withRotation(☃.toRotation(☃.getValue(FACING)));
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, ☃.getHorizontalFacing().getOpposite());
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(☃));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(FACING).getHorizontalIndex();
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING);
   }

   protected BlockPattern getSnowmanBasePattern() {
      if (this.snowmanBasePattern == null) {
         this.snowmanBasePattern = FactoryBlockPattern.start()
            .aisle(" ", "#", "#")
            .where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SNOW)))
            .build();
      }

      return this.snowmanBasePattern;
   }

   protected BlockPattern getSnowmanPattern() {
      if (this.snowmanPattern == null) {
         this.snowmanPattern = FactoryBlockPattern.start()
            .aisle("^", "#", "#")
            .where('^', BlockWorldState.hasState(IS_PUMPKIN))
            .where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SNOW)))
            .build();
      }

      return this.snowmanPattern;
   }

   protected BlockPattern getGolemBasePattern() {
      if (this.golemBasePattern == null) {
         this.golemBasePattern = FactoryBlockPattern.start()
            .aisle("~ ~", "###", "~#~")
            .where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK)))
            .where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR)))
            .build();
      }

      return this.golemBasePattern;
   }

   protected BlockPattern getGolemPattern() {
      if (this.golemPattern == null) {
         this.golemPattern = FactoryBlockPattern.start()
            .aisle("~^~", "###", "~#~")
            .where('^', BlockWorldState.hasState(IS_PUMPKIN))
            .where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK)))
            .where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR)))
            .build();
      }

      return this.golemPattern;
   }
}
