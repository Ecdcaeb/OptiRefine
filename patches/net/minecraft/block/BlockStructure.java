package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStructure extends BlockContainer {
   public static final PropertyEnum<TileEntityStructure.Mode> MODE = PropertyEnum.create("mode", TileEntityStructure.Mode.class);

   public BlockStructure() {
      super(Material.IRON, MapColor.SILVER);
      this.setDefaultState(this.blockState.getBaseState());
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityStructure();
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      return ☃ instanceof TileEntityStructure ? ((TileEntityStructure)☃).usedBy(☃) : false;
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      if (!☃.isRemote) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityStructure) {
            TileEntityStructure ☃x = (TileEntityStructure)☃;
            ☃x.createdBy(☃);
         }
      }
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.MODEL;
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(MODE, TileEntityStructure.Mode.DATA);
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(MODE, TileEntityStructure.Mode.getById(☃));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(MODE).getModeId();
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, MODE);
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.isRemote) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityStructure) {
            TileEntityStructure ☃x = (TileEntityStructure)☃;
            boolean ☃xx = ☃.isBlockPowered(☃);
            boolean ☃xxx = ☃x.isPowered();
            if (☃xx && !☃xxx) {
               ☃x.setPowered(true);
               this.trigger(☃x);
            } else if (!☃xx && ☃xxx) {
               ☃x.setPowered(false);
            }
         }
      }
   }

   private void trigger(TileEntityStructure var1) {
      switch (☃.getMode()) {
         case SAVE:
            ☃.save(false);
            break;
         case LOAD:
            ☃.load(false);
            break;
         case CORNER:
            ☃.unloadStructure();
         case DATA:
      }
   }
}
