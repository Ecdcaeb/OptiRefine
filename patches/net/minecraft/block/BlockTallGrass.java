package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTallGrass extends BlockBush implements IGrowable {
   public static final PropertyEnum<BlockTallGrass.EnumType> TYPE = PropertyEnum.create("type", BlockTallGrass.EnumType.class);
   protected static final AxisAlignedBB TALL_GRASS_AABB = new AxisAlignedBB(0.099999994F, 0.0, 0.099999994F, 0.9F, 0.8F, 0.9F);

   protected BlockTallGrass() {
      super(Material.VINE);
      this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockTallGrass.EnumType.DEAD_BUSH));
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return TALL_GRASS_AABB;
   }

   @Override
   public boolean canBlockStay(World var1, BlockPos var2, IBlockState var3) {
      return this.canSustainBush(☃.getBlockState(☃.down()));
   }

   @Override
   public boolean isReplaceable(IBlockAccess var1, BlockPos var2) {
      return true;
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return ☃.nextInt(8) == 0 ? Items.WHEAT_SEEDS : Items.AIR;
   }

   @Override
   public int quantityDroppedWithBonus(int var1, Random var2) {
      return 1 + ☃.nextInt(☃ * 2 + 1);
   }

   @Override
   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, @Nullable TileEntity var5, ItemStack var6) {
      if (!☃.isRemote && ☃.getItem() == Items.SHEARS) {
         ☃.addStat(StatList.getBlockStats(this));
         spawnAsEntity(☃, ☃, new ItemStack(Blocks.TALLGRASS, 1, ☃.getValue(TYPE).getMeta()));
      } else {
         super.harvestBlock(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(this, 1, ☃.getBlock().getMetaFromState(☃));
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      for (int ☃ = 1; ☃ < 3; ☃++) {
         ☃.add(new ItemStack(this, 1, ☃));
      }
   }

   @Override
   public boolean canGrow(World var1, BlockPos var2, IBlockState var3, boolean var4) {
      return ☃.getValue(TYPE) != BlockTallGrass.EnumType.DEAD_BUSH;
   }

   @Override
   public boolean canUseBonemeal(World var1, Random var2, BlockPos var3, IBlockState var4) {
      return true;
   }

   @Override
   public void grow(World var1, Random var2, BlockPos var3, IBlockState var4) {
      BlockDoublePlant.EnumPlantType ☃ = BlockDoublePlant.EnumPlantType.GRASS;
      if (☃.getValue(TYPE) == BlockTallGrass.EnumType.FERN) {
         ☃ = BlockDoublePlant.EnumPlantType.FERN;
      }

      if (Blocks.DOUBLE_PLANT.canPlaceBlockAt(☃, ☃)) {
         Blocks.DOUBLE_PLANT.placeAt(☃, ☃, ☃, 2);
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(TYPE, BlockTallGrass.EnumType.byMetadata(☃));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(TYPE).getMeta();
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, TYPE);
   }

   @Override
   public Block.EnumOffsetType getOffsetType() {
      return Block.EnumOffsetType.XYZ;
   }

   public static enum EnumType implements IStringSerializable {
      DEAD_BUSH(0, "dead_bush"),
      GRASS(1, "tall_grass"),
      FERN(2, "fern");

      private static final BlockTallGrass.EnumType[] META_LOOKUP = new BlockTallGrass.EnumType[values().length];
      private final int meta;
      private final String name;

      private EnumType(int var3, String var4) {
         this.meta = ☃;
         this.name = ☃;
      }

      public int getMeta() {
         return this.meta;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public static BlockTallGrass.EnumType byMetadata(int var0) {
         if (☃ < 0 || ☃ >= META_LOOKUP.length) {
            ☃ = 0;
         }

         return META_LOOKUP[☃];
      }

      @Override
      public String getName() {
         return this.name;
      }

      static {
         for (BlockTallGrass.EnumType ☃ : values()) {
            META_LOOKUP[☃.getMeta()] = ☃;
         }
      }
   }
}
