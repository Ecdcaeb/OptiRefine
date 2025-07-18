package net.minecraft.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagCompound extends NBTBase {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
   private final Map<String, NBTBase> tagMap = Maps.newHashMap();

   @Override
   void write(DataOutput var1) throws IOException {
      for (String ☃ : this.tagMap.keySet()) {
         NBTBase ☃x = this.tagMap.get(☃);
         writeEntry(☃, ☃x, ☃);
      }

      ☃.writeByte(0);
   }

   @Override
   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      ☃.read(384L);
      if (☃ > 512) {
         throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
      } else {
         this.tagMap.clear();

         byte ☃;
         while ((☃ = readType(☃, ☃)) != 0) {
            String ☃x = readKey(☃, ☃);
            ☃.read(224 + 16 * ☃x.length());
            NBTBase ☃xx = readNBT(☃, ☃x, ☃, ☃ + 1, ☃);
            if (this.tagMap.put(☃x, ☃xx) != null) {
               ☃.read(288L);
            }
         }
      }
   }

   public Set<String> getKeySet() {
      return this.tagMap.keySet();
   }

   @Override
   public byte getId() {
      return 10;
   }

   public int getSize() {
      return this.tagMap.size();
   }

   public void setTag(String var1, NBTBase var2) {
      this.tagMap.put(☃, ☃);
   }

   public void setByte(String var1, byte var2) {
      this.tagMap.put(☃, new NBTTagByte(☃));
   }

   public void setShort(String var1, short var2) {
      this.tagMap.put(☃, new NBTTagShort(☃));
   }

   public void setInteger(String var1, int var2) {
      this.tagMap.put(☃, new NBTTagInt(☃));
   }

   public void setLong(String var1, long var2) {
      this.tagMap.put(☃, new NBTTagLong(☃));
   }

   public void setUniqueId(String var1, UUID var2) {
      this.setLong(☃ + "Most", ☃.getMostSignificantBits());
      this.setLong(☃ + "Least", ☃.getLeastSignificantBits());
   }

   @Nullable
   public UUID getUniqueId(String var1) {
      return new UUID(this.getLong(☃ + "Most"), this.getLong(☃ + "Least"));
   }

   public boolean hasUniqueId(String var1) {
      return this.hasKey(☃ + "Most", 99) && this.hasKey(☃ + "Least", 99);
   }

   public void setFloat(String var1, float var2) {
      this.tagMap.put(☃, new NBTTagFloat(☃));
   }

   public void setDouble(String var1, double var2) {
      this.tagMap.put(☃, new NBTTagDouble(☃));
   }

   public void setString(String var1, String var2) {
      this.tagMap.put(☃, new NBTTagString(☃));
   }

   public void setByteArray(String var1, byte[] var2) {
      this.tagMap.put(☃, new NBTTagByteArray(☃));
   }

   public void setIntArray(String var1, int[] var2) {
      this.tagMap.put(☃, new NBTTagIntArray(☃));
   }

   public void setBoolean(String var1, boolean var2) {
      this.setByte(☃, (byte)(☃ ? 1 : 0));
   }

   public NBTBase getTag(String var1) {
      return this.tagMap.get(☃);
   }

   public byte getTagId(String var1) {
      NBTBase ☃ = this.tagMap.get(☃);
      return ☃ == null ? 0 : ☃.getId();
   }

   public boolean hasKey(String var1) {
      return this.tagMap.containsKey(☃);
   }

   public boolean hasKey(String var1, int var2) {
      int ☃ = this.getTagId(☃);
      if (☃ == ☃) {
         return true;
      } else {
         return ☃ != 99 ? false : ☃ == 1 || ☃ == 2 || ☃ == 3 || ☃ == 4 || ☃ == 5 || ☃ == 6;
      }
   }

   public byte getByte(String var1) {
      try {
         if (this.hasKey(☃, 99)) {
            return ((NBTPrimitive)this.tagMap.get(☃)).getByte();
         }
      } catch (ClassCastException var3) {
      }

      return 0;
   }

   public short getShort(String var1) {
      try {
         if (this.hasKey(☃, 99)) {
            return ((NBTPrimitive)this.tagMap.get(☃)).getShort();
         }
      } catch (ClassCastException var3) {
      }

      return 0;
   }

   public int getInteger(String var1) {
      try {
         if (this.hasKey(☃, 99)) {
            return ((NBTPrimitive)this.tagMap.get(☃)).getInt();
         }
      } catch (ClassCastException var3) {
      }

      return 0;
   }

   public long getLong(String var1) {
      try {
         if (this.hasKey(☃, 99)) {
            return ((NBTPrimitive)this.tagMap.get(☃)).getLong();
         }
      } catch (ClassCastException var3) {
      }

      return 0L;
   }

   public float getFloat(String var1) {
      try {
         if (this.hasKey(☃, 99)) {
            return ((NBTPrimitive)this.tagMap.get(☃)).getFloat();
         }
      } catch (ClassCastException var3) {
      }

      return 0.0F;
   }

   public double getDouble(String var1) {
      try {
         if (this.hasKey(☃, 99)) {
            return ((NBTPrimitive)this.tagMap.get(☃)).getDouble();
         }
      } catch (ClassCastException var3) {
      }

      return 0.0;
   }

   public String getString(String var1) {
      try {
         if (this.hasKey(☃, 8)) {
            return this.tagMap.get(☃).getString();
         }
      } catch (ClassCastException var3) {
      }

      return "";
   }

   public byte[] getByteArray(String var1) {
      try {
         if (this.hasKey(☃, 7)) {
            return ((NBTTagByteArray)this.tagMap.get(☃)).getByteArray();
         }
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(☃, 7, var3));
      }

      return new byte[0];
   }

   public int[] getIntArray(String var1) {
      try {
         if (this.hasKey(☃, 11)) {
            return ((NBTTagIntArray)this.tagMap.get(☃)).getIntArray();
         }
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(☃, 11, var3));
      }

      return new int[0];
   }

   public NBTTagCompound getCompoundTag(String var1) {
      try {
         if (this.hasKey(☃, 10)) {
            return (NBTTagCompound)this.tagMap.get(☃);
         }
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(☃, 10, var3));
      }

      return new NBTTagCompound();
   }

   public NBTTagList getTagList(String var1, int var2) {
      try {
         if (this.getTagId(☃) == 9) {
            NBTTagList ☃ = (NBTTagList)this.tagMap.get(☃);
            if (!☃.isEmpty() && ☃.getTagType() != ☃) {
               return new NBTTagList();
            }

            return ☃;
         }
      } catch (ClassCastException var4) {
         throw new ReportedException(this.createCrashReport(☃, 9, var4));
      }

      return new NBTTagList();
   }

   public boolean getBoolean(String var1) {
      return this.getByte(☃) != 0;
   }

   public void removeTag(String var1) {
      this.tagMap.remove(☃);
   }

   @Override
   public String toString() {
      StringBuilder ☃ = new StringBuilder("{");
      Collection<String> ☃x = this.tagMap.keySet();
      if (LOGGER.isDebugEnabled()) {
         List<String> ☃xx = Lists.newArrayList(this.tagMap.keySet());
         Collections.sort(☃xx);
         ☃x = ☃xx;
      }

      for (String ☃xx : ☃x) {
         if (☃.length() != 1) {
            ☃.append(',');
         }

         ☃.append(handleEscape(☃xx)).append(':').append(this.tagMap.get(☃xx));
      }

      return ☃.append('}').toString();
   }

   @Override
   public boolean isEmpty() {
      return this.tagMap.isEmpty();
   }

   private CrashReport createCrashReport(final String var1, final int var2, ClassCastException var3) {
      CrashReport ☃ = CrashReport.makeCrashReport(☃, "Reading NBT data");
      CrashReportCategory ☃x = ☃.makeCategoryDepth("Corrupt NBT tag", 1);
      ☃x.addDetail("Tag type found", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return NBTBase.NBT_TYPES[NBTTagCompound.this.tagMap.get(☃).getId()];
         }
      });
      ☃x.addDetail("Tag type expected", new ICrashReportDetail<String>() {
         public String call() throws Exception {
            return NBTBase.NBT_TYPES[☃];
         }
      });
      ☃x.addCrashSection("Tag name", ☃);
      return ☃;
   }

   public NBTTagCompound copy() {
      NBTTagCompound ☃ = new NBTTagCompound();

      for (String ☃x : this.tagMap.keySet()) {
         ☃.setTag(☃x, this.tagMap.get(☃x).copy());
      }

      return ☃;
   }

   @Override
   public boolean equals(Object var1) {
      return super.equals(☃) && Objects.equals(this.tagMap.entrySet(), ((NBTTagCompound)☃).tagMap.entrySet());
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ this.tagMap.hashCode();
   }

   private static void writeEntry(String var0, NBTBase var1, DataOutput var2) throws IOException {
      ☃.writeByte(☃.getId());
      if (☃.getId() != 0) {
         ☃.writeUTF(☃);
         ☃.write(☃);
      }
   }

   private static byte readType(DataInput var0, NBTSizeTracker var1) throws IOException {
      return ☃.readByte();
   }

   private static String readKey(DataInput var0, NBTSizeTracker var1) throws IOException {
      return ☃.readUTF();
   }

   static NBTBase readNBT(byte var0, String var1, DataInput var2, int var3, NBTSizeTracker var4) throws IOException {
      NBTBase ☃ = NBTBase.create(☃);

      try {
         ☃.read(☃, ☃, ☃);
         return ☃;
      } catch (IOException var9) {
         CrashReport ☃x = CrashReport.makeCrashReport(var9, "Loading NBT data");
         CrashReportCategory ☃xx = ☃x.makeCategory("NBT Tag");
         ☃xx.addCrashSection("Tag name", ☃);
         ☃xx.addCrashSection("Tag type", ☃);
         throw new ReportedException(☃x);
      }
   }

   public void merge(NBTTagCompound var1) {
      for (String ☃ : ☃.tagMap.keySet()) {
         NBTBase ☃x = ☃.tagMap.get(☃);
         if (☃x.getId() == 10) {
            if (this.hasKey(☃, 10)) {
               NBTTagCompound ☃xx = this.getCompoundTag(☃);
               ☃xx.merge((NBTTagCompound)☃x);
            } else {
               this.setTag(☃, ☃x.copy());
            }
         } else {
            this.setTag(☃, ☃x.copy());
         }
      }
   }

   protected static String handleEscape(String var0) {
      return SIMPLE_VALUE.matcher(☃).matches() ? ☃ : NBTTagString.quoteAndEscape(☃);
   }
}
