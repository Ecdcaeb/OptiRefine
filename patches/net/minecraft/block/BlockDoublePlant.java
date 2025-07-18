package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoublePlant extends BlockBush implements IGrowable {
   public static final PropertyEnum<BlockDoublePlant.EnumPlantType> VARIANT = PropertyEnum.create("variant", BlockDoublePlant.EnumPlantType.class);
   public static final PropertyEnum<BlockDoublePlant.EnumBlockHalf> HALF = PropertyEnum.create("half", BlockDoublePlant.EnumBlockHalf.class);
   public static final PropertyEnum<EnumFacing> FACING = BlockHorizontal.FACING;

   public BlockDoublePlant() {
      super(Material.VINE);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(VARIANT, BlockDoublePlant.EnumPlantType.SUNFLOWER)
            .withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER)
            .withProperty(FACING, EnumFacing.NORTH)
      );
      this.setHardness(0.0F);
      this.setSoundType(SoundType.PLANT);
      this.setTranslationKey("doublePlant");
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return FULL_BLOCK_AABB;
   }

   private BlockDoublePlant.EnumPlantType getType(IBlockAccess var1, BlockPos var2, IBlockState var3) {
      if (☃.getBlock() == this) {
         ☃ = ☃.getActualState(☃, ☃);
         return ☃.getValue(VARIANT);
      } else {
         return BlockDoublePlant.EnumPlantType.FERN;
      }
   }

   @Override
   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return super.canPlaceBlockAt(☃, ☃) && ☃.isAirBlock(☃.up());
   }

   @Override
   public boolean isReplaceable(IBlockAccess var1, BlockPos var2) {
      IBlockState ☃ = ☃.getBlockState(☃);
      if (☃.getBlock() != this) {
         return true;
      } else {
         BlockDoublePlant.EnumPlantType ☃x = ☃.getActualState(☃, ☃).getValue(VARIANT);
         return ☃x == BlockDoublePlant.EnumPlantType.FERN || ☃x == BlockDoublePlant.EnumPlantType.GRASS;
      }
   }

   @Override
   protected void checkAndDropBlock(World var1, BlockPos var2, IBlockState var3) {
      if (!this.canBlockStay(☃, ☃, ☃)) {
         boolean ☃ = ☃.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER;
         BlockPos ☃x = ☃ ? ☃ : ☃.up();
         BlockPos ☃xx = ☃ ? ☃.down() : ☃;
         Block ☃xxx = (Block)(☃ ? this : ☃.getBlockState(☃x).getBlock());
         Block ☃xxxx = (Block)(☃ ? ☃.getBlockState(☃xx).getBlock() : this);
         if (☃xxx == this) {
            ☃.setBlockState(☃x, Blocks.AIR.getDefaultState(), 2);
         }

         if (☃xxxx == this) {
            ☃.setBlockState(☃xx, Blocks.AIR.getDefaultState(), 3);
            if (!☃) {
               this.dropBlockAsItem(☃, ☃xx, ☃, 0);
            }
         }
      }
   }

   @Override
   public boolean canBlockStay(World var1, BlockPos var2, IBlockState var3) {
      if (☃.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
         return ☃.getBlockState(☃.down()).getBlock() == this;
      } else {
         IBlockState ☃ = ☃.getBlockState(☃.up());
         return ☃.getBlock() == this && super.canBlockStay(☃, ☃, ☃);
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      if (☃.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
         return Items.AIR;
      } else {
         BlockDoublePlant.EnumPlantType ☃ = ☃.getValue(VARIANT);
         if (☃ == BlockDoublePlant.EnumPlantType.FERN) {
            return Items.AIR;
         } else if (☃ == BlockDoublePlant.EnumPlantType.GRASS) {
            return ☃.nextInt(8) == 0 ? Items.WHEAT_SEEDS : Items.AIR;
         } else {
            return super.getItemDropped(☃, ☃, ☃);
         }
      }
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(HALF) != BlockDoublePlant.EnumBlockHalf.UPPER && ☃.getValue(VARIANT) != BlockDoublePlant.EnumPlantType.GRASS
         ? ☃.getValue(VARIANT).getMeta()
         : 0;
   }

   public void placeAt(World var1, BlockPos var2, BlockDoublePlant.EnumPlantType var3, int var4) {
      ☃.setBlockState(☃, this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER).withProperty(VARIANT, ☃), ☃);
      ☃.setBlockState(☃.up(), this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), ☃);
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      ☃.setBlockState(☃.up(), this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
   }

   @Override
   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, @Nullable TileEntity var5, ItemStack var6) {
      if (☃.isRemote || ☃.getItem() != Items.SHEARS || ☃.getValue(HALF) != BlockDoublePlant.EnumBlockHalf.LOWER || !this.onHarvest(☃, ☃, ☃, ☃)) {
         super.harvestBlock(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   @Override
   public void onBlockHarvested(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4) {
      if (☃.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
         if (☃.getBlockState(☃.down()).getBlock() == this) {
            if (☃.capabilities.isCreativeMode) {
               ☃.setBlockToAir(☃.down());
            } else {
               IBlockState ☃ = ☃.getBlockState(☃.down());
               BlockDoublePlant.EnumPlantType ☃x = ☃.getValue(VARIANT);
               if (☃x != BlockDoublePlant.EnumPlantType.FERN && ☃x != BlockDoublePlant.EnumPlantType.GRASS) {
                  ☃.destroyBlock(☃.down(), true);
               } else if (☃.isRemote) {
                  ☃.setBlockToAir(☃.down());
               } else if (!☃.getHeldItemMainhand().isEmpty() && ☃.getHeldItemMainhand().getItem() == Items.SHEARS) {
                  this.onHarvest(☃, ☃, ☃, ☃);
                  ☃.setBlockToAir(☃.down());
               } else {
                  ☃.destroyBlock(☃.down(), true);
               }
            }
         }
      } else if (☃.getBlockState(☃.up()).getBlock() == this) {
         ☃.setBlockState(☃.up(), Blocks.AIR.getDefaultState(), 2);
      }

      super.onBlockHarvested(☃, ☃, ☃, ☃);
   }

   private boolean onHarvest(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4) {
      BlockDoublePlant.EnumPlantType ☃ = ☃.getValue(VARIANT);
      if (☃ != BlockDoublePlant.EnumPlantType.FERN && ☃ != BlockDoublePlant.EnumPlantType.GRASS) {
         return false;
      } else {
         ☃.addStat(StatList.getBlockStats(this));
         int ☃x = (☃ == BlockDoublePlant.EnumPlantType.GRASS ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).getMeta();
         spawnAsEntity(☃, ☃, new ItemStack(Blocks.TALLGRASS, 2, ☃x));
         return true;
      }
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      for (BlockDoublePlant.EnumPlantType ☃ : BlockDoublePlant.EnumPlantType.values()) {
         ☃.add(new ItemStack(this, 1, ☃.getMeta()));
      }
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(this, 1, this.getType(☃, ☃, ☃).getMeta());
   }

   @Override
   public boolean canGrow(World var1, BlockPos var2, IBlockState var3, boolean var4) {
      BlockDoublePlant.EnumPlantType ☃ = this.getType(☃, ☃, ☃);
      return ☃ != BlockDoublePlant.EnumPlantType.GRASS && ☃ != BlockDoublePlant.EnumPlantType.FERN;
   }

   @Override
   public boolean canUseBonemeal(World var1, Random var2, BlockPos var3, IBlockState var4) {
      return true;
   }

   @Override
   public void grow(World var1, Random var2, BlockPos var3, IBlockState var4) {
      spawnAsEntity(☃, ☃, new ItemStack(this, 1, this.getType(☃, ☃, ☃).getMeta()));
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return (☃ & 8) > 0
         ? this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER)
         : this.getDefaultState()
            .withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER)
            .withProperty(VARIANT, BlockDoublePlant.EnumPlantType.byMetadata(☃ & 7));
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      if (☃.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
         IBlockState ☃ = ☃.getBlockState(☃.down());
         if (☃.getBlock() == this) {
            ☃ = ☃.withProperty(VARIANT, ☃.getValue(VARIANT));
         }
      }

      return ☃;
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER ? 8 | ☃.getValue(FACING).getHorizontalIndex() : ☃.getValue(VARIANT).getMeta();
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, HALF, VARIANT, FACING);
   }

   @Override
   public Block.EnumOffsetType getOffsetType() {
      return Block.EnumOffsetType.XZ;
   }

   public static enum EnumBlockHalf implements IStringSerializable {
      UPPER,
      LOWER;

      @Override
      public String toString() {
         return this.getName();
      }

      @Override
      public String getName() {
         return this == UPPER ? "upper" : "lower";
      }
   }

   public static enum EnumPlantType implements IStringSerializable {
      SUNFLOWER(0, "sunflower"),
      SYRINGA(1, "syringa"),
      GRASS(2, "double_grass", "grass"),
      FERN(3, "double_fern", "fern"),
      ROSE(4, "double_rose", "rose"),
      PAEONIA(5, "paeonia");

      private static final BlockDoublePlant.EnumPlantType[] META_LOOKUP = new BlockDoublePlant.EnumPlantType[values().length];
      private final int meta;
      private final String name;
      private final String translationKey;

      private EnumPlantType(int var3, String var4) {
         this(☃, ☃, ☃);
      }

      private EnumPlantType(int var3, String var4, String var5) {
         this.meta = ☃;
         this.name = ☃;
         this.translationKey = ☃;
      }

      public int getMeta() {
         return this.meta;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public static BlockDoublePlant.EnumPlantType byMetadata(int var0) {
         if (☃ < 0 || ☃ >= META_LOOKUP.length) {
            ☃ = 0;
         }

         return META_LOOKUP[☃];
      }

      @Override
      public String getName() {
         return this.name;
      }

      public String getTranslationKey() {
         return this.translationKey;
      }

      static {
         for (BlockDoublePlant.EnumPlantType ☃ : values()) {
            META_LOOKUP[☃.getMeta()] = ☃;
         }
      }
   }
}
