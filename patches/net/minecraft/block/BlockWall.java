package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWall extends Block {
   public static final PropertyBool UP = PropertyBool.create("up");
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   public static final PropertyEnum<BlockWall.EnumType> VARIANT = PropertyEnum.create("variant", BlockWall.EnumType.class);
   protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[]{
      new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 0.75),
      new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 1.0, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.25, 0.75, 1.0, 0.75),
      new AxisAlignedBB(0.0, 0.0, 0.25, 0.75, 1.0, 1.0),
      new AxisAlignedBB(0.25, 0.0, 0.0, 0.75, 1.0, 0.75),
      new AxisAlignedBB(0.3125, 0.0, 0.0, 0.6875, 0.875, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 0.75, 1.0, 0.75),
      new AxisAlignedBB(0.0, 0.0, 0.0, 0.75, 1.0, 1.0),
      new AxisAlignedBB(0.25, 0.0, 0.25, 1.0, 1.0, 0.75),
      new AxisAlignedBB(0.25, 0.0, 0.25, 1.0, 1.0, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.3125, 1.0, 0.875, 0.6875),
      new AxisAlignedBB(0.0, 0.0, 0.25, 1.0, 1.0, 1.0),
      new AxisAlignedBB(0.25, 0.0, 0.0, 1.0, 1.0, 0.75),
      new AxisAlignedBB(0.25, 0.0, 0.0, 1.0, 1.0, 1.0),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.75),
      new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
   };
   protected static final AxisAlignedBB[] CLIP_AABB_BY_INDEX = new AxisAlignedBB[]{
      AABB_BY_INDEX[0].setMaxY(1.5),
      AABB_BY_INDEX[1].setMaxY(1.5),
      AABB_BY_INDEX[2].setMaxY(1.5),
      AABB_BY_INDEX[3].setMaxY(1.5),
      AABB_BY_INDEX[4].setMaxY(1.5),
      AABB_BY_INDEX[5].setMaxY(1.5),
      AABB_BY_INDEX[6].setMaxY(1.5),
      AABB_BY_INDEX[7].setMaxY(1.5),
      AABB_BY_INDEX[8].setMaxY(1.5),
      AABB_BY_INDEX[9].setMaxY(1.5),
      AABB_BY_INDEX[10].setMaxY(1.5),
      AABB_BY_INDEX[11].setMaxY(1.5),
      AABB_BY_INDEX[12].setMaxY(1.5),
      AABB_BY_INDEX[13].setMaxY(1.5),
      AABB_BY_INDEX[14].setMaxY(1.5),
      AABB_BY_INDEX[15].setMaxY(1.5)
   };

   public BlockWall(Block var1) {
      super(☃.material);
      this.setDefaultState(
         this.blockState
            .getBaseState()
            .withProperty(UP, false)
            .withProperty(NORTH, false)
            .withProperty(EAST, false)
            .withProperty(SOUTH, false)
            .withProperty(WEST, false)
            .withProperty(VARIANT, BlockWall.EnumType.NORMAL)
      );
      this.setHardness(☃.blockHardness);
      this.setResistance(☃.blockResistance / 3.0F);
      this.setSoundType(☃.blockSoundType);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      ☃ = this.getActualState(☃, ☃, ☃);
      return AABB_BY_INDEX[getAABBIndex(☃)];
   }

   @Override
   public void addCollisionBoxToList(
      IBlockState var1, World var2, BlockPos var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, @Nullable Entity var6, boolean var7
   ) {
      if (!☃) {
         ☃ = this.getActualState(☃, ☃, ☃);
      }

      addCollisionBoxToList(☃, ☃, ☃, CLIP_AABB_BY_INDEX[getAABBIndex(☃)]);
   }

   @Nullable
   @Override
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      ☃ = this.getActualState(☃, ☃, ☃);
      return CLIP_AABB_BY_INDEX[getAABBIndex(☃)];
   }

   private static int getAABBIndex(IBlockState var0) {
      int ☃ = 0;
      if (☃.getValue(NORTH)) {
         ☃ |= 1 << EnumFacing.NORTH.getHorizontalIndex();
      }

      if (☃.getValue(EAST)) {
         ☃ |= 1 << EnumFacing.EAST.getHorizontalIndex();
      }

      if (☃.getValue(SOUTH)) {
         ☃ |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
      }

      if (☃.getValue(WEST)) {
         ☃ |= 1 << EnumFacing.WEST.getHorizontalIndex();
      }

      return ☃;
   }

   @Override
   public String getLocalizedName() {
      return I18n.translateToLocal(this.getTranslationKey() + "." + BlockWall.EnumType.NORMAL.getTranslationKey() + ".name");
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return false;
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   private boolean canConnectTo(IBlockAccess var1, BlockPos var2, EnumFacing var3) {
      IBlockState ☃ = ☃.getBlockState(☃);
      Block ☃x = ☃.getBlock();
      BlockFaceShape ☃xx = ☃.getBlockFaceShape(☃, ☃, ☃);
      boolean ☃xxx = ☃xx == BlockFaceShape.MIDDLE_POLE_THICK || ☃xx == BlockFaceShape.MIDDLE_POLE && ☃x instanceof BlockFenceGate;
      return !isExcepBlockForAttachWithPiston(☃x) && ☃xx == BlockFaceShape.SOLID || ☃xxx;
   }

   protected static boolean isExcepBlockForAttachWithPiston(Block var0) {
      return Block.isExceptBlockForAttachWithPiston(☃) || ☃ == Blocks.BARRIER || ☃ == Blocks.MELON_BLOCK || ☃ == Blocks.PUMPKIN || ☃ == Blocks.LIT_PUMPKIN;
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      for (BlockWall.EnumType ☃ : BlockWall.EnumType.values()) {
         ☃.add(new ItemStack(this, 1, ☃.getMetadata()));
      }
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(VARIANT).getMetadata();
   }

   @Override
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return ☃ == EnumFacing.DOWN ? super.shouldSideBeRendered(☃, ☃, ☃, ☃) : true;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockWall.EnumType.byMetadata(☃));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(VARIANT).getMetadata();
   }

   @Override
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      boolean ☃ = this.canConnectTo(☃, ☃.north(), EnumFacing.SOUTH);
      boolean ☃x = this.canConnectTo(☃, ☃.east(), EnumFacing.WEST);
      boolean ☃xx = this.canConnectTo(☃, ☃.south(), EnumFacing.NORTH);
      boolean ☃xxx = this.canConnectTo(☃, ☃.west(), EnumFacing.EAST);
      boolean ☃xxxx = ☃ && !☃x && ☃xx && !☃xxx || !☃ && ☃x && !☃xx && ☃xxx;
      return ☃.withProperty(UP, !☃xxxx || !☃.isAirBlock(☃.up()))
         .withProperty(NORTH, ☃)
         .withProperty(EAST, ☃x)
         .withProperty(SOUTH, ☃xx)
         .withProperty(WEST, ☃xxx);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, UP, NORTH, EAST, WEST, SOUTH, VARIANT);
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return ☃ != EnumFacing.UP && ☃ != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THICK : BlockFaceShape.CENTER_BIG;
   }

   public static enum EnumType implements IStringSerializable {
      NORMAL(0, "cobblestone", "normal"),
      MOSSY(1, "mossy_cobblestone", "mossy");

      private static final BlockWall.EnumType[] META_LOOKUP = new BlockWall.EnumType[values().length];
      private final int meta;
      private final String name;
      private final String translationKey;

      private EnumType(int var3, String var4, String var5) {
         this.meta = ☃;
         this.name = ☃;
         this.translationKey = ☃;
      }

      public int getMetadata() {
         return this.meta;
      }

      @Override
      public String toString() {
         return this.name;
      }

      public static BlockWall.EnumType byMetadata(int var0) {
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
         for (BlockWall.EnumType ☃ : values()) {
            META_LOOKUP[☃.getMetadata()] = ☃;
         }
      }
   }
}
