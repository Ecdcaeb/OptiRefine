package net.minecraft.nbt;

import com.google.common.collect.Lists;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagList extends NBTBase {
   private static final Logger LOGGER = LogManager.getLogger();
   private List<NBTBase> tagList = Lists.newArrayList();
   private byte tagType = 0;

   @Override
   void write(DataOutput var1) throws IOException {
      if (this.tagList.isEmpty()) {
         this.tagType = 0;
      } else {
         this.tagType = this.tagList.get(0).getId();
      }

      ☃.writeByte(this.tagType);
      ☃.writeInt(this.tagList.size());

      for (int ☃ = 0; ☃ < this.tagList.size(); ☃++) {
         this.tagList.get(☃).write(☃);
      }
   }

   @Override
   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      ☃.read(296L);
      if (☃ > 512) {
         throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
      } else {
         this.tagType = ☃.readByte();
         int ☃ = ☃.readInt();
         if (this.tagType == 0 && ☃ > 0) {
            throw new RuntimeException("Missing type on ListTag");
         } else {
            ☃.read(32L * ☃);
            this.tagList = Lists.newArrayListWithCapacity(☃);

            for (int ☃x = 0; ☃x < ☃; ☃x++) {
               NBTBase ☃xx = NBTBase.create(this.tagType);
               ☃xx.read(☃, ☃ + 1, ☃);
               this.tagList.add(☃xx);
            }
         }
      }
   }

   @Override
   public byte getId() {
      return 9;
   }

   @Override
   public String toString() {
      StringBuilder ☃ = new StringBuilder("[");

      for (int ☃x = 0; ☃x < this.tagList.size(); ☃x++) {
         if (☃x != 0) {
            ☃.append(',');
         }

         ☃.append(this.tagList.get(☃x));
      }

      return ☃.append(']').toString();
   }

   public void appendTag(NBTBase var1) {
      if (☃.getId() == 0) {
         LOGGER.warn("Invalid TagEnd added to ListTag");
      } else {
         if (this.tagType == 0) {
            this.tagType = ☃.getId();
         } else if (this.tagType != ☃.getId()) {
            LOGGER.warn("Adding mismatching tag types to tag list");
            return;
         }

         this.tagList.add(☃);
      }
   }

   public void set(int var1, NBTBase var2) {
      if (☃.getId() == 0) {
         LOGGER.warn("Invalid TagEnd added to ListTag");
      } else if (☃ >= 0 && ☃ < this.tagList.size()) {
         if (this.tagType == 0) {
            this.tagType = ☃.getId();
         } else if (this.tagType != ☃.getId()) {
            LOGGER.warn("Adding mismatching tag types to tag list");
            return;
         }

         this.tagList.set(☃, ☃);
      } else {
         LOGGER.warn("index out of bounds to set tag in tag list");
      }
   }

   public NBTBase removeTag(int var1) {
      return this.tagList.remove(☃);
   }

   @Override
   public boolean isEmpty() {
      return this.tagList.isEmpty();
   }

   public NBTTagCompound getCompoundTagAt(int var1) {
      if (☃ >= 0 && ☃ < this.tagList.size()) {
         NBTBase ☃ = this.tagList.get(☃);
         if (☃.getId() == 10) {
            return (NBTTagCompound)☃;
         }
      }

      return new NBTTagCompound();
   }

   public int getIntAt(int var1) {
      if (☃ >= 0 && ☃ < this.tagList.size()) {
         NBTBase ☃ = this.tagList.get(☃);
         if (☃.getId() == 3) {
            return ((NBTTagInt)☃).getInt();
         }
      }

      return 0;
   }

   public int[] getIntArrayAt(int var1) {
      if (☃ >= 0 && ☃ < this.tagList.size()) {
         NBTBase ☃ = this.tagList.get(☃);
         if (☃.getId() == 11) {
            return ((NBTTagIntArray)☃).getIntArray();
         }
      }

      return new int[0];
   }

   public double getDoubleAt(int var1) {
      if (☃ >= 0 && ☃ < this.tagList.size()) {
         NBTBase ☃ = this.tagList.get(☃);
         if (☃.getId() == 6) {
            return ((NBTTagDouble)☃).getDouble();
         }
      }

      return 0.0;
   }

   public float getFloatAt(int var1) {
      if (☃ >= 0 && ☃ < this.tagList.size()) {
         NBTBase ☃ = this.tagList.get(☃);
         if (☃.getId() == 5) {
            return ((NBTTagFloat)☃).getFloat();
         }
      }

      return 0.0F;
   }

   public String getStringTagAt(int var1) {
      if (☃ >= 0 && ☃ < this.tagList.size()) {
         NBTBase ☃ = this.tagList.get(☃);
         return ☃.getId() == 8 ? ☃.getString() : ☃.toString();
      } else {
         return "";
      }
   }

   public NBTBase get(int var1) {
      return (NBTBase)(☃ >= 0 && ☃ < this.tagList.size() ? this.tagList.get(☃) : new NBTTagEnd());
   }

   public int tagCount() {
      return this.tagList.size();
   }

   public NBTTagList copy() {
      NBTTagList ☃ = new NBTTagList();
      ☃.tagType = this.tagType;

      for (NBTBase ☃x : this.tagList) {
         NBTBase ☃xx = ☃x.copy();
         ☃.tagList.add(☃xx);
      }

      return ☃;
   }

   @Override
   public boolean equals(Object var1) {
      if (!super.equals(☃)) {
         return false;
      } else {
         NBTTagList ☃ = (NBTTagList)☃;
         return this.tagType == ☃.tagType && Objects.equals(this.tagList, ☃.tagList);
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ this.tagList.hashCode();
   }

   public int getTagType() {
      return this.tagType;
   }
}
