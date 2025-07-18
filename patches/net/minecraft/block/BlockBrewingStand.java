package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBrewingStand extends BlockContainer {
   public static final PropertyBool[] HAS_BOTTLE = new PropertyBool[]{
      PropertyBool.create("has_bottle_0"), PropertyBool.create("has_bottle_1"), PropertyBool.create("has_bottle_2")
   };
   protected static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
   protected static final AxisAlignedBB STICK_AABB = new AxisAlignedBB(0.4375, 0.0, 0.4375, 0.5625, 0.875, 0.5625);

   public BlockBrewingStand() {
      super(Material.IRON);
      this.setDefaultState(
         this.blockState.getBaseState().withProperty(HAS_BOTTLE[0], false).withProperty(HAS_BOTTLE[1], false).withProperty(HAS_BOTTLE[2], false)
      );
   }

   @Override
   public String getLocalizedName() {
      return I18n.translateToLocal("item.brewingStand.name");
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
      return new TileEntityBrewingStand();
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public void addCollisionBoxToList(
      IBlockState var1, World var2, BlockPos var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, @Nullable Entity var6, boolean var7
   ) {
      addCollisionBoxToList(☃, ☃, ☃, STICK_AABB);
      addCollisionBoxToList(☃, ☃, ☃, BASE_AABB);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return BASE_AABB;
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (☃.isRemote) {
         return true;
      } else {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityBrewingStand) {
            ☃.displayGUIChest((TileEntityBrewingStand)☃);
            ☃.addStat(StatList.BREWINGSTAND_INTERACTION);
         }

         return true;
      }
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      if (☃.hasDisplayName()) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityBrewingStand) {
            ((TileEntityBrewingStand)☃).setName(☃.getDisplayName());
         }
      }
   }

   @Override
   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
      double ☃ = ☃.getX() + 0.4F + ☃.nextFloat() * 0.2F;
      double ☃x = ☃.getY() + 0.7F + ☃.nextFloat() * 0.3F;
      double ☃xx = ☃.getZ() + 0.4F + ☃.nextFloat() * 0.2F;
      ☃.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, ☃, ☃x, ☃xx, 0.0, 0.0, 0.0);
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ instanceof TileEntityBrewingStand) {
         InventoryHelper.dropInventoryItems(☃, ☃, (TileEntityBrewingStand)☃);
      }

      super.breakBlock(☃, ☃, ☃);
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.BREWING_STAND;
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Items.BREWING_STAND);
   }

   @Override
   public boolean hasComparatorInputOverride(IBlockState var1) {
      return true;
   }

   @Override
   public int getComparatorInputOverride(IBlockState var1, World var2, BlockPos var3) {
      return Container.calcRedstone(☃.getTileEntity(☃));
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      IBlockState ☃ = this.getDefaultState();

      for (int ☃x = 0; ☃x < 3; ☃x++) {
         ☃ = ☃.withProperty(HAS_BOTTLE[☃x], (☃ & 1 << ☃x) > 0);
      }

      return ☃;
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;

      for (int ☃x = 0; ☃x < 3; ☃x++) {
         if (☃.getValue(HAS_BOTTLE[☃x])) {
            ☃ |= 1 << ☃x;
         }
      }

      return ☃;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, HAS_BOTTLE[0], HAS_BOTTLE[1], HAS_BOTTLE[2]);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
