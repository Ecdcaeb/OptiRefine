package net.minecraft.tileentity;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class TileEntityStructure extends TileEntity {
   private String name = "";
   private String author = "";
   private String metadata = "";
   private BlockPos position = new BlockPos(0, 1, 0);
   private BlockPos size = BlockPos.ORIGIN;
   private Mirror mirror = Mirror.NONE;
   private Rotation rotation = Rotation.NONE;
   private TileEntityStructure.Mode mode = TileEntityStructure.Mode.DATA;
   private boolean ignoreEntities = true;
   private boolean powered;
   private boolean showAir;
   private boolean showBoundingBox = true;
   private float integrity = 1.0F;
   private long seed;

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      ☃.setString("name", this.name);
      ☃.setString("author", this.author);
      ☃.setString("metadata", this.metadata);
      ☃.setInteger("posX", this.position.getX());
      ☃.setInteger("posY", this.position.getY());
      ☃.setInteger("posZ", this.position.getZ());
      ☃.setInteger("sizeX", this.size.getX());
      ☃.setInteger("sizeY", this.size.getY());
      ☃.setInteger("sizeZ", this.size.getZ());
      ☃.setString("rotation", this.rotation.toString());
      ☃.setString("mirror", this.mirror.toString());
      ☃.setString("mode", this.mode.toString());
      ☃.setBoolean("ignoreEntities", this.ignoreEntities);
      ☃.setBoolean("powered", this.powered);
      ☃.setBoolean("showair", this.showAir);
      ☃.setBoolean("showboundingbox", this.showBoundingBox);
      ☃.setFloat("integrity", this.integrity);
      ☃.setLong("seed", this.seed);
      return ☃;
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.setName(☃.getString("name"));
      this.author = ☃.getString("author");
      this.metadata = ☃.getString("metadata");
      int ☃ = MathHelper.clamp(☃.getInteger("posX"), -32, 32);
      int ☃x = MathHelper.clamp(☃.getInteger("posY"), -32, 32);
      int ☃xx = MathHelper.clamp(☃.getInteger("posZ"), -32, 32);
      this.position = new BlockPos(☃, ☃x, ☃xx);
      int ☃xxx = MathHelper.clamp(☃.getInteger("sizeX"), 0, 32);
      int ☃xxxx = MathHelper.clamp(☃.getInteger("sizeY"), 0, 32);
      int ☃xxxxx = MathHelper.clamp(☃.getInteger("sizeZ"), 0, 32);
      this.size = new BlockPos(☃xxx, ☃xxxx, ☃xxxxx);

      try {
         this.rotation = Rotation.valueOf(☃.getString("rotation"));
      } catch (IllegalArgumentException var11) {
         this.rotation = Rotation.NONE;
      }

      try {
         this.mirror = Mirror.valueOf(☃.getString("mirror"));
      } catch (IllegalArgumentException var10) {
         this.mirror = Mirror.NONE;
      }

      try {
         this.mode = TileEntityStructure.Mode.valueOf(☃.getString("mode"));
      } catch (IllegalArgumentException var9) {
         this.mode = TileEntityStructure.Mode.DATA;
      }

      this.ignoreEntities = ☃.getBoolean("ignoreEntities");
      this.powered = ☃.getBoolean("powered");
      this.showAir = ☃.getBoolean("showair");
      this.showBoundingBox = ☃.getBoolean("showboundingbox");
      if (☃.hasKey("integrity")) {
         this.integrity = ☃.getFloat("integrity");
      } else {
         this.integrity = 1.0F;
      }

      this.seed = ☃.getLong("seed");
      this.updateBlockState();
   }

   private void updateBlockState() {
      if (this.world != null) {
         BlockPos ☃ = this.getPos();
         IBlockState ☃x = this.world.getBlockState(☃);
         if (☃x.getBlock() == Blocks.STRUCTURE_BLOCK) {
            this.world.setBlockState(☃, ☃x.withProperty(BlockStructure.MODE, this.mode), 2);
         }
      }
   }

   @Nullable
   @Override
   public SPacketUpdateTileEntity getUpdatePacket() {
      return new SPacketUpdateTileEntity(this.pos, 7, this.getUpdateTag());
   }

   @Override
   public NBTTagCompound getUpdateTag() {
      return this.writeToNBT(new NBTTagCompound());
   }

   public boolean usedBy(EntityPlayer var1) {
      if (!☃.canUseCommandBlock()) {
         return false;
      } else {
         if (☃.getEntityWorld().isRemote) {
            ☃.openEditStructure(this);
         }

         return true;
      }
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      String ☃ = ☃;

      for (char ☃x : ChatAllowedCharacters.ILLEGAL_STRUCTURE_CHARACTERS) {
         ☃ = ☃.replace(☃x, '_');
      }

      this.name = ☃;
   }

   public void createdBy(EntityLivingBase var1) {
      if (!StringUtils.isNullOrEmpty(☃.getName())) {
         this.author = ☃.getName();
      }
   }

   public BlockPos getPosition() {
      return this.position;
   }

   public void setPosition(BlockPos var1) {
      this.position = ☃;
   }

   public BlockPos getStructureSize() {
      return this.size;
   }

   public void setSize(BlockPos var1) {
      this.size = ☃;
   }

   public Mirror getMirror() {
      return this.mirror;
   }

   public void setMirror(Mirror var1) {
      this.mirror = ☃;
   }

   public Rotation getRotation() {
      return this.rotation;
   }

   public void setRotation(Rotation var1) {
      this.rotation = ☃;
   }

   public String getMetadata() {
      return this.metadata;
   }

   public void setMetadata(String var1) {
      this.metadata = ☃;
   }

   public TileEntityStructure.Mode getMode() {
      return this.mode;
   }

   public void setMode(TileEntityStructure.Mode var1) {
      this.mode = ☃;
      IBlockState ☃ = this.world.getBlockState(this.getPos());
      if (☃.getBlock() == Blocks.STRUCTURE_BLOCK) {
         this.world.setBlockState(this.getPos(), ☃.withProperty(BlockStructure.MODE, ☃), 2);
      }
   }

   public void nextMode() {
      switch (this.getMode()) {
         case SAVE:
            this.setMode(TileEntityStructure.Mode.LOAD);
            break;
         case LOAD:
            this.setMode(TileEntityStructure.Mode.CORNER);
            break;
         case CORNER:
            this.setMode(TileEntityStructure.Mode.DATA);
            break;
         case DATA:
            this.setMode(TileEntityStructure.Mode.SAVE);
      }
   }

   public boolean ignoresEntities() {
      return this.ignoreEntities;
   }

   public void setIgnoresEntities(boolean var1) {
      this.ignoreEntities = ☃;
   }

   public float getIntegrity() {
      return this.integrity;
   }

   public void setIntegrity(float var1) {
      this.integrity = ☃;
   }

   public long getSeed() {
      return this.seed;
   }

   public void setSeed(long var1) {
      this.seed = ☃;
   }

   public boolean detectSize() {
      if (this.mode != TileEntityStructure.Mode.SAVE) {
         return false;
      } else {
         BlockPos ☃ = this.getPos();
         int ☃x = 80;
         BlockPos ☃xx = new BlockPos(☃.getX() - 80, 0, ☃.getZ() - 80);
         BlockPos ☃xxx = new BlockPos(☃.getX() + 80, 255, ☃.getZ() + 80);
         List<TileEntityStructure> ☃xxxx = this.getNearbyCornerBlocks(☃xx, ☃xxx);
         List<TileEntityStructure> ☃xxxxx = this.filterRelatedCornerBlocks(☃xxxx);
         if (☃xxxxx.size() < 1) {
            return false;
         } else {
            StructureBoundingBox ☃xxxxxx = this.calculateEnclosingBoundingBox(☃, ☃xxxxx);
            if (☃xxxxxx.maxX - ☃xxxxxx.minX > 1 && ☃xxxxxx.maxY - ☃xxxxxx.minY > 1 && ☃xxxxxx.maxZ - ☃xxxxxx.minZ > 1) {
               this.position = new BlockPos(☃xxxxxx.minX - ☃.getX() + 1, ☃xxxxxx.minY - ☃.getY() + 1, ☃xxxxxx.minZ - ☃.getZ() + 1);
               this.size = new BlockPos(☃xxxxxx.maxX - ☃xxxxxx.minX - 1, ☃xxxxxx.maxY - ☃xxxxxx.minY - 1, ☃xxxxxx.maxZ - ☃xxxxxx.minZ - 1);
               this.markDirty();
               IBlockState ☃xxxxxxx = this.world.getBlockState(☃);
               this.world.notifyBlockUpdate(☃, ☃xxxxxxx, ☃xxxxxxx, 3);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   private List<TileEntityStructure> filterRelatedCornerBlocks(List<TileEntityStructure> var1) {
      Iterable<TileEntityStructure> ☃ = Iterables.filter(☃, new Predicate<TileEntityStructure>() {
         public boolean apply(@Nullable TileEntityStructure var1) {
            return ☃.mode == TileEntityStructure.Mode.CORNER && TileEntityStructure.this.name.equals(☃.name);
         }
      });
      return Lists.newArrayList(☃);
   }

   private List<TileEntityStructure> getNearbyCornerBlocks(BlockPos var1, BlockPos var2) {
      List<TileEntityStructure> ☃ = Lists.newArrayList();

      for (BlockPos.MutableBlockPos ☃x : BlockPos.getAllInBoxMutable(☃, ☃)) {
         IBlockState ☃xx = this.world.getBlockState(☃x);
         if (☃xx.getBlock() == Blocks.STRUCTURE_BLOCK) {
            TileEntity ☃xxx = this.world.getTileEntity(☃x);
            if (☃xxx != null && ☃xxx instanceof TileEntityStructure) {
               ☃.add((TileEntityStructure)☃xxx);
            }
         }
      }

      return ☃;
   }

   private StructureBoundingBox calculateEnclosingBoundingBox(BlockPos var1, List<TileEntityStructure> var2) {
      StructureBoundingBox ☃;
      if (☃.size() > 1) {
         BlockPos ☃x = ☃.get(0).getPos();
         ☃ = new StructureBoundingBox(☃x, ☃x);
      } else {
         ☃ = new StructureBoundingBox(☃, ☃);
      }

      for (TileEntityStructure ☃x : ☃) {
         BlockPos ☃xx = ☃x.getPos();
         if (☃xx.getX() < ☃.minX) {
            ☃.minX = ☃xx.getX();
         } else if (☃xx.getX() > ☃.maxX) {
            ☃.maxX = ☃xx.getX();
         }

         if (☃xx.getY() < ☃.minY) {
            ☃.minY = ☃xx.getY();
         } else if (☃xx.getY() > ☃.maxY) {
            ☃.maxY = ☃xx.getY();
         }

         if (☃xx.getZ() < ☃.minZ) {
            ☃.minZ = ☃xx.getZ();
         } else if (☃xx.getZ() > ☃.maxZ) {
            ☃.maxZ = ☃xx.getZ();
         }
      }

      return ☃;
   }

   public void writeCoordinates(ByteBuf var1) {
      ☃.writeInt(this.pos.getX());
      ☃.writeInt(this.pos.getY());
      ☃.writeInt(this.pos.getZ());
   }

   public boolean save() {
      return this.save(true);
   }

   public boolean save(boolean var1) {
      if (this.mode == TileEntityStructure.Mode.SAVE && !this.world.isRemote && !StringUtils.isNullOrEmpty(this.name)) {
         BlockPos ☃ = this.getPos().add(this.position);
         WorldServer ☃x = (WorldServer)this.world;
         MinecraftServer ☃xx = this.world.getMinecraftServer();
         TemplateManager ☃xxx = ☃x.getStructureTemplateManager();
         Template ☃xxxx = ☃xxx.getTemplate(☃xx, new ResourceLocation(this.name));
         ☃xxxx.takeBlocksFromWorld(this.world, ☃, this.size, !this.ignoreEntities, Blocks.STRUCTURE_VOID);
         ☃xxxx.setAuthor(this.author);
         return !☃ || ☃xxx.writeTemplate(☃xx, new ResourceLocation(this.name));
      } else {
         return false;
      }
   }

   public boolean load() {
      return this.load(true);
   }

   public boolean load(boolean var1) {
      if (this.mode == TileEntityStructure.Mode.LOAD && !this.world.isRemote && !StringUtils.isNullOrEmpty(this.name)) {
         BlockPos ☃ = this.getPos();
         BlockPos ☃x = ☃.add(this.position);
         WorldServer ☃xx = (WorldServer)this.world;
         MinecraftServer ☃xxx = this.world.getMinecraftServer();
         TemplateManager ☃xxxx = ☃xx.getStructureTemplateManager();
         Template ☃xxxxx = ☃xxxx.get(☃xxx, new ResourceLocation(this.name));
         if (☃xxxxx == null) {
            return false;
         } else {
            if (!StringUtils.isNullOrEmpty(☃xxxxx.getAuthor())) {
               this.author = ☃xxxxx.getAuthor();
            }

            BlockPos ☃xxxxxx = ☃xxxxx.getSize();
            boolean ☃xxxxxxx = this.size.equals(☃xxxxxx);
            if (!☃xxxxxxx) {
               this.size = ☃xxxxxx;
               this.markDirty();
               IBlockState ☃xxxxxxxx = this.world.getBlockState(☃);
               this.world.notifyBlockUpdate(☃, ☃xxxxxxxx, ☃xxxxxxxx, 3);
            }

            if (☃ && !☃xxxxxxx) {
               return false;
            } else {
               PlacementSettings ☃xxxxxxxx = new PlacementSettings()
                  .setMirror(this.mirror)
                  .setRotation(this.rotation)
                  .setIgnoreEntities(this.ignoreEntities)
                  .setChunk(null)
                  .setReplacedBlock(null)
                  .setIgnoreStructureBlock(false);
               if (this.integrity < 1.0F) {
                  ☃xxxxxxxx.setIntegrity(MathHelper.clamp(this.integrity, 0.0F, 1.0F)).setSeed(this.seed);
               }

               ☃xxxxx.addBlocksToWorldChunk(this.world, ☃x, ☃xxxxxxxx);
               return true;
            }
         }
      } else {
         return false;
      }
   }

   public void unloadStructure() {
      WorldServer ☃ = (WorldServer)this.world;
      TemplateManager ☃x = ☃.getStructureTemplateManager();
      ☃x.remove(new ResourceLocation(this.name));
   }

   public boolean isStructureLoadable() {
      if (this.mode == TileEntityStructure.Mode.LOAD && !this.world.isRemote) {
         WorldServer ☃ = (WorldServer)this.world;
         MinecraftServer ☃x = this.world.getMinecraftServer();
         TemplateManager ☃xx = ☃.getStructureTemplateManager();
         return ☃xx.get(☃x, new ResourceLocation(this.name)) != null;
      } else {
         return false;
      }
   }

   public boolean isPowered() {
      return this.powered;
   }

   public void setPowered(boolean var1) {
      this.powered = ☃;
   }

   public boolean showsAir() {
      return this.showAir;
   }

   public void setShowAir(boolean var1) {
      this.showAir = ☃;
   }

   public boolean showsBoundingBox() {
      return this.showBoundingBox;
   }

   public void setShowBoundingBox(boolean var1) {
      this.showBoundingBox = ☃;
   }

   @Nullable
   @Override
   public ITextComponent getDisplayName() {
      return new TextComponentTranslation("structure_block.hover." + this.mode.modeName, this.mode == TileEntityStructure.Mode.DATA ? this.metadata : this.name);
   }

   public static enum Mode implements IStringSerializable {
      SAVE("save", 0),
      LOAD("load", 1),
      CORNER("corner", 2),
      DATA("data", 3);

      private static final TileEntityStructure.Mode[] MODES = new TileEntityStructure.Mode[values().length];
      private final String modeName;
      private final int modeId;

      private Mode(String var3, int var4) {
         this.modeName = ☃;
         this.modeId = ☃;
      }

      @Override
      public String getName() {
         return this.modeName;
      }

      public int getModeId() {
         return this.modeId;
      }

      public static TileEntityStructure.Mode getById(int var0) {
         return ☃ >= 0 && ☃ < MODES.length ? MODES[☃] : MODES[0];
      }

      static {
         for (TileEntityStructure.Mode ☃ : values()) {
            MODES[☃.getModeId()] = ☃;
         }
      }
   }
}
