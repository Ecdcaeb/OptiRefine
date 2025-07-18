package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCauldron extends Block {
   public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
   protected static final AxisAlignedBB AABB_LEGS = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.3125, 1.0);
   protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.125);
   protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0, 0.0, 0.875, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875, 0.0, 0.0, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0, 0.0, 0.0, 0.125, 1.0, 1.0);

   public BlockCauldron() {
      super(Material.IRON, MapColor.STONE);
      this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0));
   }

   @Override
   public void addCollisionBoxToList(
      IBlockState var1, World var2, BlockPos var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, @Nullable Entity var6, boolean var7
   ) {
      addCollisionBoxToList(☃, ☃, ☃, AABB_LEGS);
      addCollisionBoxToList(☃, ☃, ☃, AABB_WALL_WEST);
      addCollisionBoxToList(☃, ☃, ☃, AABB_WALL_NORTH);
      addCollisionBoxToList(☃, ☃, ☃, AABB_WALL_EAST);
      addCollisionBoxToList(☃, ☃, ☃, AABB_WALL_SOUTH);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return FULL_BLOCK_AABB;
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
   public void onEntityCollision(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      int ☃ = ☃.getValue(LEVEL);
      float ☃x = ☃.getY() + (6.0F + 3 * ☃) / 16.0F;
      if (!☃.isRemote && ☃.isBurning() && ☃ > 0 && ☃.getEntityBoundingBox().minY <= ☃x) {
         ☃.extinguish();
         this.setWaterLevel(☃, ☃, ☃, ☃ - 1);
      }
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.isEmpty()) {
         return true;
      } else {
         int ☃x = ☃.getValue(LEVEL);
         Item ☃xx = ☃.getItem();
         if (☃xx == Items.WATER_BUCKET) {
            if (☃x < 3 && !☃.isRemote) {
               if (!☃.capabilities.isCreativeMode) {
                  ☃.setHeldItem(☃, new ItemStack(Items.BUCKET));
               }

               ☃.addStat(StatList.CAULDRON_FILLED);
               this.setWaterLevel(☃, ☃, ☃, 3);
               ☃.playSound(null, ☃, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            return true;
         } else if (☃xx == Items.BUCKET) {
            if (☃x == 3 && !☃.isRemote) {
               if (!☃.capabilities.isCreativeMode) {
                  ☃.shrink(1);
                  if (☃.isEmpty()) {
                     ☃.setHeldItem(☃, new ItemStack(Items.WATER_BUCKET));
                  } else if (!☃.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET))) {
                     ☃.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                  }
               }

               ☃.addStat(StatList.CAULDRON_USED);
               this.setWaterLevel(☃, ☃, ☃, 0);
               ☃.playSound(null, ☃, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            return true;
         } else if (☃xx == Items.GLASS_BOTTLE) {
            if (☃x > 0 && !☃.isRemote) {
               if (!☃.capabilities.isCreativeMode) {
                  ItemStack ☃xxx = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
                  ☃.addStat(StatList.CAULDRON_USED);
                  ☃.shrink(1);
                  if (☃.isEmpty()) {
                     ☃.setHeldItem(☃, ☃xxx);
                  } else if (!☃.inventory.addItemStackToInventory(☃xxx)) {
                     ☃.dropItem(☃xxx, false);
                  } else if (☃ instanceof EntityPlayerMP) {
                     ((EntityPlayerMP)☃).sendContainerToPlayer(☃.inventoryContainer);
                  }
               }

               ☃.playSound(null, ☃, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
               this.setWaterLevel(☃, ☃, ☃, ☃x - 1);
            }

            return true;
         } else if (☃xx == Items.POTIONITEM && PotionUtils.getPotionFromItem(☃) == PotionTypes.WATER) {
            if (☃x < 3 && !☃.isRemote) {
               if (!☃.capabilities.isCreativeMode) {
                  ItemStack ☃xxx = new ItemStack(Items.GLASS_BOTTLE);
                  ☃.addStat(StatList.CAULDRON_USED);
                  ☃.setHeldItem(☃, ☃xxx);
                  if (☃ instanceof EntityPlayerMP) {
                     ((EntityPlayerMP)☃).sendContainerToPlayer(☃.inventoryContainer);
                  }
               }

               ☃.playSound(null, ☃, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
               this.setWaterLevel(☃, ☃, ☃, ☃x + 1);
            }

            return true;
         } else {
            if (☃x > 0 && ☃xx instanceof ItemArmor) {
               ItemArmor ☃xxx = (ItemArmor)☃xx;
               if (☃xxx.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && ☃xxx.hasColor(☃) && !☃.isRemote) {
                  ☃xxx.removeColor(☃);
                  this.setWaterLevel(☃, ☃, ☃, ☃x - 1);
                  ☃.addStat(StatList.ARMOR_CLEANED);
                  return true;
               }
            }

            if (☃x > 0 && ☃xx instanceof ItemBanner) {
               if (TileEntityBanner.getPatterns(☃) > 0 && !☃.isRemote) {
                  ItemStack ☃xxx = ☃.copy();
                  ☃xxx.setCount(1);
                  TileEntityBanner.removeBannerData(☃xxx);
                  ☃.addStat(StatList.BANNER_CLEANED);
                  if (!☃.capabilities.isCreativeMode) {
                     ☃.shrink(1);
                     this.setWaterLevel(☃, ☃, ☃, ☃x - 1);
                  }

                  if (☃.isEmpty()) {
                     ☃.setHeldItem(☃, ☃xxx);
                  } else if (!☃.inventory.addItemStackToInventory(☃xxx)) {
                     ☃.dropItem(☃xxx, false);
                  } else if (☃ instanceof EntityPlayerMP) {
                     ((EntityPlayerMP)☃).sendContainerToPlayer(☃.inventoryContainer);
                  }
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   public void setWaterLevel(World var1, BlockPos var2, IBlockState var3, int var4) {
      ☃.setBlockState(☃, ☃.withProperty(LEVEL, MathHelper.clamp(☃, 0, 3)), 2);
      ☃.updateComparatorOutputLevel(☃, this);
   }

   @Override
   public void fillWithRain(World var1, BlockPos var2) {
      if (☃.rand.nextInt(20) == 1) {
         float ☃ = ☃.getBiome(☃).getTemperature(☃);
         if (!(☃.getBiomeProvider().getTemperatureAtHeight(☃, ☃.getY()) < 0.15F)) {
            IBlockState ☃x = ☃.getBlockState(☃);
            if (☃x.getValue(LEVEL) < 3) {
               ☃.setBlockState(☃, ☃x.cycleProperty(LEVEL), 2);
            }
         }
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.CAULDRON;
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Items.CAULDRON);
   }

   @Override
   public boolean hasComparatorInputOverride(IBlockState var1) {
      return true;
   }

   @Override
   public int getComparatorInputOverride(IBlockState var1, World var2, BlockPos var3) {
      return ☃.getValue(LEVEL);
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

   @Override
   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return true;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      if (☃ == EnumFacing.UP) {
         return BlockFaceShape.BOWL;
      } else {
         return ☃ == EnumFacing.DOWN ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
      }
   }
}
