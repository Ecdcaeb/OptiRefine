package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public abstract class StructureStart {
   protected List<StructureComponent> components = Lists.newLinkedList();
   protected StructureBoundingBox boundingBox;
   private int chunkPosX;
   private int chunkPosZ;

   public StructureStart() {
   }

   public StructureStart(int var1, int var2) {
      this.chunkPosX = ☃;
      this.chunkPosZ = ☃;
   }

   public StructureBoundingBox getBoundingBox() {
      return this.boundingBox;
   }

   public List<StructureComponent> getComponents() {
      return this.components;
   }

   public void generateStructure(World var1, Random var2, StructureBoundingBox var3) {
      Iterator<StructureComponent> ☃ = this.components.iterator();

      while (☃.hasNext()) {
         StructureComponent ☃x = ☃.next();
         if (☃x.getBoundingBox().intersectsWith(☃) && !☃x.addComponentParts(☃, ☃, ☃)) {
            ☃.remove();
         }
      }
   }

   protected void updateBoundingBox() {
      this.boundingBox = StructureBoundingBox.getNewBoundingBox();

      for (StructureComponent ☃ : this.components) {
         this.boundingBox.expandTo(☃.getBoundingBox());
      }
   }

   public NBTTagCompound writeStructureComponentsToNBT(int var1, int var2) {
      NBTTagCompound ☃ = new NBTTagCompound();
      ☃.setString("id", MapGenStructureIO.getStructureStartName(this));
      ☃.setInteger("ChunkX", ☃);
      ☃.setInteger("ChunkZ", ☃);
      ☃.setTag("BB", this.boundingBox.toNBTTagIntArray());
      NBTTagList ☃x = new NBTTagList();

      for (StructureComponent ☃xx : this.components) {
         ☃x.appendTag(☃xx.createStructureBaseNBT());
      }

      ☃.setTag("Children", ☃x);
      this.writeToNBT(☃);
      return ☃;
   }

   public void writeToNBT(NBTTagCompound var1) {
   }

   public void readStructureComponentsFromNBT(World var1, NBTTagCompound var2) {
      this.chunkPosX = ☃.getInteger("ChunkX");
      this.chunkPosZ = ☃.getInteger("ChunkZ");
      if (☃.hasKey("BB")) {
         this.boundingBox = new StructureBoundingBox(☃.getIntArray("BB"));
      }

      NBTTagList ☃ = ☃.getTagList("Children", 10);

      for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
         this.components.add(MapGenStructureIO.getStructureComponent(☃.getCompoundTagAt(☃x), ☃));
      }

      this.readFromNBT(☃);
   }

   public void readFromNBT(NBTTagCompound var1) {
   }

   protected void markAvailableHeight(World var1, Random var2, int var3) {
      int ☃ = ☃.getSeaLevel() - ☃;
      int ☃x = this.boundingBox.getYSize() + 1;
      if (☃x < ☃) {
         ☃x += ☃.nextInt(☃ - ☃x);
      }

      int ☃xx = ☃x - this.boundingBox.maxY;
      this.boundingBox.offset(0, ☃xx, 0);

      for (StructureComponent ☃xxx : this.components) {
         ☃xxx.offset(0, ☃xx, 0);
      }
   }

   protected void setRandomHeight(World var1, Random var2, int var3, int var4) {
      int ☃ = ☃ - ☃ + 1 - this.boundingBox.getYSize();
      int ☃x;
      if (☃ > 1) {
         ☃x = ☃ + ☃.nextInt(☃);
      } else {
         ☃x = ☃;
      }

      int ☃xx = ☃x - this.boundingBox.minY;
      this.boundingBox.offset(0, ☃xx, 0);

      for (StructureComponent ☃xxx : this.components) {
         ☃xxx.offset(0, ☃xx, 0);
      }
   }

   public boolean isSizeableStructure() {
      return true;
   }

   public boolean isValidForPostProcess(ChunkPos var1) {
      return true;
   }

   public void notifyPostProcessAt(ChunkPos var1) {
   }

   public int getChunkPosX() {
      return this.chunkPosX;
   }

   public int getChunkPosZ() {
      return this.chunkPosZ;
   }
}
