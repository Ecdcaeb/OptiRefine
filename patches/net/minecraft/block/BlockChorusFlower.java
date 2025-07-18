package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChorusFlower extends Block {
   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 5);

   protected BlockChorusFlower() {
      super(Material.PLANTS, MapColor.PURPLE);
      this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
      this.setCreativeTab(CreativeTabs.DECORATIONS);
      this.setTickRandomly(true);
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.AIR;
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!this.canSurvive(☃, ☃)) {
         ☃.destroyBlock(☃, true);
      } else {
         BlockPos ☃ = ☃.up();
         if (☃.isAirBlock(☃) && ☃.getY() < 256) {
            int ☃x = ☃.getValue(AGE);
            if (☃x < 5 && ☃.nextInt(1) == 0) {
               boolean ☃xx = false;
               boolean ☃xxx = false;
               IBlockState ☃xxxx = ☃.getBlockState(☃.down());
               Block ☃xxxxx = ☃xxxx.getBlock();
               if (☃xxxxx == Blocks.END_STONE) {
                  ☃xx = true;
               } else if (☃xxxxx == Blocks.CHORUS_PLANT) {
                  int ☃xxxxxx = 1;

                  for (int ☃xxxxxxx = 0; ☃xxxxxxx < 4; ☃xxxxxxx++) {
                     Block ☃xxxxxxxx = ☃.getBlockState(☃.down(☃xxxxxx + 1)).getBlock();
                     if (☃xxxxxxxx != Blocks.CHORUS_PLANT) {
                        if (☃xxxxxxxx == Blocks.END_STONE) {
                           ☃xxx = true;
                        }
                        break;
                     }

                     ☃xxxxxx++;
                  }

                  int ☃xxxxxxx = 4;
                  if (☃xxx) {
                     ☃xxxxxxx++;
                  }

                  if (☃xxxxxx < 2 || ☃.nextInt(☃xxxxxxx) >= ☃xxxxxx) {
                     ☃xx = true;
                  }
               } else if (☃xxxx.getMaterial() == Material.AIR) {
                  ☃xx = true;
               }

               if (☃xx && areAllNeighborsEmpty(☃, ☃, null) && ☃.isAirBlock(☃.up(2))) {
                  ☃.setBlockState(☃, Blocks.CHORUS_PLANT.getDefaultState(), 2);
                  this.placeGrownFlower(☃, ☃, ☃x);
               } else if (☃x < 4) {
                  int ☃xxxxxx = ☃.nextInt(4);
                  boolean ☃xxxxxxxx = false;
                  if (☃xxx) {
                     ☃xxxxxx++;
                  }

                  for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < ☃xxxxxx; ☃xxxxxxxxx++) {
                     EnumFacing ☃xxxxxxxxxx = EnumFacing.Plane.HORIZONTAL.random(☃);
                     BlockPos ☃xxxxxxxxxxx = ☃.offset(☃xxxxxxxxxx);
                     if (☃.isAirBlock(☃xxxxxxxxxxx) && ☃.isAirBlock(☃xxxxxxxxxxx.down()) && areAllNeighborsEmpty(☃, ☃xxxxxxxxxxx, ☃xxxxxxxxxx.getOpposite())) {
                        this.placeGrownFlower(☃, ☃xxxxxxxxxxx, ☃x + 1);
                        ☃xxxxxxxx = true;
                     }
                  }

                  if (☃xxxxxxxx) {
                     ☃.setBlockState(☃, Blocks.CHORUS_PLANT.getDefaultState(), 2);
                  } else {
                     this.placeDeadFlower(☃, ☃);
                  }
               } else if (☃x == 4) {
                  this.placeDeadFlower(☃, ☃);
               }
            }
         }
      }
   }

   private void placeGrownFlower(World var1, BlockPos var2, int var3) {
      ☃.setBlockState(☃, this.getDefaultState().withProperty(AGE, ☃), 2);
      ☃.playEvent(1033, ☃, 0);
   }

   private void placeDeadFlower(World var1, BlockPos var2) {
      ☃.setBlockState(☃, this.getDefaultState().withProperty(AGE, 5), 2);
      ☃.playEvent(1034, ☃, 0);
   }

   private static boolean areAllNeighborsEmpty(World var0, BlockPos var1, EnumFacing var2) {
      for (EnumFacing ☃ : EnumFacing.Plane.HORIZONTAL) {
         if (☃ != ☃ && !☃.isAirBlock(☃.offset(☃))) {
            return false;
         }
      }

      return true;
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
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return super.canPlaceBlockAt(☃, ☃) && this.canSurvive(☃, ☃);
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!this.canSurvive(☃, ☃)) {
         ☃.scheduleUpdate(☃, this, 1);
      }
   }

   public boolean canSurvive(World var1, BlockPos var2) {
      IBlockState ☃ = ☃.getBlockState(☃.down());
      Block ☃x = ☃.getBlock();
      if (☃x != Blocks.CHORUS_PLANT && ☃x != Blocks.END_STONE) {
         if (☃.getMaterial() == Material.AIR) {
            int ☃xx = 0;

            for (EnumFacing ☃xxx : EnumFacing.Plane.HORIZONTAL) {
               IBlockState ☃xxxx = ☃.getBlockState(☃.offset(☃xxx));
               Block ☃xxxxx = ☃xxxx.getBlock();
               if (☃xxxxx == Blocks.CHORUS_PLANT) {
                  ☃xx++;
               } else if (☃xxxx.getMaterial() != Material.AIR) {
                  return false;
               }
            }

            return ☃xx == 1;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   @Override
   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, @Nullable TileEntity var5, ItemStack var6) {
      super.harvestBlock(☃, ☃, ☃, ☃, ☃, ☃);
      spawnAsEntity(☃, ☃, new ItemStack(Item.getItemFromBlock(this)));
   }

   @Override
   protected ItemStack getSilkTouchDrop(IBlockState var1) {
      return ItemStack.EMPTY;
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
      return new BlockStateContainer(this, AGE);
   }

   public static void generatePlant(World var0, BlockPos var1, Random var2, int var3) {
      ☃.setBlockState(☃, Blocks.CHORUS_PLANT.getDefaultState(), 2);
      growTreeRecursive(☃, ☃, ☃, ☃, ☃, 0);
   }

   private static void growTreeRecursive(World var0, BlockPos var1, Random var2, BlockPos var3, int var4, int var5) {
      int ☃ = ☃.nextInt(4) + 1;
      if (☃ == 0) {
         ☃++;
      }

      for (int ☃x = 0; ☃x < ☃; ☃x++) {
         BlockPos ☃xx = ☃.up(☃x + 1);
         if (!areAllNeighborsEmpty(☃, ☃xx, null)) {
            return;
         }

         ☃.setBlockState(☃xx, Blocks.CHORUS_PLANT.getDefaultState(), 2);
      }

      boolean ☃x = false;
      if (☃ < 4) {
         int ☃xx = ☃.nextInt(4);
         if (☃ == 0) {
            ☃xx++;
         }

         for (int ☃xxx = 0; ☃xxx < ☃xx; ☃xxx++) {
            EnumFacing ☃xxxx = EnumFacing.Plane.HORIZONTAL.random(☃);
            BlockPos ☃xxxxx = ☃.up(☃).offset(☃xxxx);
            if (Math.abs(☃xxxxx.getX() - ☃.getX()) < ☃
               && Math.abs(☃xxxxx.getZ() - ☃.getZ()) < ☃
               && ☃.isAirBlock(☃xxxxx)
               && ☃.isAirBlock(☃xxxxx.down())
               && areAllNeighborsEmpty(☃, ☃xxxxx, ☃xxxx.getOpposite())) {
               ☃x = true;
               ☃.setBlockState(☃xxxxx, Blocks.CHORUS_PLANT.getDefaultState(), 2);
               growTreeRecursive(☃, ☃xxxxx, ☃, ☃, ☃, ☃ + 1);
            }
         }
      }

      if (!☃x) {
         ☃.setBlockState(☃.up(☃), Blocks.CHORUS_FLOWER.getDefaultState().withProperty(AGE, 5), 2);
      }
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
