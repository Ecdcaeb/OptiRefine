package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBed extends BlockHorizontal implements ITileEntityProvider {
   public static final PropertyEnum<BlockBed.EnumPartType> PART = PropertyEnum.create("part", BlockBed.EnumPartType.class);
   public static final PropertyBool OCCUPIED = PropertyBool.create("occupied");
   protected static final AxisAlignedBB BED_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5625, 1.0);

   public BlockBed() {
      super(Material.CLOTH);
      this.setDefaultState(this.blockState.getBaseState().withProperty(PART, BlockBed.EnumPartType.FOOT).withProperty(OCCUPIED, false));
      this.hasTileEntity = true;
   }

   @Override
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      if (☃.getValue(PART) == BlockBed.EnumPartType.FOOT) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityBed) {
            EnumDyeColor ☃x = ((TileEntityBed)☃).getColor();
            return MapColor.getBlockColor(☃x);
         }
      }

      return MapColor.CLOTH;
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (☃.isRemote) {
         return true;
      } else {
         if (☃.getValue(PART) != BlockBed.EnumPartType.HEAD) {
            ☃ = ☃.offset(☃.getValue(FACING));
            ☃ = ☃.getBlockState(☃);
            if (☃.getBlock() != this) {
               return true;
            }
         }

         if (☃.provider.canRespawnHere() && ☃.getBiome(☃) != Biomes.HELL) {
            if (☃.getValue(OCCUPIED)) {
               EntityPlayer ☃ = this.getPlayerInBed(☃, ☃);
               if (☃ != null) {
                  ☃.sendStatusMessage(new TextComponentTranslation("tile.bed.occupied"), true);
                  return true;
               }

               ☃ = ☃.withProperty(OCCUPIED, false);
               ☃.setBlockState(☃, ☃, 4);
            }

            EntityPlayer.SleepResult ☃ = ☃.trySleep(☃);
            if (☃ == EntityPlayer.SleepResult.OK) {
               ☃ = ☃.withProperty(OCCUPIED, true);
               ☃.setBlockState(☃, ☃, 4);
               return true;
            } else {
               if (☃ == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW) {
                  ☃.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep"), true);
               } else if (☃ == EntityPlayer.SleepResult.NOT_SAFE) {
                  ☃.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe"), true);
               } else if (☃ == EntityPlayer.SleepResult.TOO_FAR_AWAY) {
                  ☃.sendStatusMessage(new TextComponentTranslation("tile.bed.tooFarAway"), true);
               }

               return true;
            }
         } else {
            ☃.setBlockToAir(☃);
            BlockPos ☃ = ☃.offset(☃.getValue(FACING).getOpposite());
            if (☃.getBlockState(☃).getBlock() == this) {
               ☃.setBlockToAir(☃);
            }

            ☃.newExplosion(null, ☃.getX() + 0.5, ☃.getY() + 0.5, ☃.getZ() + 0.5, 5.0F, true, true);
            return true;
         }
      }
   }

   @Nullable
   private EntityPlayer getPlayerInBed(World var1, BlockPos var2) {
      for (EntityPlayer ☃ : ☃.playerEntities) {
         if (☃.isPlayerSleeping() && ☃.bedLocation.equals(☃)) {
            return ☃;
         }
      }

      return null;
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
   public void onFallenUpon(World var1, BlockPos var2, Entity var3, float var4) {
      super.onFallenUpon(☃, ☃, ☃, ☃ * 0.5F);
   }

   @Override
   public void onLanded(World var1, Entity var2) {
      if (☃.isSneaking()) {
         super.onLanded(☃, ☃);
      } else if (☃.motionY < 0.0) {
         ☃.motionY = -☃.motionY * 0.66F;
         if (!(☃ instanceof EntityLivingBase)) {
            ☃.motionY *= 0.8;
         }
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      EnumFacing ☃ = ☃.getValue(FACING);
      if (☃.getValue(PART) == BlockBed.EnumPartType.FOOT) {
         if (☃.getBlockState(☃.offset(☃)).getBlock() != this) {
            ☃.setBlockToAir(☃);
         }
      } else if (☃.getBlockState(☃.offset(☃.getOpposite())).getBlock() != this) {
         if (!☃.isRemote) {
            this.dropBlockAsItem(☃, ☃, ☃, 0);
         }

         ☃.setBlockToAir(☃);
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return ☃.getValue(PART) == BlockBed.EnumPartType.FOOT ? Items.AIR : Items.BED;
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return BED_AABB;
   }

   @Override
   public boolean hasCustomBreakingProgress(IBlockState var1) {
      return true;
   }

   @Nullable
   public static BlockPos getSafeExitLocation(World var0, BlockPos var1, int var2) {
      EnumFacing ☃ = ☃.getBlockState(☃).getValue(FACING);
      int ☃x = ☃.getX();
      int ☃xx = ☃.getY();
      int ☃xxx = ☃.getZ();

      for (int ☃xxxx = 0; ☃xxxx <= 1; ☃xxxx++) {
         int ☃xxxxx = ☃x - ☃.getXOffset() * ☃xxxx - 1;
         int ☃xxxxxx = ☃xxx - ☃.getZOffset() * ☃xxxx - 1;
         int ☃xxxxxxx = ☃xxxxx + 2;
         int ☃xxxxxxxx = ☃xxxxxx + 2;

         for (int ☃xxxxxxxxx = ☃xxxxx; ☃xxxxxxxxx <= ☃xxxxxxx; ☃xxxxxxxxx++) {
            for (int ☃xxxxxxxxxx = ☃xxxxxx; ☃xxxxxxxxxx <= ☃xxxxxxxx; ☃xxxxxxxxxx++) {
               BlockPos ☃xxxxxxxxxxx = new BlockPos(☃xxxxxxxxx, ☃xx, ☃xxxxxxxxxx);
               if (hasRoomForPlayer(☃, ☃xxxxxxxxxxx)) {
                  if (☃ <= 0) {
                     return ☃xxxxxxxxxxx;
                  }

                  ☃--;
               }
            }
         }
      }

      return null;
   }

   protected static boolean hasRoomForPlayer(World var0, BlockPos var1) {
      return ☃.getBlockState(☃.down()).isTopSolid() && !☃.getBlockState(☃).getMaterial().isSolid() && !☃.getBlockState(☃.up()).getMaterial().isSolid();
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      if (☃.getValue(PART) == BlockBed.EnumPartType.HEAD) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         EnumDyeColor ☃x = ☃ instanceof TileEntityBed ? ((TileEntityBed)☃).getColor() : EnumDyeColor.RED;
         spawnAsEntity(☃, ☃, new ItemStack(Items.BED, 1, ☃x.getMetadata()));
      }
   }

   @Override
   public EnumPushReaction getPushReaction(IBlockState var1) {
      return EnumPushReaction.DESTROY;
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      BlockPos ☃ = ☃;
      if (☃.getValue(PART) == BlockBed.EnumPartType.FOOT) {
         ☃ = ☃.offset(☃.getValue(FACING));
      }

      TileEntity ☃x = ☃.getTileEntity(☃);
      EnumDyeColor ☃xx = ☃x instanceof TileEntityBed ? ((TileEntityBed)☃x).getColor() : EnumDyeColor.RED;
      return new ItemStack(Items.BED, 1, ☃xx.getMetadata());
   }

   @Override
   public void onBlockHarvested(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4) {
      if (☃.capabilities.isCreativeMode && ☃.getValue(PART) == BlockBed.EnumPartType.FOOT) {
         BlockPos ☃ = ☃.offset(☃.getValue(FACING));
         if (☃.getBlockState(☃).getBlock() == this) {
            ☃.setBlockToAir(☃);
         }
      }
   }

   @Override
   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, TileEntity var5, ItemStack var6) {
      if (☃.getValue(PART) == BlockBed.EnumPartType.HEAD && ☃ instanceof TileEntityBed) {
         TileEntityBed ☃ = (TileEntityBed)☃;
         ItemStack ☃x = ☃.getItemStack();
         spawnAsEntity(☃, ☃, ☃x);
      } else {
         super.harvestBlock(☃, ☃, ☃, ☃, null, ☃);
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      super.breakBlock(☃, ☃, ☃);
      ☃.removeTileEntity(☃);
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      EnumFacing ☃ = EnumFacing.byHorizontalIndex(☃);
      return (☃ & 8) > 0
         ? this.getDefaultState().withProperty(PART, BlockBed.EnumPartType.HEAD).withProperty(FACING, ☃).withProperty(OCCUPIED, (☃ & 4) > 0)
         : this.getDefaultState().withProperty(PART, BlockBed.EnumPartType.FOOT).withProperty(FACING, ☃);
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      if (☃.getValue(PART) == BlockBed.EnumPartType.FOOT) {
         IBlockState ☃ = ☃.getBlockState(☃.offset(☃.getValue(FACING)));
         if (☃.getBlock() == this) {
            ☃ = ☃.withProperty(OCCUPIED, ☃.getValue(OCCUPIED));
         }
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
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getHorizontalIndex();
      if (☃.getValue(PART) == BlockBed.EnumPartType.HEAD) {
         ☃ |= 8;
         if (☃.getValue(OCCUPIED)) {
            ☃ |= 4;
         }
      }

      return ☃;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING, PART, OCCUPIED);
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityBed();
   }

   public static boolean isHeadPiece(int var0) {
      return (☃ & 8) != 0;
   }

   public static enum EnumPartType implements IStringSerializable {
      HEAD("head"),
      FOOT("foot");

      private final String name;

      private EnumPartType(String var3) {
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
