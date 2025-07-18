package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider {
   public static final PropertyBool POWERED = PropertyBool.create("powered");
   public static final PropertyEnum<BlockRedstoneComparator.Mode> MODE = PropertyEnum.create("mode", BlockRedstoneComparator.Mode.class);

   public BlockRedstoneComparator(boolean var1) {
      super(☃);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(POWERED, false)
            .withProperty(MODE, BlockRedstoneComparator.Mode.COMPARE)
      );
      this.hasTileEntity = true;
   }

   @Override
   public String getLocalizedName() {
      return I18n.translateToLocal("item.comparator.name");
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.COMPARATOR;
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Items.COMPARATOR);
   }

   @Override
   protected int getDelay(IBlockState var1) {
      return 2;
   }

   @Override
   protected IBlockState getPoweredState(IBlockState var1) {
      Boolean ☃ = ☃.getValue(POWERED);
      BlockRedstoneComparator.Mode ☃x = ☃.getValue(MODE);
      EnumFacing ☃xx = ☃.getValue(FACING);
      return Blocks.POWERED_COMPARATOR.getDefaultState().withProperty(FACING, ☃xx).withProperty(POWERED, ☃).withProperty(MODE, ☃x);
   }

   @Override
   protected IBlockState getUnpoweredState(IBlockState var1) {
      Boolean ☃ = ☃.getValue(POWERED);
      BlockRedstoneComparator.Mode ☃x = ☃.getValue(MODE);
      EnumFacing ☃xx = ☃.getValue(FACING);
      return Blocks.UNPOWERED_COMPARATOR.getDefaultState().withProperty(FACING, ☃xx).withProperty(POWERED, ☃).withProperty(MODE, ☃x);
   }

   @Override
   protected boolean isPowered(IBlockState var1) {
      return this.isRepeaterPowered || ☃.getValue(POWERED);
   }

   @Override
   protected int getActiveSignal(IBlockAccess var1, BlockPos var2, IBlockState var3) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      return ☃ instanceof TileEntityComparator ? ((TileEntityComparator)☃).getOutputSignal() : 0;
   }

   private int calculateOutput(World var1, BlockPos var2, IBlockState var3) {
      return ☃.getValue(MODE) == BlockRedstoneComparator.Mode.SUBTRACT
         ? Math.max(this.calculateInputStrength(☃, ☃, ☃) - this.getPowerOnSides(☃, ☃, ☃), 0)
         : this.calculateInputStrength(☃, ☃, ☃);
   }

   @Override
   protected boolean shouldBePowered(World var1, BlockPos var2, IBlockState var3) {
      int ☃ = this.calculateInputStrength(☃, ☃, ☃);
      if (☃ >= 15) {
         return true;
      } else if (☃ == 0) {
         return false;
      } else {
         int ☃x = this.getPowerOnSides(☃, ☃, ☃);
         return ☃x == 0 ? true : ☃ >= ☃x;
      }
   }

   @Override
   protected int calculateInputStrength(World var1, BlockPos var2, IBlockState var3) {
      int ☃ = super.calculateInputStrength(☃, ☃, ☃);
      EnumFacing ☃x = ☃.getValue(FACING);
      BlockPos ☃xx = ☃.offset(☃x);
      IBlockState ☃xxx = ☃.getBlockState(☃xx);
      if (☃xxx.hasComparatorInputOverride()) {
         ☃ = ☃xxx.getComparatorInputOverride(☃, ☃xx);
      } else if (☃ < 15 && ☃xxx.isNormalCube()) {
         ☃xx = ☃xx.offset(☃x);
         ☃xxx = ☃.getBlockState(☃xx);
         if (☃xxx.hasComparatorInputOverride()) {
            ☃ = ☃xxx.getComparatorInputOverride(☃, ☃xx);
         } else if (☃xxx.getMaterial() == Material.AIR) {
            EntityItemFrame ☃xxxx = this.findItemFrame(☃, ☃x, ☃xx);
            if (☃xxxx != null) {
               ☃ = ☃xxxx.getAnalogOutput();
            }
         }
      }

      return ☃;
   }

   @Nullable
   private EntityItemFrame findItemFrame(World var1, final EnumFacing var2, BlockPos var3) {
      List<EntityItemFrame> ☃ = ☃.getEntitiesWithinAABB(
         EntityItemFrame.class, new AxisAlignedBB(☃.getX(), ☃.getY(), ☃.getZ(), ☃.getX() + 1, ☃.getY() + 1, ☃.getZ() + 1), new Predicate<Entity>() {
            public boolean apply(@Nullable Entity var1) {
               return ☃ != null && ☃.getHorizontalFacing() == ☃;
            }
         }
      );
      return ☃.size() == 1 ? ☃.get(0) : null;
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (!☃.capabilities.allowEdit) {
         return false;
      } else {
         ☃ = ☃.cycleProperty(MODE);
         float ☃ = ☃.getValue(MODE) == BlockRedstoneComparator.Mode.SUBTRACT ? 0.55F : 0.5F;
         ☃.playSound(☃, ☃, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, ☃);
         ☃.setBlockState(☃, ☃, 2);
         this.onStateChange(☃, ☃, ☃);
         return true;
      }
   }

   @Override
   protected void updateState(World var1, BlockPos var2, IBlockState var3) {
      if (!☃.isBlockTickPending(☃, this)) {
         int ☃ = this.calculateOutput(☃, ☃, ☃);
         TileEntity ☃x = ☃.getTileEntity(☃);
         int ☃xx = ☃x instanceof TileEntityComparator ? ((TileEntityComparator)☃x).getOutputSignal() : 0;
         if (☃ != ☃xx || this.isPowered(☃) != this.shouldBePowered(☃, ☃, ☃)) {
            if (this.isFacingTowardsRepeater(☃, ☃, ☃)) {
               ☃.updateBlockTick(☃, this, 2, -1);
            } else {
               ☃.updateBlockTick(☃, this, 2, 0);
            }
         }
      }
   }

   private void onStateChange(World var1, BlockPos var2, IBlockState var3) {
      int ☃ = this.calculateOutput(☃, ☃, ☃);
      TileEntity ☃x = ☃.getTileEntity(☃);
      int ☃xx = 0;
      if (☃x instanceof TileEntityComparator) {
         TileEntityComparator ☃xxx = (TileEntityComparator)☃x;
         ☃xx = ☃xxx.getOutputSignal();
         ☃xxx.setOutputSignal(☃);
      }

      if (☃xx != ☃ || ☃.getValue(MODE) == BlockRedstoneComparator.Mode.COMPARE) {
         boolean ☃xxx = this.shouldBePowered(☃, ☃, ☃);
         boolean ☃xxxx = this.isPowered(☃);
         if (☃xxxx && !☃xxx) {
            ☃.setBlockState(☃, ☃.withProperty(POWERED, false), 2);
         } else if (!☃xxxx && ☃xxx) {
            ☃.setBlockState(☃, ☃.withProperty(POWERED, true), 2);
         }

         this.notifyNeighbors(☃, ☃, ☃);
      }
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (this.isRepeaterPowered) {
         ☃.setBlockState(☃, this.getUnpoweredState(☃).withProperty(POWERED, true), 4);
      }

      this.onStateChange(☃, ☃, ☃);
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      super.onBlockAdded(☃, ☃, ☃);
      ☃.setTileEntity(☃, this.createNewTileEntity(☃, 0));
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      super.breakBlock(☃, ☃, ☃);
      ☃.removeTileEntity(☃);
      this.notifyNeighbors(☃, ☃, ☃);
   }

   @Override
   public boolean eventReceived(IBlockState var1, World var2, BlockPos var3, int var4, int var5) {
      super.eventReceived(☃, ☃, ☃, ☃, ☃);
      TileEntity ☃ = ☃.getTileEntity(☃);
      return ☃ == null ? false : ☃.receiveClientEvent(☃, ☃);
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityComparator();
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState()
         .withProperty(FACING, EnumFacing.byHorizontalIndex(☃))
         .withProperty(POWERED, (☃ & 8) > 0)
         .withProperty(MODE, (☃ & 4) > 0 ? BlockRedstoneComparator.Mode.SUBTRACT : BlockRedstoneComparator.Mode.COMPARE);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getHorizontalIndex();
      if (☃.getValue(POWERED)) {
         ☃ |= 8;
      }

      if (☃.getValue(MODE) == BlockRedstoneComparator.Mode.SUBTRACT) {
         ☃ |= 4;
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
      return new BlockStateContainer(this, FACING, MODE, POWERED);
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState()
         .withProperty(FACING, ☃.getHorizontalFacing().getOpposite())
         .withProperty(POWERED, false)
         .withProperty(MODE, BlockRedstoneComparator.Mode.COMPARE);
   }

   public static enum Mode implements IStringSerializable {
      COMPARE("compare"),
      SUBTRACT("subtract");

      private final String name;

      private Mode(String var3) {
         this.name = ☃;
      }

      @Override
      public String toString() {
         return this.name;
      }

      @Override
      public String getName() {
         return this.name;
      }
   }
}
