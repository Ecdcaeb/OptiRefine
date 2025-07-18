package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonMoving extends BlockContainer {
   public static final PropertyDirection FACING = BlockPistonExtension.FACING;
   public static final PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE = BlockPistonExtension.TYPE;

   public BlockPistonMoving() {
      super(Material.PISTON);
      this.setDefaultState(
         this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, BlockPistonExtension.EnumPistonType.DEFAULT)
      );
      this.setHardness(-1.0F);
   }

   @Nullable
   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return null;
   }

   public static TileEntity createTilePiston(IBlockState var0, EnumFacing var1, boolean var2, boolean var3) {
      return new TileEntityPiston(☃, ☃, ☃, ☃);
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ instanceof TileEntityPiston) {
         ((TileEntityPiston)☃).clearPistonTileEntity();
      } else {
         super.breakBlock(☃, ☃, ☃);
      }
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return false;
   }

   @Override
   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
      return false;
   }

   @Override
   public void onPlayerDestroy(World var1, BlockPos var2, IBlockState var3) {
      BlockPos ☃ = ☃.offset(☃.getValue(FACING).getOpposite());
      IBlockState ☃x = ☃.getBlockState(☃);
      if (☃x.getBlock() instanceof BlockPistonBase && ☃x.getValue(BlockPistonBase.EXTENDED)) {
         ☃.setBlockToAir(☃);
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
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (!☃.isRemote && ☃.getTileEntity(☃) == null) {
         ☃.setBlockToAir(☃);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.AIR;
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      if (!☃.isRemote) {
         TileEntityPiston ☃ = this.getTilePistonAt(☃, ☃);
         if (☃ != null) {
            IBlockState ☃x = ☃.getPistonState();
            ☃x.getBlock().dropBlockAsItem(☃, ☃, ☃x, 0);
         }
      }
   }

   @Nullable
   @Override
   public RayTraceResult collisionRayTrace(IBlockState var1, World var2, BlockPos var3, Vec3d var4, Vec3d var5) {
      return null;
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.isRemote) {
         ☃.getTileEntity(☃);
      }
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      TileEntityPiston ☃ = this.getTilePistonAt(☃, ☃);
      return ☃ == null ? null : ☃.getAABB(☃, ☃);
   }

   @Override
   public void addCollisionBoxToList(
      IBlockState var1, World var2, BlockPos var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, @Nullable Entity var6, boolean var7
   ) {
      TileEntityPiston ☃ = this.getTilePistonAt(☃, ☃);
      if (☃ != null) {
         ☃.addCollissionAABBs(☃, ☃, ☃, ☃, ☃);
      }
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      TileEntityPiston ☃ = this.getTilePistonAt(☃, ☃);
      return ☃ != null ? ☃.getAABB(☃, ☃) : FULL_BLOCK_AABB;
   }

   @Nullable
   private TileEntityPiston getTilePistonAt(IBlockAccess var1, BlockPos var2) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      return ☃ instanceof TileEntityPiston ? (TileEntityPiston)☃ : null;
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return ItemStack.EMPTY;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState()
         .withProperty(FACING, BlockPistonExtension.getFacing(☃))
         .withProperty(TYPE, (☃ & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
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
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getIndex();
      if (☃.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
         ☃ |= 8;
      }

      return ☃;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING, TYPE);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
