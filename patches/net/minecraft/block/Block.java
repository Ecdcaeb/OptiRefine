package net.minecraft.block;

import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Block {
   private static final ResourceLocation AIR_ID = new ResourceLocation("air");
   public static final RegistryNamespacedDefaultedByKey<ResourceLocation, Block> REGISTRY = new RegistryNamespacedDefaultedByKey<>(AIR_ID);
   public static final ObjectIntIdentityMap<IBlockState> BLOCK_STATE_IDS = new ObjectIntIdentityMap<>();
   public static final AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
   @Nullable
   public static final AxisAlignedBB NULL_AABB = null;
   private CreativeTabs displayOnCreativeTab;
   protected boolean fullBlock;
   protected int lightOpacity;
   protected boolean translucent;
   protected int lightValue;
   protected boolean useNeighborBrightness;
   protected float blockHardness;
   protected float blockResistance;
   protected boolean enableStats = true;
   protected boolean needsRandomTick;
   protected boolean hasTileEntity;
   protected SoundType blockSoundType = SoundType.STONE;
   public float blockParticleGravity = 1.0F;
   protected final Material material;
   protected final MapColor blockMapColor;
   public float slipperiness = 0.6F;
   protected final BlockStateContainer blockState;
   private IBlockState defaultBlockState;
   private String translationKey;

   public static int getIdFromBlock(Block var0) {
      return REGISTRY.getIDForObject(☃);
   }

   public static int getStateId(IBlockState var0) {
      Block ☃ = ☃.getBlock();
      return getIdFromBlock(☃) + (☃.getMetaFromState(☃) << 12);
   }

   public static Block getBlockById(int var0) {
      return REGISTRY.getObjectById(☃);
   }

   public static IBlockState getStateById(int var0) {
      int ☃ = ☃ & 4095;
      int ☃x = ☃ >> 12 & 15;
      return getBlockById(☃).getStateFromMeta(☃x);
   }

   public static Block getBlockFromItem(@Nullable Item var0) {
      return ☃ instanceof ItemBlock ? ((ItemBlock)☃).getBlock() : Blocks.AIR;
   }

   @Nullable
   public static Block getBlockFromName(String var0) {
      ResourceLocation ☃ = new ResourceLocation(☃);
      if (REGISTRY.containsKey(☃)) {
         return REGISTRY.getObject(☃);
      } else {
         try {
            return REGISTRY.getObjectById(Integer.parseInt(☃));
         } catch (NumberFormatException var3) {
            return null;
         }
      }
   }

   @Deprecated
   public boolean isTopSolid(IBlockState var1) {
      return ☃.getMaterial().isOpaque() && ☃.isFullCube();
   }

   @Deprecated
   public boolean isFullBlock(IBlockState var1) {
      return this.fullBlock;
   }

   @Deprecated
   public boolean canEntitySpawn(IBlockState var1, Entity var2) {
      return true;
   }

   @Deprecated
   public int getLightOpacity(IBlockState var1) {
      return this.lightOpacity;
   }

   @Deprecated
   public boolean isTranslucent(IBlockState var1) {
      return this.translucent;
   }

   @Deprecated
   public int getLightValue(IBlockState var1) {
      return this.lightValue;
   }

   @Deprecated
   public boolean getUseNeighborBrightness(IBlockState var1) {
      return this.useNeighborBrightness;
   }

   @Deprecated
   public Material getMaterial(IBlockState var1) {
      return this.material;
   }

   @Deprecated
   public MapColor getMapColor(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return this.blockMapColor;
   }

   @Deprecated
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState();
   }

   public int getMetaFromState(IBlockState var1) {
      if (☃.getPropertyKeys().isEmpty()) {
         return 0;
      } else {
         throw new IllegalArgumentException("Don't know how to convert " + ☃ + " back into data...");
      }
   }

   @Deprecated
   public IBlockState getActualState(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return ☃;
   }

   @Deprecated
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      return ☃;
   }

   @Deprecated
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      return ☃;
   }

   public Block(Material var1, MapColor var2) {
      this.material = ☃;
      this.blockMapColor = ☃;
      this.blockState = this.createBlockState();
      this.setDefaultState(this.blockState.getBaseState());
      this.fullBlock = this.getDefaultState().isOpaqueCube();
      this.lightOpacity = this.fullBlock ? 255 : 0;
      this.translucent = !☃.blocksLight();
   }

   protected Block(Material var1) {
      this(☃, ☃.getMaterialMapColor());
   }

   protected Block setSoundType(SoundType var1) {
      this.blockSoundType = ☃;
      return this;
   }

   protected Block setLightOpacity(int var1) {
      this.lightOpacity = ☃;
      return this;
   }

   protected Block setLightLevel(float var1) {
      this.lightValue = (int)(15.0F * ☃);
      return this;
   }

   protected Block setResistance(float var1) {
      this.blockResistance = ☃ * 3.0F;
      return this;
   }

   protected static boolean isExceptionBlockForAttaching(Block var0) {
      return ☃ instanceof BlockShulkerBox
         || ☃ instanceof BlockLeaves
         || ☃ instanceof BlockTrapDoor
         || ☃ == Blocks.BEACON
         || ☃ == Blocks.CAULDRON
         || ☃ == Blocks.GLASS
         || ☃ == Blocks.GLOWSTONE
         || ☃ == Blocks.ICE
         || ☃ == Blocks.SEA_LANTERN
         || ☃ == Blocks.STAINED_GLASS;
   }

   protected static boolean isExceptBlockForAttachWithPiston(Block var0) {
      return isExceptionBlockForAttaching(☃) || ☃ == Blocks.PISTON || ☃ == Blocks.STICKY_PISTON || ☃ == Blocks.PISTON_HEAD;
   }

   @Deprecated
   public boolean isBlockNormalCube(IBlockState var1) {
      return ☃.getMaterial().blocksMovement() && ☃.isFullCube();
   }

   @Deprecated
   public boolean isNormalCube(IBlockState var1) {
      return ☃.getMaterial().isOpaque() && ☃.isFullCube() && !☃.canProvidePower();
   }

   @Deprecated
   public boolean causesSuffocation(IBlockState var1) {
      return this.material.blocksMovement() && this.getDefaultState().isFullCube();
   }

   @Deprecated
   public boolean isFullCube(IBlockState var1) {
      return true;
   }

   @Deprecated
   public boolean hasCustomBreakingProgress(IBlockState var1) {
      return false;
   }

   public boolean isPassable(IBlockAccess var1, BlockPos var2) {
      return !this.material.blocksMovement();
   }

   @Deprecated
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.MODEL;
   }

   public boolean isReplaceable(IBlockAccess var1, BlockPos var2) {
      return false;
   }

   protected Block setHardness(float var1) {
      this.blockHardness = ☃;
      if (this.blockResistance < ☃ * 5.0F) {
         this.blockResistance = ☃ * 5.0F;
      }

      return this;
   }

   protected Block setBlockUnbreakable() {
      this.setHardness(-1.0F);
      return this;
   }

   @Deprecated
   public float getBlockHardness(IBlockState var1, World var2, BlockPos var3) {
      return this.blockHardness;
   }

   protected Block setTickRandomly(boolean var1) {
      this.needsRandomTick = ☃;
      return this;
   }

   public boolean getTickRandomly() {
      return this.needsRandomTick;
   }

   public boolean hasTileEntity() {
      return this.hasTileEntity;
   }

   @Deprecated
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return FULL_BLOCK_AABB;
   }

   @Deprecated
   public int getPackedLightmapCoords(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      int ☃ = ☃.getCombinedLight(☃, ☃.getLightValue());
      if (☃ == 0 && ☃.getBlock() instanceof BlockSlab) {
         ☃ = ☃.down();
         ☃ = ☃.getBlockState(☃);
         return ☃.getCombinedLight(☃, ☃.getLightValue());
      } else {
         return ☃;
      }
   }

   @Deprecated
   public boolean shouldSideBeRendered(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      AxisAlignedBB ☃ = ☃.getBoundingBox(☃, ☃);
      switch (☃) {
         case DOWN:
            if (☃.minY > 0.0) {
               return true;
            }
            break;
         case UP:
            if (☃.maxY < 1.0) {
               return true;
            }
            break;
         case NORTH:
            if (☃.minZ > 0.0) {
               return true;
            }
            break;
         case SOUTH:
            if (☃.maxZ < 1.0) {
               return true;
            }
            break;
         case WEST:
            if (☃.minX > 0.0) {
               return true;
            }
            break;
         case EAST:
            if (☃.maxX < 1.0) {
               return true;
            }
      }

      return !☃.getBlockState(☃.offset(☃)).isOpaqueCube();
   }

   @Deprecated
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      return BlockFaceShape.SOLID;
   }

   @Deprecated
   public AxisAlignedBB getSelectedBoundingBox(IBlockState var1, World var2, BlockPos var3) {
      return ☃.getBoundingBox(☃, ☃).offset(☃);
   }

   @Deprecated
   public void addCollisionBoxToList(
      IBlockState var1, World var2, BlockPos var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, @Nullable Entity var6, boolean var7
   ) {
      addCollisionBoxToList(☃, ☃, ☃, ☃.getCollisionBoundingBox(☃, ☃));
   }

   protected static void addCollisionBoxToList(BlockPos var0, AxisAlignedBB var1, List<AxisAlignedBB> var2, @Nullable AxisAlignedBB var3) {
      if (☃ != NULL_AABB) {
         AxisAlignedBB ☃ = ☃.offset(☃);
         if (☃.intersects(☃)) {
            ☃.add(☃);
         }
      }
   }

   @Deprecated
   @Nullable
   public AxisAlignedBB getCollisionBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      return ☃.getBoundingBox(☃, ☃);
   }

   @Deprecated
   public boolean isOpaqueCube(IBlockState var1) {
      return true;
   }

   public boolean canCollideCheck(IBlockState var1, boolean var2) {
      return this.isCollidable();
   }

   public boolean isCollidable() {
      return true;
   }

   public void randomTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      this.updateTick(☃, ☃, ☃, ☃);
   }

   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
   }

   public void randomDisplayTick(IBlockState var1, World var2, BlockPos var3, Random var4) {
   }

   public void onPlayerDestroy(World var1, BlockPos var2, IBlockState var3) {
   }

   @Deprecated
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
   }

   public int tickRate(World var1) {
      return 10;
   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
   }

   public int quantityDropped(Random var1) {
      return 1;
   }

   public Item getItemDropped(IBlockState var1, Random var2, int var3) {
      return Item.getItemFromBlock(this);
   }

   @Deprecated
   public float getPlayerRelativeBlockHardness(IBlockState var1, EntityPlayer var2, World var3, BlockPos var4) {
      float ☃ = ☃.getBlockHardness(☃, ☃);
      if (☃ < 0.0F) {
         return 0.0F;
      } else {
         return !☃.canHarvestBlock(☃) ? ☃.getDigSpeed(☃) / ☃ / 100.0F : ☃.getDigSpeed(☃) / ☃ / 30.0F;
      }
   }

   public final void dropBlockAsItem(World var1, BlockPos var2, IBlockState var3, int var4) {
      this.dropBlockAsItemWithChance(☃, ☃, ☃, 1.0F, ☃);
   }

   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      if (!☃.isRemote) {
         int ☃ = this.quantityDroppedWithBonus(☃, ☃.rand);

         for (int ☃x = 0; ☃x < ☃; ☃x++) {
            if (!(☃.rand.nextFloat() > ☃)) {
               Item ☃xx = this.getItemDropped(☃, ☃.rand, ☃);
               if (☃xx != Items.AIR) {
                  spawnAsEntity(☃, ☃, new ItemStack(☃xx, 1, this.damageDropped(☃)));
               }
            }
         }
      }
   }

   public static void spawnAsEntity(World var0, BlockPos var1, ItemStack var2) {
      if (!☃.isRemote && !☃.isEmpty() && ☃.getGameRules().getBoolean("doTileDrops")) {
         float ☃ = 0.5F;
         double ☃x = ☃.rand.nextFloat() * 0.5F + 0.25;
         double ☃xx = ☃.rand.nextFloat() * 0.5F + 0.25;
         double ☃xxx = ☃.rand.nextFloat() * 0.5F + 0.25;
         EntityItem ☃xxxx = new EntityItem(☃, ☃.getX() + ☃x, ☃.getY() + ☃xx, ☃.getZ() + ☃xxx, ☃);
         ☃xxxx.setDefaultPickupDelay();
         ☃.spawnEntity(☃xxxx);
      }
   }

   protected void dropXpOnBlockBreak(World var1, BlockPos var2, int var3) {
      if (!☃.isRemote && ☃.getGameRules().getBoolean("doTileDrops")) {
         while (☃ > 0) {
            int ☃ = EntityXPOrb.getXPSplit(☃);
            ☃ -= ☃;
            ☃.spawnEntity(new EntityXPOrb(☃, ☃.getX() + 0.5, ☃.getY() + 0.5, ☃.getZ() + 0.5, ☃));
         }
      }
   }

   public int damageDropped(IBlockState var1) {
      return 0;
   }

   public float getExplosionResistance(Entity var1) {
      return this.blockResistance / 5.0F;
   }

   @Deprecated
   @Nullable
   public RayTraceResult collisionRayTrace(IBlockState var1, World var2, BlockPos var3, Vec3d var4, Vec3d var5) {
      return this.rayTrace(☃, ☃, ☃, ☃.getBoundingBox(☃, ☃));
   }

   @Nullable
   protected RayTraceResult rayTrace(BlockPos var1, Vec3d var2, Vec3d var3, AxisAlignedBB var4) {
      Vec3d ☃ = ☃.subtract(☃.getX(), ☃.getY(), ☃.getZ());
      Vec3d ☃x = ☃.subtract(☃.getX(), ☃.getY(), ☃.getZ());
      RayTraceResult ☃xx = ☃.calculateIntercept(☃, ☃x);
      return ☃xx == null ? null : new RayTraceResult(☃xx.hitVec.add(☃.getX(), ☃.getY(), ☃.getZ()), ☃xx.sideHit, ☃);
   }

   public void onExplosionDestroy(World var1, BlockPos var2, Explosion var3) {
   }

   public BlockRenderLayer getRenderLayer() {
      return BlockRenderLayer.SOLID;
   }

   public boolean canPlaceBlockOnSide(World var1, BlockPos var2, EnumFacing var3) {
      return this.canPlaceBlockAt(☃, ☃);
   }

   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return ☃.getBlockState(☃).getBlock().material.isReplaceable();
   }

   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      return false;
   }

   public void onEntityWalk(World var1, BlockPos var2, Entity var3) {
   }

   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getStateFromMeta(☃);
   }

   public void onBlockClicked(World var1, BlockPos var2, EntityPlayer var3) {
   }

   public Vec3d modifyAcceleration(World var1, BlockPos var2, Entity var3, Vec3d var4) {
      return ☃;
   }

   @Deprecated
   public int getWeakPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return 0;
   }

   @Deprecated
   public boolean canProvidePower(IBlockState var1) {
      return false;
   }

   public void onEntityCollision(World var1, BlockPos var2, IBlockState var3, Entity var4) {
   }

   @Deprecated
   public int getStrongPower(IBlockState var1, IBlockAccess var2, BlockPos var3, EnumFacing var4) {
      return 0;
   }

   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, @Nullable TileEntity var5, ItemStack var6) {
      ☃.addStat(StatList.getBlockStats(this));
      ☃.addExhaustion(0.005F);
      if (this.canSilkHarvest() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, ☃) > 0) {
         ItemStack ☃ = this.getSilkTouchDrop(☃);
         spawnAsEntity(☃, ☃, ☃);
      } else {
         int ☃ = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, ☃);
         this.dropBlockAsItem(☃, ☃, ☃, ☃);
      }
   }

   protected boolean canSilkHarvest() {
      return this.getDefaultState().isFullCube() && !this.hasTileEntity;
   }

   protected ItemStack getSilkTouchDrop(IBlockState var1) {
      Item ☃ = Item.getItemFromBlock(this);
      int ☃x = 0;
      if (☃.getHasSubtypes()) {
         ☃x = this.getMetaFromState(☃);
      }

      return new ItemStack(☃, 1, ☃x);
   }

   public int quantityDroppedWithBonus(int var1, Random var2) {
      return this.quantityDropped(☃);
   }

   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
   }

   public boolean canSpawnInBlock() {
      return !this.material.isSolid() && !this.material.isLiquid();
   }

   public Block setTranslationKey(String var1) {
      this.translationKey = ☃;
      return this;
   }

   public String getLocalizedName() {
      return I18n.translateToLocal(this.getTranslationKey() + ".name");
   }

   public String getTranslationKey() {
      return "tile." + this.translationKey;
   }

   @Deprecated
   public boolean eventReceived(IBlockState var1, World var2, BlockPos var3, int var4, int var5) {
      return false;
   }

   public boolean getEnableStats() {
      return this.enableStats;
   }

   protected Block disableStats() {
      this.enableStats = false;
      return this;
   }

   @Deprecated
   public EnumPushReaction getPushReaction(IBlockState var1) {
      return this.material.getPushReaction();
   }

   @Deprecated
   public float getAmbientOcclusionLightValue(IBlockState var1) {
      return ☃.isBlockNormalCube() ? 0.2F : 1.0F;
   }

   public void onFallenUpon(World var1, BlockPos var2, Entity var3, float var4) {
      ☃.fall(☃, 1.0F);
   }

   public void onLanded(World var1, Entity var2) {
      ☃.motionY = 0.0;
   }

   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      return new ItemStack(Item.getItemFromBlock(this), 1, this.damageDropped(☃));
   }

   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      ☃.add(new ItemStack(this));
   }

   public CreativeTabs getCreativeTab() {
      return this.displayOnCreativeTab;
   }

   public Block setCreativeTab(CreativeTabs var1) {
      this.displayOnCreativeTab = ☃;
      return this;
   }

   public void onBlockHarvested(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4) {
   }

   public void fillWithRain(World var1, BlockPos var2) {
   }

   public boolean requiresUpdates() {
      return true;
   }

   public boolean canDropFromExplosion(Explosion var1) {
      return true;
   }

   public boolean isAssociatedBlock(Block var1) {
      return this == ☃;
   }

   public static boolean isEqualTo(Block var0, Block var1) {
      if (☃ == null || ☃ == null) {
         return false;
      } else {
         return ☃ == ☃ ? true : ☃.isAssociatedBlock(☃);
      }
   }

   @Deprecated
   public boolean hasComparatorInputOverride(IBlockState var1) {
      return false;
   }

   @Deprecated
   public int getComparatorInputOverride(IBlockState var1, World var2, BlockPos var3) {
      return 0;
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this);
   }

   public BlockStateContainer getBlockState() {
      return this.blockState;
   }

   protected final void setDefaultState(IBlockState var1) {
      this.defaultBlockState = ☃;
   }

   public final IBlockState getDefaultState() {
      return this.defaultBlockState;
   }

   public Block.EnumOffsetType getOffsetType() {
      return Block.EnumOffsetType.NONE;
   }

   @Deprecated
   public Vec3d getOffset(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      Block.EnumOffsetType ☃ = this.getOffsetType();
      if (☃ == Block.EnumOffsetType.NONE) {
         return Vec3d.ZERO;
      } else {
         long ☃x = MathHelper.getCoordinateRandom(☃.getX(), 0, ☃.getZ());
         return new Vec3d(
            ((float)(☃x >> 16 & 15L) / 15.0F - 0.5) * 0.5,
            ☃ == Block.EnumOffsetType.XYZ ? ((float)(☃x >> 20 & 15L) / 15.0F - 1.0) * 0.2 : 0.0,
            ((float)(☃x >> 24 & 15L) / 15.0F - 0.5) * 0.5
         );
      }
   }

   public SoundType getSoundType() {
      return this.blockSoundType;
   }

   @Override
   public String toString() {
      return "Block{" + REGISTRY.getNameForObject(this) + "}";
   }

   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
   }

   public static void registerBlocks() {
      registerBlock(0, AIR_ID, new BlockAir().setTranslationKey("air"));
      registerBlock(1, "stone", new BlockStone().setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE).setTranslationKey("stone"));
      registerBlock(2, "grass", new BlockGrass().setHardness(0.6F).setSoundType(SoundType.PLANT).setTranslationKey("grass"));
      registerBlock(3, "dirt", new BlockDirt().setHardness(0.5F).setSoundType(SoundType.GROUND).setTranslationKey("dirt"));
      Block ☃ = new Block(Material.ROCK)
         .setHardness(2.0F)
         .setResistance(10.0F)
         .setSoundType(SoundType.STONE)
         .setTranslationKey("stonebrick")
         .setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
      registerBlock(4, "cobblestone", ☃);
      Block ☃x = new BlockPlanks().setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setTranslationKey("wood");
      registerBlock(5, "planks", ☃x);
      registerBlock(6, "sapling", new BlockSapling().setHardness(0.0F).setSoundType(SoundType.PLANT).setTranslationKey("sapling"));
      registerBlock(
         7,
         "bedrock",
         new BlockEmptyDrops(Material.ROCK)
            .setBlockUnbreakable()
            .setResistance(6000000.0F)
            .setSoundType(SoundType.STONE)
            .setTranslationKey("bedrock")
            .disableStats()
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
      );
      registerBlock(8, "flowing_water", new BlockDynamicLiquid(Material.WATER).setHardness(100.0F).setLightOpacity(3).setTranslationKey("water").disableStats());
      registerBlock(9, "water", new BlockStaticLiquid(Material.WATER).setHardness(100.0F).setLightOpacity(3).setTranslationKey("water").disableStats());
      registerBlock(10, "flowing_lava", new BlockDynamicLiquid(Material.LAVA).setHardness(100.0F).setLightLevel(1.0F).setTranslationKey("lava").disableStats());
      registerBlock(11, "lava", new BlockStaticLiquid(Material.LAVA).setHardness(100.0F).setLightLevel(1.0F).setTranslationKey("lava").disableStats());
      registerBlock(12, "sand", new BlockSand().setHardness(0.5F).setSoundType(SoundType.SAND).setTranslationKey("sand"));
      registerBlock(13, "gravel", new BlockGravel().setHardness(0.6F).setSoundType(SoundType.GROUND).setTranslationKey("gravel"));
      registerBlock(14, "gold_ore", new BlockOre().setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setTranslationKey("oreGold"));
      registerBlock(15, "iron_ore", new BlockOre().setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setTranslationKey("oreIron"));
      registerBlock(16, "coal_ore", new BlockOre().setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setTranslationKey("oreCoal"));
      registerBlock(17, "log", new BlockOldLog().setTranslationKey("log"));
      registerBlock(18, "leaves", new BlockOldLeaf().setTranslationKey("leaves"));
      registerBlock(19, "sponge", new BlockSponge().setHardness(0.6F).setSoundType(SoundType.PLANT).setTranslationKey("sponge"));
      registerBlock(20, "glass", new BlockGlass(Material.GLASS, false).setHardness(0.3F).setSoundType(SoundType.GLASS).setTranslationKey("glass"));
      registerBlock(21, "lapis_ore", new BlockOre().setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setTranslationKey("oreLapis"));
      registerBlock(
         22,
         "lapis_block",
         new Block(Material.IRON, MapColor.LAPIS)
            .setHardness(3.0F)
            .setResistance(5.0F)
            .setSoundType(SoundType.STONE)
            .setTranslationKey("blockLapis")
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
      );
      registerBlock(23, "dispenser", new BlockDispenser().setHardness(3.5F).setSoundType(SoundType.STONE).setTranslationKey("dispenser"));
      Block ☃xx = new BlockSandStone().setSoundType(SoundType.STONE).setHardness(0.8F).setTranslationKey("sandStone");
      registerBlock(24, "sandstone", ☃xx);
      registerBlock(25, "noteblock", new BlockNote().setSoundType(SoundType.WOOD).setHardness(0.8F).setTranslationKey("musicBlock"));
      registerBlock(26, "bed", new BlockBed().setSoundType(SoundType.WOOD).setHardness(0.2F).setTranslationKey("bed").disableStats());
      registerBlock(27, "golden_rail", new BlockRailPowered().setHardness(0.7F).setSoundType(SoundType.METAL).setTranslationKey("goldenRail"));
      registerBlock(28, "detector_rail", new BlockRailDetector().setHardness(0.7F).setSoundType(SoundType.METAL).setTranslationKey("detectorRail"));
      registerBlock(29, "sticky_piston", new BlockPistonBase(true).setTranslationKey("pistonStickyBase"));
      registerBlock(30, "web", new BlockWeb().setLightOpacity(1).setHardness(4.0F).setTranslationKey("web"));
      registerBlock(31, "tallgrass", new BlockTallGrass().setHardness(0.0F).setSoundType(SoundType.PLANT).setTranslationKey("tallgrass"));
      registerBlock(32, "deadbush", new BlockDeadBush().setHardness(0.0F).setSoundType(SoundType.PLANT).setTranslationKey("deadbush"));
      registerBlock(33, "piston", new BlockPistonBase(false).setTranslationKey("pistonBase"));
      registerBlock(34, "piston_head", new BlockPistonExtension().setTranslationKey("pistonBase"));
      registerBlock(35, "wool", new BlockColored(Material.CLOTH).setHardness(0.8F).setSoundType(SoundType.CLOTH).setTranslationKey("cloth"));
      registerBlock(36, "piston_extension", new BlockPistonMoving());
      registerBlock(37, "yellow_flower", new BlockYellowFlower().setHardness(0.0F).setSoundType(SoundType.PLANT).setTranslationKey("flower1"));
      registerBlock(38, "red_flower", new BlockRedFlower().setHardness(0.0F).setSoundType(SoundType.PLANT).setTranslationKey("flower2"));
      Block ☃xxx = new BlockMushroom().setHardness(0.0F).setSoundType(SoundType.PLANT).setLightLevel(0.125F).setTranslationKey("mushroom");
      registerBlock(39, "brown_mushroom", ☃xxx);
      Block ☃xxxx = new BlockMushroom().setHardness(0.0F).setSoundType(SoundType.PLANT).setTranslationKey("mushroom");
      registerBlock(40, "red_mushroom", ☃xxxx);
      registerBlock(
         41,
         "gold_block",
         new Block(Material.IRON, MapColor.GOLD)
            .setHardness(3.0F)
            .setResistance(10.0F)
            .setSoundType(SoundType.METAL)
            .setTranslationKey("blockGold")
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
      );
      registerBlock(
         42,
         "iron_block",
         new Block(Material.IRON, MapColor.IRON)
            .setHardness(5.0F)
            .setResistance(10.0F)
            .setSoundType(SoundType.METAL)
            .setTranslationKey("blockIron")
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
      );
      registerBlock(
         43,
         "double_stone_slab",
         new BlockDoubleStoneSlab().setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setTranslationKey("stoneSlab")
      );
      registerBlock(
         44, "stone_slab", new BlockHalfStoneSlab().setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setTranslationKey("stoneSlab")
      );
      Block ☃xxxxx = new Block(Material.ROCK, MapColor.RED)
         .setHardness(2.0F)
         .setResistance(10.0F)
         .setSoundType(SoundType.STONE)
         .setTranslationKey("brick")
         .setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
      registerBlock(45, "brick_block", ☃xxxxx);
      registerBlock(46, "tnt", new BlockTNT().setHardness(0.0F).setSoundType(SoundType.PLANT).setTranslationKey("tnt"));
      registerBlock(47, "bookshelf", new BlockBookshelf().setHardness(1.5F).setSoundType(SoundType.WOOD).setTranslationKey("bookshelf"));
      registerBlock(
         48,
         "mossy_cobblestone",
         new Block(Material.ROCK)
            .setHardness(2.0F)
            .setResistance(10.0F)
            .setSoundType(SoundType.STONE)
            .setTranslationKey("stoneMoss")
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
      );
      registerBlock(49, "obsidian", new BlockObsidian().setHardness(50.0F).setResistance(2000.0F).setSoundType(SoundType.STONE).setTranslationKey("obsidian"));
      registerBlock(50, "torch", new BlockTorch().setHardness(0.0F).setLightLevel(0.9375F).setSoundType(SoundType.WOOD).setTranslationKey("torch"));
      registerBlock(51, "fire", new BlockFire().setHardness(0.0F).setLightLevel(1.0F).setSoundType(SoundType.CLOTH).setTranslationKey("fire").disableStats());
      registerBlock(52, "mob_spawner", new BlockMobSpawner().setHardness(5.0F).setSoundType(SoundType.METAL).setTranslationKey("mobSpawner").disableStats());
      registerBlock(
         53, "oak_stairs", new BlockStairs(☃x.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK)).setTranslationKey("stairsWood")
      );
      registerBlock(54, "chest", new BlockChest(BlockChest.Type.BASIC).setHardness(2.5F).setSoundType(SoundType.WOOD).setTranslationKey("chest"));
      registerBlock(
         55, "redstone_wire", new BlockRedstoneWire().setHardness(0.0F).setSoundType(SoundType.STONE).setTranslationKey("redstoneDust").disableStats()
      );
      registerBlock(56, "diamond_ore", new BlockOre().setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setTranslationKey("oreDiamond"));
      registerBlock(
         57,
         "diamond_block",
         new Block(Material.IRON, MapColor.DIAMOND)
            .setHardness(5.0F)
            .setResistance(10.0F)
            .setSoundType(SoundType.METAL)
            .setTranslationKey("blockDiamond")
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
      );
      registerBlock(58, "crafting_table", new BlockWorkbench().setHardness(2.5F).setSoundType(SoundType.WOOD).setTranslationKey("workbench"));
      registerBlock(59, "wheat", new BlockCrops().setTranslationKey("crops"));
      Block ☃xxxxxx = new BlockFarmland().setHardness(0.6F).setSoundType(SoundType.GROUND).setTranslationKey("farmland");
      registerBlock(60, "farmland", ☃xxxxxx);
      registerBlock(
         61,
         "furnace",
         new BlockFurnace(false).setHardness(3.5F).setSoundType(SoundType.STONE).setTranslationKey("furnace").setCreativeTab(CreativeTabs.DECORATIONS)
      );
      registerBlock(
         62, "lit_furnace", new BlockFurnace(true).setHardness(3.5F).setSoundType(SoundType.STONE).setLightLevel(0.875F).setTranslationKey("furnace")
      );
      registerBlock(63, "standing_sign", new BlockStandingSign().setHardness(1.0F).setSoundType(SoundType.WOOD).setTranslationKey("sign").disableStats());
      registerBlock(64, "wooden_door", new BlockDoor(Material.WOOD).setHardness(3.0F).setSoundType(SoundType.WOOD).setTranslationKey("doorOak").disableStats());
      registerBlock(65, "ladder", new BlockLadder().setHardness(0.4F).setSoundType(SoundType.LADDER).setTranslationKey("ladder"));
      registerBlock(66, "rail", new BlockRail().setHardness(0.7F).setSoundType(SoundType.METAL).setTranslationKey("rail"));
      registerBlock(67, "stone_stairs", new BlockStairs(☃.getDefaultState()).setTranslationKey("stairsStone"));
      registerBlock(68, "wall_sign", new BlockWallSign().setHardness(1.0F).setSoundType(SoundType.WOOD).setTranslationKey("sign").disableStats());
      registerBlock(69, "lever", new BlockLever().setHardness(0.5F).setSoundType(SoundType.WOOD).setTranslationKey("lever"));
      registerBlock(
         70,
         "stone_pressure_plate",
         new BlockPressurePlate(Material.ROCK, BlockPressurePlate.Sensitivity.MOBS)
            .setHardness(0.5F)
            .setSoundType(SoundType.STONE)
            .setTranslationKey("pressurePlateStone")
      );
      registerBlock(71, "iron_door", new BlockDoor(Material.IRON).setHardness(5.0F).setSoundType(SoundType.METAL).setTranslationKey("doorIron").disableStats());
      registerBlock(
         72,
         "wooden_pressure_plate",
         new BlockPressurePlate(Material.WOOD, BlockPressurePlate.Sensitivity.EVERYTHING)
            .setHardness(0.5F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("pressurePlateWood")
      );
      registerBlock(
         73,
         "redstone_ore",
         new BlockRedstoneOre(false)
            .setHardness(3.0F)
            .setResistance(5.0F)
            .setSoundType(SoundType.STONE)
            .setTranslationKey("oreRedstone")
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
      );
      registerBlock(
         74,
         "lit_redstone_ore",
         new BlockRedstoneOre(true).setLightLevel(0.625F).setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setTranslationKey("oreRedstone")
      );
      registerBlock(75, "unlit_redstone_torch", new BlockRedstoneTorch(false).setHardness(0.0F).setSoundType(SoundType.WOOD).setTranslationKey("notGate"));
      registerBlock(
         76,
         "redstone_torch",
         new BlockRedstoneTorch(true)
            .setHardness(0.0F)
            .setLightLevel(0.5F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("notGate")
            .setCreativeTab(CreativeTabs.REDSTONE)
      );
      registerBlock(77, "stone_button", new BlockButtonStone().setHardness(0.5F).setSoundType(SoundType.STONE).setTranslationKey("button"));
      registerBlock(78, "snow_layer", new BlockSnow().setHardness(0.1F).setSoundType(SoundType.SNOW).setTranslationKey("snow").setLightOpacity(0));
      registerBlock(79, "ice", new BlockIce().setHardness(0.5F).setLightOpacity(3).setSoundType(SoundType.GLASS).setTranslationKey("ice"));
      registerBlock(80, "snow", new BlockSnowBlock().setHardness(0.2F).setSoundType(SoundType.SNOW).setTranslationKey("snow"));
      registerBlock(81, "cactus", new BlockCactus().setHardness(0.4F).setSoundType(SoundType.CLOTH).setTranslationKey("cactus"));
      registerBlock(82, "clay", new BlockClay().setHardness(0.6F).setSoundType(SoundType.GROUND).setTranslationKey("clay"));
      registerBlock(83, "reeds", new BlockReed().setHardness(0.0F).setSoundType(SoundType.PLANT).setTranslationKey("reeds").disableStats());
      registerBlock(84, "jukebox", new BlockJukebox().setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setTranslationKey("jukebox"));
      registerBlock(
         85,
         "fence",
         new BlockFence(Material.WOOD, BlockPlanks.EnumType.OAK.getMapColor())
            .setHardness(2.0F)
            .setResistance(5.0F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("fence")
      );
      Block ☃xxxxxxx = new BlockPumpkin().setHardness(1.0F).setSoundType(SoundType.WOOD).setTranslationKey("pumpkin");
      registerBlock(86, "pumpkin", ☃xxxxxxx);
      registerBlock(87, "netherrack", new BlockNetherrack().setHardness(0.4F).setSoundType(SoundType.STONE).setTranslationKey("hellrock"));
      registerBlock(88, "soul_sand", new BlockSoulSand().setHardness(0.5F).setSoundType(SoundType.SAND).setTranslationKey("hellsand"));
      registerBlock(
         89, "glowstone", new BlockGlowstone(Material.GLASS).setHardness(0.3F).setSoundType(SoundType.GLASS).setLightLevel(1.0F).setTranslationKey("lightgem")
      );
      registerBlock(90, "portal", new BlockPortal().setHardness(-1.0F).setSoundType(SoundType.GLASS).setLightLevel(0.75F).setTranslationKey("portal"));
      registerBlock(91, "lit_pumpkin", new BlockPumpkin().setHardness(1.0F).setSoundType(SoundType.WOOD).setLightLevel(1.0F).setTranslationKey("litpumpkin"));
      registerBlock(92, "cake", new BlockCake().setHardness(0.5F).setSoundType(SoundType.CLOTH).setTranslationKey("cake").disableStats());
      registerBlock(
         93, "unpowered_repeater", new BlockRedstoneRepeater(false).setHardness(0.0F).setSoundType(SoundType.WOOD).setTranslationKey("diode").disableStats()
      );
      registerBlock(
         94, "powered_repeater", new BlockRedstoneRepeater(true).setHardness(0.0F).setSoundType(SoundType.WOOD).setTranslationKey("diode").disableStats()
      );
      registerBlock(
         95, "stained_glass", new BlockStainedGlass(Material.GLASS).setHardness(0.3F).setSoundType(SoundType.GLASS).setTranslationKey("stainedGlass")
      );
      registerBlock(
         96, "trapdoor", new BlockTrapDoor(Material.WOOD).setHardness(3.0F).setSoundType(SoundType.WOOD).setTranslationKey("trapdoor").disableStats()
      );
      registerBlock(97, "monster_egg", new BlockSilverfish().setHardness(0.75F).setTranslationKey("monsterStoneEgg"));
      Block ☃xxxxxxxx = new BlockStoneBrick().setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE).setTranslationKey("stonebricksmooth");
      registerBlock(98, "stonebrick", ☃xxxxxxxx);
      registerBlock(
         99,
         "brown_mushroom_block",
         new BlockHugeMushroom(Material.WOOD, MapColor.DIRT, ☃xxx).setHardness(0.2F).setSoundType(SoundType.WOOD).setTranslationKey("mushroom")
      );
      registerBlock(
         100,
         "red_mushroom_block",
         new BlockHugeMushroom(Material.WOOD, MapColor.RED, ☃xxxx).setHardness(0.2F).setSoundType(SoundType.WOOD).setTranslationKey("mushroom")
      );
      registerBlock(
         101,
         "iron_bars",
         new BlockPane(Material.IRON, true).setHardness(5.0F).setResistance(10.0F).setSoundType(SoundType.METAL).setTranslationKey("fenceIron")
      );
      registerBlock(102, "glass_pane", new BlockPane(Material.GLASS, false).setHardness(0.3F).setSoundType(SoundType.GLASS).setTranslationKey("thinGlass"));
      Block ☃xxxxxxxxx = new BlockMelon().setHardness(1.0F).setSoundType(SoundType.WOOD).setTranslationKey("melon");
      registerBlock(103, "melon_block", ☃xxxxxxxxx);
      registerBlock(104, "pumpkin_stem", new BlockStem(☃xxxxxxx).setHardness(0.0F).setSoundType(SoundType.WOOD).setTranslationKey("pumpkinStem"));
      registerBlock(105, "melon_stem", new BlockStem(☃xxxxxxxxx).setHardness(0.0F).setSoundType(SoundType.WOOD).setTranslationKey("pumpkinStem"));
      registerBlock(106, "vine", new BlockVine().setHardness(0.2F).setSoundType(SoundType.PLANT).setTranslationKey("vine"));
      registerBlock(
         107,
         "fence_gate",
         new BlockFenceGate(BlockPlanks.EnumType.OAK).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setTranslationKey("fenceGate")
      );
      registerBlock(108, "brick_stairs", new BlockStairs(☃xxxxx.getDefaultState()).setTranslationKey("stairsBrick"));
      registerBlock(
         109,
         "stone_brick_stairs",
         new BlockStairs(☃xxxxxxxx.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT))
            .setTranslationKey("stairsStoneBrickSmooth")
      );
      registerBlock(110, "mycelium", new BlockMycelium().setHardness(0.6F).setSoundType(SoundType.PLANT).setTranslationKey("mycel"));
      registerBlock(111, "waterlily", new BlockLilyPad().setHardness(0.0F).setSoundType(SoundType.PLANT).setTranslationKey("waterlily"));
      Block ☃xxxxxxxxxx = new BlockNetherBrick()
         .setHardness(2.0F)
         .setResistance(10.0F)
         .setSoundType(SoundType.STONE)
         .setTranslationKey("netherBrick")
         .setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
      registerBlock(112, "nether_brick", ☃xxxxxxxxxx);
      registerBlock(
         113,
         "nether_brick_fence",
         new BlockFence(Material.ROCK, MapColor.NETHERRACK)
            .setHardness(2.0F)
            .setResistance(10.0F)
            .setSoundType(SoundType.STONE)
            .setTranslationKey("netherFence")
      );
      registerBlock(114, "nether_brick_stairs", new BlockStairs(☃xxxxxxxxxx.getDefaultState()).setTranslationKey("stairsNetherBrick"));
      registerBlock(115, "nether_wart", new BlockNetherWart().setTranslationKey("netherStalk"));
      registerBlock(116, "enchanting_table", new BlockEnchantmentTable().setHardness(5.0F).setResistance(2000.0F).setTranslationKey("enchantmentTable"));
      registerBlock(117, "brewing_stand", new BlockBrewingStand().setHardness(0.5F).setLightLevel(0.125F).setTranslationKey("brewingStand"));
      registerBlock(118, "cauldron", new BlockCauldron().setHardness(2.0F).setTranslationKey("cauldron"));
      registerBlock(119, "end_portal", new BlockEndPortal(Material.PORTAL).setHardness(-1.0F).setResistance(6000000.0F));
      registerBlock(
         120,
         "end_portal_frame",
         new BlockEndPortalFrame()
            .setSoundType(SoundType.GLASS)
            .setLightLevel(0.125F)
            .setHardness(-1.0F)
            .setTranslationKey("endPortalFrame")
            .setResistance(6000000.0F)
            .setCreativeTab(CreativeTabs.DECORATIONS)
      );
      registerBlock(
         121,
         "end_stone",
         new Block(Material.ROCK, MapColor.SAND)
            .setHardness(3.0F)
            .setResistance(15.0F)
            .setSoundType(SoundType.STONE)
            .setTranslationKey("whiteStone")
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
      );
      registerBlock(
         122,
         "dragon_egg",
         new BlockDragonEgg().setHardness(3.0F).setResistance(15.0F).setSoundType(SoundType.STONE).setLightLevel(0.125F).setTranslationKey("dragonEgg")
      );
      registerBlock(
         123,
         "redstone_lamp",
         new BlockRedstoneLight(false).setHardness(0.3F).setSoundType(SoundType.GLASS).setTranslationKey("redstoneLight").setCreativeTab(CreativeTabs.REDSTONE)
      );
      registerBlock(124, "lit_redstone_lamp", new BlockRedstoneLight(true).setHardness(0.3F).setSoundType(SoundType.GLASS).setTranslationKey("redstoneLight"));
      registerBlock(
         125, "double_wooden_slab", new BlockDoubleWoodSlab().setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setTranslationKey("woodSlab")
      );
      registerBlock(
         126, "wooden_slab", new BlockHalfWoodSlab().setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setTranslationKey("woodSlab")
      );
      registerBlock(127, "cocoa", new BlockCocoa().setHardness(0.2F).setResistance(5.0F).setSoundType(SoundType.WOOD).setTranslationKey("cocoa"));
      registerBlock(
         128,
         "sandstone_stairs",
         new BlockStairs(☃xx.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH)).setTranslationKey("stairsSandStone")
      );
      registerBlock(129, "emerald_ore", new BlockOre().setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setTranslationKey("oreEmerald"));
      registerBlock(
         130,
         "ender_chest",
         new BlockEnderChest().setHardness(22.5F).setResistance(1000.0F).setSoundType(SoundType.STONE).setTranslationKey("enderChest").setLightLevel(0.5F)
      );
      registerBlock(131, "tripwire_hook", new BlockTripWireHook().setTranslationKey("tripWireSource"));
      registerBlock(132, "tripwire", new BlockTripWire().setTranslationKey("tripWire"));
      registerBlock(
         133,
         "emerald_block",
         new Block(Material.IRON, MapColor.EMERALD)
            .setHardness(5.0F)
            .setResistance(10.0F)
            .setSoundType(SoundType.METAL)
            .setTranslationKey("blockEmerald")
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
      );
      registerBlock(
         134,
         "spruce_stairs",
         new BlockStairs(☃x.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE)).setTranslationKey("stairsWoodSpruce")
      );
      registerBlock(
         135,
         "birch_stairs",
         new BlockStairs(☃x.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH)).setTranslationKey("stairsWoodBirch")
      );
      registerBlock(
         136,
         "jungle_stairs",
         new BlockStairs(☃x.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE)).setTranslationKey("stairsWoodJungle")
      );
      registerBlock(
         137, "command_block", new BlockCommandBlock(MapColor.BROWN).setBlockUnbreakable().setResistance(6000000.0F).setTranslationKey("commandBlock")
      );
      registerBlock(138, "beacon", new BlockBeacon().setTranslationKey("beacon").setLightLevel(1.0F));
      registerBlock(139, "cobblestone_wall", new BlockWall(☃).setTranslationKey("cobbleWall"));
      registerBlock(140, "flower_pot", new BlockFlowerPot().setHardness(0.0F).setSoundType(SoundType.STONE).setTranslationKey("flowerPot"));
      registerBlock(141, "carrots", new BlockCarrot().setTranslationKey("carrots"));
      registerBlock(142, "potatoes", new BlockPotato().setTranslationKey("potatoes"));
      registerBlock(143, "wooden_button", new BlockButtonWood().setHardness(0.5F).setSoundType(SoundType.WOOD).setTranslationKey("button"));
      registerBlock(144, "skull", new BlockSkull().setHardness(1.0F).setSoundType(SoundType.STONE).setTranslationKey("skull"));
      registerBlock(145, "anvil", new BlockAnvil().setHardness(5.0F).setSoundType(SoundType.ANVIL).setResistance(2000.0F).setTranslationKey("anvil"));
      registerBlock(146, "trapped_chest", new BlockChest(BlockChest.Type.TRAP).setHardness(2.5F).setSoundType(SoundType.WOOD).setTranslationKey("chestTrap"));
      registerBlock(
         147,
         "light_weighted_pressure_plate",
         new BlockPressurePlateWeighted(Material.IRON, 15, MapColor.GOLD)
            .setHardness(0.5F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("weightedPlate_light")
      );
      registerBlock(
         148,
         "heavy_weighted_pressure_plate",
         new BlockPressurePlateWeighted(Material.IRON, 150).setHardness(0.5F).setSoundType(SoundType.WOOD).setTranslationKey("weightedPlate_heavy")
      );
      registerBlock(
         149,
         "unpowered_comparator",
         new BlockRedstoneComparator(false).setHardness(0.0F).setSoundType(SoundType.WOOD).setTranslationKey("comparator").disableStats()
      );
      registerBlock(
         150,
         "powered_comparator",
         new BlockRedstoneComparator(true).setHardness(0.0F).setLightLevel(0.625F).setSoundType(SoundType.WOOD).setTranslationKey("comparator").disableStats()
      );
      registerBlock(151, "daylight_detector", new BlockDaylightDetector(false));
      registerBlock(
         152,
         "redstone_block",
         new BlockCompressedPowered(Material.IRON, MapColor.TNT)
            .setHardness(5.0F)
            .setResistance(10.0F)
            .setSoundType(SoundType.METAL)
            .setTranslationKey("blockRedstone")
            .setCreativeTab(CreativeTabs.REDSTONE)
      );
      registerBlock(
         153,
         "quartz_ore",
         new BlockOre(MapColor.NETHERRACK).setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setTranslationKey("netherquartz")
      );
      registerBlock(154, "hopper", new BlockHopper().setHardness(3.0F).setResistance(8.0F).setSoundType(SoundType.METAL).setTranslationKey("hopper"));
      Block ☃xxxxxxxxxxx = new BlockQuartz().setSoundType(SoundType.STONE).setHardness(0.8F).setTranslationKey("quartzBlock");
      registerBlock(155, "quartz_block", ☃xxxxxxxxxxx);
      registerBlock(
         156,
         "quartz_stairs",
         new BlockStairs(☃xxxxxxxxxxx.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.DEFAULT)).setTranslationKey("stairsQuartz")
      );
      registerBlock(157, "activator_rail", new BlockRailPowered().setHardness(0.7F).setSoundType(SoundType.METAL).setTranslationKey("activatorRail"));
      registerBlock(158, "dropper", new BlockDropper().setHardness(3.5F).setSoundType(SoundType.STONE).setTranslationKey("dropper"));
      registerBlock(
         159,
         "stained_hardened_clay",
         new BlockStainedHardenedClay().setHardness(1.25F).setResistance(7.0F).setSoundType(SoundType.STONE).setTranslationKey("clayHardenedStained")
      );
      registerBlock(
         160, "stained_glass_pane", new BlockStainedGlassPane().setHardness(0.3F).setSoundType(SoundType.GLASS).setTranslationKey("thinStainedGlass")
      );
      registerBlock(161, "leaves2", new BlockNewLeaf().setTranslationKey("leaves"));
      registerBlock(162, "log2", new BlockNewLog().setTranslationKey("log"));
      registerBlock(
         163,
         "acacia_stairs",
         new BlockStairs(☃x.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA)).setTranslationKey("stairsWoodAcacia")
      );
      registerBlock(
         164,
         "dark_oak_stairs",
         new BlockStairs(☃x.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK)).setTranslationKey("stairsWoodDarkOak")
      );
      registerBlock(165, "slime", new BlockSlime().setTranslationKey("slime").setSoundType(SoundType.SLIME));
      registerBlock(166, "barrier", new BlockBarrier().setTranslationKey("barrier"));
      registerBlock(
         167,
         "iron_trapdoor",
         new BlockTrapDoor(Material.IRON).setHardness(5.0F).setSoundType(SoundType.METAL).setTranslationKey("ironTrapdoor").disableStats()
      );
      registerBlock(
         168, "prismarine", new BlockPrismarine().setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE).setTranslationKey("prismarine")
      );
      registerBlock(
         169,
         "sea_lantern",
         new BlockSeaLantern(Material.GLASS).setHardness(0.3F).setSoundType(SoundType.GLASS).setLightLevel(1.0F).setTranslationKey("seaLantern")
      );
      registerBlock(
         170,
         "hay_block",
         new BlockHay().setHardness(0.5F).setSoundType(SoundType.PLANT).setTranslationKey("hayBlock").setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
      );
      registerBlock(171, "carpet", new BlockCarpet().setHardness(0.1F).setSoundType(SoundType.CLOTH).setTranslationKey("woolCarpet").setLightOpacity(0));
      registerBlock(
         172, "hardened_clay", new BlockHardenedClay().setHardness(1.25F).setResistance(7.0F).setSoundType(SoundType.STONE).setTranslationKey("clayHardened")
      );
      registerBlock(
         173,
         "coal_block",
         new Block(Material.ROCK, MapColor.BLACK)
            .setHardness(5.0F)
            .setResistance(10.0F)
            .setSoundType(SoundType.STONE)
            .setTranslationKey("blockCoal")
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
      );
      registerBlock(174, "packed_ice", new BlockPackedIce().setHardness(0.5F).setSoundType(SoundType.GLASS).setTranslationKey("icePacked"));
      registerBlock(175, "double_plant", new BlockDoublePlant());
      registerBlock(
         176,
         "standing_banner",
         new BlockBanner.BlockBannerStanding().setHardness(1.0F).setSoundType(SoundType.WOOD).setTranslationKey("banner").disableStats()
      );
      registerBlock(
         177, "wall_banner", new BlockBanner.BlockBannerHanging().setHardness(1.0F).setSoundType(SoundType.WOOD).setTranslationKey("banner").disableStats()
      );
      registerBlock(178, "daylight_detector_inverted", new BlockDaylightDetector(true));
      Block ☃xxxxxxxxxxxx = new BlockRedSandstone().setSoundType(SoundType.STONE).setHardness(0.8F).setTranslationKey("redSandStone");
      registerBlock(179, "red_sandstone", ☃xxxxxxxxxxxx);
      registerBlock(
         180,
         "red_sandstone_stairs",
         new BlockStairs(☃xxxxxxxxxxxx.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH))
            .setTranslationKey("stairsRedSandStone")
      );
      registerBlock(
         181,
         "double_stone_slab2",
         new BlockDoubleStoneSlabNew().setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setTranslationKey("stoneSlab2")
      );
      registerBlock(
         182, "stone_slab2", new BlockHalfStoneSlabNew().setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setTranslationKey("stoneSlab2")
      );
      registerBlock(
         183,
         "spruce_fence_gate",
         new BlockFenceGate(BlockPlanks.EnumType.SPRUCE)
            .setHardness(2.0F)
            .setResistance(5.0F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("spruceFenceGate")
      );
      registerBlock(
         184,
         "birch_fence_gate",
         new BlockFenceGate(BlockPlanks.EnumType.BIRCH).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setTranslationKey("birchFenceGate")
      );
      registerBlock(
         185,
         "jungle_fence_gate",
         new BlockFenceGate(BlockPlanks.EnumType.JUNGLE)
            .setHardness(2.0F)
            .setResistance(5.0F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("jungleFenceGate")
      );
      registerBlock(
         186,
         "dark_oak_fence_gate",
         new BlockFenceGate(BlockPlanks.EnumType.DARK_OAK)
            .setHardness(2.0F)
            .setResistance(5.0F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("darkOakFenceGate")
      );
      registerBlock(
         187,
         "acacia_fence_gate",
         new BlockFenceGate(BlockPlanks.EnumType.ACACIA)
            .setHardness(2.0F)
            .setResistance(5.0F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("acaciaFenceGate")
      );
      registerBlock(
         188,
         "spruce_fence",
         new BlockFence(Material.WOOD, BlockPlanks.EnumType.SPRUCE.getMapColor())
            .setHardness(2.0F)
            .setResistance(5.0F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("spruceFence")
      );
      registerBlock(
         189,
         "birch_fence",
         new BlockFence(Material.WOOD, BlockPlanks.EnumType.BIRCH.getMapColor())
            .setHardness(2.0F)
            .setResistance(5.0F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("birchFence")
      );
      registerBlock(
         190,
         "jungle_fence",
         new BlockFence(Material.WOOD, BlockPlanks.EnumType.JUNGLE.getMapColor())
            .setHardness(2.0F)
            .setResistance(5.0F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("jungleFence")
      );
      registerBlock(
         191,
         "dark_oak_fence",
         new BlockFence(Material.WOOD, BlockPlanks.EnumType.DARK_OAK.getMapColor())
            .setHardness(2.0F)
            .setResistance(5.0F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("darkOakFence")
      );
      registerBlock(
         192,
         "acacia_fence",
         new BlockFence(Material.WOOD, BlockPlanks.EnumType.ACACIA.getMapColor())
            .setHardness(2.0F)
            .setResistance(5.0F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("acaciaFence")
      );
      registerBlock(
         193, "spruce_door", new BlockDoor(Material.WOOD).setHardness(3.0F).setSoundType(SoundType.WOOD).setTranslationKey("doorSpruce").disableStats()
      );
      registerBlock(
         194, "birch_door", new BlockDoor(Material.WOOD).setHardness(3.0F).setSoundType(SoundType.WOOD).setTranslationKey("doorBirch").disableStats()
      );
      registerBlock(
         195, "jungle_door", new BlockDoor(Material.WOOD).setHardness(3.0F).setSoundType(SoundType.WOOD).setTranslationKey("doorJungle").disableStats()
      );
      registerBlock(
         196, "acacia_door", new BlockDoor(Material.WOOD).setHardness(3.0F).setSoundType(SoundType.WOOD).setTranslationKey("doorAcacia").disableStats()
      );
      registerBlock(
         197, "dark_oak_door", new BlockDoor(Material.WOOD).setHardness(3.0F).setSoundType(SoundType.WOOD).setTranslationKey("doorDarkOak").disableStats()
      );
      registerBlock(198, "end_rod", new BlockEndRod().setHardness(0.0F).setLightLevel(0.9375F).setSoundType(SoundType.WOOD).setTranslationKey("endRod"));
      registerBlock(199, "chorus_plant", new BlockChorusPlant().setHardness(0.4F).setSoundType(SoundType.WOOD).setTranslationKey("chorusPlant"));
      registerBlock(200, "chorus_flower", new BlockChorusFlower().setHardness(0.4F).setSoundType(SoundType.WOOD).setTranslationKey("chorusFlower"));
      Block ☃xxxxxxxxxxxxx = new Block(Material.ROCK, MapColor.MAGENTA)
         .setHardness(1.5F)
         .setResistance(10.0F)
         .setSoundType(SoundType.STONE)
         .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
         .setTranslationKey("purpurBlock");
      registerBlock(201, "purpur_block", ☃xxxxxxxxxxxxx);
      registerBlock(
         202,
         "purpur_pillar",
         new BlockRotatedPillar(Material.ROCK, MapColor.MAGENTA)
            .setHardness(1.5F)
            .setResistance(10.0F)
            .setSoundType(SoundType.STONE)
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
            .setTranslationKey("purpurPillar")
      );
      registerBlock(203, "purpur_stairs", new BlockStairs(☃xxxxxxxxxxxxx.getDefaultState()).setTranslationKey("stairsPurpur"));
      registerBlock(
         204,
         "purpur_double_slab",
         new BlockPurpurSlab.Double().setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setTranslationKey("purpurSlab")
      );
      registerBlock(
         205, "purpur_slab", new BlockPurpurSlab.Half().setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setTranslationKey("purpurSlab")
      );
      registerBlock(
         206,
         "end_bricks",
         new Block(Material.ROCK, MapColor.SAND)
            .setSoundType(SoundType.STONE)
            .setHardness(0.8F)
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
            .setTranslationKey("endBricks")
      );
      registerBlock(207, "beetroots", new BlockBeetroot().setTranslationKey("beetroots"));
      Block ☃xxxxxxxxxxxxxx = new BlockGrassPath().setHardness(0.65F).setSoundType(SoundType.PLANT).setTranslationKey("grassPath").disableStats();
      registerBlock(208, "grass_path", ☃xxxxxxxxxxxxxx);
      registerBlock(209, "end_gateway", new BlockEndGateway(Material.PORTAL).setHardness(-1.0F).setResistance(6000000.0F));
      registerBlock(
         210,
         "repeating_command_block",
         new BlockCommandBlock(MapColor.PURPLE).setBlockUnbreakable().setResistance(6000000.0F).setTranslationKey("repeatingCommandBlock")
      );
      registerBlock(
         211,
         "chain_command_block",
         new BlockCommandBlock(MapColor.GREEN).setBlockUnbreakable().setResistance(6000000.0F).setTranslationKey("chainCommandBlock")
      );
      registerBlock(
         212, "frosted_ice", new BlockFrostedIce().setHardness(0.5F).setLightOpacity(3).setSoundType(SoundType.GLASS).setTranslationKey("frostedIce")
      );
      registerBlock(213, "magma", new BlockMagma().setHardness(0.5F).setSoundType(SoundType.STONE).setTranslationKey("magma"));
      registerBlock(
         214,
         "nether_wart_block",
         new Block(Material.GRASS, MapColor.RED)
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
            .setHardness(1.0F)
            .setSoundType(SoundType.WOOD)
            .setTranslationKey("netherWartBlock")
      );
      registerBlock(
         215,
         "red_nether_brick",
         new BlockNetherBrick()
            .setHardness(2.0F)
            .setResistance(10.0F)
            .setSoundType(SoundType.STONE)
            .setTranslationKey("redNetherBrick")
            .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
      );
      registerBlock(216, "bone_block", new BlockBone().setTranslationKey("boneBlock"));
      registerBlock(217, "structure_void", new BlockStructureVoid().setTranslationKey("structureVoid"));
      registerBlock(218, "observer", new BlockObserver().setHardness(3.0F).setTranslationKey("observer"));
      registerBlock(
         219, "white_shulker_box", new BlockShulkerBox(EnumDyeColor.WHITE).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxWhite")
      );
      registerBlock(
         220,
         "orange_shulker_box",
         new BlockShulkerBox(EnumDyeColor.ORANGE).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxOrange")
      );
      registerBlock(
         221,
         "magenta_shulker_box",
         new BlockShulkerBox(EnumDyeColor.MAGENTA).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxMagenta")
      );
      registerBlock(
         222,
         "light_blue_shulker_box",
         new BlockShulkerBox(EnumDyeColor.LIGHT_BLUE).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxLightBlue")
      );
      registerBlock(
         223,
         "yellow_shulker_box",
         new BlockShulkerBox(EnumDyeColor.YELLOW).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxYellow")
      );
      registerBlock(
         224, "lime_shulker_box", new BlockShulkerBox(EnumDyeColor.LIME).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxLime")
      );
      registerBlock(
         225, "pink_shulker_box", new BlockShulkerBox(EnumDyeColor.PINK).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxPink")
      );
      registerBlock(
         226, "gray_shulker_box", new BlockShulkerBox(EnumDyeColor.GRAY).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxGray")
      );
      registerBlock(
         227,
         "silver_shulker_box",
         new BlockShulkerBox(EnumDyeColor.SILVER).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxSilver")
      );
      registerBlock(
         228, "cyan_shulker_box", new BlockShulkerBox(EnumDyeColor.CYAN).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxCyan")
      );
      registerBlock(
         229,
         "purple_shulker_box",
         new BlockShulkerBox(EnumDyeColor.PURPLE).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxPurple")
      );
      registerBlock(
         230, "blue_shulker_box", new BlockShulkerBox(EnumDyeColor.BLUE).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxBlue")
      );
      registerBlock(
         231, "brown_shulker_box", new BlockShulkerBox(EnumDyeColor.BROWN).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxBrown")
      );
      registerBlock(
         232, "green_shulker_box", new BlockShulkerBox(EnumDyeColor.GREEN).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxGreen")
      );
      registerBlock(
         233, "red_shulker_box", new BlockShulkerBox(EnumDyeColor.RED).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxRed")
      );
      registerBlock(
         234, "black_shulker_box", new BlockShulkerBox(EnumDyeColor.BLACK).setHardness(2.0F).setSoundType(SoundType.STONE).setTranslationKey("shulkerBoxBlack")
      );
      registerBlock(235, "white_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.WHITE));
      registerBlock(236, "orange_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.ORANGE));
      registerBlock(237, "magenta_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.MAGENTA));
      registerBlock(238, "light_blue_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.LIGHT_BLUE));
      registerBlock(239, "yellow_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.YELLOW));
      registerBlock(240, "lime_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.LIME));
      registerBlock(241, "pink_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.PINK));
      registerBlock(242, "gray_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.GRAY));
      registerBlock(243, "silver_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.SILVER));
      registerBlock(244, "cyan_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.CYAN));
      registerBlock(245, "purple_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.PURPLE));
      registerBlock(246, "blue_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BLUE));
      registerBlock(247, "brown_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BROWN));
      registerBlock(248, "green_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.GREEN));
      registerBlock(249, "red_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.RED));
      registerBlock(250, "black_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BLACK));
      registerBlock(251, "concrete", new BlockColored(Material.ROCK).setHardness(1.8F).setSoundType(SoundType.STONE).setTranslationKey("concrete"));
      registerBlock(252, "concrete_powder", new BlockConcretePowder().setHardness(0.5F).setSoundType(SoundType.SAND).setTranslationKey("concretePowder"));
      registerBlock(255, "structure_block", new BlockStructure().setBlockUnbreakable().setResistance(6000000.0F).setTranslationKey("structureBlock"));
      REGISTRY.validateKey();

      for (Block ☃xxxxxxxxxxxxxxx : REGISTRY) {
         if (☃xxxxxxxxxxxxxxx.material == Material.AIR) {
            ☃xxxxxxxxxxxxxxx.useNeighborBrightness = false;
         } else {
            boolean ☃xxxxxxxxxxxxxxxx = false;
            boolean ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx instanceof BlockStairs;
            boolean ☃xxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx instanceof BlockSlab;
            boolean ☃xxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx == ☃xxxxxx || ☃xxxxxxxxxxxxxxx == ☃xxxxxxxxxxxxxx;
            boolean ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.translucent;
            boolean ☃xxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.lightOpacity == 0;
            if (☃xxxxxxxxxxxxxxxxx || ☃xxxxxxxxxxxxxxxxxx || ☃xxxxxxxxxxxxxxxxxxx || ☃xxxxxxxxxxxxxxxxxxxx || ☃xxxxxxxxxxxxxxxxxxxxx) {
               ☃xxxxxxxxxxxxxxxx = true;
            }

            ☃xxxxxxxxxxxxxxx.useNeighborBrightness = ☃xxxxxxxxxxxxxxxx;
         }
      }

      Set<Block> ☃xxxxxxxxxxxxxxxx = Sets.newHashSet(new Block[]{REGISTRY.getObject(new ResourceLocation("tripwire"))});

      for (Block ☃xxxxxxxxxxxxxxxxx : REGISTRY) {
         if (☃xxxxxxxxxxxxxxxx.contains(☃xxxxxxxxxxxxxxxxx)) {
            for (int ☃xxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxx < 15; ☃xxxxxxxxxxxxxxxxxx++) {
               int ☃xxxxxxxxxxxxxxxxxxx = REGISTRY.getIDForObject(☃xxxxxxxxxxxxxxxxx) << 4 | ☃xxxxxxxxxxxxxxxxxx;
               BLOCK_STATE_IDS.put(☃xxxxxxxxxxxxxxxxx.getStateFromMeta(☃xxxxxxxxxxxxxxxxxx), ☃xxxxxxxxxxxxxxxxxxx);
            }
         } else {
            UnmodifiableIterator var26 = ☃xxxxxxxxxxxxxxxxx.getBlockState().getValidStates().iterator();

            while (var26.hasNext()) {
               IBlockState ☃xxxxxxxxxxxxxxxxxx = (IBlockState)var26.next();
               int ☃xxxxxxxxxxxxxxxxxxx = REGISTRY.getIDForObject(☃xxxxxxxxxxxxxxxxx) << 4 | ☃xxxxxxxxxxxxxxxxx.getMetaFromState(☃xxxxxxxxxxxxxxxxxx);
               BLOCK_STATE_IDS.put(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx);
            }
         }
      }
   }

   private static void registerBlock(int var0, ResourceLocation var1, Block var2) {
      REGISTRY.register(☃, ☃, ☃);
   }

   private static void registerBlock(int var0, String var1, Block var2) {
      registerBlock(☃, new ResourceLocation(☃), ☃);
   }

   public static enum EnumOffsetType {
      NONE,
      XZ,
      XYZ;
   }
}
