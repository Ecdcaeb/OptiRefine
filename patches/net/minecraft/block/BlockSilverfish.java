package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSilverfish extends Block {
   public static final PropertyEnum<BlockSilverfish.EnumType> VARIANT = PropertyEnum.create("variant", BlockSilverfish.EnumType.class);

   public BlockSilverfish() {
      super(Material.CLAY);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockSilverfish.EnumType.STONE));
      this.setHardness(0.0F);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   public static boolean canContainSilverfish(IBlockState var0) {
      Block ☃ = ☃.getBlock();
      return ☃ == Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE)
         || ☃ == Blocks.COBBLESTONE
         || ☃ == Blocks.STONEBRICK;
   }

   @Override
   protected ItemStack getSilkTouchDrop(IBlockState var1) {
      switch ((BlockSilverfish.EnumType)☃.getValue(VARIANT)) {
         case COBBLESTONE:
            return new ItemStack(Blocks.COBBLESTONE);
         case STONEBRICK:
            return new ItemStack(Blocks.STONEBRICK);
         case MOSSY_STONEBRICK:
            return new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.MOSSY.getMetadata());
         case CRACKED_STONEBRICK:
            return new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.CRACKED.getMetadata());
         case CHISELED_STONEBRICK:
            return new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.CHISELED.getMetadata());
         default:
            return new ItemStack(Blocks.STONE);
      }
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      if (!☃.isRemote && ☃.getGameRules().getBoolean("doTileDrops")) {
         EntitySilverfish ☃ = new EntitySilverfish(☃);
         ☃.setLocationAndAngles(☃.getX() + 0.5, ☃.getY(), ☃.getZ() + 0.5, 0.0F, 0.0F);
         ☃.spawnEntity(☃);
         ☃.spawnExplosionParticle();
      }
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(this, 1, ☃.getBlock().getMetaFromState(☃));
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      for (BlockSilverfish.EnumType ☃ : BlockSilverfish.EnumType.values()) {
         ☃.add(new ItemStack(this, 1, ☃.getMetadata()));
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, BlockSilverfish.EnumType.byMetadata(☃));
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(VARIANT).getMetadata();
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, VARIANT);
   }

   public static enum EnumType implements IStringSerializable {
      STONE(0, "stone") {
         @Override
         public IBlockState getModelBlock() {
            return Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE);
         }
      },
      COBBLESTONE(1, "cobblestone", "cobble") {
         @Override
         public IBlockState getModelBlock() {
            return Blocks.COBBLESTONE.getDefaultState();
         }
      },
      STONEBRICK(2, "stone_brick", "brick") {
         @Override
         public IBlockState getModelBlock() {
            return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT);
         }
      },
      MOSSY_STONEBRICK(3, "mossy_brick", "mossybrick") {
         @Override
         public IBlockState getModelBlock() {
            return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
         }
      },
      CRACKED_STONEBRICK(4, "cracked_brick", "crackedbrick") {
         @Override
         public IBlockState getModelBlock() {
            return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
         }
      },
      CHISELED_STONEBRICK(5, "chiseled_brick", "chiseledbrick") {
         @Override
         public IBlockState getModelBlock() {
            return Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
         }
      };

      private static final BlockSilverfish.EnumType[] META_LOOKUP = new BlockSilverfish.EnumType[values().length];
      private final int meta;
      private final String name;
      private final String translationKey;

      private EnumType(int var3, String var4) {
         this(☃, ☃, ☃);
      }

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

      public static BlockSilverfish.EnumType byMetadata(int var0) {
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

      public abstract IBlockState getModelBlock();

      public static BlockSilverfish.EnumType forModelBlock(IBlockState var0) {
         for (BlockSilverfish.EnumType ☃ : values()) {
            if (☃ == ☃.getModelBlock()) {
               return ☃;
            }
         }

         return STONE;
      }

      static {
         for (BlockSilverfish.EnumType ☃ : values()) {
            META_LOOKUP[☃.getMetadata()] = ☃;
         }
      }
   }
}
