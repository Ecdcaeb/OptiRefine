package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockNote extends BlockContainer {
   private static final List<SoundEvent> INSTRUMENTS = Lists.newArrayList(
      new SoundEvent[]{
         SoundEvents.BLOCK_NOTE_HARP,
         SoundEvents.BLOCK_NOTE_BASEDRUM,
         SoundEvents.BLOCK_NOTE_SNARE,
         SoundEvents.BLOCK_NOTE_HAT,
         SoundEvents.BLOCK_NOTE_BASS,
         SoundEvents.BLOCK_NOTE_FLUTE,
         SoundEvents.BLOCK_NOTE_BELL,
         SoundEvents.BLOCK_NOTE_GUITAR,
         SoundEvents.BLOCK_NOTE_CHIME,
         SoundEvents.BLOCK_NOTE_XYLOPHONE
      }
   );

   public BlockNote() {
      super(Material.WOOD);
      this.setCreativeTab(CreativeTabs.REDSTONE);
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      boolean ☃ = ☃.isBlockPowered(☃);
      TileEntity ☃x = ☃.getTileEntity(☃);
      if (☃x instanceof TileEntityNote) {
         TileEntityNote ☃xx = (TileEntityNote)☃x;
         if (☃xx.previousRedstoneState != ☃) {
            if (☃) {
               ☃xx.triggerNote(☃, ☃);
            }

            ☃xx.previousRedstoneState = ☃;
         }
      }
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (☃.isRemote) {
         return true;
      } else {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityNote) {
            TileEntityNote ☃x = (TileEntityNote)☃;
            ☃x.changePitch();
            ☃x.triggerNote(☃, ☃);
            ☃.addStat(StatList.NOTEBLOCK_TUNED);
         }

         return true;
      }
   }

   @Override
   public void onBlockClicked(World var1, BlockPos var2, EntityPlayer var3) {
      if (!☃.isRemote) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityNote) {
            ((TileEntityNote)☃).triggerNote(☃, ☃);
            ☃.addStat(StatList.NOTEBLOCK_PLAYED);
         }
      }
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityNote();
   }

   private SoundEvent getInstrument(int var1) {
      if (☃ < 0 || ☃ >= INSTRUMENTS.size()) {
         ☃ = 0;
      }

      return INSTRUMENTS.get(☃);
   }

   @Override
   public boolean eventReceived(IBlockState var1, World var2, BlockPos var3, int var4, int var5) {
      float ☃ = (float)Math.pow(2.0, (☃ - 12) / 12.0);
      ☃.playSound(null, ☃, this.getInstrument(☃), SoundCategory.RECORDS, 3.0F, ☃);
      ☃.spawnParticle(EnumParticleTypes.NOTE, ☃.getX() + 0.5, ☃.getY() + 1.2, ☃.getZ() + 0.5, ☃ / 24.0, 0.0, 0.0);
      return true;
   }

   @Override
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.MODEL;
   }
}
