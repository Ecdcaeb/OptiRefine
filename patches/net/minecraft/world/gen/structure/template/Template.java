package net.minecraft.world.gen.structure.template;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.Mirror;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.Rotation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class Template {
   private final List<Template.BlockInfo> blocks = Lists.newArrayList();
   private final List<Template.EntityInfo> entities = Lists.newArrayList();
   private BlockPos size = BlockPos.ORIGIN;
   private String author = "?";

   public BlockPos getSize() {
      return this.size;
   }

   public void setAuthor(String var1) {
      this.author = ☃;
   }

   public String getAuthor() {
      return this.author;
   }

   public void takeBlocksFromWorld(World var1, BlockPos var2, BlockPos var3, boolean var4, @Nullable Block var5) {
      if (☃.getX() >= 1 && ☃.getY() >= 1 && ☃.getZ() >= 1) {
         BlockPos ☃ = ☃.add(☃).add(-1, -1, -1);
         List<Template.BlockInfo> ☃x = Lists.newArrayList();
         List<Template.BlockInfo> ☃xx = Lists.newArrayList();
         List<Template.BlockInfo> ☃xxx = Lists.newArrayList();
         BlockPos ☃xxxx = new BlockPos(Math.min(☃.getX(), ☃.getX()), Math.min(☃.getY(), ☃.getY()), Math.min(☃.getZ(), ☃.getZ()));
         BlockPos ☃xxxxx = new BlockPos(Math.max(☃.getX(), ☃.getX()), Math.max(☃.getY(), ☃.getY()), Math.max(☃.getZ(), ☃.getZ()));
         this.size = ☃;

         for (BlockPos.MutableBlockPos ☃xxxxxx : BlockPos.getAllInBoxMutable(☃xxxx, ☃xxxxx)) {
            BlockPos ☃xxxxxxx = ☃xxxxxx.subtract(☃xxxx);
            IBlockState ☃xxxxxxxx = ☃.getBlockState(☃xxxxxx);
            if (☃ == null || ☃ != ☃xxxxxxxx.getBlock()) {
               TileEntity ☃xxxxxxxxx = ☃.getTileEntity(☃xxxxxx);
               if (☃xxxxxxxxx != null) {
                  NBTTagCompound ☃xxxxxxxxxx = ☃xxxxxxxxx.writeToNBT(new NBTTagCompound());
                  ☃xxxxxxxxxx.removeTag("x");
                  ☃xxxxxxxxxx.removeTag("y");
                  ☃xxxxxxxxxx.removeTag("z");
                  ☃xx.add(new Template.BlockInfo(☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxxx));
               } else if (!☃xxxxxxxx.isFullBlock() && !☃xxxxxxxx.isFullCube()) {
                  ☃xxx.add(new Template.BlockInfo(☃xxxxxxx, ☃xxxxxxxx, null));
               } else {
                  ☃x.add(new Template.BlockInfo(☃xxxxxxx, ☃xxxxxxxx, null));
               }
            }
         }

         this.blocks.clear();
         this.blocks.addAll(☃x);
         this.blocks.addAll(☃xx);
         this.blocks.addAll(☃xxx);
         if (☃) {
            this.takeEntitiesFromWorld(☃, ☃xxxx, ☃xxxxx.add(1, 1, 1));
         } else {
            this.entities.clear();
         }
      }
   }

   private void takeEntitiesFromWorld(World var1, BlockPos var2, BlockPos var3) {
      List<Entity> ☃ = ☃.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(☃, ☃), new Predicate<Entity>() {
         public boolean apply(@Nullable Entity var1) {
            return !(☃ instanceof EntityPlayer);
         }
      });
      this.entities.clear();

      for (Entity ☃x : ☃) {
         Vec3d ☃xx = new Vec3d(☃x.posX - ☃.getX(), ☃x.posY - ☃.getY(), ☃x.posZ - ☃.getZ());
         NBTTagCompound ☃xxx = new NBTTagCompound();
         ☃x.writeToNBTOptional(☃xxx);
         BlockPos ☃xxxx;
         if (☃x instanceof EntityPainting) {
            ☃xxxx = ((EntityPainting)☃x).getHangingPosition().subtract(☃);
         } else {
            ☃xxxx = new BlockPos(☃xx);
         }

         this.entities.add(new Template.EntityInfo(☃xx, ☃xxxx, ☃xxx));
      }
   }

   public Map<BlockPos, String> getDataBlocks(BlockPos var1, PlacementSettings var2) {
      Map<BlockPos, String> ☃ = Maps.newHashMap();
      StructureBoundingBox ☃x = ☃.getBoundingBox();

      for (Template.BlockInfo ☃xx : this.blocks) {
         BlockPos ☃xxx = transformedBlockPos(☃, ☃xx.pos).add(☃);
         if (☃x == null || ☃x.isVecInside(☃xxx)) {
            IBlockState ☃xxxx = ☃xx.blockState;
            if (☃xxxx.getBlock() == Blocks.STRUCTURE_BLOCK && ☃xx.tileentityData != null) {
               TileEntityStructure.Mode ☃xxxxx = TileEntityStructure.Mode.valueOf(☃xx.tileentityData.getString("mode"));
               if (☃xxxxx == TileEntityStructure.Mode.DATA) {
                  ☃.put(☃xxx, ☃xx.tileentityData.getString("metadata"));
               }
            }
         }
      }

      return ☃;
   }

   public BlockPos calculateConnectedPos(PlacementSettings var1, BlockPos var2, PlacementSettings var3, BlockPos var4) {
      BlockPos ☃ = transformedBlockPos(☃, ☃);
      BlockPos ☃x = transformedBlockPos(☃, ☃);
      return ☃.subtract(☃x);
   }

   public static BlockPos transformedBlockPos(PlacementSettings var0, BlockPos var1) {
      return transformedBlockPos(☃, ☃.getMirror(), ☃.getRotation());
   }

   public void addBlocksToWorldChunk(World var1, BlockPos var2, PlacementSettings var3) {
      ☃.setBoundingBoxFromChunk();
      this.addBlocksToWorld(☃, ☃, ☃);
   }

   public void addBlocksToWorld(World var1, BlockPos var2, PlacementSettings var3) {
      this.addBlocksToWorld(☃, ☃, new BlockRotationProcessor(☃, ☃), ☃, 2);
   }

   public void addBlocksToWorld(World var1, BlockPos var2, PlacementSettings var3, int var4) {
      this.addBlocksToWorld(☃, ☃, new BlockRotationProcessor(☃, ☃), ☃, ☃);
   }

   public void addBlocksToWorld(World var1, BlockPos var2, @Nullable ITemplateProcessor var3, PlacementSettings var4, int var5) {
      if ((!this.blocks.isEmpty() || !☃.getIgnoreEntities() && !this.entities.isEmpty())
         && this.size.getX() >= 1
         && this.size.getY() >= 1
         && this.size.getZ() >= 1) {
         Block ☃ = ☃.getReplacedBlock();
         StructureBoundingBox ☃x = ☃.getBoundingBox();

         for (Template.BlockInfo ☃xx : this.blocks) {
            BlockPos ☃xxx = transformedBlockPos(☃, ☃xx.pos).add(☃);
            Template.BlockInfo ☃xxxx = ☃ != null ? ☃.processBlock(☃, ☃xxx, ☃xx) : ☃xx;
            if (☃xxxx != null) {
               Block ☃xxxxx = ☃xxxx.blockState.getBlock();
               if ((☃ == null || ☃ != ☃xxxxx) && (!☃.getIgnoreStructureBlock() || ☃xxxxx != Blocks.STRUCTURE_BLOCK) && (☃x == null || ☃x.isVecInside(☃xxx))) {
                  IBlockState ☃xxxxxx = ☃xxxx.blockState.withMirror(☃.getMirror());
                  IBlockState ☃xxxxxxx = ☃xxxxxx.withRotation(☃.getRotation());
                  if (☃xxxx.tileentityData != null) {
                     TileEntity ☃xxxxxxxx = ☃.getTileEntity(☃xxx);
                     if (☃xxxxxxxx != null) {
                        if (☃xxxxxxxx instanceof IInventory) {
                           ((IInventory)☃xxxxxxxx).clear();
                        }

                        ☃.setBlockState(☃xxx, Blocks.BARRIER.getDefaultState(), 4);
                     }
                  }

                  if (☃.setBlockState(☃xxx, ☃xxxxxxx, ☃) && ☃xxxx.tileentityData != null) {
                     TileEntity ☃xxxxxxxx = ☃.getTileEntity(☃xxx);
                     if (☃xxxxxxxx != null) {
                        ☃xxxx.tileentityData.setInteger("x", ☃xxx.getX());
                        ☃xxxx.tileentityData.setInteger("y", ☃xxx.getY());
                        ☃xxxx.tileentityData.setInteger("z", ☃xxx.getZ());
                        ☃xxxxxxxx.readFromNBT(☃xxxx.tileentityData);
                        ☃xxxxxxxx.mirror(☃.getMirror());
                        ☃xxxxxxxx.rotate(☃.getRotation());
                     }
                  }
               }
            }
         }

         for (Template.BlockInfo ☃xxx : this.blocks) {
            if (☃ == null || ☃ != ☃xxx.blockState.getBlock()) {
               BlockPos ☃xxxx = transformedBlockPos(☃, ☃xxx.pos).add(☃);
               if (☃x == null || ☃x.isVecInside(☃xxxx)) {
                  ☃.notifyNeighborsRespectDebug(☃xxxx, ☃xxx.blockState.getBlock(), false);
                  if (☃xxx.tileentityData != null) {
                     TileEntity ☃xxxxx = ☃.getTileEntity(☃xxxx);
                     if (☃xxxxx != null) {
                        ☃xxxxx.markDirty();
                     }
                  }
               }
            }
         }

         if (!☃.getIgnoreEntities()) {
            this.addEntitiesToWorld(☃, ☃, ☃.getMirror(), ☃.getRotation(), ☃x);
         }
      }
   }

   private void addEntitiesToWorld(World var1, BlockPos var2, Mirror var3, Rotation var4, @Nullable StructureBoundingBox var5) {
      for (Template.EntityInfo ☃ : this.entities) {
         BlockPos ☃x = transformedBlockPos(☃.blockPos, ☃, ☃).add(☃);
         if (☃ == null || ☃.isVecInside(☃x)) {
            NBTTagCompound ☃xx = ☃.entityData;
            Vec3d ☃xxx = transformedVec3d(☃.pos, ☃, ☃);
            Vec3d ☃xxxx = ☃xxx.add(☃.getX(), ☃.getY(), ☃.getZ());
            NBTTagList ☃xxxxx = new NBTTagList();
            ☃xxxxx.appendTag(new NBTTagDouble(☃xxxx.x));
            ☃xxxxx.appendTag(new NBTTagDouble(☃xxxx.y));
            ☃xxxxx.appendTag(new NBTTagDouble(☃xxxx.z));
            ☃xx.setTag("Pos", ☃xxxxx);
            ☃xx.setUniqueId("UUID", UUID.randomUUID());

            Entity ☃xxxxxx;
            try {
               ☃xxxxxx = EntityList.createEntityFromNBT(☃xx, ☃);
            } catch (Exception var15) {
               ☃xxxxxx = null;
            }

            if (☃xxxxxx != null) {
               float ☃xxxxxxx = ☃xxxxxx.getMirroredYaw(☃);
               ☃xxxxxxx += ☃xxxxxx.rotationYaw - ☃xxxxxx.getRotatedYaw(☃);
               ☃xxxxxx.setLocationAndAngles(☃xxxx.x, ☃xxxx.y, ☃xxxx.z, ☃xxxxxxx, ☃xxxxxx.rotationPitch);
               ☃.spawnEntity(☃xxxxxx);
            }
         }
      }
   }

   public BlockPos transformedSize(Rotation var1) {
      switch (☃) {
         case COUNTERCLOCKWISE_90:
         case CLOCKWISE_90:
            return new BlockPos(this.size.getZ(), this.size.getY(), this.size.getX());
         default:
            return this.size;
      }
   }

   private static BlockPos transformedBlockPos(BlockPos var0, Mirror var1, Rotation var2) {
      int ☃ = ☃.getX();
      int ☃x = ☃.getY();
      int ☃xx = ☃.getZ();
      boolean ☃xxx = true;
      switch (☃) {
         case LEFT_RIGHT:
            ☃xx = -☃xx;
            break;
         case FRONT_BACK:
            ☃ = -☃;
            break;
         default:
            ☃xxx = false;
      }

      switch (☃) {
         case COUNTERCLOCKWISE_90:
            return new BlockPos(☃xx, ☃x, -☃);
         case CLOCKWISE_90:
            return new BlockPos(-☃xx, ☃x, ☃);
         case CLOCKWISE_180:
            return new BlockPos(-☃, ☃x, -☃xx);
         default:
            return ☃xxx ? new BlockPos(☃, ☃x, ☃xx) : ☃;
      }
   }

   private static Vec3d transformedVec3d(Vec3d var0, Mirror var1, Rotation var2) {
      double ☃ = ☃.x;
      double ☃x = ☃.y;
      double ☃xx = ☃.z;
      boolean ☃xxx = true;
      switch (☃) {
         case LEFT_RIGHT:
            ☃xx = 1.0 - ☃xx;
            break;
         case FRONT_BACK:
            ☃ = 1.0 - ☃;
            break;
         default:
            ☃xxx = false;
      }

      switch (☃) {
         case COUNTERCLOCKWISE_90:
            return new Vec3d(☃xx, ☃x, 1.0 - ☃);
         case CLOCKWISE_90:
            return new Vec3d(1.0 - ☃xx, ☃x, ☃);
         case CLOCKWISE_180:
            return new Vec3d(1.0 - ☃, ☃x, 1.0 - ☃xx);
         default:
            return ☃xxx ? new Vec3d(☃, ☃x, ☃xx) : ☃;
      }
   }

   public BlockPos getZeroPositionWithTransform(BlockPos var1, Mirror var2, Rotation var3) {
      return getZeroPositionWithTransform(☃, ☃, ☃, this.getSize().getX(), this.getSize().getZ());
   }

   public static BlockPos getZeroPositionWithTransform(BlockPos var0, Mirror var1, Rotation var2, int var3, int var4) {
      ☃--;
      ☃--;
      int ☃ = ☃ == Mirror.FRONT_BACK ? ☃ : 0;
      int ☃x = ☃ == Mirror.LEFT_RIGHT ? ☃ : 0;
      BlockPos ☃xx = ☃;
      switch (☃) {
         case COUNTERCLOCKWISE_90:
            ☃xx = ☃.add(☃x, 0, ☃ - ☃);
            break;
         case CLOCKWISE_90:
            ☃xx = ☃.add(☃ - ☃x, 0, ☃);
            break;
         case CLOCKWISE_180:
            ☃xx = ☃.add(☃ - ☃, 0, ☃ - ☃x);
            break;
         case NONE:
            ☃xx = ☃.add(☃, 0, ☃x);
      }

      return ☃xx;
   }

   public static void registerFixes(DataFixer var0) {
      ☃.registerWalker(FixTypes.STRUCTURE, new IDataWalker() {
         @Override
         public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3) {
            if (☃.hasKey("entities", 9)) {
               NBTTagList ☃ = ☃.getTagList("entities", 10);

               for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
                  NBTTagCompound ☃xx = (NBTTagCompound)☃.get(☃x);
                  if (☃xx.hasKey("nbt", 10)) {
                     ☃xx.setTag("nbt", ☃.process(FixTypes.ENTITY, ☃xx.getCompoundTag("nbt"), ☃));
                  }
               }
            }

            if (☃.hasKey("blocks", 9)) {
               NBTTagList ☃ = ☃.getTagList("blocks", 10);

               for (int ☃xx = 0; ☃xx < ☃.tagCount(); ☃xx++) {
                  NBTTagCompound ☃xxx = (NBTTagCompound)☃.get(☃xx);
                  if (☃xxx.hasKey("nbt", 10)) {
                     ☃xxx.setTag("nbt", ☃.process(FixTypes.BLOCK_ENTITY, ☃xxx.getCompoundTag("nbt"), ☃));
                  }
               }
            }

            return ☃;
         }
      });
   }

   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      Template.BasicPalette ☃ = new Template.BasicPalette();
      NBTTagList ☃x = new NBTTagList();

      for (Template.BlockInfo ☃xx : this.blocks) {
         NBTTagCompound ☃xxx = new NBTTagCompound();
         ☃xxx.setTag("pos", this.writeInts(☃xx.pos.getX(), ☃xx.pos.getY(), ☃xx.pos.getZ()));
         ☃xxx.setInteger("state", ☃.idFor(☃xx.blockState));
         if (☃xx.tileentityData != null) {
            ☃xxx.setTag("nbt", ☃xx.tileentityData);
         }

         ☃x.appendTag(☃xxx);
      }

      NBTTagList ☃xx = new NBTTagList();

      for (Template.EntityInfo ☃xxx : this.entities) {
         NBTTagCompound ☃xxxx = new NBTTagCompound();
         ☃xxxx.setTag("pos", this.writeDoubles(☃xxx.pos.x, ☃xxx.pos.y, ☃xxx.pos.z));
         ☃xxxx.setTag("blockPos", this.writeInts(☃xxx.blockPos.getX(), ☃xxx.blockPos.getY(), ☃xxx.blockPos.getZ()));
         if (☃xxx.entityData != null) {
            ☃xxxx.setTag("nbt", ☃xxx.entityData);
         }

         ☃xx.appendTag(☃xxxx);
      }

      NBTTagList ☃xxx = new NBTTagList();

      for (IBlockState ☃xxxx : ☃) {
         ☃xxx.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), ☃xxxx));
      }

      ☃.setTag("palette", ☃xxx);
      ☃.setTag("blocks", ☃x);
      ☃.setTag("entities", ☃xx);
      ☃.setTag("size", this.writeInts(this.size.getX(), this.size.getY(), this.size.getZ()));
      ☃.setString("author", this.author);
      ☃.setInteger("DataVersion", 1343);
      return ☃;
   }

   public void read(NBTTagCompound var1) {
      this.blocks.clear();
      this.entities.clear();
      NBTTagList ☃ = ☃.getTagList("size", 3);
      this.size = new BlockPos(☃.getIntAt(0), ☃.getIntAt(1), ☃.getIntAt(2));
      this.author = ☃.getString("author");
      Template.BasicPalette ☃x = new Template.BasicPalette();
      NBTTagList ☃xx = ☃.getTagList("palette", 10);

      for (int ☃xxx = 0; ☃xxx < ☃xx.tagCount(); ☃xxx++) {
         ☃x.addMapping(NBTUtil.readBlockState(☃xx.getCompoundTagAt(☃xxx)), ☃xxx);
      }

      NBTTagList ☃xxx = ☃.getTagList("blocks", 10);

      for (int ☃xxxx = 0; ☃xxxx < ☃xxx.tagCount(); ☃xxxx++) {
         NBTTagCompound ☃xxxxx = ☃xxx.getCompoundTagAt(☃xxxx);
         NBTTagList ☃xxxxxx = ☃xxxxx.getTagList("pos", 3);
         BlockPos ☃xxxxxxx = new BlockPos(☃xxxxxx.getIntAt(0), ☃xxxxxx.getIntAt(1), ☃xxxxxx.getIntAt(2));
         IBlockState ☃xxxxxxxx = ☃x.stateFor(☃xxxxx.getInteger("state"));
         NBTTagCompound ☃xxxxxxxxx;
         if (☃xxxxx.hasKey("nbt")) {
            ☃xxxxxxxxx = ☃xxxxx.getCompoundTag("nbt");
         } else {
            ☃xxxxxxxxx = null;
         }

         this.blocks.add(new Template.BlockInfo(☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx));
      }

      NBTTagList ☃xxxx = ☃.getTagList("entities", 10);

      for (int ☃xxxxx = 0; ☃xxxxx < ☃xxxx.tagCount(); ☃xxxxx++) {
         NBTTagCompound ☃xxxxxx = ☃xxxx.getCompoundTagAt(☃xxxxx);
         NBTTagList ☃xxxxxxx = ☃xxxxxx.getTagList("pos", 6);
         Vec3d ☃xxxxxxxx = new Vec3d(☃xxxxxxx.getDoubleAt(0), ☃xxxxxxx.getDoubleAt(1), ☃xxxxxxx.getDoubleAt(2));
         NBTTagList ☃xxxxxxxxx = ☃xxxxxx.getTagList("blockPos", 3);
         BlockPos ☃xxxxxxxxxx = new BlockPos(☃xxxxxxxxx.getIntAt(0), ☃xxxxxxxxx.getIntAt(1), ☃xxxxxxxxx.getIntAt(2));
         if (☃xxxxxx.hasKey("nbt")) {
            NBTTagCompound ☃xxxxxxxxxxx = ☃xxxxxx.getCompoundTag("nbt");
            this.entities.add(new Template.EntityInfo(☃xxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx));
         }
      }
   }

   private NBTTagList writeInts(int... var1) {
      NBTTagList ☃ = new NBTTagList();

      for (int ☃x : ☃) {
         ☃.appendTag(new NBTTagInt(☃x));
      }

      return ☃;
   }

   private NBTTagList writeDoubles(double... var1) {
      NBTTagList ☃ = new NBTTagList();

      for (double ☃x : ☃) {
         ☃.appendTag(new NBTTagDouble(☃x));
      }

      return ☃;
   }

   static class BasicPalette implements Iterable<IBlockState> {
      public static final IBlockState DEFAULT_BLOCK_STATE = Blocks.AIR.getDefaultState();
      final ObjectIntIdentityMap<IBlockState> ids = new ObjectIntIdentityMap<>(16);
      private int lastId;

      private BasicPalette() {
      }

      public int idFor(IBlockState var1) {
         int ☃ = this.ids.get(☃);
         if (☃ == -1) {
            ☃ = this.lastId++;
            this.ids.put(☃, ☃);
         }

         return ☃;
      }

      @Nullable
      public IBlockState stateFor(int var1) {
         IBlockState ☃ = this.ids.getByValue(☃);
         return ☃ == null ? DEFAULT_BLOCK_STATE : ☃;
      }

      @Override
      public Iterator<IBlockState> iterator() {
         return this.ids.iterator();
      }

      public void addMapping(IBlockState var1, int var2) {
         this.ids.put(☃, ☃);
      }
   }

   public static class BlockInfo {
      public final BlockPos pos;
      public final IBlockState blockState;
      public final NBTTagCompound tileentityData;

      public BlockInfo(BlockPos var1, IBlockState var2, @Nullable NBTTagCompound var3) {
         this.pos = ☃;
         this.blockState = ☃;
         this.tileentityData = ☃;
      }
   }

   public static class EntityInfo {
      public final Vec3d pos;
      public final BlockPos blockPos;
      public final NBTTagCompound entityData;

      public EntityInfo(Vec3d var1, BlockPos var2, NBTTagCompound var3) {
         this.pos = ☃;
         this.blockPos = ☃;
         this.entityData = ☃;
      }
   }
}
