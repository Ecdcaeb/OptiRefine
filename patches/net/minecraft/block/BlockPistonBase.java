package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonBase extends BlockDirectional {
   public static final PropertyBool EXTENDED = PropertyBool.create("extended");
   protected static final AxisAlignedBB PISTON_BASE_EAST_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.75, 1.0, 1.0);
   protected static final AxisAlignedBB PISTON_BASE_WEST_AABB = new AxisAlignedBB(0.25, 0.0, 0.0, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB PISTON_BASE_SOUTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.75);
   protected static final AxisAlignedBB PISTON_BASE_NORTH_AABB = new AxisAlignedBB(0.0, 0.0, 0.25, 1.0, 1.0, 1.0);
   protected static final AxisAlignedBB PISTON_BASE_UP_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
   protected static final AxisAlignedBB PISTON_BASE_DOWN_AABB = new AxisAlignedBB(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);
   private final boolean isSticky;

   public BlockPistonBase(boolean var1) {
      super(Material.PISTON);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EXTENDED, false));
      this.isSticky = ☃;
      this.setSoundType(SoundType.STONE);
      this.setHardness(0.5F);
      this.setCreativeTab(CreativeTabs.REDSTONE);
   }

   @Override
   public boolean causesSuffocation(IBlockState var1) {
      return !☃.getValue(EXTENDED);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      if (☃.getValue(EXTENDED)) {
         switch ((EnumFacing)☃.getValue(FACING)) {
            case DOWN:
               return PISTON_BASE_DOWN_AABB;
            case UP:
            default:
               return PISTON_BASE_UP_AABB;
            case NORTH:
               return PISTON_BASE_NORTH_AABB;
            case SOUTH:
               return PISTON_BASE_SOUTH_AABB;
            case WEST:
               return PISTON_BASE_WEST_AABB;
            case EAST:
               return PISTON_BASE_EAST_AABB;
         }
      } else {
         return FULL_BLOCK_AABB;
      }
   }

   @Override
   public boolean isTopSolid(IBlockState var1) {
      return !☃.getValue(EXTENDED) || ☃.getValue(FACING) == EnumFacing.DOWN;
   }

   @Override
   public void addCollisionBoxToList(
      IBlockState var1, World var2, BlockPos var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, @Nullable Entity var6, boolean var7
   ) {
      addCollisionBoxToList(☃, ☃, ☃, ☃.getBoundingBox(☃, ☃));
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      ☃.setBlockState(☃, ☃.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(☃, ☃)), 2);
      if (!☃.isRemote) {
         this.checkForMove(☃, ☃, ☃);
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.isRemote) {
         this.checkForMove(☃, ☃, ☃);
      }
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!☃.isRemote && ☃.getTileEntity(☃) == null) {
         this.checkForMove(☃, ☃, ☃);
      }
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(☃, ☃)).withProperty(EXTENDED, false);
   }

   private void checkForMove(World var1, BlockPos var2, IBlockState var3) {
      EnumFacing ☃ = ☃.getValue(FACING);
      boolean ☃x = this.shouldBeExtended(☃, ☃, ☃);
      if (☃x && !☃.getValue(EXTENDED)) {
         if (new BlockPistonStructureHelper(☃, ☃, ☃, true).canMove()) {
            ☃.addBlockEvent(☃, this, 0, ☃.getIndex());
         }
      } else if (!☃x && ☃.getValue(EXTENDED)) {
         ☃.addBlockEvent(☃, this, 1, ☃.getIndex());
      }
   }

   private boolean shouldBeExtended(World var1, BlockPos var2, EnumFacing var3) {
      for (EnumFacing ☃ : EnumFacing.values()) {
         if (☃ != ☃ && ☃.isSidePowered(☃.offset(☃), ☃)) {
            return true;
         }
      }

      if (☃.isSidePowered(☃, EnumFacing.DOWN)) {
         return true;
      } else {
         BlockPos ☃x = ☃.up();

         for (EnumFacing ☃xx : EnumFacing.values()) {
            if (☃xx != EnumFacing.DOWN && ☃.isSidePowered(☃x.offset(☃xx), ☃xx)) {
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public boolean eventReceived(IBlockState var1, World var2, BlockPos var3, int var4, int var5) {
      EnumFacing ☃ = ☃.getValue(FACING);
      if (!☃.isRemote) {
         boolean ☃x = this.shouldBeExtended(☃, ☃, ☃);
         if (☃x && ☃ == 1) {
            ☃.setBlockState(☃, ☃.withProperty(EXTENDED, true), 2);
            return false;
         }

         if (!☃x && ☃ == 0) {
            return false;
         }
      }

      if (☃ == 0) {
         if (!this.doMove(☃, ☃, ☃, true)) {
            return false;
         }

         ☃.setBlockState(☃, ☃.withProperty(EXTENDED, true), 3);
         ☃.playSound(null, ☃, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, ☃.rand.nextFloat() * 0.25F + 0.6F);
      } else if (☃ == 1) {
         TileEntity ☃xx = ☃.getTileEntity(☃.offset(☃));
         if (☃xx instanceof TileEntityPiston) {
            ((TileEntityPiston)☃xx).clearPistonTileEntity();
         }

         ☃.setBlockState(
            ☃,
            Blocks.PISTON_EXTENSION
               .getDefaultState()
               .withProperty(BlockPistonMoving.FACING, ☃)
               .withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT),
            3
         );
         ☃.setTileEntity(☃, BlockPistonMoving.createTilePiston(this.getStateFromMeta(☃), ☃, false, true));
         if (this.isSticky) {
            BlockPos ☃xxx = ☃.add(☃.getXOffset() * 2, ☃.getYOffset() * 2, ☃.getZOffset() * 2);
            IBlockState ☃xxxx = ☃.getBlockState(☃xxx);
            Block ☃xxxxx = ☃xxxx.getBlock();
            boolean ☃xxxxxx = false;
            if (☃xxxxx == Blocks.PISTON_EXTENSION) {
               TileEntity ☃xxxxxxx = ☃.getTileEntity(☃xxx);
               if (☃xxxxxxx instanceof TileEntityPiston) {
                  TileEntityPiston ☃xxxxxxxx = (TileEntityPiston)☃xxxxxxx;
                  if (☃xxxxxxxx.getFacing() == ☃ && ☃xxxxxxxx.isExtending()) {
                     ☃xxxxxxxx.clearPistonTileEntity();
                     ☃xxxxxx = true;
                  }
               }
            }

            if (!☃xxxxxx
               && ☃xxxx.getMaterial() != Material.AIR
               && canPush(☃xxxx, ☃, ☃xxx, ☃.getOpposite(), false, ☃)
               && (☃xxxx.getPushReaction() == EnumPushReaction.NORMAL || ☃xxxxx == Blocks.PISTON || ☃xxxxx == Blocks.STICKY_PISTON)) {
               this.doMove(☃, ☃, ☃, false);
            }
         } else {
            ☃.setBlockToAir(☃.offset(☃));
         }

         ☃.playSound(null, ☃, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, ☃.rand.nextFloat() * 0.15F + 0.6F);
      }

      return true;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Nullable
   public static EnumFacing getFacing(int var0) {
      int ☃ = ☃ & 7;
      return ☃ > 5 ? null : EnumFacing.byIndex(☃);
   }

   public static boolean canPush(IBlockState var0, World var1, BlockPos var2, EnumFacing var3, boolean var4, EnumFacing var5) {
      Block ☃ = ☃.getBlock();
      if (☃ == Blocks.OBSIDIAN) {
         return false;
      } else if (!☃.getWorldBorder().contains(☃)) {
         return false;
      } else if (☃.getY() >= 0 && (☃ != EnumFacing.DOWN || ☃.getY() != 0)) {
         if (☃.getY() <= ☃.getHeight() - 1 && (☃ != EnumFacing.UP || ☃.getY() != ☃.getHeight() - 1)) {
            if (☃ != Blocks.PISTON && ☃ != Blocks.STICKY_PISTON) {
               if (☃.getBlockHardness(☃, ☃) == -1.0F) {
                  return false;
               }

               switch (☃.getPushReaction()) {
                  case BLOCK:
                     return false;
                  case DESTROY:
                     return ☃;
                  case PUSH_ONLY:
                     return ☃ == ☃;
               }
            } else if (☃.getValue(EXTENDED)) {
               return false;
            }

            return !☃.hasTileEntity();
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean doMove(World var1, BlockPos var2, EnumFacing var3, boolean var4) {
      if (!☃) {
         ☃.setBlockToAir(☃.offset(☃));
      }

      BlockPistonStructureHelper ☃ = new BlockPistonStructureHelper(☃, ☃, ☃, ☃);
      if (!☃.canMove()) {
         return false;
      } else {
         List<BlockPos> ☃x = ☃.getBlocksToMove();
         List<IBlockState> ☃xx = Lists.newArrayList();

         for (int ☃xxx = 0; ☃xxx < ☃x.size(); ☃xxx++) {
            BlockPos ☃xxxx = ☃x.get(☃xxx);
            ☃xx.add(☃.getBlockState(☃xxxx).getActualState(☃, ☃xxxx));
         }

         List<BlockPos> ☃xxx = ☃.getBlocksToDestroy();
         int ☃xxxx = ☃x.size() + ☃xxx.size();
         IBlockState[] ☃xxxxx = new IBlockState[☃xxxx];
         EnumFacing ☃xxxxxx = ☃ ? ☃ : ☃.getOpposite();

         for (int ☃xxxxxxx = ☃xxx.size() - 1; ☃xxxxxxx >= 0; ☃xxxxxxx--) {
            BlockPos ☃xxxxxxxx = ☃xxx.get(☃xxxxxxx);
            IBlockState ☃xxxxxxxxx = ☃.getBlockState(☃xxxxxxxx);
            ☃xxxxxxxxx.getBlock().dropBlockAsItem(☃, ☃xxxxxxxx, ☃xxxxxxxxx, 0);
            ☃.setBlockState(☃xxxxxxxx, Blocks.AIR.getDefaultState(), 4);
            ☃xxxxx[--☃xxxx] = ☃xxxxxxxxx;
         }

         for (int ☃xxxxxxx = ☃x.size() - 1; ☃xxxxxxx >= 0; ☃xxxxxxx--) {
            BlockPos ☃xxxxxxxx = ☃x.get(☃xxxxxxx);
            IBlockState ☃xxxxxxxxx = ☃.getBlockState(☃xxxxxxxx);
            ☃.setBlockState(☃xxxxxxxx, Blocks.AIR.getDefaultState(), 2);
            ☃xxxxxxxx = ☃xxxxxxxx.offset(☃xxxxxx);
            ☃.setBlockState(☃xxxxxxxx, Blocks.PISTON_EXTENSION.getDefaultState().withProperty(FACING, ☃), 4);
            ☃.setTileEntity(☃xxxxxxxx, BlockPistonMoving.createTilePiston(☃xx.get(☃xxxxxxx), ☃, ☃, false));
            ☃xxxxx[--☃xxxx] = ☃xxxxxxxxx;
         }

         BlockPos ☃xxxxxxx = ☃.offset(☃);
         if (☃) {
            BlockPistonExtension.EnumPistonType ☃xxxxxxxx = this.isSticky
               ? BlockPistonExtension.EnumPistonType.STICKY
               : BlockPistonExtension.EnumPistonType.DEFAULT;
            IBlockState ☃xxxxxxxxx = Blocks.PISTON_HEAD
               .getDefaultState()
               .withProperty(BlockPistonExtension.FACING, ☃)
               .withProperty(BlockPistonExtension.TYPE, ☃xxxxxxxx);
            IBlockState ☃xxxxxxxxxx = Blocks.PISTON_EXTENSION
               .getDefaultState()
               .withProperty(BlockPistonMoving.FACING, ☃)
               .withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
            ☃.setBlockState(☃xxxxxxx, ☃xxxxxxxxxx, 4);
            ☃.setTileEntity(☃xxxxxxx, BlockPistonMoving.createTilePiston(☃xxxxxxxxx, ☃, true, true));
         }

         for (int ☃xxxxxxxx = ☃xxx.size() - 1; ☃xxxxxxxx >= 0; ☃xxxxxxxx--) {
            ☃.notifyNeighborsOfStateChange(☃xxx.get(☃xxxxxxxx), ☃xxxxx[☃xxxx++].getBlock(), false);
         }

         for (int ☃xxxxxxxx = ☃x.size() - 1; ☃xxxxxxxx >= 0; ☃xxxxxxxx--) {
            ☃.notifyNeighborsOfStateChange(☃x.get(☃xxxxxxxx), ☃xxxxx[☃xxxx++].getBlock(), false);
         }

         if (☃) {
            ☃.notifyNeighborsOfStateChange(☃xxxxxxx, Blocks.PISTON_HEAD, false);
         }

         return true;
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, getFacing(☃)).withProperty(EXTENDED, (☃ & 8) > 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getIndex();
      if (☃.getValue(EXTENDED)) {
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
      return new BlockStateContainer(this, FACING, EXTENDED);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      ☃ = this.getActualState(☃, ☃, ☃);
      return ☃.getValue(FACING) != ☃.getOpposite() && ☃.getValue(EXTENDED) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
   }
}
