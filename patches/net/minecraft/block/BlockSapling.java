package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockSapling extends BlockBush implements IGrowable {
   public static final PropertyEnum<BlockPlanks.EnumType> TYPE = PropertyEnum.create("type", BlockPlanks.EnumType.class);
   public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
   protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.099999994F, 0.0, 0.099999994F, 0.9F, 0.8F, 0.9F);

   protected BlockSapling() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, BlockPlanks.EnumType.OAK).withProperty(STAGE, 0));
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return SAPLING_AABB;
   }

   @Override
   public String getLocalizedName() {
      return I18n.translateToLocal(this.getTranslationKey() + "." + BlockPlanks.EnumType.OAK.getTranslationKey() + ".name");
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!☃.isRemote) {
         super.updateTick(☃, ☃, ☃, ☃);
         if (☃.getLightFromNeighbors(☃.up()) >= 9 && ☃.nextInt(7) == 0) {
            this.grow(☃, ☃, ☃, ☃);
         }
      }
   }

   public void grow(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (☃.getValue(STAGE) == 0) {
         ☃.setBlockState(☃, ☃.cycleProperty(STAGE), 4);
      } else {
         this.generateTree(☃, ☃, ☃, ☃);
      }
   }

   public void generateTree(World var1, BlockPos var2, IBlockState var3, Random var4) {
      WorldGenerator ☃ = (WorldGenerator)(☃.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true));
      int ☃x = 0;
      int ☃xx = 0;
      boolean ☃xxx = false;
      switch ((BlockPlanks.EnumType)☃.getValue(TYPE)) {
         case SPRUCE:
            label68:
            for (☃x = 0; ☃x >= -1; ☃x--) {
               for (☃xx = 0; ☃xx >= -1; ☃xx--) {
                  if (this.isTwoByTwoOfType(☃, ☃, ☃x, ☃xx, BlockPlanks.EnumType.SPRUCE)) {
                     ☃ = new WorldGenMegaPineTree(false, ☃.nextBoolean());
                     ☃xxx = true;
                     break label68;
                  }
               }
            }

            if (!☃xxx) {
               ☃x = 0;
               ☃xx = 0;
               ☃ = new WorldGenTaiga2(true);
            }
            break;
         case BIRCH:
            ☃ = new WorldGenBirchTree(true, false);
            break;
         case JUNGLE:
            IBlockState ☃xxxx = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
            IBlockState ☃xxxxx = Blocks.LEAVES
               .getDefaultState()
               .withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE)
               .withProperty(BlockLeaves.CHECK_DECAY, false);

            label82:
            for (☃x = 0; ☃x >= -1; ☃x--) {
               for (☃xx = 0; ☃xx >= -1; ☃xx--) {
                  if (this.isTwoByTwoOfType(☃, ☃, ☃x, ☃xx, BlockPlanks.EnumType.JUNGLE)) {
                     ☃ = new WorldGenMegaJungle(true, 10, 20, ☃xxxx, ☃xxxxx);
                     ☃xxx = true;
                     break label82;
                  }
               }
            }

            if (!☃xxx) {
               ☃x = 0;
               ☃xx = 0;
               ☃ = new WorldGenTrees(true, 4 + ☃.nextInt(7), ☃xxxx, ☃xxxxx, false);
            }
            break;
         case ACACIA:
            ☃ = new WorldGenSavannaTree(true);
            break;
         case DARK_OAK:
            label96:
            for (☃x = 0; ☃x >= -1; ☃x--) {
               for (☃xx = 0; ☃xx >= -1; ☃xx--) {
                  if (this.isTwoByTwoOfType(☃, ☃, ☃x, ☃xx, BlockPlanks.EnumType.DARK_OAK)) {
                     ☃ = new WorldGenCanopyTree(true);
                     ☃xxx = true;
                     break label96;
                  }
               }
            }

            if (!☃xxx) {
               return;
            }
         case OAK:
      }

      IBlockState ☃ = Blocks.AIR.getDefaultState();
      if (☃xxx) {
         ☃.setBlockState(☃.add(☃x, 0, ☃xx), ☃, 4);
         ☃.setBlockState(☃.add(☃x + 1, 0, ☃xx), ☃, 4);
         ☃.setBlockState(☃.add(☃x, 0, ☃xx + 1), ☃, 4);
         ☃.setBlockState(☃.add(☃x + 1, 0, ☃xx + 1), ☃, 4);
      } else {
         ☃.setBlockState(☃, ☃, 4);
      }

      if (!☃.generate(☃, ☃, ☃.add(☃x, 0, ☃xx))) {
         if (☃xxx) {
            ☃.setBlockState(☃.add(☃x, 0, ☃xx), ☃, 4);
            ☃.setBlockState(☃.add(☃x + 1, 0, ☃xx), ☃, 4);
            ☃.setBlockState(☃.add(☃x, 0, ☃xx + 1), ☃, 4);
            ☃.setBlockState(☃.add(☃x + 1, 0, ☃xx + 1), ☃, 4);
         } else {
            ☃.setBlockState(☃, ☃, 4);
         }
      }
   }

   private boolean isTwoByTwoOfType(World var1, BlockPos var2, int var3, int var4, BlockPlanks.EnumType var5) {
      return this.isTypeAt(☃, ☃.add(☃, 0, ☃), ☃)
         && this.isTypeAt(☃, ☃.add(☃ + 1, 0, ☃), ☃)
         && this.isTypeAt(☃, ☃.add(☃, 0, ☃ + 1), ☃)
         && this.isTypeAt(☃, ☃.add(☃ + 1, 0, ☃ + 1), ☃);
   }

   public boolean isTypeAt(World var1, BlockPos var2, BlockPlanks.EnumType var3) {
      IBlockState ☃ = ☃.getBlockState(☃);
      return ☃.getBlock() == this && ☃.getValue(TYPE) == ☃;
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(TYPE).getMetadata();
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      for (BlockPlanks.EnumType ☃ : BlockPlanks.EnumType.values()) {
         ☃.add(new ItemStack(this, 1, ☃.getMetadata()));
      }
   }

   @Override
   public boolean canGrow(World var1, BlockPos var2, IBlockState var3, boolean var4) {
      return true;
   }

   @Override
   public boolean canUseBonemeal(World var1, Random var2, BlockPos var3, IBlockState var4) {
      return ☃.rand.nextFloat() < 0.45;
   }

   @Override
   public void grow(World var1, Random var2, BlockPos var3, IBlockState var4) {
      this.grow(☃, ☃, ☃, ☃);
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(TYPE, BlockPlanks.EnumType.byMetadata(☃ & 7)).withProperty(STAGE, (☃ & 8) >> 3);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(TYPE).getMetadata();
      return ☃ | ☃.getValue(STAGE) << 3;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, TYPE, STAGE);
   }
}
