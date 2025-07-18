package net.minecraft.block;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

public class BlockFire extends Block {
   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   public static final PropertyBool UPPER = PropertyBool.create("up");
   private final Map<Block, Integer> encouragements = Maps.newIdentityHashMap();
   private final Map<Block, Integer> flammabilities = Maps.newIdentityHashMap();

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return !☃.getBlockState(☃.down()).isTopSolid() && !Blocks.FIRE.canCatchFire(☃, ☃.down())
         ? ☃.withProperty(NORTH, this.canCatchFire(☃, ☃.north()))
            .withProperty(EAST, this.canCatchFire(☃, ☃.east()))
            .withProperty(SOUTH, this.canCatchFire(☃, ☃.south()))
            .withProperty(WEST, this.canCatchFire(☃, ☃.west()))
            .withProperty(UPPER, this.canCatchFire(☃, ☃.up()))
         : this.getDefaultState();
   }

   protected BlockFire() {
      super(Material.FIRE);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(AGE, 0)
            .withProperty(NORTH, false)
            .withProperty(EAST, false)
            .withProperty(SOUTH, false)
            .withProperty(WEST, false)
            .withProperty(UPPER, false)
      );
      this.setTickRandomly(true);
   }

   public static void init() {
      Blocks.FIRE.setFireInfo(Blocks.PLANKS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.DOUBLE_WOODEN_SLAB, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.WOODEN_SLAB, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.OAK_FENCE_GATE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.SPRUCE_FENCE_GATE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.BIRCH_FENCE_GATE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.JUNGLE_FENCE_GATE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.ACACIA_FENCE_GATE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.OAK_FENCE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.SPRUCE_FENCE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.BIRCH_FENCE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.JUNGLE_FENCE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.DARK_OAK_FENCE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.ACACIA_FENCE, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.OAK_STAIRS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.BIRCH_STAIRS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.SPRUCE_STAIRS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.JUNGLE_STAIRS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.ACACIA_STAIRS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.DARK_OAK_STAIRS, 5, 20);
      Blocks.FIRE.setFireInfo(Blocks.LOG, 5, 5);
      Blocks.FIRE.setFireInfo(Blocks.LOG2, 5, 5);
      Blocks.FIRE.setFireInfo(Blocks.LEAVES, 30, 60);
      Blocks.FIRE.setFireInfo(Blocks.LEAVES2, 30, 60);
      Blocks.FIRE.setFireInfo(Blocks.BOOKSHELF, 30, 20);
      Blocks.FIRE.setFireInfo(Blocks.TNT, 15, 100);
      Blocks.FIRE.setFireInfo(Blocks.TALLGRASS, 60, 100);
      Blocks.FIRE.setFireInfo(Blocks.DOUBLE_PLANT, 60, 100);
      Blocks.FIRE.setFireInfo(Blocks.YELLOW_FLOWER, 60, 100);
      Blocks.FIRE.setFireInfo(Blocks.RED_FLOWER, 60, 100);
      Blocks.FIRE.setFireInfo(Blocks.DEADBUSH, 60, 100);
      Blocks.FIRE.setFireInfo(Blocks.WOOL, 30, 60);
      Blocks.FIRE.setFireInfo(Blocks.VINE, 15, 100);
      Blocks.FIRE.setFireInfo(Blocks.COAL_BLOCK, 5, 5);
      Blocks.FIRE.setFireInfo(Blocks.HAY_BLOCK, 60, 20);
      Blocks.FIRE.setFireInfo(Blocks.CARPET, 60, 20);
   }

   public void setFireInfo(Block var1, int var2, int var3) {
      this.encouragements.put(☃, ☃);
      this.flammabilities.put(☃, ☃);
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return NULL_AABB;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public int tickRate(World var1) {
      return 30;
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (☃.getGameRules().getBoolean("doFireTick")) {
         if (!this.canPlaceBlockAt(☃, ☃)) {
            ☃.setBlockToAir(☃);
         }

         Block ☃ = ☃.getBlockState(☃.down()).getBlock();
         boolean ☃x = ☃ == Blocks.NETHERRACK || ☃ == Blocks.MAGMA;
         if (☃.provider instanceof WorldProviderEnd && ☃ == Blocks.BEDROCK) {
            ☃x = true;
         }

         int ☃xx = ☃.getValue(AGE);
         if (!☃x && ☃.isRaining() && this.canDie(☃, ☃) && ☃.nextFloat() < 0.2F + ☃xx * 0.03F) {
            ☃.setBlockToAir(☃);
         } else {
            if (☃xx < 15) {
               ☃ = ☃.withProperty(AGE, ☃xx + ☃.nextInt(3) / 2);
               ☃.setBlockState(☃, ☃, 4);
            }

            ☃.scheduleUpdate(☃, this, this.tickRate(☃) + ☃.nextInt(10));
            if (!☃x) {
               if (!this.canNeighborCatchFire(☃, ☃)) {
                  if (!☃.getBlockState(☃.down()).isTopSolid() || ☃xx > 3) {
                     ☃.setBlockToAir(☃);
                  }

                  return;
               }

               if (!this.canCatchFire(☃, ☃.down()) && ☃xx == 15 && ☃.nextInt(4) == 0) {
                  ☃.setBlockToAir(☃);
                  return;
               }
            }

            boolean ☃xxx = ☃.isBlockinHighHumidity(☃);
            int ☃xxxx = 0;
            if (☃xxx) {
               ☃xxxx = -50;
            }

            this.catchOnFire(☃, ☃.east(), 300 + ☃xxxx, ☃, ☃xx);
            this.catchOnFire(☃, ☃.west(), 300 + ☃xxxx, ☃, ☃xx);
            this.catchOnFire(☃, ☃.down(), 250 + ☃xxxx, ☃, ☃xx);
            this.catchOnFire(☃, ☃.up(), 250 + ☃xxxx, ☃, ☃xx);
            this.catchOnFire(☃, ☃.north(), 300 + ☃xxxx, ☃, ☃xx);
            this.catchOnFire(☃, ☃.south(), 300 + ☃xxxx, ☃, ☃xx);

            for (int ☃xxxxx = -1; ☃xxxxx <= 1; ☃xxxxx++) {
               for (int ☃xxxxxx = -1; ☃xxxxxx <= 1; ☃xxxxxx++) {
                  for (int ☃xxxxxxx = -1; ☃xxxxxxx <= 4; ☃xxxxxxx++) {
                     if (☃xxxxx != 0 || ☃xxxxxxx != 0 || ☃xxxxxx != 0) {
                        int ☃xxxxxxxx = 100;
                        if (☃xxxxxxx > 1) {
                           ☃xxxxxxxx += (☃xxxxxxx - 1) * 100;
                        }

                        BlockPos ☃xxxxxxxxx = ☃.add(☃xxxxx, ☃xxxxxxx, ☃xxxxxx);
                        int ☃xxxxxxxxxx = this.getNeighborEncouragement(☃, ☃xxxxxxxxx);
                        if (☃xxxxxxxxxx > 0) {
                           int ☃xxxxxxxxxxx = (☃xxxxxxxxxx + 40 + ☃.getDifficulty().getId() * 7) / (☃xx + 30);
                           if (☃xxx) {
                              ☃xxxxxxxxxxx /= 2;
                           }

                           if (☃xxxxxxxxxxx > 0 && ☃.nextInt(☃xxxxxxxx) <= ☃xxxxxxxxxxx && (!☃.isRaining() || !this.canDie(☃, ☃xxxxxxxxx))) {
                              int ☃xxxxxxxxxxxx = ☃xx + ☃.nextInt(5) / 4;
                              if (☃xxxxxxxxxxxx > 15) {
                                 ☃xxxxxxxxxxxx = 15;
                              }

                              ☃.setBlockState(☃xxxxxxxxx, ☃.withProperty(AGE, ☃xxxxxxxxxxxx), 3);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   protected boolean canDie(World var1, BlockPos var2) {
      return ☃.isRainingAt(☃) || ☃.isRainingAt(☃.west()) || ☃.isRainingAt(☃.east()) || ☃.isRainingAt(☃.north()) || ☃.isRainingAt(☃.south());
   }

   @Override
   public boolean requiresUpdates() {
      return false;
   }

   private int getFlammability(Block var1) {
      Integer ☃ = this.flammabilities.get(☃);
      return ☃ == null ? 0 : ☃;
   }

   private int getEncouragement(Block var1) {
      Integer ☃ = this.encouragements.get(☃);
      return ☃ == null ? 0 : ☃;
   }

   private void catchOnFire(World var1, BlockPos var2, int var3, Random var4, int var5) {
      int ☃ = this.getFlammability(☃.getBlockState(☃).getBlock());
      if (☃.nextInt(☃) < ☃) {
         IBlockState ☃x = ☃.getBlockState(☃);
         if (☃.nextInt(☃ + 10) < 5 && !☃.isRainingAt(☃)) {
            int ☃xx = ☃ + ☃.nextInt(5) / 4;
            if (☃xx > 15) {
               ☃xx = 15;
            }

            ☃.setBlockState(☃, this.getDefaultState().withProperty(AGE, ☃xx), 3);
         } else {
            ☃.setBlockToAir(☃);
         }

         if (☃x.getBlock() == Blocks.TNT) {
            Blocks.TNT.onPlayerDestroy(☃, ☃, ☃x.withProperty(BlockTNT.EXPLODE, true));
         }
      }
   }

   private boolean canNeighborCatchFire(World var1, BlockPos var2) {
      for (EnumFacing ☃ : EnumFacing.values()) {
         if (this.canCatchFire(☃, ☃.offset(☃))) {
            return true;
         }
      }

      return false;
   }

   private int getNeighborEncouragement(World var1, BlockPos var2) {
      if (!☃.isAirBlock(☃)) {
         return 0;
      } else {
         int ☃ = 0;

         for (EnumFacing ☃x : EnumFacing.values()) {
            ☃ = Math.max(this.getEncouragement(☃.getBlockState(☃.offset(☃x)).getBlock()), ☃);
         }

         return ☃;
      }
   }

   @Override
   public boolean isCollidable() {
      return false;
   }

   public boolean canCatchFire(IBlockAccess var1, BlockPos var2) {
      return this.getEncouragement(☃.getBlockState(☃).getBlock()) > 0;
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return ☃.getBlockState(☃.down()).isTopSolid() || this.canNeighborCatchFire(☃, ☃);
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.getBlockState(☃.down()).isTopSolid() && !this.canNeighborCatchFire(☃, ☃)) {
         ☃.setBlockToAir(☃);
      }
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (☃.provider.getDimensionType().getId() > 0 || !Blocks.PORTAL.trySpawnPortal(☃, ☃)) {
         if (!☃.getBlockState(☃.down()).isTopSolid() && !this.canNeighborCatchFire(☃, ☃)) {
            ☃.setBlockToAir(☃);
         } else {
            ☃.scheduleUpdate(☃, this, this.tickRate(☃) + ☃.rand.nextInt(10));
         }
      }
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      if (☃.nextInt(24) == 0) {
         ☃.playSound(
            ☃.getX() + 0.5F,
            ☃.getY() + 0.5F,
            ☃.getZ() + 0.5F,
            SoundEvents.BLOCK_FIRE_AMBIENT,
            SoundCategory.BLOCKS,
            1.0F + ☃.nextFloat(),
            ☃.nextFloat() * 0.7F + 0.3F,
            false
         );
      }

      if (!☃.getBlockState(☃.down()).isTopSolid() && !Blocks.FIRE.canCatchFire(☃, ☃.down())) {
         if (Blocks.FIRE.canCatchFire(☃, ☃.west())) {
            for (int ☃ = 0; ☃ < 2; ☃++) {
               double ☃x = ☃.getX() + ☃.nextDouble() * 0.1F;
               double ☃xx = ☃.getY() + ☃.nextDouble();
               double ☃xxx = ☃.getZ() + ☃.nextDouble();
               ☃.spawnParticle(EnumParticleTypes.SMOKE_LARGE, ☃x, ☃xx, ☃xxx, 0.0, 0.0, 0.0);
            }
         }

         if (Blocks.FIRE.canCatchFire(☃, ☃.east())) {
            for (int ☃ = 0; ☃ < 2; ☃++) {
               double ☃x = ☃.getX() + 1 - ☃.nextDouble() * 0.1F;
               double ☃xx = ☃.getY() + ☃.nextDouble();
               double ☃xxx = ☃.getZ() + ☃.nextDouble();
               ☃.spawnParticle(EnumParticleTypes.SMOKE_LARGE, ☃x, ☃xx, ☃xxx, 0.0, 0.0, 0.0);
            }
         }

         if (Blocks.FIRE.canCatchFire(☃, ☃.north())) {
            for (int ☃ = 0; ☃ < 2; ☃++) {
               double ☃x = ☃.getX() + ☃.nextDouble();
               double ☃xx = ☃.getY() + ☃.nextDouble();
               double ☃xxx = ☃.getZ() + ☃.nextDouble() * 0.1F;
               ☃.spawnParticle(EnumParticleTypes.SMOKE_LARGE, ☃x, ☃xx, ☃xxx, 0.0, 0.0, 0.0);
            }
         }

         if (Blocks.FIRE.canCatchFire(☃, ☃.south())) {
            for (int ☃ = 0; ☃ < 2; ☃++) {
               double ☃x = ☃.getX() + ☃.nextDouble();
               double ☃xx = ☃.getY() + ☃.nextDouble();
               double ☃xxx = ☃.getZ() + 1 - ☃.nextDouble() * 0.1F;
               ☃.spawnParticle(EnumParticleTypes.SMOKE_LARGE, ☃x, ☃xx, ☃xxx, 0.0, 0.0, 0.0);
            }
         }

         if (Blocks.FIRE.canCatchFire(☃, ☃.up())) {
            for (int ☃ = 0; ☃ < 2; ☃++) {
               double ☃x = ☃.getX() + ☃.nextDouble();
               double ☃xx = ☃.getY() + 1 - ☃.nextDouble() * 0.1F;
               double ☃xxx = ☃.getZ() + ☃.nextDouble();
               ☃.spawnParticle(EnumParticleTypes.SMOKE_LARGE, ☃x, ☃xx, ☃xxx, 0.0, 0.0, 0.0);
            }
         }
      } else {
         for (int ☃ = 0; ☃ < 3; ☃++) {
            double ☃x = ☃.getX() + ☃.nextDouble();
            double ☃xx = ☃.getY() + ☃.nextDouble() * 0.5 + 0.5;
            double ☃xxx = ☃.getZ() + ☃.nextDouble();
            ☃.spawnParticle(EnumParticleTypes.SMOKE_LARGE, ☃x, ☃xx, ☃xxx, 0.0, 0.0, 0.0);
         }
      }
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return MapColor.TNT;
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(AGE, ☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(AGE);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, AGE, NORTH, EAST, SOUTH, WEST, UPPER);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
