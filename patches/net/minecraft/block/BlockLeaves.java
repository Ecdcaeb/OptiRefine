package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLeaves extends Block {
   public static final PropertyBool DECAYABLE = PropertyBool.create("decayable");
   public static final PropertyBool CHECK_DECAY = PropertyBool.create("check_decay");
   protected boolean leavesFancy;
   int[] surroundings;

   public BlockLeaves() {
      super(Material.LEAVES);
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
      this.setHardness(0.2F);
      this.setLightOpacity(1);
      this.setSoundType(SoundType.PLANT);
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      int ☃ = 1;
      int ☃x = 2;
      int ☃xx = ☃.getX();
      int ☃xxx = ☃.getY();
      int ☃xxxx = ☃.getZ();
      if (☃.isAreaLoaded(new BlockPos(☃xx - 2, ☃xxx - 2, ☃xxxx - 2), new BlockPos(☃xx + 2, ☃xxx + 2, ☃xxxx + 2))) {
         for (int ☃xxxxx = -1; ☃xxxxx <= 1; ☃xxxxx++) {
            for (int ☃xxxxxx = -1; ☃xxxxxx <= 1; ☃xxxxxx++) {
               for (int ☃xxxxxxx = -1; ☃xxxxxxx <= 1; ☃xxxxxxx++) {
                  BlockPos ☃xxxxxxxx = ☃.add(☃xxxxx, ☃xxxxxx, ☃xxxxxxx);
                  IBlockState ☃xxxxxxxxx = ☃.getBlockState(☃xxxxxxxx);
                  if (☃xxxxxxxxx.getMaterial() == Material.LEAVES && !☃xxxxxxxxx.getValue(CHECK_DECAY)) {
                     ☃.setBlockState(☃xxxxxxxx, ☃xxxxxxxxx.withProperty(CHECK_DECAY, true), 4);
                  }
               }
            }
         }
      }
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!☃.isRemote) {
         if (☃.getValue(CHECK_DECAY) && ☃.getValue(DECAYABLE)) {
            int ☃ = 4;
            int ☃x = 5;
            int ☃xx = ☃.getX();
            int ☃xxx = ☃.getY();
            int ☃xxxx = ☃.getZ();
            int ☃xxxxx = 32;
            int ☃xxxxxx = 1024;
            int ☃xxxxxxx = 16;
            if (this.surroundings == null) {
               this.surroundings = new int[32768];
            }

            if (☃.isAreaLoaded(new BlockPos(☃xx - 5, ☃xxx - 5, ☃xxxx - 5), new BlockPos(☃xx + 5, ☃xxx + 5, ☃xxxx + 5))) {
               BlockPos.MutableBlockPos ☃xxxxxxxx = new BlockPos.MutableBlockPos();

               for (int ☃xxxxxxxxx = -4; ☃xxxxxxxxx <= 4; ☃xxxxxxxxx++) {
                  for (int ☃xxxxxxxxxx = -4; ☃xxxxxxxxxx <= 4; ☃xxxxxxxxxx++) {
                     for (int ☃xxxxxxxxxxx = -4; ☃xxxxxxxxxxx <= 4; ☃xxxxxxxxxxx++) {
                        IBlockState ☃xxxxxxxxxxxx = ☃.getBlockState(☃xxxxxxxx.setPos(☃xx + ☃xxxxxxxxx, ☃xxx + ☃xxxxxxxxxx, ☃xxxx + ☃xxxxxxxxxxx));
                        Block ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxx.getBlock();
                        if (☃xxxxxxxxxxxxx != Blocks.LOG && ☃xxxxxxxxxxxxx != Blocks.LOG2) {
                           if (☃xxxxxxxxxxxx.getMaterial() == Material.LEAVES) {
                              this.surroundings[(☃xxxxxxxxx + 16) * 1024 + (☃xxxxxxxxxx + 16) * 32 + ☃xxxxxxxxxxx + 16] = -2;
                           } else {
                              this.surroundings[(☃xxxxxxxxx + 16) * 1024 + (☃xxxxxxxxxx + 16) * 32 + ☃xxxxxxxxxxx + 16] = -1;
                           }
                        } else {
                           this.surroundings[(☃xxxxxxxxx + 16) * 1024 + (☃xxxxxxxxxx + 16) * 32 + ☃xxxxxxxxxxx + 16] = 0;
                        }
                     }
                  }
               }

               for (int ☃xxxxxxxxx = 1; ☃xxxxxxxxx <= 4; ☃xxxxxxxxx++) {
                  for (int ☃xxxxxxxxxx = -4; ☃xxxxxxxxxx <= 4; ☃xxxxxxxxxx++) {
                     for (int ☃xxxxxxxxxxxx = -4; ☃xxxxxxxxxxxx <= 4; ☃xxxxxxxxxxxx++) {
                        for (int ☃xxxxxxxxxxxxx = -4; ☃xxxxxxxxxxxxx <= 4; ☃xxxxxxxxxxxxx++) {
                           if (this.surroundings[(☃xxxxxxxxxx + 16) * 1024 + (☃xxxxxxxxxxxx + 16) * 32 + ☃xxxxxxxxxxxxx + 16] == ☃xxxxxxxxx - 1) {
                              if (this.surroundings[(☃xxxxxxxxxx + 16 - 1) * 1024 + (☃xxxxxxxxxxxx + 16) * 32 + ☃xxxxxxxxxxxxx + 16] == -2) {
                                 this.surroundings[(☃xxxxxxxxxx + 16 - 1) * 1024 + (☃xxxxxxxxxxxx + 16) * 32 + ☃xxxxxxxxxxxxx + 16] = ☃xxxxxxxxx;
                              }

                              if (this.surroundings[(☃xxxxxxxxxx + 16 + 1) * 1024 + (☃xxxxxxxxxxxx + 16) * 32 + ☃xxxxxxxxxxxxx + 16] == -2) {
                                 this.surroundings[(☃xxxxxxxxxx + 16 + 1) * 1024 + (☃xxxxxxxxxxxx + 16) * 32 + ☃xxxxxxxxxxxxx + 16] = ☃xxxxxxxxx;
                              }

                              if (this.surroundings[(☃xxxxxxxxxx + 16) * 1024 + (☃xxxxxxxxxxxx + 16 - 1) * 32 + ☃xxxxxxxxxxxxx + 16] == -2) {
                                 this.surroundings[(☃xxxxxxxxxx + 16) * 1024 + (☃xxxxxxxxxxxx + 16 - 1) * 32 + ☃xxxxxxxxxxxxx + 16] = ☃xxxxxxxxx;
                              }

                              if (this.surroundings[(☃xxxxxxxxxx + 16) * 1024 + (☃xxxxxxxxxxxx + 16 + 1) * 32 + ☃xxxxxxxxxxxxx + 16] == -2) {
                                 this.surroundings[(☃xxxxxxxxxx + 16) * 1024 + (☃xxxxxxxxxxxx + 16 + 1) * 32 + ☃xxxxxxxxxxxxx + 16] = ☃xxxxxxxxx;
                              }

                              if (this.surroundings[(☃xxxxxxxxxx + 16) * 1024 + (☃xxxxxxxxxxxx + 16) * 32 + (☃xxxxxxxxxxxxx + 16 - 1)] == -2) {
                                 this.surroundings[(☃xxxxxxxxxx + 16) * 1024 + (☃xxxxxxxxxxxx + 16) * 32 + (☃xxxxxxxxxxxxx + 16 - 1)] = ☃xxxxxxxxx;
                              }

                              if (this.surroundings[(☃xxxxxxxxxx + 16) * 1024 + (☃xxxxxxxxxxxx + 16) * 32 + ☃xxxxxxxxxxxxx + 16 + 1] == -2) {
                                 this.surroundings[(☃xxxxxxxxxx + 16) * 1024 + (☃xxxxxxxxxxxx + 16) * 32 + ☃xxxxxxxxxxxxx + 16 + 1] = ☃xxxxxxxxx;
                              }
                           }
                        }
                     }
                  }
               }
            }

            int ☃xxxxxxxx = this.surroundings[16912];
            if (☃xxxxxxxx >= 0) {
               ☃.setBlockState(☃, ☃.withProperty(CHECK_DECAY, false), 4);
            } else {
               this.destroy(☃, ☃);
            }
         }
      }
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      if (☃.isRainingAt(☃.up()) && !☃.getBlockState(☃.down()).isTopSolid() && ☃.nextInt(15) == 1) {
         double ☃ = ☃.getX() + ☃.nextFloat();
         double ☃x = ☃.getY() - 0.05;
         double ☃xx = ☃.getZ() + ☃.nextFloat();
         ☃.spawnParticle(EnumParticleTypes.DRIP_WATER, ☃, ☃x, ☃xx, 0.0, 0.0, 0.0);
      }
   }

   private void destroy(World var1, BlockPos var2) {
      this.dropBlockAsItem(☃, ☃, ☃.getBlockState(☃), 0);
      ☃.setBlockToAir(☃);
   }

   @Override
   public int quantityDropped(Random var1) {
      return ☃.nextInt(20) == 0 ? 1 : 0;
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.SAPLING);
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      if (!☃.isRemote) {
         int ☃ = this.getSaplingDropChance(☃);
         if (☃ > 0) {
            ☃ -= 2 << ☃;
            if (☃ < 10) {
               ☃ = 10;
            }
         }

         if (☃.rand.nextInt(☃) == 0) {
            Item ☃x = this.getItemDropped(☃, ☃.rand, ☃);
            spawnAsEntity(☃, ☃, new ItemStack(☃x, 1, this.damageDropped(☃)));
         }

         ☃ = 200;
         if (☃ > 0) {
            ☃ -= 10 << ☃;
            if (☃ < 40) {
               ☃ = 40;
            }
         }

         this.dropApple(☃, ☃, ☃, ☃);
      }
   }

   protected void dropApple(World var1, BlockPos var2, IBlockState var3, int var4) {
   }

   protected int getSaplingDropChance(IBlockState var1) {
      return 20;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return !this.leavesFancy;
   }

   public void setGraphicsLevel(boolean var1) {
      this.leavesFancy = ☃;
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return this.leavesFancy ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
   }

   @Override
   public boolean causesSuffocation(IBlockState var1) {
      return false;
   }

   public abstract BlockPlanks.EnumType getWoodType(int var1);

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return !this.leavesFancy && ☃.getBlockState(☃.offset(☃)).getBlock() == this ? false : super.shouldSideBeRendered(☃, ☃, ☃, ☃);
   }
}
