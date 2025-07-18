package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class BlockBeacon extends BlockContainer {
   public BlockBeacon() {
      super(Material.GLASS, MapColor.DIAMOND);
      this.setHardness(3.0F);
      this.setCreativeTab(CreativeTabs.MISC);
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityBeacon();
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (☃.isRemote) {
         return true;
      } else {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityBeacon) {
            ☃.displayGUIChest((TileEntityBeacon)☃);
            ☃.addStat(StatList.BEACON_INTERACTION);
         }

         return true;
      }
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
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.MODEL;
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      super.onBlockPlacedBy(☃, ☃, ☃, ☃, ☃);
      if (☃.hasDisplayName()) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityBeacon) {
            ((TileEntityBeacon)☃).setName(☃.getDisplayName());
         }
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ instanceof TileEntityBeacon) {
         ((TileEntityBeacon)☃).updateBeacon();
         ☃.addBlockEvent(☃, this, 1, 0);
      }
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   public static void updateColorAsync(final World var0, final BlockPos var1) {
      HttpUtil.DOWNLOADER_EXECUTOR.submit(new Runnable() {
         @Override
         public void run() {
            Chunk ☃ = ☃.getChunk(☃);

            for (int ☃x = ☃.getY() - 1; ☃x >= 0; ☃x--) {
               final BlockPos ☃xx = new BlockPos(☃.getX(), ☃x, ☃.getZ());
               if (!☃.canSeeSky(☃xx)) {
                  break;
               }

               IBlockState ☃xxx = ☃.getBlockState(☃xx);
               if (☃xxx.getBlock() == Blocks.BEACON) {
                  ((WorldServer)☃).addScheduledTask(new Runnable() {
                     @Override
                     public void run() {
                        TileEntity ☃ = ☃.getTileEntity(☃);
                        if (☃ instanceof TileEntityBeacon) {
                           ((TileEntityBeacon)☃).updateBeacon();
                           ☃.addBlockEvent(☃, Blocks.BEACON, 1, 0);
                        }
                     }
                  });
               }
            }
         }
      });
   }
}
