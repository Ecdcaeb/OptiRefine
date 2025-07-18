package net.minecraft.block;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockFlower extends BlockBush {
   protected PropertyEnum<BlockFlower.EnumFlowerType> type;

   protected BlockFlower() {
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(
               this.getTypeProperty(),
               this.getBlockType() == BlockFlower.EnumFlowerColor.RED ? BlockFlower.EnumFlowerType.POPPY : BlockFlower.EnumFlowerType.DANDELION
            )
      );
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return super.getBoundingBox(☃, ☃, ☃).offset(☃.getOffset(☃, ☃));
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(this.getTypeProperty()).getMeta();
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      for (BlockFlower.EnumFlowerType ☃ : BlockFlower.EnumFlowerType.getTypes(this.getBlockType())) {
         ☃.add(new ItemStack(this, 1, ☃.getMeta()));
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(this.getTypeProperty(), BlockFlower.EnumFlowerType.getType(this.getBlockType(), ☃));
   }

   public abstract BlockFlower.EnumFlowerColor getBlockType();

   public IProperty<BlockFlower.EnumFlowerType> getTypeProperty() {
      if (this.type == null) {
         this.type = PropertyEnum.create("type", BlockFlower.EnumFlowerType.class, new Predicate<BlockFlower.EnumFlowerType>() {
            public boolean apply(@Nullable BlockFlower.EnumFlowerType var1) {
               return ☃.getBlockType() == BlockFlower.this.getBlockType();
            }
         });
      }

      return this.type;
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(this.getTypeProperty()).getMeta();
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, this.getTypeProperty());
   }

   @Override
   public Block.EnumOffsetType getOffsetType() {
      return Block.EnumOffsetType.XZ;
   }

   public static enum EnumFlowerColor {
      YELLOW,
      RED;

      public BlockFlower getBlock() {
         return this == YELLOW ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
      }
   }

   public static enum EnumFlowerType implements IStringSerializable {
      DANDELION(BlockFlower.EnumFlowerColor.YELLOW, 0, "dandelion"),
      POPPY(BlockFlower.EnumFlowerColor.RED, 0, "poppy"),
      BLUE_ORCHID(BlockFlower.EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"),
      ALLIUM(BlockFlower.EnumFlowerColor.RED, 2, "allium"),
      HOUSTONIA(BlockFlower.EnumFlowerColor.RED, 3, "houstonia"),
      RED_TULIP(BlockFlower.EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"),
      ORANGE_TULIP(BlockFlower.EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"),
      WHITE_TULIP(BlockFlower.EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"),
      PINK_TULIP(BlockFlower.EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"),
      OXEYE_DAISY(BlockFlower.EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");

      private static final BlockFlower.EnumFlowerType[][] TYPES_FOR_BLOCK = new BlockFlower.EnumFlowerType[BlockFlower.EnumFlowerColor.values().length][];
      private final BlockFlower.EnumFlowerColor blockType;
      private final int meta;
      private final String name;
      private final String translationKey;

      private EnumFlowerType(BlockFlower.EnumFlowerColor var3, int var4, String var5) {
         this(☃, ☃, ☃, ☃);
      }

      private EnumFlowerType(BlockFlower.EnumFlowerColor var3, int var4, String var5, String var6) {
         this.blockType = ☃;
         this.meta = ☃;
         this.name = ☃;
         this.translationKey = ☃;
      }

      public BlockFlower.EnumFlowerColor getBlockType() {
         return this.blockType;
      }

      public int getMeta() {
         return this.meta;
      }

      public static BlockFlower.EnumFlowerType getType(BlockFlower.EnumFlowerColor var0, int var1) {
         BlockFlower.EnumFlowerType[] ☃ = TYPES_FOR_BLOCK[☃.ordinal()];
         if (☃ < 0 || ☃ >= ☃.length) {
            ☃ = 0;
         }

         return ☃[☃];
      }

      public static BlockFlower.EnumFlowerType[] getTypes(BlockFlower.EnumFlowerColor var0) {
         return TYPES_FOR_BLOCK[☃.ordinal()];
      }

      @Override
      public String toString() {
         return this.name;
      }

      @Override
      public String getName() {
         return this.name;
      }

      public String getTranslationKey() {
         return this.translationKey;
      }

      static {
         for (final BlockFlower.EnumFlowerColor ☃ : BlockFlower.EnumFlowerColor.values()) {
            Collection<BlockFlower.EnumFlowerType> ☃x = Collections2.filter(Lists.newArrayList(values()), new Predicate<BlockFlower.EnumFlowerType>() {
               public boolean apply(@Nullable BlockFlower.EnumFlowerType var1) {
                  return ☃.getBlockType() == ☃;
               }
            });
            TYPES_FOR_BLOCK[☃.ordinal()] = ☃x.toArray(new BlockFlower.EnumFlowerType[☃x.size()]);
         }
      }
   }
}
