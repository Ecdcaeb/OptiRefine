package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.world.World;

public class BlockDispenser extends BlockContainer {
   public static final PropertyDirection FACING = BlockDirectional.FACING;
   public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
   public static final RegistryDefaulted<Item, IBehaviorDispenseItem> DISPENSE_BEHAVIOR_REGISTRY = new RegistryDefaulted<>(new BehaviorDefaultDispenseItem());
   protected Random rand = new Random();

   protected BlockDispenser() {
      super(Material.ROCK);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TRIGGERED, false));
      this.setCreativeTab(CreativeTabs.REDSTONE);
   }

   @Override
   public int tickRate(World var1) {
      return 4;
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      super.onBlockAdded(☃, ☃, ☃);
      this.setDefaultDirection(☃, ☃, ☃);
   }

   private void setDefaultDirection(World var1, BlockPos var2, IBlockState var3) {
      if (!☃.isRemote) {
         EnumFacing ☃ = ☃.getValue(FACING);
         boolean ☃x = ☃.getBlockState(☃.north()).isFullBlock();
         boolean ☃xx = ☃.getBlockState(☃.south()).isFullBlock();
         if (☃ == EnumFacing.NORTH && ☃x && !☃xx) {
            ☃ = EnumFacing.SOUTH;
         } else if (☃ == EnumFacing.SOUTH && ☃xx && !☃x) {
            ☃ = EnumFacing.NORTH;
         } else {
            boolean ☃xxx = ☃.getBlockState(☃.west()).isFullBlock();
            boolean ☃xxxx = ☃.getBlockState(☃.east()).isFullBlock();
            if (☃ == EnumFacing.WEST && ☃xxx && !☃xxxx) {
               ☃ = EnumFacing.EAST;
            } else if (☃ == EnumFacing.EAST && ☃xxxx && !☃xxx) {
               ☃ = EnumFacing.WEST;
            }
         }

         ☃.setBlockState(☃, ☃.withProperty(FACING, ☃).withProperty(TRIGGERED, false), 2);
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
         if (☃ instanceof TileEntityDispenser) {
            ☃.displayGUIChest((TileEntityDispenser)☃);
            if (☃ instanceof TileEntityDropper) {
               ☃.addStat(StatList.DROPPER_INSPECTED);
            } else {
               ☃.addStat(StatList.DISPENSER_INSPECTED);
            }
         }

         return true;
      }
   }

   protected void dispense(World var1, BlockPos var2) {
      BlockSourceImpl ☃ = new BlockSourceImpl(☃, ☃);
      TileEntityDispenser ☃x = ☃.getBlockTileEntity();
      if (☃x != null) {
         int ☃xx = ☃x.getDispenseSlot();
         if (☃xx < 0) {
            ☃.playEvent(1001, ☃, 0);
         } else {
            ItemStack ☃xxx = ☃x.getStackInSlot(☃xx);
            IBehaviorDispenseItem ☃xxxx = this.getBehavior(☃xxx);
            if (☃xxxx != IBehaviorDispenseItem.DEFAULT_BEHAVIOR) {
               ☃x.setInventorySlotContents(☃xx, ☃xxxx.dispense(☃, ☃xxx));
            }
         }
      }
   }

   protected IBehaviorDispenseItem getBehavior(ItemStack var1) {
      return DISPENSE_BEHAVIOR_REGISTRY.getObject(☃.getItem());
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      boolean ☃ = ☃.isBlockPowered(☃) || ☃.isBlockPowered(☃.up());
      boolean ☃x = ☃.getValue(TRIGGERED);
      if (☃ && !☃x) {
         ☃.scheduleUpdate(☃, this, this.tickRate(☃));
         ☃.setBlockState(☃, ☃.withProperty(TRIGGERED, true), 4);
      } else if (!☃ && ☃x) {
         ☃.setBlockState(☃, ☃.withProperty(TRIGGERED, false), 4);
      }
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!☃.isRemote) {
         this.dispense(☃, ☃);
      }
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityDispenser();
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(☃, ☃)).withProperty(TRIGGERED, false);
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      ☃.setBlockState(☃, ☃.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(☃, ☃)), 2);
      if (☃.hasDisplayName()) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityDispenser) {
            ((TileEntityDispenser)☃).setCustomName(☃.getDisplayName());
         }
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ instanceof TileEntityDispenser) {
         InventoryHelper.dropInventoryItems(☃, ☃, (TileEntityDispenser)☃);
         ☃.updateComparatorOutputLevel(☃, this);
      }

      super.breakBlock(☃, ☃, ☃);
   }

   public static IPosition getDispensePosition(IBlockSource var0) {
      EnumFacing ☃ = ☃.getBlockState().getValue(FACING);
      double ☃x = ☃.getX() + 0.7 * ☃.getXOffset();
      double ☃xx = ☃.getY() + 0.7 * ☃.getYOffset();
      double ☃xxx = ☃.getZ() + 0.7 * ☃.getZOffset();
      return new PositionImpl(☃x, ☃xx, ☃xxx);
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
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.MODEL;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(☃ & 7)).withProperty(TRIGGERED, (☃ & 8) > 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getIndex();
      if (☃.getValue(TRIGGERED)) {
         ☃ |= 8;
      }

      return ☃;
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
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING, TRIGGERED);
   }
}
