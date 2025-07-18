package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TileEntity {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> REGISTRY = new RegistryNamespaced<>();
   protected World world;
   protected BlockPos pos = BlockPos.ORIGIN;
   protected boolean tileEntityInvalid;
   private int blockMetadata = -1;
   protected Block blockType;

   private static void register(String var0, Class<? extends TileEntity> var1) {
      REGISTRY.putObject(new ResourceLocation(☃), ☃);
   }

   @Nullable
   public static ResourceLocation getKey(Class<? extends TileEntity> var0) {
      return REGISTRY.getNameForObject(☃);
   }

   public World getWorld() {
      return this.world;
   }

   public void setWorld(World var1) {
      this.world = ☃;
   }

   public boolean hasWorld() {
      return this.world != null;
   }

   public void readFromNBT(NBTTagCompound var1) {
      this.pos = new BlockPos(☃.getInteger("x"), ☃.getInteger("y"), ☃.getInteger("z"));
   }

   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      return this.writeInternal(☃);
   }

   private NBTTagCompound writeInternal(NBTTagCompound var1) {
      ResourceLocation ☃ = REGISTRY.getNameForObject((Class<? extends TileEntity>)this.getClass());
      if (☃ == null) {
         throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
      } else {
         ☃.setString("id", ☃.toString());
         ☃.setInteger("x", this.pos.getX());
         ☃.setInteger("y", this.pos.getY());
         ☃.setInteger("z", this.pos.getZ());
         return ☃;
      }
   }

   @Nullable
   public static TileEntity create(World var0, NBTTagCompound var1) {
      TileEntity ☃ = null;
      String ☃x = ☃.getString("id");

      try {
         Class<? extends TileEntity> ☃xx = REGISTRY.getObject(new ResourceLocation(☃x));
         if (☃xx != null) {
            ☃ = ☃xx.newInstance();
         }
      } catch (Throwable var6) {
         LOGGER.error("Failed to create block entity {}", ☃x, var6);
      }

      if (☃ != null) {
         try {
            ☃.setWorldCreate(☃);
            ☃.readFromNBT(☃);
         } catch (Throwable var5) {
            LOGGER.error("Failed to load data for block entity {}", ☃x, var5);
            ☃ = null;
         }
      } else {
         LOGGER.warn("Skipping BlockEntity with id {}", ☃x);
      }

      return ☃;
   }

   protected void setWorldCreate(World var1) {
   }

   public int getBlockMetadata() {
      if (this.blockMetadata == -1) {
         IBlockState ☃ = this.world.getBlockState(this.pos);
         this.blockMetadata = ☃.getBlock().getMetaFromState(☃);
      }

      return this.blockMetadata;
   }

   public void markDirty() {
      if (this.world != null) {
         IBlockState ☃ = this.world.getBlockState(this.pos);
         this.blockMetadata = ☃.getBlock().getMetaFromState(☃);
         this.world.markChunkDirty(this.pos, this);
         if (this.getBlockType() != Blocks.AIR) {
            this.world.updateComparatorOutputLevel(this.pos, this.getBlockType());
         }
      }
   }

   public double getDistanceSq(double var1, double var3, double var5) {
      double ☃ = this.pos.getX() + 0.5 - ☃;
      double ☃x = this.pos.getY() + 0.5 - ☃;
      double ☃xx = this.pos.getZ() + 0.5 - ☃;
      return ☃ * ☃ + ☃x * ☃x + ☃xx * ☃xx;
   }

   public double getMaxRenderDistanceSquared() {
      return 4096.0;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public Block getBlockType() {
      if (this.blockType == null && this.world != null) {
         this.blockType = this.world.getBlockState(this.pos).getBlock();
      }

      return this.blockType;
   }

   @Nullable
   public SPacketUpdateTileEntity getUpdatePacket() {
      return null;
   }

   public NBTTagCompound getUpdateTag() {
      return this.writeInternal(new NBTTagCompound());
   }

   public boolean isInvalid() {
      return this.tileEntityInvalid;
   }

   public void invalidate() {
      this.tileEntityInvalid = true;
   }

   public void validate() {
      this.tileEntityInvalid = false;
   }

   public boolean receiveClientEvent(int var1, int var2) {
      return false;
   }

   public void updateContainingBlockInfo() {
      this.blockType = null;
      this.blockMetadata = -1;
   }

   public void addInfoToCrashReport(CrashReportCategory var1) {
      ☃.addDetail(
         "Name",
         new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return TileEntity.REGISTRY.getNameForObject((Class<? extends TileEntity>)TileEntity.this.getClass())
                  + " // "
                  + TileEntity.this.getClass().getCanonicalName();
            }
         }
      );
      if (this.world != null) {
         CrashReportCategory.addBlockInfo(☃, this.pos, this.getBlockType(), this.getBlockMetadata());
         ☃.addDetail("Actual block type", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               int ☃ = Block.getIdFromBlock(TileEntity.this.world.getBlockState(TileEntity.this.pos).getBlock());

               try {
                  return String.format("ID #%d (%s // %s)", ☃, Block.getBlockById(☃).getTranslationKey(), Block.getBlockById(☃).getClass().getCanonicalName());
               } catch (Throwable var3) {
                  return "ID #" + ☃;
               }
            }
         });
         ☃.addDetail("Actual block data value", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               IBlockState ☃ = TileEntity.this.world.getBlockState(TileEntity.this.pos);
               int ☃x = ☃.getBlock().getMetaFromState(☃);
               if (☃x < 0) {
                  return "Unknown? (Got " + ☃x + ")";
               } else {
                  String ☃xx = String.format("%4s", Integer.toBinaryString(☃x)).replace(" ", "0");
                  return String.format("%1$d / 0x%1$X / 0b%2$s", ☃x, ☃xx);
               }
            }
         });
      }
   }

   public void setPos(BlockPos var1) {
      this.pos = ☃.toImmutable();
   }

   public boolean onlyOpsCanSetNbt() {
      return false;
   }

   @Nullable
   public ITextComponent getDisplayName() {
      return null;
   }

   public void rotate(Rotation var1) {
   }

   public void mirror(Mirror var1) {
   }

   static {
      register("furnace", TileEntityFurnace.class);
      register("chest", TileEntityChest.class);
      register("ender_chest", TileEntityEnderChest.class);
      register("jukebox", BlockJukebox.TileEntityJukebox.class);
      register("dispenser", TileEntityDispenser.class);
      register("dropper", TileEntityDropper.class);
      register("sign", TileEntitySign.class);
      register("mob_spawner", TileEntityMobSpawner.class);
      register("noteblock", TileEntityNote.class);
      register("piston", TileEntityPiston.class);
      register("brewing_stand", TileEntityBrewingStand.class);
      register("enchanting_table", TileEntityEnchantmentTable.class);
      register("end_portal", TileEntityEndPortal.class);
      register("beacon", TileEntityBeacon.class);
      register("skull", TileEntitySkull.class);
      register("daylight_detector", TileEntityDaylightDetector.class);
      register("hopper", TileEntityHopper.class);
      register("comparator", TileEntityComparator.class);
      register("flower_pot", TileEntityFlowerPot.class);
      register("banner", TileEntityBanner.class);
      register("structure_block", TileEntityStructure.class);
      register("end_gateway", TileEntityEndGateway.class);
      register("command_block", TileEntityCommandBlock.class);
      register("shulker_box", TileEntityShulkerBox.class);
      register("bed", TileEntityBed.class);
   }
}
