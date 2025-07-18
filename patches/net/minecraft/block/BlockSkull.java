package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSkull extends BlockContainer {
   public static final PropertyDirection FACING = BlockDirectional.FACING;
   public static final PropertyBool NODROP = PropertyBool.create("nodrop");
   private static final Predicate<BlockWorldState> IS_WITHER_SKELETON = new Predicate<BlockWorldState>() {
      public boolean apply(@Nullable BlockWorldState var1) {
         return ☃.getBlockState() != null
            && ☃.getBlockState().getBlock() == Blocks.SKULL
            && ☃.getTileEntity() instanceof TileEntitySkull
            && ((TileEntitySkull)☃.getTileEntity()).getSkullType() == 1;
      }
   };
   protected static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 0.5, 0.75);
   protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.25, 0.25, 0.5, 0.75, 0.75, 1.0);
   protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.25, 0.25, 0.0, 0.75, 0.75, 0.5);
   protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.5, 0.25, 0.25, 1.0, 0.75, 0.75);
   protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0, 0.25, 0.25, 0.5, 0.75, 0.75);
   private BlockPattern witherBasePattern;
   private BlockPattern witherPattern;

   protected BlockSkull() {
      super(Material.CIRCUITS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(NODROP, false));
   }

   @Override
   public String getLocalizedName() {
      return I18n.translateToLocal("tile.skull.skeleton.name");
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
   public boolean hasCustomBreakingProgress(IBlockState var1) {
      return true;
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      switch ((EnumFacing)☃.getValue(FACING)) {
         case UP:
         default:
            return DEFAULT_AABB;
         case NORTH:
            return NORTH_AABB;
         case SOUTH:
            return SOUTH_AABB;
         case WEST:
            return WEST_AABB;
         case EAST:
            return EAST_AABB;
      }
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, ☃.getHorizontalFacing()).withProperty(NODROP, false);
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntitySkull();
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      int ☃ = 0;
      TileEntity ☃x = ☃.getTileEntity(☃);
      if (☃x instanceof TileEntitySkull) {
         ☃ = ((TileEntitySkull)☃x).getSkullType();
      }

      return new ItemStack(Items.SKULL, 1, ☃);
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
   }

   @Override
   public void onBlockHarvested(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4) {
      if (☃.capabilities.isCreativeMode) {
         ☃ = ☃.withProperty(NODROP, true);
         ☃.setBlockState(☃, ☃, 4);
      }

      super.onBlockHarvested(☃, ☃, ☃, ☃);
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      if (!☃.isRemote) {
         if (!☃.getValue(NODROP)) {
            TileEntity ☃ = ☃.getTileEntity(☃);
            if (☃ instanceof TileEntitySkull) {
               TileEntitySkull ☃x = (TileEntitySkull)☃;
               ItemStack ☃xx = this.getItem(☃, ☃, ☃);
               if (☃x.getSkullType() == 3 && ☃x.getPlayerProfile() != null) {
                  ☃xx.setTagCompound(new NBTTagCompound());
                  NBTTagCompound ☃xxx = new NBTTagCompound();
                  NBTUtil.writeGameProfile(☃xxx, ☃x.getPlayerProfile());
                  ☃xx.getTagCompound().setTag("SkullOwner", ☃xxx);
               }

               spawnAsEntity(☃, ☃, ☃xx);
            }
         }

         super.breakBlock(☃, ☃, ☃);
      }
   }

   @Override
   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Items.SKULL;
   }

   public boolean canDispenserPlace(World var1, BlockPos var2, ItemStack var3) {
      return ☃.getMetadata() == 1 && ☃.getY() >= 2 && ☃.getDifficulty() != EnumDifficulty.PEACEFUL && !☃.isRemote
         ? this.getWitherBasePattern().match(☃, ☃) != null
         : false;
   }

   public void checkWitherSpawn(World var1, BlockPos var2, TileEntitySkull var3) {
      if (☃.getSkullType() == 1 && ☃.getY() >= 2 && ☃.getDifficulty() != EnumDifficulty.PEACEFUL && !☃.isRemote) {
         BlockPattern ☃ = this.getWitherPattern();
         BlockPattern.PatternHelper ☃x = ☃.match(☃, ☃);
         if (☃x != null) {
            for (int ☃xx = 0; ☃xx < 3; ☃xx++) {
               BlockWorldState ☃xxx = ☃x.translateOffset(☃xx, 0, 0);
               ☃.setBlockState(☃xxx.getPos(), ☃xxx.getBlockState().withProperty(NODROP, true), 2);
            }

            for (int ☃xx = 0; ☃xx < ☃.getPalmLength(); ☃xx++) {
               for (int ☃xxx = 0; ☃xxx < ☃.getThumbLength(); ☃xxx++) {
                  BlockWorldState ☃xxxx = ☃x.translateOffset(☃xx, ☃xxx, 0);
                  ☃.setBlockState(☃xxxx.getPos(), Blocks.AIR.getDefaultState(), 2);
               }
            }

            BlockPos ☃xx = ☃x.translateOffset(1, 0, 0).getPos();
            EntityWither ☃xxx = new EntityWither(☃);
            BlockPos ☃xxxx = ☃x.translateOffset(1, 2, 0).getPos();
            ☃xxx.setLocationAndAngles(
               ☃xxxx.getX() + 0.5, ☃xxxx.getY() + 0.55, ☃xxxx.getZ() + 0.5, ☃x.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F, 0.0F
            );
            ☃xxx.renderYawOffset = ☃x.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F;
            ☃xxx.ignite();

            for (EntityPlayerMP ☃xxxxx : ☃.getEntitiesWithinAABB(EntityPlayerMP.class, ☃xxx.getEntityBoundingBox().grow(50.0))) {
               CriteriaTriggers.SUMMONED_ENTITY.trigger(☃xxxxx, ☃xxx);
            }

            ☃.spawnEntity(☃xxx);

            for (int ☃xxxxx = 0; ☃xxxxx < 120; ☃xxxxx++) {
               ☃.spawnParticle(
                  EnumParticleTypes.SNOWBALL,
                  ☃xx.getX() + ☃.rand.nextDouble(),
                  ☃xx.getY() - 2 + ☃.rand.nextDouble() * 3.9,
                  ☃xx.getZ() + ☃.rand.nextDouble(),
                  0.0,
                  0.0,
                  0.0
               );
            }

            for (int ☃xxxxx = 0; ☃xxxxx < ☃.getPalmLength(); ☃xxxxx++) {
               for (int ☃xxxxxx = 0; ☃xxxxxx < ☃.getThumbLength(); ☃xxxxxx++) {
                  BlockWorldState ☃xxxxxxx = ☃x.translateOffset(☃xxxxx, ☃xxxxxx, 0);
                  ☃.notifyNeighborsRespectDebug(☃xxxxxxx.getPos(), Blocks.AIR, false);
               }
            }
         }
      }
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(☃ & 7)).withProperty(NODROP, (☃ & 8) > 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(FACING).getIndex();
      if (☃.getValue(NODROP)) {
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
      return new BlockStateContainer(this, FACING, NODROP);
   }

   protected BlockPattern getWitherBasePattern() {
      if (this.witherBasePattern == null) {
         this.witherBasePattern = FactoryBlockPattern.start()
            .aisle("   ", "###", "~#~")
            .where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SOUL_SAND)))
            .where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR)))
            .build();
      }

      return this.witherBasePattern;
   }

   protected BlockPattern getWitherPattern() {
      if (this.witherPattern == null) {
         this.witherPattern = FactoryBlockPattern.start()
            .aisle("^^^", "###", "~#~")
            .where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SOUL_SAND)))
            .where('^', IS_WITHER_SKELETON)
            .where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR)))
            .build();
      }

      return this.witherPattern;
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.UNDEFINED;
   }
}
