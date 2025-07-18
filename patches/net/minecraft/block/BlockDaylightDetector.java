package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDaylightDetector extends BlockContainer {
   public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
   protected static final AxisAlignedBB DAYLIGHT_DETECTOR_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.375, 1.0);
   private final boolean inverted;

   public BlockDaylightDetector(boolean var1) {
      super(Material.WOOD);
      this.inverted = ☃;
      this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, 0));
      this.setCreativeTab(CreativeTabs.REDSTONE);
      this.setHardness(0.2F);
      this.setSoundType(SoundType.WOOD);
      this.setTranslationKey("daylightDetector");
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return DAYLIGHT_DETECTOR_AABB;
   }

   @Override
   public int getWeakPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return ☃.getValue(POWER);
   }

   public void updatePower(World var1, BlockPos var2) {
      if (☃.provider.hasSkyLight()) {
         IBlockState ☃ = ☃.getBlockState(☃);
         int ☃x = ☃.getLightFor(EnumSkyBlock.SKY, ☃) - ☃.getSkylightSubtracted();
         float ☃xx = ☃.getCelestialAngleRadians(1.0F);
         if (this.inverted) {
            ☃x = 15 - ☃x;
         }

         if (☃x > 0 && !this.inverted) {
            float ☃xxx = ☃xx < (float) Math.PI ? 0.0F : (float) (Math.PI * 2);
            ☃xx += (☃xxx - ☃xx) * 0.2F;
            ☃x = Math.round(☃x * MathHelper.cos(☃xx));
         }

         ☃x = MathHelper.clamp(☃x, 0, 15);
         if (☃.getValue(POWER) != ☃x) {
            ☃.setBlockState(☃, ☃.withProperty(POWER, ☃x), 3);
         }
      }
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (☃.isAllowEdit()) {
         if (☃.isRemote) {
            return true;
         } else {
            if (this.inverted) {
               ☃.setBlockState(☃, Blocks.DAYLIGHT_DETECTOR.getDefaultState().withProperty(POWER, ☃.getValue(POWER)), 4);
               Blocks.DAYLIGHT_DETECTOR.updatePower(☃, ☃);
            } else {
               ☃.setBlockState(☃, Blocks.DAYLIGHT_DETECTOR_INVERTED.getDefaultState().withProperty(POWER, ☃.getValue(POWER)), 4);
               Blocks.DAYLIGHT_DETECTOR_INVERTED.updatePower(☃, ☃);
            }

            return true;
         }
      } else {
         return super.onBlockActivated(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(Blocks.DAYLIGHT_DETECTOR);
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Blocks.DAYLIGHT_DETECTOR);
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
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.MODEL;
   }

   @Override
   public boolean canProvidePower(IBlockState var1) {
      return true;
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityDaylightDetector();
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(POWER, ☃);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(POWER);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, POWER);
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      if (!this.inverted) {
         super.getSubBlocks(☃, ☃);
      }
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return ☃ == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
   }
}
