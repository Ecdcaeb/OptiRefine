package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockAnvil extends BlockFalling {
   public static final PropertyDirection FACING = BlockHorizontal.FACING;
   public static final PropertyInteger DAMAGE = PropertyInteger.create("damage", 0, 2);
   protected static final AxisAlignedBB X_AXIS_AABB = new AxisAlignedBB(0.0, 0.0, 0.125, 1.0, 1.0, 0.875);
   protected static final AxisAlignedBB Z_AXIS_AABB = new AxisAlignedBB(0.125, 0.0, 0.0, 0.875, 1.0, 1.0);
   protected static final Logger LOGGER = LogManager.getLogger();

   protected BlockAnvil() {
      super(Material.ANVIL);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DAMAGE, 0));
      this.setLightOpacity(0);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      EnumFacing ☃ = ☃.getHorizontalFacing().rotateY();

      try {
         return super.getStateForPlacement(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃).withProperty(FACING, ☃).withProperty(DAMAGE, ☃ >> 2);
      } catch (IllegalArgumentException var11) {
         if (!☃.isRemote) {
            LOGGER.warn(String.format("Invalid damage property for anvil at %s. Found %d, must be in [0, 1, 2]", ☃, ☃ >> 2));
            if (☃ instanceof EntityPlayer) {
               ☃.sendMessage(new TextComponentTranslation("Invalid damage property. Please pick in [0, 1, 2]"));
            }
         }

         return super.getStateForPlacement(☃, ☃, ☃, ☃, ☃, ☃, 0, ☃).withProperty(FACING, ☃).withProperty(DAMAGE, 0);
      }
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (!☃.isRemote) {
         ☃.displayGui(new BlockAnvil.Anvil(☃, ☃));
      }

      return true;
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(DAMAGE);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      EnumFacing ☃ = ☃.getValue(FACING);
      return ☃.getAxis() == EnumFacing.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      ☃.add(new ItemStack(this));
      ☃.add(new ItemStack(this, 1, 1));
      ☃.add(new ItemStack(this, 1, 2));
   }

   @Override
   protected void onStartFalling(EntityFallingBlock var1) {
      ☃.setHurtEntities(true);
   }

   @Override
   public void onEndFalling(World var1, BlockPos var2, IBlockState var3, IBlockState var4) {
      ☃.playEvent(1031, ☃, 0);
   }

   @Override
   public void onBroken(World var1, BlockPos var2) {
      ☃.playEvent(1029, ☃, 0);
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return true;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(☃ & 3)).withProperty(DAMAGE, (☃ & 15) >> 2);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getHorizontalIndex();
      return ☃ | ☃.getValue(DAMAGE) << 2;
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      return ☃.getBlock() != this ? ☃ : ☃.withProperty(FACING, ☃.rotate(☃.getValue(FACING)));
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING, DAMAGE);
   }

   public static class Anvil implements IInteractionObject {
      private final World world;
      private final BlockPos position;

      public Anvil(World var1, BlockPos var2) {
         this.world = ☃;
         this.position = ☃;
      }

      @Override
      public String getName() {
         return "anvil";
      }

      @Override
      public boolean hasCustomName() {
         return false;
      }

      @Override
      public ITextComponent getDisplayName() {
         return new TextComponentTranslation(Blocks.ANVIL.getTranslationKey() + ".name");
      }

      @Override
      public Container createContainer(InventoryPlayer var1, EntityPlayer var2) {
         return new ContainerRepair(☃, this.world, this.position, ☃);
      }

      @Override
      public String getGuiID() {
         return "minecraft:anvil";
      }
   }
}
