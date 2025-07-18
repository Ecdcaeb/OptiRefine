package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneRepeater extends BlockRedstoneDiode {
   public static final PropertyBool LOCKED = PropertyBool.create("locked");
   public static final PropertyInteger DELAY = PropertyInteger.create("delay", 1, 4);

   protected BlockRedstoneRepeater(boolean var1) {
      super(☃);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DELAY, 1).withProperty(LOCKED, false));
   }

   @Override
   public String getLocalizedName() {
      return I18n.translateToLocal("item.diode.name");
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return ☃.withProperty(LOCKED, this.isLocked(☃, ☃, ☃));
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      return ☃.withProperty(FACING, ☃.rotate(☃.getValue(FACING)));
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      return ☃.withRotation(☃.toRotation(☃.getValue(FACING)));
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (!☃.capabilities.allowEdit) {
         return false;
      } else {
         ☃.setBlockState(☃, ☃.cycleProperty(DELAY), 3);
         return true;
      }
   }

   @Override
   protected int getDelay(IBlockState var1) {
      return ☃.getValue(DELAY) * 2;
   }

   @Override
   protected IBlockState getPoweredState(IBlockState var1) {
      Integer ☃ = ☃.getValue(DELAY);
      Boolean ☃x = ☃.getValue(LOCKED);
      EnumFacing ☃xx = ☃.getValue(FACING);
      return Blocks.POWERED_REPEATER.getDefaultState().withProperty(FACING, ☃xx).withProperty(DELAY, ☃).withProperty(LOCKED, ☃x);
   }

   @Override
   protected IBlockState getUnpoweredState(IBlockState var1) {
      Integer ☃ = ☃.getValue(DELAY);
      Boolean ☃x = ☃.getValue(LOCKED);
      EnumFacing ☃xx = ☃.getValue(FACING);
      return Blocks.UNPOWERED_REPEATER.getDefaultState().withProperty(FACING, ☃xx).withProperty(DELAY, ☃).withProperty(LOCKED, ☃x);
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.REPEATER;
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Items.REPEATER);
   }

   @Override
   public boolean isLocked(IBlockAccess var1, BlockPos var2, IBlockState var3) {
      return this.getPowerOnSides(☃, ☃, ☃) > 0;
   }

   @Override
   protected boolean isAlternateInput(IBlockState var1) {
      return isDiode(☃);
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      if (this.isRepeaterPowered) {
         EnumFacing ☃ = ☃.getValue(FACING);
         double ☃x = ☃.getX() + 0.5F + (☃.nextFloat() - 0.5F) * 0.2;
         double ☃xx = ☃.getY() + 0.4F + (☃.nextFloat() - 0.5F) * 0.2;
         double ☃xxx = ☃.getZ() + 0.5F + (☃.nextFloat() - 0.5F) * 0.2;
         float ☃xxxx = -5.0F;
         if (☃.nextBoolean()) {
            ☃xxxx = ☃.getValue(DELAY) * 2 - 1;
         }

         ☃xxxx /= 16.0F;
         double ☃xxxxx = ☃xxxx * ☃.getXOffset();
         double ☃xxxxxx = ☃xxxx * ☃.getZOffset();
         ☃.spawnParticle(EnumParticleTypes.REDSTONE, ☃x + ☃xxxxx, ☃xx, ☃xxx + ☃xxxxxx, 0.0, 0.0, 0.0);
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      super.breakBlock(☃, ☃, ☃);
      this.notifyNeighbors(☃, ☃, ☃);
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(☃)).withProperty(LOCKED, false).withProperty(DELAY, 1 + (☃ >> 2));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getHorizontalIndex();
      return ☃ | ☃.getValue(DELAY) - 1 << 2;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING, DELAY, LOCKED);
   }
}
