package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEnchantmentTable extends BlockContainer {
   protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);

   protected BlockEnchantmentTable() {
      super(Material.ROCK, MapColor.RED);
      this.setLightOpacity(0);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return AABB;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      super.randomDisplayTick(☃, ☃, ☃, ☃);

      for (int ☃ = -2; ☃ <= 2; ☃++) {
         for (int ☃x = -2; ☃x <= 2; ☃x++) {
            if (☃ > -2 && ☃ < 2 && ☃x == -1) {
               ☃x = 2;
            }

            if (☃.nextInt(16) == 0) {
               for (int ☃xx = 0; ☃xx <= 1; ☃xx++) {
                  BlockPos ☃xxx = ☃.add(☃, ☃xx, ☃x);
                  if (☃.getBlockState(☃xxx).getBlock() == Blocks.BOOKSHELF) {
                     if (!☃.isAirBlock(☃.add(☃ / 2, 0, ☃x / 2))) {
                        break;
                     }

                     ☃.spawnParticle(
                        EnumParticleTypes.ENCHANTMENT_TABLE,
                        ☃.getX() + 0.5,
                        ☃.getY() + 2.0,
                        ☃.getZ() + 0.5,
                        ☃ + ☃.nextFloat() - 0.5,
                        ☃xx - ☃.nextFloat() - 1.0F,
                        ☃x + ☃.nextFloat() - 0.5
                     );
                  }
               }
            }
         }
      }
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.MODEL;
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityEnchantmentTable();
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (☃.isRemote) {
         return true;
      } else {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityEnchantmentTable) {
            ☃.displayGui((TileEntityEnchantmentTable)☃);
         }

         return true;
      }
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      super.onBlockPlacedBy(☃, ☃, ☃, ☃, ☃);
      if (☃.hasDisplayName()) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityEnchantmentTable) {
            ((TileEntityEnchantmentTable)☃).setCustomName(☃.getDisplayName());
         }
      }
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return ☃ == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }
}
