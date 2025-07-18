package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockFlowerPot extends BlockContainer {
   public static final PropertyInteger LEGACY_DATA = PropertyInteger.create("legacy_data", 0, 15);
   public static final PropertyEnum<BlockFlowerPot.EnumFlowerType> CONTENTS = PropertyEnum.create("contents", BlockFlowerPot.EnumFlowerType.class);
   protected static final AxisAlignedBB FLOWER_POT_AABB = new AxisAlignedBB(0.3125, 0.0, 0.3125, 0.6875, 0.375, 0.6875);

   public BlockFlowerPot() {
      super(Material.CIRCUITS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(CONTENTS, BlockFlowerPot.EnumFlowerType.EMPTY).withProperty(LEGACY_DATA, 0));
   }

   @Override
   public String getLocalizedName() {
      return I18n.translateToLocal("item.flowerPot.name");
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return FLOWER_POT_AABB;
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
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      TileEntityFlowerPot ☃x = this.getTileEntity(☃, ☃);
      if (☃x == null) {
         return false;
      } else {
         ItemStack ☃xx = ☃x.getFlowerItemStack();
         if (☃xx.isEmpty()) {
            if (!this.canBePotted(☃)) {
               return false;
            }

            ☃x.setItemStack(☃);
            ☃.addStat(StatList.FLOWER_POTTED);
            if (!☃.capabilities.isCreativeMode) {
               ☃.shrink(1);
            }
         } else {
            if (☃.isEmpty()) {
               ☃.setHeldItem(☃, ☃xx);
            } else if (!☃.addItemStackToInventory(☃xx)) {
               ☃.dropItem(☃xx, false);
            }

            ☃x.setItemStack(ItemStack.EMPTY);
         }

         ☃x.markDirty();
         ☃.notifyBlockUpdate(☃, ☃, ☃, 3);
         return true;
      }
   }

   private boolean canBePotted(ItemStack var1) {
      Block ☃ = Block.getBlockFromItem(☃.getItem());
      if (☃ != Blocks.YELLOW_FLOWER
         && ☃ != Blocks.RED_FLOWER
         && ☃ != Blocks.CACTUS
         && ☃ != Blocks.BROWN_MUSHROOM
         && ☃ != Blocks.RED_MUSHROOM
         && ☃ != Blocks.SAPLING
         && ☃ != Blocks.DEADBUSH) {
         int ☃x = ☃.getMetadata();
         return ☃ == Blocks.TALLGRASS && ☃x == BlockTallGrass.EnumType.FERN.getMeta();
      } else {
         return true;
      }
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      TileEntityFlowerPot ☃ = this.getTileEntity(☃, ☃);
      if (☃ != null) {
         ItemStack ☃x = ☃.getFlowerItemStack();
         if (!☃x.isEmpty()) {
            return ☃x;
         }
      }

      return new ItemStack(Items.FLOWER_POT);
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return super.canPlaceBlockAt(☃, ☃) && ☃.getBlockState(☃.down()).isTopSolid();
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.getBlockState(☃.down()).isTopSolid()) {
         this.dropBlockAsItem(☃, ☃, ☃, 0);
         ☃.setBlockToAir(☃);
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      TileEntityFlowerPot ☃ = this.getTileEntity(☃, ☃);
      if (☃ != null && ☃.getFlowerPotItem() != null) {
         spawnAsEntity(☃, ☃, new ItemStack(☃.getFlowerPotItem(), 1, ☃.getFlowerPotData()));
      }

      super.breakBlock(☃, ☃, ☃);
   }

   @Override
   public void onBlockHarvested(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4) {
      super.onBlockHarvested(☃, ☃, ☃, ☃);
      if (☃.capabilities.isCreativeMode) {
         TileEntityFlowerPot ☃ = this.getTileEntity(☃, ☃);
         if (☃ != null) {
            ☃.setItemStack(ItemStack.EMPTY);
         }
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.FLOWER_POT;
   }

   @Nullable
   private TileEntityFlowerPot getTileEntity(World var1, BlockPos var2) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      return ☃ instanceof TileEntityFlowerPot ? (TileEntityFlowerPot)☃ : null;
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      Block ☃ = null;
      int ☃x = 0;
      switch (☃) {
         case 1:
            ☃ = Blocks.RED_FLOWER;
            ☃x = BlockFlower.EnumFlowerType.POPPY.getMeta();
            break;
         case 2:
            ☃ = Blocks.YELLOW_FLOWER;
            break;
         case 3:
            ☃ = Blocks.SAPLING;
            ☃x = BlockPlanks.EnumType.OAK.getMetadata();
            break;
         case 4:
            ☃ = Blocks.SAPLING;
            ☃x = BlockPlanks.EnumType.SPRUCE.getMetadata();
            break;
         case 5:
            ☃ = Blocks.SAPLING;
            ☃x = BlockPlanks.EnumType.BIRCH.getMetadata();
            break;
         case 6:
            ☃ = Blocks.SAPLING;
            ☃x = BlockPlanks.EnumType.JUNGLE.getMetadata();
            break;
         case 7:
            ☃ = Blocks.RED_MUSHROOM;
            break;
         case 8:
            ☃ = Blocks.BROWN_MUSHROOM;
            break;
         case 9:
            ☃ = Blocks.CACTUS;
            break;
         case 10:
            ☃ = Blocks.DEADBUSH;
            break;
         case 11:
            ☃ = Blocks.TALLGRASS;
            ☃x = BlockTallGrass.EnumType.FERN.getMeta();
            break;
         case 12:
            ☃ = Blocks.SAPLING;
            ☃x = BlockPlanks.EnumType.ACACIA.getMetadata();
            break;
         case 13:
            ☃ = Blocks.SAPLING;
            ☃x = BlockPlanks.EnumType.DARK_OAK.getMetadata();
      }

      return new TileEntityFlowerPot(Item.getItemFromBlock(☃), ☃x);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, CONTENTS, LEGACY_DATA);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(LEGACY_DATA);
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      BlockFlowerPot.EnumFlowerType ☃ = BlockFlowerPot.EnumFlowerType.EMPTY;
      TileEntity ☃x = ☃ instanceof ChunkCache ? ((ChunkCache)☃).getTileEntity(☃, Chunk.EnumCreateEntityType.CHECK) : ☃.getTileEntity(☃);
      if (☃x instanceof TileEntityFlowerPot) {
         TileEntityFlowerPot ☃xx = (TileEntityFlowerPot)☃x;
         Item ☃xxx = ☃xx.getFlowerPotItem();
         if (☃xxx instanceof ItemBlock) {
            int ☃xxxx = ☃xx.getFlowerPotData();
            Block ☃xxxxx = Block.getBlockFromItem(☃xxx);
            if (☃xxxxx == Blocks.SAPLING) {
               switch (BlockPlanks.EnumType.byMetadata(☃xxxx)) {
                  case OAK:
                     ☃ = BlockFlowerPot.EnumFlowerType.OAK_SAPLING;
                     break;
                  case SPRUCE:
                     ☃ = BlockFlowerPot.EnumFlowerType.SPRUCE_SAPLING;
                     break;
                  case BIRCH:
                     ☃ = BlockFlowerPot.EnumFlowerType.BIRCH_SAPLING;
                     break;
                  case JUNGLE:
                     ☃ = BlockFlowerPot.EnumFlowerType.JUNGLE_SAPLING;
                     break;
                  case ACACIA:
                     ☃ = BlockFlowerPot.EnumFlowerType.ACACIA_SAPLING;
                     break;
                  case DARK_OAK:
                     ☃ = BlockFlowerPot.EnumFlowerType.DARK_OAK_SAPLING;
                     break;
                  default:
                     ☃ = BlockFlowerPot.EnumFlowerType.EMPTY;
               }
            } else if (☃xxxxx == Blocks.TALLGRASS) {
               switch (☃xxxx) {
                  case 0:
                     ☃ = BlockFlowerPot.EnumFlowerType.DEAD_BUSH;
                     break;
                  case 2:
                     ☃ = BlockFlowerPot.EnumFlowerType.FERN;
                     break;
                  default:
                     ☃ = BlockFlowerPot.EnumFlowerType.EMPTY;
               }
            } else if (☃xxxxx == Blocks.YELLOW_FLOWER) {
               ☃ = BlockFlowerPot.EnumFlowerType.DANDELION;
            } else if (☃xxxxx == Blocks.RED_FLOWER) {
               switch (BlockFlower.EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, ☃xxxx)) {
                  case POPPY:
                     ☃ = BlockFlowerPot.EnumFlowerType.POPPY;
                     break;
                  case BLUE_ORCHID:
                     ☃ = BlockFlowerPot.EnumFlowerType.BLUE_ORCHID;
                     break;
                  case ALLIUM:
                     ☃ = BlockFlowerPot.EnumFlowerType.ALLIUM;
                     break;
                  case HOUSTONIA:
                     ☃ = BlockFlowerPot.EnumFlowerType.HOUSTONIA;
                     break;
                  case RED_TULIP:
                     ☃ = BlockFlowerPot.EnumFlowerType.RED_TULIP;
                     break;
                  case ORANGE_TULIP:
                     ☃ = BlockFlowerPot.EnumFlowerType.ORANGE_TULIP;
                     break;
                  case WHITE_TULIP:
                     ☃ = BlockFlowerPot.EnumFlowerType.WHITE_TULIP;
                     break;
                  case PINK_TULIP:
                     ☃ = BlockFlowerPot.EnumFlowerType.PINK_TULIP;
                     break;
                  case OXEYE_DAISY:
                     ☃ = BlockFlowerPot.EnumFlowerType.OXEYE_DAISY;
                     break;
                  default:
                     ☃ = BlockFlowerPot.EnumFlowerType.EMPTY;
               }
            } else if (☃xxxxx == Blocks.RED_MUSHROOM) {
               ☃ = BlockFlowerPot.EnumFlowerType.MUSHROOM_RED;
            } else if (☃xxxxx == Blocks.BROWN_MUSHROOM) {
               ☃ = BlockFlowerPot.EnumFlowerType.MUSHROOM_BROWN;
            } else if (☃xxxxx == Blocks.DEADBUSH) {
               ☃ = BlockFlowerPot.EnumFlowerType.DEAD_BUSH;
            } else if (☃xxxxx == Blocks.CACTUS) {
               ☃ = BlockFlowerPot.EnumFlowerType.CACTUS;
            }
         }
      }

      return ☃.withProperty(CONTENTS, ☃);
   }

   @Override
   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }

   public static enum EnumFlowerType implements IStringSerializable {
      EMPTY("empty"),
      POPPY("rose"),
      BLUE_ORCHID("blue_orchid"),
      ALLIUM("allium"),
      HOUSTONIA("houstonia"),
      RED_TULIP("red_tulip"),
      ORANGE_TULIP("orange_tulip"),
      WHITE_TULIP("white_tulip"),
      PINK_TULIP("pink_tulip"),
      OXEYE_DAISY("oxeye_daisy"),
      DANDELION("dandelion"),
      OAK_SAPLING("oak_sapling"),
      SPRUCE_SAPLING("spruce_sapling"),
      BIRCH_SAPLING("birch_sapling"),
      JUNGLE_SAPLING("jungle_sapling"),
      ACACIA_SAPLING("acacia_sapling"),
      DARK_OAK_SAPLING("dark_oak_sapling"),
      MUSHROOM_RED("mushroom_red"),
      MUSHROOM_BROWN("mushroom_brown"),
      DEAD_BUSH("dead_bush"),
      FERN("fern"),
      CACTUS("cactus");

      private final String name;

      private EnumFlowerType(String var3) {
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
