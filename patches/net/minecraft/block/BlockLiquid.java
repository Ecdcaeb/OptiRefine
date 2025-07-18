package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLiquid extends Block {
   public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);

   protected BlockLiquid(Material var1) {
      super(☃);
      this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0));
      this.setTickRandomly(true);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return FULL_BLOCK_AABB;
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return NULL_AABB;
   }

   @Override
   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return this.material != Material.LAVA;
   }

   public static float getLiquidHeightPercent(int var0) {
      if (☃ >= 8) {
         ☃ = 0;
      }

      return (☃ + 1) / 9.0F;
   }

   protected int getDepth(IBlockState var1) {
      return ☃.getMaterial() == this.material ? ☃.getValue(LEVEL) : -1;
   }

   protected int getRenderedDepth(IBlockState var1) {
      int ☃ = this.getDepth(☃);
      return ☃ >= 8 ? 0 : ☃;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean canCollideCheck(IBlockState var1, boolean var2) {
      return ☃ && ☃.getValue(LEVEL) == 0;
   }

   private boolean causesDownwardCurrent(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      IBlockState ☃ = ☃.getBlockState(☃);
      Block ☃x = ☃.getBlock();
      Material ☃xx = ☃.getMaterial();
      if (☃xx == this.material) {
         return false;
      } else if (☃ == EnumFacing.UP) {
         return true;
      } else if (☃xx == Material.ICE) {
         return false;
      } else {
         boolean ☃xxx = isExceptBlockForAttachWithPiston(☃x) || ☃x instanceof BlockStairs;
         return !☃xxx && ☃.getBlockFaceShape(☃, ☃, ☃) == BlockFaceShape.SOLID;
      }
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      if (☃.getBlockState(☃.offset(☃)).getMaterial() == this.material) {
         return false;
      } else {
         return ☃ == EnumFacing.UP ? true : super.shouldSideBeRendered(☃, ☃, ☃, ☃);
      }
   }

   public boolean shouldRenderSides(IBlockAccess var1, BlockPos var2) {
      for (int ☃ = -1; ☃ <= 1; ☃++) {
         for (int ☃x = -1; ☃x <= 1; ☃x++) {
            IBlockState ☃xx = ☃.getBlockState(☃.add(☃, 0, ☃x));
            if (☃xx.getMaterial() != this.material && !☃xx.isFullBlock()) {
               return true;
            }
         }
      }

      return false;
   }

   @Override
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.LIQUID;
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.AIR;
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   protected Vec3d getFlow(IBlockAccess var1, BlockPos var2, IBlockState var3) {
      double ☃ = 0.0;
      double ☃x = 0.0;
      double ☃xx = 0.0;
      int ☃xxx = this.getRenderedDepth(☃);
      BlockPos.PooledMutableBlockPos ☃xxxx = BlockPos.PooledMutableBlockPos.retain();

      for (EnumFacing ☃xxxxx : EnumFacing.Plane.HORIZONTAL) {
         ☃xxxx.setPos(☃).move(☃xxxxx);
         int ☃xxxxxx = this.getRenderedDepth(☃.getBlockState(☃xxxx));
         if (☃xxxxxx < 0) {
            if (!☃.getBlockState(☃xxxx).getMaterial().blocksMovement()) {
               ☃xxxxxx = this.getRenderedDepth(☃.getBlockState(☃xxxx.down()));
               if (☃xxxxxx >= 0) {
                  int ☃xxxxxxx = ☃xxxxxx - (☃xxx - 8);
                  ☃ += ☃xxxxx.getXOffset() * ☃xxxxxxx;
                  ☃x += ☃xxxxx.getYOffset() * ☃xxxxxxx;
                  ☃xx += ☃xxxxx.getZOffset() * ☃xxxxxxx;
               }
            }
         } else if (☃xxxxxx >= 0) {
            int ☃xxxxxxx = ☃xxxxxx - ☃xxx;
            ☃ += ☃xxxxx.getXOffset() * ☃xxxxxxx;
            ☃x += ☃xxxxx.getYOffset() * ☃xxxxxxx;
            ☃xx += ☃xxxxx.getZOffset() * ☃xxxxxxx;
         }
      }

      Vec3d ☃xxxxxx = new Vec3d(☃, ☃x, ☃xx);
      if (☃.getValue(LEVEL) >= 8) {
         for (EnumFacing ☃xxxxxxx : EnumFacing.Plane.HORIZONTAL) {
            ☃xxxx.setPos(☃).move(☃xxxxxxx);
            if (this.causesDownwardCurrent(☃, ☃xxxx, ☃xxxxxxx) || this.causesDownwardCurrent(☃, ☃xxxx.up(), ☃xxxxxxx)) {
               ☃xxxxxx = ☃xxxxxx.normalize().add(0.0, -6.0, 0.0);
               break;
            }
         }
      }

      ☃xxxx.release();
      return ☃xxxxxx.normalize();
   }

   @Override
   public Vec3d modifyAcceleration(World var1, BlockPos var2, Entity var3, Vec3d var4) {
      return ☃.add(this.getFlow(☃, ☃, ☃.getBlockState(☃)));
   }

   @Override
   public int tickRate(World var1) {
      if (this.material == Material.WATER) {
         return 5;
      } else if (this.material == Material.LAVA) {
         return ☃.provider.isNether() ? 10 : 30;
      } else {
         return 0;
      }
   }

   @Override
   public int getPackedLightmapCoords(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      int ☃ = ☃.getCombinedLight(☃, 0);
      int ☃x = ☃.getCombinedLight(☃.up(), 0);
      int ☃xx = ☃ & 0xFF;
      int ☃xxx = ☃x & 0xFF;
      int ☃xxxx = ☃ >> 16 & 0xFF;
      int ☃xxxxx = ☃x >> 16 & 0xFF;
      return (☃xx > ☃xxx ? ☃xx : ☃xxx) | (☃xxxx > ☃xxxxx ? ☃xxxx : ☃xxxxx) << 16;
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return this.material == Material.WATER ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.SOLID;
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      double ☃ = ☃.getX();
      double ☃x = ☃.getY();
      double ☃xx = ☃.getZ();
      if (this.material == Material.WATER) {
         int ☃xxx = ☃.getValue(LEVEL);
         if (☃xxx > 0 && ☃xxx < 8) {
            if (☃.nextInt(64) == 0) {
               ☃.playSound(
                  ☃ + 0.5,
                  ☃x + 0.5,
                  ☃xx + 0.5,
                  SoundEvents.BLOCK_WATER_AMBIENT,
                  SoundCategory.BLOCKS,
                  ☃.nextFloat() * 0.25F + 0.75F,
                  ☃.nextFloat() + 0.5F,
                  false
               );
            }
         } else if (☃.nextInt(10) == 0) {
            ☃.spawnParticle(EnumParticleTypes.SUSPENDED, ☃ + ☃.nextFloat(), ☃x + ☃.nextFloat(), ☃xx + ☃.nextFloat(), 0.0, 0.0, 0.0);
         }
      }

      if (this.material == Material.LAVA && ☃.getBlockState(☃.up()).getMaterial() == Material.AIR && !☃.getBlockState(☃.up()).isOpaqueCube()) {
         if (☃.nextInt(100) == 0) {
            double ☃xxx = ☃ + ☃.nextFloat();
            double ☃xxxx = ☃x + ☃.getBoundingBox(☃, ☃).maxY;
            double ☃xxxxx = ☃xx + ☃.nextFloat();
            ☃.spawnParticle(EnumParticleTypes.LAVA, ☃xxx, ☃xxxx, ☃xxxxx, 0.0, 0.0, 0.0);
            ☃.playSound(☃xxx, ☃xxxx, ☃xxxxx, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + ☃.nextFloat() * 0.2F, 0.9F + ☃.nextFloat() * 0.15F, false);
         }

         if (☃.nextInt(200) == 0) {
            ☃.playSound(☃, ☃x, ☃xx, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + ☃.nextFloat() * 0.2F, 0.9F + ☃.nextFloat() * 0.15F, false);
         }
      }

      if (☃.nextInt(10) == 0 && ☃.getBlockState(☃.down()).isTopSolid()) {
         Material ☃xxx = ☃.getBlockState(☃.down(2)).getMaterial();
         if (!☃xxx.blocksMovement() && !☃xxx.isLiquid()) {
            double ☃xxxx = ☃ + ☃.nextFloat();
            double ☃xxxxx = ☃x - 1.05;
            double ☃xxxxxx = ☃xx + ☃.nextFloat();
            if (this.material == Material.WATER) {
               ☃.spawnParticle(EnumParticleTypes.DRIP_WATER, ☃xxxx, ☃xxxxx, ☃xxxxxx, 0.0, 0.0, 0.0);
            } else {
               ☃.spawnParticle(EnumParticleTypes.DRIP_LAVA, ☃xxxx, ☃xxxxx, ☃xxxxxx, 0.0, 0.0, 0.0);
            }
         }
      }
   }

   public static float getSlopeAngle(IBlockAccess var0, BlockPos var1, Material var2, IBlockState var3) {
      Vec3d ☃ = getFlowingBlock(☃).getFlow(☃, ☃, ☃);
      return ☃.x == 0.0 && ☃.z == 0.0 ? -1000.0F : (float)MathHelper.atan2(☃.z, ☃.x) - (float) (Math.PI / 2);
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      this.checkForMixing(☃, ☃, ☃);
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      this.checkForMixing(☃, ☃, ☃);
   }

   public boolean checkForMixing(World var1, BlockPos var2, IBlockState var3) {
      if (this.material == Material.LAVA) {
         boolean ☃ = false;

         for (EnumFacing ☃x : EnumFacing.values()) {
            if (☃x != EnumFacing.DOWN && ☃.getBlockState(☃.offset(☃x)).getMaterial() == Material.WATER) {
               ☃ = true;
               break;
            }
         }

         if (☃) {
            Integer ☃xx = ☃.getValue(LEVEL);
            if (☃xx == 0) {
               ☃.setBlockState(☃, Blocks.OBSIDIAN.getDefaultState());
               this.triggerMixEffects(☃, ☃);
               return true;
            }

            if (☃xx <= 4) {
               ☃.setBlockState(☃, Blocks.COBBLESTONE.getDefaultState());
               this.triggerMixEffects(☃, ☃);
               return true;
            }
         }
      }

      return false;
   }

   protected void triggerMixEffects(World var1, BlockPos var2) {
      double ☃ = ☃.getX();
      double ☃x = ☃.getY();
      double ☃xx = ☃.getZ();
      ☃.playSound(null, ☃, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (☃.rand.nextFloat() - ☃.rand.nextFloat()) * 0.8F);

      for (int ☃xxx = 0; ☃xxx < 8; ☃xxx++) {
         ☃.spawnParticle(EnumParticleTypes.SMOKE_LARGE, ☃ + Math.random(), ☃x + 1.2, ☃xx + Math.random(), 0.0, 0.0, 0.0);
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(LEVEL, ☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(LEVEL);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, LEVEL);
   }

   public static BlockDynamicLiquid getFlowingBlock(Material var0) {
      if (☃ == Material.WATER) {
         return Blocks.FLOWING_WATER;
      } else if (☃ == Material.LAVA) {
         return Blocks.FLOWING_LAVA;
      } else {
         throw new IllegalArgumentException("Invalid material");
      }
   }

   public static BlockStaticLiquid getStaticBlock(Material var0) {
      if (☃ == Material.WATER) {
         return Blocks.WATER;
      } else if (☃ == Material.LAVA) {
         return Blocks.LAVA;
      } else {
         throw new IllegalArgumentException("Invalid material");
      }
   }

   public static float getBlockLiquidHeight(IBlockState var0, IBlockAccess var1, BlockPos var2) {
      int ☃ = ☃.getValue(LEVEL);
      return (☃ & 7) == 0 && ☃.getBlockState(☃.up()).getMaterial() == Material.WATER ? 1.0F : 1.0F - getLiquidHeightPercent(☃);
   }

   public static float getLiquidHeight(IBlockState var0, IBlockAccess var1, BlockPos var2) {
      return ☃.getY() + getBlockLiquidHeight(☃, ☃, ☃);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
